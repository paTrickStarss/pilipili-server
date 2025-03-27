/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.common.component.RedisHelper;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.feign.api.VideoFeignAPI;
import com.bubble.pilipili.feign.pojo.req.UpdateVideoInfoReq;
import com.bubble.pilipili.mq.entity.VideoInfoMessage;
import com.bubble.pilipili.mq.util.MessageHelper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.io.IOException;

// todo: 这个队列似乎没有存在的必要了
/**
 * @author Bubble
 * @date 2025.03.26 17:13
 */
@Slf4j
//@Service
//@RabbitListener(queues = "queue.pilipili.info.video")
public class VideoInfoConsumer {

    private final MessageHelper messageHelper;
    private final RedisHelper redisHelper;
    private final VideoFeignAPI videoFeignAPI;

    public VideoInfoConsumer(MessageHelper messageHelper, RedisHelper redisHelper, VideoFeignAPI videoFeignAPI) {
        this.messageHelper = messageHelper;
        this.redisHelper = redisHelper;
        this.videoFeignAPI = videoFeignAPI;
    }

//    @RabbitHandler
    public void receiveMessage(Object message, Channel channel) throws IOException {
        Message msg = (Message) message;
        long deliveryTag = msg.getMessageProperties().getDeliveryTag();

        Object messageBody = messageHelper.getMessageBody(msg);
        if (messageBody instanceof VideoInfoMessage) {
            VideoInfoMessage body = (VideoInfoMessage) messageBody;
            String taskId = body.getTaskId();

            // todo: 如果用户还没保存视频，这里获取不到taskId对应的vid，考虑等待一段时间重试
            Integer vid = redisHelper.getVideoTaskVid(taskId);
            if (vid == null) {
                channel.basicNack(deliveryTag, false, true);
            }
            // 更新视频信息（contentUrl）
            UpdateVideoInfoReq req = new UpdateVideoInfoReq();
            req.setVid(vid);
            req.setContentUrl(body.getContentUrl());
            SimpleResponse<String> response = videoFeignAPI.update(req);
            if (!response.isSuccess()) {
                log.warn("视频信息更新失败: {}", response.getMsg());
                // 重新入队，等待一段时间重试
                channel.basicNack(deliveryTag, false, true);
//                    channel.basicReject(deliveryTag, false);
            } else {
                channel.basicAck(deliveryTag, false);
            }
        }

    }
}
