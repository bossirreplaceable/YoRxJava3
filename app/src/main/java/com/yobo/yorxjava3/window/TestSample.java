package com.yobo.yorxjava3.window;


import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by ZhangBoshi
 * on 2020-04-16
 */
public class TestSample {


    public static void main(String[] args) {

        testSample();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1、interval()-从0开始，每一毫秒+1；
     * 2、sample()每隔1000毫秒从上游取一次值；
     * 预期结果：1000、2000、3000.......
     * 实际结果：1002、2000、3001、4000..... 和预期结果相符，每隔1000毫秒取一次值
     */
    private static void testSample() {

        Observable.interval(1, TimeUnit.MILLISECONDS)
                .sample(1,TimeUnit.SECONDS)
                .take(10)
                .subscribe(System.out::println);
    }
    /**
     * 1、throttleLast()和sample()是一样效果，都会取每个时间段的最后一个值；
     * 2、就比如：该例子，是从0开始递增的，但是第一次取的值，是等待一秒以后取的值。
     */
    private static void testThrottleLast() {
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .throttleLast(1,TimeUnit.SECONDS)
                .take(10)
                .subscribe(System.out::println);
    }
    /**
     * 1、throttleFirst()与throttleLast()相反，它会取每个时间段第一个值。
     * 2、所以这里的结果应该是：0、1000、2000、3000.......
     */
    private static void testThrottleFirst() {
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .throttleFirst(1,TimeUnit.SECONDS)
                .take(10)
                .subscribe(System.out::println);
    }


}
