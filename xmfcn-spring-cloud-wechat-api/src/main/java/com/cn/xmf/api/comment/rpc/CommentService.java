package com.cn.xmf.api.comment.rpc;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Comment;
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
@FeignClient(value = "${base-service.wechat-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/msg/")// 配置远程服务路径

public interface CommentService {

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
    List<Comment> getWxUserMessageList(@RequestBody Comment comment);

     /**
     * getWxUserMessage:(查询微信留言单个实体数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getWxUserMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Comment getWxUserMessage(@RequestBody Comment comment);

    /**
     * save:(保存微信留言数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    Comment save(@RequestBody Comment comment);


    /**
     * delete:(删除微信留言数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);
	
}