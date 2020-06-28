package com.example.designmodel.map;

import java.util.*;

public class TestHashMap {


    public static void main(String[] args) {
        Map<String, String> likedHashMap = new LinkedHashMap<>();
        likedHashMap.put("1111", "aaa");
        likedHashMap.put("222", "bbbb");
        likedHashMap.put("3333", "cccc");
        Set<Map.Entry<String, String>> set = likedHashMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println("-----------------");
        Map<String, String> map = new HashMap();
        map.put("1111", "aaa");
        map.put("222", "bbbb");
        map.put("3333", "cccc");
        Set<Map.Entry<String, String>> set2 = map.entrySet();
        for (Map.Entry<String, String> entry : set2) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println("-----------------");
        System.out.println(1 << 30);
        Map map1 = new LinkedHashMap(10, 1);
        map1.hashCode();


        for (; ; ) {
            System.out.println("for--");
        }
    }
}
