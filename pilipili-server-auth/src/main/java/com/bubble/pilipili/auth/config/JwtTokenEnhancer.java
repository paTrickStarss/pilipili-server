package com.bubble.pilipili.auth.config;

import com.bubble.pilipili.auth.dto.UserDTO;
import com.bubble.pilipili.auth.entity.UserAuth;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT内容增强器
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    /**
     * Provides an opportunity for customization of an access token (e.g. through its additional information map) during
     * the process of creating a new token for use by a client.
     *
     * @param accessToken    the current access token with its expiration and refresh token
     * @param authentication the current authentication including client and user details
     * @return a new token enhanced with additional information
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UserDTO userAuth = (UserDTO) authentication.getPrincipal();
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("username", userAuth.getUsername());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }
}
