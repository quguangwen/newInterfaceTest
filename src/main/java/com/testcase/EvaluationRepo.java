package com.testcase;

import com.data.ReadData;
import com.jayway.restassured.response.Response;
import com.testBase.EvaluationBase;
import com.util.DateUtil;
import com.util.LogUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * 评测模块接口测试用例入口类
 * @author 曲广文
 */
public class EvaluationRepo extends EvaluationBase {
    /**
     * 评测模块测试入口方法
     * @param data 通过TESTNG传递EXCEL测试数据。
     */
    @Test(dataProvider = "evaluationRepo", dataProviderClass = ReadData.class)
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
