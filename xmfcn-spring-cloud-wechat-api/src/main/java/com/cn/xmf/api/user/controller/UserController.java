package com.cn.xmf.api.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.common.SysCommonService;
import com.cn.xmf.api.user.service.UserService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.user.User;
import com.cn.xmf.model.wx.WxUser;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * UserController(用户信息)
 *
 * @author rufei.cn
 * @version 2018-09-16
 */
@RestController
@SuppressWarnings("all")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);
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
    public RetData getList(HttpServletRequest request, String parms) {
        RetData retData = new RetData();
        try {
            logger.info("getList:(获取用户信息分页查询接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            int pageNo = json.getIntValue("pageNo");
            int pageSize = json.getIntValue("pageSize");
            JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
            param.putAll(json);
            Partion pt = userService.getList(param);
            List<User> list = null;
            int totalCount = 0;
            if (pt != null) {
                list = (List<User>) pt.getList();
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
            sysCommonService.sendDingTalkMessage("getList", parms, JSON.toJSONString(retData), msg, this.getClass());
        }
        logger.info("getList:(获取用户信息分页查询接口) 结束  parms={}", parms);
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
        Map parms = new HashMap();
        Map<String, String[]> parameterMap = request.getParameterMap();

        String openId = request.getParameter("openId");
        String nickname = request.getParameter("nickname");
        String age = request.getParameter("age");
        String country = request.getParameter("country");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String photourl = request.getParameter("photourl");
        Enumeration<String> enu = request.getParameterNames();
        String requestURI = request.getRequestURI();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            logger.info("In parameter: " + requestURI + "  " + paraName + ": " + request.getParameter(paraName));
        }
        if(StringUtil.isBlank(nickname))
        {
            object.put("code",501);
            return object;
        }
        if(StringUtil.isBlank(age))
        {
            object.put("code",501);
            return object;
        }
        if(age.contains("undefined"))
        {
            object.put("code",501);
            return object;
        }
        try {
            WxUser wx = new WxUser();
            wx.setOpenid(openId);
            wx.setAge(age);
            wx.setCountry(country);
            wx.setProvince(province);
            wx.setCity(city);
            wx.setNickname(nickname);
            wx.setPhotourl(photourl);
            userService.save(wx);
            object.put("code",200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return object;
    }

}