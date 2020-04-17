package com.yobo.yorxjava3.window;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by ZhangBoshi
 * on 2020-04-16
 */
public class TestDebounce {


    public static void main(String[] args) {

        test();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static void test() {

        /**
         * 每个值都会输出
         */
//        Observable.interval(500, TimeUnit.MILLISECONDS)
//                .debounce(100,TimeUnit.MILLISECONDS)
//                .subscribe(s-> System.out.println("输出1="+s));
        /**
         * 没有任何值输出
         */
        Observable.interval(50, TimeUnit.MILLISECONDS)
                .debounce(100,TimeUnit.MILLISECONDS)
                .subscribe(s-> System.out.println("输出2="+s));

        timeDebounce(Observable.interval(50, TimeUnit.MILLISECONDS))
                .subscribe(s-> System.out.println("输出3="+s)) ;

    }

    private static  Observable<Long> timeDebounce(Observable<Long> upStream){
        Observable<Long> onTimeout=upStream
                .take(1)
                .concatWith(Observable.defer(()->timeDebounce(upStream)));
        return upStream.debounce(100,TimeUnit.MILLISECONDS)
                .timeout(1,TimeUnit.SECONDS,onTimeout);
    }


}
