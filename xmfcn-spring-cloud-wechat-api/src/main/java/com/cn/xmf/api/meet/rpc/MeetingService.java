package com.cn.xmf.api.meet.rpc;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Meeting;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * MeetingService(参会人员信息登记)
 *
 * @author rufen.cn
 * @version 2019-10-26
 */
@SuppressWarnings("all")
@FeignClient(value = "${base-service.wechat-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/meet/")// 配置远程服务路径

public interface MeetingService {

    /**
     * list:(查询参会人员信息登记 带分页数据)
     *
     * @return
     * @Author rufen.cn
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);


    /**
     * getMeetingList:(查询参会人员信息登记 不带分页数据)
     *
     * @return
     * @Author rufen.cn
     */
    @RequestMapping(value = "getMeetingList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Meeting> getMeetingList(@RequestBody Meeting meeting);


    /**
     * getMeeting:(查询参会人员信息登记单个实体数据)
     *
     * @return
     * @Author rufen.cn
     */
    @RequestMapping(value = "getMeeting", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Meeting getMeeting(@RequestBody Meeting meeting);

    /**
     * save:(保存参会人员信息登记数据)
     *
     * @return
     * @Author rufen.cn
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    Meeting save(@RequestBody Meeting meeting);


    /**
     * delete:(删除参会人员信息登记数据)
     *
     * @return
     * @Author rufen.cn
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);

}