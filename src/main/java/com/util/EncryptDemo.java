package com.util;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

public class EncryptDemo {
    public String getEncryptString(String s) {
        String temp = null;
        try {
            temp = DigestUtils.md5DigestAsHex(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return temp !=null ? temp : null;
    }

    public static void main(String[] args) {
        System.out.println(new EncryptDemo().getEncryptString("123456"));
    }
}
