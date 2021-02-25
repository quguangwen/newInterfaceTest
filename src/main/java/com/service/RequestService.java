package com.service;

import com.jayway.restassured.response.Response;
import com.util.DataHandle;
import com.util.LogUtils;
import com.util.ModData;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.useRelaxedHTTPSValidation;
/**
 * 算法仓库模块测试基类实现测试定义的基类方法。
 * 实现了测试行为接口类BaseRun
 * 继承数据库操作基类根据需要实现数据库相关的增删改查方法。
 * @author 曲广文
 *
 */
public class RequestService {
    /**
     * 发起post请求。
     * @param url
     * 请求接口路径
     * @param body
     * 请求参数体
     * @param data
     * Excel数据参数
     * @return
     * 返回结果
     */
    public Response doPost(String url, String body,HashMap<String, Object> data){
        Response response = null;
        response = given().when().headers(RequestHeader.initalHeader(ModData.token)).body(body).post(url);
//        response = (Response) given().config(RestAssuredConfig.config().sslConfig(new SSLConfig().allowAllHostnames())).when().headers(RequestHeader.initalHeader(ModData.token)).body(body).post(url);
        return response;
    }


    /**
     * 发起get请求，带参数需要处理参数拼接请求参数，如果没有直接发起get请求。
     * @param url
     * 请求接口路径
     * @param parameter
     * 请求参数
     * @return
     * 返回请求结果
     */
    public Response doGet(String url,String parameter){
        Response response = null;
        if(parameter != null && parameter != ""){
            LogUtils.logInfo(url + "?" + parameter);
            response = given().when().headers(RequestHeader.initalHeader(ModData.token)).get(url + "?" + parameter);
        } else {
            LogUtils.logInfo(url);
            response = given().when().headers(RequestHeader.initalHeader(ModData.token)).get(url);
        }
        return response;
    }

    /**
     * 上传文件或者图片接口
     * @param url
     * 接口请求路径
     * @param params
     * Excel参数
     * @return
     */
    public Response uploadFile(String url, HashMap<String,String> params){
        Response response = null;
        LogUtils.logInfo("调用上传文件接口：" + url);
        File file = new File("testData/testUpload.png");
        if(file.exists()){
            Map headers = RequestHeader.initalHeader(ModData.token);
            headers.put("Content-type","multipart/form-data");
            response = (Response) given().when().headers(headers).formParameters(params).multiPart("file",file).post(url);
        } else {
            LogUtils.logInfo("file is not exist");
        }
        return response;
    }

    /**
     * 定义请求入口类，通过参数判断是get请求或者post请求
     * @param data
     * Excel测试数据
     * @param url
     * 请求接口路径。
     * @return
     */
    public  Response reqAction(HashMap<String,Object> data, String url){
        Response response = null;
        String body = null;
        if(data.get("PARAMETER") != null){
            body = String.valueOf(data.get("PARAMETER"));
        }
        useRelaxedHTTPSValidation();
        if(DataHandle.processUrl(url).contains("upload")){
            HashMap<String, String> hashMap = DataHandle.processFormData(data);
            response = uploadFile(url, hashMap);
            DataHandle.processResultParam(response.getBody().asString(),"id");
        } else if(String.valueOf(data.get("METHOD")).equals("post") && data.get("FormType").equals("Yes")){
            HashMap<String, String> hashMap = DataHandle.processFormData(data);
            response = given().when().header("Authorization",ModData.token).formParameters(hashMap).post(url);
        } else if(String.valueOf(data.get("METHOD")).equals("post")){
            response = new RequestService().doPost(url,body,data);
        } else {
            response = new RequestService().doGet(url,body);
        }
        return response;
    }



}
