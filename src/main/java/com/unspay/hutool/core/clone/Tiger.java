package com.unspay.hutool.core.clone;

import cn.hutool.core.clone.Cloneable;
import cn.hutool.core.util.ObjectUtil;

import java.io.Serializable;

/**
 * User: ji.chen
 * Date: 2020/9/8
 * Time: 10:46
 * Description: 深克隆：对引用数据类型进行拷贝的时候，创建了一个新的对象，并且复制其内的成员变量
 */
public class Tiger implements Cloneable<Tiger>,Serializable {
    private String name = "wauwau";
    private int age = 2;

    @Override
    public Tiger clone() {
        return ObjectUtil.cloneByStream(this);
    }
}
