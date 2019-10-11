package com.cn.xmf.service.photo.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.WxPhoto;
import com.cn.xmf.service.photo.dao.WxPhotoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service(微信照片)
 * @author airufei
 * @version 2019-10-11
 */
@Service
@SuppressWarnings("all")
public class WxPhotoHelperService  {

	@Autowired
	private WxPhotoDao wxPhotoDao;
    private static Logger logger = LoggerFactory.getLogger(WxPhotoService.class);
	  /**
	   * 获取分页总记录数
	   * @param map
	   * @return
	   */
	   public int  getTotalCount(JSONObject map)
	   {
	      int resCount=0;
	      Integer totalCount =wxPhotoDao.getTotalCount(map);
	      if(totalCount!=null)
	      {
	        resCount=totalCount;
	      }
	      return resCount;
	   }


    /*
     * save(保存微信照片)
     * @param wxPhoto
     * @author airufei
     * @date 2018/1/30 14:59
     */
   @Transactional(propagation = Propagation.REQUIRED,readOnly =false,isolation = Isolation.REPEATABLE_READ,timeout = 30,rollbackFor = Exception.class)
    public WxPhoto save(WxPhoto wxPhoto) throws  Exception {
       WxPhoto ret=null;
        if (wxPhoto == null) {
            return ret;
        }
        if (wxPhoto.getId() != null && wxPhoto.getId() > 0) {
            updateById(wxPhoto);
            ret=wxPhoto;
        } else {
            wxPhoto.setId(null);
            wxPhotoDao.add(wxPhoto);
            ret=wxPhoto;
        }
        return ret;
    }

     /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	 public WxPhoto getWxPhotoById (long id)
	 {
	      return wxPhotoDao.getWxPhotoById(id);
	 }

    /**
	 * 获取单条数据
	 * @param wxPhoto
	 * @author airufei
	 * @return
	 */
	 public WxPhoto getSignleWxPhoto(WxPhoto wxPhoto)
	 {
	      return wxPhotoDao.getSignleWxPhoto(wxPhoto);
	 }
	 /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(WxPhoto wxPhoto)
	  {
	     wxPhotoDao.updateById(wxPhoto);
	  }
	
}