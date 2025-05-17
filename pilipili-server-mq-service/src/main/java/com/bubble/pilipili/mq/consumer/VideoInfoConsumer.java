/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.feign.api.VideoFeignAPI;
import com.bubble.pilipili.feign.pojo.req.UpdateVideoInfoReq;
import com.bubble.pilipili.mq.entity.VideoInfoMessage;
import com.bubble.pilipili.mq.producer.MessageProducer;
import com.bubble.pilipili.mq.util.MQRedisHelper;
import com.bubble.pilipili.mq.util.MessageHelper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.cache.NullValue;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * 视频信息更新消息队列消费者，这是个延迟队列
 * @author Bubble
 * @date 2025.03.26 17:13
 */
@Slf4j
@Service
@RabbitListener(queues = "queue.pilipili.delay")
public class VideoInfoConsumer {

    private final MessageHelper messageHelper;
    private final MQRedisHelper mqRedisHelper;
    private final VideoFeignAPI videoFeignAPI;
    private final MessageProducer messageProducer;

    public VideoInfoConsumer(MessageHelper messageHelper, MQRedisHelper mqRedisHelper, VideoFeignAPI videoFeignAPI, MessageProducer messageProducer) {
        this.messageHelper = messageHelper;
        this.mqRedisHelper = mqRedisHelper;
        this.videoFeignAPI = videoFeignAPI;
        this.messageProducer = messageProducer;
    }

    @RabbitHandler
    public void receiveMessage(Object message, Channel channel) throws IOException {
        Message msg = (Message) message;
        long deliveryTag = msg.getMessageProperties().getDeliveryTag();

        Object messageBody = messageHelper.getMessageBody(msg);
        if (messageBody instanceof VideoInfoMessage) {
            VideoInfoMessage body = (VideoInfoMessage) messageBody;
            String taskId = body.getTaskId();

            Integer vid = mqRedisHelper.getVideoTaskVid(taskId);
            // 如果用户还没保存视频，这里获取不到taskId对应的vid，等待一段时间重试
            if (vid == null) {
                // new: 用户还没保存，先存任务ID
                log.debug("用户还没保存，先存任务ID: {}", taskId);
                mqRedisHelper.saveVideoTaskIdWithRLockTask(
                        taskId, null,
                        (cache) -> {
                            if (cache instanceof NullValue) {
                                return;
                            }
                            Integer vidCache = (Integer) cache;
                            updateVideoInfo(vidCache, body, msg);
                        }
                );
//                channel.basicNack(deliveryTag, false, true);
                // 由Spring AMQP在消费者实现的重试，只要抛出异常就可以触发，消息不会重新入队
//                throw new MQConsumerException("找不到任务ID对应的vid，进行重试");
                // 通过延迟队列实现的重试，会将消息重新入队，并配合延迟作为间隔，同时记录重试次数，在达到最大次数后停止重试
//                log.warn("找不到任务ID[{}]对应的vid，进行重试", taskId);
//                retryMessage(msg, body);
//                // 重新入队的是新的消息，所以要将旧消息确认
//                channel.basicAck(deliveryTag, false);
            } else {
                updateVideoInfo(vid, body, msg);
            }


        } else {
            log.warn("不支持的消息类型: {}", msg);
        }

        channel.basicAck(deliveryTag, false);
    }

    /**
     * 更新视频信息
     * @param vid
     * @param body
     */
    private void updateVideoInfo(Integer vid, VideoInfoMessage body, Message msg) {
        // 更新视频信息
        UpdateVideoInfoReq req = new UpdateVideoInfoReq();
        req.setVid(vid);
        if (body.getDuration() != null) {
            req.setDuration(body.getDuration().intValue());
        }
        if (body.getContentUrl() != null) {
            req.setContentUrl(body.getContentUrl());
        }
        if (body.getCoverUrl() != null) {
            req.setCoverUrl(body.getCoverUrl());
        }
        if (body.getStatus() != null) {
            req.setStatus(body.getStatus());
        }
        try {
            SimpleResponse<String> response = videoFeignAPI.updateFromMQ(req);
            if (!response.isSuccess()) {
                log.warn("视频信息更新失败: {}", response.getMsg());
                retryMessage(msg, body);
//                throw new MQConsumerException("找不到任务ID对应的vid，进行重试");
                // 重新入队，会立即重试，跟配置文件里的重试配置没有关系
//                channel.basicNack(deliveryTag, false, true);
//                    channel.basicReject(deliveryTag, false);
            } else {
                log.debug("视频信息更新成功: {}", req);
            }
        } catch (Exception e) {
            log.warn("视频信息更新失败: {}", e.getMessage());
            retryMessage(msg, body);
        }
    }

    /**
     * 重试消息
     * @param msg
     * @param body
     */
    private void retryMessage(Message msg, VideoInfoMessage body) {
        Integer delay = msg.getMessageProperties().getReceivedDelay();
        Integer retryCount = msg.getMessageProperties().getHeader(MessageHelper.HEADER_RETRY_COUNT);
        if (delay == null || delay <= 0) {
            delay = MessageHelper.INIT_DELAY_MS;
        } else {
            delay *= MessageHelper.RETRY_MULTIPLIER;
        }
        if (retryCount == null) {
            retryCount = 0;
        }
        if (++retryCount > MessageHelper.MAX_RETRY_COUNT) {
            log.warn("消息达到最大重试次数，将被丢弃: {}", msg.getMessageProperties());
            // todo: 自定达到最大重试次数后处理逻辑，默认丢弃
            return;
        }
        log.info("消息[{}]进行第{}次重试", msg.getMessageProperties(), retryCount);
        messageProducer.sendVideoInfo(body, delay, retryCount);
    }
}
