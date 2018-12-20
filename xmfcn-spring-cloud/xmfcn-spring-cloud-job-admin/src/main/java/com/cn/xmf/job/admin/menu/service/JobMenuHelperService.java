package com.cn.xmf.job.admin.menu.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.menu.dao.JobMenuDao;
import com.cn.xmf.job.admin.menu.model.JobMenu;
import com.cn.xmf.job.admin.menu.model.MenuNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service(job-菜单)
 *
 * @author airufei
 * @version 2018-10-10
 */
@Service
@SuppressWarnings("all")
public class JobMenuHelperService {

    @Autowired
    private JobMenuDao jobMenuDao;

    private static Logger logger = LoggerFactory.getLogger(JobMenuService.class);

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public int getTotalCount(JSONObject map) {
        int resCount = 0;
        Integer totalCount = jobMenuDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }


    /*
     * save(保存job-菜单)
     * @param jobMenu
     * @author airufei
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JobMenu save(JobMenu jobMenu) {
        if (jobMenu == null) {
            return jobMenu;
        }
        if (jobMenu.getId() != null && jobMenu.getId() > 0) {
            updateById(jobMenu);
        } else {
            jobMenuDao.add(jobMenu);
        }
        return jobMenu;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public JobMenu getJobMenuById(long id) {
        return jobMenuDao.getJobMenuById(id);
    }

    /**
     * 获取单条数据
     *
     * @param jobMenu
     * @return
     * @author airufei
     */
    public JobMenu getSignleJobMenu(JobMenu jobMenu) {
        return jobMenuDao.getSignleJobMenu(jobMenu);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(JobMenu jobMenu) {
        jobMenuDao.updateById(jobMenu);
    }



}