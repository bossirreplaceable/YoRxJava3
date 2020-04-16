package com.yobo.yorxjava3.map;

import com.yobo.yorxjava3.create.YoEmitter;
import com.yobo.yorxjava3.create.YoObservable;
import com.yobo.yorxjava3.create.YoObservableOnSubscribe;
import com.yobo.yorxjava3.create.YoObserver;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;

/**
 * Created by ZhangBoshi
 * on 2020-04-10
 */
public class TestMap {

    public static void main(String[] args) {

        // testNative();
        testYo();
    }

    private static void testNative() {

        Observable<Integer> o = Observable.<Integer>create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onComplete();
        }).window(3).flatMap(s->s);

        Observable<String> o1 = o.map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Throwable {
                return "ID:" + integer;
            }
        });

        o1.subscribe(new Observer<String>() {
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

    private static void testYo() {

        YoObservable o = YoObservable.<Integer> create(new YoObservableOnSubscribe() {
            @Override
            public void subscribe(YoEmitter e) {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        });
        YoObservable o1 = o.map(new YoFunction<Integer, String>() {
            @Override
            public String apply(Integer i) {
                return "ID:" + i;
            }
        });
        o1.subscribe(new YoObserver<String>() {
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
