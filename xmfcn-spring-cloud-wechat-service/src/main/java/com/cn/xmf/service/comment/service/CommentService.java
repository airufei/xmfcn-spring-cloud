package com.cn.xmf.service.comment.service;


import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Comment;
import com.cn.xmf.service.common.SysCommonService;
import com.cn.xmf.service.comment.dao.CommentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * WxUserMessageService(微信留言)
 * @author rufei.cn
 * service 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 * @version 2019-10-15
 */
@RestController
@RequestMapping(value = "/server/msg/")
@SuppressWarnings("all")
public class CommentService {

    private static Logger logger = LoggerFactory.getLogger(CommentService.class);

	@Autowired
	private CommentDao commentDao;
	@Autowired
	private CommentHelperService commentHelperService;
	@Autowired
    private SysCommonService sysCommonService; //如果不需要发钉钉消息可以注释了

      /**
	   * getList(获取微信留言带分页数据-服务)
       * @author rufei.cn
       * @param json
	   * @return
	   */
	   @RequestMapping("getList")
	   public Partion  getList(@RequestBody JSONObject json)
	   {
          logger.info("getList(获取微信留言带分页数据-服务) 开始 json={}", json);
          if(json==null||json.size()<1)
          {
             return null;
          }
          long totalcount =commentHelperService.getTotalCount(json);
          List<Comment> list= null;
          if(totalcount>0)
          {
             list= commentDao.getList(json);
          }
          Partion pt = new Partion(json, list, totalcount);
		  logger.info("getList(获取微信留言带分页数据-服务) 结束 ");
		  return pt;
	   }

	   /**
	   * getWxUserMessageList(获取微信留言 不带分页数据-服务)
       * @author rufei.cn
       * @param comment
	   * @return
	   */
	   @RequestMapping("getWxUserMessageList")
	   public List<Comment>  getWxUserMessageList(@RequestBody Comment comment)
	   {
	       List<Comment> list=null;
          logger.info("getWxUserMessageList(获取微信留言 不带分页数据-服务) 开始 parms={}", comment);
          if(comment==null)
          {
             return list;
          }
          list=commentDao.getWxUserMessageList(comment);
	      logger.info("getWxUserMessageList(获取微信留言 不带分页数据-服务) 结束");
	      return list;
	   }


    /**
	 * save (保存微信留言 数据-服务)
	 * @param comment
     * @author rufei.cn
	 * @return
	 */
    @RequestMapping("save")
	public Comment save(@RequestBody Comment comment) throws  Exception
	{
        logger.info("save (保存微信留言 数据-服务) 开始 parms={}", comment);
        Comment ret=commentHelperService.save(comment);
	    logger.info("save (保存微信留言 数据-服务) 结束");
	    return  ret;
	}


	   /**
	   * getWxUserMessage(获取微信留言单条数据-服务)
	   * @author rufei.cn
	   * @param comment
	   * @return
	   */
	   @RequestMapping("getWxUserMessage")
	   public Comment getWxUserMessage(@RequestBody Comment comment)
	   {
	       Comment ret=null;
           logger.info("getWxUserMessage(获取微信留言单条数据-服务) 开始 parms={}", comment);
           if(comment==null)
           {
             return ret;
           }
	       ret=commentHelperService.getSignleWxUserMessage(comment);
	       logger.info("getWxUserMessage(获取微信留言单条数据-服务) 结束 ");
	       return ret;
	   }

    /**
	 * delete(逻辑删除微信留言数据-服务)
	 * @param id
	 * @author rufei.cn
	 * @return
	 */
     @RequestMapping("delete")
	 public boolean delete(Long id)
	 {
	    logger.info("delete(逻辑删除微信留言数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        Comment dt = commentHelperService.getWxUserMessageById(id);
        if (dt == null) {
            return isSuccess;
        }
	    commentDao.delete(id);
	    isSuccess = true;
        logger.info("delete(逻辑删除微信留言数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
	 }
}