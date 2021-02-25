package com.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * 用于获取json字符串中指定key的value
 *
 * @author wind
 * @since 05.05.2015
 */
public class JsonValueTool {

    /**
     * 用于获取json字符串中指定key的value
     *
     * 示例：
     * json：{"name": "刘禅", "age": "6", "father": {"name": "刘备", "age": "50", "properties": {country":{"id":"002","name": "蜀"}}}}
     *      *"
     * 要获取country中的name值
     * startKey = father
     * targetKeyExpression = father#properties#country#name
     *
     * @param json
     * @param startKey
     * @param targetKeyExpression 使用#连接
     * @return
     */
    public static Object getValueByKeyExpression(String json, String startKey, String targetKeyExpression) {
        return doGetValueByKeyExpression(JSONObject.parseObject(json), startKey, targetKeyExpression);
    }

    private static Object doGetValueByKeyExpression(Object originObject, String startKey, String targetKeyExpression) {
        if (isNullOrEmpty(startKey)) {
            return originObject;
        }
        if (originObject instanceof JSONObject) {
            return getValueFromJSONObjectByKeyExpression((JSONObject) originObject, startKey, targetKeyExpression);
        }
        if (originObject instanceof JSONArray) {
            return getValueFromJSONArrayByKeyExpression((JSONArray) originObject, startKey, targetKeyExpression);
        }
        return null;
    }

    private static String getNextKey(String startKey, String targetKeyExpression) {
        if (isNullOrEmpty(targetKeyExpression)) {
            return null;
        }

        String[] keys = targetKeyExpression.split("#");
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(startKey) && (i < keys.length - 1)) {
                return keys[i + 1];
            }
        }
        return null;
    }

    private static Object getValueFromJSONArrayByKeyExpression(JSONArray originObject, String startKey, String targetKeyExpression) {
        for (int j = 0; j < originObject.size(); j++) {
            JSONObject jsonObject = originObject.getJSONObject(j);
            Object targetObject = getValueFromJSONObjectByKeyExpression(jsonObject, startKey, targetKeyExpression);
            if (targetObject != null) {
                return targetObject;
            }
        }
        return null;
    }

    private static Object getValueFromJSONObjectByKeyExpression(JSONObject originObject, String startKey, String targetKeyExpression) {
        Object object = originObject.get(startKey);
        return object != null ? doGetValueByKeyExpression(object, getNextKey(startKey, targetKeyExpression), targetKeyExpression) : null;
    }

    public static void main(String[] args) {
        String jsonString = "{\"code\":200,\"message\":\"success\",\"data\":{\"records\":[{\"approveBill\":{\"id\":401,\"title\":\"autoTestinter1创建审核单\",\"billStatus\":\"1\",\"bizId\":\"196\",\"flowType\":\"model\",\"nodeName\":null,\"transactUserId\":null,\"transactUserName\":null,\"transactUserLoginName\":null,\"delFlag\":\"0\",\"createBy\":\"sysadmin\",\"createTime\":\"2021-01-27 11:49:05\",\"updateBy\":\"sysadmin\",\"updateTime\":\"2021-01-27 11:49:05\",\"extensionField1\":\"autoTestinter1\",\"extensionField2\":\"1\",\"extensionField3\":\"1,\",\"extensionField4\":\"1\",\"extensionField5\":\"sysadmin\",\"extensionField6\":\"313\",\"extensionField7\":null,\"extensionField8\":null,\"extensionField9\":null,\"extensionField10\":null},\"approveProcessList\":null,\"workitemList\":[],\"hisWorkitemList\":[]}],\"total\":1,\"size\":10,\"current\":1,\"orders\":[],\"optimizeCountSql\":true,\"hitCount\":false,\"searchCount\":true,\"pages\":1}}";
        String value = String.valueOf(JsonValueTool.getValueByKeyExpression(jsonString,"data","data#records#approveBill#bizId"));
        System.out.println(value);
    }

}
