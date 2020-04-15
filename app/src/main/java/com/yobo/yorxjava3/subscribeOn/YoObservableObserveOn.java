package com.yobo.yorxjava3.subscribeOn;

import com.yobo.yorxjava3.create.YoObservable;
import com.yobo.yorxjava3.create.YoObservableSource;
import com.yobo.yorxjava3.create.YoObserver;

import java.util.concurrent.Executor;

/**
 * Created by ZhangBoshi
 * on 2020-04-14
 */



public class YoObservableObserveOn<T> extends YoObservable<T> {

    private final Executor scheduler;
    private final YoObservableSource<T> source;

    public YoObservableObserveOn(YoObservableSource<T> source, Executor scheduler) {
        this.scheduler = scheduler;
        this.source = source;
    }

    @Override
    protected void subscribeActual(YoObserver<? super T> observer) {

        source.subscribe(new YoObserveOnObserver<>(observer, scheduler));

    }

    static final class YoObserveOnObserver<T> implements YoObserver<T>, Runnable {

        final YoObserver<? super T> downstream;
        final Executor worker;
        Throwable error;

        LinkedQueue<T> queue = new LinkedQueue<>();

        YoObserveOnObserver(YoObserver<? super T> actual, Executor worker) {
            this.downstream = actual;
            this.worker = worker;
        }

        @Override
        public void onSubscribe() {
        }
        /**
         * schedule()应该在接受完所有数据调用，这里没做那种处理，
         * 所以就在onComplete中调用一次，这里不调用了
         */
        @Override
        public void onNext(T t) {
            queue.enqueue(t);
            schedule();
        }

        @Override
        public void onError(Throwable t) {
            error = t;
            schedule();
        }
        @Override
        public void onComplete() {
            schedule();
        }
        /**
         * 在自己的线程中运行这个Runnable
         */
        void schedule() {
            worker.execute(this);
        }
        @Override
        public void run() {
            drainNormal();
        }

        void drainNormal() {
            System.out.println("----------------------");
            YoObserver<? super T> a = downstream;
            if (checkTerminated(queue.isEmpty(), a)) {
                return;
            }
            for (;;) {
                T v;
                try {
                    v = queue.dequeue();
                } catch (Throwable ex) {
                    a.onComplete();
                    return;
                }
                boolean empty = v == null;

                if (empty) {
                    break;
                }
                a.onNext(v);
            }
        }
        boolean checkTerminated(boolean empty, YoObserver<? super T> a) {
            Throwable e = error;
            if (e != null) {
                a.onError(e);
                return true;
            } else if (empty) {
                a.onComplete();
                return true;
            }
            return false;
        }
    }

}
