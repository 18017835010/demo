package com.unspay.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * User: ji.chen
 * Date: 2021/3/4
 * Time: 4:53 下午
 * Description: No Description
 */
public class FileUtil {

    /**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String txt2String(String filePath){
        String result = "";
        try{
            File file = new File("filePath");
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result = result + "\n" +s;
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
