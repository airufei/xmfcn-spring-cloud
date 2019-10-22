package com.cn.xmf.service.msg.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.WxUserMessage;
import com.cn.xmf.service.msg.dao.WxUserMessageDao;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.LocalCacheUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * WxUserMessage Service(微信留言辅助类)
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
@Service
@SuppressWarnings("all")
public class WxUserMessageHelperService {
    private static Logger logger = LoggerFactory.getLogger(WxUserMessageHelperService.class);

    @Autowired
    private WxUserMessageDao wxUserMessageDao;

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public long getTotalCount(JSONObject map) {
        long resCount = 0;
        Long totalCount = wxUserMessageDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }


    /*
     * save(保存微信留言)
     * @param wxUserMessage
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.REPEATABLE_READ, timeout = 30, rollbackFor = Exception.class)
    public WxUserMessage save(WxUserMessage wxUserMessage) throws Exception {
        WxUserMessage ret = null;
        if (wxUserMessage == null) {
            return ret;
        }
        if (wxUserMessage.getId() != null && wxUserMessage.getId() > 0) {
            updateById(wxUserMessage);
            ret = wxUserMessage;
        } else {
            wxUserMessage.setId(null);
            wxUserMessageDao.add(wxUserMessage);
            ret = wxUserMessage;
        }
        return ret;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public WxUserMessage getWxUserMessageById(long id) {
        return wxUserMessageDao.getWxUserMessageById(id);
    }

    /**
     * 获取单条数据
     *
     * @param wxUserMessage
     * @return
     * @author rufei.cn
     */
    public long getPhotoCommentCount(String bizId, String type) {
        long count = 0;
        JSONObject map = new JSONObject();
        map.put("type", type);
        map.put("bizid", bizId);
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + "getPhotoCommentCount_" + bizId + type;
        String cache = LocalCacheUtil.getCache(key);
        if (StringUtil.isNotBlank(cache)) {
            count = StringUtil.stringToLong(cache);
        }
        if (count > 0) {
            return count;
        }
        count = getTotalCount(map);
        LocalCacheUtil.saveCache(key, String.valueOf(count), 60);
        return count;
    }

    /**
     * 获取单条数据
     *
     * @param wxUserMessage
     * @return
     * @author rufei.cn
     */
    public WxUserMessage getSignleWxUserMessage(WxUserMessage wxUserMessage) {
        WxUserMessage ret = null;
        if (wxUserMessage == null) {
            return ret;
        }
        String bizId = wxUserMessage.getBizid();
        String type = wxUserMessage.getType();
        if (StringUtil.isBlank(bizId)) {
            return ret;
        }
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + bizId + type;
        String cache = LocalCacheUtil.getCache(key);
        if (StringUtil.isNotBlank(cache)) {
            ret = JSONObject.parseObject(cache, WxUserMessage.class);
        }
        if (ret != null) {
            return ret;
        }
        ret = wxUserMessageDao.getSignleWxUserMessage(wxUserMessage);
        if (ret != null) {
            LocalCacheUtil.saveCache(key, wxUserMessage.toString(), 60);
        }
        return ret;
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    private void updateById(WxUserMessage wxUserMessage) {
        wxUserMessageDao.updateById(wxUserMessage);
    }

}