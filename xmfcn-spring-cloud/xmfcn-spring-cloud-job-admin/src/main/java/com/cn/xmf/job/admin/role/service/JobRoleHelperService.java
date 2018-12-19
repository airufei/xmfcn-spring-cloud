package com.cn.xmf.job.admin.role.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.role.dao.JobRoleDao;
import com.cn.xmf.job.admin.role.model.JobRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service(角色数据)
 *
 * @author airufei
 * @version 2018-12-19
 */
@Service
@SuppressWarnings("all")
public class JobRoleHelperService {

    @Autowired
    private JobRoleDao jobRoleDao;

    private static Logger logger = LoggerFactory.getLogger(JobRoleService.class);

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public int getTotalCount(JSONObject map) {
        int resCount = 0;
        Integer totalCount = jobRoleDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }


    /*
     * save(保存角色数据)
     * @param jobRole
     * @author airufei
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JobRole save(JobRole jobRole) {
        JobRole ret = null;
        if (jobRole == null) {
            return ret;
        }
        if (jobRole.getId() != null && jobRole.getId() > 0) {
            updateById(jobRole);
            ret = jobRole;
        } else {
            jobRole.setId(null);
            jobRoleDao.add(jobRole);
            ret = jobRole;
        }
        return ret;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public JobRole getJobRoleById(long id) {
        return jobRoleDao.getJobRoleById(id);
    }

    /**
     * 获取单条数据
     *
     * @param jobRole
     * @return
     * @author airufei
     */
    public JobRole getSignleJobRole(JobRole jobRole) {
        return jobRoleDao.getSignleJobRole(jobRole);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(JobRole jobRole) {
        jobRoleDao.updateById(jobRole);
    }

}