package com.cn.xmf.api.photo.controller;

import java.util.Date;
import java.util.List;

import com.cn.xmf.model.wx.WxPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.cn.xmf.base.model.*;
import com.cn.xmf.util.*;
import com.cn.xmf.api.photo.service.*;

/**
 * WxPhotoController(微信照片)
 * Controller 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author airufei
 * @version 2019-10-11
 */
@RestController
@SuppressWarnings("all")
public class WxPhotoController {

    private static Logger logger = LoggerFactory.getLogger(WxPhotoService.class);
    @Autowired
    private WxPhotoService wxPhotoService;

    /**
     * getList:(获取微信照片分页查询接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping("getList")
    public RetData getList(HttpServletRequest request, String parms) {
        RetData retData = new RetData();
        logger.info("getList:(获取微信照片分页查询接口) 开始  parms={}", parms);
        if (StringUtil.isBlank(parms)) {
            retData.setMessage("参数为空");
            return retData;
        }
        JSONObject json = JSONObject.parseObject(parms);
        if (json == null) {
            retData.setMessage("参数为空");
            return retData;
        }
        int pageNo = json.getIntValue("pageNo");
        int pageSize = json.getIntValue("pageSize");
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        param.putAll(json);
        Partion pt = wxPhotoService.getList(param);
        List<WxPhoto> list = null;
        int totalCount = 0;
        if (pt != null) {
            list = (List<WxPhoto>) pt.getList();
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
        logger.info("getList:(获取微信照片分页查询接口) 结束");
        return retData;
    }

    /**
     * getWxPhoto:(查询微信照片单条数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping("getWxPhoto")
    public RetData getWxPhoto(HttpServletRequest request, String parms) {
        RetData retData = new RetData();
        logger.info("getWxPhoto:(查询微信照片单条数据接口) 开始  parms={}", parms);
        if (StringUtil.isBlank(parms)) {
            retData.setMessage("参数为空");
            return retData;
        }
        JSONObject json = JSONObject.parseObject(parms);
        if (json == null) {
            retData.setMessage("参数为空");
            return retData;
        }
        WxPhoto wxPhoto = JSON.toJavaObject(json, WxPhoto.class);
        if (wxPhoto == null) {
            retData.setMessage("参数为空");
            return retData;
        }
        WxPhoto retwxPhoto = wxPhotoService.getWxPhoto(wxPhoto);
        retData.setData(retwxPhoto);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getWxPhoto:(查询微信照片单条数据接口) 结束");
        return retData;
    }

    /**
     * delete:(逻辑删除微信照片数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping("delete")
    public RetData delete(HttpServletRequest request, String parms) {
        RetData retData = new RetData();
        logger.info("delete:(逻辑删除微信照片数据接口) 开始  parms={}", parms);
        if (StringUtil.isBlank(parms)) {
            retData.setMessage("参数为空");
            return retData;
        }
        JSONObject json = JSONObject.parseObject(parms);
        if (json == null) {
            retData.setMessage("参数为空");
            return retData;
        }
        Long id = json.getLong("id");
        if (id != null && id > 0) {
            wxPhotoService.delete(id);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        } else {
            retData.setMessage("请选择需要删除的数据");
        }
        logger.info("delete:(逻辑删除微信照片数据接口) 结束");
        return retData;
    }

    /**
     * save:(保存微信照片数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping(value = "save")
    public RetData save(HttpServletRequest request, String parms) {
        RetData retData = new RetData();
        logger.info("save:(保存微信照片数据接口) 开始  parms={}", parms);
        if (StringUtil.isBlank(parms)) {
            retData.setMessage("参数为空");
            return retData;
        }
        JSONObject json = JSONObject.parseObject(parms);
        if (json == null) {
            retData.setMessage("参数为空");
            return retData;
        }
        WxPhoto wxPhoto = JSON.toJavaObject(json, WxPhoto.class);
        // 无保存内容
        if (wxPhoto == null) {
            retData.setMessage("无保存内容");
            return retData;
        }
        wxPhoto.setCreateTime(new Date());
        wxPhoto.setUpdateTime(new Date());
        // 保存数据库
        WxPhoto ret = wxPhotoService.save(wxPhoto);
        if (ret != null) {
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        }
        logger.info("save:(保存微信照片数据接口) 结束");
        return retData;
    }

}