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
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
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
        if (es == null) {
            retData.setMessage("json 不能为空");
            return retData;
        }
        String message = es.getMessage();
        String indexName = es.getIndex();
        String type = es.getType();
        try {
            RetData aReturn = elasticsearchHelperService.validateParms(es);
            if (aReturn.getCode() == RetCodeAndMessage.SYS_ERROR) {
                return aReturn;
            }
            JSONObject messageJson = JSONObject.parseObject(message);
            Index index = new Index.Builder(messageJson).index(indexName).type(type).build();
            jestClient.execute(index);
            retData.setCode(RetCodeAndMessage.SUCCESS);
            retData.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
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
        if (es == null) {
            retData.setMessage("json 不能为空");
            return retData;
        }
        String message = es.getMessage();
        String indexName = es.getIndex();
        String type = es.getType();
        try {
            RetData aReturn = elasticsearchHelperService.validateParms(es);
            if (aReturn.getCode() == RetCodeAndMessage.SYS_ERROR) {
                return aReturn;
            }
            List<JSONObject> list = JSONObject.parseArray(message, JSONObject.class);
            if (list == null || list.size() <= 0) {
                retData.setMessage("list 消息不能为空");
                return retData;
            }
            Bulk.Builder bulk = new Bulk.Builder();
            for (JSONObject entity : list) {
                Index index = new Index.Builder(entity).index(indexName).type(type).build();
                bulk.addAction(index);
            }
            jestClient.execute(bulk.build());
            retData.setCode(RetCodeAndMessage.SUCCESS);
            retData.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
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
                return pt;
            }
            pt = elasticsearchHelperService.getSearchResult(result, esPage);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = StringUtil.getExceptionMsg(e);
            StringBuilder builder = new StringBuilder();
            builder.append("search(根据 关键词查询ES信息) 参数 es:").append(JSON.toJSONString(es)).append(" \n\n错误消息：").append(msg);
            logger.error(builder.toString());
        }
        return pt;
    }
}
