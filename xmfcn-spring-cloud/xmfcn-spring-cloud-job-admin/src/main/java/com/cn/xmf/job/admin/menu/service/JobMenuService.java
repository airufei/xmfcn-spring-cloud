package com.cn.xmf.job.admin.menu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.menu.dao.JobMenuDao;
import com.cn.xmf.job.admin.menu.model.JobMenu;
import com.cn.xmf.job.admin.menu.model.MenuNode;
import com.cn.xmf.job.admin.role.model.JobMenuRole;
import com.cn.xmf.job.admin.role.service.JobMenuRoleService;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service(job-菜单)
 *
 * @author airufei
 * @version 2018-10-10
 */
@Service
@SuppressWarnings("all")
public class JobMenuService {

    @Autowired
    private JobMenuDao jobMenuDao;
    @Autowired
    private JobMenuHelperService jobMenuHelperService;
    @Autowired
    private JobMenuRoleService jobMenuRoleService;

    @Autowired
    private SysCommonService sysCommonService;
    private static Logger logger = LoggerFactory.getLogger(JobMenuService.class);

    /**
     * getList(获取job-菜单带分页数据-服务)
     *
     * @param json
     * @return
     * @author airufei
     */
    public Partion getList(JSONObject json) {
        logger.info("getList(获取job-菜单带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        Partion pt = null;
        try {
            int totalcount = jobMenuHelperService.getTotalCount(json);
            List<JobMenu> list = null;
            if (totalcount > 0) {
                list = jobMenuDao.getList(json);
            }
            pt = new Partion(json, list, totalcount);
        } catch (Exception e) {
            String msg = "getList(获取job-菜单 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            String parms = null;
            if (json != null) {
                parms = json.toString();
            }
            sysCommonService.sendDingMessage("base-service[getList]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getList(获取job-菜单带分页数据-服务) 结束 ");
        return pt;
    }

    /**
     * getJobMenuList(获取job-菜单 不带分页数据-服务)
     *
     * @param jobMenu
     * @return
     * @author airufei
     */
    public List<JobMenu> getJobMenuList(JobMenu jobMenu) {
        String parms = JSON.toJSONString(jobMenu);
        List<JobMenu> list = null;
        logger.info("getJobMenuList(获取job-菜单 不带分页数据-服务) 开始 parms={}", parms);
        if (jobMenu == null) {
            return list;
        }
        try {
            list = jobMenuDao.getJobMenuList(jobMenu);
        } catch (Exception e) {
            String msg = "getJobMenuList 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[getJobMenuList]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getJobMenuList(获取job-菜单 不带分页数据-服务) 结束");
        return list;
    }


    /**
     * save (保存job-菜单 数据-服务)
     *
     * @param jobMenu
     * @return
     * @author airufei
     */
    public JobMenu save(JobMenu jobMenu) {
        String parms = JSON.toJSONString(jobMenu);
        logger.info("save (保存job-菜单 数据-服务) 开始 parms={}", parms);
        if (jobMenu == null) {
            return jobMenu;
        }
        try {
            jobMenu = jobMenuHelperService.save(jobMenu);
        } catch (Exception e) {
            String msg = "save (保存job-菜单 数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[save]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("save (保存job-菜单 数据-服务) 结束");
        return jobMenu;
    }

    /**
     * getJobMenu(获取job-菜单单条数据-服务)
     *
     * @param jobMenu
     * @return
     * @author airufei
     */
    public JobMenu getJobMenu(JobMenu jobMenu) {
        JobMenu ret = null;
        String parms = JSON.toJSONString(jobMenu);
        List<JobMenu> list = null;
        logger.info("getJobMenu(获取job-菜单单条数据-服务) 开始 parms={}", parms);
        if (jobMenu == null) {
            return ret;
        }
        try {
            ret = jobMenuHelperService.getSignleJobMenu(jobMenu);
        } catch (Exception e) {
            String msg = "getJobMenu(获取job-菜单单条数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[getJobMenu]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getJobMenu(获取job-菜单单条数据-服务) 结束 ");
        return ret;
    }

    /**
     * delete(逻辑删除job-菜单数据-服务)
     *
     * @param id
     * @return
     * @author airufei
     */
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除job-菜单数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        JobMenu dt = jobMenuHelperService.getJobMenuById(id);
        if (dt == null) {
            return isSuccess;
        }
        jobMenuDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除job-菜单数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }

    /**
     * 获取参数树
     *
     * @param level        菜单等级
     * @param fid          父级ID
     * @param roleId       角色ID 用于查询所具备的权限菜单信息
     * @param pageType     当前查询的页面
     * @param selecdRoleId //角色ID  用于查询这个角色下的需要选中的菜单
     * @return
     */
    public List<MenuNode> getTreeList(int level, Long fid, long roleId, String pageType, long selecdRoleId) {
        List<MenuNode> list = null;
        List<JobMenu> menuList = null;
        if ("left_menu".equals(pageType)) {
            Map parms = new HashMap();
            parms.put("flag", 1);
            parms.put("level", level);
            parms.put("roleId", roleId);
            if (fid != null && fid > 0) {
                parms.put("fid", fid);
            }
            menuList = jobMenuDao.getRoleMenuList(parms);
        } else {
            JobMenu job = new JobMenu();
            job.setLevel(level);
            if (fid != null && fid > 0) {
                job.setFid(fid);
            }
            menuList = jobMenuDao.getJobMenuList(job);
        }
        if (menuList == null || menuList.size() <= 0) {
            return list;
        }
        int size = menuList.size();
        list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            JobMenu jobMenu = menuList.get(i);
            if (jobMenu == null) {
                continue;
            }
            Long id = jobMenu.getId();
            level = jobMenu.getLevel() + 1;
            List<MenuNode> nodeList = getTreeList(level, id, roleId, pageType, selecdRoleId);
            MenuNode node = new MenuNode();
            if (nodeList != null && nodeList.size() > 0) {
                node.setNodes(nodeList);
            }
            boolean isChecked = isNodeChecked(selecdRoleId, id);
            JSONObject state = new JSONObject();
            state.put("checked", isChecked);
            node.setText(jobMenu.getName());
            node.setState(state);
            if (i % 2 == 0) {
                node.setIcon("fa fa-circle-o text-red");
            } else {
                node.setIcon("fa fa-circle-o text-aqua");
            }
            if ("left_menu".equals(pageType)) {
                node.setHref(jobMenu.getUrl());
            }
            node.setSelectable(false);
            node.setNodeid(jobMenu.getId());
            list.add(node);
        }
        return list;
    }

    /**
     * 判断当前节点是否需要选中
     *
     * @param roleId
     * @param menuId
     * @return
     */
    public boolean isNodeChecked(long roleId, Long menuId) {
        boolean isChecked = false;
        if(roleId<=0)
        {
            return isChecked;
        }
        List<JobMenu> roleList = getJobMenuRoles(roleId, null);
        if (roleList == null || roleList.size() <= 0) {
            return isChecked;
        }
        int size = roleList.size();
        for (int i = 0; i < size; i++) {
            JobMenu menuRole = roleList.get(i);
            Long roleMenuId = menuRole.getId();
            if (roleMenuId == menuId) {
                isChecked = true;
                break;
            }
        }
        return isChecked;
    }

    /**
     * 根据角色ID获取需要选中的菜单数据
     *
     * @param roleId
     * @return
     */
    public List<JobMenu> getJobMenuRoles(long roleId, String roleCode) {
        List<JobMenu> list = null;
        String key = ConstantUtil.CACHE_KEY_PREFIX_MENU_ + roleId + roleCode;
        String cache = sysCommonService.getCache(key);
        list = JSONObject.parseArray(cache, JobMenu.class);
        if (list != null && list.size() > 0) {
            return list;
        }
        Map parms = new HashMap();
        parms.put("flag", 1);
        if (roleId > 0) {
            parms.put("roleId", roleId);
        }
        if (StringUtil.isNotBlank(roleCode)) {
            parms.put("roleCode", roleCode);
        }
        list = jobMenuDao.getRoleMenuList(parms);
        if (list != null && list.size() > 0) {
            sysCommonService.save(key, JSON.toJSONString(list), 30);
        }
        return list;
    }
}