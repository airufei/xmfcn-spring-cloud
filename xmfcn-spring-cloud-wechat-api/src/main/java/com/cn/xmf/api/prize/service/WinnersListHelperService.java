package com.cn.xmf.api.prize.service;

import com.cn.xmf.api.prize.rpc.WinnersListService;
import com.cn.xmf.model.wx.WinnersList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WinnersListHelperService(获奖名单辅助类)
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
@Service
@SuppressWarnings("all")
public class WinnersListHelperService {

    private static Logger logger = LoggerFactory.getLogger(WinnersListHelperService.class);
    @Autowired
    private WinnersListService winnersListService;

    /**
     * 获取单条数据
     *
     * @param winnersList
     * @return
     * @author rufei.cn
     */
    public WinnersList getWinnersList(WinnersList winnersList) {
        return winnersListService.getWinnersList(winnersList);
    }

}