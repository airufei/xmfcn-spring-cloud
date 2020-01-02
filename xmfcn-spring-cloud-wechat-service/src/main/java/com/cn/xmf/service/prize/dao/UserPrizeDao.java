package com.cn.xmf.service.prize.dao;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.UserPrize;

import java.util.List;
import java.util.Map;

/**
 * 奖品信息DAO接口
 * @author rufei.cn
 * @version 2020-01-02
 */
@SuppressWarnings("all")
public interface UserPrizeDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param userPrize
	 * @return
	 */
	public void add(UserPrize userPrize);

    /**
	 * 批量数据增加
	 * @param userPrize
	 * @return
	 */
	 public void addTrainRecordBatch(List<UserPrize> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public UserPrize getUserPrizeById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(UserPrize userPrize);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<UserPrize>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<UserPrize>  getUserPrizeList(UserPrize userPrize);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public UserPrize  getSignleUserPrize(UserPrize userPrize);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}