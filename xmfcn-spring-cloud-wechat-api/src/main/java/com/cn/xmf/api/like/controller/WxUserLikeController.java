package com.cn.xmf.api.like.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.like.service.WxUserLikeService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.WxUserLike;
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
public class WxUserLikeController {

    private static Logger logger = LoggerFactory.getLogger(WxUserLikeController.class);

    @Autowired
    private WxUserLikeService wxUserLikeService;

    /**
     * getList:(获取微信点赞分页查询接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("getList")
    public RetData getList(HttpServletRequest request) {
        RetData retData = new RetData();
        String pageNoStr = request.getParameter("pageNo");
        String length = request.getParameter("pageSize");
        String openid = request.getParameter("openid");
        String type = request.getParameter("type");
        String bizid = request.getParameter("bizid");
        int pageSize = 10;
        int pageNo = 1;
        if (StringUtil.isNotBlank(pageNoStr)) {
            pageNo = StringUtil.stringToInt(pageNoStr);
        }
        if (StringUtil.isNotBlank(length)) {
            pageSize = StringUtil.stringToInt(length);
        }
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        param.put("openid", openid);
        param.put("type", type);
        param.put("bizid", bizid);
        logger.info("getList:(获取微信点赞分页查询接口) 开始  param={}", param);
        Partion pt = wxUserLikeService.getList(param);
        List<WxUserLike> list = null;
        int totalCount = 0;
        if (pt != null) {
            list = (List<WxUserLike>) pt.getList();
            totalCount = pt.getPageCount();
        }
        if (list == null || list.size() <= 0) {
            retData.setData(list);
            retData.setCode(ResultCodeMessage.NO_DATA);
            retData.setMessage(ResultCodeMessage.NO_DATA_MESSAGE);
            return retData;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        jsonObject.put("totalCount", totalCount);
        retData.setData(jsonObject);
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
    @RequestMapping("getWxUserLike")
    public RetData getWxUserLike(HttpServletRequest request) {
        RetData retData = new RetData();
        WxUserLike wxUserLike = new WxUserLike();
        String openid = request.getParameter("openid");
        String type = request.getParameter("type");
        String bizid = request.getParameter("bizid");
        wxUserLike.setOpenid(openid);
        wxUserLike.setType(type);
        wxUserLike.setBizid(bizid);
        logger.info("getWxUserLike:(查询微信点赞单条数据接口) 开始  wxUserLike={}", wxUserLike);
        WxUserLike retwxUserLike = wxUserLikeService.getWxUserLike(wxUserLike);
        retData.setData(retwxUserLike);
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
    @RequestMapping(value = "save")
    public RetData save(HttpServletRequest request) {
        RetData retData = new RetData();
        WxUserLike wxUserLike = new WxUserLike();
        String openid = request.getParameter("openid");
        String type = request.getParameter("type");
        String photourl = request.getParameter("photourl");
        String remark = request.getParameter("remark");
        String nickname = request.getParameter("nickname");
        String bizid = request.getParameter("bizid");
        wxUserLike.setOpenid(openid);
        wxUserLike.setType(type);
        wxUserLike.setPhotourl(photourl);
        wxUserLike.setRemark(remark);
        wxUserLike.setNickname(nickname);
        wxUserLike.setBizid(bizid);
        logger.info("save:(保存微信点赞数据接口) 开始  wxUserLike={}", wxUserLike);
        wxUserLike.setCreateTime(new Date());
        wxUserLike.setUpdateTime(new Date());
        // 保存数据库
        WxUserLike ret = wxUserLikeService.save(wxUserLike);
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
    @RequestMapping("delete")
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
            wxUserLikeService.delete(id);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        } else {
            retData.setMessage("请选择需要删除的数据");
        }
        logger.info("delete:(逻辑删除微信点赞数据接口) 结束");
        return retData;
    }

}