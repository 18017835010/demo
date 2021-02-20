package com.unspay.hutool.crypto.rsa;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import org.apache.commons.lang3.StringUtils;

/**
 * User: ji.chen
 * Date: 2020/9/7
 * Time: 16:32
 * Description: RSA签名+非对称加密测试类
 */
public class Demo {
    //明文，填入
    private static String str = "123456";
    //密问，填入
    private static String mac = "";
    //私钥，填入
    private static String rsaPrivateKey = "";
    //公钥，填入
    private static String rsaPublicKey = "";
    
    public static void main(String[] args)  {
        if(StringUtils.isBlank(rsaPrivateKey) && StringUtils.isBlank(rsaPublicKey)){
            Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA);
            rsaPrivateKey = Base64.encode(sign.getPrivateKey().getEncoded());
            rsaPublicKey = Base64.encode(sign.getPublicKey().getEncoded());
        }

        //测试加签
        String signMac = sign(str);
        //测试验签
        if(StringUtils.isBlank(mac)){
            mac = signMac;
        }
        verify(str,mac);

//        //测试公钥加密
//        String publicMac = publicEncrypt(str);
//        //测试私钥解密
//        if(StringUtils.isBlank(mac)){
//            mac = publicMac;
//        }
//        privateDecode(mac);

//        //测试私钥加密
//        String privateMac = privateEncrypt(str);
//        //测试公钥解密
//        if(StringUtils.isBlank(mac)){
//            mac = privateMac;
//        }
//        publicDecode(mac);
    }


    /**
     * RSA私钥加签
     */
    private static String sign(String str){
        try {
            System.out.println("rsaPrivateKey---------" + rsaPrivateKey + "----------");
            System.out.println("rsaPublicKey---------" + rsaPublicKey + "---------");
            System.out.println("明文："+str);
            byte[] bt = SecureUtil.sign(SignAlgorithm.SHA256withRSA, rsaPrivateKey, rsaPublicKey).sign(str.getBytes());
            String mac = Base64.encode(bt);
            System.out.println("密文："+mac);
            return mac;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA公钥验签
     */
    private static void verify(String str,String mac){
        try {
            System.out.println("rsaPrivateKey---------" + rsaPrivateKey + "----------");
            System.out.println("rsaPublicKey---------" + rsaPublicKey + "---------");
            System.out.println("明文："+str+",密文："+mac);
            boolean flag = SecureUtil.sign(SignAlgorithm.SHA256withRSA, rsaPrivateKey, rsaPublicKey).verify(str.getBytes(),Base64.decode(mac));
            System.out.println("验签结果："+flag);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * RSA加密，公钥加密
     */
    private static String publicEncrypt(String str){
        try {
            System.out.println("rsaPrivateKey---------" + rsaPrivateKey + "----------");
            System.out.println("rsaPublicKey---------" + rsaPublicKey + "---------");
            System.out.println("明文："+str);
            byte[] bt = SecureUtil.rsa(null,rsaPublicKey).encrypt(str, KeyType.PublicKey);
            String mac = Base64.encode(bt);
            System.out.println("密文："+mac);
            return mac;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA解密，私钥解密
     */
    private static void privateDecode(String mac){
        try {
            System.out.println("rsaPrivateKey---------" + rsaPrivateKey + "----------");
            System.out.println("rsaPublicKey---------" + rsaPublicKey + "---------");
            System.out.println("密文："+mac);
            String str = SecureUtil.rsa(rsaPrivateKey,null).decryptStr(mac,KeyType.PrivateKey);
            System.out.println("明文："+str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * RSA解密，私钥加密
     */
    private static String privateEncrypt(String str){
        try {
            System.out.println("rsaPrivateKey---------" + rsaPrivateKey + "----------");
            System.out.println("rsaPublicKey---------" + rsaPublicKey + "---------");
            System.out.println("明文："+str);
            byte[] bt = SecureUtil.rsa(rsaPrivateKey,null).encrypt(str, KeyType.PrivateKey);
            String mac = Base64.encode(bt);
            System.out.println("密文："+mac);
            return mac;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * RSA加密，公钥解密
     */
    private static void publicDecode(String mac){
        try {
            System.out.println("rsaPrivateKey---------" + rsaPrivateKey + "----------");
            System.out.println("rsaPublicKey---------" + rsaPublicKey + "---------");
            System.out.println("密文："+mac);
            String str = SecureUtil.rsa(null,rsaPublicKey).decryptStr(mac,KeyType.PublicKey);
            System.out.println("明文："+str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
