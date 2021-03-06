package com.yobo.yorxjava3.create;

import com.yobo.yorxjava3.flatMap.YoObservableFlatMap;
import com.yobo.yorxjava3.map.YoFunction;
import com.yobo.yorxjava3.map.YoObservableMap;
import com.yobo.yorxjava3.subscribeOn.YoObservableObserveOn;
import com.yobo.yorxjava3.subscribeOn.YoObservableSubscribeOn;

import java.util.concurrent.Executor;

/**
 * Created by ZhangBoshi
 * on 2020-04-09
 */

public abstract class YoObservable<T> implements YoObservableSource<T> {
    @Override
    public void subscribe(YoObserver<? super T> yoObserver) {
        subscribeActual(yoObserver);
    }

    public static YoObservable create(YoObservableOnSubscribe source) {
        return new YoObservableCreate(source);
    }

    public final <R> YoObservable<R> map(YoFunction<? super T, ? extends R> mapper) {
        return new YoObservableMap<>(this, mapper);
    }

    public final YoObservable<T> subscribeOn(Executor scheduler) {
        return new YoObservableSubscribeOn<>(this, scheduler);
    }

    public final YoObservable<T> observeOn(Executor scheduler) {
        return new YoObservableObserveOn<>(this, scheduler);
    }

    public final <R> YoObservable<R> flatMap(
            YoFunction<? super T, ? extends YoObservableSource<? extends R>> mapper) {
        return new YoObservableFlatMap<>(this, mapper);
    }

    protected abstract void subscribeActual(YoObserver<? super T> observer);
}
