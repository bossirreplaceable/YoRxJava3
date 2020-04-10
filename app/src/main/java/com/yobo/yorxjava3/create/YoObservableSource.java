package com.yobo.yorxjava3.create;

/**
 * on 2020-04-09
 */
public interface YoObservableSource<T> {
    void subscribe(YoObserver<? super T> yoObserver);
}
