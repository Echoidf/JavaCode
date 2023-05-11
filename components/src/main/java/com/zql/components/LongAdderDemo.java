package com.zql.components;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author：zql
 * @date: 2023/5/11
 */
public class LongAdderDemo {
    public static void main(String[] args) {
        int corePollSize = 4;
        int maxPollSize = 8;
        long keepAliveTime = 0L;
        BlockingQueue blockingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory factory = new CustomizableThreadFactory("ThreadName=");
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

        //创建线程池
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(corePollSize, maxPollSize, keepAliveTime, TimeUnit.SECONDS, blockingQueue, factory, handler);

        //设置线程并发量
        final Semaphore semaphore = new Semaphore(maxPollSize);

        //线程同步类
        final CountDownLatch countDownLatch = new CountDownLatch(maxPollSize);

        LongAdder longAdder = new LongAdder();

        try{
            for (int i = 0; i < maxPollSize; i++) {
                threadPoolExecutor.submit(() -> {
                    try{
                        semaphore.acquire();
                        synchronized (LongAdderDemo.class){
                            longAdder.increment();
                            System.out.println("线程"+Thread.currentThread().getName() + " longAdder=" + longAdder);
                        }
                        semaphore.release();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    //当前线程执行完才释放
                    countDownLatch.countDown();
                });
            }

            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPoolExecutor.shutdown();
        }

        System.out.println("longAdder = " + longAdder);
    }
}
