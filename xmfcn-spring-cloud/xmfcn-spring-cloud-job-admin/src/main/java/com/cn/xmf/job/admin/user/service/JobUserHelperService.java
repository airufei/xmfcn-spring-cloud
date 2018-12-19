package com.cn.xmf.job.admin.user.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCode;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.job.admin.core.util.I18nUtil;
import com.cn.xmf.job.admin.user.dao.JobUserDao;
import com.cn.xmf.model.user.JobUser;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service(调度系统用户)
 *
 * @author airufei
 * @version 2018-09-18
 */
@Service
@SuppressWarnings("all")
public class JobUserHelperService {

    @Autowired
    private JobUserDao jobUserDao;
    private static Logger logger = LoggerFactory.getLogger(JobUserService.class);

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public int getTotalCount(JSONObject map) {
        int resCount = 0;
        Integer totalCount = jobUserDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }


    /*
     * save(保存调度系统用户)
     * @param jobUser
     * @author airufei
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JobUser save(JobUser jobUser) {
        if (jobUser == null) {
            return jobUser;
        }
        if (jobUser.getId() != null && jobUser.getId() > 0) {
            updateById(jobUser);
        } else {
            jobUserDao.add(jobUser);
        }
        return jobUser;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public JobUser getJobUserById(long id) {
        return jobUserDao.getJobUserById(id);
    }

    /**
     * 获取单条数据
     *
     * @param jobUser
     * @return
     * @author airufei
     */
    public JobUser getSignleJobUser(JobUser jobUser) {
        return jobUserDao.getSignleJobUser(jobUser);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(JobUser jobUser) {
        jobUserDao.updateById(jobUser);
    }


    /**
     * login(登录)
     *
     * @param username
     * @param password
     * @return
     */
    public RetData login(String phone, String password) {
        RetData retData = new RetData();
        if (StringUtil.isBlank(phone)) {
            retData.setMessage(I18nUtil.getString("login_param_empty"));
            return retData;
        }
        if (StringUtil.isBlank(password)) {
            retData.setMessage(I18nUtil.getString("login_param_empty"));
            return retData;
        }
        if (!StringUtil.isPhone(phone)) {
            retData.setMessage("手机号不正确");
            return retData;
        }
        int length = password.length();
        if (length != -1 && (length < 6 || length > 20)) {
            retData.setMessage("密码长度6-20位");
            return retData;
        }
        try {
            password = StringUtil.getEncryptPassword(password);
            JobUser jobUser = new JobUser();
            jobUser.setPhone(phone);
            jobUser.setPassword(password);
            JobUser u = getSignleJobUser(jobUser);
            if (u != null && u.getId() > 0) {
                retData.setCode(RetCode.SUCCESS);
                retData.setData(u);
                retData.setMessage("成功");
            } else {
                retData.setCode(RetCode.SYS_ERROR);
                retData.setMessage("账户或者密码不正确");
            }
        } catch (Exception e) {
            String msg = "login(登录) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            e.printStackTrace();
        }
        return retData;
    }

}