package com.cn.xmf.service.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.es.EsModel;
import com.cn.xmf.model.es.EsPage;
import com.cn.xmf.model.es.EsPartion;
import com.cn.xmf.service.common.SysCommonService;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.DateUtil;
import com.cn.xmf.util.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.search.sort.Sort;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.PutMapping;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    private JestClient jestClient;

    @Autowired
    private SysCommonService sysCommonService;

    /**
     * 创建mapping
     *
     * @param es
     * @return
     */
    public JestResult createMapping(EsModel es) {
        JestResult result = null;
        RetData aReturn = validateParms(es);
        if (aReturn.getCode() == RetCodeAndMessage.FAILURE) {
            return result;
        }
        String message = es.getMessage();
        String indexName = es.getIndex();
        String type = es.getType();
        JSONObject messageJson = JSONObject.parseObject(message);
        if (messageJson == null || messageJson.size() <= 0) {
            return result;
        }
        try {
            Iterator<Map.Entry<String, Object>> iterator = messageJson.entrySet().iterator();
            JSONObject typeName = new JSONObject();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                if (key == null) {
                    continue;
                }
                JSONObject object = new JSONObject();
                object.put("type", "text");
                if (key.equals("createTime")) {
                    object.put("type", "date");
                    object.put("format", "yyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis");
                }
                if (key.equals("time")) {
                    object.put("type", "keyword");
                }
                if (key.equals("level")) {
                    object.put("type", "keyword");
                }
                if (key.equals("subSysName")) {
                    object.put("type", "keyword");
                }
                if (key.equals("traceId")) {
                    object.put("type", "keyword");
                }
                if (key.equals("id")) {
                    object.put("type", "keyword");
                }
                typeName.put(key, object);
            }
            JSONObject properties = new JSONObject();
            properties.put("properties", typeName);
            JSONObject fieldMapping = new JSONObject();
            fieldMapping.put(type, properties);
            logger.info("properties={}", properties);
            PutMapping putMapping = new PutMapping.Builder(indexName, type, properties.toString()).build();
            result = jestClient.execute(putMapping);
            logger.info("result={}", result.getJsonString());
        } catch (IOException e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.info(exceptionMsg);

        } catch (Exception e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.info(exceptionMsg);

        }
        return result;

    }

    /**
     * 判断 ES集群中 是否含有 此索引名的索引
     *
     * @param indexName
     * @return
     */
    public JestResult indicesExists(String indexName) {
        JestResult result = null;
        try {
            IndicesExists indicesExists = new IndicesExists.Builder(indexName).build();
            result = jestClient.execute(indicesExists);
        } catch (IOException e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.info(exceptionMsg);

        } catch (Exception e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.info(exceptionMsg);

        }
        return result;
    }

    /**
     * 删除索引
     *
     * @param indexName
     * @throws Exception
     */
    public JestResult deleteIndex(EsModel es) {
        JestResult result = null;
        RetData aReturn = validateParms(es);
        if (aReturn.getCode() == RetCodeAndMessage.FAILURE) {
            return result;
        }
        String message = es.getMessage();
        String indexName = es.getIndex();
        String type = es.getType();
        result = indicesExists(indexName);
        if (result == null) {
            return result;
        }
        int responseCode = result.getResponseCode();
        if (responseCode != 200) {
            return result;
        }
        try {
            result = jestClient.execute(new DeleteIndex.Builder(indexName).build());
        } catch (Exception e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.info(exceptionMsg);

        }
        return result;
    }

    /**
     * 创建索引
     *
     * @param indexName
     * @throws Exception
     */
    public JestResult createIndex(EsModel es) {
        JestResult result = null;
        RetData aReturn = validateParms(es);
        if (aReturn.getCode() == RetCodeAndMessage.FAILURE) {
            return result;
        }
        String message = es.getMessage();
        String indexName = es.getIndex();
        String type = es.getType();
        result = indicesExists(indexName);
        if (result == null) {
            return result;
        }
        int responseCode = result.getResponseCode();
        try {
            if (responseCode == 404) {
                result = jestClient.execute(new CreateIndex.Builder(indexName).build());
                responseCode = result.getResponseCode();
                if (responseCode == 200) {
                    result = createMapping(es);
                }
            }
        } catch (Exception e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.info(exceptionMsg);

        }
        return result;
    }

    /**
     * 保存单条数据到ES
     *
     * @param message
     * @return
     */
    public RetData validateParms(EsModel es) {
        RetData dataReturn = new RetData();
        if (es == null) {
            dataReturn.setMessage("json 不能为空");
            return dataReturn;
        }
        String message = es.getMessage();
        String indexName = es.getIndex();
        String type = es.getType();
        if (StringUtil.isBlank(message)) {
            dataReturn.setMessage("message 消息不能为空");
            return dataReturn;
        }
        if (StringUtil.isBlank(indexName)) {
            dataReturn.setMessage("indexName 不能为空");
            return dataReturn;
        }
        if (StringUtil.isBlank(type)) {
            dataReturn.setMessage("type 不能为空");
            return dataReturn;
        }
        dataReturn.setCode(RetCodeAndMessage.SUCCESS);
        return dataReturn;
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
        AddKeyWords(keywords, searchSourceBuilder);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        AddHighLigh(keywords, highlightBuilder);
        searchSourceBuilder.explain(isAccuracySort);
        searchSourceBuilder.from(startIndex);
        searchSourceBuilder.size(pageSize);
        Sort sort = new Sort("time", Sort.Sorting.DESC);
        searchSourceBuilder.highlighter(highlightBuilder);
        search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(indexName).addType(type).addSort(sort).build();

        return search;
    }


    /**
     * 通过SQL统计ES数据
     *
     * @param sql
     * @return
     */
    public JSONObject getEsDataBySQl(String sql) {
        JSONObject jsonObject = null;
        if (StringUtil.isBlank(sql)) {
            return jsonObject;
        }
        String es_http_rest_url = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "es_http_rest_url");//获取kafka地址 包括端口号
        if (StringUtil.isBlank(es_http_rest_url)) {
            return jsonObject;
        }
        es_http_rest_url = es_http_rest_url + "/_xpack/sql";
        JSONObject parms = new JSONObject();
        parms.put("query", sql);
        String doHttpPost = null;
        try {
            //doHttpPost = HttpClientUtil.excuteRequestEntity(kafaka_address, parms, "utf-8", 5000);
            if (StringUtil.isNotBlank(doHttpPost)) {
                JSONObject.parseObject(doHttpPost);
            }
        } catch (Exception e) {
            String exceptionMsg = sql + "=======》" + StringUtil.getExceptionMsg(e);
            logger.error(exceptionMsg);

        }
        return jsonObject;
    }

    /**
     * 增加高亮词
     *
     * @param keywords
     * @param highlightBuilder
     */
    private void AddHighLigh(JSONObject keywords, HighlightBuilder highlightBuilder) {
        if (keywords == null || keywords.size() <= 0) {
            return;
        }
        Iterator<Map.Entry<String, Object>> iterator = keywords.entrySet().iterator();
        if (iterator == null) {
            return;
        }
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

    /**
     * 添加查询条件(等值查询)
     *
     * @param keywords
     * @param searchSourceBuilder
     */
    private void AddKeyWords(JSONObject keywords, SearchSourceBuilder searchSourceBuilder) {
        if (keywords == null || keywords.size() <= 0) {
            return;
        }
        Iterator<Map.Entry<String, Object>> iterator = keywords.entrySet().iterator();
        if (iterator == null) {
            return;
        }
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            if (value == null || value.toString().length() <= 0) {
                continue;
            }
            if ("id".equals(key)) {
                searchSourceBuilder.query(QueryBuilders.termQuery(key, value));
            } else {
                searchSourceBuilder.query(QueryBuilders.commonTermsQuery(key, value));
            }
        }
    }


    /**
     * 按天统计  各个日志级别的数量 查询条件
     *
     * @param EsModel es
     *                keywords
     *                highlights
     * @return
     */
    public Search statisticsDayCondition(EsModel es) {
        Search search = null;
        if (es == null) {
            return search;
        }
        String indexName = es.getIndex();
        String type = es.getType();
        String startTime = es.getStartTime();
        String endTime = es.getEndTime();
        JSONObject keywords = es.getKeyWord();
        if (StringUtil.isBlank(indexName)) {
            return search;
        }
        if (StringUtil.isBlank(type)) {
            return search;
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (StringUtil.isNotBlank(startTime) && StringUtil.isNotBlank(endTime)) {
            RangeQueryBuilder rangequerybuilder = QueryBuilders
                    .rangeQuery("createTime")
                    .from(startTime).to(endTime);
            searchSourceBuilder.query(rangequerybuilder);
        }
        AddKeyWords(keywords, searchSourceBuilder);
        ExtendedBounds extendedBounds = new ExtendedBounds(startTime, endTime);
        AggregationBuilder levelAgg = AggregationBuilders.terms("level_count").field("level").minDocCount(0);
        AggregationBuilder dateAgg = AggregationBuilders.dateHistogram("day_count")
                .field("createTime")
                .dateHistogramInterval(DateHistogramInterval.DAY)
                .format("yyyy-MM-dd")
                .extendedBounds(extendedBounds)
                .minDocCount(0l)//为空0补充
                .timeZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+8")));
        AggregationBuilder builder = dateAgg.subAggregation(levelAgg);
        searchSourceBuilder.aggregation(builder).size(0);
        search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(indexName).addType(type).build();
        return search;
    }

    /**
     * 按天统计 各个日志级别的数量结果处理
     *
     * @param result
     * @return
     */
    public JSONObject getDayStatisticsResult(JestResult result) {
        JSONObject object = null;
        JsonElement aggregations = result.getJsonObject().get("aggregations");
        if (aggregations == null) {
            return object;
        }
        JsonElement day_count = aggregations.getAsJsonObject().get("day_count");
        if (day_count == null) {
            return object;
        }
        JsonArray jsonArray = day_count.getAsJsonObject().getAsJsonArray("buckets");
        if (jsonArray == null || jsonArray.size() <= 0) {
            return object;
        }
        int size = jsonArray.size();
        object = new JSONObject();
        List<String> dayList = new ArrayList<String>();
        List<Integer> dayCountInfoList = new ArrayList<Integer>();
        List<Integer> dayCountErrorList = new ArrayList<Integer>();
        List<Integer> dayCountWarnList = new ArrayList<Integer>();
        List<Integer> dayCountDebugList = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            JsonElement jsonElement = jsonArray.get(i);
            if (jsonElement == null) {
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(jsonElement.toString());
            if (jsonObject == null) {
                continue;
            }
            String day = jsonObject.getString("key_as_string");
            JSONObject levelCount = jsonObject.getJSONObject("level_count");
            if (jsonObject == null || jsonObject.size() <= 0) {
                continue;
            }
            JSONArray buckets = levelCount.getJSONArray("buckets");
            if (jsonArray == null || jsonArray.size() <= 0) {
                return object;
            }
            int sizeLengt = buckets.size();
            dayList.add(day);
            int infoCount = 0;
            int errorCount = 0;
            int warnCount = 0;
            int debugCount = 0;
            for (int j = 0; j < sizeLengt; j++) {
                JSONObject bucketsJSONObject = buckets.getJSONObject(j);
                if (bucketsJSONObject == null) {
                    continue;
                }
                String key = bucketsJSONObject.getString("key");
                if (StringUtil.isBlank(key)) {
                    continue;
                }
                int doc_count = bucketsJSONObject.getIntValue("doc_count");
                String value = "0";
                switch (key) {
                    case "INFO":
                        infoCount = infoCount + doc_count;
                        break;
                    case "ERROR":
                        errorCount = errorCount + doc_count;
                        break;
                    case "WARN":
                        warnCount = warnCount + doc_count;
                        break;
                    case "DEBUG":
                        debugCount = debugCount + doc_count;
                        break;
                    default:
                        debugCount = 0;
                }
            }
            dayCountInfoList.add(infoCount);
            dayCountErrorList.add(errorCount);
            dayCountWarnList.add(warnCount);
            dayCountDebugList.add(debugCount);
        }
        object.put("dayList", dayList);
        object.put("dayCountInfoList", dayCountInfoList);
        object.put("dayCountErrorList", dayCountErrorList);
        object.put("dayCountWarnList", dayCountWarnList);
        object.put("dayCountDebugList", dayCountDebugList);
        return object;
    }

    /**
     * 按天Level统计  各个日志级别的数量 查询条件
     *
     * @param EsModel es
     *                keywords
     *                highlights
     * @return
     */
    public Search statisticsLevelCondition(EsModel es) {
        Search search = null;
        if (es == null) {
            return search;
        }
        String indexName = es.getIndex();
        String type = es.getType();
        String startTime = es.getStartTime();
        String endTime = es.getEndTime();
        JSONObject keywords = es.getKeyWord();
        if (StringUtil.isBlank(indexName)) {
            return search;
        }
        if (StringUtil.isBlank(type)) {
            return search;
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (StringUtil.isNotBlank(startTime) && StringUtil.isNotBlank(endTime)) {
            RangeQueryBuilder rangequerybuilder = QueryBuilders
                    .rangeQuery("createTime")
                    .from(startTime).to(endTime);
            searchSourceBuilder.query(rangequerybuilder);
        }
        AddKeyWords(keywords, searchSourceBuilder);
        ExtendedBounds extendedBounds = new ExtendedBounds(startTime, endTime);
        AggregationBuilder levelAgg = AggregationBuilders.terms("level_count").field("level").minDocCount(0);
        searchSourceBuilder.aggregation(levelAgg).size(0);
        search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(indexName).addType(type).build();
        return search;
    }

    /**
     * 按天Level统计 各个日志级别的数量
     *
     * @param result
     * @return
     */
    public JSONObject getLevelStatisticsResult(JestResult result) {
        JSONObject object = null;
        JsonElement aggregations = result.getJsonObject().get("aggregations");
        if (aggregations == null) {
            return object;
        }
        JsonElement day_count = aggregations.getAsJsonObject().get("level_count");
        if (day_count == null) {
            return object;
        }
        JsonArray jsonArray = day_count.getAsJsonObject().getAsJsonArray("buckets");
        if (jsonArray == null || jsonArray.size() <= 0) {
            return object;
        }
        int size = jsonArray.size();
        object = new JSONObject();
        for (int i = 0; i < size; i++) {
            JsonElement jsonElement = jsonArray.get(i);
            if (jsonElement == null) {
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(jsonElement.toString());
            if (jsonObject == null) {
                continue;
            }
            String key = jsonObject.getString("key");
            String doc_count = jsonObject.getString("doc_count");
            if (StringUtil.isBlank(doc_count)) {
                doc_count = "0";
            }
            switch (key) {
                case "INFO":
                    object.put("infoCount", doc_count);
                    break;
                case "ERROR":
                    object.put("errorCount", doc_count);
                    break;
                case "WARN":
                    object.put("warnCount", doc_count);
                    break;
                case "DEBUG":
                    object.put("debugCount", doc_count);
                    break;
                default:
                    object.put("debugCount", doc_count);
            }
        }
        return object;
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
            if (jsonObject == null || jsonObject.size() <= 0) {
                continue;
            }
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
            if ("id".equals(key)) {
                continue;
            }
            jsonObject.put(key, value);
        }
        return jsonObject;
    }
}
