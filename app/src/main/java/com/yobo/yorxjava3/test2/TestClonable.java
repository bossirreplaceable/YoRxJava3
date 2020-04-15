package com.yobo.yorxjava3.test2;

/**
 * Created by ZhangBoshi
 * on 2020-04-14
 */
public class TestClonable {

    String name;

    public TestClonable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {

        TestClonable c=new TestClonable("A");


        TestClonable c1=null;
        try {
            c1= (TestClonable) c.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        System.out.println("equal"+(c==c1));


    }
}
