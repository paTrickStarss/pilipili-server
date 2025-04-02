/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.04.01 15:37
 */
@Data
@NoArgsConstructor
public class FFprobeFormatTags {

    /**
     * MPEG-4: mp42<br>
     * MOV: <pre>qt  </pre>(qt后有两个空格)
     */
    private String major_brand;
    private String minor_version;
    private String compatible_brands;
    private String creation_time;
    private String date;

    // MOV 特有 Apple设备拍摄的视频元信息（地理位置信息、设备信息、签名）
    @JSONField(name = "com.apple.quicktime.location.accuracy.horizontal")
    private String location_accuracy_horizontal;
    /**
     * 地理位置（ISO6709标准）
     */
    @JSONField(name = "com.apple.quicktime.location.ISO6709")
    private String location_iso6709;
    /**
     * 制造商
     */
    @JSONField(name = "com.apple.quicktime.make")
    private String make;
    /**
     * 设备名
     */
    @JSONField(name = "com.apple.quicktime.model")
    private String model;
    /**
     * 软件版本（iOS版本）
     */
    @JSONField(name = "com.apple.quicktime.software")
    private String software;
    /**
     * 创建日期
     */
    @JSONField(name = "com.apple.quicktime.creationdate")
    private String creationdate;
    /**
     * 签名
     */
    @JSONField(name = "com.apple.photos.originating.signature")
    private String signature;
}
