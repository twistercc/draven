package com.example.designmodel;

import java.util.HashSet;
import java.util.Set;

public class HashCodeTest {
    public static void main(String[] args) {
        Personsa person1 = new Personsa("wulinfei", 1);
        Personsa person2 = new Personsa("wulinfei", 1);
        System.out.println("stu:" + person1.equals(person2));
        Set<Personsa> set = new HashSet<>();
        set.add(person1);
        System.out.println("s1 hashCode:" + person1.hashCode());
        System.out.println("add s1 size:" + set.size());
        set.add(person2);
        System.out.println("s2 hashCode:" + person2.hashCode());
        System.out.println("add s2 size::" + set.size());
    }
}