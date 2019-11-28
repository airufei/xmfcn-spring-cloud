package com.cn.xmf.api.like.rpc;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Like;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * Service(微信点赞)
 * @author rufei.cn
 * @version 2019-10-15
 */
@SuppressWarnings("all")
@FeignClient(value = "${base-service.wechat-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/like/")// 配置远程服务路径

public interface LikeService {

    /**
     * list:(查询微信点赞 带分页数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);

    /**
     * getWxUserLikeList:(查询微信点赞 不带分页数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getWxUserLikeList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Like> getWxUserLikeList(@RequestBody Like like);

     /**
     * getWxUserLike:(查询微信点赞单个实体数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getWxUserLike", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Like getWxUserLike(@RequestBody Like like);

    /**
     * save:(保存微信点赞数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    Like save(@RequestBody Like like);


    /**
     * delete:(删除微信点赞数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);

    /**
     * 获取单条数据
     *
     * @param bizId
     * @param type
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getPhotoLikeCount")
    public long getPhotoLikeCount(@RequestParam("bizId") String bizId, @RequestParam("type") String type);
	
}