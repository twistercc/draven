package com.example.multi_thread.Collection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * arrayList线程不安全的问题
 *
 * @author twister
 * @date 2020/8/5
 */
public class TestArrayList {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>(); // 不安全
//        List<String> list3 = Collections.synchronizedList(new ArrayList<>()); // 安全
//        List<String> list2 = new CopyOnWriteArrayList<>();  // 安全
        for(int i=0; i<3; i++){
            new Thread(()->{
                list1.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list1);
            }, String.valueOf(i)).start();

        }
        System.out.println();
    }
}
