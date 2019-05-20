package com.cn.xmf.service.es;

import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.sys.EsModel;
import com.cn.xmf.model.sys.LogMessage;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志 ES服务
 *
 * @author rufei.cn
 * @version 2019-05-19
 */
@RestController
@RequestMapping(value = "/server/loges/")
@SuppressWarnings("all")
public class LogElasticsearchService {
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
            dataReturn.setMessage("logMessage 不能为空");
            return dataReturn;
        }
        String message = es.getMessage();
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setIndexName(es.getIndex());
        indexQuery.setType(es.getType());
        indexQuery.setObject(message);
        elasticsearchTemplate.index(indexQuery);
        return dataReturn;
    }
}
