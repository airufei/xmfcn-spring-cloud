package com.cn.xmf.job.admin.role.dao;

import com.cn.xmf.job.admin.role.model.*;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
/**
 * 角色菜单关系DAO接口
 * @author rufei.cn
 * @version 2018-12-19
 */
@SuppressWarnings("all")
public interface JobMenuRoleDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long roleId);
    /**
	 * 单条数据增加
	 * @param jobMenuRole
	 * @return
	 */
	public void add(JobMenuRole jobMenuRole);

    /**
	 * 批量数据增加
	 * @param jobMenuRole
	 * @return
	 */
	 public void addTrainRecordBatch(List<JobMenuRole> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public JobMenuRole getJobMenuRoleById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(JobMenuRole jobMenuRole);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<JobMenuRole>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<JobMenuRole>  getJobMenuRoleList(JobMenuRole jobMenuRole);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public JobMenuRole  getSignleJobMenuRole(JobMenuRole jobMenuRole);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}