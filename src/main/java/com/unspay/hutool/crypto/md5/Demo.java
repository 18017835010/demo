package com.unspay.hutool.crypto.md5;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;

/**
 * User: ji.chen
 * Date: 2020/9/7
 * Time: 16:32
 * Description: MD5算法测试类
 */
public class Demo {
    //明文，填入
    private static String str = "123456";
    //密问，填入
    private static String mac = "4QRCOUM6WAU+VUBX8G+IPG==";
    
    public static void main(String[] args)  {

        //测试加密
        String publicMac = encrypt(str);
        System.out.println(publicMac.equals(mac));
    }


    /**
     * MD5加密
     */
    private static String encrypt(String str){
        try {
            System.out.println("明文："+str);
            String mac = Base64.encode(SecureUtil.md5().digest(str)).toUpperCase();
            System.out.println("密文："+mac);
            return mac;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
