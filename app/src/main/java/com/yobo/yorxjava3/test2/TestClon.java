package com.yobo.yorxjava3.test2;

/**
 * Created by ZhangBoshi
 * on 2020-04-14
 */
public class TestClon implements Cloneable{

    String name;

    public TestClon(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {

        TestClon c=new TestClon("A");
        TestClon c1=null;

        try {
             c1= (TestClon) c.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("c ="+c);
        System.out.println("c1="+c1);

        System.out.println("==   :"+(c==c1));
        System.out.println("equal:"+(c.equals(c1)));

    }

   
}
