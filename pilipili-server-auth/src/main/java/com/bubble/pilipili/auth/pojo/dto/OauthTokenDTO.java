/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthTokenDTO {
    /*
    *     "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxMDAxIiwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTczNTExNDY2MCwiYXV0aG9yaXRpZXMiOlsiVVNFUiJdLCJqdGkiOiJmMzk2ZmU5NS05MTU2LTRkN2UtYWZhNC03YzlmM2IxNDM3ZjMiLCJjbGllbnRfaWQiOiJjbGllbnQiLCJ1c2VybmFtZSI6IjEwMDEifQ.XpHhdPE2_fbTGJ6V5ndLaZxEuPsrmLK1JDxjB7gLosfHFwbSCO__i3ho2J5F2xSGap2IYU7CWITy8wiNmUXZWIX6vffcQZs28miH1Z4TNnfcWXJ2ccZTwdTbcEHrAyZJJLlNLn7mbHdYyIiKkYE8zCaKgaZjlirb0359son_pNpuY-Eh1BHNfU5nTO-_Va-NtcqDAjyuJS3_M14t-i5qrJMswxZmzg64jBzIMXMpEfgianBtkgmLtEAR9WJYL14V2sOlsVyoW1NFBWnjUStoAOl8qMqw9Db4LcCFKIJE1ZIWwAbG0BRTFYnEN4v_bZG3ycaJCHm5gkRzwV1GX485JA",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxMDAxIiwic2NvcGUiOlsiYWxsIl0sImF0aSI6ImYzOTZmZTk1LTkxNTYtNGQ3ZS1hZmE0LTdjOWYzYjE0MzdmMyIsImV4cCI6MTczNTE5NzQ2MCwiYXV0aG9yaXRpZXMiOlsiVVNFUiJdLCJqdGkiOiJhYWY5ZTcxYS05MGQxLTRiNDItYmFhNi02ZTNkZjA0ZjEwNDkiLCJjbGllbnRfaWQiOiJjbGllbnQiLCJ1c2VybmFtZSI6IjEwMDEifQ.SIJcDozDq5A4_XYFE1rqRRpWM0hwML_vGptuOKhQv9FJ2RWomyzy69J5oE25y_gfeugDBR5G5Nbz4i1oYgOKcSZrOAFzVkqE7Pcvo3ECzZo513k_P5O7X78JZFbmChehbNgm1ynIan86b2RvrJpeSHOcG0iGsLk0yfsrcjyY1BcWPUBjtHEFLkCJiukG5rSzQ7A0uZit6CiLdQWIyukVO7iROwdoKBias4-TXsPk-dYXN7epCfwRJO24j9sYz9hJbz8s6sKCcF8YuF5ePR6110QR2vWkzgKzflkBjs8f5xiblhLY8nXQWujqxoCLpGI_LTJIanVaUgZJiWC6iPBh9g",
    "expires_in": 3599,
    "scope": "all",
    "username": "1001",
    "jti": "f396fe95-9156-4d7e-afa4-7c9f3b1437f3"
    * */
    private String access_token;
    private String refresh_token;
    private String token_type;
    private Long expires_in;
    private String scope;
    private String jti;
    private String username;

    private String error;
    private String error_description;
}
