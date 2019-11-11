package com.cn.xmf.api.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.common.SysCommonService;
import com.cn.xmf.api.music.rpc.MusicService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.Music;
import com.cn.xmf.util.ConstantUtil;
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
 * MusicController(微信音乐)
 * Controller 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 *
 * @author rufei.cn
 * @version 2019-10-21
 */
@RestController
@RequestMapping("/music/")
@SuppressWarnings("all")
public class MusicController {

    private static Logger logger = LoggerFactory.getLogger(MusicController.class);
    @Autowired
    private SysCommonService sysCommonService;
    @Autowired
    private MusicService musicService;

    /**
     * getList:(获取微信音乐分页查询接口)
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
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + "music_list_" + type + pageSize + pageNoStr;
        String cache = sysCommonService.getCache(key);
        if (StringUtil.isNotBlank(cache)) {
            JSONObject jsonObject = JSONObject.parseObject(cache);
            retData.setData(jsonObject);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
            return retData;
        }
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        logger.info("getList:(获取微信音乐分页查询接口) 开始  param={}", param);
        param.put("type", type);
        Partion pt = musicService.getList(param);
        List<Music> list = null;
        long totalCount = 0;
        if (pt != null) {
            list = (List<Music>) pt.getList();
            totalCount = pt.getTotalCount();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        jsonObject.put("totalCount", totalCount);
        sysCommonService.save(key, jsonObject.toString(), 60 * 30);
        if (list == null || list.size() <= 0) {
            retData.setData(jsonObject);
            retData.setCode(ResultCodeMessage.NO_DATA);
            retData.setMessage(ResultCodeMessage.NO_DATA_MESSAGE);
            return retData;
        }
        retData.setData(jsonObject);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getList:(获取微信音乐分页查询接口) 结束");
        return retData;
    }
}