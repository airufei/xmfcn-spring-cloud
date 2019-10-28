package com.cn.xmf.service.invit.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Invitation;
import com.cn.xmf.service.invit.dao.InvitationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Invitation Service(邀请函)
 * service 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufei.cn
 * @version 2019-10-26
 */
@RestController
@RequestMapping(value = "/server/invit/")
@SuppressWarnings("all")
public class InvitationService {

    private static Logger logger = LoggerFactory.getLogger(InvitationService.class);

    @Autowired
    private InvitationDao invitationDao;
    @Autowired
    private InvitationHelperService invitationHelperService;

    /**
     * getList(获取邀请函带分页数据-服务)
     *
     * @param json
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getList")
    public Partion getList(@RequestBody JSONObject json) {
        logger.info("getList(获取邀请函带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        int totalcount = invitationHelperService.getTotalCount(json);
        List<Invitation> list = null;
        if (totalcount > 0) {
            list = invitationDao.getList(json);
        }
        Partion pt = new Partion(json, list, totalcount);
        logger.info("getList(获取邀请函带分页数据-服务) 结束 ");
        return pt;
    }

    /**
     * getInvitationList(获取邀请函 不带分页数据-服务)
     *
     * @param invitation
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getInvitationList")
    public List<Invitation> getInvitationList(@RequestBody Invitation invitation) {
        String parms = JSON.toJSONString(invitation);
        List<Invitation> list = null;
        logger.info("getInvitationList(获取邀请函 不带分页数据-服务) 开始 parms={}", parms);
        if (invitation == null) {
            return list;
        }
        list = invitationDao.getInvitationList(invitation);
        logger.info("getInvitationList(获取邀请函 不带分页数据-服务) 结束");
        return list;
    }


    /**
     * save (保存邀请函 数据-服务)
     *
     * @param invitation
     * @return
     * @author rufei.cn
     */
    @RequestMapping("save")
    public Invitation save(@RequestBody Invitation invitation) throws Exception {
        String parms = JSON.toJSONString(invitation);
        logger.info("save (保存邀请函 数据-服务) 开始 parms={}", parms);
        Invitation ret = null;
        if (invitation == null) {
            return ret;
        }
        ret = invitationHelperService.save(invitation);
        logger.info("save (保存邀请函 数据-服务) 结束");
        return ret;
    }


    /**
     * getInvitation(获取邀请函单条数据-服务)
     *
     * @param invitation
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getInvitation")
    public Invitation getInvitation(@RequestBody Invitation invitation) {
        Invitation ret = null;
        String parms = JSON.toJSONString(invitation);
        logger.info("getInvitation(获取邀请函单条数据-服务) 开始 parms={}", parms);
        if (invitation == null) {
            return ret;
        }
        ret = invitationHelperService.getSignleInvitation(invitation);
        logger.info("getInvitation(获取邀请函单条数据-服务) 结束 ");
        return ret;
    }

    /**
     * delete(逻辑删除邀请函数据-服务)
     *
     * @param id
     * @return
     * @author rufei.cn
     */
    @RequestMapping("delete")
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除邀请函数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        Invitation dt = invitationHelperService.getInvitationById(id);
        if (dt == null) {
            return isSuccess;
        }
        invitationDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除邀请函数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }
}