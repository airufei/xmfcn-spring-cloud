package com.cn.xmf.job.admin.role.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.role.dao.JobRoleDao;
import com.cn.xmf.job.admin.role.model.JobRole;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Service(角色数据)
 *
 * @author airufei
 * @version 2018-12-19
 */
@Service
@SuppressWarnings("all")
public class JobRoleService {

    @Autowired
    private JobRoleDao jobRoleDao;
    @Autowired
    private JobRoleHelperService jobRoleHelperService;
    @Autowired
    private SysCommonService sysCommonService;
    private static Logger logger = LoggerFactory.getLogger(JobRoleService.class);

    /**
     * getList(获取角色数据带分页数据-服务)
     *
     * @param json
     * @return
     * @author airufei
     */
    public Partion getList(@RequestBody JSONObject json) {
        logger.info("getList(获取角色数据带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        Partion pt = null;
        try {
            int totalcount = jobRoleHelperService.getTotalCount(json);
            List<JobRole> list = null;
            if (totalcount > 0) {
                list = jobRoleDao.getList(json);
            }
            pt = new Partion(json, list, totalcount);
        } catch (Exception e) {
            String msg = "getList(获取角色数据 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            String parms = null;
            if (json != null) {
                parms = json.toString();
            }
            sysCommonService.sendDingMessage("base-service[getList]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getList(获取角色数据带分页数据-服务) 结束 ");
        return pt;
    }

    /**
     * getJobRoleList(获取角色数据 不带分页数据-服务)
     *
     * @param jobRole
     * @return
     * @author airufei
     */
    public List<JobRole> getJobRoleList(@RequestBody JobRole jobRole) {
        String parms = JSON.toJSONString(jobRole);
        List<JobRole> list = null;
        logger.info("getJobRoleList(获取角色数据 不带分页数据-服务) 开始 parms={}", parms);
        if (jobRole == null) {
            return list;
        }
        try {
            list = jobRoleDao.getJobRoleList(jobRole);
        } catch (Exception e) {
            String msg = "getJobRoleList 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[getJobRoleList]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getJobRoleList(获取角色数据 不带分页数据-服务) 结束");
        return list;
    }


    /**
     * save (保存角色数据 数据-服务)
     *
     * @param jobRole
     * @return
     * @author airufei
     */
    public JobRole save(@RequestBody JobRole jobRole) {
        String parms = JSON.toJSONString(jobRole);
        logger.info("save (保存角色数据 数据-服务) 开始 parms={}", parms);
        JobRole ret = null;
        if (jobRole == null) {
            return ret;
        }
        try {
            ret = jobRoleHelperService.save(jobRole);
        } catch (Exception e) {
            String msg = "save (保存角色数据 数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            ret = null;
            sysCommonService.sendDingMessage("base-service[save]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("save (保存角色数据 数据-服务) 结束");
        return ret;
    }

    /**
     * getJobRole(获取角色数据单条数据-服务)
     *
     * @param jobRole
     * @return
     * @author airufei
     */
    public JobRole getJobRole(@RequestBody JobRole jobRole) {
        JobRole ret = null;
        String parms = JSON.toJSONString(jobRole);
        List<JobRole> list = null;
        logger.info("getJobRole(获取角色数据单条数据-服务) 开始 parms={}", parms);
        if (jobRole == null) {
            return ret;
        }
        try {
            ret = jobRoleHelperService.getSignleJobRole(jobRole);
        } catch (Exception e) {
            String msg = "getJobRole(获取角色数据单条数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[getJobRole]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getJobRole(获取角色数据单条数据-服务) 结束 ");
        return ret;
    }

    /**
     * delete(逻辑删除角色数据数据-服务)
     *
     * @param id
     * @return
     * @author airufei
     */
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除角色数据数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        JobRole dt = jobRoleHelperService.getJobRoleById(id);
        if (dt == null) {
            return isSuccess;
        }
        jobRoleDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除角色数据数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }
}