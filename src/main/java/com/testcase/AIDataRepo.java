package com.testcase;

import com.data.ReadData;
import com.jayway.restassured.response.Response;
import com.service.RequestHeader;
import com.testBase.AIBase;
import com.testBase.AIDataBase;
import com.util.DateUtil;
import com.util.LogUtils;
import com.util.ModData;
import com.util.ReadProperties;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;

import static com.jayway.restassured.RestAssured.given;

/**
 * 数据仓库模块测试用例类
 * @author 曲广文
 */
public class AIDataRepo extends AIDataBase {
    /**
     * 数据仓库测试入口方法
     * @param data
     */
    @Test(dataProvider = "dataRepo", dataProviderClass = ReadData.class)
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
            LogUtils.logInfo(e);
        }
    }
}
