package com.unspay.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ji.chen
 * Date: 2020/12/11
 * Time: 16:26
 * Description: No Description
 */
public class Test {



    public static void main(String[] args){
        int start = 1;//最小数
        int end = 9;//最大数
        int count = 9;//数字个数
        int sum = 45;//总数
        Map<String,String> res = new HashMap<String,String>();
        List<Integer> intList = new ArrayList<Integer>();
        util(start,end,1,count,sum,intList,res);
        for (Map.Entry<String, String> entry:res.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    private static void util(int start,int end,int i,int count,int sum,List<Integer> intList,Map<String,String> res){
        for (int a=start;a<=end;a++){
            if(intList.contains(a)){
                continue;
            }
            if(i<count){
                intList.add(a);
                util(start,end,i+1,count,sum,intList,res);
                intList.remove(intList.size()-1);
            }
            if(i==count){
                intList.add(a);
                int bsum = 0;
                for (int b:intList) {
                    bsum = bsum + b;
                }
                if(bsum == sum){
                    List<Integer> alist = new ArrayList<Integer>();
                    alist.addAll(intList);
                    alist.sort(Comparator.comparingInt(Integer::intValue));
                    String csum = "";
                    for (Integer c:alist) {
                        csum = csum+c;
                    }
                    if(!"1".equals(res.get(csum))){
                        res.put(csum,"1");
                    }
                }
                intList.remove(intList.size()-1);
            }
        }

    }
}
