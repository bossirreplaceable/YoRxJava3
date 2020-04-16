package com.yobo.yorxjava3.window;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by ZhangBoshi
 * on 2020-04-16
 */
public class TestBuffer {


    public static void main(String[] args) {

        testbuffer();

    }

    /**
     *
     */
    private static void testbuffer() {

        Observable.range(1,10)
                .buffer(3)
                .subscribe((List<Integer> list)->{
                    System.out.println(list);
                });
    }
}
