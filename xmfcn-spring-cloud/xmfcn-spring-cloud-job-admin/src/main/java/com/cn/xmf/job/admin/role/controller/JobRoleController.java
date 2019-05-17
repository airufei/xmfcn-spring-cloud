package com.cn.xmf.job.admin.role.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.role.model.JobRole;
import com.cn.xmf.job.admin.role.service.JobMenuRoleService;
import com.cn.xmf.job.admin.role.service.JobRoleService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * JobRoleController(角色数据)
 *
 * @author rufei.cn
 * @version 2018-12-19
 */
@Controller
@RequestMapping("/jobRole")
@SuppressWarnings("all")
public class JobRoleController {

    private static Logger logger = LoggerFactory.getLogger(JobRoleService.class);
    @Autowired
    private JobRoleService jobRoleService;
    @Autowired
    private SysCommonService sysCommonService;
    @Autowired
    private JobMenuRoleService jobMenuRoleService;

    @RequestMapping
    public String index() {
        return "role/jobRole-index";
    }

    /**
     * getList:(获取角色数据分页查询接口)
     *
     * @param request
     * @return
     * @author rufei.cn
     */
    @RequestMapping("pageList")
    @ResponseBody
    public JSONObject getList(HttpServletRequest request) {
        JSONObject retJon = new JSONObject();
        JSONObject param = null;
        try {
            String startStr = request.getParameter("start");
            String length = request.getParameter("length");
            String name = request.getParameter("name");
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
            param = StringUtil.getPageJSONObject(pageNo, pageSize);
            logger.info("getList:(获取角色数据分页查询接口) 开始  param={}", param);

            param.put("name", name);
            Partion pt = jobRoleService.getList(param);
            List<JobRole> list = null;
            int totalCount = 0;
            if (pt != null) {
                list = (List<JobRole>) pt.getList();
                totalCount = pt.getTotalCount();
            }
            retJon.put("data", list);
            retJon.put("recordsTotal", totalCount);
            retJon.put("recordsFiltered", totalCount);
        } catch (Exception e) {
            String msg = "getList:(获取角色数据分页查询接口) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("getList", param.toString(), JSON.toJSONString(retJon), msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getList:(获取角色数据分页查询接口) 结束");
        return retJon;
    }

    /**
     * delete:(逻辑删除角色数据数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping("delete")
    @ResponseBody
    public ReturnT<String> delete(HttpServletRequest request) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "删除失败");
        String ids = null;
        try {
            ids = request.getParameter("id");
            int id = StringUtil.stringToInt(ids);
            logger.info("delete 开始============>" + id);
            if (id <= 0) {
                returnT.setMsg("参数错误");
                return returnT;
            }
            long newId = id;
            boolean ret = jobRoleService.delete(newId);
            if (ret) {
                returnT.setCode(ReturnT.SUCCESS_CODE);
                returnT.setMsg("成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnT = returnT.FAIL;
            String msg = "delete:(逻辑删除角色数据数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("delete", ids, null, msg, this.getClass());
        }
        logger.info("delete 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

    /**
     * save:(保存角色数据数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public ReturnT<String> save(JobRole jobRole,HttpServletRequest request) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "保存数据失败");
        String parms = null;
        try {
            parms = JSON.toJSONString(jobRole);
            logger.info("save:(保存角色数据数据接口) 开始  parms={}", parms);
            if (jobRole == null) {
                returnT.setMsg("参数为空");
                return returnT;
            }
            if (jobRole == null) {
                returnT.setMsg("参数错误");
                return returnT;
            }
            Long roleId = jobRole.getId();
            boolean isDelete=true;
            if(roleId!=null&&roleId>0)
            {
                isDelete= jobMenuRoleService.delete(roleId);
            }
            if(!isDelete)
            {
                returnT.setMsg("保存数据失败");
                return returnT;
            }
            JobRole ret = jobRoleService.save(jobRole);
            if (ret == null) {
                returnT.setMsg("保存数据失败");
                return returnT;
            }
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setMsg("成功");
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "save:(保存角色数据数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("save", parms, JSON.toJSONString(returnT), msg, this.getClass());
            returnT.setMsg("服务器繁忙，请稍后再试");
        }
        logger.info("save 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

}