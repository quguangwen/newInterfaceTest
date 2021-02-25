package com.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义HTTP或HTTPS请求头信息。
 */
public class RequestHeader {

    public static Map initalHeader(String auth){

        Map headers = new HashMap();
        headers.put("Content-type","application/json");
        if(auth != null){
            headers.put("Authorization",auth);
        }

        return headers;
    }

}
