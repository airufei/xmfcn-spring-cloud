package com.cn.xmf.job.admin.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.user.model.JobUser;
import com.cn.xmf.job.admin.user.service.JobUserService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JobUserController(调度系统用户)
 *
 * @author rufei.cn
 * @version 2018-09-18
 */
@Controller
@RequestMapping("/user")
public class JobUserController {

    private static Logger logger = LoggerFactory.getLogger(JobUserController.class);
    @Autowired
    private JobUserService jobUserService;
    @Autowired
    private SysCommonService sysCommonService;

    @RequestMapping
    public String index() {
        return "user/jobUser-index";
    }
    /**
     * getList:(获取调度系统用户分页查询接口)
     *
     * @param request
     * @param request
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getList")
    @ResponseBody
    public Map<String, Object> getList(HttpServletRequest request) {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", 0);        // 总记录数
        maps.put("recordsFiltered", 0);    // 过滤后的总记录数
        maps.put("data", null);                    // 分页列表
        try {
            String startStr = request.getParameter("start");
            String length = request.getParameter("length");
            String username = request.getParameter("username");;
            String email = request.getParameter("email");;
            String phone = request.getParameter("phone");;
            int pageSize = 10;
            int pageNo = 1;
            int start = 0;
            if (StringUtil.isNotBlank(startStr)) {
                start = StringUtil.stringToInt(startStr);
            }
            if (StringUtil.isNotBlank(length)) {
                pageSize = StringUtil.stringToInt(length);
            }
            if (start > 0) {
                pageNo = (start / pageSize) + 1;
            }
            JSONObject object = StringUtil.getPageJSONObject(pageNo, pageSize);
            object.put("username",username);
            object.put("email",email);
            object.put("phone",phone);
            Partion pt = jobUserService.getList(object);
            List<JobUser> list = null;
            int totalCount = 0;
            if (pt != null) {
                list = (List<JobUser>) pt.getList();
                totalCount = pt.getPageCount();
            }
            maps.put("recordsTotal", totalCount);        // 总记录数
            maps.put("recordsFiltered", totalCount);    // 过滤后的总记录数
            maps.put("data", list);                    // 分页列表
        } catch (Exception e) {
            String msg = "getList:(获取调度系统用户分页查询接口) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("getList", null, JSON.toJSONString(maps), msg, this.getClass());

        }
        logger.info("getList:(获取调度系统用户分页查询接口) 结束");
        return maps;
    }

    /**
     * delete:(逻辑删除调度系统用户数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping("delete")
    @ResponseBody
    public ReturnT<String> delete(HttpServletRequest request, String parms) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "删除失败");
        try {
            logger.info("delete:(逻辑删除调度系统用户数据接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                returnT.setMsg("参数为空");
                return returnT;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                returnT.setMsg("参数为空");
                return returnT;
            }
            Long id = json.getLong("id");
            if (id != null && id > 0) {
                jobUserService.delete(id);
                returnT.setMsg("删除成功");
                returnT.setCode(ReturnT.SUCCESS_CODE);
            } else {
                returnT.setMsg("请选择需要删除的数据");
            }
        } catch (Exception e) {

            String msg = "delete:(逻辑删除调度系统用户数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("delete", parms, JSON.toJSONString(returnT), msg, this.getClass());
            returnT.setMsg("服务器繁忙，请稍后再试");
        }
        logger.info("delete:(逻辑删除调度系统用户数据接口) 结束  parms={}", parms);
        return returnT;
    }

    /**
     * save:(保存调度系统用户数据接口)
     *
     * @param request
     * @param jobUser
     * @return
     * @author rufei.cn
     */
    @RequestMapping("save")
    @ResponseBody
    public ReturnT<String> save(HttpServletRequest request, JobUser jobUser) {
        ReturnT<String> retData = new ReturnT<String>();
        try {
            // 无保存内容
            if (jobUser == null) {
                retData.setMsg("无保存内容");
                return retData;
            }
            String username = jobUser.getUsername();
            String phone = jobUser.getPhone();
            if(StringUtil.isBlank(username))
            {
                retData.setMsg("用户名不能为空");
                return retData;
            }
            if(StringUtil.isBlank(phone))
            {
                retData.setMsg("手机号不能为空");
                return retData;
            }
            if(!StringUtil.isMobilePhone(phone))
            {
                retData.setMsg("手机号格式不正确");
                return retData;
            }
            Long id = jobUser.getId();
            String password = jobUser.getPassword();
            if(id!=null&&id>=0)
            {
                jobUser.setCreateTime(new Date());
                jobUser.setUpdateTime(new Date());
            }else if(StringUtil.isBlank(password))
            {
                retData.setMsg("密码不能为空");
                return retData;
            }
            int length =-1;
            if(StringUtil.isNotBlank(password))
            {
                length = password.length();
                password = StringUtil.getEncryptPassword(password);
                jobUser.setPassword(password);
            }
            if(length!=-1&&(length<6||length>20))
            {
                retData.setMsg("密码长度6-20位");
                return retData;
            }
            // 保存数据库
            JobUser ret = jobUserService.save(jobUser);
            if (ret != null) {
                retData.setCode(ReturnT.SUCCESS_CODE);
                retData.setMsg("保存成功");
            }
        } catch (Exception e) {

            String msg = "save:(保存调度系统用户数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("save", null, JSON.toJSONString(retData), msg, this.getClass());
            retData.setMsg("服务器繁忙，请稍后再试");
            return retData;
        }
        logger.info("save:(保存调度系统用户数据接口) 结束");
        return retData;
    }



}