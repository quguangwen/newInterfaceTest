package com.testBase;

import com.db.BaseDB;
import com.db.DBConn;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.service.RequestService;
import com.util.*;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 用户管理模块测试基类实现测试定义的基类方法。
 * 实现了测试行为接口类BaseRun
 * 继承数据库操作基类根据需要实现数据库相关的增删改查方法。
 * @author 曲广文
 *
 */
public class AIBase extends BaseDB implements BaseRun {
    /**
     * 测试环境数据初始化。
     *
     * @param data
     * 读取Excel测试数据。
     */
    @Override
    public void initialize(HashMap<String, Object> data){
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
            if(DB_Operation.equals("delete")){
                LogUtils.logInfo("删除存在账号：" + DB_Param);
                sql = String.format("delete from %s.%s where %s = '%s'",DB_Name,DB_Table,DB_Key,DB_Param);
                dbConn.deleteDB(sql);
            }
            if(DB_Operation.equals("select")){
                LogUtils.logInfo("获取存在ID：" + DB_Param);
                String parakey = String.valueOf(data.get("PARAMETER"));
                sql = String.format("select * from %s.%s where %s = '%s'", DB_Name,DB_Table,DB_Key,DB_Param);
                if(parakey.equals("userId")){
                    ModData.userId = dbConn.selectID(sql,"user_id");
                    System.out.println("userId:" + ModData.userId);
                }
                if(parakey.equals("roleId")){
                    ModData.roleId = dbConn.selectID(sql,"role_id");
                    System.out.println(ModData.roleId);
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
    public HashMap<String, Object> processData(HashMap<String, Object> data) {
        HashMap<String,Object> parameter = data;
        LogUtils.logInfo("开始处理参数：");
        Object param = data.get("PARAMETER");
        if(param != null && String.valueOf(param).equals("userId") ){
            parameter = DataHandle.processDataParam(data,param);
        }
        if(param != null && String.valueOf(param).equals("roleId") ){
            parameter = DataHandle.processDataParam(data,param);
        }
        return parameter;
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
        HashMap<String,Object> temparam = null;
        if(data.get("PARAMETER") != null){
            temparam = DataHandle.procesRequestJson(data);
        }
        String url = ReadProperties.getProByKey("server")+data.get("URL");
        LogUtils.logInfo("URL:" + url);
        String body = null;
        if(temparam !=null){
            response = new RequestService().reqAction(temparam,url);
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
    public String processResult(Response re, HashMap<String, Object> data) {
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
            DataHandle.processAuditList(data,re);
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
