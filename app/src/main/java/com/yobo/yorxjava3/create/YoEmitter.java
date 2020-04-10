package com.yobo.yorxjava3.create;

/**
 * on 2020-04-09
 */
public interface YoEmitter<T> {

    void onNext(T value);

    void onError(Throwable error);

    void onComplete();

    boolean isDisposed();
}
