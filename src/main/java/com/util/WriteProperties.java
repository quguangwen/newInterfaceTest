package com.util;

import org.apache.commons.io.output.WriterOutputStream;

import java.io.*;

import java.util.Properties;

public class WriteProperties {

    public static String fileName = "tempKey.properties";

    public static String readTempPro(String key) {
        String value = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF-8");
            Properties properties = new Properties();
            properties.load(inputStreamReader);
            if(properties.containsKey(key)){
                value = properties.getProperty(key);
            }
            inputStreamReader.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value !=null ? value :null;
    }


    public static void writePro(String key, String value) {
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName,false),"UTF-8");
            Properties properties = new Properties();
            properties.setProperty(key,value);
            properties.store(outputStreamWriter, "key:value");
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStreamWriter.flush();
                outputStreamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new WriteProperties().writePro("1","测试");
        new WriteProperties().readTempPro("1");
    }
}