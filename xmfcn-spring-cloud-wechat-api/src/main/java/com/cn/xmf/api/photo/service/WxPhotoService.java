package com.cn.xmf.api.photo.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.WxPhoto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * Service(微信照片)
 * @author airufei
 * @version 2019-10-11
 */
@SuppressWarnings("all")
@FeignClient(value = "${base-service.wechat-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/photo/")// 配置远程服务路径
public interface WxPhotoService  {

    /**
     * list:(查询微信照片 带分页数据)
     * @Author airufei
     * @return
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);

    /**
     * getWxPhotoList:(查询微信照片 不带分页数据)
     * @Author airufei
     * @return
     */
    @RequestMapping(value = "getWxPhotoList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<WxPhoto> getWxPhotoList(@RequestBody WxPhoto wxPhoto);

     /**
     * getWxPhoto:(查询微信照片单个实体数据)
     * @Author airufei
     * @return
     */
    @RequestMapping(value = "getWxPhoto", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WxPhoto getWxPhoto(@RequestBody WxPhoto wxPhoto);

    /**
     * save:(保存微信照片数据)
     * @Author airufei
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    WxPhoto save(@RequestBody WxPhoto wxPhoto);

    /**
     * delete:(删除微信照片数据)
     * @Author airufei
     * @return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);
	
}