package com.cn.xmf.api.invit.rpc;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Invitation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * InvitationService(邀请函)
 * @author rufei.cn
 * @version 2019-10-26
 */
@SuppressWarnings("all")
@FeignClient(value = "${base-service.wechat-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/invit/")// 配置远程服务路径

public interface InvitationService  {

    /**
     * list:(查询邀请函 带分页数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);


    /**
     * getInvitationList:(查询邀请函 不带分页数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getInvitationList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Invitation> getInvitationList(@RequestBody Invitation invitation);


     /**
     * getInvitation:(查询邀请函单个实体数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getInvitation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Invitation getInvitation(@RequestBody Invitation invitation);

    /**
     * save:(保存邀请函数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    Invitation save(@RequestBody Invitation invitation);


    /**
     * delete:(删除邀请函数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);
	
}