/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
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
@Slf4j
@Component
public class CryptoHelper {

    public static final String CRYPTO_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final int KEY_SIZE = 2048;
    public static final String privateKeyPrefix = "-----BEGIN PRIVATE KEY-----";
    public static final String privateKeySuffix = "-----END PRIVATE KEY-----";
    public static final String publicKeyPrefix = "-----BEGIN PUBLIC KEY-----";
    public static final String publicKeySuffix = "-----END PUBLIC KEY-----";

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Decoder BASE64_DECODER = Base64.getDecoder();

    @Getter
    private String publicKeyStrWrapped;
    private KeyFactory KEY_FACTORY;
    private PrivateKey PRIVATE_KEY;
    private PublicKey PUBLIC_KEY;
    private Cipher CIPHER_INSTANCE_ENCRYPT;
    private Cipher CIPHER_INSTANCE_DECRYPT;
    private Signature SIGNATURE_INSTANCE_SIGN;
    private Signature SIGNATURE_INSTANCE_VERIFY;

    /**
     * 读取密钥文件，构建密钥对象
     */
    private void readKeyStrFromFile() {
        String[] pathArr = new String[]{"rsa_key/crypto_private_key.txt", "rsa_key/crypto_public_key.txt"};
        String[] keyStrArr = new String[2];
        for (int i = 0; i < pathArr.length; i++) {
            String path = pathArr[i];
            ClassPathResource privateKeyResource = new ClassPathResource(path);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(privateKeyResource.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                if (sb.length() == 0) {
                    throw new RuntimeException("Empty key file");
                }
                keyStrArr[i] = sb.toString();
            } catch (IOException e) {
                log.error("读取密钥对文件失败");
                throw new RuntimeException(e);
            }
        }
        try {
            publicKeyStrWrapped = keyStrArr[1];

            String privateKeyStr = keyStrArr[0].substring(
                    keyStrArr[0].indexOf(privateKeyPrefix) + privateKeyPrefix.length(),
                    keyStrArr[0].indexOf(privateKeySuffix)
            );
            String publicKeyStr = keyStrArr[1].substring(
                    keyStrArr[1].indexOf(publicKeyPrefix) + publicKeyPrefix.length(),
                    keyStrArr[1].indexOf(publicKeySuffix)
            );
            KEY_FACTORY = KeyFactory.getInstance(CRYPTO_ALGORITHM);
            PRIVATE_KEY = (PrivateKey) getKeyFromBase64Str(privateKeyStr, 1);
            PUBLIC_KEY = (PublicKey) getKeyFromBase64Str(publicKeyStr, 2);
        } catch (Exception e) {
            log.error("构建密钥对象失败: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        try {
            readKeyStrFromFile();

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

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            log.error("[{}]初始化异常", getClass().getCanonicalName());
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成公钥私钥对象
     * @param base64KeyStr
     * @param mode 1-PrivateKey  2-PublicKey
     * @return
     * @throws InvalidKeySpecException
     */
    private Key getKeyFromBase64Str(String base64KeyStr, int mode) throws InvalidKeySpecException {
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
     * 使用公钥加密
     * @param plainText
     * @return
     */
    public String encrypt(String plainText) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = CIPHER_INSTANCE_ENCRYPT.doFinal(plainText.getBytes(DEFAULT_CHARSET));

        return BASE64_ENCODER.encodeToString(encryptedBytes);
    }

    /**
     * 使用私钥解密
     * @param encryptedText
     * @return
     */
    public String decrypt(String encryptedText) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = BASE64_DECODER.decode(encryptedText);
        byte[] decryptedBytes = CIPHER_INSTANCE_DECRYPT.doFinal(encryptedBytes);

        return new String(decryptedBytes, DEFAULT_CHARSET);
    }

    /**
     * 使用私钥加签
     * @param text
     * @return
     */
    public String sign(String text) throws SignatureException {
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
    public boolean verify(String text, String signatureText) throws SignatureException {
        SIGNATURE_INSTANCE_VERIFY.update(text.getBytes(DEFAULT_CHARSET));
        return SIGNATURE_INSTANCE_VERIFY.verify(BASE64_DECODER.decode(signatureText));
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

    private static void writeKeyStrToFile(String keyStr, String filePath, String prefix, String suffix) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(prefix);
            writer.write(keyStr);
            writer.write(suffix);
        } catch (IOException e) {
            log.error("写入文件失败");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        log.info("Current directory: " + System.getProperty("user.dir"));
        AsymmetricallyKeyPair keyPair = generateRSAKeyPair();
        writeKeyStrToFile(keyPair.getPrivateKey(), "private_key.txt", privateKeyPrefix, privateKeySuffix);
        writeKeyStrToFile(keyPair.getPublicKey(), "public_key.txt", publicKeyPrefix, publicKeySuffix);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AsymmetricallyKeyPair {

        private String publicKey;
        private String privateKey;
    }
}
