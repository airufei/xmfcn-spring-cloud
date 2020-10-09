package com.cn.xmf.job.admin.photo.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.photo.rpc.PhotoService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.model.wx.Photo;
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
 * @author rufei
 * @version 2019-10-11
 */
@Controller
@SuppressWarnings("all")
@RequestMapping("/photo")
public class PhotoController {

    private static Logger logger = LoggerFactory.getLogger(PhotoController.class);
    @Autowired
    private PhotoService photoService;
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
     * @author rufei
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
        Partion pt = photoService.getList(param);
        List<Photo> list = null;
        long totalCount = 0;
        if (pt != null) {
            list = (List<Photo>) pt.getList();
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
     * @author rufei
     */
    @RequestMapping("/getWxPhoto")
    @ResponseBody
    public RetData getWxPhoto(HttpServletRequest request) {
        RetData retData = new RetData();
        Photo photo = new Photo();
        if (photo == null) {
            retData.setMessage("参数为空");
            return retData;
        }
        Photo retphoto = photoService.getWxPhoto(photo);
        retData.setData(retphoto);
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
     * @author rufei
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
            photoService.delete(id);
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
     * @author rufei
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
        logger.info("save:(保存微信照片数据接口) 开始  name={},type={},description={}", name, type, description);
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
            logger.info(" 获取文件的后缀 filename={}", newFilenName);
            if (StringUtil.isBlank(newFilenName)) {
                continue;
            }
            logger.info(" 获取文件的后缀 newFilenName={}", newFilenName);
            String ossPath = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "img_store_path");
            if (StringUtil.isBlank(ossPath)) {
                String message = "存储路径不能为空";
                logger.info(message);
                continue;
            }
            long oldfileSize = oldfile.getSize();
            String temp_path = "/mnt/file/pic_temp/";
            String temp_scale = "/mnt/file/pic_scale/";
            StringUtil.threadSleep(500);
            FileReadUtil.uploadFile(oldfile.getBytes(), temp_path, newFilenName);//写入临时目录
            FileReadUtil.uploadOss(oldfileSize,newFilenName,temp_scale,temp_path,ossPath);
            Photo photo = new Photo();
            String read_path = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "img_read_path");
            if (StringUtil.isBlank(read_path)) {
                String message = "读取路径不能为空";
                logger.info(message);
                retData.setMsg(message);
                continue;
            }
            photo.setUrl(read_path + newFilenName);
            photo.setCreateTime(new Date());
            photo.setUpdateTime(new Date());
            photo.setPath(ossPath);
            photo.setDescription(description);
            photo.setName(name);
            photo.setType(type);
            photo.setRemark("AliCloud_OSS");
            // 保存数据库
            Photo ret = photoService.save(photo);
            if (ret != null) {
                retData.setCode(ResultCodeMessage.SUCCESS);
                retData.setMsg(ResultCodeMessage.SUCCESS_MESSAGE);
            }
        }
        logger.info("save:(保存微信照片数据接口) 结束");
        return retData;
    }

}