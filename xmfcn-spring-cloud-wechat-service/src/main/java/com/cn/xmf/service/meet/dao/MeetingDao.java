package com.cn.xmf.service.meet.dao;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Meeting;

/**
 * 参会人员信息登记DAO接口
 * @author rufen.cn
 * @version 2019-10-26
 */
@SuppressWarnings("all")
public interface MeetingDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param meeting
	 * @return
	 */
	public void add(Meeting meeting);

    /**
	 * 批量数据增加
	 * @param meeting
	 * @return
	 */
	 public void addTrainRecordBatch(List<Meeting> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public Meeting getMeetingById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(Meeting meeting);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<Meeting>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<Meeting>  getMeetingList(Meeting meeting);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public Meeting  getSignleMeeting(Meeting meeting);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}