package com.cn.xmf.service.prize.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.WinnersList;
import com.cn.xmf.service.prize.dao.WinnersListDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * WinnersList Service(获奖名单)
 * service 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
@RestController
@RequestMapping(value = "/server/prizelist/")
@SuppressWarnings("all")
public class WinnersListService {

    private static Logger logger = LoggerFactory.getLogger(WinnersListService.class);

    @Autowired
    private WinnersListDao winnersListDao;
    @Autowired
    private WinnersListHelperService winnersListHelperService;

    /**
     * getList(获取获奖名单带分页数据-服务)
     *
     * @param json
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getList")
    public Partion getList(@RequestBody JSONObject json) {
        logger.info("getList(获取获奖名单带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        int totalcount = winnersListHelperService.getTotalCount(json);
        List<WinnersList> list = null;
        if (totalcount > 0) {
            list = winnersListDao.getList(json);
        }
        Partion pt = new Partion(json, list, totalcount);
        logger.info("getList(获取获奖名单带分页数据-服务) 结束 ");
        return pt;
    }

    /**
     * getWinnersListList(获取获奖名单 不带分页数据-服务)
     *
     * @param winnersList
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getWinnersListList")
    public List<WinnersList> getWinnersListList(@RequestBody WinnersList winnersList) {
        String parms = JSON.toJSONString(winnersList);
        List<WinnersList> list = null;
        logger.info("getWinnersListList(获取获奖名单 不带分页数据-服务) 开始 parms={}", parms);
        if (winnersList == null) {
            return list;
        }
        list = winnersListDao.getWinnersListList(winnersList);
        logger.info("getWinnersListList(获取获奖名单 不带分页数据-服务) 结束");
        return list;
    }


    /**
     * save (保存获奖名单 数据-服务)
     *
     * @param winnersList
     * @return
     * @author rufei.cn
     */
    @RequestMapping("save")
    public WinnersList save(@RequestBody WinnersList winnersList) throws Exception {
        String parms = JSON.toJSONString(winnersList);
        logger.info("save (保存获奖名单 数据-服务) 开始 parms={}", parms);
        WinnersList ret = null;
        if (winnersList == null) {
            return ret;
        }
        ret = winnersListHelperService.save(winnersList);
        logger.info("save (保存获奖名单 数据-服务) 结束");
        return ret;
    }


    /**
     * getWinnersList(获取获奖名单单条数据-服务)
     *
     * @param winnersList
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getWinnersList")
    public WinnersList getWinnersList(@RequestBody WinnersList winnersList) {
        WinnersList ret = null;
        String parms = JSON.toJSONString(winnersList);
        logger.info("getWinnersList(获取获奖名单单条数据-服务) 开始 parms={}", parms);
        if (winnersList == null) {
            return ret;
        }
        ret = winnersListHelperService.getSignleWinnersList(winnersList);
        logger.info("getWinnersList(获取获奖名单单条数据-服务) 结束 ");
        return ret;
    }

    /**
     * delete(逻辑删除获奖名单数据-服务)
     *
     * @param id
     * @return
     * @author rufei.cn
     */
    @RequestMapping("delete")
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除获奖名单数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        WinnersList dt = winnersListHelperService.getWinnersListById(id);
        if (dt == null) {
            return isSuccess;
        }
        winnersListDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除获奖名单数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }
}