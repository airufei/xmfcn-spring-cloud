package com.cn.xmf.service.msg.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.WxUserMessage;
import com.cn.xmf.service.msg.dao.WxUserMessageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * WxUserMessage Service(微信留言辅助类)
 * @author rufei.cn
 * @version 2019-10-15
 */
@Service
@SuppressWarnings("all")
public class WxUserMessageHelperService  {
    private static Logger logger = LoggerFactory.getLogger(WxUserMessageHelperService.class);

	@Autowired
	private WxUserMessageDao wxUserMessageDao;

	  /**
	   * 获取分页总记录数
	   * @param map
	   * @return
	   */
	   public int  getTotalCount(JSONObject map)
	   {
	      int resCount=0;
	      Integer totalCount =wxUserMessageDao.getTotalCount(map);
	      if(totalCount!=null)
	      {
	        resCount=totalCount;
	      }
	      return resCount;
	   }


    /*
     * save(保存微信留言)
     * @param wxUserMessage
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
   @Transactional(propagation = Propagation.REQUIRED,readOnly =false,isolation = Isolation.REPEATABLE_READ,timeout = 30,rollbackFor = Exception.class)
    public WxUserMessage save(WxUserMessage wxUserMessage) throws  Exception {
       WxUserMessage ret=null;
        if (wxUserMessage == null) {
            return ret;
        }
        if (wxUserMessage.getId() != null && wxUserMessage.getId() > 0) {
            updateById(wxUserMessage);
            ret=wxUserMessage;
        } else {
            wxUserMessage.setId(null);
            wxUserMessageDao.add(wxUserMessage);
            ret=wxUserMessage;
        }
        return ret;
    }

     /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	 public WxUserMessage getWxUserMessageById (long id)
	 {
	      return wxUserMessageDao.getWxUserMessageById(id);
	 }

    /**
	 * 获取单条数据
	 * @param wxUserMessage
	 * @author rufei.cn
	 * @return
	 */
	 public WxUserMessage getSignleWxUserMessage(WxUserMessage wxUserMessage)
	 {
	      return wxUserMessageDao.getSignleWxUserMessage(wxUserMessage);
	 }
	 /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  private void updateById(WxUserMessage wxUserMessage)
	  {
	     wxUserMessageDao.updateById(wxUserMessage);
	  }
	
}