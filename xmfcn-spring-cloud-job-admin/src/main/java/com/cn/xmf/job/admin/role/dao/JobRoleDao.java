package com.cn.xmf.job.admin.role.dao;

import com.cn.xmf.job.admin.role.model.*;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
/**
 * 角色数据DAO接口
 * @author rufei.cn
 * @version 2018-12-19
 */
@SuppressWarnings("all")
public interface JobRoleDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param jobRole
	 * @return
	 */
	public void add(JobRole jobRole);

    /**
	 * 批量数据增加
	 * @param jobRole
	 * @return
	 */
	 public void addTrainRecordBatch(List<JobRole> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public JobRole getJobRoleById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(JobRole jobRole);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<JobRole>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<JobRole>  getJobRoleList(JobRole jobRole);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public JobRole  getSignleJobRole(JobRole jobRole);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}