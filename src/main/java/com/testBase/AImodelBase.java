package com.testBase;

import com.db.BaseDB;
import com.db.DBConn;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.service.RequestService;
import com.util.DataHandle;
import com.util.LogUtils;
import com.util.ModData;
import com.util.ReadProperties;

import java.sql.SQLException;
import java.util.HashMap;
/**
 * 数据仓库模块测试基类实现测试定义的基类方法。
 * 实现了测试行为接口类BaseRun
 * 继承数据库操作基类根据需要实现数据库相关的增删改查方法。
 * @author 曲广文
 *
 */
public class AImodelBase extends BaseDB implements BaseRun {

    /**
     * 测试环境数据初始化。
     *
     * @param data
     * 读取Excel测试数据。
     */
    @Override
    public void initialize(HashMap<String, Object> data) throws Exception {
        String dbValue = String.valueOf(data.get("DB"));
        String sql = "";
        if(dbValue.equals("yes")){
            String DB_Name = String.valueOf(data.get("DB_Name"));
            String DB_Table = String.valueOf(data.get("DB_Table"));
            String DB_Param = String.valueOf(data.get("DB_Param"));
            String DB_Key = String.valueOf(data.get("DB_Key"));
            String DB_Operation = String.valueOf(data.get("DB_Operation"));
            DBConn dbConn = new DBConn();
            dbConn.getDBconn(DB_Name);

            if(DB_Operation.equals("select")){
                LogUtils.logInfo("获取ModeID：" + DB_Param);
                String parakey = String.valueOf(data.get("PARAMETER"));
                sql = String.format("select * from %s.%s where %s = '%s'", DB_Name,DB_Table,DB_Key,DB_Param);
                if(parakey.equals("modelId=_modelId")){
                    ModData.modelId = dbConn.selectID(sql,"id");
                    System.out.println("modeID=" + ModData.modelId);
                }
            }

            try {
                dbConn.closeDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
        String value = "modelId=" + ModData.modelId;
        if(String.valueOf(data.get("PARAMETER")).equals("modelId=_modelId")){
            data.put("PARAMETER",value);
        }
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
        HashMap<String,Object>paramData = null;
        String url = String.valueOf(ReadProperties.getProByKey("server")) + data.get("URL");
        LogUtils.logInfo("URL:" + url);
        String flag = String.valueOf(data.get("Flag"));
        if(flag.equals("processRequest")){
            paramData = DataHandle.processGetParam(data);
            response = new RequestService().reqAction(paramData,url);
        } else {
            response = new RequestService().reqAction(data,url);
        }
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
        if(String.valueOf(data.get("Flag")).equals("审核中")){
            DataHandle.processResponse(data,re);
        }

        if(String.valueOf(data.get("Flag")).equals("processRequest")){
            ModData.tempValue = null;
        }
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
