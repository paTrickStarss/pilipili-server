///*
// * Copyright (c) 2025. Bubble
// */
//
//package com.bubble.pilipili.mq.consumer;
//
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
///**
// * @author Bubble
// * @date 2025.04.03 15:01
// */
//@Slf4j
////@Service
////@RabbitListener(queues = "queue.pilipili.delay")
//public class DelayMessageConsumer {
//
//
////    @RabbitHandler
//    public void receiveMessage(Object message, Channel channel) throws IOException {
//        Message msg = (Message) message;
//        long deliveryTag = msg.getMessageProperties().getDeliveryTag();
//
//        Integer delay = msg.getMessageProperties().getReceivedDelay();
//        log.debug("receive message: {}\ndelay:{} ms", msg, delay);
//        channel.basicAck(deliveryTag, false);
//    }
//}
