package com.cn.xmf.job.admin.menu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.menu.dao.JobMenuDao;
import com.cn.xmf.job.admin.menu.model.JobMenu;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

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
    private SysCommonService sysCommonService;
    private static Logger logger = LoggerFactory.getLogger(JobMenuService.class);

    /**
     * getList(获取job-菜单带分页数据-服务)
     *
     * @param json
     * @return
     * @author airufei
     */
    public Partion getList(@RequestBody JSONObject json) {
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
    public List<JobMenu> getJobMenuList(@RequestBody JobMenu jobMenu) {
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
    public JobMenu save(@RequestBody JobMenu jobMenu) {
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
    public JobMenu getJobMenu(@RequestBody JobMenu jobMenu) {
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
}