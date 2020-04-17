package com.yobo.yorxjava3.window;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by ZhangBoshi
 * on 2020-04-16
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class TestBuffer {

    public static void main(String[] args) {

        testBuffer3();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * buffer(3)
     * 输出结果：
     * [1, 2, 3]
     * [4, 5, 6]
     * [7, 8, 9]
     * [10]
     */
    private static void testbuffer() {
        Observable.range(1, 10)
                .buffer(3)
                .subscribe((List<Integer> list) -> {
                           System.out.println(list);
                });
    }
    /**
     * buffer(1，2)  每次跳过元素的数量
     * 输出结果：
     *      [1]
     *      [3]
     *      [5]
     *      [7]
     *      [9]
     */
    private static void testbuffer1() {
        Observable.range(1, 10)
                .buffer(1,2)
                .subscribe((List<Integer> list) -> {
                    System.out.println(list);
                });
    }
    /**
     *  buffer(1,TimeUnit.SECONDS)  1秒内缓冲的元素转换成List。
     *  输出结果：
     *  [0, 1, 2]
     *  [3, 4, 5]
     *  [6, 7, 8]
     *  [9, 10, 11, 12]
     *  [13, 14, 15]
     */
    private static void testbuffer2() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .buffer(1,TimeUnit.SECONDS)
                .take(5)
                .subscribe((List<Long> list) -> {
                    System.out.println(list);
                });
    }


    /**
     *
     */
    private static long startTime=System.currentTimeMillis();
    private static void testBuffer3() {

        Observable<Duration> insideBusinessHours=Observable.interval(5,TimeUnit.SECONDS)
                .filter(x->isBusinessHour())
                .map(x->Duration.ofMillis(1000));

        Observable<Duration> outsideBusinessHours=Observable.interval(10,TimeUnit.SECONDS)
                .filter(x->!isBusinessHour())
                .map(x->Duration.ofMillis(1000));

        Observable<Duration> openings=Observable.merge(insideBusinessHours,outsideBusinessHours)
                .doOnNext(s-> System.out.println("时间间隔："+s.toMillis()+"毫秒"));

        Observable.interval(500, TimeUnit.MILLISECONDS)
                .window(openings,duration -> Observable.empty().delay(duration.toMillis(),TimeUnit.MILLISECONDS))
//                .flatMap(s->s)
                .subscribe(new Observer<Observable<Long>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Observable<Long> longObservable) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 判断是否是  9:00 --17:00 之内的时间段
     */
    private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_END = LocalTime.of(17, 0);
    private static boolean isBusinessHour(){
        ZonedDateTime zdt=ZonedDateTime.now();
        LocalTime localTime=zdt.toLocalTime();
        return !localTime.isBefore(BUSINESS_START)&&!localTime.isAfter(BUSINESS_END);
    }


}
