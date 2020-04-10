package com.yobo.yorxjava3.create;

/**
 * on 2020-04-09
 */
public class YoObservableCreate<T> extends YoObservable<T> {
    private final YoObservableOnSubscribe<T> source;

    public YoObservableCreate(YoObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(YoObserver<? super T> observer) {
        YoEmitter emitter = new YoCreateEmitter(observer);

        source.subscribe(emitter);
    }

    static final class YoCreateEmitter<T> implements YoEmitter<T> {

        final YoObserver<? super T> observer;

        YoCreateEmitter(YoObserver<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T t) {
            if (!isDisposed()) {
                observer.onNext(t);
            }
        }

        @Override
        public void onError(Throwable t) {
            if (!isDisposed()) {
                try {
                    observer.onError(t);
                } finally {
                    dispose();
                }
            }
        }

        @Override
        public void onComplete() {
            if (!isDisposed()) {
                try {
                    observer.onComplete();
                } finally {
                    dispose();
                }
            }
        }

        void dispose() {
            // 取消订阅
        }

        @Override
        public boolean isDisposed() {
            return false;
        }
    }

}
