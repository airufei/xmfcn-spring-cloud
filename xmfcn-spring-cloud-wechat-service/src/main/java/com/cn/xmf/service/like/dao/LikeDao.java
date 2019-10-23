package com.cn.xmf.service.like.dao;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Like;

import java.util.List;
import java.util.Map;

/**
 * 微信点赞DAO接口
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
@SuppressWarnings("all")
public interface LikeDao {

    /**
     * 删除数据（逻辑删除）
     *
     * @param question
     * @return
     */
    public void delete(long id);

    /**
     * 单条数据增加
     *
     * @param like
     * @return
     */
    public void add(Like like);

    /**
     * 批量数据增加
     *
     * @param like
     * @return
     */
    public void addTrainRecordBatch(List<Like> list);

    /**
     * 根据ID获取单条数据
     *
     * @param id
     * @return
     */
    public Like getWxUserLikeById(long id);

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(Like like);

    /**
     * 获取分页数据
     *
     * @param map
     * @return
     */
    public List<Like> getList(JSONObject map);


    /**
     * 获取集合数据，不带分页
     *
     * @param map
     * @return
     */
    public List<Like> getWxUserLikeList(Like like);

    /**
     * 获取单条数据
     *
     * @param map
     * @return
     */
    public Like getSignleWxUserLike(Like like);

    /**
     * 获取分页记录总数
     *
     * @param map
     * @return
     */
    public Long getTotalCount(Map map);

    /**
     * 点赞数量
     *
     * @param map
     * @return
     */
    public Long getLikeCount(Map map);


}