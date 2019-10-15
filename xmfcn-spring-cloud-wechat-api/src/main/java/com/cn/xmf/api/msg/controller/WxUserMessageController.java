package com.cn.xmf.api.msg.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.msg.service.WxUserMessageService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.WxUserMessage;
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
 * WxUserMessageController(微信留言)
 * Controller 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 * @author rufei.cn
 * @version 2019-10-15
 */
@RestController
@RequestMapping("/msg")
@SuppressWarnings("all")
public class WxUserMessageController {

    private static Logger logger = LoggerFactory.getLogger(WxUserMessageController.class);

    @Autowired
    private WxUserMessageService wxUserMessageService;

    /**
     * getList:(获取微信留言分页查询接口)
     * @Author rufei.cn
     * @param request
     * @param parms
     * @return
     */
    @RequestMapping("getList")
    public RetData getList(HttpServletRequest request){
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
        param.put("openid",openid);
        param.put("type",type);
        param.put("bizid",bizid);
        logger.info("getList:(获取微信留言分页查询接口) 开始  param={}", param);
        Partion pt = wxUserMessageService.getList(param);
        List<WxUserMessage> list = null;
        int totalCount = 0;
        if (pt != null) {
            list = (List<WxUserMessage>) pt.getList();
            totalCount = pt.getPageCount();
        }
        if (list == null||list.size()<=0) {
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
        logger.info("getList:(获取微信留言分页查询接口) 结束");
        return retData;
    }

    /**
     * getWxUserMessage:(查询微信留言单条数据接口)
     * @Author rufei.cn
     * @param request
     * @param parms
     * @return
     */
    @RequestMapping("getWxUserMessage")
    public RetData getWxUserMessage(HttpServletRequest request) {
        RetData retData = new RetData();
        WxUserMessage wxUserMessage= new WxUserMessage();
        String openid = request.getParameter("openid");
        String type = request.getParameter("type");
        String bizid = request.getParameter("bizid");
        wxUserMessage.setOpenid(openid);
        wxUserMessage.setType(type);
        wxUserMessage.setBizid(bizid);
        logger.info("getWxUserMessage:(查询微信留言单条数据接口) 开始  wxUserMessage={}", wxUserMessage);
        WxUserMessage retwxUserMessage= wxUserMessageService.getWxUserMessage(wxUserMessage);
        retData.setData(retwxUserMessage);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getWxUserMessage:(查询微信留言单条数据接口) 结束");
        return retData;
    }

    /**
     * save:(保存微信留言数据接口)
     * @Author rufei.cn
     * @param request
     * @param parms
     * @return
     */
    @RequestMapping(value = "save")
    public RetData save(HttpServletRequest request) {
        RetData retData = new RetData();
        WxUserMessage wxUserMessage= new WxUserMessage();
        String openid = request.getParameter("openid");
        String type = request.getParameter("type");
        String content = request.getParameter("content");
        String photourl = request.getParameter("photourl");
        String remark = request.getParameter("remark");
        String nickname = request.getParameter("nickname");
        String bizid = request.getParameter("bizid");
        wxUserMessage.setOpenid(openid);
        wxUserMessage.setType(type);
        wxUserMessage.setContent(content);
        wxUserMessage.setPhotourl(photourl);
        wxUserMessage.setRemark(remark);
        wxUserMessage.setNickname(nickname);
        wxUserMessage.setBizid(bizid);
        logger.info("save:(保存微信留言数据接口) 开始  wxUserMessage={}", wxUserMessage);
        wxUserMessage.setCreateTime(new Date());
        wxUserMessage.setUpdateTime(new Date());
        // 保存数据库
        WxUserMessage ret =wxUserMessageService.save(wxUserMessage);
        if(ret!=null)
        {
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        }
        logger.info("save:(保存微信留言数据接口) 结束");
        return retData;
    }

    /**
     * delete:(逻辑删除微信留言数据接口)
     * @Author rufei.cn
     * @param request
     * @param parms
     * @return
     */
    @RequestMapping("delete")
    public RetData delete(HttpServletRequest request){
        RetData retData = new RetData();
        String idStr = request.getParameter("id");
        logger.info("delete:(逻辑删除微信留言数据接口) 开始  idStr={}", idStr);
        if (StringUtil.isBlank(idStr)) {
            retData.setMessage("参数为空");
            return retData;
        }
        Long id = StringUtil.stringToLong(idStr);
        if (id != null && id > 0) {
            wxUserMessageService.delete(id);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        } else {
            retData.setMessage("请选择需要删除的数据");
        }
        logger.info("delete:(逻辑删除微信留言数据接口) 结束");
        return retData;
    }

}