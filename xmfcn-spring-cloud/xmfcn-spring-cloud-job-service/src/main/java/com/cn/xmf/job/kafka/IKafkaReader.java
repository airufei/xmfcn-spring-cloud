package com.cn.xmf.job.kafka;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetData;

/**
 * kafka消费数据接口
 *
 * @author rufei.cn
 * @version 2019-05-19
 */
public interface IKafkaReader {

    /**
     * 获取kafka数据，执行业务操作
     *
     * @param jsonObject
     * @return
     */
    public RetData execute(JSONObject jsonObject);
}
