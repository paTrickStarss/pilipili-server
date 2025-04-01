/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.04.01 22:20
 */
@Data
@NoArgsConstructor
public class CommandResult {

    private Boolean success;
    private String output;
    private long elapsedTime;

    public static CommandResult success(String output, long elapsedTime) {
        CommandResult result = new CommandResult();
        result.success = true;
        result.output = output;
        result.elapsedTime = elapsedTime;
        return result;
    }

    public static CommandResult failure(String output, long elapsedTime) {
        CommandResult result = new CommandResult();
        result.success = false;
        result.output = output;
        result.elapsedTime = elapsedTime;
        return result;
    }
}
