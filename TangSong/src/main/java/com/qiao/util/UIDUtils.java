package com.qiao.util;

import java.util.Random;
import java.util.UUID;

public class UIDUtils {
    //给定一个字符串
    static String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    static String str2="0123456789";
    public static String getSalt(){
        StringBuffer salt = new StringBuffer();
        for (int i = 0; i <= 7; i++) {
            salt.append(str.charAt(new Random().nextInt(62)));
        }
        return salt.toString();
    }

    public static String getNum(){
        StringBuffer salt = new StringBuffer();
        for (int i = 0; i <= 8; i++) {
            salt.append(str.charAt(new Random().nextInt(10)));
        }
        return salt.toString();
    }
    public static String getUUID32(){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return uuid;
        //  return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
