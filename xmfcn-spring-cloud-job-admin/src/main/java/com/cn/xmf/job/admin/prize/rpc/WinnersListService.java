package com.cn.xmf.job.admin.prize.rpc;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.WinnersList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * WinnersListService(获奖名单)
 * @author rufei.cn
 * @version 2020-01-02
 */
@SuppressWarnings("all")
@FeignClient(value = "${base-service.wechat-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/prizelist/")// 配置远程服务路径

public interface WinnersListService  {

    /**
     * list:(查询获奖名单 带分页数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);


    /**
     * getWinnersListList:(查询获奖名单 不带分页数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getWinnersListList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<WinnersList> getWinnersListList(@RequestBody WinnersList winnersList);


     /**
     * getWinnersList:(查询获奖名单单个实体数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getWinnersList", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WinnersList getWinnersList(@RequestBody WinnersList winnersList);

    /**
     * save:(保存获奖名单数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    WinnersList save(@RequestBody WinnersList winnersList);


    /**
     * delete:(删除获奖名单数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);
	
}