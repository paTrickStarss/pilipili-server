/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.common.constant;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
public class AuthConstant {

    public static final String AUTHORITY_PREFIX = "ROLE_";
    public static final String AUTHORITY_SUFFIX = "_AUTH";
    public static final String AUTHORITY_DELIMITER = ",";
    public static final String AUTHORITY_CLAIM_NAME = "authorities";

    public static final String UNAUTHORIZED_MSG = "Unauthorized";
    public static final String FORBIDDEN_MSG = "You do not have permission to access this resource.";

    public static final String JWT_PAYLOAD_HEADER = "JWT-Payload";

}
