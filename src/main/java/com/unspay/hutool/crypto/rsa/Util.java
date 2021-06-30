package com.unspay.hutool.crypto.rsa;

import cn.hutool.core.codec.Base64;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;

/**
 * User: ji.chen
 * Date: 2020/12/8
 * Time: 18:37
 * Description: 通过RSA证书文件获取公私钥
 * 首先需要通过jdk的keytool生成秘钥库等，需确保机器中安装有jdk
 * 0.在本代码环境下,执行1命令时,秘钥库的密码和证书的密码须一致，请自行修改路径
 *   请确保路径下无名为mykeystore.keystore的文件，否则可能会报密码错等
 *   windows：
 * 1.keytool -genkey -alias mykey -keyalg RSA -keystore D:/workSpace/JMeterWorkSpace/mykey/mykeystore.keystore -keysize 1024 -validity 3650
 * 2.keytool -export -alias mykey -keystore D:/workSpace/JMeterWorkSpace/mykey/mykeystore.keystore -file  D:/workSpace/JMeterWorkSpace/mykey/mykey.cer
 *
 * mac：
 * 1.keytool -genkey -alias mykey -keyalg RSA -keystore D:/workSpace/JMeterWorkSpace/mykey/mykeystore.keystore -keysize 1024 -validity 3650
 * 2.keytool -export -alias mykey -keystore D:/workSpace/JMeterWorkSpace/mykey/mykeystore.keystore -file  D:/workSpace/JMeterWorkSpace/mykey/mykey.cer
 *
 * 3.代码中的秘钥库密码为123456,请自行修改
 *
 */
public class Util {

    public static void main(String[] args) {

        //windows
//        String path = "D:/workSpace/JMeterWorkSpace/mykey/";

        //mac
        String path = "/Users/chenji/Documents/workspace/JMeterWorkSpace/mykey/";

        String keyStoreFile = "mykeystore.keystore";
        String passwd = "123456";
        String keyAlias = "mykey";
        String pfxFile = "mykey.pfx";
        String cerFile = "mykey.cer";

//        System.out.println("请确保已执行完注释中的两条命令再执行本代码\n\n");
//        System.out.println("1.开始生成PFX文件");
//        coverToPfx(path + keyStoreFile, passwd, keyAlias, path + pfxFile);

//        System.out.println("2.开始提取.cer中的公钥字符串");
//        String cerPub = getCerPubStr(path + cerFile);
//        System.out.println("从.cer文件中提取的公钥字符串如下:");
//        System.out.println(cerPub);

        System.out.println("3.开始提取.pfx中的公钥字符串");
        String pfxPub = getPfxPubStr(path + pfxFile,passwd);
        System.out.println("从.pfx文件中提取的公钥字符串如下:");
        System.out.println(pfxPub);

        System.out.println("4.开始提取.pfx中的私钥字符串");
        String pfxPri = getPfxPriStr(path + pfxFile,passwd);
        System.out.println("从.pfx文件中提取的私钥字符串如下:");
        System.out.println(pfxPri);
    }

    //1生成pfx文件
    public static void coverToPfx(String keyStoreFile, String passwd, String keyAlias, String pfxFile) {
        try {
            KeyStore inputKeyStore = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(keyStoreFile);
            char[] nPassword = null;
            if ((passwd == null)
                    || passwd.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = passwd.toCharArray();
            }
            inputKeyStore.load(fis, nPassword);
            fis.close();
            KeyStore outputKeyStore = KeyStore.getInstance("PKCS12");
            outputKeyStore.load(null, passwd.toCharArray());

//            System.out.println("alias=[" + keyAlias + "]");
            if (inputKeyStore.isKeyEntry(keyAlias)) {
                Key key = inputKeyStore.getKey(keyAlias, passwd.toCharArray());
                Certificate[] certChain = inputKeyStore
                        .getCertificateChain(keyAlias);
                outputKeyStore.setKeyEntry(keyAlias, key, passwd
                        .toCharArray(), certChain);
            }

            FileOutputStream out = new FileOutputStream(pfxFile);
            outputKeyStore.store(out, nPassword);
            out.close();
            System.out.println("已生成PFX文件" + pfxFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //2获取.cer公钥字符串
    public static String getCerPubStr(String cerFile) {
        String key = "";
        // 读取证书文件
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            FileInputStream in = new FileInputStream(cerFile);

            //生成一个证书对象并使用从输入流 inStream 中读取的数据对它进行初始化。
            Certificate c = cf.generateCertificate(in);
            PublicKey publicKey = c.getPublicKey();
            key = Base64.encode(publicKey.getEncoded());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return key;
    }

    //3获取.pfx公钥字符串
    public static String getPfxPubStr(String pfxFile,String passwd) {
        String key = "";
        // 读取证书文件
        try {
            FileInputStream fis = new FileInputStream(pfxFile);
            char[] nPassword = null;
            if ((passwd == null) || passwd.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = passwd.toCharArray();
            }
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(fis, nPassword);
            fis.close();
            Enumeration enumas = ks.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = (String) enumas.nextElement();
            }
            //获取公钥
            Certificate cert = ks.getCertificate(keyAlias);
            PublicKey pubkey = cert.getPublicKey();
            key = Base64.encode(pubkey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    //3获取.pfx私钥字符串
    public static String getPfxPriStr(String pfxFile,String passwd) {
        String key = "";
        // 读取证书文件
        try {
            FileInputStream fis = new FileInputStream(pfxFile);
            char[] nPassword = null;
            if ((passwd == null) || passwd.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = passwd.toCharArray();
            }
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(fis, nPassword);
            fis.close();
            Enumeration enumas = ks.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = (String) enumas.nextElement();
            }
            //获取私钥
            PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
            key = Base64.encode(prikey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }
}
