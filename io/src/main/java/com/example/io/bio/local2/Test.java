package com.example.io.bio.local2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Scanner;
public class Test
{
    public static void main(String[] args) {
//        int n, count = 0;
//        System.out.println("enter n:");
//        Scanner sc = new Scanner(new Scanner(System.in).nextLine());
//        n = sc.nextInt();
//        char[] a = new char[n];
//        char[] b = new char[200];
//        for (int i = 0; i < n; i++) {
//            a[i] = Character.forDigit(i%10, 10);
//        }
//        for (int i = 0; i < 200; i++) {
//            if (i < 200 - n) {
//                b[i] = Character.forDigit(0, 10);
//            } else {
//                b[i] = a[count++];
//            }
//            System.out.print(b[i]);
//        }

        JSONObject msg = new JSONObject();
        msg.put("cmd", "update");
        JSONObject info = new JSONObject();
        info.put("version", 1); // 版本号
        info.put("path", "/&&&");
        msg.put("info", info);
        String msgStr = JSON.toJSONString(msg);

        byte[] newbytes = new byte[512];
        int oldLeth = msgStr.getBytes().length;
        byte[] oldbytes =  msgStr.getBytes();
        if(oldLeth < newbytes.length){
            for (int len = 0; len < newbytes.length; len++) {
                if (len < oldLeth) {
                    newbytes[len] = oldbytes[len];
                }else {
                    newbytes[len] = 0;
                }
            }
        }
        System.out.println(newbytes);
        System.out.println(new String(newbytes));
        JSONObject newMsg = JSON.parseObject(new String(newbytes));
        System.out.println(newMsg.toJSONString());

    }
}