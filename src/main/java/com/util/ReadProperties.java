package com.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

    public static String getProByKey(String key){
        String fileName= "sysconfig.properties";
        InputStream inputStream = ReadProperties.class.getClassLoader().getResourceAsStream(fileName);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        Properties properties = new Properties();
        String value = "";
        try {
            properties.load(bufferedInputStream);
            value = properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

//   public static void main(String[] args) {
//        System.out.println(ReadProperties.getProByKey("server"));
//    }
}
