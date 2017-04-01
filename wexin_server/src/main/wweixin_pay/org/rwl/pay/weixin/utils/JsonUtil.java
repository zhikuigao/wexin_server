package org.rwl.pay.weixin.utils;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@SuppressWarnings("unchecked")
public class JsonUtil {

    private static Gson gson;

    private JsonUtil() {
    }

    static {
        GsonBuilder gb = new GsonBuilder();
        gb.setDateFormat("yyyy-MM-dd HH:mm:ss");
        gson = gb.create();
    }

    public static final String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static final <T> T fromJson(final String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static final <T> T fromJson(final String json, Type t) {
        return gson.fromJson(json, t);
    }

    /**
     * 将json解析，存放在map集合
     * 
     * 作者: zhoubang 
     * 日期：2015年6月26日 下午3:53:08
     * @param json
     * @return
     */
    public static final Map<String, Object> fromJson(final String json) {
        return fromJson(json, Map.class);
    }
    
    /**
     * 获取json中的value
     * 
     * 作者: zhoubang 
     * 日期：2015年6月26日 下午3:53:27
     * @param rescontent
     * @param key
     * @return
     */
    public static String getJsonValue(String rescontent, String key) {
        Map<String, Object> data = fromJson(rescontent);
        return (String) data.get(key);
    }
}
