package com.yobo.yorxjava3.create;

/**
 * on 2020-04-09
 */
public interface YoObservableOnSubscribe<T> {

    void subscribe(YoEmitter<T> emitter);
}
