package com.cn.xmf.service.comment.dao;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Comment;

import java.util.List;
import java.util.Map;

/**
 * 微信留言DAO接口
 * @author rufei.cn
 * @version 2019-10-15
 */
@SuppressWarnings("all")
public interface CommentDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param comment
	 * @return
	 */
	public void add(Comment comment);

    /**
	 * 批量数据增加
	 * @param comment
	 * @return
	 */
	 public void addTrainRecordBatch(List<Comment> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public Comment getWxUserMessageById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(Comment comment);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<Comment>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<Comment>  getWxUserMessageList(Comment comment);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public Comment getSignleWxUserMessage(Comment comment);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}