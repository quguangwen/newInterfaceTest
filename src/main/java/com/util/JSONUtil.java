package com.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Objects;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

/**
 *
 * 常用JSON工具类 2020/2/26
 *
 * @author GAO
 */
public class JSONUtil {

    //private static final Log log = LogFactory.getLog(JSONUtil.class);

    /**
     * key的value值
     */
    private Object value;


    /**
     * 判断是否还有子级
     */
    private static boolean flag = true;

    /**
     * @param entry
     * @param key 指定的key值
     * @return
     * @描述 map按动态的key解析值 ,直到解析出想要的值
     */
    public Object parseJsonMap(Map.Entry<String, Object> entry, String key) {
        //LogUtils.logInfo("value:" + value);
        if (ObjectUtils.isEmpty(value)) {
            //LogUtils.logInfo("value is null");
            if (flag) {
                //LogUtils.logInfo("entry key:" + entry.getKey());
                //LogUtils.logInfo("key:" + key);
                if (Objects.equal(entry.getKey(), key)) {
                    value = entry.getValue();
                    flag = false;
                } else {
                    //LogUtils.logInfo("entry value:" + entry.getValue());
                    //如果是单个map继续遍历
                    if (entry.getValue() instanceof Map) {
                       // LogUtils.logInfo("value is map");
                        if (isJsonMap(String.valueOf(entry.getValue()))) {
                            LinkedHashMap<String, Object> jsonMap = JSON.parseObject(String.valueOf(entry.getValue()), new TypeReference<LinkedHashMap<String, Object>>() {
                            });
                            for (Map.Entry<String, Object> entry2 : jsonMap.entrySet()) {
                                if (!StringUtils.contains(entry2.getKey(), key)) {
                                    //LogUtils.logInfo("entry2 entry2.getKey() is not key ");
                                    value = parseJsonMap(entry2, key);
                                } else {
                                    //LogUtils.logWarn(" entry2 entry2.getKey() is  key  ");
                                    value = entry2.getValue();
                                    //LogUtils.logInfo("value:" + value);
                                    flag = false;
                                    break;
                                }
                            }
                        }
                    }

                    //如果是String就获取它的值
                    if (entry.getValue() instanceof String) {
                        //LogUtils.logInfo("entry.getValue() is String:");
                        //LogUtils.logInfo("key:" + entry.getKey() + "value:" + entry.getValue());
//                        parseJsonString(entry.getValue().toString(), key);
                    }
                    //如果是空JSONArray,就返回它的值
                    if (entry.getValue() instanceof JSONArray && ((JSONArray) entry.getValue()).isEmpty()) {
                        //LogUtils.logInfo("key" + entry.getKey() + ",   value: []");
                        value = "[]";
                        //LogUtils.logInfo("value:" + value);
                        flag = false;
                    }
                    //如果是list就提取出来
                    if (entry.getValue() instanceof List && ((Collection) entry.getValue()).size() > 0) {
                        List list = (List) entry.getValue();
                        for (int i = 0; i < list.size(); i++) {
                            parseJsonString(String.valueOf(list.get(i)), key);
                        }
                    }
                }
            }
        }
        //LogUtils.logInfo("value:" + value);
        return value;
    }

    /**
     * @param json
     * @param key 指定key值
     * @return
     * @描述 从JSON对象中，根据指定key获取值，只拿第一个
     */
    public Object parseJsonString(String json, Object key) {
        //LogUtils.logInfo("parseJsonString json:" + json);
        //LogUtils.logInfo("parseJsonString key:" + key);
        if (!ObjectUtils.isEmpty(json) && !ObjectUtils.isEmpty(key)) {
            //LogUtils.logInfo(" parseJsonString json and key is not null");
            if (isJsonMap(json)) {
                LinkedHashMap<String, Object> jsonMap = JSON.parseObject(json, new TypeReference<LinkedHashMap<String, Object>>() {
                });
                for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                    //LogUtils.logInfo("entry key:" + entry.getKey());
                    //LogUtils.logInfo("key:" + key);
                    if (!StringUtils.contains(entry.getKey(), String.valueOf(key))) {
                        //LogUtils.logInfo("is not blank ");
                        value = parseJsonMap(entry, String.valueOf(key));
                    } else {
                        //LogUtils.logWarn(" is blank ");
                        value = entry.getValue();
                        break;
                    }
                }
            }
            return value;
        } else {
            //LogUtils.logInfo("json and key is  null");
            return null;
        }
    }

    /**
     * 判断字符串是否可以转化为json对象
     *
     * @param content
     * @return
     */
    public boolean isJsonObject(String content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if (StringUtils.isBlank(content)) {
            return false;
        }
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串是否可以转化为JSON数组
     *
     * @param content
     * @return
     */
    public boolean isJsonArray(String content) {
        if (StringUtils.isBlank(content)) {
            return false;
        }
        StringUtils.isEmpty(content);
        try {
            JSONArray jsonStr = JSONArray.parseArray(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否可以按照map动态的key解析值
     *
     * @param content
     * @return
     */
    public boolean isJsonMap(String content) {
        //LogUtils.logInfo("content:" + content);
        if (StringUtils.isBlank(content)) {
            return false;
        }
        StringUtils.isEmpty(content);
        try {
            LinkedHashMap<String, Object> jsonMap = JSON.parseObject(content, new TypeReference<LinkedHashMap<String, Object>>() {
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据key获取value
     *
     * @param json 需要传入的JSON对象
     * @param key key的名称
     * @return
     */
    public String getJsonValueBykey(String json, Object key) {
        //LogUtils.logInfo("getJsonValueBykey  json:" + json);
        //LogUtils.logInfo("getJsonValueBykey  key:" + key);
        if (StringUtils.isNotBlank(json) && StringUtils.isNotBlank(String.valueOf(key))) {
            //LogUtils.logInfo("getJsonValueBykey json and key is not null");
            if (isJsonArray(json)) {
                //LogUtils.logInfo("json is jsonArray");
                JSONArray jsonDataArray = JSONArray.parseArray(json);
                //LogUtils.logInfo("jsonDataArray size:" + jsonDataArray.size());
                for (int i = 0; i < jsonDataArray.size(); i++) {
                    if (isJsonObject(String.valueOf(jsonDataArray.get(i)))) {
                        //LogUtils.logInfo("job json:" + jsonDataArray.get(i));
                        JSONObject job = jsonDataArray.getJSONObject(i);
                        if(job != null){
                            value = String.valueOf(parseJsonString(job.toJSONString(), key));
                        } else {
                            System.out.println("为空不满足");
                        }
                    }
                }
            } else if (isJsonObject(json)) {
                if(json != null){
                    value = String.valueOf(parseJsonString(json, key));
                }

            }
            if(value != null){
                return String.valueOf(value);
            } else {
                return "";
            }

        } else {
            //LogUtils.logError("json and key is  null");
            return null;
        }
    }

    public static void main(String[] args) {
        //String json = "{\"status\":\"0\",\"t\":\"\",\"set_cache_time\":\"\",\"data\":[{\"StdStg\":6004,\"StdStl\":8,\"_update_time\":\"1577497907\",\"cambrian_appid\":\"0\",\"city\":\"武汉\",\"key\":\"1897134\",\"prov\":\"湖北\",\"showurl\":\"http:\\/\\/haoma.baidu.com\",\"title\":\"XXX\",\"type\":\"中国电信\",\"url\":\"http:\\/\\/haoma.baidu.com\",\"loc\":\"https:\\/\\/dss1.baidu.com\\/8aQDcnSm2Q5IlBGlnYG\\/q?r=2002696&k=1897134\",\"SiteId\":2002696,\"_version\":30208,\"_select_time\":1577497151,\"querytype\":\"手机号码\",\"phoneinfo\":\"手机号码&quot;18971344569&quot;\",\"phoneno\":\"18971344569\",\"origphoneno\":\"18971344569\",\"titlecont\":\"手机号码归属地查询\",\"showlamp\":\"1\",\"clickneed\":\"0\",\"ExtendedLocation\":\"\",\"OriginQuery\":\"18971344569\",\"tplt\":\"mobilephone\",\"resourceid\":\"6004\",\"fetchkey\":\"6004_1897134\",\"appinfo\":\"\",\"role_id\":1,\"disp_type\":0}]}";
        //String key = "city";
       // String test = ReadProperties.getProByKey("tempjson");
        String json = "{\"code\":200,\"message\":\"success\",\"data\":{\"records\":[{\"approveBill\":{\"id\":379,\"title\":\"autoTestinter1创建审核单\",\"billStatus\":\"1\",\"bizId\":\"248\",\"flowType\":\"model\",\"nodeName\":null,\"transactUserId\":null,\"transactUserName\":null,\"transactUserLoginName\":null,\"delFlag\":\"0\",\"createBy\":\"sysadmin\",\"createTime\":\"2021-01-26 16:45:00\",\"updateBy\":\"sysadmin\",\"updateTime\":\"2021-01-26 16:45:00\",\"extensionField1\":\"autoTestinter1\",\"extensionField2\":\"1\",\"extensionField3\":\"1,\",\"extensionField4\":\"1\",\"extensionField5\":\"sysadmin\",\"extensionField6\":\"299\",\"extensionField7\":null,\"extensionField8\":null,\"extensionField9\":null,\"extensionField10\":null},\"approveProcessList\":null,\"workitemList\":[],\"hisWorkitemList\":[]}],\"total\":1,\"size\":10,\"current\":1,\"orders\":[],\"optimizeCountSql\":true,\"hitCount\":false,\"searchCount\":true,\"pages\":1}}\n";
        //log.info("key的值等于:" + value);
        System.out.println(new JSONUtil().getJsonValueBykey(json,"bizId"));
    }
}

