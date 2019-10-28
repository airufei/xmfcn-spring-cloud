package com.cn.xmf.service.meet.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Meeting;
import com.cn.xmf.service.meet.dao.MeetingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Meeting Service(参会人员信息登记)
 * service 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufen.cn
 * @version 2019-10-26
 */
@RestController
@RequestMapping(value = "/server/meet/")
@SuppressWarnings("all")
public class MeetingService {

    private static Logger logger = LoggerFactory.getLogger(MeetingService.class);

    @Autowired
    private MeetingDao meetingDao;
    @Autowired
    private MeetingHelperService meetingHelperService;

    /**
     * getList(获取参会人员信息登记带分页数据-服务)
     *
     * @param json
     * @return
     * @author rufen.cn
     */
    @RequestMapping("getList")
    public Partion getList(@RequestBody JSONObject json) {
        logger.info("getList(获取参会人员信息登记带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        int totalcount = meetingHelperService.getTotalCount(json);
        List<Meeting> list = null;
        if (totalcount > 0) {
            list = meetingDao.getList(json);
        }
        Partion pt = new Partion(json, list, totalcount);
        logger.info("getList(获取参会人员信息登记带分页数据-服务) 结束 ");
        return pt;
    }

    /**
     * getMeetingList(获取参会人员信息登记 不带分页数据-服务)
     *
     * @param meeting
     * @return
     * @author rufen.cn
     */
    @RequestMapping("getMeetingList")
    public List<Meeting> getMeetingList(@RequestBody Meeting meeting) {
        String parms = JSON.toJSONString(meeting);
        List<Meeting> list = null;
        logger.info("getMeetingList(获取参会人员信息登记 不带分页数据-服务) 开始 parms={}", parms);
        if (meeting == null) {
            return list;
        }
        list = meetingDao.getMeetingList(meeting);
        logger.info("getMeetingList(获取参会人员信息登记 不带分页数据-服务) 结束");
        return list;
    }


    /**
     * save (保存参会人员信息登记 数据-服务)
     *
     * @param meeting
     * @return
     * @author rufen.cn
     */
    @RequestMapping("save")
    public Meeting save(@RequestBody Meeting meeting) throws Exception {
        String parms = JSON.toJSONString(meeting);
        logger.info("save (保存参会人员信息登记 数据-服务) 开始 parms={}", parms);
        Meeting ret = null;
        if (meeting == null) {
            return ret;
        }
        ret = meetingHelperService.save(meeting);
        logger.info("save (保存参会人员信息登记 数据-服务) 结束");
        return ret;
    }


    /**
     * getMeeting(获取参会人员信息登记单条数据-服务)
     *
     * @param meeting
     * @return
     * @author rufen.cn
     */
    @RequestMapping("getMeeting")
    public Meeting getMeeting(@RequestBody Meeting meeting) {
        Meeting ret = null;
        String parms = JSON.toJSONString(meeting);
        logger.info("getMeeting(获取参会人员信息登记单条数据-服务) 开始 parms={}", parms);
        if (meeting == null) {
            return ret;
        }
        ret = meetingHelperService.getSignleMeeting(meeting);
        logger.info("getMeeting(获取参会人员信息登记单条数据-服务) 结束 ");
        return ret;
    }

    /**
     * delete(逻辑删除参会人员信息登记数据-服务)
     *
     * @param id
     * @return
     * @author rufen.cn
     */
    @RequestMapping("delete")
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除参会人员信息登记数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        Meeting dt = meetingHelperService.getMeetingById(id);
        if (dt == null) {
            return isSuccess;
        }
        meetingDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除参会人员信息登记数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }
}