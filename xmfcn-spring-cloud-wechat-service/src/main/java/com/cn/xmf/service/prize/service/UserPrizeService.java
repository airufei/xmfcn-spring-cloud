package com.cn.xmf.service.prize.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.UserPrize;
import com.cn.xmf.service.prize.dao.UserPrizeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserPrize Service(奖品信息)
 * service 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
@RestController
@RequestMapping(value = "/server/prize/")
@SuppressWarnings("all")
public class UserPrizeService {

    private static Logger logger = LoggerFactory.getLogger(UserPrizeService.class);

    @Autowired
    private UserPrizeDao userPrizeDao;
    @Autowired
    private UserPrizeHelperService userPrizeHelperService;

    /**
     * getList(获取奖品信息带分页数据-服务)
     *
     * @param json
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getList")
    public Partion getList(@RequestBody JSONObject json) {
        logger.info("getList(获取奖品信息带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        int totalcount = userPrizeHelperService.getTotalCount(json);
        List<UserPrize> list = null;
        if (totalcount > 0) {
            list = userPrizeDao.getList(json);
        }
        Partion pt = new Partion(json, list, totalcount);
        logger.info("getList(获取奖品信息带分页数据-服务) 结束 ");
        return pt;
    }

    /**
     * getUserPrizeList(获取奖品信息 不带分页数据-服务)
     *
     * @param userPrize
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getUserPrizeList")
    public List<UserPrize> getUserPrizeList(@RequestBody UserPrize userPrize) {
        String parms = JSON.toJSONString(userPrize);
        List<UserPrize> list = null;
        logger.info("getUserPrizeList(获取奖品信息 不带分页数据-服务) 开始 parms={}", parms);
        if (userPrize == null) {
            return list;
        }
        list = userPrizeDao.getUserPrizeList(userPrize);
        logger.info("getUserPrizeList(获取奖品信息 不带分页数据-服务) 结束");
        return list;
    }


    /**
     * save (保存奖品信息 数据-服务)
     *
     * @param userPrize
     * @return
     * @author rufei.cn
     */
    @RequestMapping("save")
    public UserPrize save(@RequestBody UserPrize userPrize) throws Exception {
        String parms = JSON.toJSONString(userPrize);
        logger.info("save (保存奖品信息 数据-服务) 开始 parms={}", parms);
        UserPrize ret = null;
        if (userPrize == null) {
            return ret;
        }
        ret = userPrizeHelperService.save(userPrize);
        logger.info("save (保存奖品信息 数据-服务) 结束");
        return ret;
    }


    /**
     * getUserPrize(获取奖品信息单条数据-服务)
     *
     * @param userPrize
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getUserPrize")
    public UserPrize getUserPrize(@RequestBody UserPrize userPrize) {
        UserPrize ret = null;
        String parms = JSON.toJSONString(userPrize);
        logger.info("getUserPrize(获取奖品信息单条数据-服务) 开始 parms={}", parms);
        if (userPrize == null) {
            return ret;
        }
        ret = userPrizeHelperService.getSignleUserPrize(userPrize);
        logger.info("getUserPrize(获取奖品信息单条数据-服务) 结束 ");
        return ret;
    }

    /**
     * delete(逻辑删除奖品信息数据-服务)
     *
     * @param id
     * @return
     * @author rufei.cn
     */
    @RequestMapping("delete")
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除奖品信息数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        UserPrize dt = userPrizeHelperService.getUserPrizeById(id);
        if (dt == null) {
            return isSuccess;
        }
        userPrizeDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除奖品信息数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }
}