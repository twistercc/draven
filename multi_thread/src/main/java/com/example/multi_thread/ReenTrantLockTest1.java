package com.example.multi_thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试lock可重入锁
 */

public class ReenTrantLockTest1 {
    private Lock lock = new ReentrantLock();  // 默认为非公平锁
//    private Lock lock2 = new ReentrantLock(true);// 公平锁



    public void myThread1(){
        lock.lock();
        System.out.println("myThread1() invoked");
//        lock.unlock();
    }

    public void myThread2(){
        lock.lock();
        System.out.println("myThread2() invoked");
        lock.unlock();
    }

    public void myThread3(){
        boolean res = false;
        try {
             res = lock.tryLock(800, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (res) {
            System.out.println("get the lock");
        } else {
            System.out.println("cannot get the lock");
        }
    }

    public static void main(String[] args) {
        ReenTrantLockTest1 testReenTrantLock = new ReenTrantLockTest1();

        Thread t1 = new Thread(()->{
            for (int i = 0; i < 10; i++) {
                testReenTrantLock.myThread1();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(()->{
            for (int i = 0; i < 10; i++) {
                testReenTrantLock.myThread2();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t3 = new Thread(()->{
            for (int i = 0; i < 10; i++) {
                testReenTrantLock.myThread3();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
//        t2.start();
        t3.start();

    }


    /**
     以上是对于Lock的一个简单使用，下面咱们对Lock与synchronized关健字在锁的处理上的重要差别进行梳理一下：

     1、锁的获取方法：前者是通过程序代码的方式由开发者手工获取，后者是通过JVM来获取（无需开发者干预）。

     2、具体实现方法：前者是通过Java代码的方式来实现，后者是通过JVM底层来实现（无需开发者关注）。

     3、锁的释放方法：前者务必通过unlock()方法在finally块中手工释放，后者是通过JVM来释放（无需开发者关注）。

     4、锁的具体类型：前者提供了多种，如公平锁、非公平锁，后者与前者均提供了可重入锁。其中公平锁和非公平锁在ReentrantLock源码中看到身影：
     */
}
