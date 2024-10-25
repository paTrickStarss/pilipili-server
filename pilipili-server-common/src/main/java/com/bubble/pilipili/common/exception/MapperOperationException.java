package com.bubble.pilipili.common.exception;

/**
 * 数据访问异常
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
public class MapperOperationException extends RuntimeException {

    public MapperOperationException(String message) {
        super(message);
    }
}
