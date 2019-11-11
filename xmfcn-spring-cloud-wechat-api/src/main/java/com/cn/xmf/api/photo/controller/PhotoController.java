package com.cn.xmf.api.photo.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.common.SysCommonService;
import com.cn.xmf.api.photo.service.PhotoService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.Photo;
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
 * WxPhotoController(微信照片)
 * Controller 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
@RestController
@RequestMapping("/photo")
@SuppressWarnings("all")
public class PhotoController {

    private static Logger logger = LoggerFactory.getLogger(PhotoController.class);

    @Autowired
    private PhotoService photoService;
    @Autowired
    private SysCommonService sysCommonService;

    /**
     * getList:(获取微信照片分页查询接口)
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
        String type = request.getParameter("type");
        int pageSize = 10;
        int pageNo = 1;
        if (pageNo > 50) {
            pageNo = 50;
        }
        if (StringUtil.isNotBlank(pageNoStr)) {
            pageNo = StringUtil.stringToInt(pageNoStr);
        }
        String key = "getPhotoList_" + type + pageNo + pageSize;
        String cache = sysCommonService.getCache(key);
        if (StringUtil.isNotBlank(cache)) {
            JSONObject jsonObject = JSONObject.parseObject(cache);
            retData.setData(jsonObject);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
            return retData;
        }
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        param.put("type", type);
        logger.info("getList:(获取微信照片分页查询接口) 开始  param={}", param);
        Partion pt = photoService.getList(param);
        List<Photo> list = null;
        long totalCount = 0;
        if (pt != null) {
            list = (List<Photo>) pt.getList();
            totalCount = pt.getTotalCount();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        jsonObject.put("totalCount", totalCount);
        sysCommonService.save(key, jsonObject.toString(), 10 * 60);
        retData.setData(jsonObject);
        if (list == null || list.size() <= 0) {
            retData.setCode(ResultCodeMessage.NO_DATA);
            retData.setMessage(ResultCodeMessage.NO_DATA_MESSAGE);
            return retData;
        }
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getList:(获取微信照片分页查询接口) 结束");
        return retData;
    }

    /**
     * getWxPhoto:(查询微信照片单条数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("getWxPhoto")
    public RetData getWxPhoto(HttpServletRequest request) {
        RetData retData = new RetData();
        Photo photo = new Photo();
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        long id = StringUtil.stringToLong(idStr);
        photo.setId(id);
        photo.setName(name);
        photo.setType(type);
        logger.info("getWxPhoto:(查询微信照片单条数据接口) 开始  photo={}", photo);
        Photo retphoto = photoService.getWxPhoto(photo);
        retData.setData(retphoto);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getWxPhoto:(查询微信照片单条数据接口) 结束");
        return retData;
    }
}