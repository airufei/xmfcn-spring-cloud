package com.cn.xmf.job.admin.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.user.service.JobUserService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.model.user.JobUser;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JobUserController(调度系统用户)
 *
 * @author airufei
 * @version 2018-09-18
 */
@RestController
@RequestMapping("/user/")
@SuppressWarnings("all")
public class JobUserController {

    private static Logger logger = LoggerFactory.getLogger(JobUserController.class);
    @Autowired
    private JobUserService jobUserService;
    @Autowired
    private SysCommonService sysCommonService;

    /**
     * getList:(获取调度系统用户分页查询接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
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
            e.printStackTrace();
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
     * @Author airufei
     */
    @RequestMapping("delete")
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
            e.printStackTrace();
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
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "save")
    public ReturnT<String> save(HttpServletRequest request, String parms) {
        ReturnT<String> dataReturn = new ReturnT<String>();
        try {
            logger.info("save:(保存调度系统用户数据接口) 开始  parms={}", parms);
            if (StringUtil.isBlank(parms)) {
                dataReturn.setMsg("参数为空");
                return dataReturn;
            }
            JSONObject json = JSONObject.parseObject(parms);
            if (json == null) {
                dataReturn.setMsg("参数为空");
                return dataReturn;
            }
            JobUser jobUser = json.toJavaObject(JobUser.class);
            // 无保存内容
            if (jobUser == null) {
                dataReturn.setMsg("无保存内容");
                return dataReturn;
            }
            jobUser.setCreateTime(new Date());
            jobUser.setUpdateTime(new Date());
            // 保存数据库
            JobUser ret = jobUserService.save(jobUser);
            if (ret != null) {
                dataReturn.setCode(ReturnT.SUCCESS_CODE);
                dataReturn.setMsg("保存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "save:(保存调度系统用户数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("save", parms, JSON.toJSONString(dataReturn), msg, this.getClass());
            dataReturn.setMsg("服务器繁忙，请稍后再试");
            return dataReturn;
        }
        logger.info("save:(保存调度系统用户数据接口) 结束  parms={}", parms);
        return dataReturn;
    }

}