package com.management.common.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zyf
 */
public class JsonUtil {
    public JSONObject success(Object data) {
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("data", data);
        result.put("code", 1);
        return result;
    }

    public JSONObject fail(Object data) {
        JSONObject result = new JSONObject();
        result.put("code", 400);
        result.put("data", data);
        result.put("code", 0);
        return result;
    }
}
