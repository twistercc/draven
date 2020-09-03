package com.example.multi_thread.communicate;

import java.util.concurrent.Semaphore;

/***
 * 三个线程 a、b、c 并发运行，b,c 需要 a 线程的数据怎么实现？
 *
 * 一是使用纯Java API的Semaphore类来控制线程的等待和释放，
 * 二是使用Android提供的Handler消息机制。
 */
public class ThreadCommunication {
    private static int num;
    /**
     * 定义一个信号量，该类内部维持了多个线程锁，可以阻塞多个线程，释放多个线程，
     * 线程的阻塞和释放是通过 permit 概念来实现的
     * 线程通过 semaphore.acquire()方法获取 permit，如果当前 semaphore 有 permit 则分配给该线程，
     * 如果没有则阻塞该线程直到 semaphore
     * 调用 release（）方法释放 permit。
     * 构造函数中参数：permit（允许） 个数，
     */
    private static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) {

        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //模拟耗时操作之后初始化变量 num
                    Thread.sleep(1000);
                    num = 1;
                    //初始化完参数后释放两个 permit
                    semaphore.release(2);
                    System.out.println("A \t"+Thread.currentThread().getName() + "获取到 num 的值为：" + num);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取 permit，如果 semaphore 没有可用的 permit 则等待，如果有则消耗一个
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("B \t"+Thread.currentThread().getName() + "获取到 num 的值为：" + num);
            }
        });

        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取 permit，如果 semaphore 没有可用的 permit 则等待，如果有则消耗一个
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("C \t"+Thread.currentThread().getName() + "获取到 num 的值为：" + num);
            }
        });


        //同时开启 3 个线程
        threadA.start();
        threadB.start();
        threadC.start();

    }
}