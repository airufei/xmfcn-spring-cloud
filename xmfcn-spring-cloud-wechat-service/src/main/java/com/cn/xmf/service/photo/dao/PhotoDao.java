package com.cn.xmf.service.photo.dao;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Photo;

import java.util.List;
import java.util.Map;
/**
 * 微信照片DAO接口
 * @author rufei
 * @version 2019-10-11
 */
@SuppressWarnings("all")
public interface PhotoDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param photo
	 * @return
	 */
	public void add(Photo photo);

    /**
	 * 批量数据增加
	 * @param photo
	 * @return
	 */
	 public void addTrainRecordBatch(List<Photo> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public Photo getWxPhotoById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(Photo photo);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<Photo>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<Photo>  getWxPhotoList(Photo photo);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public Photo getSignleWxPhoto(Photo photo);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}