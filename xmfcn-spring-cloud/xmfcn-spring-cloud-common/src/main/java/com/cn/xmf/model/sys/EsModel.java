package com.cn.xmf.model.sys;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.awt.print.Pageable;
import java.io.Serializable;

/**
 * ES存储数据模型
 */
public class EsModel implements Serializable {

    private String index;//索引（Index）：相当于数据库
    private String type;//文档类型（Type）
    private String message;//消息主体
    private JSONObject keyWord;//关键词
    private JSONObject hightWord;//高亮词
    private JSONObject sortWord;//排序参数
    private EsPage esPage;//分页参数

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

    public JSONObject getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(JSONObject keyWord) {
        this.keyWord = keyWord;
    }

    public JSONObject getHightWord() {
        return hightWord;
    }

    public void setHightWord(JSONObject hightWord) {
        this.hightWord = hightWord;
    }

    public JSONObject getSortWord() {
        return sortWord;
    }

    public void setSortWord(JSONObject sortWord) {
        this.sortWord = sortWord;
    }

    public EsPage getEsPage() {
        return esPage;
    }

    public void setEsPage(EsPage esPage) {
        this.esPage = esPage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("index", index)
                .append("type", type)
                .append("message", message)
                .append("keyWord", keyWord)
                .append("hightWord", hightWord)
                .append("sortWord", sortWord)
                .append("esPage", esPage)
                .toString();
    }
}
