package com.cn.xmf.service.like.dao;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.WxUserLike;

import java.util.List;
import java.util.Map;

/**
 * 微信点赞DAO接口
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
@SuppressWarnings("all")
public interface WxUserLikeDao {

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
     * @param wxUserLike
     * @return
     */
    public void add(WxUserLike wxUserLike);

    /**
     * 批量数据增加
     *
     * @param wxUserLike
     * @return
     */
    public void addTrainRecordBatch(List<WxUserLike> list);

    /**
     * 根据ID获取单条数据
     *
     * @param id
     * @return
     */
    public WxUserLike getWxUserLikeById(long id);

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(WxUserLike wxUserLike);

    /**
     * 获取分页数据
     *
     * @param map
     * @return
     */
    public List<WxUserLike> getList(JSONObject map);


    /**
     * 获取集合数据，不带分页
     *
     * @param map
     * @return
     */
    public List<WxUserLike> getWxUserLikeList(WxUserLike wxUserLike);

    /**
     * 获取单条数据
     *
     * @param map
     * @return
     */
    public WxUserLike getSignleWxUserLike(WxUserLike wxUserLike);

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