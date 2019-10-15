package com.cn.xmf.api.msg.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.WxUserMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * Service(微信留言)
 * @author rufei.cn
 * @version 2019-10-15
 */
@SuppressWarnings("all")
@FeignClient(value = "${base-service.base-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/msg/")// 配置远程服务路径

public interface WxUserMessageService  {

    /**
     * list:(查询微信留言 带分页数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);

    /**
     * getWxUserMessageList:(查询微信留言 不带分页数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getWxUserMessageList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<WxUserMessage> getWxUserMessageList(@RequestBody WxUserMessage wxUserMessage);

     /**
     * getWxUserMessage:(查询微信留言单个实体数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getWxUserMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WxUserMessage getWxUserMessage(@RequestBody WxUserMessage wxUserMessage);

    /**
     * save:(保存微信留言数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    WxUserMessage save(@RequestBody WxUserMessage wxUserMessage);


    /**
     * delete:(删除微信留言数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);
	
}