/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.*;

/**
 * @author Bubble
 * @date 2025/02/03 15:07
 */
public class RSACryptoUtil {

    public static final String CRYPTO_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String PUBLIC_KEY_B64_STR = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlmR/sv0t9aSdYYmKQUveJ2JV+vsaDVQ584/E8PGJxZcrD94UcPK1OcDQX+s8fwPWMsWR6GbR7+GqP5wLTWf8AFCvaInsVDV2vFfUKkHyImKbq4EsSJa7Yem/B0/5dXYbYoDfsgzSFp159wguiW88bXxOFeO5J4rmRHUfcV5lurRya80z8wdPlSjm3KmGgvS8nJB0GHav2QRe6BcbzW3WQ26zSLsa3qnvIb3Q75Coaw2FOWgoUXrOLrKssDuR3dvOPNCjW8p8lGpwRG3+CYxS3R5tsQ6o64S44PdP3iEC+yr0Rwvcc6cWY0HZRGLdvv8BC3p9Nza2PLT7yvprXixGLQIDAQAB";
    public static final String PRIVATE_KEY_B64_STR = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCWZH+y/S31pJ1hiYpBS94nYlX6+xoNVDnzj8Tw8YnFlysP3hRw8rU5wNBf6zx/A9YyxZHoZtHv4ao/nAtNZ/wAUK9oiexUNXa8V9QqQfIiYpurgSxIlrth6b8HT/l1dhtigN+yDNIWnXn3CC6JbzxtfE4V47kniuZEdR9xXmW6tHJrzTPzB0+VKObcqYaC9LyckHQYdq/ZBF7oFxvNbdZDbrNIuxreqe8hvdDvkKhrDYU5aChRes4usqywO5Hd28480KNbynyUanBEbf4JjFLdHm2xDqjrhLjg90/eIQL7KvRHC9xzpxZjQdlEYt2+/wELen03NrY8tPvK+mteLEYtAgMBAAECggEAD+qi+TaCXrUFUwVTdIWrBVAK+X0JzM9VMsnMdhOaGSdej1dZonqNY4dchkpHJMorKURvpMD9Bv5DHmhNaevPZ9kiN1YhhOgDoPtKoQ5Jqgn1n7kUDrqlKj+q0ANOy3esfkhVZGqm8W3JMrPS+QikxJGbUgW/2seAh049/amX6qJ0LgTAjIsPq+gKeBOtVaO5Rzdym9ydaWHrhrbB0FCmT0RbcqhdidUWyRldcjbPmm8Uy3S2wrO8fP5f9h3hlRG8W4YpcLeG3IYo/A37ULFeuWIIkEsO0qB4VzXp12jveyI05xHj2xyHYckCj0kZ+1An7JqlNjtuhFIpQiMFeloSAQKBgQDpvOCnyLZMJ1OwSOzBnLdMlNlVXtKJCeVSUFK1ZVgoafTDi4aduL1rquLAijrRELF+HT5x0s2pIgSUBT8XFGljFI0g1DcvZ8C2RsvfH5qvQMIFxBNjZ4FPYSf44On9wKiZSFeryheL/evfwepXf7vRXENKCEMr3PjY8oxReEc5HQKBgQCkt3PVo9W61JkJsQmimW4ygVcW7QwKPd9ifkojag+Hd6SmjFg1nIafSWDgInJqWhzeRNrLL3OYxNLShdyZ+gtQ+rLwaWwLxSH+aXIDzD5uXEJZqrUHmB4sGDv18ZjLy7hjgrwN7lg7nuYzqsmtWvBkESaZBP/EQZecojzOHD3EUQKBgQC/xVHlF05KSPHm7s4hQ/03/eAapQPDn7Tgxjo1zgJ4lgZV8cHu4cW38qq8Ii2DnliiCkhThwtpuxlagLt0qjCVNCVeS/9DC7MXWhqm4/070LvfkzH9Gs0DXck37wDMSyeJK8uSjOqo1xqRqT4lsA1fBn3G3eSOtmwNQ2NQ7IgzGQKBgQCgkxsATVxhaznZ4dXDxpFP+Rj+h45yOYkYyobZ6V5EMNW5ax3e/b2bcPxY5BvoI/r6j+M/qv2lE1N22fqJvOHlStDAX/Vtr/sIuqDkpOrDyXsBUNJ0Gc5oLCPgCmtrWSljESLk7dLkJG7ENZ2wOvQYiUO4btAr8cmsb9iZEZ0BUQKBgBBV3oGXrSpr9UU74BrlAqwiUlaqwPwEei7c+Vta/NvaMkDXHix57J1ioafaYM2rlqAd7/CjW98o+n+p8bLN2r4sWusX/+DriFT/PDTq9m54/wa/KYGo+Q7jVVRLFbQzGM4z7npDQz09A9qyzJiI289+n7+RyxWxOgG9GBGPqD2Q";
    public static final int KEY_SIZE = 2048;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Decoder BASE64_DECODER = Base64.getDecoder();
    private static final KeyFactory KEY_FACTORY;
    private static final PrivateKey PRIVATE_KEY;
    private static final PublicKey PUBLIC_KEY;
    private static final Cipher CIPHER_INSTANCE_ENCRYPT;
    private static final Cipher CIPHER_INSTANCE_DECRYPT;

    private static final Signature SIGNATURE_INSTANCE_SIGN;
    private static final Signature SIGNATURE_INSTANCE_VERIFY;

    static {
        try {
            KEY_FACTORY = KeyFactory.getInstance(CRYPTO_ALGORITHM);
            PRIVATE_KEY = (PrivateKey) getKeyFromBase64Str(PRIVATE_KEY_B64_STR, 1);
            PUBLIC_KEY = (PublicKey) getKeyFromBase64Str(PUBLIC_KEY_B64_STR, 2);

            Cipher encryptCipher = Cipher.getInstance(CRYPTO_ALGORITHM);
            encryptCipher.init(Cipher.ENCRYPT_MODE, PUBLIC_KEY);
            CIPHER_INSTANCE_ENCRYPT = encryptCipher;
            Cipher decryptCipher = Cipher.getInstance(CRYPTO_ALGORITHM);
            decryptCipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
            CIPHER_INSTANCE_DECRYPT = decryptCipher;

            Signature instanceSign = Signature.getInstance(SIGNATURE_ALGORITHM);
            instanceSign.initSign(PRIVATE_KEY);
            SIGNATURE_INSTANCE_SIGN = instanceSign;
            Signature instanceVerify = Signature.getInstance(SIGNATURE_ALGORITHM);
            instanceVerify.initVerify(PUBLIC_KEY);
            SIGNATURE_INSTANCE_VERIFY = instanceVerify;

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

//    private static final Map<Algorithm>

    /**
     * 生成公钥私钥对象
     * @param base64KeyStr
     * @param mode 1-PrivateKey  2-PublicKey
     * @return
     * @throws InvalidKeySpecException
     */
    public static Key getKeyFromBase64Str(String base64KeyStr, int mode) throws InvalidKeySpecException {
        if (mode == 1) {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(BASE64_DECODER.decode(base64KeyStr));
            return KEY_FACTORY.generatePrivate(keySpec);
        } else if (mode == 2) {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(BASE64_DECODER.decode(base64KeyStr));
            return KEY_FACTORY.generatePublic(keySpec);
        }
        return null;
    }

    /**
     * 生成RSA密钥对
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static AsymmetricallyKeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(CRYPTO_ALGORITHM);
        generator.initialize(KEY_SIZE);
        KeyPair keyPair = generator.generateKeyPair();
        String publicKey = BASE64_ENCODER.encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = BASE64_ENCODER.encodeToString(keyPair.getPrivate().getEncoded());
        return new AsymmetricallyKeyPair(publicKey, privateKey);
    }


    /**
     * 使用公钥加密
     * @param plainText
     * @return
     */
    public static String encrypt(String plainText) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = CIPHER_INSTANCE_ENCRYPT.doFinal(plainText.getBytes(DEFAULT_CHARSET));

        return BASE64_ENCODER.encodeToString(encryptedBytes);
    }

    /**
     * 使用私钥解密
     * @param encryptedText
     * @return
     */
    public static String decrypt(String encryptedText) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = BASE64_DECODER.decode(encryptedText);
        byte[] decryptedBytes = CIPHER_INSTANCE_DECRYPT.doFinal(encryptedBytes);

        return new String(decryptedBytes, DEFAULT_CHARSET);
    }

    /**
     * 使用私钥加签
     * @param text
     * @return
     */
    public static String sign(String text) throws SignatureException {
        SIGNATURE_INSTANCE_SIGN.update(text.getBytes(DEFAULT_CHARSET));
        return BASE64_ENCODER.encodeToString(SIGNATURE_INSTANCE_SIGN.sign());
    }

    /**
     * 使用公钥验签
     * @param text 内容
     * @param signatureText 数字签名
     * @return
     * @throws SignatureException
     */
    public static boolean verify(String text, String signatureText) throws SignatureException {
        SIGNATURE_INSTANCE_VERIFY.update(text.getBytes(DEFAULT_CHARSET));
        return SIGNATURE_INSTANCE_VERIFY.verify(BASE64_DECODER.decode(signatureText));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AsymmetricallyKeyPair {

        private String publicKey;
        private String privateKey;
    }
}
