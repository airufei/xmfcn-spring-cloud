package com.cn.xmf.service.user.service;

import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.WxUser;
import com.cn.xmf.service.user.dao.WxUserDao;
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
public class WxUserService {

    @Autowired
    private static Logger logger = LoggerFactory.getLogger(WxUserService.class);

    @Resource
    private WxUserDao wxUserDao;

    /**
     * 删除数据（逻辑删除）
     *
     * @param question
     * @return
     */
    @RequestMapping("delete")
    public void delete(long id) {
        wxUserDao.delete(id);
    }

    /**
     * 保存用户数据
     * @param wxUser
     * @return
     */
    @RequestMapping("save")
    public WxUser save(@RequestBody WxUser wxUser) {
        if (wxUser == null) {
            return null;
        }
        String openid = wxUser.getOpenid();
        logger.info("保存用户数据 开始 openid={}",openid);
        WxUser user = getWxUserByOpenId(openid);
        if (user != null) {
            wxUser.setId(user.getId());
        }
        if (wxUser.getId()!=null&&wxUser.getId() > 0) {
            wxUserDao.updateById(wxUser);
        } else {
            wxUserDao.add(wxUser);
        }
        return wxUser;
    }

    /**
     * 获取分页数据
     *
     * @param map
     * @return
     */
    @RequestMapping("getList")
    public Partion getList(@RequestBody Map map) {
        List<WxUser> list = wxUserDao.getList(map);
        int totalcount = getTotalCount(map);
        Partion pt = new Partion(map, list, totalcount);
        return pt;
    }


    /**
     *
     * 根据openid获取用户
     * @param openId
     * @return
     */
    @RequestMapping("getWxUserByOpenId")
    public WxUser getWxUserByOpenId(String openId) {
        WxUser wx = null;
        if (StringUtil.isBlank(openId)) {
            return wx;
        }
        WxUser wxUser = new WxUser();
        wxUser.setOpenid(openId);
        List<WxUser> list = wxUserDao.getWxUserList(wxUser);
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
        return wxUserDao.getTotalCount(map);
    }
    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    private WxUser getWxUserById(long id) {
        return wxUserDao.getWxUserById(id);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    private void updateById(WxUser wxUser) {
        wxUserDao.updateById(wxUser);
    }

}