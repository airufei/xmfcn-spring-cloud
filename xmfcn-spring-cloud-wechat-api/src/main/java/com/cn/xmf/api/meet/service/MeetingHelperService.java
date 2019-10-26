package com.cn.xmf.api.meet.service;

import com.cn.xmf.api.meet.rpc.MeetingService;
import com.cn.xmf.model.wx.Meeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MeetingHelperService(参会人员信息登记辅助类)
 *
 * @author rufen.cn
 * @version 2019-10-26
 */
@Service
@SuppressWarnings("all")
public class MeetingHelperService {

    private static Logger logger = LoggerFactory.getLogger(MeetingHelperService.class);
    @Autowired
    private MeetingService meetingService;

    /**
     * 获取单条数据
     *
     * @param meeting
     * @return
     * @author rufen.cn
     */
    public Meeting getMeeting(Meeting meeting) {
        return meetingService.getMeeting(meeting);
    }

}