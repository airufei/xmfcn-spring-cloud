package com.cn.xmf.service.user.dao;


import com.cn.xmf.model.wx.WxUser;

import java.util.List;
import java.util.Map;

/**
 * 微信用户DAO接口
 * @author airufei
 * @version 2018-07-09
 */
@SuppressWarnings("all")
public interface WxUserDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param wxUser
	 * @return
	 */
	public void add(WxUser wxUser);

    /**
	 * 批量数据增加
	 * @param wxUser
	 * @return
	 */
	 public void addTrainRecordBatch(List<WxUser> list);

     /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	 public WxUser getWxUserById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(WxUser wxUser);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<WxUser>  getList(Map map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<WxUser>  getWxUserList(WxUser wxUser);
	   
	   /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public int  getTotalCount(Map map);
	   
}