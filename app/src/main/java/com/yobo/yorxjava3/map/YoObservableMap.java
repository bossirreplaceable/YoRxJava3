package com.yobo.yorxjava3.map;

import com.yobo.yorxjava3.create.YoObservable;
import com.yobo.yorxjava3.create.YoObservableSource;
import com.yobo.yorxjava3.create.YoObserver;

/**
 * Created by ZhangBoshi
 * on 2020-04-10
 */
public class YoObservableMap<T, R> extends YoObservable<R> {

    private final YoFunction<? super T, ? extends R> function;
    private final YoObservableSource<T> source;

    public YoObservableMap(YoObservableSource<T> source,
            YoFunction<? super T, ? extends R> function) {
        this.function = function;
        this.source = source;
    }

    @Override
    protected void subscribeActual(YoObserver<? super R> observer) {
        source.subscribe(new YoMapObserver<T, R>(observer, function));
    }

    static final class YoMapObserver<T, U> implements YoObserver<T> {
        final YoFunction<? super T, ? extends U> mapper;
        final YoObserver<? super U> downstream;
        boolean done;

        YoMapObserver(YoObserver<? super U> actual, YoFunction<? super T, ? extends U> mapper) {
            this.downstream = actual;
            this.mapper = mapper;
        }

        @Override
        public void onSubscribe() {
            downstream.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
            U v = mapper.apply(t);
            downstream.onNext(v);
        }

        @Override
        public void onError(Throwable e) {
            done = true;
        }

        @Override
        public void onComplete() {
            downstream.onComplete();
            done = true;
        }
    }
}
