package com.yobo.yorxjava3.create;

/**
 * on 2020-04-09
 */
public interface YoObserver<T> {

    void onSubscribe();

    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}
