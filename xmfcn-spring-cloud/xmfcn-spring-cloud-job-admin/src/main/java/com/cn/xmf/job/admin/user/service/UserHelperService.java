package com.cn.xmf.job.admin.user.service;


import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCode;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.job.admin.core.util.I18nUtil;
import com.cn.xmf.model.user.User;
import com.cn.xmf.util.MD5Util;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHelperService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserService userService;

    /**
     * login(登录)
     * @param username
     * @param password
     * @return
     */
    public RetData login(String username, String password )
    {
        RetData retData=new RetData();
        if(StringUtil.isBlank(username))
        {
            retData.setMessage(I18nUtil.getString("login_param_empty"));
            return retData;
        }
        if(StringUtil.isBlank(password))
        {
            retData.setMessage(I18nUtil.getString("login_param_empty"));
            return retData;
        }
        try {
            password = MD5Util.getMD5(password);
            User user=new User();
            user.setUsername(username);
            user.setPassword(password);
            User u = userService.getUser(user);
            if(u!=null&&u.getId()>0)
            {
                retData.setCode(RetCode.SUCCESS);
                retData.setData(u);
                retData.setMessage("成功");
            }
        } catch (Exception e) {
            String msg = "login(登录) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            e.printStackTrace();
        }
        return retData;
    }
}
