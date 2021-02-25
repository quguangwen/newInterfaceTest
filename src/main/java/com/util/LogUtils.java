package com.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;


public class LogUtils {

    private static Logger logger;

    private static String filePath="src/main/resources/log4f.properties";

    private static boolean flag = false;

    public LogUtils() {
    }

    private static synchronized void getPropertyFile(){
        logger=Logger.getLogger("TestProject");
        PropertyConfigurator.configure(new File(filePath).getAbsolutePath());
        flag=true;
    }

    private static void getFlag(){
        if(!flag){
            LogUtils.getPropertyFile();
        }
    }

    public static void logInfo(String message){
        LogUtils.getFlag();
        logger.info(message);
    }

    public static void logInfo(Exception e){
        LogUtils.getFlag();
        logger.info(e);
    }

    public static void logError(String message){
        LogUtils.getFlag();
        logger.error(message);
    }

    public static void logError(Exception e){
        LogUtils.getFlag();
        logger.error(e);
    }

    public static void logWarn(String message){
        LogUtils.getFlag();
        logger.warn(message);
    }

    public static void logWarn(Exception e){
        LogUtils.getFlag();
        logger.warn(e);
    }
}
