package com.cn.xmf.job.admin.role.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.role.model.JobMenuRole;
import com.cn.xmf.job.admin.role.service.JobMenuRoleService;
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

/**
 * JobMenuRoleController(角色菜单关系)
 *
 * @author airufei
 * @version 2018-12-19
 */
@Controller
@RequestMapping("/jobMenuRole")
@SuppressWarnings("all")
public class JobMenuRoleController {

    private static Logger logger = LoggerFactory.getLogger(JobMenuRoleService.class);
    @Autowired
    private JobMenuRoleService jobMenuRoleService;
    @Autowired
    private SysCommonService sysCommonService;

    @RequestMapping
    public String index() {
        return "role/jobMenuRole-index";
    }

    /**
     * getList:(获取角色菜单关系分页查询接口)
     *
     * @param request
     * @return
     * @Author airufei
     */
    @RequestMapping("pageList")
    @ResponseBody
    public JSONObject getList(HttpServletRequest request) {
        JSONObject retJon = new JSONObject();
        JSONObject param = null;
        try {
            String startStr = request.getParameter("start");
            String length = request.getParameter("length");
            String roleId = request.getParameter("roleId");
            String menuId = request.getParameter("menuId");
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
            logger.info("getList:(获取角色菜单关系分页查询接口) 开始  param={}", param);

            param.put("roleId", roleId);
            param.put("menuId", menuId);
            Partion pt = jobMenuRoleService.getList(param);
            List<JobMenuRole> list = null;
            int totalCount = 0;
            if (pt != null) {
                list = (List<JobMenuRole>) pt.getList();
                totalCount = pt.getTotalCount();
            }
            retJon.put("data", list);
            retJon.put("recordsTotal", totalCount);
            retJon.put("recordsFiltered", totalCount);
        } catch (Exception e) {
            String msg = "getList:(获取角色菜单关系分页查询接口) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("getList", param.toString(), JSON.toJSONString(retJon), msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getList:(获取角色菜单关系分页查询接口) 结束");
        return retJon;
    }

    /**
     * delete:(逻辑删除角色菜单关系数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
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
            boolean ret = jobMenuRoleService.delete(newId);
            if (ret) {
                returnT.setCode(ReturnT.SUCCESS_CODE);
                returnT.setMsg("成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnT = returnT.FAIL;
            String msg = "delete:(逻辑删除角色菜单关系数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("delete", ids, null, msg, this.getClass());
        }
        logger.info("delete 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

    /**
     * save:(保存角色菜单关系数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public ReturnT<String> save(JobMenuRole jobMenuRole) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "保存数据失败");
        String parms = null;
        try {
            parms = JSON.toJSONString(jobMenuRole);
            logger.info("save:(保存角色菜单关系数据接口) 开始  parms={}", parms);
            if (jobMenuRole == null) {
                returnT.setMsg("参数为空");
                return returnT;
            }
            if (jobMenuRole == null) {
                returnT.setMsg("参数错误");
                return returnT;
            }
            JobMenuRole ret = jobMenuRoleService.save(jobMenuRole);
            if (ret == null) {
                returnT.setMsg("保存数据失败");
                return returnT;
            }
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setMsg("成功");
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "save:(保存角色菜单关系数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("save", parms, JSON.toJSONString(returnT), msg, this.getClass());
            returnT.setMsg("服务器繁忙，请稍后再试");
        }
        logger.info("save 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

}