package com.yobo.yorxjava3.subscribeOn;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by ZhangBoshi
 * on 2020-04-13
 */
public class TestSubscribeOn {

    public static void main(String[] args) {

        testOn();
        try {
            TimeUnit.SECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testOn() {
        Observable<String> o = Observable.<String> create(s -> {
            log("开始发送消息");
            s.onNext("A");
            s.onNext("B");
            s.onNext("C");
            s.onComplete();
        }).subscribeOn(Schedulers.io())
          .doOnNext(s -> log("发送->" + s))
                .map(s -> s + "1")
                .subscribeOn(Schedulers.newThread())
                .doOnNext(s -> log("发送1->" + s)) ;

        o.observeOn(Schedulers.newThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                log("我刚被订阅上，开始初始化工作！");
            }

            @Override
            public void onNext(@NonNull String s) {
                log("我接收到：" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                log("错误：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                log("接受完成！");
            }
        });

    }

    private static void log(String msg) {
        System.out.println(Thread.currentThread().getName() + ":" + msg);
    }
}
