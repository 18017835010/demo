package com.unspay.hutool.crypto.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSAHelper - 对RSA 签名&验签/分段加密&分段解密 的包装
 * 签名算法: "SHA1withRSA", 私钥进行签名; 公钥进行验签.
 * 加密算法: "RSA/ECB/PKCS1Padding", 公钥进行加密; 私钥进行解密.
 *
 * [localPrivKey]是自己的私钥, 自己的公钥给通信对方.
 * [peerPubKey]是对方的公钥, 对方的私钥在对方那边.
 * 为了方便, 这里假定双方的密钥长度一致, 签名和加密的规则也一致.
 *
 * 以`Base64Str`结尾的参数表示内容是Base64编码的字符串, 其他情况都是raw字符串.
 *
 * @author sangechen
 */
public class NoahRSAHelper {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"; //加密block需要预留11字节
    public static final int KEY_BIT = 2048;
    public static final int RESERVE_BYTES = 11;
    private KeyFactory keyFactory;
    private Signature signature;
    private Cipher cipher;
    private PrivateKey localPrivateKey;
    private PublicKey peerPublicKey;
    private int encryptBlock;
    private int decryptBlock;

    public NoahRSAHelper() {
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            localPrivateKey = null;
            peerPublicKey = null;
            decryptBlock = KEY_BIT / 8; //256 bytes
            encryptBlock = decryptBlock - RESERVE_BYTES; //245 bytes
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        }
    }

    /**
     * 初始化自己的私钥,对方的公钥以及密钥长度.
     * `openssl genrsa -out rsa_2048.key 2048` #指定生成的密钥的位数: 2048
     * `openssl pkcs8 -topk8 -inform PEM -in rsa_2048.key -outform PEM -nocrypt -out pkcs8.txt` #for Java 转换成PKCS#8编码
     * `openssl rsa -in rsa_2048.key -pubout -out rsa_2048_pub.key` #导出pubkey
     * @param localPrivateKeyBase64 Base64编码的私钥,PKCS#8编码. (去掉pem文件中的头尾标识)
     * @param peerPublicKeyBase64 Base64编码的公钥. (去掉pem文件中的头尾标识)
     * @param keySize 密钥长度, 一般2048
     */
    public void initKey(String localPrivateKeyBase64, String peerPublicKeyBase64, int keySize) {
        try {
            localPrivateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(localPrivateKeyBase64)));
            peerPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(peerPublicKeyBase64)));
            decryptBlock = keySize / 8;
            encryptBlock = decryptBlock - RESERVE_BYTES;
        } catch (InvalidKeySpecException e) {
        }
    }

    public void initSignature(String algorithm) {
        try {
            signature = Signature.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public String sign(byte[] plaintext) {
        String signBase64Str = "";
        try {
            signature.initSign(localPrivateKey);
            signature.update(plaintext);
            signBase64Str = new String(Base64.getEncoder().encode(signature.sign()));
        } catch (InvalidKeyException | SignatureException e) {
        }
        return signBase64Str;
    }

    public String sign(String plaintext) {
        return sign(plaintext.getBytes());
    }

    public boolean verify(byte[] plaintext, String signBase64) {
        boolean isValid = false;
        try {
            signature.initVerify(peerPublicKey);
            signature.update(plaintext);
            isValid = signature.verify(Base64.getDecoder().decode(signBase64));
        } catch (InvalidKeyException | SignatureException e) {
        }
        return isValid;
    }

    public boolean verify(String plaintext, String signBase64) {
        return verify(plaintext.getBytes(), signBase64);
    }

    public void initCipher(String transformation) {
        try {
            cipher = Cipher.getInstance(transformation);
            //TODO decryptBlock和encryptBlock可能需要重新计算
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        }
    }

    public String encrypt(byte[] data) {
        //计算分段加密的block数 (向上取整)
        int nBlock = (data.length / encryptBlock);
        if ((data.length % encryptBlock) != 0) { //余数非0block数再加1
            nBlock += 1;
        }
        //for debug. System.out.printf("encryptBlock: %d/%d ~ %d\n", data.length, encryptBlock, nBlock);
        //输出buffer, 大小为nBlock个decryptBlock
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream(nBlock * decryptBlock);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, peerPublicKey);
            //cryptedBase64Str = Base64.encodeBase64String(cipher.doFinal(plaintext.getBytes()));
            //分段加密
            for (int offset = 0; offset < data.length; offset += encryptBlock) {
                //block大小: encryptBlock 或 剩余字节数
                int inputLen = (data.length - offset);
                if (inputLen > encryptBlock) {
                    inputLen = encryptBlock;
                }
                //得到分段加密结果
                byte[] encryptedBlock = cipher.doFinal(data, offset, inputLen);
                //追加结果到输出buffer中
                outBuf.write(encryptedBlock);
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
        }
        return new String(Base64.getEncoder().encode(outBuf.toByteArray())); //ciphertext
    }

    public String encrypt(String plaintext) {
        return encrypt(plaintext.getBytes());
    }

    public byte[] decrypt2Byte(String encryptedBase64) {
        //转换得到字节流
        byte[] data = Base64.getDecoder().decode(encryptedBase64);
        //计算分段解密的block数 (理论上应该能整除)
        int nBlock = (data.length / decryptBlock);
        //for debug. System.out.printf("decryptBlock: %d/%d ~ %d\n", data.length, decryptBlock, nBlock);
        //输出buffer, , 大小为nBlock个encryptBlock
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * encryptBlock);
        try {
            cipher.init(Cipher.DECRYPT_MODE, localPrivateKey);
            //plaintext = new String(cipher.doFinal(Base64.decodeBase64(cryptedBase64Str)));
            //分段解密
            for (int offset = 0; offset < data.length; offset += decryptBlock) {
                //block大小: decryptBlock 或 剩余字节数
                int inputLen = (data.length - offset);
                if (inputLen > decryptBlock) {
                    inputLen = decryptBlock;
                }
                //得到分段解密结果
                byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
                //追加结果到输出buffer中
                outbuf.write(decryptedBlock);
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
            e.printStackTrace();
        }
        return outbuf.toByteArray();
    }

    public String decrypt(String encryptedBase64) {
        return new String(decrypt2Byte(encryptedBase64));
    }

    // for static user
    public static String encryptByPublicKey(String plaintext, String publicKey, int keySize) {
        int decryptBlock = keySize / 8;
        int encryptBlock = decryptBlock - RESERVE_BYTES;

        byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
        //计算分段加密的block数 (向上取整)
        int nBlock = (data.length / encryptBlock);
        if ((data.length % encryptBlock) != 0) { //余数非0block数再加1
            nBlock += 1;
        }
        //for debug. System.out.printf("encryptBlock: %d/%d ~ %d\n", data.length, encryptBlock, nBlock);
        //输出buffer, 大小为nBlock个decryptBlock
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream(nBlock * decryptBlock);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            PublicKey peerPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
            cipher.init(Cipher.ENCRYPT_MODE, peerPublicKey);
            //分段加密
            for (int offset = 0; offset < data.length; offset += encryptBlock) {
                //block大小: encryptBlock 或 剩余字节数
                int inputLen = (data.length - offset);
                if (inputLen > encryptBlock) {
                    inputLen = encryptBlock;
                }
                //得到分段加密结果
                byte[] encryptedBlock = cipher.doFinal(data, offset, inputLen);
                //追加结果到输出buffer中
                outBuf.write(encryptedBlock);
            }
        } catch (Exception e) {
        }

        return new String(Base64.getEncoder().encode(outBuf.toByteArray())); //ciphertext
    }
}