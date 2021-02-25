package com.testBase;

import com.db.BaseDB;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.service.RequestService;
import com.util.LogUtils;
import com.util.ReadProperties;

import java.util.HashMap;
/**
 * 系统管理模块测试基类实现测试定义的基类方法。
 * 实现了测试行为接口类BaseRun
 * 继承数据库操作基类根据需要实现数据库相关的增删改查方法。
 * @author 曲广文
 *
 */
public class SystemManageBase extends BaseDB implements BaseRun {
    /**
     * 测试环境数据初始化。
     *
     * @param data
     * 读取Excel测试数据。
     */
    @Override
    public void initialize(HashMap<String, Object> data) throws Exception {

    }
    /**
     * 根据特殊接口需求处理和替换测试请求参数方法。
     *
     * @param data
     * 读取Excel测试数据
     * @return
     * 返回测试数据参数
     */
    @Override
    public HashMap<String, Object> processData(HashMap<String, Object> data) throws Exception {
        HashMap<String,Object> parameter = data;
        return data;
    }
    /**
     * 发起HTTPS或HTTP请求
     * @param data
     * 读取Excel测试数据
     * @return
     * 返回请求结果
     * @throws Exception
     */
    @Override
    public Response requestInterface(HashMap<String, Object> data) throws Exception {
        Response response = null;
        String url = String.valueOf(ReadProperties.getProByKey("server"))+ data.get("URL");
        LogUtils.logInfo("URL:" + url);
        response = new RequestService().reqAction(data, url);
        return response;
    }
    /**
     * 接续请求结果，判断接口是否符合响应预期。
     * @param re
     * 接口请求结果
     * @param data
     * Excel测试数据参数。
     * @return
     * 返回测试断言结果
     */
    @Override
    public String processResult(Response re, HashMap<String, Object> data) throws Exception {
        LogUtils.logInfo("请求返回值：" + re.asString());
        JsonPath jp = re.body().jsonPath();
        String result = "false";
        String expectCode = String.valueOf(data.get("CODE"));
        String actualCode = jp.getString("code");
        if(actualCode == null){
            actualCode = String.valueOf(jp.getString("status"));
        }
        LogUtils.logInfo("expectCode:" + expectCode);
        LogUtils.logInfo("actualCode:" + actualCode);

        String actualMessage = jp.getString("message");
        String expectMessage = String.valueOf(data.get("MESSAGE"));
        LogUtils.logInfo("actualMessage:" + actualMessage);
        LogUtils.logInfo("expectMessage:" + expectMessage);
        if(expectCode.equals(actualCode) && expectMessage.equals(actualMessage)){
            LogUtils.logInfo("code值比对一致：" + expectCode);
            LogUtils.logInfo("message:" + actualMessage);
            result ="true";
        }  else {
            LogUtils.logError("code值不一致");
            result = "false";
        }
        return result;
    }
}
