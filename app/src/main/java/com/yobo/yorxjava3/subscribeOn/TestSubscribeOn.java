package com.yobo.yorxjava3.subscribeOn;

import com.yobo.yorxjava3.create.YoEmitter;
import com.yobo.yorxjava3.create.YoObservable;
import com.yobo.yorxjava3.create.YoObservableOnSubscribe;
import com.yobo.yorxjava3.create.YoObserver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by ZhangBoshi
 * on 2020-04-13
 */
public class TestSubscribeOn {

    public static void main(String[] args) {

        // testOn();
        testYo();
        try {
            TimeUnit.SECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testOn() {
        Scheduler scheduler = new Scheduler() {
            @Override
            public @NonNull Worker createWorker() {
                return new Worker() {
                    @Override
                    public @NonNull Disposable schedule(@NonNull Runnable run, long delay,
                            @NonNull TimeUnit unit) {

                        ScheduledExecutorService executor = Executors
                                .newSingleThreadScheduledExecutor();
                        executor.schedule(run, delay, unit);
                        return null;
                    }

                    @Override
                    public void dispose() {
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                };
            }
        };
        Observable<String> o = Observable.<String> create(s -> {
            log("开始发送消息");
            s.onNext("A");
            s.onNext("B");
            s.onNext("C");
            s.onComplete();
        }).doOnNext(s -> log("发送->" + s)).subscribeOn(scheduler).observeOn(Schedulers.io())
                .map(s -> s + "1").subscribeOn(Schedulers.newThread())
                .doOnNext(s -> log("发送1->" + s));
        o.subscribe(new Observer<String>() {
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
    //

    private static void testYo() {
        YoObservable yo1 = YoObservable.create(new YoObservableOnSubscribe<String>() {
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
        YoObservable yo2 = yo1.subscribeOn(Executors.newSingleThreadExecutor())
                              .observeOn(Executors.newSingleThreadExecutor());

        yo2.subscribe(new YoObserver<String>() {
            @Override
            public void onSubscribe() {
                log("Yo我刚被订阅上，开始初始化工作！");
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













