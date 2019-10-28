package com.cn.xmf.service.user.service;

import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.User;
import com.cn.xmf.service.user.dao.UserDao;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Service(微信用户)
 *
 * @author airufei
 * @version 2018-07-09
 */
@RestController
@RequestMapping(value = "/server/wx/user/")
@SuppressWarnings("all")
public class UserService {

    @Autowired
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserDao userDao;

    /**
     * 删除数据（逻辑删除）
     *
     * @param question
     * @return
     */
    @RequestMapping("delete")
    public void delete(long id) {
        userDao.delete(id);
    }

    /**
     * 保存用户数据
     * @param user
     * @return
     */
    @RequestMapping("save")
    public User save(@RequestBody User user) {
        if (user == null) {
            return null;
        }
        String openId = user.getOpenId();
        logger.info("保存用户数据 开始 openId={}",openId);
        User dbuser = getWxUserByOpenId(openId);
        if (dbuser != null) {
            user.setId(dbuser.getId());
        }
        if (user.getId()!=null&&user.getId() > 0) {
            userDao.updateById(user);
        } else {
            userDao.add(user);
        }
        return user;
    }

    /**
     * 获取分页数据
     *
     * @param map
     * @return
     */
    @RequestMapping("getList")
    public Partion getList(@RequestBody Map map) {
        List<User> list = userDao.getList(map);
        int totalcount = getTotalCount(map);
        Partion pt = new Partion(map, list, totalcount);
        return pt;
    }


    /**
     *
     * 根据openId获取用户
     * @param openId
     * @return
     */
    @RequestMapping("getWxUserByOpenId")
    public User getWxUserByOpenId(String openId) {
        User wx = null;
        if (StringUtil.isBlank(openId)) {
            return wx;
        }
        User user = new User();
        user.setOpenId(openId);
        List<User> list = userDao.getWxUserList(user);
        if (list != null && list.size() > 0) {
            wx = list.get(0);
        }
        return wx;
    }

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    private int getTotalCount(Map map) {
        return userDao.getTotalCount(map);
    }
    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    private User getWxUserById(long id) {
        return userDao.getWxUserById(id);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    private void updateById(User user) {
        userDao.updateById(user);
    }

}