package com.cn.xmf.api.prize.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.prize.rpc.UserPrizeService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.UserPrize;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * UserPrizeController(奖品信息)
 * Controller 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
@RestController
@RequestMapping("/prize/")
@SuppressWarnings("all")
public class UserPrizeController {

    private static Logger logger = LoggerFactory.getLogger(UserPrizeController.class);

    @Autowired
    private UserPrizeService userPrizeService;

    /**
     * getList:(获取奖品信息分页查询接口)
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
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        String name = request.getParameter("name");
        int pageSize = 10;
        int pageNo = 1;
        if (StringUtil.isNotBlank(pageNoStr)) {
            pageNo = StringUtil.stringToInt(pageNoStr);
        }
        if (StringUtil.isNotBlank(length)) {
            pageSize = StringUtil.stringToInt(length);
        }
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        logger.info("getList:(获取奖品信息分页查询接口) 开始  param={}", param);
        param.put("id", id);
        param.put("type", type);
        param.put("name", name);
        Partion pt = userPrizeService.getList(param);
        List<UserPrize> list = null;
        long totalCount = 0;
        if (pt != null) {
            list = (List<UserPrize>) pt.getList();
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
        logger.info("getList:(获取奖品信息分页查询接口) 结束");
        return retData;
    }
}