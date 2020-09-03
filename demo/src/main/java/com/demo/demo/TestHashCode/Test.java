package com.demo.demo.TestHashCode;

/**
 * 规范
 * 1.hashcode的和equals返回值应该是稳定的，不应有随机性
 * 2.俩对象==返回true则这两个对象的equals也应该返回true
 * 3.俩对象的equals则这两个对象的hashcode应该相等
 *
 * @author twister
 * @date 2020/8/5
 */
public class Test {

    public static void main(String[] args) {
        User user1 = new User();
        user1.setAge(11);
        user1.setName("a");

        User user2 = new User();
        user2.setAge(11);
        user2.setName("a");

        System.out.println(user1.hashCode()+"\t"+ user2.hashCode()+"\t"+ user1.equals(user2));


    }
}
