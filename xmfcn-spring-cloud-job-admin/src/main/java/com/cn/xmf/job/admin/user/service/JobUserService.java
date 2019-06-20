package com.cn.xmf.job.admin.user.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.user.dao.JobUserDao;
import com.cn.xmf.job.admin.user.model.JobUser;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Service(调度系统用户)
 *
 * @author rufei.cn
 * @version 2018-09-18
 */
@Service
@SuppressWarnings("all")
public class JobUserService {

    @Autowired
    private JobUserDao jobUserDao;
    @Autowired
    private JobUserHelperService jobUserHelperService;
    @Autowired
    private SysCommonService sysCommonService;
    private static Logger logger = LoggerFactory.getLogger(JobUserService.class);

    /**
     * getList(获取调度系统用户带分页数据-服务)
     *
     * @param json
     * @return
     * @author rufei.cn
     */
    public Partion getList(@RequestBody JSONObject json) {
        logger.info("getList(获取调度系统用户带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        Partion pt = null;
        try {
            int totalcount = jobUserHelperService.getTotalCount(json);
            List<JobUser> list = null;
            if (totalcount > 0) {
                list = jobUserDao.getList(json);
            }
            pt = new Partion(json, list, totalcount);
        } catch (Exception e) {
            String msg = "getList(获取调度系统用户 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            String parms = null;
            if (json != null) {
                parms = json.toString();
            }
            sysCommonService.sendDingMessage("base-service[getList]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getList(获取调度系统用户带分页数据-服务) 结束 ");
        return pt;
    }

    /**
     * getJobUserList(获取调度系统用户 不带分页数据-服务)
     *
     * @param jobUser
     * @return
     * @author rufei.cn
     */
    public List<JobUser> getJobUserList(@RequestBody JobUser jobUser) {
        String parms = JSON.toJSONString(jobUser);
        List<JobUser> list = null;
        logger.info("getJobUserList(获取调度系统用户 不带分页数据-服务) 开始 parms={}", parms);
        if (jobUser == null) {
            return list;
        }
        try {
            list = jobUserDao.getJobUserList(jobUser);
        } catch (Exception e) {
            String msg = "getJobUserList 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[getJobUserList]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getJobUserList(获取调度系统用户 不带分页数据-服务) 结束");
        return list;
    }


    /**
     * save (保存调度系统用户 数据-服务)
     *
     * @param jobUser
     * @return
     * @author rufei.cn
     */
    public JobUser save(@RequestBody JobUser jobUser) {
        String parms = JSON.toJSONString(jobUser);
        logger.info("save (保存调度系统用户 数据-服务) 开始 parms={}", parms);
        if (jobUser == null) {
            return jobUser;
        }
        try {
            jobUser = jobUserHelperService.save(jobUser);
        } catch (Exception e) {
            String msg = "save (保存调度系统用户 数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[save]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("save (保存调度系统用户 数据-服务) 结束");
        return jobUser;
    }


    /**
     * getJobUser(获取调度系统用户单条数据-服务)
     *
     * @param jobUser
     * @return
     * @author rufei.cn
     */
    public JobUser getJobUser(@RequestBody JobUser jobUser) {
        JobUser ret = null;
        String parms = JSON.toJSONString(jobUser);
        List<JobUser> list = null;
        logger.info("getJobUser(获取调度系统用户单条数据-服务) 开始 parms={}", parms);
        if (jobUser == null) {
            return ret;
        }
        try {
            ret = jobUserHelperService.getSignleJobUser(jobUser);
        } catch (Exception e) {
            String msg = "getJobUser(获取调度系统用户单条数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("base-service[getJobUser]", parms, null, msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("getJobUser(获取调度系统用户单条数据-服务) 结束 ");
        return ret;
    }


    /**
     * delete(逻辑删除调度系统用户数据-服务)
     *
     * @param id
     * @return
     * @author rufei.cn
     */
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除调度系统用户数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        JobUser dt = jobUserHelperService.getJobUserById(id);
        if (dt == null) {
            return isSuccess;
        }
        jobUserDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除调度系统用户数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }
}