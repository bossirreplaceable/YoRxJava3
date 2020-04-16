package com.yobo.yorxjava3.flatMap;

import com.yobo.yorxjava3.create.YoEmitter;
import com.yobo.yorxjava3.create.YoObservable;
import com.yobo.yorxjava3.create.YoObservableOnSubscribe;
import com.yobo.yorxjava3.create.YoObservableSource;
import com.yobo.yorxjava3.create.YoObserver;
import com.yobo.yorxjava3.map.YoFunction;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by ZhangBoshi
 * on 2020-04-15
 */
public class Test_flatMap extends AtomicInteger {

    public static void main(String[] args) {

        // Test_flatMap test=new Test_flatMap();
        // System.out.println("result1="+test.getAndIncrement());
        // System.out.println("result2="+test.getAndIncrement());
        // System.out.println("result3="+test.getAndIncrement());
        // System.out.println("result31="+test.getAndDecrement());
        // System.out.println("result4="+test.addAndGet(-1));
        // System.out.println("result5="+test.addAndGet(-1));
        // test();
        // try {
        // TimeUnit.SECONDS.sleep(3000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        testYo();
    }

    private static void test() {

        Observable<String> o = Observable.<String> create(s -> {
            log("开始发送消息");
            s.onNext("A");
            s.onNext("B");
            s.onNext("C");
            s.onComplete();
        }).doOnNext(s -> log("发送消息：" + s));

        Observable<String> o1 = o
                .flatMap(new Function<String, ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> apply(String s) throws Throwable {
                        return Observable.just(s).map(item -> item + "+1")
                                .subscribeOn(Schedulers.io());
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
    @SuppressWarnings("unchecked")
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
        YoObservable yo2 = yo1
                .flatMap(new YoFunction<String, YoObservableSource<? extends String>>() {
                    @Override
                    public YoObservableSource<? extends String> apply(String o) {
                        return YoObservable.create(s -> {
                            s.onNext(o + "1");
                            s.onNext(o + "2");
                        });
                    }
                });

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
