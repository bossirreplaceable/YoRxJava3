package com.yobo.yorxjava3.flatMap;

import com.yobo.yorxjava3.create.YoObservable;
import com.yobo.yorxjava3.create.YoObservableSource;
import com.yobo.yorxjava3.create.YoObserver;
import com.yobo.yorxjava3.map.YoFunction;
import com.yobo.yorxjava3.subscribeOn.LinkedQueue;

/**
 * Created by ZhangBoshi
 * on 2020-04-15
 */








public class YoObservableFlatMap<T, U> extends YoObservable<U> {

    private final YoFunction<? super T, ? extends YoObservableSource<? extends U>> mapper;
    private final YoObservableSource<T> source;

    public YoObservableFlatMap(YoObservableSource<T> source,
            YoFunction<? super T, ? extends YoObservableSource<? extends U>> mapper) {
        this.mapper = mapper;
        this.source = source;
    }
    @Override
    protected void subscribeActual(YoObserver<? super U> observer) {

        source.subscribe(new MergeObserver<>(observer, mapper));

    }

    static final class MergeObserver<T, U> implements YoObserver<T> {

        final YoObserver<? super U> downstream;
        final YoFunction<? super T, ? extends YoObservableSource<? extends U>> mapper;
        LinkedQueue<U> queue = new LinkedQueue<>();

        volatile boolean done;
        volatile boolean disposed;
        int wip;
        long uniqueId;

        MergeObserver(YoObserver<? super U> actual,
                YoFunction<? super T, ? extends YoObservableSource<? extends U>> mapper) {
            this.downstream = actual;
            this.mapper = mapper;
        }
        @Override
        public void onSubscribe() {
            downstream.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            // safeguard against misbehaving sources
            if (done) {
                return;
            }
            YoObservableSource<? extends U> p;
            try {
                p = (YoObservableSource<? extends U>) mapper.apply(t);
            } catch (Throwable e) {
                onError(e);
                return;
            }

            subscribeInner(p);
        }

        void subscribeInner(YoObservableSource<? extends U> p) {
            InnerObserver<T, U> inner = new InnerObserver<>(this, uniqueId++);
            p.subscribe(inner);
        }
        @Override
        public void onError(Throwable t) {
            if (done) {
                return;
            }
            done = true;
            downstream.onError(t);
        }
        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            done = true;
            downstream.onComplete();
        }
        void drain(U t) {
            downstream.onNext(t);
        }
    }

    static final class InnerObserver<T, U> implements YoObserver<U> {

        final long id;
        final MergeObserver<T, U> parent;
        volatile boolean done;

        InnerObserver(MergeObserver<T, U> parent, long id) {
            this.id = id;
            this.parent = parent;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(U t) {
            parent.drain(t);
        }

        @Override
        public void onError(Throwable t) {
            done = true;
            parent.onError(t);
        }

        @Override
        public void onComplete() {
            done = true;
            parent.onComplete();
        }

    }

}
