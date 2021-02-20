package com.unspay.hutool.crypto.dsa;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import org.apache.commons.lang3.StringUtils;

/**
 * User: ji.chen
 * Date: 2020/9/7
 * Time: 16:32
 * Description: DSA非对称加密测试类
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
            Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withECDSA);
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
    }


    /**
     * DSA私钥加签
     */
    private static String sign(String str){
        try {
            System.out.println("rsaPrivateKey---------" + rsaPrivateKey + "----------");
            System.out.println("rsaPublicKey---------" + rsaPublicKey + "---------");
            System.out.println("明文："+str);
            byte[] bt = SecureUtil.sign(SignAlgorithm.SHA256withECDSA, rsaPrivateKey, rsaPublicKey).sign(str.getBytes());
            String mac = Base64.encode(bt);
            System.out.println("密文："+mac);
            return mac;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DSA公钥验签
     */
    private static void verify(String str,String mac){
        try {
            System.out.println("rsaPrivateKey---------" + rsaPrivateKey + "----------");
            System.out.println("rsaPublicKey---------" + rsaPublicKey + "---------");
            System.out.println("明文："+str+",密文："+mac);
            boolean flag = SecureUtil.sign(SignAlgorithm.SHA256withECDSA, rsaPrivateKey, rsaPublicKey).verify(str.getBytes(),Base64.decode(mac));
            System.out.println("验签结果："+flag);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
