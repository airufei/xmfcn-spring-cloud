package com.cn.xmf.service.msg.dao;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.WxUserMessage;

import java.util.List;
import java.util.Map;

/**
 * 微信留言DAO接口
 * @author rufei.cn
 * @version 2019-10-15
 */
@SuppressWarnings("all")
public interface WxUserMessageDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param wxUserMessage
	 * @return
	 */
	public void add(WxUserMessage wxUserMessage);

    /**
	 * 批量数据增加
	 * @param wxUserMessage
	 * @return
	 */
	 public void addTrainRecordBatch(List<WxUserMessage> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public WxUserMessage getWxUserMessageById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(WxUserMessage wxUserMessage);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<WxUserMessage>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<WxUserMessage>  getWxUserMessageList(WxUserMessage wxUserMessage);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public WxUserMessage  getSignleWxUserMessage(WxUserMessage wxUserMessage);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}