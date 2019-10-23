package com.cn.xmf.api.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.common.SysCommonService;
import com.cn.xmf.api.user.rpc.UserService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.User;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * UserController(用户信息)
 *
 * @author rufei.cn
 * @version 2018-09-16
 */

@RestController
@RequestMapping(value = "/user")
@SuppressWarnings("all")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private SysCommonService sysCommonService; //如果不需要发钉钉消息可以注释了

    /**
     * getList:(获取用户信息分页查询接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getList")
    public RetData getList(HttpServletRequest request) {
        RetData retData = new RetData();
        try {
            String pageNoStr = request.getParameter("pageNo");
            String pageSizeStr = request.getParameter("pageSize");
            String type = request.getParameter("type");
            int pageNo = StringUtil.stringToInt(pageNoStr);
            int pageSize = StringUtil.stringToInt(pageSizeStr);
            JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
            param.put("type", type);
            Partion pt = userService.getList(param);
            List<com.cn.xmf.model.user.User> list = null;
            long totalCount = 0;
            if (pt != null) {
                list = (List<com.cn.xmf.model.user.User>) pt.getList();
                totalCount = pt.getPageCount();
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

        } catch (Exception e) {
            retData.setCode(ResultCodeMessage.FAILURE);
            retData.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
            String msg = "getList:(获取用户信息分页查询接口) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
        }
        logger.info("结束 retData={}",retData);
        return retData;
    }

    /**
     * save:(保存用户信息数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping(value = "save")
    public JSONObject save(HttpServletRequest request) {
        JSONObject object = new JSONObject();
        String openId = request.getParameter("openId");
        String nickName = request.getParameter("nickName");
        String age = request.getParameter("age");
        String country = request.getParameter("country");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String photoUrl = request.getParameter("photoUrl");
        logger.info("保存微信用户数据开始：openId={},nickName={}",openId,nickName);
        if (StringUtil.isBlank(nickName)) {
            object.put("code", 501);
            return object;
        }
        if (StringUtil.isBlank(openId)) {
            object.put("code", 501);
            return object;
        }
        if (StringUtil.isBlank(age)) {
            object.put("code", 501);
            return object;
        }
        if (age.contains("undefined")) {
            object.put("code", 501);
            return object;
        }
        try {
            User wx = new User();
            wx.setOpenId(openId);
            wx.setAge(age);
            wx.setCountry(country);
            wx.setProvince(province);
            wx.setCity(city);
            wx.setNickname(nickName);
            wx.setPhotoUrl(photoUrl);
            userService.save(wx);
            object.put("code", 200);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
            object.put("code", 501);
        }
        return object;
    }

}