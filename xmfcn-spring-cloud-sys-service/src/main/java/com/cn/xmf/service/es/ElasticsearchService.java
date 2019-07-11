package com.cn.xmf.service.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.es.EsModel;
import com.cn.xmf.model.es.EsPage;
import com.cn.xmf.model.es.EsPartion;
import com.cn.xmf.util.StringUtil;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ES服务
 *
 * @version 2019-05-23
 * @Author rufei.cn
 */
@RestController
@RequestMapping(value = "/server/es/")
@SuppressWarnings("all")
public class ElasticsearchService {
    private static Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);
    @Autowired
    private JestClient jestClient;

    @Autowired
    private ElasticsearchHelperService elasticsearchHelperService;


    /**
     * 保存单条数据到ES
     *
     * @param message
     * @return
     */
    @RequestMapping("save")
    public RetData save(@RequestBody EsModel es) {
        RetData retData = new RetData();
        RetData aReturn = elasticsearchHelperService.validateParms(es);
        if (aReturn.getCode() == RetCodeAndMessage.FAILURE) {
            return aReturn;
        }
        String message = es.getMessage();
        String indexName = es.getIndex();
        String type = es.getType();
        try {
            JestResult jestResult = elasticsearchHelperService.createIndex(es);
            if (jestResult == null) {
                return aReturn;
            }
            int responseCode = jestResult.getResponseCode();
            if (responseCode != 200) {
                return aReturn;
            }
            Index index = new Index.Builder(message).index(indexName).type(type).build();
            DocumentResult result = jestClient.execute(index);
            responseCode = result.getResponseCode();
            if (responseCode == 200) {
                retData.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
                retData.setCode(RetCodeAndMessage.SUCCESS);
            } else {
                retData.setMessage(result.getErrorMessage());
            }
        } catch (Exception e) {

            String msg = StringUtil.getExceptionMsg(e);
            StringBuilder builder = new StringBuilder();
            builder.append("saveBatch(保存集合数据到ES) 参数 es:").append(JSON.toJSONString(es)).append(" \n\n错误消息：").append(msg);
            logger.error(builder.toString());
            retData.setMessage(msg);
        }
        return retData;
    }

    /**
     * saveBatch(保存集合数据到ES)
     *
     * @param message
     * @return
     */
    @RequestMapping("saveBatch")
    public RetData saveBatch(@RequestBody EsModel es) {
        RetData retData = new RetData();
        RetData aReturn = elasticsearchHelperService.validateParms(es);
        if (aReturn.getCode() == RetCodeAndMessage.FAILURE) {
            return aReturn;
        }
        String message = es.getMessage();
        String indexName = es.getIndex();
        String type = es.getType();
        try {
            List<JSONObject> list = es.getList();
            if (list == null || list.size() <= 0) {
                retData.setMessage("list 消息不能为空");
                return retData;
            }
            JSONObject object = list.get(0);
            es.setMessage(JSON.toJSONString(object));
            JestResult jestResult = elasticsearchHelperService.createIndex(es);
            if (jestResult == null) {
                return aReturn;
            }
            int responseCode = jestResult.getResponseCode();
            if (responseCode != 200) {
                return aReturn;
            }
            Bulk.Builder bulk = new Bulk.Builder();
            for (JSONObject entity : list) {
                Index index = new Index.Builder(entity).index(indexName).type(type).build();
                bulk.addAction(index);
            }
            BulkResult result = jestClient.execute(bulk.build());
            responseCode = result.getResponseCode();
            if (responseCode == 200) {
                retData.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
                retData.setCode(RetCodeAndMessage.SUCCESS);
            } else {
                String errorMessage = "saveBatch(保存集合数据到ES):" + result.getErrorMessage();
                logger.error(errorMessage);
                retData.setMessage(result.getErrorMessage());
            }
        } catch (Exception e) {

            String msg = StringUtil.getExceptionMsg(e);
            StringBuilder builder = new StringBuilder();
            builder.append("saveBatch(保存集合数据到ES) 参数 es:").append(JSON.toJSONString(es)).append(" \n\n错误消息：").append(msg);
            logger.error(builder.toString());
            retData.setMessage(msg);
        }
        return retData;
    }


    /**
     * search(根据 关键词查询ES信息)
     *
     * @param EsModel es
     *                keywords
     *                highlights
     * @return
     */
    @RequestMapping("search")
    public EsPartion search(@RequestBody EsModel es) {
        EsPartion pt = null;
        try {
            Search search = elasticsearchHelperService.searchCondition(es);
            if (search == null) {
                return pt;
            }
            EsPage esPage = es.getEsPage();
            JestResult result = jestClient.execute(search);
            int responseCode = -1;
            if (result != null) {
                responseCode = result.getResponseCode();
            }
            if (responseCode != 200) {
                logger.error("搜索错误：" + result);
                return pt;
            }
            pt = elasticsearchHelperService.getSearchResult(result, esPage);
        } catch (Exception e) {

            String msg = StringUtil.getExceptionMsg(e);
            StringBuilder builder = new StringBuilder();
            builder.append("search(根据 关键词查询ES信息) 参数 es:").append(JSON.toJSONString(es)).append(" \n\n错误消息：").append(msg);
            logger.error(builder.toString());
        }
        return pt;
    }

    /**
     * getStatisticsCountByLevel(按日志级别统计时间内的数据)
     *
     * @param EsModel es
     *                startTime
     *                endTime
     * @return
     */
    @RequestMapping("getStatisticsCountByLevel")
    public JSONObject getStatisticsCountByLevel(@RequestBody EsModel es) {
        JSONObject resJson = null;
        try {
            Search search = elasticsearchHelperService.statisticsLevelCondition(es);
            if (search == null) {
                return resJson;
            }
            EsPage esPage = es.getEsPage();
            JestResult result = jestClient.execute(search);
            logger.info("getStatisticsCountByLevel " + result.getJsonString());
            int responseCode = -1;
            if (result != null) {
                responseCode = result.getResponseCode();
            }
            if (responseCode != 200) {
                logger.error("ES 搜索错误信息：" + result.getErrorMessage());
                return resJson;
            }
            resJson = elasticsearchHelperService.getLevelStatisticsResult(result);
        } catch (Exception e) {

            String msg = StringUtil.getExceptionMsg(e);
            StringBuilder builder = new StringBuilder();
            builder.append("getStatisticsCountByLevel(按日志级别统计时间内的数据) 参数 es:").append(JSON.toJSONString(es)).append(" \n\n错误消息：").append(msg);
            logger.error(builder.toString());
        }
        return resJson;
    }


    /**
     * getStatisticsCountByDay(按天统计时间内的数据)
     *
     * @param EsModel es
     *                startTime
     *                endTime
     * @return
     */
    @RequestMapping("getStatisticsCountByDay")
    public JSONObject getStatisticsCountByDay(@RequestBody EsModel es) {
        JSONObject resJson = null;
        try {
            Search search = elasticsearchHelperService.statisticsDayCondition(es);
            if (search == null) {
                return resJson;
            }
            EsPage esPage = es.getEsPage();
            JestResult result = jestClient.execute(search);
            logger.info("getStatisticsCountByDay :" + result.getJsonString());
            int responseCode = -1;
            if (result != null) {
                responseCode = result.getResponseCode();
            }
            if (responseCode != 200) {
                logger.error("ES 搜索错误信息：" + result.getErrorMessage());
                return resJson;
            }
            resJson = elasticsearchHelperService.getDayStatisticsResult(result);
        } catch (Exception e) {

            String msg = StringUtil.getExceptionMsg(e);
            StringBuilder builder = new StringBuilder();
            builder.append("getStatisticsCountByDay(按天统计时间内的数据) 参数 es:").append(JSON.toJSONString(es)).append(" \n\n错误消息：").append(msg);
            logger.error(builder.toString());
        }
        return resJson;
    }
}
