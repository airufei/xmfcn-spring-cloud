package com.cn.xmf.job.admin.role.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.role.dao.JobRoleDao;
import com.cn.xmf.job.admin.role.model.JobMenuRole;
import com.cn.xmf.job.admin.role.model.JobRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private JobMenuRoleHelperService jobMenuRoleHelperService;

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
    public JobRole save(JobRole jobRole) throws Exception {
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
        boolean b = saveRoleAndMenu(ret);
        if (!b) {
            ret = null;
        }
        return ret;
    }

    /**
     * 保持角色和菜单关系
     *
     * @param jobRole
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveRoleAndMenu(JobRole jobRole) throws Exception {
        boolean ret = false;
        Long roleId = jobRole.getId();
        List<String> list = jobRole.getList();
        if (roleId == null || roleId <= 0) {
            return ret;
        }
        if (list == null || list.size() <= 0) {
            return ret;
        }
        int len = 0;
        len = list.size();
        List<JobMenuRole> menuRoleList = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            String s = list.get(i).replace("[", "").replace("]", "");
            JSONObject jsonObject = JSONObject.parseObject(s);
            if (jsonObject == null) {
                continue;
            }
            JobMenuRole jobMenuRole = new JobMenuRole();
            long id = jsonObject.getLongValue("id");
            jobMenuRole.setMenuId(id);
            jobMenuRole.setRoleId(roleId);
            jobMenuRole.setRoleCode(jobRole.getRoleCode());
            menuRoleList.add(jobMenuRole);
        }
        if (menuRoleList.size() > 0) {
            jobMenuRoleHelperService.addTrainRecordBatch(menuRoleList);
            ret = true;
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