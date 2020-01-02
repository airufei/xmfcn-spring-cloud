package com.cn.xmf.service.prize.dao;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.WinnersList;

/**
 * 获奖名单DAO接口
 * @author rufei.cn
 * @version 2020-01-02
 */
@SuppressWarnings("all")
public interface WinnersListDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param winnersList
	 * @return
	 */
	public void add(WinnersList winnersList);

    /**
	 * 批量数据增加
	 * @param winnersList
	 * @return
	 */
	 public void addTrainRecordBatch(List<WinnersList> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public WinnersList getWinnersListById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(WinnersList winnersList);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<WinnersList>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<WinnersList>  getWinnersListList(WinnersList winnersList);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public WinnersList  getSignleWinnersList(WinnersList winnersList);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}