/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.config;

import com.aliyun.oss.OSS;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * OSS客户端连接池
 * @author Bubble
 * @date 2025.03.21 15:28
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss.pool")
@Data
public class OssClientPool {

    @Value("${aliyun.oss.pool.maxIdleConnections}")
    private int maxIdleConnections;
    @Value("${aliyun.oss.pool.keepAliveTime}")
    private long keepAliveTime;

    @Autowired
    private OssClientConfig ossClientConfig;

    private BlockingQueue<OSS> clientQueue;

    @PostConstruct
    public void init() {
        clientQueue = new LinkedBlockingQueue<>(maxIdleConnections);
    }

    /**
     * 获取一个OSS客户端
     * @return
     */
    public OSS fetchClient() {
        OSS client = clientQueue.poll();
        if (client == null) {
            return ossClientConfig.ossClient();
        }
        return client;
    }

    /**
     * 释放OSS客户端
     * @param client
     */
    public void releaseClient(OSS client) {
        if (client == null) {
            return;
        }
        if (clientQueue.size() < maxIdleConnections) {
            boolean offer = clientQueue.offer(client);
            // 队列已满，直接关闭
            if (!offer) {
                client.shutdown();
            }
        } else {
            // 队列已满，直接关闭
            client.shutdown();
        }
    }

    /**
     * 关闭连接池
     */
    public void shutdown() {
        clientQueue.forEach(OSS::shutdown);
        clientQueue.clear();
    }


}
