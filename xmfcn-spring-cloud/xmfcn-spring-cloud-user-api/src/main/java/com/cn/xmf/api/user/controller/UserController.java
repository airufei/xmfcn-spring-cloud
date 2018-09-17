package com.cn.xmf.api.user.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
 * @author airufei
 * @version 2018-09-16
 */
@RestController
@RequestMapping("/user/")
@SuppressWarnings("all")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserService userService;
	@Autowired
    private SysCommonService commonService; //如果不需要发钉钉消息可以注释了

	/**
	 * getList:(获取用户信息分页查询接口)
	 * @Author airufei
	 * @param request
	 * @param parms
	 * @return
	 */
	@RequestMapping("getList")
	public RetData getList(HttpServletRequest request, String parms){
		RetData retData = new RetData();
	   try {
	        logger.info("getList:(获取用户信息分页查询接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                retData.setMessage("参数为空");
                return retData;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                retData.setMessage("参数为空");
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
            retData.setData(jsonObject);
            retData.setCode(RetCode.SUCCESS);
        } catch (Exception e) {
            retData.setCode(RetCode.SYS_ERROR);
            retData.setMessage(RetMessage.SYS_ERROR);
            String msg="getList:(获取用户信息分页查询接口) 异常====>"+ StringUtil.getExceptionMsg(e);
            logger.error(msg);
            commonService.sendDingMessage("getList", parms, JSON.toJSONString(retData), msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getList:(获取用户信息分页查询接口) 结束  parms={}", parms);
        return retData;
	}

     /**
     * getUserList:(获取用户信息不分页查询接口)
     * @Author airufei
     * @param request
     * @param parms
     * @return
     */
    @RequestMapping("getUserList")
    public RetData getUserList(HttpServletRequest request, String parms) {
       RetData retData = new RetData();
        try {
            logger.info("getUserList:(获取用户信息不分页查询接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                retData.setMessage("参数为空");
                return retData;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                retData.setMessage("参数为空");
                return retData;
            }
            User user=json.toJavaObject(User.class);
            if (user == null) {
                retData.setMessage("参数为空");
                return retData;
            }
            List<User> list= userService.getUserList(user);
            retData.setData(list);
            retData.setCode(RetCode.SUCCESS);
            retData.setMessage(RetMessage.SUCCESS);
        } catch (Exception e) {
            retData.setCode(RetCode.SYS_ERROR);
            retData.setMessage(RetMessage.SYS_ERROR);
            String msg="getUserList:(获取用户信息不分页查询接口)====>"+ StringUtil.getExceptionMsg(e);
            logger.error(msg);
            commonService.sendDingMessage("getUserList", parms, JSON.toJSONString(retData), msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getUserList:(获取用户信息不分页查询接口) 结束  parms={},", parms);
        return retData;
    }

     /**
     * getUser:(查询用户信息单条数据接口)
     * @Author airufei
     * @param request
     * @param parms
     * @return
     */
    @RequestMapping("getUser")
    public RetData getUser(HttpServletRequest request, String parms) {
        RetData retData = new RetData();
        try {
            logger.info("getUser:(查询用户信息单条数据接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                retData.setMessage("参数为空");
                return retData;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                retData.setMessage("参数为空");
                return retData;
            }
            User user=json.toJavaObject(User.class);
            if (user == null) {
                retData.setMessage("参数为空");
                return retData;
            }
            User retuser= userService.getUser(user);
            retData.setData(retuser);
            retData.setCode(RetCode.SUCCESS);
            retData.setMessage(RetMessage.SUCCESS);
        } catch (Exception e) {
            retData.setCode(RetCode.SYS_ERROR);
            retData.setMessage(RetMessage.SYS_ERROR);
            String msg="getUser:(查询用户信息单条数据接口) 异常====>"+StringUtil.getExceptionMsg(e);
            logger.error(msg);
            commonService.sendDingMessage("getUser", parms, JSON.toJSONString(retData), msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getUser:(查询用户信息单条数据接口) 结束  parms={},", parms);
        return retData;
    }

	/**
	 * delete:(逻辑删除用户信息数据接口)
	 * @Author airufei
     * @param request
     * @param parms
     * @return
	 */
	@RequestMapping("delete")
	public RetData delete(HttpServletRequest request, String parms){
	    RetData retData = new RetData();
        try {
            logger.info("delete:(逻辑删除用户信息数据接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                retData.setMessage("参数为空");
                return retData;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                retData.setMessage("参数为空");
                return retData;
            }
            Long id = json.getLong("id");
            if (id != null && id > 0) {
                userService.delete(id);
                retData.setCode(RetCode.SUCCESS);
                retData.setMessage(RetMessage.SUCCESS);
            } else {
                retData.setMessage("请选择需要删除的数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retData.setCode(RetCode.SYS_ERROR);
            retData.setMessage(RetMessage.SYS_ERROR);
            String msg="delete:(逻辑删除用户信息数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            commonService.sendDingMessage("delete", parms, JSON.toJSONString(retData), msg, this.getClass());
        }
        logger.info("delete:(逻辑删除用户信息数据接口) 结束  parms={}", parms);
        return retData;
	}
	
	/**
	 * save:(保存用户信息数据接口)
	 * @Author airufei
     * @param request
     * @param parms
     * @return
	 */
	@RequestMapping(value = "save")
    public RetData save(HttpServletRequest request, String parms) {
		RetData retData = new RetData();
        try {
            logger.info("save:(保存用户信息数据接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                retData.setMessage("参数为空");
                return retData;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                retData.setMessage("参数为空");
                return retData;
            }
            User user=json.toJavaObject(User.class);
            // 无保存内容
            if (user == null) {
                retData.setMessage("无保存内容");
                return retData;
            }
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            // 保存数据库
            User ret =userService.save(user);
            if(ret!=null)
            {
              retData.setCode(RetCode.SUCCESS);
              retData.setMessage(RetMessage.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retData.setCode(RetCode.SYS_ERROR);
            retData.setMessage(RetMessage.SYS_ERROR);
            String msg="save:(保存用户信息数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            commonService.sendDingMessage("save", parms, JSON.toJSONString(retData), msg, this.getClass());
        }
        logger.info("save:(保存用户信息数据接口) 结束  parms={}", parms);
        return retData;
	}

}