package com.cn.xmf.job.admin.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.user.JobUser;

import java.util.List;
import java.util.Map;

/**
 * 调度系统用户DAO接口
 * @author airufei
 * @version 2018-09-18
 */
@SuppressWarnings("all")
public interface JobUserDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param jobUser
	 * @return
	 */
	public void add(JobUser jobUser);

    /**
	 * 批量数据增加
	 * @param jobUser
	 * @return
	 */
	 public void addTrainRecordBatch(List<JobUser> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public JobUser getJobUserById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(JobUser jobUser);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<JobUser>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<JobUser>  getJobUserList(JobUser jobUser);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public JobUser  getSignleJobUser(JobUser jobUser);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}