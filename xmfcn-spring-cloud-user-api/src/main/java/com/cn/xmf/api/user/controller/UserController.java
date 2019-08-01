package com.cn.xmf.api.user.controller;

import java.util.Date;
import java.util.List;

import com.cn.xmf.api.common.SysCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.cn.xmf.model.user.*;
import com.cn.xmf.base.model.*;
import com.cn.xmf.util.*;
import com.cn.xmf.api.user.service.*;

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
    private SysCommonService commonService; //如果不需要发钉钉消息可以注释了

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
            commonService.sendDingMessage("getList", parms, JSON.toJSONString(retData), msg, this.getClass());
        }
        logger.info("getList:(获取用户信息分页查询接口) 结束  parms={}", parms);
        return retData;
    }

    /**
     * getUserList:(获取用户信息不分页查询接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getUserList")
    public RetData getUserList(HttpServletRequest request, String parms) {
        RetData retData = new RetData();
        try {
            logger.info("getUserList:(获取用户信息不分页查询接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            User user = json.toJavaObject(User.class);
            if (user == null) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            List<User> list = userService.getUserList(user);
            retData.setData(list);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        } catch (Exception e) {
            retData.setCode(ResultCodeMessage.FAILURE);
            retData.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
            String msg = "getUserList:(获取用户信息不分页查询接口)====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            commonService.sendDingMessage("getUserList", parms, JSON.toJSONString(retData), msg, this.getClass());

        }
        logger.info("getUserList:(获取用户信息不分页查询接口) 结束  parms={},", parms);
        return retData;
    }

    /**
     * getUser:(查询用户信息单条数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getUser")
    public RetData getUser(HttpServletRequest request, String parms) {
        RetData retData = new RetData();
        try {
            logger.info("getUser:(查询用户信息单条数据接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            User user = json.toJavaObject(User.class);
            if (user == null) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            User retuser = userService.getUser(user);
            retData.setData(retuser);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        } catch (Exception e) {
            retData.setCode(ResultCodeMessage.FAILURE);
            retData.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
            String msg = "getUser:(查询用户信息单条数据接口) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            commonService.sendDingMessage("getUser", parms, JSON.toJSONString(retData), msg, this.getClass());

        }
        logger.info("getUser:(查询用户信息单条数据接口) 结束  parms={},", parms);
        return retData;
    }

    /**
     * delete:(逻辑删除用户信息数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping("delete")
    public RetData delete(HttpServletRequest request, String parms) {
        RetData retData = new RetData();
        try {
            logger.info("delete:(逻辑删除用户信息数据接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            Long id = json.getLong("id");
            if (id != null && id > 0) {
                userService.delete(id);
                retData.setCode(ResultCodeMessage.SUCCESS);
                retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
            } else {
                retData.setMessage("请选择需要删除的数据");
            }
        } catch (Exception e) {

            retData.setCode(ResultCodeMessage.FAILURE);
            retData.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
            String msg = "delete:(逻辑删除用户信息数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            commonService.sendDingMessage("delete", parms, JSON.toJSONString(retData), msg, this.getClass());
        }
        logger.info("delete:(逻辑删除用户信息数据接口) 结束  parms={}", parms);
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
    public RetData save(HttpServletRequest request, String parms) {
        RetData retData = new RetData();
        try {
            logger.info("save:(保存用户信息数据接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                retData.setMessage(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            User user = json.toJavaObject(User.class);
            // 无保存内容
            if (user == null) {
                retData.setMessage("无保存内容");
                return retData;
            }
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            // 保存数据库
            User ret = userService.save(user);
            if (ret != null) {
                retData.setCode(ResultCodeMessage.SUCCESS);
                retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
            }
        } catch (Exception e) {
            retData.setCode(ResultCodeMessage.FAILURE);
            retData.setMessage(ResultCodeMessage.FAILURE_MESSAGE);
            String msg = "save:(保存用户信息数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            commonService.sendDingMessage("save", parms, JSON.toJSONString(retData), msg, this.getClass());
        }
        logger.info("save:(保存用户信息数据接口) 结束  parms={}", parms);
        return retData;
    }

}