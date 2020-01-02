package com.cn.xmf.api.prize.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.prize.rpc.WinnersListService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.WinnersList;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * WinnersListController(获奖名单)
 * Controller 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
@RestController
@RequestMapping("/myprize/")
@SuppressWarnings("all")
public class WinnersListController {

    private static Logger logger = LoggerFactory.getLogger(WinnersListController.class);

    @Autowired
    private WinnersListService winnersListService;

    /**
     * getList:(获取获奖名单分页查询接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("getList")
    public RetData getList(HttpServletRequest request) {
        RetData retData = new RetData();
        String pageNoStr = request.getParameter("pageNo");
        String length = request.getParameter("pageSize");
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        String userId = request.getParameter("userId");
        int pageSize = 10;
        int pageNo = 1;
        if (StringUtil.isNotBlank(pageNoStr)) {
            pageNo = StringUtil.stringToInt(pageNoStr);
        }
        if (StringUtil.isNotBlank(length)) {
            pageSize = StringUtil.stringToInt(length);
        }
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        logger.info("getList:(获取获奖名单分页查询接口) 开始  param={}", param);
        param.put("type", type);
        param.put("name", name);
        param.put("userId", userId);
        Partion pt = winnersListService.getList(param);
        List<WinnersList> list = null;
        long totalCount = 0;
        if (pt != null) {
            list = (List<WinnersList>) pt.getList();
            totalCount = pt.getTotalCount();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        jsonObject.put("totalCount", totalCount);
        if (list == null || list.size() <= 0) {
            retData.setData(jsonObject);
            retData.setCode(ResultCodeMessage.NO_DATA);
            retData.setMessage(ResultCodeMessage.NO_DATA_MESSAGE);
            return retData;
        }
        retData.setData(jsonObject);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getList:(获取获奖名单分页查询接口) 结束");
        return retData;
    }

    /**
     * getWinnersList:(查询获奖名单单条数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("getWinnersList")
    public RetData getWinnersList(HttpServletRequest request) {
        RetData retData = new RetData();
        WinnersList winnersList = new WinnersList();
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        String userId = request.getParameter("userId");
        winnersList.setType(type);
        winnersList.setName(name);
        winnersList.setUserId(StringUtil.stringToInt(userId));
        logger.info("getWinnersList:(查询获奖名单单条数据接口) 开始  winnersList={}", winnersList);
        WinnersList retwinnersList = winnersListService.getWinnersList(winnersList);
        retData.setData(retwinnersList);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getWinnersList:(查询获奖名单单条数据接口) 结束");
        return retData;
    }
}