package com.testcase;

import com.alibaba.fastjson.JSONObject;
import com.data.ReadData;
import com.jayway.restassured.response.Response;
import com.service.RequestHeader;
import com.testBase.AIBase;
import com.testBase.AImodelBase;
import com.util.*;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.useRelaxedHTTPSValidation;

/**
 * AImodel模块测试用例入口类
 * @author 曲广文
 **/

public class AIModelRepo extends AImodelBase {
    /**
     * AI model模块测试入口方法
     * @param data 通过TESTNG传递EXCEL测试数据。
     *
     */
    @Test(dataProvider = "modelRepo", dataProviderClass = ReadData.class)
    public void test(HashMap<String,Object> data){
        LogUtils.logInfo("CASE: " + data.get("CASE "));
        LogUtils.logInfo("DESC：" +data.get("DESC"));
        LogUtils.logInfo("PARAMETER：" + data.get("PARAMETER"));
        LogUtils.logInfo("开始执行时间:" + DateUtil.convertDate());
        HashMap<String,Object> hashmap = null;
        try {
            initialize(data);
            hashmap = processData(data);
            Response re = requestInterface(hashmap);
            String result = processResult(re,data);
            Assert.assertEquals(result,"true");
            LogUtils.logInfo("执行结束时间：" + DateUtil.convertDate());
            System.out.println(" ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
