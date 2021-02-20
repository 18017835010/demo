package com.unspay.hutool.core.clone;

import cn.hutool.core.clone.CloneSupport;

/**
 * User: ji.chen
 * Date: 2020/9/8
 * Time: 10:43
 * Description: 浅克隆：只对基本数据类型进行了拷贝，而对引用数据类型只是进行了引用的传递
 */
public class Dog extends CloneSupport<Dog> {
    private String name = "wangwang";
    private int age = 3;
}
