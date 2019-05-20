package com.cn.xmf.service.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.sys.EsModel;
import com.cn.xmf.model.sys.EsPage;
import com.cn.xmf.util.StringUtil;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
    private ElasticsearchTemplate elasticsearchTemplate;

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
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setIndexName(es.getIndex());
            indexQuery.setType(es.getType());
            indexQuery.setObject(message);
            String index = elasticsearchTemplate.index(indexQuery);
            dataReturn.setCode(RetCodeAndMessage.SUCCESS);
            dataReturn.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
        } catch (Exception e) {
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
        JSONObject json = es.getKeyWord();
        if (json == null) {
            return list;
        }
        EsPage esPage = es.getEsPage();
        Pageable pageable = PageRequest.of(esPage.pageNo, esPage.pageSize);
        JSONObject keywords = es.getKeyWord();
        JSONObject highword = es.getHightWord();
        String indexName = es.getIndex();
        String type = es.getType();
        if (StringUtil.isBlank(indexName)) {
            return list;
        }
        if (StringUtil.isBlank(type)) {
            return list;
        }
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
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
                boolQueryBuilder.must(QueryBuilders.matchQuery(key, value));
            }
        }
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        if (highword != null && highword.size() > 0) {
            Iterator<Map.Entry<String, Object>> iterator = highword.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                if (value == null || value.toString().length() <= 0) {
                    continue;
                }
                boolQueryBuilder.must(QueryBuilders.matchQuery(key, value));
            }
        }
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();
        try {
            AggregatedPage<JSONObject> jsonObjects = elasticsearchTemplate.queryForPage(searchQuery, JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.error(exceptionMsg);
        }
        return list;
    }
}
