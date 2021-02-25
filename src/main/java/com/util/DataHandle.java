package com.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.response.Response;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import javax.jws.WebParam;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 测试数据参数工具类
 * @author 曲广文
 */
public class DataHandle {
    /**
     * 测试参数处理，根据数据库里的userID和roleID替换传入的参数。
     * @param data
     * Excel参数，
     * @param key
     * Excel参数列名称
     * @return
     */
    public static HashMap<String,Object> processDataParam(HashMap<String,Object> data, Object key){

        if(String.valueOf(key).equals("userId") && String.valueOf(data.get("METHOD")).equals("post")){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(String.valueOf(key),ModData.userId);
            data.put("PARAMETER",jsonObject);
        }
        if(String.valueOf(key).equals("roleId") && String.valueOf(data.get("METHOD")).equals("get")){
            String param = String.valueOf(key) + "=" + ModData.roleId;
            data.put("PARAMETER",param);
        }
        return data;
    }

    /**
     * 处理解析传入的接口路径解析接口名称。
     * 例：/data/uploadFile，通过方法处理返回接口名称uploadFile。
     * @param url
     * 接口路径
     * @return
     */
    public static String processUrl(String url){
        String[] urls = url.split("/");
        String result = urls[urls.length-1];
        return result;
    }

    /**
     * 处理表单类型的请求参数。
     * 例：Excel表里的参数需要写成：dirName:autoTest,userName:sysadmin
     * 抽取解析成hashmap 通过restassured表单请求参数格式传递表单参数。
     * @param data
     * Excel参数以hashmap的方式传入。
     * @return
     */
    public static HashMap<String,String> processFormData(HashMap<String,Object> data){
        String[] params = String.valueOf(data.get("PARAMETER")).split(",");

        HashMap<String,String> hashMap = new HashMap<>();
        if(params != null){
            for(String s1:params){
                String[] subParam = s1.split(":");
                String ref = updateParam(subParam[1]);
                subParam[1] = ref;
                hashMap.put(subParam[0],subParam[1]);
            }
        }
        Set<Map.Entry<String,String>> entrySet = hashMap.entrySet();
        for(Map.Entry<String,String> entry : entrySet){
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        return hashMap;
    }

    /**
     * 替换图片上传的IDS参数
     * @param oValue
     * Excel参数传递旧的值
     * @return
     */
    public static String updateParam(String oValue){
        if(oValue.equals("_ids")){
            oValue = ModData.ids;
            LogUtils.logInfo("更新参数值：" + oValue);
        }
        return oValue;
    }

    /**
     * 处理请求结果，抽取字段供后面的接口请求使用
     * @param json
     * 请求结果json格式字符串。
     * @param key
     * json字符串里的关键字。
     */
    public static void processResultParam(String json, String key){
        String keyValue = new JSONUtil().getJsonValueBykey(json, key);
        ModData.ids = keyValue;
        System.out.println("ids:" + ModData.ids);
    }

    /**
     * 抽取请求结果的某一个字段，并且复制给全局变量，供下次接口请求使用。
     * @param data
     * Excel请求参数
     * @param response
     * 接口请求的结果
     */
    public static void processResponse(HashMap<String,Object> data, Response response){
        String dataParam = String.valueOf(data.get("ProcessResult"));
        HashMap<String,String> hashMap = new HashMap<>();
        if(dataParam != null){
            String result = response.asString();
            System.out.println(result);
            String[] params = dataParam.split(",");
            for(String s : params){
                String temp = new JSONUtil().getJsonValueBykey(result,s);
                System.out.println(s + ":" + temp);
                hashMap.put(s,temp);
            }
            ModData.tempValue = hashMap;
        } else {
            LogUtils.logInfo("没有依赖接口数据需要处理");
        }
    }

    /**
     * 处理请求json数据
     * @param data
     * Excel参数
     * @return
     */
    public static HashMap<String, Object> procesRequestJson(HashMap<String,Object> data){
        HashMap<String,Object> param = data;
        String jsonString = String.valueOf(param.get("PARAMETER"));
        if(jsonString.startsWith("{") && jsonString.endsWith("}")){
            if("" != jsonString){
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                if(jsonObject != null){
                    for(Map.Entry<String,Object> entry : jsonObject.entrySet()){
                        String value = String.valueOf(entry.getValue());
                        if(value.startsWith("#")){
                            value = ModData.tempValue.get(entry.getKey());
                            jsonObject.put(entry.getKey(),value);
                            System.out.println(entry.getKey() + ":" + value);
                        }
                    }
                    data.put("PARAMETER",jsonObject);
                }
            }
        }
        return param;
    }

    /**
     * 处理审核接口请求参数数据
     * @param data
     * @param response
     */
    public static void processAuditList(HashMap<String,Object> data,Response response){
        String body = response.asString();
        System.out.println(body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        String temp = "";
        JSONObject tmpJson = null;
        JSONArray jsonArray = null;
        for(Map.Entry entry : jsonObject.entrySet()){
            if(entry.getKey() == "data"){
                temp = String.valueOf(entry.getValue());
                tmpJson = JSONObject.parseObject(temp);
                jsonArray = JSONArray.parseArray(String.valueOf(tmpJson.get("records")));
                JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                if("审核中".equals(jsonObject2.getString("userAuditStatus"))){
                    ModData.tempValue.put("billId",jsonObject2.getString("billId"));
                    ModData.tempValue.put("bizId",jsonObject2.getString("bizId"));
                    System.out.println("billId:" + jsonObject2.getString("billId"));
                    System.out.println("bizId:" + jsonObject2.getString("bizId"));

                }
            }
        }
    }

    /**
     * 处理get请求参数替换。
     * @param data
     * @return
     */
    public static HashMap<String, Object> processGetParam(HashMap<String,Object>data){
        String paramTemp = String.valueOf(data.get("PARAMETER")).trim();
        StringBuilder sb = new StringBuilder(paramTemp);
        String[] params = String.valueOf(sb).split("&");
        String temps;
        int sublength;
        int subPostionStart;
        int subPostionEnd;
        for(String s1 : params){
            if(s1.startsWith("#")){
                sublength = s1.length();
                subPostionStart = paramTemp.indexOf(s1);
                subPostionEnd = subPostionStart + sublength;
                System.out.println(sublength + "," +subPostionStart + "," + subPostionEnd);
                s1 = s1.substring(1).trim();
                System.out.println(s1);
                temps = s1 + "=" + ModData.tempValue.get(s1);
                sb.replace(subPostionStart,subPostionEnd,temps);
            }
        }
        LogUtils.logInfo("替换参数：" + sb);
        data.put("PARAMETER",sb);
        System.out.println(data.get("PARAMETER"));
        return data;
    }

//    public static void main(String[] args) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        HashMap<String, Object> hashMap1 = new HashMap<>();
//        hashMap1.put("PARAMETER", "#bizId&operateName=审核不通过&serviceUrl=&suggestion=审核不通过");
//        hashMap.put("bizId", "171");
//        ModData.tempValue = hashMap;
//        DataHandle.processGetParam(hashMap1);
//
//    }

}
