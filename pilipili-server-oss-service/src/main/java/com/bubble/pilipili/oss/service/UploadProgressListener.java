/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.service;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Bubble
 * @date 2025.03.26 22:35
 */
@Slf4j
public class UploadProgressListener implements ProgressListener {

    private final int partNum;
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private long lastTime;
    private boolean succeed = false;

    public UploadProgressListener(int partNum) {
        this.partNum = partNum;
        this.lastTime = System.currentTimeMillis();
    }

    /**
     * @param progressEvent
     */
    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();

        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                log.info("[PART_UPLOAD] PART[{}] Start to upload......", partNum);
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                log.info("[PART_UPLOAD] PART[{}] {} bytes in total will be uploaded to OSS", partNum, this.totalBytes);
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastTime;
                if (elapsedTime < 1000) {
                    return;
                }
                lastTime = currentTime;
                if (this.totalBytes != -1) {
                    int percent = (int)(this.bytesWritten * 100.0 / this.totalBytes);
                    log.info("[PART_UPLOAD] PART[{}] {} bytes have been written at this time, upload progress: {}%({}/{})",
                            partNum, bytes, percent, this.bytesWritten, this.totalBytes);
                } else {
                    log.info("[PART_UPLOAD] PART[{}] {} bytes have been written at this time, upload ratio: unknown({}/...)",
                            partNum, bytes, this.bytesWritten);
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                log.info("[PART_UPLOAD] PART[{}] Succeed to upload, {} bytes have been transferred in total",
                        partNum, this.bytesWritten);
                break;
            case TRANSFER_FAILED_EVENT:
                log.info("[PART_UPLOAD] PART[{}] Failed to upload, {} bytes have been transferred",
                        partNum, this.bytesWritten);
                break;
            default:
                break;
        }
        }

}
