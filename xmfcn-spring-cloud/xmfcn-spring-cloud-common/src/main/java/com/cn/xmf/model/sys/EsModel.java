package com.cn.xmf.model.sys;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * ES存储数据模型
 */
public class EsModel implements Serializable {

    private String index;//索引（Index）：相当于数据库
    private String type;//文档类型（Type）
    private String message;//消息体
    private JSONObject parms;//扩展参数内容

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getParms() {
        return parms;
    }

    public void setParms(JSONObject parms) {
        this.parms = parms;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("index", index)
                .append("type", type)
                .append("message", message)
                .append("parms", parms)
                .toString();
    }
}
