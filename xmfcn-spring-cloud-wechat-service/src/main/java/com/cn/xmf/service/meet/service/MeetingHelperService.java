package com.cn.xmf.service.meet.service;

import java.util.List;
import java.util.Map;

import com.cn.xmf.service.meet.dao.MeetingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cn.xmf.model.wx.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.stereotype.Service;

/**
 * Meeting Service(参会人员信息登记辅助类)
 *
 * @author rufen.cn
 * @version 2019-10-26
 */
@Service
@SuppressWarnings("all")
public class MeetingHelperService {

    private static Logger logger = LoggerFactory.getLogger(MeetingHelperService.class);

    @Autowired
    private MeetingDao meetingDao;

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public int getTotalCount(JSONObject map) {
        int resCount = 0;
        Integer totalCount = meetingDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }


    /*
     * save(保存参会人员信息登记)
     * @param meeting
     * @author rufen.cn
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.REPEATABLE_READ, timeout = 30, rollbackFor = Exception.class)
    public Meeting save(Meeting meeting) throws Exception {
        Meeting ret = null;
        if (meeting == null) {
            return ret;
        }
        String openId = meeting.getOpenId();
        Meeting met=new Meeting();
        met.setOpenId(openId);
        Meeting signle = getSignleMeeting(met);
        if(signle!=null){
            meeting.setId(signle.getId());
        }
        if (meeting.getId() != null && meeting.getId() > 0) {
            updateById(meeting);
            ret = meeting;
        } else {
            meeting.setId(null);
            meetingDao.add(meeting);
            ret = meeting;
        }
        return ret;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public Meeting getMeetingById(long id) {
        return meetingDao.getMeetingById(id);
    }

    /**
     * 获取单条数据
     *
     * @param meeting
     * @return
     * @author rufen.cn
     */
    public Meeting getSignleMeeting(Meeting meeting) {
        return meetingDao.getSignleMeeting(meeting);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    public void updateById(Meeting meeting) {
        meetingDao.updateById(meeting);
    }

}