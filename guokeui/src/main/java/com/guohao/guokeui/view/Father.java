package com.guohao.guokeui.view;

public class Father {

    private int age;

    // 无参数的默认构造函数不需要子类实现
    public Father(){

    }

    // 有参数的构造函数需要子类实现
    public Father(int age) {
        this.age = age;
    }
}
