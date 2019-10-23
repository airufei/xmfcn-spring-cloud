package com.cn.xmf.service.comment.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Comment;
import com.cn.xmf.service.comment.dao.CommentDao;
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
 * Comment Service(微信留言辅助类)
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
@Service
@SuppressWarnings("all")
public class CommentHelperService {
    private static Logger logger = LoggerFactory.getLogger(CommentHelperService.class);

    @Autowired
    private CommentDao commentDao;

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public long getTotalCount(JSONObject map) {
        long resCount = 0;
        Long totalCount = commentDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }


    /*
     * save(保存微信留言)
     * @param comment
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.REPEATABLE_READ, timeout = 30, rollbackFor = Exception.class)
    public Comment save(Comment comment) throws Exception {
        Comment ret = null;
        if (comment == null) {
            return ret;
        }
        if (comment.getId() != null && comment.getId() > 0) {
            updateById(comment);
            ret = comment;
        } else {
            comment.setId(null);
            commentDao.add(comment);
            ret = comment;
        }
        return ret;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public Comment getWxUserMessageById(long id) {
        return commentDao.getWxUserMessageById(id);
    }

    /**
     * 获取单条数据
     *
     * @param comment
     * @return
     * @author rufei.cn
     */
    public long getPhotoCommentCount(String bizId, String type) {
        long count = 0;
        JSONObject map = new JSONObject();
        map.put("type", type);
        map.put("bizId", bizId);
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
     * @param comment
     * @return
     * @author rufei.cn
     */
    public Comment getSignleWxUserMessage(Comment comment) {
        Comment ret = null;
        if (comment == null) {
            return ret;
        }
        String bizId = comment.getBizId();
        String type = comment.getType();
        if (StringUtil.isBlank(bizId)) {
            return ret;
        }
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + bizId + type;
        String cache = LocalCacheUtil.getCache(key);
        if (StringUtil.isNotBlank(cache)) {
            ret = JSONObject.parseObject(cache, Comment.class);
        }
        if (ret != null) {
            return ret;
        }
        ret = commentDao.getSignleWxUserMessage(comment);
        if (ret != null) {
            LocalCacheUtil.saveCache(key, comment.toString(), 60);
        }
        return ret;
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    private void updateById(Comment comment) {
        commentDao.updateById(comment);
    }

}