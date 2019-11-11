package com.cn.xmf.api.like.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.common.SysCommonService;
import com.cn.xmf.api.like.rpc.LikeService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.Like;
import com.cn.xmf.util.LocalCacheUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * WxUserLikeController(微信点赞)
 * Controller 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
@RestController
@RequestMapping(value = "/like")
@SuppressWarnings("all")
public class LikeController {

    private static Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    private LikeService likeService;
    @Autowired
    private SysCommonService sysCommonService;


    /**
     * save:(保存微信点赞数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping(value = "/save")
    public RetData save(HttpServletRequest request) {
        RetData retData = new RetData();
        Like like = new Like();
        String openId = request.getParameter("openId");
        String type = request.getParameter("type");
        String photoUrl = request.getParameter("photoUrl");
        String remark = request.getParameter("remark");
        String nickName = request.getParameter("nickName");
        String bizId = request.getParameter("bizId");
        String likeCountStr = request.getParameter("likeCount");
        long likeCount = StringUtil.stringToLong(likeCountStr);
        if (StringUtil.isBlank(bizId)) {
            bizId = StringUtil.getUuId();
        }
        if (StringUtil.isBlank(type)) {
            type = "common_like";
        }
        if (StringUtil.isBlank(openId) || "undefined".equals(openId)) {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("不好意思，请先登录");
            return retData;
        }
        String key = "like_cache_" + openId + bizId;
        String cache = sysCommonService.getCache(key);
        if (StringUtil.isNotBlank(cache)) {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("这张已赞过，你可以点赞下一张，谢谢.");
            return retData;
        }
        like.setOpenId(openId);
        like.setType(type);
        like.setPhotoUrl(photoUrl);
        like.setRemark(remark);
        like.setNickName(nickName);
        like.setBizId(bizId);
        like.setLikeCount(likeCount);
        logger.info("save:(保存微信点赞数据接口) 开始  like={}", like);
        like.setCreateTime(new Date());
        like.setUpdateTime(new Date());
        // 保存数据库
        Like ret = likeService.save(like);
        if (ret != null) {
            sysCommonService.save(key, "has_like", 60 * 30);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        }
        logger.info("save:(保存微信点赞数据接口) 结束");
        return retData;
    }

}