package com.cn.xmf.service.photo.service;


import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cn.xmf.model.photo.*;
import com.cn.xmf.base.model.*;
import com.cn.xmf.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cn.xmf.service.photo.dao.WxPhotoDao;
import com.cn.xmf.service.common.CommonService;
/**
 * Service(微信照片)
 * @author airufei
 * @version 2019-10-11
 */
@RestController
@RequestMapping(value = "/server/photo/")
@SuppressWarnings("all")
public class WxPhotoService  {

	@Autowired
	private WxPhotoDao wxPhotoDao;
	@Autowired
	private WxPhotoHelperService  wxPhotoHelperService;
	@Autowired
    private SysCommonService sysCommonService; //如果不需要发钉钉消息可以注释了
    @Autowired
     private static Logger logger = LoggerFactory.getLogger(WxPhotoService.class);
      /**
	   * getList(获取微信照片带分页数据-服务)
       * @author airufei
       * @param json
	   * @return
	   */
	   @RequestMapping("getList")
	   public Partion  getList(@RequestBody JSONObject json)
	   {
          logger.info("getList(获取微信照片带分页数据-服务) 开始 json={}", json);
          if(json==null||json.size()<1)
          {
             return null;
          }
          Partion pt =null;
          try
           {
              int totalcount =wxPhotoHelperService.getTotalCount(json);
              List<WxPhoto> list= null;
              if(totalcount>0)
              {
                list= wxPhotoDao.getList(json);
              }
               pt = new Partion(json, list, totalcount);
		   } catch (Exception e) {
            String msg = "getList(获取微信照片 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            String parms = null;
            if (json != null) {
                parms = json.toString();
            }
            sysCommonService.sendDingMessage("base-service[getList]", parms, null, msg, this.getClass());

          }
		  logger.info("getList(获取微信照片带分页数据-服务) 结束 ");
		  return pt;
	   }

	   /**
	   * getWxPhotoList(获取微信照片 不带分页数据-服务)
       * @author airufei
       * @param wxPhoto
	   * @return
	   */
	   @RequestMapping("getWxPhotoList")
	   public List<WxPhoto>  getWxPhotoList(@RequestBody WxPhoto wxPhoto)
	   {
	      String parms = JSON.toJSONString(wxPhoto);
	      List<WxPhoto> list=null;
          logger.info("getWxPhotoList(获取微信照片 不带分页数据-服务) 开始 parms={}", parms);
          if(wxPhoto==null)
          {
             return list;
          }
          try {
	          list=wxPhotoDao.getWxPhotoList(wxPhoto);
	       } catch (Exception e) {
            String msg = "getWxPhotoList 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[getWxPhotoList]", parms, null, msg, this.getClass());

          }
	      logger.info("getWxPhotoList(获取微信照片 不带分页数据-服务) 结束");
	      return list;
	   }


    /**
	 * save (保存微信照片 数据-服务)
	 * @param wxPhoto
     * @author airufei
	 * @return
	 */
    @RequestMapping("save")
	public WxPhoto save(@RequestBody WxPhoto wxPhoto)
	{
	    String parms = JSON.toJSONString(wxPhoto);
        logger.info("save (保存微信照片 数据-服务) 开始 parms={}", parms);
         WxPhoto ret=null;
        if (wxPhoto == null) {
            return ret;
        }
        try {
	         ret=wxPhotoHelperService.save(wxPhoto);
	     } catch (Exception e) {
            String msg = "save (保存微信照片 数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            ret=null;
            sysCommonService.sendDingMessage("base-service[save]", parms, null, msg, this.getClass());

          }
	    logger.info("save (保存微信照片 数据-服务) 结束");
	    return  ret;
	}


	   /**
	   * getWxPhoto(获取微信照片单条数据-服务)
	   * @author airufei
	   * @param wxPhoto
	   * @return
	   */
	   @RequestMapping("getWxPhoto")
	   public WxPhoto  getWxPhoto(@RequestBody WxPhoto wxPhoto)
	   {
	      WxPhoto ret=null;
	      String parms = JSON.toJSONString(wxPhoto);
	      List<WxPhoto> list=null;
          logger.info("getWxPhoto(获取微信照片单条数据-服务) 开始 parms={}", parms);
           if(wxPhoto==null)
           {
             return ret;
           }
           try{
	            ret=wxPhotoHelperService.getSignleWxPhoto(wxPhoto);
	       } catch (Exception e) {
            String msg = "getWxPhoto(获取微信照片单条数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[getWxPhoto]", parms, null, msg, this.getClass());

          }
	       logger.info("getWxPhoto(获取微信照片单条数据-服务) 结束 ");
	      return ret;
	   }

    /**
	 * delete(逻辑删除微信照片数据-服务)
	 * @param id
	 * @author airufei
	 * @return
	 */
     @RequestMapping("delete")
	 public boolean delete(Long id)
	 {
	    logger.info("delete(逻辑删除微信照片数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        WxPhoto dt = wxPhotoHelperService.getWxPhotoById(id);
        if (dt == null) {
            return isSuccess;
        }
	    wxPhotoDao.delete(id);
	    isSuccess = true;
        logger.info("delete(逻辑删除微信照片数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
	 }
}