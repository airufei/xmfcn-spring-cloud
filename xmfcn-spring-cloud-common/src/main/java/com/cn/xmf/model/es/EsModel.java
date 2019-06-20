package com.cn.xmf.model.es;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * ES 参数模型
 *
 * @Author rufei.cn
 */
public class EsModel implements Serializable {

    private String index;//索引（Index）：相当于数据库
    private String type;//文档类型（Type）
    private String message;//消息主体
    private JSONObject keyWord;//关键词
    private JSONObject hightWord;//高亮词
    private JSONObject sortWord;//排序参数
    private EsPage esPage;//分页参数
    private boolean isVague;//是否模糊搜索
    private boolean isAccuracySort;//是否根据精确度排序
    private boolean isAndSearch;//是否and搜索
    private List<JSONObject> list;//消息集合
    private String startTime;//开始时间
    private String endTime;//结束时间

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

    public boolean isVague() {
        return isVague;
    }

    public void setVague(boolean vague) {
        isVague = vague;
    }

    public boolean isAccuracySort() {
        return isAccuracySort;
    }

    public void setAccuracySort(boolean accuracySort) {
        isAccuracySort = accuracySort;
    }

    public boolean isAndSearch() {
        return isAndSearch;
    }

    public void setAndSearch(boolean andSearch) {
        isAndSearch = andSearch;
    }

    public List<JSONObject> getList() {
        return list;
    }

    public void setList(List<JSONObject> list) {
        this.list = list;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
                .append("isVague", isVague)
                .append("isAccuracySort", isAccuracySort)
                .append("isAndSearch", isAndSearch)
                .append("list", list)
                .append("startTime", startTime)
                .append("endTime", endTime)
                .toString();
    }
}
