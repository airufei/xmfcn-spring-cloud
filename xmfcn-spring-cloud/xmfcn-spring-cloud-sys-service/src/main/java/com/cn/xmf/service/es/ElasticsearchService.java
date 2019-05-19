package com.cn.xmf.service.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.base.model.RetMessage;
import com.cn.xmf.model.sys.EsModel;
import com.cn.xmf.util.StringUtil;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ES服务
 *
 * @author rufei.cn
 * @version 2019-05-19
 */
@RestController
@RequestMapping(value = "/server/es/")
@SuppressWarnings("all")
public class ElasticsearchService {
    private static Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);
    @Autowired
    private JestClient jestClient;

    /**
     * 保存单条数据到ES
     *
     * @param message
     * @return
     */
    @RequestMapping("save")
    public RetData save(@RequestBody EsModel es) {
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
        try {
            JSONObject messageJson = JSONObject.parseObject(message);
            Index index = new Index.Builder(messageJson).index(indexName).type(type).build();
            jestClient.execute(index);
            dataReturn.setCode(RetCodeAndMessage.SUCCESS);
            dataReturn.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            dataReturn.setMessage(exceptionMsg);
            logger.error(exceptionMsg);
        }
        return dataReturn;
    }

    /**
     * 保存集合数据到ES
     *
     * @param message
     * @return
     */
    @RequestMapping("saveBatch")
    public RetData saveBatch(@RequestBody EsModel es) {
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
        try {
            List<JSONObject> list = JSONObject.parseArray(message, JSONObject.class);
            if (list == null || list.size() <= 0) {
                dataReturn.setMessage("list 消息不能为空");
                return dataReturn;
            }
            Bulk.Builder bulk = new Bulk.Builder();
            for (JSONObject entity : list) {
                Index index = new Index.Builder(entity).index(indexName).type(type).build();
                bulk.addAction(index);
            }
            jestClient.execute(bulk.build());
            dataReturn.setCode(RetCodeAndMessage.SUCCESS);
            dataReturn.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            dataReturn.setMessage(exceptionMsg);
            logger.error(exceptionMsg);
        }
        return dataReturn;
    }

    /**
     * search根据 关键词查询ES信息
     *
     * @param EsModel es
     *                keywords
     *                highlights
     * @return
     */
    @RequestMapping("search")
    public List<JSONObject> search(@RequestBody EsModel es) {
        List<JSONObject> list = null;
        if (es == null) {
            return list;
        }
        JSONObject json = es.getParms();
        if (json == null) {
            return list;
        }
        JSONObject keywords = json.getJSONObject("keywords");
        JSONArray jsonArray = json.getJSONArray("highlights");
        String indexName = es.getIndex();
        String type = es.getType();
        if (StringUtil.isBlank(indexName)) {
            return list;
        }
        if (StringUtil.isBlank(type)) {
            return list;
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
                searchSourceBuilder.query(QueryBuilders.commonTermsQuery(key, value));
            }
        }
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        if (jsonArray != null && jsonArray.size() > 0) {
            List<String> strings = jsonArray.toJavaList(String.class);
            for (String field : strings) {
                highlightBuilder.field(field);//高亮field
                highlightBuilder.preTags("<em>").postTags("</em>");//高亮标签
                highlightBuilder.fragmentSize(200);//高亮内容长度
            }
        }
        searchSourceBuilder.highlighter(highlightBuilder);
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(indexName).addType(type).build();
        try {
            JestResult result = jestClient.execute(search);
            list = result.getSourceAsObjectList(JSONObject.class);
        } catch (IOException e) {
            e.printStackTrace();
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.error(exceptionMsg);
        }
        return list;
    }
}
