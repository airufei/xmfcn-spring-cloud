package com.cn.xmf.service.invit.dao;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Invitation;

import java.util.List;
import java.util.Map;

/**
 * 邀请函DAO接口
 *
 * @author rufei.cn
 * @version 2019-10-26
 */
@SuppressWarnings("all")
public interface InvitationDao {

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
     * @param invitation
     * @return
     */
    public void add(Invitation invitation);

    /**
     * 批量数据增加
     *
     * @param invitation
     * @return
     */
    public void addTrainRecordBatch(List<Invitation> list);

    /**
     * 根据ID获取单条数据
     *
     * @param id
     * @return
     */
    public Invitation getInvitationById(long id);

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(Invitation invitation);

    /**
     * 获取分页数据
     *
     * @param map
     * @return
     */
    public List<Invitation> getList(JSONObject map);


    /**
     * 获取集合数据，不带分页
     *
     * @param map
     * @return
     */
    public List<Invitation> getInvitationList(Invitation invitation);

    /**
     * 获取单条数据
     *
     * @param map
     * @return
     */
    public Invitation getSignleInvitation(Invitation invitation);

    /**
     * 获取分页记录总数
     *
     * @param map
     * @return
     */
    public Integer getTotalCount(Map map);

}