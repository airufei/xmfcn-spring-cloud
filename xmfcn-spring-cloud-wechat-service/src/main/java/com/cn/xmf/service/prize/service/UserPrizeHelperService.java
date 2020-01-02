package com.cn.xmf.service.prize.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.UserPrize;
import com.cn.xmf.service.prize.dao.UserPrizeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserPrize Service(奖品信息辅助类)
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
@Service
@SuppressWarnings("all")
public class UserPrizeHelperService {

    private static Logger logger = LoggerFactory.getLogger(UserPrizeHelperService.class);

    @Autowired
    private UserPrizeDao userPrizeDao;

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public int getTotalCount(JSONObject map) {
        int resCount = 0;
        Integer totalCount = userPrizeDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }


    /*
     * save(保存奖品信息)
     * @param userPrize
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.REPEATABLE_READ, timeout = 30, rollbackFor = Exception.class)
    public UserPrize save(UserPrize userPrize) throws Exception {
        UserPrize ret = null;
        if (userPrize == null) {
            return ret;
        }
        if (userPrize.getId() != null && userPrize.getId() > 0) {
            updateById(userPrize);
            ret = userPrize;
        } else {
            userPrize.setId(null);
            userPrizeDao.add(userPrize);
            ret = userPrize;
        }
        return ret;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public UserPrize getUserPrizeById(long id) {
        return userPrizeDao.getUserPrizeById(id);
    }

    /**
     * 获取单条数据
     *
     * @param userPrize
     * @return
     * @author rufei.cn
     */
    public UserPrize getSignleUserPrize(UserPrize userPrize) {
        return userPrizeDao.getSignleUserPrize(userPrize);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(UserPrize userPrize) {
        userPrizeDao.updateById(userPrize);
    }

}