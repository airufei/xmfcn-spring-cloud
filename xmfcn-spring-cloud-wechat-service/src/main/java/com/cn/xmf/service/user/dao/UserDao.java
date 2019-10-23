package com.cn.xmf.service.user.dao;


import com.cn.xmf.model.wx.User;

import java.util.List;
import java.util.Map;

/**
 * 微信用户DAO接口
 * @author airufei
 * @version 2018-07-09
 */
@SuppressWarnings("all")
public interface UserDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param user
	 * @return
	 */
	public void add(User user);

    /**
	 * 批量数据增加
	 * @param user
	 * @return
	 */
	 public void addTrainRecordBatch(List<User> list);

     /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	 public User getWxUserById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(User user);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<User>  getList(Map map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<User>  getWxUserList(User user);
	   
	   /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Long  getTotalCount(Map map);
	   
}