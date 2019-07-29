package com.cn.xmf.job.admin.role.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.role.dao.JobMenuRoleDao;
import com.cn.xmf.job.admin.role.model.JobMenuRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service(角色菜单关系)
 *
 * @author rufei.cn
 * @version 2018-12-19
 */
@Service
@SuppressWarnings("all")
public class JobMenuRoleHelperService {

    @Autowired
    private JobMenuRoleDao jobMenuRoleDao;
    private static Logger logger = LoggerFactory.getLogger(JobMenuRoleService.class);

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public int getTotalCount(JSONObject map) {
        int resCount = 0;
        Integer totalCount = jobMenuRoleDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }


    /**
     * save(保存角色菜单关系)
     * @param jobMenuRole
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JobMenuRole save(JobMenuRole jobMenuRole) throws  Exception {
        JobMenuRole ret = null;
        if (jobMenuRole == null) {
            return ret;
        }
        Long roleId = jobMenuRole.getId();
        long id=0;
        if(roleId!=null)
        {
            id=roleId.longValue();
        }
        if (id > 0) {
            updateById(jobMenuRole);
            ret = jobMenuRole;
        } else {
            jobMenuRole.setId(null);
            jobMenuRoleDao.add(jobMenuRole);

            ret = jobMenuRole;
        }
        return ret;
    }

    /**
     * addTrainRecordBatch(批量新增角色菜单关系数据)
     * @param jobMenuRole
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addTrainRecordBatch(List<JobMenuRole> list) throws  Exception
    {
        jobMenuRoleDao.addTrainRecordBatch(list);
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public JobMenuRole getJobMenuRoleById(long id) {
        return jobMenuRoleDao.getJobMenuRoleById(id);
    }

    /**
     * 获取单条数据
     *
     * @param jobMenuRole
     * @return
     * @author rufei.cn
     */
    public JobMenuRole getSignleJobMenuRole(JobMenuRole jobMenuRole) {
        return jobMenuRoleDao.getSignleJobMenuRole(jobMenuRole);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(JobMenuRole jobMenuRole) {
        jobMenuRoleDao.updateById(jobMenuRole);
    }

}