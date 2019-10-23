package com.cn.xmf.api.like.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.like.rpc.LikeService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.Like;
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

    /**
     * getList:(获取微信点赞分页查询接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("/getList")
    public RetData getList(HttpServletRequest request) {
        RetData retData = new RetData();
        String pageNoStr = request.getParameter("pageNo");
        String length = request.getParameter("pageSize");
        String openId = request.getParameter("openId");
        String type = request.getParameter("type");
        String bizId = request.getParameter("bizId");
        int pageSize = 10;
        int pageNo = 1;
        if (StringUtil.isNotBlank(pageNoStr)) {
            pageNo = StringUtil.stringToInt(pageNoStr);
        }
        if (StringUtil.isNotBlank(length)) {
            pageSize = StringUtil.stringToInt(length);
        }
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        param.put("openId", openId);
        param.put("type", type);
        param.put("bizId", bizId);
        logger.info("getList:(获取微信点赞分页查询接口) 开始  param={}", param);
        Partion pt = likeService.getList(param);
        List<Like> list = null;
        long totalCount = 0;
        if (pt != null) {
            list = (List<Like>) pt.getList();
            totalCount = pt.getTotalCount();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        jsonObject.put("totalCount", totalCount);
        retData.setData(jsonObject);
        if (list == null||list.size()<=0) {
            retData.setCode(ResultCodeMessage.NO_DATA);
            retData.setMessage(ResultCodeMessage.NO_DATA_MESSAGE);
            return retData;
        }
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getList:(获取微信点赞分页查询接口) 结束");
        return retData;
    }

    /**
     * getWxUserLike:(查询微信点赞单条数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("/getWxUserLike")
    public RetData getWxUserLike(HttpServletRequest request) {
        RetData retData = new RetData();
        Like like = new Like();
        String openId = request.getParameter("openId");
        String type = request.getParameter("type");
        String bizId = request.getParameter("bizId");
        like.setOpenId(openId);
        like.setType(type);
        like.setBizId(bizId);
        logger.info("getWxUserLike:(查询微信点赞单条数据接口) 开始  like={}", like);
        Like retlike = likeService.getWxUserLike(like);
        retData.setData(retlike);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getWxUserLike:(查询微信点赞单条数据接口) 结束");
        return retData;
    }

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
        long likeCount=StringUtil.stringToLong(likeCountStr);
        if(StringUtil.isBlank(bizId))
        {
            bizId=StringUtil.getUuId();
        }
        if(StringUtil.isBlank(type))
        {
            type="common_like";
        }
        if(StringUtil.isBlank(openId))
        {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("不好意思，请先登录");
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
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        }
        logger.info("save:(保存微信点赞数据接口) 结束");
        return retData;
    }

    /**
     * delete:(逻辑删除微信点赞数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("/delete")
    public RetData delete(HttpServletRequest request) {
        RetData retData = new RetData();
        String idStr = request.getParameter("id");
        logger.info("delete:(逻辑删除微信点赞数据接口) 开始  idStr={}", idStr);
        if (StringUtil.isBlank(idStr)) {
            retData.setMessage("参数为空");
            return retData;
        }
        Long id = StringUtil.stringToLong(idStr);
        if (id != null && id > 0) {
            likeService.delete(id);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        } else {
            retData.setMessage("请选择需要删除的数据");
        }
        logger.info("delete:(逻辑删除微信点赞数据接口) 结束");
        return retData;
    }

}