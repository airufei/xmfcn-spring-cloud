package com.cn.xmf.job.admin.photo.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.photo.service.WxPhotoService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.model.wx.WxPhoto;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.FileReadUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * WxPhotoController(微信照片)
 * Controller 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author airufei
 * @version 2019-10-11
 */
@Controller
@SuppressWarnings("all")
@RequestMapping("/photo")
public class WxPhotoController {

    private static Logger logger = LoggerFactory.getLogger(WxPhotoController.class);
    @Autowired
    private WxPhotoService wxPhotoService;
    @Autowired
    private SysCommonService sysCommonService;

    @RequestMapping
    public String index() {
        return "photo/photo.index";
    }

    /**
     * getList:(获取微信照片分页查询接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping("/getList")
    @ResponseBody
    public Map<String, Object> getList(HttpServletRequest request) {
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String type = request.getParameter("type");
        int pageNo = StringUtil.stringToInt(pageNoStr);
        int pageSize = StringUtil.stringToInt(pageSizeStr);
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        param.put("type", type);
        Partion pt = wxPhotoService.getList(param);
        List<WxPhoto> list = null;
        int totalCount = 0;
        if (pt != null) {
            list = (List<WxPhoto>) pt.getList();
            totalCount = pt.getPageCount();
        }
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", totalCount);        // 总记录数
        maps.put("recordsFiltered", totalCount);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        logger.info("getList:(获取微信照片分页查询接口) 结束");
        return maps;
    }

    /**
     * getWxPhoto:(查询微信照片单条数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author airufei
     */
    @RequestMapping("/getWxPhoto")
    @ResponseBody
    public RetData getWxPhoto(HttpServletRequest request) {
        RetData retData = new RetData();
        WxPhoto wxPhoto = new WxPhoto();
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
    @RequestMapping("/delete")
    @ResponseBody
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
    @RequestMapping("/save")
    public ReturnT<String> save(HttpServletRequest request) throws Exception {
        ReturnT<String> retData = new ReturnT<>(ResultCodeMessage.FAILURE, "保存数据失败");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        /** 页面控件的文件流* */
        Map map = multipartRequest.getFileMap();
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String description = request.getParameter("description");
        logger.info("save:(保存微信照片数据接口) 开始  name={},type={},description={}", name,type,description);
        if (StringUtil.isBlank(type)) {
            retData.setMsg("类型不能为空");
            return retData;
        }
        if (StringUtil.isBlank(name)) {
            retData.setMsg("名称不能为空");
            return retData;
        }
        for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            MultipartFile oldfile = (MultipartFile) map.get(obj);
            if (oldfile == null) {
                continue;
            }
            /** 获取文件的后缀* */
            String filename = oldfile.getOriginalFilename();
            logger.info(" 获取文件的后缀 filename={}", filename);
            if (StringUtil.isBlank(filename)) {
                continue;
            }
            String suffix = filename.substring(filename.lastIndexOf(".") + 1);
            String newFilenName = StringUtil.getUuId() + "." + suffix;
            logger.info(" 获取文件的后缀 newFilenName={}", newFilenName);
            String path = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "img_store_path");
            if (StringUtil.isBlank(path)) {
                String message="存储路径不能为空";
                logger.info(message);
                continue;
            }
            FileReadUtil.uploadFile(oldfile.getBytes(), path, newFilenName);
            WxPhoto wxPhoto = new WxPhoto();
            String read_path = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "img_read_path");
            if (StringUtil.isBlank(read_path)) {
                String message="读取路径不能为空";
                logger.info(message);
                retData.setMsg(message);
                continue;
            }
            wxPhoto.setUrl(read_path + newFilenName);
            wxPhoto.setCreateTime(new Date());
            wxPhoto.setUpdateTime(new Date());
            wxPhoto.setPath(path);
            wxPhoto.setDescription(description);
            wxPhoto.setName(name);
            wxPhoto.setType(type);
            // 保存数据库
            WxPhoto ret = wxPhotoService.save(wxPhoto);
            if (ret != null) {
                retData.setCode(ResultCodeMessage.SUCCESS);
                retData.setMsg(ResultCodeMessage.SUCCESS_MESSAGE);
            }
        }
        logger.info("save:(保存微信照片数据接口) 结束");
        return retData;
    }

}