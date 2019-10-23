package com.cn.xmf.api.user.rpc;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * Service(微信用户信息)
 * @author rufei.cn
 * @version 2018-09-16
 */
@SuppressWarnings("all")
@FeignClient(value = "${base-service.wechat-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/wx/user/")// 配置远程服务路径
public interface UserService  {

    /**
     * list:(查询用户信息 带分页数据)
     * @author rufei.cn
     * @return
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);


    /**
     *
     * 根据openId获取用户
     * @param openId
     * @return
     */
    @RequestMapping(value = "getWxUserByOpenId", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User getWxUserByOpenId(String openId);

    /**
     * save:(保存用户信息数据)
     * @author rufei.cn
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    User save(@RequestBody User user);


    /**
     * delete:(删除用户信息数据)
     * @author rufei.cn
     * @return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);
	
}