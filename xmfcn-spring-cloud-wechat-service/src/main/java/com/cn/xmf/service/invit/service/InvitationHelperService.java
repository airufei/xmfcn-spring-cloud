package com.cn.xmf.service.invit.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Invitation;
import com.cn.xmf.service.invit.dao.InvitationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * Invitation Service(邀请函辅助类)
 * @author rufei.cn
 * @version 2019-10-26
 */
@Service
@SuppressWarnings("all")
public class InvitationHelperService  {

    private static Logger logger = LoggerFactory.getLogger(InvitationHelperService.class);

	@Autowired
	private InvitationDao invitationDao;

	  /**
	   * 获取分页总记录数
	   * @param map
	   * @return
	   */
	   public int  getTotalCount(JSONObject map)
	   {
	      int resCount=0;
	      Integer totalCount =invitationDao.getTotalCount(map);
	      if(totalCount!=null)
	      {
	        resCount=totalCount;
	      }
	      return resCount;
	   }


    /*
     * save(保存邀请函)
     * @param invitation
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
   @Transactional(propagation = Propagation.REQUIRED,readOnly =false,isolation = Isolation.REPEATABLE_READ,timeout = 30,rollbackFor = Exception.class)
    public Invitation save(Invitation invitation) throws  Exception{
        Invitation ret=null;
        if (invitation == null) {
            return ret;
        }
        if (invitation.getId() != null && invitation.getId() > 0) {
            updateById(invitation);
            ret=invitation;
        } else {
            invitation.setId(null);
            invitationDao.add(invitation);
            ret=invitation;
        }
        return ret;
    }

     /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	 public Invitation getInvitationById (long id)
	 {
	      return invitationDao.getInvitationById(id);
	 }

    /**
	 * 获取单条数据
	 * @param invitation
	 * @author rufei.cn
	 * @return
	 */
	 public Invitation getSignleInvitation(Invitation invitation)
	 {
	      return invitationDao.getSignleInvitation(invitation);
	 }
	 /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(Invitation invitation)
	  {
	     invitationDao.updateById(invitation);
	  }
	
}