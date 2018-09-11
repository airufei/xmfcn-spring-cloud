package com.cn.xmf.api.user.service;

import java.util.List;
import java.util.Map;

import com.cn.xmf.base.model.*;
import com.cn.xmf.model.user.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSONObject;

/**
 * Service(用户信息)
 *
 * @author airufei
 * @version 2018-09-11
 */
@SuppressWarnings("all")

@FeignClient(value = "${base-service.user-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/user/")// 配置远程服务路径

public interface UserService {

    /**
     * list:(查询用户信息 带分页数据)
     *
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);


    /**
     * getUserList:(查询用户信息 不带分页数据)
     *
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "getUserList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<User> getUserList(@RequestBody User user);


    /**
     * getUser:(查询用户信息单个实体数据)
     *
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "getUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@RequestBody User user);

    /**
     * save:(保存用户信息数据)
     *
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    User save(@RequestBody User user);


    /**
     * delete:(删除用户信息数据)
     *
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);

}