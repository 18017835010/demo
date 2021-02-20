package com.unspay.hutool.core.clone;

import cn.hutool.core.clone.CloneRuntimeException;
import cn.hutool.core.clone.Cloneable;

/**
 * User: ji.chen
 * Date: 2020/9/8
 * Time: 10:40
 * Description: 浅克隆:只对基本数据类型进行了拷贝，而对引用数据类型只是进行了引用的传递
 */
public class Cat implements Cloneable<Cat> {
    private String name = "miaomiao";
    private int age = 2;

    @Override
    public Cat clone() {
        try {
            return (Cat) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneRuntimeException(e);
        }
    }
}
