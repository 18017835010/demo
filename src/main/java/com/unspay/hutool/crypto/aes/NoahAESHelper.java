package com.unspay.hutool.crypto.aes;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 *
 * AES加解密工具类
 *
 * @author tengyun@noahgroup.com
 * @date 2020/12/27
 */
public class NoahAESHelper {
    private final static String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private final static String IV = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";

    private final static int AES256_KEY_SIZE = 32;
    private final static byte[] AES_CODE_BOOK = new byte[]{
            (byte)0x10, (byte)0x82, (byte)0x3d, (byte)0xfc, (byte)0xaa, (byte)0x9e, (byte)0x51, (byte)0x44,
            (byte)0xc9, (byte)0x7d, (byte)0x9d, (byte)0x3a, (byte)0x93, (byte)0xb1, (byte)0x19, (byte)0x3e,
            (byte)0xfa, (byte)0xd3, (byte)0xba, (byte)0x5b, (byte)0x64, (byte)0x9f, (byte)0x2c, (byte)0x94,
            (byte)0x15, (byte)0xec, (byte)0x76, (byte)0x74, (byte)0x48, (byte)0x49, (byte)0x1c, (byte)0x23,
            (byte)0xdf, (byte)0x9f, (byte)0x02, (byte)0x49, (byte)0x69, (byte)0x9d, (byte)0x0d, (byte)0xf0,
            (byte)0x24, (byte)0xca, (byte)0xaf, (byte)0x6b, (byte)0xc5, (byte)0x8f, (byte)0x57, (byte)0xad,
            (byte)0x1d, (byte)0x05, (byte)0x72, (byte)0x60, (byte)0x21, (byte)0x99, (byte)0xf4, (byte)0xce,
            (byte)0xc2, (byte)0x8e, (byte)0x00, (byte)0x62, (byte)0x87, (byte)0xd8, (byte)0x12, (byte)0xf7,
            (byte)0x3e, (byte)0x45, (byte)0x67, (byte)0xbc, (byte)0x12, (byte)0x06, (byte)0x84, (byte)0x14,
            (byte)0x41, (byte)0xb1, (byte)0xb1, (byte)0x7f, (byte)0x50, (byte)0xcc, (byte)0xa2, (byte)0xba,
            (byte)0x06, (byte)0x6e, (byte)0x60, (byte)0x2b, (byte)0xaf, (byte)0x40, (byte)0xf2, (byte)0xe3,
            (byte)0xcb, (byte)0xb9, (byte)0x16, (byte)0x2e, (byte)0x2c, (byte)0x99, (byte)0xe7, (byte)0x7a,
            (byte)0xdf, (byte)0x9c, (byte)0xcb, (byte)0x60, (byte)0xf2, (byte)0x3e, (byte)0xfd, (byte)0x5c,
            (byte)0xba, (byte)0x79, (byte)0x56, (byte)0x1d, (byte)0xba, (byte)0x33, (byte)0xe4, (byte)0xf1,
            (byte)0x31, (byte)0xe7, (byte)0xb6, (byte)0x41, (byte)0x30, (byte)0xed, (byte)0x66, (byte)0xf6,
            (byte)0x71, (byte)0x36, (byte)0x3d, (byte)0x24, (byte)0xd9, (byte)0x70, (byte)0xa9, (byte)0xf5,
            (byte)0xc8, (byte)0xa9, (byte)0x54, (byte)0xe0, (byte)0xd3, (byte)0x03, (byte)0xcd, (byte)0x9f,
            (byte)0xc2, (byte)0xb4, (byte)0x0c, (byte)0x75, (byte)0xff, (byte)0x18, (byte)0x32, (byte)0xd5,
            (byte)0x17, (byte)0x79, (byte)0x33, (byte)0xab, (byte)0xd8, (byte)0xfc, (byte)0x81, (byte)0xdd,
            (byte)0x34, (byte)0x26, (byte)0x84, (byte)0x30, (byte)0x54, (byte)0xea, (byte)0xb6, (byte)0x2e,
            (byte)0xca, (byte)0x27, (byte)0x81, (byte)0xc7, (byte)0x68, (byte)0x0e, (byte)0x7f, (byte)0x5a,
            (byte)0x5d, (byte)0x60, (byte)0x46, (byte)0x6e, (byte)0x7b, (byte)0x26, (byte)0x1d, (byte)0x0f,
            (byte)0xe3, (byte)0x6d, (byte)0x4b, (byte)0xf7, (byte)0xd8, (byte)0xc3, (byte)0x60, (byte)0x90,
            (byte)0x1d, (byte)0x63, (byte)0xd6, (byte)0x10, (byte)0xe1, (byte)0xff, (byte)0xbd, (byte)0xe7,
            (byte)0x02, (byte)0x05, (byte)0x4e, (byte)0x29, (byte)0x0b, (byte)0x74, (byte)0x5d, (byte)0x6a,
            (byte)0xba, (byte)0xaf, (byte)0xb8, (byte)0x37, (byte)0x0d, (byte)0x06, (byte)0x31, (byte)0xe0,
            (byte)0x46, (byte)0x24, (byte)0x47, (byte)0xef, (byte)0x59, (byte)0xb2, (byte)0x84, (byte)0x62,
            (byte)0x89, (byte)0x43, (byte)0x3d, (byte)0x1f, (byte)0x89, (byte)0xf5, (byte)0x1a, (byte)0x38,
            (byte)0x52, (byte)0xa6, (byte)0x0a, (byte)0xda, (byte)0xf1, (byte)0xc5, (byte)0x24, (byte)0xc8,
            (byte)0x62, (byte)0x83, (byte)0xfe, (byte)0x0a, (byte)0x45, (byte)0x27, (byte)0x64, (byte)0x47,
            (byte)0xdf, (byte)0xe2, (byte)0x11, (byte)0x3b, (byte)0xdf, (byte)0x5e, (byte)0x37, (byte)0xe5,
            (byte)0x92, (byte)0xd6, (byte)0xcc, (byte)0xec, (byte)0x68, (byte)0x2e, (byte)0xf9, (byte)0x3a
    };

    /**
     * 根据传入spec和密码本动态生成密钥
     * @param spec 密钥spec，一般可取字段名例如 user.user（user表，user字段）
     * @return 密钥
     */
    private static byte[] getKey(final String spec) {
        byte[] specByte = spec.getBytes(StandardCharsets.UTF_8);
        int specLen = specByte.length;
        if (specLen >= AES256_KEY_SIZE) {
            return Arrays.copyOfRange(specByte, 0, AES256_KEY_SIZE);
        } else if (specLen <= 0) {
            return Arrays.copyOfRange(AES_CODE_BOOK, 0, AES256_KEY_SIZE);
        }
        byte[] key = new byte[AES256_KEY_SIZE];
        int j = 0, offset = 0;
        for (int i = 0; i < AES256_KEY_SIZE; ++i) {
            if (j == specLen) {
                j = 0;
            }
            offset = (offset + ((specByte[j]) & 0x7F) + 1) & 0xFF;
            key[i] = AES_CODE_BOOK[offset];
            j++;
        }
        return key;
    }

    /**
     * AES加密
     * @param input 明文
     * @param spec 密钥spec，一般可取字段名例如 user.user（user表，user字段）
     * @return 密文，经过Base64Encode处理；异常时返回空字符串，不抛出异常
     */
    public static String encrypt(final String input, final String spec) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(getKey(spec), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return new String(Base64.getEncoder().encode(cipher.doFinal(input.getBytes(StandardCharsets.UTF_8))));
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException
                | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
        }
        return "";
    }

    /**
     * AES解密
     * @param input 密文
     * @param spec 密钥spec，一般可取字段名例如 user.user（user表，user字段）
     * @return 原文；异常时返回空字符串，不抛出异常
     */
    public static String decrypt(final String input, final String spec) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(getKey(spec), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(input.getBytes(StandardCharsets.UTF_8))));
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException
                | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
        }
        return "";
    }
}