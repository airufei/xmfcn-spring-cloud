package com.cn.xmf.service.es;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.es.EsModel;
import com.cn.xmf.model.es.EsPage;
import com.cn.xmf.model.es.EsPartion;
import com.cn.xmf.util.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.search.sort.Sort;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ES 辅助类
 *
 * @version 2019-05-23
 * @Author rufei.cn
 */
@Service
@SuppressWarnings("all")
public class ElasticsearchHelperService {
    private static Logger logger = LoggerFactory.getLogger(ElasticsearchHelperService.class);

    /**
     * 保存单条数据到ES
     *
     * @param message
     * @return
     */
    public RetData validateParms(EsModel es) {
        RetData retData = new RetData();
        if (es == null) {
            retData.setMessage("json 不能为空");
            return retData;
        }
        String message = es.getMessage();
        String indexName = es.getIndex();
        String type = es.getType();
        if (StringUtil.isBlank(message)) {
            retData.setMessage("message 消息不能为空");
            return retData;
        }
        if (StringUtil.isBlank(indexName)) {
            retData.setMessage("indexName 不能为空");
            return retData;
        }
        if (StringUtil.isBlank(type)) {
            retData.setMessage("type 不能为空");
            return retData;
        }
        retData.setCode(RetCodeAndMessage.SUCCESS);
        retData.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
        return retData;
    }

    /**
     * 组装搜索条件
     *
     * @param EsModel es
     *                keywords
     *                highlights
     * @return
     */
    public Search searchCondition(EsModel es) {
        Search search = null;
        if (es == null) {
            return search;
        }
        JSONObject keywords = es.getKeyWord();
        JSONObject hightWord = es.getHightWord();
        EsPage esPage = es.getEsPage();
        String indexName = es.getIndex();
        String type = es.getType();
        boolean isVague = es.isVague();//是否模糊搜索
        boolean isAccuracySort = es.isAccuracySort();//是否根据精确度排序
        boolean isAndSearch = es.isAndSearch();//是否And搜索
        if (StringUtil.isBlank(indexName)) {
            return search;
        }
        if (StringUtil.isBlank(type)) {
            return search;
        }
        int pageNo = 1;
        int pageSize = 10;
        int startIndex = 0;
        if (esPage != null) {
            pageNo = esPage.getPageNo();
            pageSize = esPage.getPageSize();
            startIndex = esPage.getStartIndex();
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (keywords != null && keywords.size() > 0) {
            Iterator<Map.Entry<String, Object>> iterator = keywords.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                if (value == null || value.toString().length() <= 0) {
                    continue;
                }
                if (isAndSearch) {
                    searchSourceBuilder.query(QueryBuilders.termQuery(key, value));
                } else {
                    searchSourceBuilder.query(QueryBuilders.commonTermsQuery(key, value));
                }
            }
        }
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        if (hightWord != null && hightWord.size() > 0) {
            Iterator<Map.Entry<String, Object>> iterator = keywords.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                if (value == null || value.toString().length() <= 0) {
                    continue;
                }
                highlightBuilder.preTags("<span style=color:red>");
                highlightBuilder.postTags("</span>");
                highlightBuilder.field(key);
            }
        }
        searchSourceBuilder.explain(isAccuracySort);
        searchSourceBuilder.from(startIndex);
        searchSourceBuilder.size(pageSize);
        Sort sort = new Sort("time.keyword", Sort.Sorting.DESC);
        searchSourceBuilder.highlighter(highlightBuilder);
        search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(indexName).addType(type).addSort(sort).build();

        return search;
    }


    /**
     * 获取搜索结果，处理高亮词
     *
     * @param result
     * @param esPage
     */
    public EsPartion getSearchResult(JestResult result, EsPage esPage) {
        EsPartion pt = null;
        int totalCount = result.getJsonObject().get("hits").getAsJsonObject().get("total").getAsInt();
        JsonArray asJsonArray = result.getJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < asJsonArray.size(); i++) {
            JsonElement jsonElement = asJsonArray.get(i);
            JsonObject source = jsonElement.getAsJsonObject().getAsJsonObject("_source").getAsJsonObject();
            JsonObject highlight = jsonElement.getAsJsonObject().getAsJsonObject("highlight");
            Set<Map.Entry<String, JsonElement>> entries = null;
            if (highlight != null) {
                entries = highlight.entrySet();
            }
            if (source == null || source.size() <= 0) {
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(source.toString());
            jsonObject = addHightWord(entries, jsonObject);
            list.add(jsonObject);
        }
        pt = new EsPartion(esPage, list, totalCount);
        return pt;
    }

    /**
     * 添加高亮词到结果集中
     *
     * @param entries
     * @param jsonObject
     * @return
     */
    private JSONObject addHightWord(Set<Map.Entry<String, JsonElement>> entries, JSONObject jsonObject) {
        if (entries == null) {
            return jsonObject;
        }
        if (jsonObject == null) {
            return jsonObject;
        }
        Iterator<Map.Entry<String, JsonElement>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue().toString();
            jsonObject.put(key, value);
        }
        return jsonObject;
    }
}
