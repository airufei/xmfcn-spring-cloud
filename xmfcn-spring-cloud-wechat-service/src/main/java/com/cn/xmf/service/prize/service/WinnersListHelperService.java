package com.cn.xmf.service.prize.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.WinnersList;
import com.cn.xmf.service.prize.dao.WinnersListDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * WinnersList Service(获奖名单辅助类)
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
@Service
@SuppressWarnings("all")
public class WinnersListHelperService {

    private static Logger logger = LoggerFactory.getLogger(WinnersListHelperService.class);

    @Autowired
    private WinnersListDao winnersListDao;

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public int getTotalCount(JSONObject map) {
        int resCount = 0;
        Integer totalCount = winnersListDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }


    /*
     * save(保存获奖名单)
     * @param winnersList
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.REPEATABLE_READ, timeout = 30, rollbackFor = Exception.class)
    public WinnersList save(WinnersList winnersList) throws Exception {
        WinnersList ret = null;
        if (winnersList == null) {
            return ret;
        }
        if (winnersList.getId() != null && winnersList.getId() > 0) {
            updateById(winnersList);
            ret = winnersList;
        } else {
            winnersList.setId(null);
            winnersListDao.add(winnersList);
            ret = winnersList;
        }
        return ret;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public WinnersList getWinnersListById(long id) {
        return winnersListDao.getWinnersListById(id);
    }

    /**
     * 获取单条数据
     *
     * @param winnersList
     * @return
     * @author rufei.cn
     */
    public WinnersList getSignleWinnersList(WinnersList winnersList) {
        return winnersListDao.getSignleWinnersList(winnersList);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(WinnersList winnersList) {
        winnersListDao.updateById(winnersList);
    }

}