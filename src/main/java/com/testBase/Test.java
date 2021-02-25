package com.testBase;

import com.alibaba.fastjson.JSONObject;
import com.data.ReadData;
import com.jayway.restassured.response.Response;
import com.util.JSONUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.responseSpecification;

public class Test {

    public static void updateValue(String s){
        JSONObject jsonObject = JSONObject.parseObject(s);
        System.out.println(new JSONUtil().getJsonValueBykey(s,"id"));
    }

    @org.testng.annotations.Test(dataProvider = "test", dataProviderClass = ReadData.class)
    public void debugTest(HashMap<String, Object> data){
        File file = new File("testData/testUpload.png");
    }

    public static void main(String[] args) {
        Map map = new HashMap<String,String>();
//        map.put("dirName","autoTest");
//        map.put("userName","qgw123");
//        File file = new File("testData/testUpload.png");
//        Response response = (Response) given().formParameters(map).multiPart("file",file).post("http://172.16.77.116:30040/data/uploadFile");
        map.put("ids","be1c4057809f4c8da2a5e353e4aa5c4e");
        map.put("userName","qgw123");
        Response response = given().formParameters(map).post("http://172.16.77.116:30040/data/deleteBatch");
        response.prettyPrint();
//        System.out.println(result);
//        updateValue(result);
    }
}
