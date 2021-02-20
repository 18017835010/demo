package com.unspay.hutool.crypto.des;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import org.apache.commons.lang3.StringUtils;

/**
 * User: ji.chen
 * Date: 2020/9/7
 * Time: 16:32
 * Description: DES对称加密测试类
 */
public class Demo {
    //明文，填入
    private static String str = "陈楫";
    //密问，填入
    private static String mac = "";
    
    public static void main(String[] args)  {

        //测试加密
        String key = Base64.encode(SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded());
        String publicMac = encrypt(str,key);
        if(StringUtils.isBlank(mac)){
           mac = publicMac;
        }
        decrypt(mac,key);
    }


    /**
     * DES加密
     */
    private static String encrypt(String str,String key){
        try {
            System.out.println("-------------key："+key);
            System.out.println("明文："+str);
            String mac = Base64.encode(SecureUtil.des(Base64.decode(key)).encrypt(str), CharsetUtil.UTF_8);
            System.out.println("密文："+mac);
            return mac;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * DES解密
     */
    private static String decrypt(String mac,String key){
        try {
            System.out.println("-------------key："+key);
            System.out.println("密文："+mac);
            String str = SecureUtil.des(Base64.decode(key)).decryptStr(Base64.decode(mac, CharsetUtil.UTF_8));
            System.out.println("明文："+str);
            return mac;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
