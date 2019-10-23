package com.cn.xmf.api.photo.controller;

import com.alibaba.fastjson.JSONObject;
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
        String length = request.getParameter("pageSize");
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        int pageSize = 10;
        int pageNo = 1;
        if (StringUtil.isNotBlank(pageNoStr)) {
            pageNo = StringUtil.stringToInt(pageNoStr);
        }
        if (StringUtil.isNotBlank(length)) {
            pageSize = StringUtil.stringToInt(length);
        }
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        param.put("id", id);
        param.put("name", name);
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
        retData.setData(jsonObject);
        if (list == null||list.size()<=0) {
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
        long id=StringUtil.stringToLong(idStr);
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

    /**
     * save:(保存微信照片数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping(value = "save")
    public RetData save(HttpServletRequest request) {
        RetData retData = new RetData();
        Photo photo = new Photo();
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String url = request.getParameter("url");
        String description = request.getParameter("description");
        String remark = request.getParameter("remark");
        String path = request.getParameter("path");
        photo.setName(name);
        photo.setType(type);
        photo.setUrl(url);
        photo.setDescription(description);
        photo.setRemark(remark);
        photo.setPath(path);
        logger.info("save:(保存微信照片数据接口) 开始  photo={}", photo);
        photo.setCreateTime(new Date());
        photo.setUpdateTime(new Date());
        // 保存数据库
        Photo ret = photoService.save(photo);
        if (ret != null) {
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        }
        logger.info("save:(保存微信照片数据接口) 结束");
        return retData;
    }

    /**
     * delete:(逻辑删除微信照片数据接口)
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
        logger.info("delete:(逻辑删除微信照片数据接口) 开始  idStr={}", idStr);
        if (StringUtil.isBlank(idStr)) {
            retData.setMessage("参数为空");
            return retData;
        }
        Long id = StringUtil.stringToLong(idStr);
        if (id != null && id > 0) {
            photoService.delete(id);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        } else {
            retData.setMessage("请选择需要删除的数据");
        }
        logger.info("delete:(逻辑删除微信照片数据接口) 结束");
        return retData;
    }

}