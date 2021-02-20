package com.unspay.test;

/**
 * User: ji.chen
 * Date: 2020/12/14
 * Time: 16:17
 * Description: No Description
 */
public class Test1 {
    //计算总共有多少列表
    private static int i = 0;
    //保存所有排列结果
    String resultstr = "";

    //将数组转换成字符串
    private String getList(int[] a) {
        String result = "";
        for (int len = 0; len < a.length; len++)
            result = result + a[len];
        return result;
    }

    /*
    传递去除元素的数组
    */
    private int[] DelArray(int[] a, int pos) {
        if (pos > a.length - 1) return a;
        int len = a.length - 1;
        int[] newArray = new int[len];
        for (int i = 0; i < len + 1; i++) {
            if (i < pos) newArray[i] = a[i];
            if (i > pos) newArray[i - 1] = a[i];
        }
        return newArray;
    }

    void GetList(String prestr, int[] a) {
        //只有一个数组元素的时候就返回结果了
        if (a.length < 2) {
            String result = prestr + getList(a); //结果
            resultstr = resultstr + result + ",";
            i++;
            return;
        }

        //关键是下面一句，递归
        for (int i = 0; i < a.length; i++)
            GetList(prestr + a[i], DelArray(a, i));
    }


    public static void main(String[] args) {
        Test1 printlisttest1 = new Test1();
        int[] a = {1, 2, 3};
        printlisttest1.GetList("", a); //得到所有排列
        System.out.println(printlisttest1.resultstr);
        System.out.println("共有" + printlisttest1.i + "种排列");
    }
}
