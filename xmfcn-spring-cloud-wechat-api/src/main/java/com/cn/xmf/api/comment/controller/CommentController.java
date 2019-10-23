package com.cn.xmf.api.comment.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.comment.rpc.CommentService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.Comment;
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
@RequestMapping(value = "/msg")
@SuppressWarnings("all")
public class CommentController {

    private static Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

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
        param.put("openId",openId);
        param.put("type",type);
        param.put("bizId",bizId);
        logger.info("getList:(获取微信留言分页查询接口) 开始  param={}", param);
        Partion pt = commentService.getList(param);
        List<Comment> list = null;
        long totalCount = 0;
        if (pt != null) {
            list = (List<Comment>) pt.getList();
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
        Comment comment= new Comment();
        String openId = request.getParameter("openId");
        String type = request.getParameter("type");
        String bizId = request.getParameter("bizId");
        comment.setOpenId(openId);
        comment.setType(type);
        comment.setBizId(bizId);
        logger.info("getWxUserMessage:(查询微信留言单条数据接口) 开始  comment={}", comment);
        Comment retcomment= commentService.getWxUserMessage(comment);
        retData.setData(retcomment);
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
        Comment comment= new Comment();
        String openId = request.getParameter("openId");
        String type = request.getParameter("type");
        String content = request.getParameter("content");
        String photoUrl = request.getParameter("photoUrl");
        String remark = request.getParameter("remark");
        String nickName = request.getParameter("nickName");
        String bizId = request.getParameter("bizId");
        if(StringUtil.isBlank(bizId))
        {
            bizId=StringUtil.getUuId();
        }
        if(StringUtil.isBlank(type))
        {
            type="common_comment";
        }
        if(StringUtil.isBlank(content))
        {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("不好意思，留言信息不能为空");
            return retData;
        }
        if(StringUtil.isBlank(openId))
        {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("不好意思，请先登录");
            return retData;
        }
        if(content.length()>120)
        {
            retData.setCode(ResultCodeMessage.PARMS_ERROR);
            retData.setMessage("太长了,可以简短一点，谢谢。");
            return retData;
        }
        comment.setOpenId(openId);
        comment.setType(type);
        comment.setContent(content);
        comment.setPhotoUrl(photoUrl);
        comment.setRemark(remark);
        comment.setNickName(nickName);
        comment.setBizId(bizId);
        logger.info("save:(保存微信留言数据接口) 开始  comment={}", comment);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        // 保存数据库
        Comment ret =commentService.save(comment);
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
            commentService.delete(id);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        } else {
            retData.setMessage("请选择需要删除的数据");
        }
        logger.info("delete:(逻辑删除微信留言数据接口) 结束");
        return retData;
    }

}