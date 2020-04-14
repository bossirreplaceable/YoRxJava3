package com.yobo.yorxjava3.subscribeOn;

import com.yobo.yorxjava3.create.YoObservable;
import com.yobo.yorxjava3.create.YoObservableSource;
import com.yobo.yorxjava3.create.YoObserver;

import java.util.concurrent.Executor;

/**
 * Created by ZhangBoshi
 * on 2020-04-13
 */



public class YoObservableSubscribeOn<T> extends YoObservable<T> {

    private final Executor scheduler;
    private final YoObservableSource<T> source;

    public YoObservableSubscribeOn(YoObservableSource<T> source, Executor scheduler) {
        this.scheduler = scheduler;
        this.source = source;
    }

    @Override
    protected void subscribeActual(YoObserver<? super T> observer) {

        YoSubscribeOnObserver<T> parent = new YoSubscribeOnObserver<>(observer);

        observer.onSubscribe();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                source.subscribe(parent);
            }
        };

        scheduler.execute(runnable);

    }

    static final class YoSubscribeOnObserver<T> implements YoObserver<T> {

        final YoObserver<? super T> downstream;

        YoSubscribeOnObserver(YoObserver<? super T> downstream) {
            this.downstream = downstream;
        }

        @Override
        public void onSubscribe() {
            downstream.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            downstream.onNext(t);
        }

        @Override
        public void onError(Throwable t) {
            downstream.onError(t);
        }

        @Override
        public void onComplete() {
            downstream.onComplete();
        }

    }

}
