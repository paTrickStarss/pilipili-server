/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author Bubble
 * @date 2025/02/03 16:21
 */
public class TestRSA {

    public static void main(String[] args) throws NoSuchAlgorithmException {
//        RSAUtil.AsymmetricallyKeyPair keyPair = RSAUtil.generateRSAKeyPair();
//        System.out.println("Public Key: ");
//        System.out.println(keyPair.getPublicKey());
//        System.out.println("Private Key: ");
//        System.out.println(keyPair.getPrivateKey());

//        String plainText = "This is a test from Bubble!!!";
//        try {
//            String encryptText = RSACryptoUtil.encrypt(plainText);
//            System.out.println("After encrypt: " + encryptText);
//            String decryptText = RSACryptoUtil.decrypt(encryptText);
//            System.out.println("After decrypt: " + decryptText);
//        } catch (IllegalBlockSizeException | BadPaddingException e) {
//            throw new RuntimeException(e);
//        }

        System.out.println(UUID.randomUUID().toString());
    }
}
