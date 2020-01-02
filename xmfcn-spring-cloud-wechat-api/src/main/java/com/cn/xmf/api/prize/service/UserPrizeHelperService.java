package com.cn.xmf.api.prize.service;

import com.cn.xmf.api.prize.rpc.UserPrizeService;
import com.cn.xmf.model.wx.UserPrize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * UserPrizeHelperService(奖品信息辅助类)
 * @author rufei.cn
 * @version 2020-01-02
 */
@Service
@SuppressWarnings("all")
public class UserPrizeHelperService  {

    private static Logger logger = LoggerFactory.getLogger(UserPrizeHelperService.class);
    @Autowired
	private UserPrizeService userPrizeService;

    /**
	 * 获取单条数据
	 * @param userPrize
	 * @author rufei.cn
	 * @return
	 */
	 public UserPrize getUserPrize(UserPrize userPrize)
	 {
	      return userPrizeService.getUserPrize(userPrize);
	 }

}