package com.yobo.yorxjava3.create;

import com.yobo.yorxjava3.map.YoFunction;
import com.yobo.yorxjava3.map.YoObservableMap;

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
        return new YoObservableMap<T,R>(this, mapper);
    }
    protected abstract void subscribeActual(YoObserver<? super T> observer);
}
