package com.cn.xmf.job.admin.menu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.menu.model.JobMenu;
import com.cn.xmf.job.admin.menu.model.MenuNode;
import com.cn.xmf.job.admin.menu.service.JobMenuService;
import com.cn.xmf.job.admin.role.model.JobRole;
import com.cn.xmf.job.admin.role.service.JobRoleService;
import com.cn.xmf.job.admin.user.model.JobUser;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * JobMenuController(job-菜单)
 *
 * @author rufei.cn
 * @version 2018-10-17
 */
@Controller
@RequestMapping("/jobMenu")
@SuppressWarnings("all")
public class JobMenuController {

    private static Logger logger = LoggerFactory.getLogger(JobMenuService.class);
    @Autowired
    private JobMenuService jobMenuService;
    @Autowired
    private SysCommonService sysCommonService;
    @Autowired
    private JobRoleService jobRoleService;


    @RequestMapping
    public String index(HttpServletRequest request, Model model) {
        JobMenu jobMenu = new JobMenu();
        jobMenu.setLevel(1);
        List<JobMenu> menuList = jobMenuService.getJobMenuList(jobMenu);
        if (menuList == null) {
            menuList = new ArrayList<>();
        }
        List<JobMenu> list = new ArrayList<>();
        jobMenu.setName("无上级");
        long id = -1;
        jobMenu.setId(id);
        list.add(0, jobMenu);
        for (JobMenu item : menuList) {
            list.add(item);
        }
        model.addAttribute("menuList", list);
        return "menu/jobMenu-index";
    }


    /**
     * 获取参数树
     *
     * @param uId
     * @return
     */
    @RequestMapping("getTreeList")
    @ResponseBody
    public List<MenuNode> getTreeList(HttpServletRequest request) {
        List<MenuNode> list = null;
        HttpSession session = request.getSession();
        logger.info("1==========================================================1");
        JobUser user = (JobUser) session.getAttribute("user");
        if (user == null) {
            return list;
        }
        String roleCode = user.getRoleCode();
        if (StringUtil.isBlank(roleCode)) {
            return list;
        }
        String pageType = request.getParameter("pageType");
        long  selecdRoleId = StringUtil.stringToLong(request.getParameter("roleId"));
        if (!"left_menu".equals(pageType)) {
            roleCode = "admin_role";
        }
        JobRole role = new JobRole();
        role.setRoleCode(roleCode);
        JobRole jobRole = jobRoleService.getJobRole(role);
        if (jobRole == null) {
            return list;
        }
        Long roleId = jobRole.getId();
        int level = 1;//默认从根节点开始查询
        list = jobMenuService.getTreeList(1, null, roleId, pageType,selecdRoleId);
        return list;
    }

    /**
     * getList:(获取job-菜单分页查询接口)
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
            String type = request.getParameter("type");
            String name = request.getParameter("name");
            String fid = request.getParameter("fid");
            String level = request.getParameter("level");
            if ("-1".equals(fid)) {
                fid = null;
            }
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
            logger.info("getList:(获取job-菜单分页查询接口) 开始  param={}", param);
            param.put("name", name);
            param.put("fid", fid);
            param.put("level", level);
            Partion pt = jobMenuService.getList(param);
            List<JobMenu> list = null;
            int totalCount = 0;
            if (pt != null) {
                list = (List<JobMenu>) pt.getList();
                totalCount = pt.getTotalCount();
            }
            retJon.put("data", list);
            retJon.put("recordsTotal", totalCount);
            retJon.put("recordsFiltered", totalCount);
        } catch (Exception e) {
            String msg = "getList:(获取job-菜单分页查询接口) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("getList", param.toString(), JSON.toJSONString(retJon), msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getList:(获取job-菜单分页查询接口) 结束");
        return retJon;
    }

    /**
     * delete:(逻辑删除job-菜单数据接口)
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
            boolean ret = jobMenuService.delete(newId);
            if (ret) {
                returnT.setCode(ReturnT.SUCCESS_CODE);
                returnT.setMsg("成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnT = returnT.FAIL;
            String msg = "delete:(逻辑删除job-菜单数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("delete", ids, null, msg, this.getClass());
        }
        logger.info("delete 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

    /**
     * save:(保存job-菜单数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public ReturnT<String> save(JobMenu jobMenu, HttpServletRequest request) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "保存数据失败");
        String parms = null;
        try {
            Map<String, String[]> map = request.getParameterMap();
            logger.info(JSON.toJSONString(map));
            parms = JSON.toJSONString(jobMenu);
            logger.info("save:(保存job-菜单数据接口) 开始  parms={}", parms);
            if (jobMenu == null) {
                returnT.setMsg("参数为空");
                return returnT;
            }
            if (jobMenu == null) {
                returnT.setMsg("参数错误");
                return returnT;
            }
            String url = jobMenu.getUrl();
            Integer level = jobMenu.getLevel();
            if (level == null) {
                returnT.setMsg("菜单级别level不能为空");
                return returnT;
            }
            if (level == 1) {
                jobMenu.setUrl(null);
            }
            jobMenu.setCreateTime(new Date());
            jobMenu.setUpdateTime(new Date());
            JobMenu ret = jobMenuService.save(jobMenu);
            if (ret == null) {
                returnT.setMsg("保存数据失败");
                return returnT;
            }
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setMsg("成功");
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "save:(保存job-菜单数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("save", parms, JSON.toJSONString(returnT), msg, this.getClass());
            returnT.setMsg("服务器繁忙，请稍后再试");
        }
        logger.info("save 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

}