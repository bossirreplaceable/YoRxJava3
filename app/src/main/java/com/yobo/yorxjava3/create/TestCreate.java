package com.yobo.yorxjava3.create;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by ZhangBoshi
 * on 2020-04-09
 */
public class TestCreate {

    public static void main(String[] args) {

//        testCreate();
         testYoCreate();
    }

    private static void testCreate() {

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Throwable {
                log("开始发送消息！");
                e.onNext("A");
                e.onNext("B");
                e.onNext("C");
                e.onComplete();
            }
        }).doOnNext(s -> log("发送消息：" + s));

        observable.subscribe(new Observer<String>() {
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

    private static void testYoCreate() {

        YoObservable yoObservable = YoObservable.create(new YoObservableOnSubscribe<String>() {
            @Override
            public void subscribe(YoEmitter<String> e) {
                log("发送消息：A");
                e.onNext("A");
                log("发送消息：B");
                e.onNext("B");
                log("发送消息：C");
                e.onNext("C");
                e.onComplete();
            }
        });

        yoObservable.subscribe(new YoObserver<String>() {
            @Override
            public void onSubscribe() {
                log("我刚被订阅上，开始初始化工作！");
            }

            @Override
            public void onNext(String s) {
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
