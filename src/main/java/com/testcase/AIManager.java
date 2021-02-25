package com.testcase;

import com.alibaba.fastjson.JSONObject;
import com.data.ReadData;

import com.jayway.restassured.response.Response;
import com.service.RequestHeader;
import com.testBase.AIBase;
import com.util.*;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.HashMap;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.useRelaxedHTTPSValidation;
/**
 *AI用户模块测试用例入口类
 * @author 曲广文
 *
 **/
public class AIManager extends AIBase{

    /**
     *用户登录获取系统访问auth
     */
    @BeforeTest
    public void getToken(){
        String url = ReadProperties.getProByKey("server") + "/login/login";
        String body = ReadProperties.getProByKey("loginBody");
        JSONObject jsonObject = JSONObject.parseObject(body);
        String encryptPassword = new EncryptDemo().getEncryptString(String.valueOf(jsonObject.getString("password")));
        System.out.println(encryptPassword);
        jsonObject.put("password",encryptPassword);
        useRelaxedHTTPSValidation();
        Response response = (Response) given().header("Content-type","application/json; charset=UTF-8").body(jsonObject).post(url);

        ModData.token= response.getHeader("Authorization");
        RequestHeader.initalHeader(ModData.token);
        LogUtils.logInfo("Authrization:" + ModData.token);
    }

    /**
     * 用户模块测试入口方法。
     * @param data 通过TESTNG传递EXCEL测试数据。
     */

    @Test(dataProvider = "data", dataProviderClass = ReadData.class)
    public void test(HashMap<String,Object> data){
        LogUtils.logInfo("CASE: " + data.get("CASE "));
        LogUtils.logInfo("DESC：" +data.get("DESC"));
        LogUtils.logInfo("PARAMETER：" + data.get("PARAMETER"));
        LogUtils.logInfo("开始执行时间:" + DateUtil.convertDate());
        HashMap<String,Object> hashMap = null;
        try {
            initialize(data);
            hashMap =  processData(data);
            Response re =  requestInterface(hashMap);
            String result =  processResult(re,data);
            Assert.assertEquals(result,"true");
            LogUtils.logInfo("执行结束时间：" + DateUtil.convertDate());
            System.out.println(" ");
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

}
