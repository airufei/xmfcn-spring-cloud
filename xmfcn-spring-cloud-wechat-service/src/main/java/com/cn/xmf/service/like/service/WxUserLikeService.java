package com.cn.xmf.service.like.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.WxUserLike;
import com.cn.xmf.service.common.SysCommonService;
import com.cn.xmf.service.like.dao.WxUserLikeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * WxUserLikeService(微信点赞)
 * @author rufei.cn
 * service 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 * @version 2019-10-15
 */
@RestController
@RequestMapping(value = "/server/like/")
@SuppressWarnings("all")
public class WxUserLikeService  {

    private static Logger logger = LoggerFactory.getLogger(WxUserLikeService.class);

	@Autowired
	private WxUserLikeDao wxUserLikeDao;
	@Autowired
	private WxUserLikeHelperService  wxUserLikeHelperService;
	@Autowired
    private SysCommonService sysCommonService; //如果不需要发钉钉消息可以注释了

      /**
	   * getList(获取微信点赞带分页数据-服务)
       * @author rufei.cn
       * @param json
	   * @return
	   */
	   @RequestMapping("getList")
	   public Partion getList(@RequestBody JSONObject json)
	   {
          logger.info("getList(获取微信点赞带分页数据-服务) 开始 json={}", json);
          if(json==null||json.size()<1)
          {
             return null;
          }
          long totalcount =wxUserLikeHelperService.getTotalCount(json);
          List<WxUserLike> list= null;
          if(totalcount>0)
          {
             list= wxUserLikeDao.getList(json);
          }
          Partion pt = new Partion(json, list, totalcount);
		  logger.info("getList(获取微信点赞带分页数据-服务) 结束 ");
		  return pt;
	   }

	   /**
	   * getWxUserLikeList(获取微信点赞 不带分页数据-服务)
       * @author rufei.cn
       * @param wxUserLike
	   * @return
	   */
	   @RequestMapping("getWxUserLikeList")
	   public List<WxUserLike>  getWxUserLikeList(@RequestBody WxUserLike wxUserLike)
	   {
	      String parms = JSON.toJSONString(wxUserLike);
		   List<WxUserLike>  list=null;
          logger.info("getWxUserLikeList(获取微信点赞 不带分页数据-服务) 开始 parms={}", parms);
          if(wxUserLike==null)
          {
             return list;
          }
	       list=wxUserLikeDao.getWxUserLikeList(wxUserLike);
	      logger.info("getWxUserLikeList(获取微信点赞 不带分页数据-服务) 结束");
	      return list;
	   }


    /**
	 * save (保存微信点赞 数据-服务)
	 * @param wxUserLike
     * @author rufei.cn
	 * @return
	 */
    @RequestMapping("save")
	public WxUserLike save(@RequestBody WxUserLike wxUserLike) throws Exception {
	    String parms = JSON.toJSONString(wxUserLike);
        logger.info("save (保存微信点赞 数据-服务) 开始 parms={}", parms);
        WxUserLike  ret=wxUserLikeHelperService.save(wxUserLike);
	    logger.info("save (保存微信点赞 数据-服务) 结束");
	    return  ret;
	}


	   /**
	   * getWxUserLike(获取微信点赞单条数据-服务)
	   * @author rufei.cn
	   * @param wxUserLike
	   * @return
	   */
	   @RequestMapping("getWxUserLike")
	   public WxUserLike  getWxUserLike(@RequestBody WxUserLike wxUserLike)
	   {
	      String parms = JSON.toJSONString(wxUserLike);
          logger.info("getWxUserLike(获取微信点赞单条数据-服务) 开始 parms={}", parms);
		   WxUserLike  ret=null;
           if(wxUserLike==null)
           {
             return ret;
           }
           ret=wxUserLikeHelperService.getSignleWxUserLike(wxUserLike);
	      logger.info("getWxUserLike(获取微信点赞单条数据-服务) 结束 ");
	      return ret;
	   }

    /**
	 * delete(逻辑删除微信点赞数据-服务)
	 * @param id
	 * @author rufei.cn
	 * @return
	 */
     @RequestMapping("delete")
	 public boolean delete(Long id)
	 {
	    logger.info("delete(逻辑删除微信点赞数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        WxUserLike dt = wxUserLikeHelperService.getWxUserLikeById(id);
        if (dt == null) {
            return isSuccess;
        }
	    wxUserLikeDao.delete(id);
	    isSuccess = true;
        logger.info("delete(逻辑删除微信点赞数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
	 }
}