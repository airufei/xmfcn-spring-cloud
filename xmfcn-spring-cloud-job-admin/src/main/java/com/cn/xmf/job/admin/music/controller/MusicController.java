package com.cn.xmf.job.admin.music.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.music.service.MusicService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.model.wx.Music;
import com.cn.xmf.model.wx.WxPhoto;
import com.cn.xmf.util.AliyunOSSClientUtil;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * MusicController(微信音乐)
 *
 * @author rufei.cn
 * @version 2019-10-21
 */
@Controller
@RequestMapping("/music")
@SuppressWarnings("all")
public class MusicController {

    private static Logger logger = LoggerFactory.getLogger(MusicService.class);
    @Autowired
    private MusicService musicService;
    @Autowired
    private SysCommonService sysCommonService;

    @RequestMapping
    public String index() {
        return "music/music-index";
    }

    /**
     * getList:(获取微信音乐分页查询接口)
     *
     * @param request
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("/pageList")
    @ResponseBody
    public JSONObject getList(HttpServletRequest request) {
        JSONObject retJon = new JSONObject();
        JSONObject param = null;
        String type = request.getParameter("type");
        String title = request.getParameter("title");
        String startStr = request.getParameter("start");
        String length = request.getParameter("length");
        int pageSize = 10;
        int pageNo = 1;
        int start = 0;
        if (StringUtil.isNotBlank(startStr)) {
            start = StringUtil.stringToInt(startStr);
        }
        if (StringUtil.isNotBlank(length)) {
            pageSize = StringUtil.stringToInt(length);
        }
        if (start > 0) {
            pageNo = (start / pageSize) + 1;
        }
        param = StringUtil.getPageJSONObject(pageNo, pageSize);
        logger.info("getList:(获取微信音乐分页查询接口) 开始  param={}", param);
        param.put("type", type);
        param.put("title", title);
        Partion pt = musicService.getList(param);
        List<Music> list = null;
        int totalCount = 0;
        if (pt != null) {
            list = (List<Music>) pt.getList();
            totalCount = pt.getTotalCount();
        }
        retJon.put("data", list);
        retJon.put("recordsTotal", totalCount);
        retJon.put("recordsFiltered", totalCount);
        logger.info("getList:(获取微信音乐分页查询接口) 结束");
        return retJon;
    }

    /**
     * delete:(逻辑删除微信音乐数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ReturnT<String> delete(HttpServletRequest request) {
        ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "删除失败");
        String ids = null;
        ids = request.getParameter("id");
        int id = StringUtil.stringToInt(ids);
        logger.info("delete 开始============>" + id);
        if (id <= 0) {
            returnT.setMsg("参数错误");
            return returnT;
        }
        long newId = id;
        boolean ret = musicService.delete(newId);
        if (ret) {
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setMsg("成功");
        }
        logger.info("delete 结束============>" + JSON.toJSONString(returnT));
        return returnT;
    }

    /**
     * save:(保存微信音乐数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public ReturnT<String> save(HttpServletRequest request) throws IOException {
        ReturnT<String> retData = new ReturnT<>(ReturnT.FAIL_CODE, "保存数据失败");
        String remark = request.getParameter("remark");
        String type = request.getParameter("type");
        String description = request.getParameter("description");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        /** 页面控件的文件流* */
        Map map = multipartRequest.getFileMap();
        if (StringUtil.isBlank(type)) {
            retData.setMsg("类型不能为空");
            return retData;
        }
        if (map == null) {
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
            String ossPath = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "music_store_path");
            if (StringUtil.isBlank(ossPath)) {
                String message = "存储路径不能为空";
                logger.info(message);
                continue;
            }
            String temp_path = "/mnt/file/music_temp/";
            FileReadUtil.uploadFile(oldfile.getBytes(), temp_path, newFilenName);//写入临时目录
            File temFile = new File(temp_path + newFilenName);
            AliyunOSSClientUtil.upLoadFileOSS(temFile, ossPath);
            if (temFile.exists()) {
                temFile.delete();
                temFile=null;
            }
            WxPhoto wxPhoto = new WxPhoto();
            String read_path = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "music_read_path");
            if (StringUtil.isBlank(read_path)) {
                String message = "读取路径不能为空";
                logger.info(message);
                retData.setMsg(message);
                continue;
            }
            Music music = new Music();
            music.setTitle(filename);
            music.setType(type);
            music.setUrl(read_path + newFilenName);
            music.setCreateTime(new Date());
            music.setUpdateTime(new Date());
            music.setRemark(remark);
            Music ret = musicService.save(music);
            if (ret == null) {
                retData.setMsg("保存数据失败");
                retData.setCode(ReturnT.FAIL_CODE);
                continue;
            }
        }
        retData.setCode(ReturnT.SUCCESS_CODE);
        retData.setMsg("成功");
        logger.info("save 结束============>" + JSON.toJSONString(retData));
        return retData;
    }

}