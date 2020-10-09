package com.cn.xmf.job.admin.photo.rpc;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Photo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Service(微信照片)
 * @author rufei
 * @version 2019-10-11
 */
@SuppressWarnings("all")
@FeignClient(value = "${base-service.wechat-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/photo/")// 配置远程服务路径

public interface PhotoService {

    /**
     * list:(查询微信照片 带分页数据)
     * @author rufei
     * @return
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);

    /**
     * getWxPhotoList:(查询微信照片 不带分页数据)
     * @author rufei
     * @return
     */
    @RequestMapping(value = "getWxPhotoList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Photo> getWxPhotoList(@RequestBody Photo photo);

     /**
     * getWxPhoto:(查询微信照片单个实体数据)
     * @author rufei
     * @return
     */
    @RequestMapping(value = "getWxPhoto", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Photo getWxPhoto(@RequestBody Photo photo);

    /**
     * save:(保存微信照片数据)
     * @author rufei
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    Photo save(@RequestBody Photo photo);

    /**
     * delete:(删除微信照片数据)
     * @author rufei
     * @return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);
	
}