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
    private static String rsaPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC79RaKfNgHkEgrh583VSXGI/uW4Y2mG3TjUZghMAEgdLR4nFOc42GF6nXICm5bqio5z4ai8lQ0o//AyAWBjbw0yDMEJRawHGFIp8/McFCOaqXOd2ho5CVAR6HeUNE7qfA7PVSLHpcTYK2m0WooULqkVsxOBUYieqxn8bivCfrK7a8VI16NqPrBrCHlMenqCHq5fOMV2HIl9kl7aKWR5xV07Be/A/wcvab2mi3cpfLYfZsU/nV4gcIFWu+Z/+t7xekxRMtYSoo4uQovzAdyM0INCjEGxtESp5eRrWPk8cGlTLXeTSYuMCXMjR/ReFxBtU1RR9nlOgslhoHXRkNQvJghAgMBAAECggEAekUIgdSPD/SLDk4ug+dPKGM2AkP+SoVn94yXw1cgFIX29NL1DYVZZq1MK3KlsdmzmGHo+s1be9cHw9gaxNl4CnoFvfeGl9AOdR49bofNz6eO7w38jOeFWBiBM0ctpXt6PH88JTgkHUXyESf/v/QsRkX0p/iEVF+rEf9MHgoG7MSukLkLWvDFrce/jsgW3iq7N4rqTAJsRLBPymWlMzoeObfYtTDTVVgjUq35jjivmhrX69omq/4Xg7UshrrJN6vFB6nHajTNDut7ilTUf9LuiIGoeI/YQ4B0yVCKpu5gbXW+Ha9lfA3CtIA78BMqWsDP/omYzQ97XLGz/MpOXc1fpQKBgQDbZ7LRWF1kOcdJ2aaXRwbRYt5XQcJXE8Tsfadja+aJe0Z2nZuLJtKlxEelc0PHWGQaQjXhbdQeFjVg6SzDPBt1htHaoXHi35EdwTOasyBAaCjvcJuz9KETe4QC+2JJnVA9VFDA96IK7hvATQjF3LMxo5j5osESACO6infNSMnnNwKBgQDbTp18DYBIKODd4/mhxxRQ3nCQGSkY2UCeUAFU1HfHt6FmRyBkrITtFnoEwzmUNlc7VRX8/FQQjJAS86Ar2tQN79vg7YlB6XJRywk/s285nJF1mSs8ITKYUuvuqdHovbl/Hh0dOcEQB18cLaegiwXyWIIjgLXAKoBiP9QXsip3ZwKBgGUsi4QH2IEYN7LxX+XQhe9WRSs9kq1u21cmlmK9ZgCtzQ9znD9ttqNkQJml7nJlazvEnm4GpFL/cLvwqoiZFhx7ABhubELvT07GQdKrmUq5/W50ogg43XJLedOaMne5yD+PuvFQ//NEW52wqEP9gtB4HVm4WInBcLhbk+UhFKG/AoGBAMle8WpkN0OLDZeRarmqxDPRXEhkYgEoE5aKXf6XrXicW74/VzY510CgH+BZpng6QXUAltUamNItT2mnfI5oO8ikUyDA3ATCp68FIPP1/g5HfqUdxL5MuVl24p7eHhl4hGh9rTwVuPnP2vxTy5S1svspXC3fVIQkQoh0lXpApY/HAoGASDQPiJEMBzAsaO+ftGe8U84QY12jf9EVwPxwTgP/LVIEepFOlMnl9fMhPtMOFEjSz2vv3ef9qTjW1hXsBUUzScKVHoxlKfEyRihXUy/s1QxHmE8oBDqiqnSeZTsxy0d/crzyyzMkvnq8zac2JRrANnrRYEZm4Xh3LYguGavibwA=";
    //公钥，填入
    private static String rsaPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu/UWinzYB5BIK4efN1UlxiP7luGNpht041GYITABIHS0eJxTnONhhep1yApuW6oqOc+GovJUNKP/wMgFgY28NMgzBCUWsBxhSKfPzHBQjmqlzndoaOQlQEeh3lDRO6nwOz1Uix6XE2CtptFqKFC6pFbMTgVGInqsZ/G4rwn6yu2vFSNejaj6wawh5THp6gh6uXzjFdhyJfZJe2ilkecVdOwXvwP8HL2m9pot3KXy2H2bFP51eIHCBVrvmf/re8XpMUTLWEqKOLkKL8wHcjNCDQoxBsbREqeXka1j5PHBpUy13k0mLjAlzI0f0XhcQbVNUUfZ5ToLJYaB10ZDULyYIQIDAQAB";
    
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

        //测试公钥加密
        String publicMac = publicEncrypt(str);
        //测试私钥解密
        if(StringUtils.isBlank(mac)){
            mac = publicMac;
        }
        privateDecode(mac);

        //测试私钥加密
        String privateMac = privateEncrypt(str);
        //测试公钥解密
        if(StringUtils.isBlank(mac)){
            mac = privateMac;
        }
        publicDecode(mac);
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
