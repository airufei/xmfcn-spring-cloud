package com.cn.xmf.job.admin.prize.rpc;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.UserPrize;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * UserPrizeService(奖品信息)
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
@SuppressWarnings("all")
@FeignClient(value = "${base-service.wechat-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/prize/")// 配置远程服务路径

public interface UserPrizeService {

    /**
     * list:(查询奖品信息 带分页数据)
     *
     * @return
     * @Author rufei.cn
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);


    /**
     * getUserPrizeList:(查询奖品信息 不带分页数据)
     *
     * @return
     * @Author rufei.cn
     */
    @RequestMapping(value = "getUserPrizeList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<UserPrize> getUserPrizeList(@RequestBody UserPrize userPrize);


    /**
     * getUserPrize:(查询奖品信息单个实体数据)
     *
     * @return
     * @Author rufei.cn
     */
    @RequestMapping(value = "getUserPrize", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserPrize getUserPrize(@RequestBody UserPrize userPrize);

    /**
     * save:(保存奖品信息数据)
     *
     * @return
     * @Author rufei.cn
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserPrize save(@RequestBody UserPrize userPrize);


    /**
     * delete:(删除奖品信息数据)
     *
     * @return
     * @Author rufei.cn
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);

}