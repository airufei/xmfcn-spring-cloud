package com.cn.xmf.service.like.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Like;
import com.cn.xmf.service.like.dao.LikeDao;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.LocalCacheUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * Like Service(微信点赞辅助类)
 * @author rufei.cn
 * @version 2019-10-15
 */
@Service
@SuppressWarnings("all")
public class LikeHelperService {
    private static Logger logger = LoggerFactory.getLogger(LikeHelperService.class);

	@Autowired
	private LikeDao likeDao;

	/**
	 * 获取分页总记录数
	 *
	 * @param map
	 * @return
	 */
	public int getTotalCount(JSONObject map) {
		int resCount = 0;
		Integer totalCount = likeDao.getTotalCount(map);
		if (totalCount != null) {
			resCount = totalCount;
		}
		return resCount;
	}

	/**
	 * 获取单条数据
	 *
	 * @param bizId
	 * @param type
	 * @return
	 * @author rufei.cn
	 */
	public long getPhotoLikeCount(String bizId, String type) {
		long count = 0;
		JSONObject map = new JSONObject();
		map.put("type", type);
		map.put("bizId", bizId);
		map.put("flag", 1);
		String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + "getPhotoLikeCount" + bizId + type;
		String cache = LocalCacheUtil.getCache(key);
		if (StringUtil.isNotBlank(cache)) {
			count = StringUtil.stringToLong(cache);
		}
		if (count > 0) {
			return count;
		}
		Long likeCount = likeDao.getLikeCount(map);
		if(likeCount!=null)
		{
			count=likeCount;
		}
		LocalCacheUtil.saveCache(key, String.valueOf(count), 60);
		return count;
	}

    /*
     * save(保存微信点赞)
     * @param like
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
   @Transactional(propagation = Propagation.REQUIRED,readOnly =false,isolation = Isolation.REPEATABLE_READ,timeout = 30,rollbackFor = Exception.class)
    public Like save(Like like) throws  Exception {
       Like ret=null;
        if (like == null) {
            return ret;
        }
        if (like.getId() != null && like.getId() > 0) {
            updateById(like);
            ret=like;
        } else {
            like.setId(null);
            likeDao.add(like);
            ret=like;
        }
        return ret;
    }

     /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	 public Like getWxUserLikeById (long id)
	 {
	      return likeDao.getWxUserLikeById(id);
	 }

    /**
	 * 获取单条数据
	 * @param like
	 * @author rufei.cn
	 * @return
	 */
	 public Like getSignleWxUserLike(Like like)
	 {
	      return likeDao.getSignleWxUserLike(like);
	 }
	 /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  private void updateById(Like like)
	  {
	     likeDao.updateById(like);
	  }
	
}