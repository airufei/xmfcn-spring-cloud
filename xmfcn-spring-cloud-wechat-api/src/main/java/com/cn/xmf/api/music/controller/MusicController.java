package com.cn.xmf.api.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.music.service.MusicService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.model.wx.Music;
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
        String length = request.getParameter("pageSize");
        String type = request.getParameter("type");
        String title = request.getParameter("title");
        int pageSize = 10;
        int pageNo = 1;
        if (StringUtil.isNotBlank(pageNoStr)) {
            pageNo = StringUtil.stringToInt(pageNoStr);
        }
        if (StringUtil.isNotBlank(length)) {
            pageSize = StringUtil.stringToInt(length);
        }
        JSONObject param = StringUtil.getPageJSONObject(pageNo, pageSize);
        logger.info("getList:(获取微信音乐分页查询接口) 开始  param={}", param);
        param.put("type", type);
        param.put("title", title);
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

    /**
     * getMusic:(查询微信音乐单条数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("getMusic")
    public RetData getMusic(HttpServletRequest request) {
        RetData retData = new RetData();
        Music music = new Music();
        String type = request.getParameter("type");
        String title = request.getParameter("title");
        music.setType(type);
        music.setTitle(title);
        logger.info("getMusic:(查询微信音乐单条数据接口) 开始  music={}", music);
        Music retmusic = musicService.getMusic(music);
        retData.setData(retmusic);
        retData.setCode(ResultCodeMessage.SUCCESS);
        retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        logger.info("getMusic:(查询微信音乐单条数据接口) 结束");
        return retData;
    }

    /**
     * save:(保存微信音乐数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @Author rufei.cn
     */
    @RequestMapping(value = "save")
    public RetData save(HttpServletRequest request) {
        RetData retData = new RetData();
        Music music = new Music();
        String type = request.getParameter("type");
        String title = request.getParameter("title");
        String url = request.getParameter("url");
        music.setType(type);
        music.setTitle(title);
        music.setUrl(url);
        logger.info("save:(保存微信音乐数据接口) 开始  music={}", music);
        music.setCreateTime(new Date());
        music.setUpdateTime(new Date());
        // 保存数据库
        Music ret = musicService.save(music);
        if (ret != null) {
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        }
        logger.info("save:(保存微信音乐数据接口) 结束");
        return retData;
    }

    /**
     * delete:(逻辑删除微信音乐数据接口)
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
        logger.info("delete:(逻辑删除微信音乐数据接口) 开始  idStr={}", idStr);
        if (StringUtil.isBlank(idStr)) {
            retData.setMessage("参数为空");
            return retData;
        }
        Long id = StringUtil.stringToLong(idStr);
        if (id != null && id > 0) {
            musicService.delete(id);
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMessage(ResultCodeMessage.SUCCESS_MESSAGE);
        } else {
            retData.setMessage("请选择需要删除的数据");
        }
        logger.info("delete:(逻辑删除微信音乐数据接口) 结束");
        return retData;
    }

}