package com.cn.xmf.service.user.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.user.User;
import com.cn.xmf.service.user.dao.UserDao;
import com.cn.xmf.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service(用户信息)
 * @author airufei
 * @version 2018-09-16
 */
@Service
@SuppressWarnings("all")
public class UserHelperService  {

	@Autowired
	private UserDao userDao;
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
	  /**
	   * 获取分页总记录数
	   * @param map
	   * @return
	   */
	   public int  getTotalCount(JSONObject map)
	   {
	      int resCount=0;
	      Integer totalCount =userDao.getTotalCount(map);
	      if(totalCount!=null)
	      {
	        resCount=totalCount;
	      }
	      return resCount;
	   }


    /*
     * save(保存用户信息)
     * @param user
     * @author airufei
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    public User save(User user) {
        if (user == null) {
            return user;
        }
        if (user.getId() != null && user.getId() > 0) {
            updateById(user);
        } else {
            userDao.add(user);
        }
        return user;
    }

     /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	 public User getUserById (long id)
	 {
	      return userDao.getUserById(id);
	 }

    /**
	 * 获取单条数据
	 * @param user
	 * @author airufei
	 * @return
	 */
	 public User getSignleUser(User user)
	 {
	      return userDao.getSignleUser(user);
	 }
	 /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(User user)
	  {
	     userDao.updateById(user);
	  }
	
}