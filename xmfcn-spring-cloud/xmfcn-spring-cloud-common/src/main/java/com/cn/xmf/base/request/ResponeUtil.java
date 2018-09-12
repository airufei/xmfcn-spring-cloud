package com.cn.xmf.base.request;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

public class ResponeUtil {

    /**
     * getParms:(请求参数封装)
     *
     * @param
     * @return
     * @author airufei
     * @Date 2017/11/1 16:56
     **/
    public static JSONObject getParms(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap == null || parameterMap.size() < 1) {//返回非json格式，则从request getAttribute中获取参数
            Enumeration attributeNames = request.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                Object key = attributeNames.nextElement();
                Object value = null;
                if (key != null) {
                    value = request.getAttribute(key.toString());
                }
                if (value != null) {
                    json.put(key.toString().trim(), value);
                }
            }
        } else {
            Set<Map.Entry<String, String[]>> requestEntries = parameterMap.entrySet();
            for (Map.Entry<String, String[]> entry : requestEntries) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                String value = "";
                if (values != null && values.length > 0) {
                    value = values[0].toString();
                }
                json.put(key.trim(), value);
            }
        }
        return json;
    }

    public static void writeResponse(ServletResponse response, String responseString)
            throws IOException {
        PrintWriter out = response.getWriter();
        if (out != null) {
            out.write(responseString);
        }
        if (out != null) {
            out.close();
        }

    }
}
