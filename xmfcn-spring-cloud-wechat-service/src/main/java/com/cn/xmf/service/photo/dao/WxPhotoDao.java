package com.cn.xmf.service.photo.dao;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.WxPhoto;

import java.util.List;
import java.util.Map;
/**
 * 微信照片DAO接口
 * @author airufei
 * @version 2019-10-11
 */
@SuppressWarnings("all")
public interface WxPhotoDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param wxPhoto
	 * @return
	 */
	public void add(WxPhoto wxPhoto);

    /**
	 * 批量数据增加
	 * @param wxPhoto
	 * @return
	 */
	 public void addTrainRecordBatch(List<WxPhoto> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public WxPhoto getWxPhotoById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(WxPhoto wxPhoto);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<WxPhoto>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<WxPhoto>  getWxPhotoList(WxPhoto wxPhoto);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public WxPhoto  getSignleWxPhoto(WxPhoto wxPhoto);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Long  getTotalCount(Map map);
	   
}