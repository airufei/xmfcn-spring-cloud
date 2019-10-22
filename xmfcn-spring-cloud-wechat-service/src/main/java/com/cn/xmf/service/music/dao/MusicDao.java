package com.cn.xmf.service.music.dao;


import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Music;

/**
 * 微信音乐DAO接口
 * @author rufei.cn
 * @version 2019-10-21
 */
@SuppressWarnings("all")
public interface MusicDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param music
	 * @return
	 */
	public void add(Music music);

    /**
	 * 批量数据增加
	 * @param music
	 * @return
	 */
	 public void addTrainRecordBatch(List<Music> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public Music getMusicById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(Music music);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<Music>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<Music>  getMusicList(Music music);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public Music  getSignleMusic(Music music);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Long  getTotalCount(Map map);
	   
}