package com.cn.xmf.job.admin.menu.dao;

import com.cn.xmf.job.admin.menu.model.*;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;

/**
 * job-菜单DAO接口
 * @author airufei
 * @version 2018-10-10
 */
@SuppressWarnings("all")
public interface JobMenuDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param jobMenu
	 * @return
	 */
	public void add(JobMenu jobMenu);

    /**
	 * 批量数据增加
	 * @param jobMenu
	 * @return
	 */
	 public void addTrainRecordBatch(List<JobMenu> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public JobMenu getJobMenuById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(JobMenu jobMenu);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<JobMenu>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<JobMenu>  getJobMenuList(JobMenu jobMenu);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public JobMenu  getSignleJobMenu(JobMenu jobMenu);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}