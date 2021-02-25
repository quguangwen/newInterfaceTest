package com.testBase;

import com.jayway.restassured.response.Response;

import java.util.HashMap;
/**
 * 测试类行为接口
 *
 * @author 曲广文
 *
 */
public interface BaseRun {

    public void initialize(HashMap<String, Object> data) throws  Exception;
    public HashMap<String, Object> processData(HashMap<String, Object> data) throws  Exception;
    public Response requestInterface(HashMap<String, Object> data) throws Exception;
    public String processResult(Response re, HashMap<String, Object> data) throws Exception;

}
