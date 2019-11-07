package com.cn.xmf.api.meet.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.meet.rpc.MeetingService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.Meeting;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * MeetingController(参会人员信息登记)
 * Controller 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufen.cn
 * @version 2019-10-26
 */
@RestController
@RequestMapping("/meet/")
@SuppressWarnings("all")
public class MeetingController {

    private static Logger logger = LoggerFactory.getLogger(MeetingController.class);

    @Autowired
    private MeetingService meetingService;

    /**
     * getList:(获取参会人员信息登记分页查询接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufen.cn
     */
    @RequestMapping("getList")
    public RetData getList(HttpServletRequest request) {
        RetData retData = new RetData();
        String pageNoStr = request.getParameter("pageNo");
        String length = request.getParameter("pageSize");
        String userName = request.getParameter("userName");
        String flag = request.getParameter("flag");
        String phone = request.getParameter("phone");
        int pageSize = 10;
        int pageNo = 1;
        if (StringUtil.isNotBlank(pageNoStr)) {
            pageNo = StringUtil.stringToInt(pageNoStr);
        }
        if (StringUtil.isNotBlank(length)) {
            pageSize = StringUtil.stringToInt(length);
        }
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        logger.info("getList:(获取参会人员信息登记分页查询接口) 开始  param={}", param);
        param.put("userName", userName);
        param.put("flag", flag);
        param.put("phone", phone);
        Partion pt = meetingService.getList(param);
        List<Meeting> list = null;
        int totalCount = 0;
        if (pt != null) {
            list = (List<Meeting>) pt.getList();
            totalCount = pt.getTotalCount();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        jsonObject.put("totalCount", totalCount);
        if (list == null || list.size() <= 0) {
            retData.setData(jsonObject);
            retData.setCode(ResultCodeMessage.NO_DATA);
            retData.setMessage(ResultCodeMessage.NO_DATA_MESSAGE);
            return retData;
        }
        retData.setData(jsonObject);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getList:(获取参会人员信息登记分页查询接口) 结束");
        return retData;
    }

    /**
     * getMeeting:(查询参会人员信息登记单条数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufen.cn
     */
    @RequestMapping("getMeeting")
    public RetData getMeeting(HttpServletRequest request) {
        RetData retData = new RetData();
        Meeting meeting = new Meeting();
        String openId = request.getParameter("openId");
        if(StringUtil.isBlank(openId)||"undefined".equals(openId))
        {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("未授权，请退出再试");
            return retData;
        }
        String userName = request.getParameter("userName");
        String phone = request.getParameter("phone");
        meeting.setUserName(userName);
        meeting.setPhone(phone);
        meeting.setOpenId(openId);
        logger.info("getMeeting:(查询参会人员信息登记单条数据接口) 开始  meeting={}", meeting);
        Meeting retmeeting = meetingService.getMeeting(meeting);
        retData.setData(retmeeting);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getMeeting:(查询参会人员信息登记单条数据接口) 结束");
        return retData;
    }

    /**
     * save:(保存参会人员信息登记数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufen.cn
     */
    @RequestMapping(value = "save")
    public RetData save(HttpServletRequest request) {
        RetData retData = new RetData();
        Meeting meeting = new Meeting();
        String userName = request.getParameter("userName");
        String remark = request.getParameter("remark");
        String phone = request.getParameter("phone");
        String numStr = request.getParameter("num");
        String typeStr = request.getParameter("type");
        String openId = request.getParameter("openId");
        String nickName = request.getParameter("nickName");
        String photoUrl = request.getParameter("photoUrl");
        int num=StringUtil.stringToInt(numStr);
        int type=StringUtil.stringToInt(typeStr);
        meeting.setUserName(userName);
        meeting.setPhone(phone);
        meeting.setNum(num);
        meeting.setType(type);
        meeting.setOpenId(openId);
        meeting.setNickName(nickName);
        meeting.setPhotoUrl(photoUrl);
        logger.info("save:(保存参会人员信息登记数据接口) 开始  meeting={}", meeting);
        if(StringUtil.isBlank(openId)||"undefined".equals(openId))
        {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("未授权，请退出再试");
            return retData;
        }
        if(!StringUtil.isMobilePhone(phone))
        {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("手机输入有误，请重新输入");
            return retData;
        }
        if(num<=0)
        {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("人数输入有误，请重新输入");
            return retData;
        }
        if(!StringUtil.isChinese(userName))
        {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("姓名输入有误，请重新输入");
            return retData;
        }
        if(StringUtil.isNotBlank(remark)&&remark.length()>200)
        {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("备注信息输入不能超过200字，请检查");
            return retData;
        }
        remark=StringUtil.stringFilter(remark);
        meeting.setRemark(remark);
        meeting.setCreateTime(new Date());
        meeting.setUpdateTime(new Date());
        // 保存数据库
        Meeting ret = meetingService.save(meeting);
        if (ret != null) {
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        }
        logger.info("save:(保存参会人员信息登记数据接口) 结束");
        return retData;
    }
}