package com.cn.xmf.service.like.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Like;
import com.cn.xmf.service.common.SysCommonService;
import com.cn.xmf.service.like.dao.LikeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * WxUserLikeService(微信点赞)
 *
 * @author rufei.cn
 * service 层的异常应该统一捕获进行处理，这样业务代码更加清晰
 * @version 2019-10-15
 */
@RestController
@RequestMapping(value = "/server/like/")
@SuppressWarnings("all")
public class LikeService {

    private static Logger logger = LoggerFactory.getLogger(LikeService.class);

    @Autowired
    private LikeDao likeDao;
    @Autowired
    private LikeHelperService likeHelperService;
    @Autowired
    private SysCommonService sysCommonService; //如果不需要发钉钉消息可以注释了

    /**
     * getList(获取微信点赞带分页数据-服务)
     *
     * @param json
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getList")
    public Partion getList(@RequestBody JSONObject json) {
        logger.info("getList(获取微信点赞带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        int totalcount = likeHelperService.getTotalCount(json);
        List<Like> list = null;
        if (totalcount > 0) {
            list = likeDao.getList(json);
        }
        Partion pt = new Partion(json, list, totalcount);
        logger.info("getList(获取微信点赞带分页数据-服务) 结束 ");
        return pt;
    }

    /**
     * getWxUserLikeList(获取微信点赞 不带分页数据-服务)
     *
     * @param like
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getWxUserLikeList")
    public List<Like> getWxUserLikeList(@RequestBody Like like) {
        String parms = JSON.toJSONString(like);
        List<Like> list = null;
        logger.info("getWxUserLikeList(获取微信点赞 不带分页数据-服务) 开始 parms={}", parms);
        if (like == null) {
            return list;
        }
        list = likeDao.getWxUserLikeList(like);
        logger.info("getWxUserLikeList(获取微信点赞 不带分页数据-服务) 结束");
        return list;
    }

    /**
     * 获取单条数据
     *
     * @param bizId
     * @param type
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getPhotoLikeCount")
    public long getPhotoLikeCount(String bizId, String type) {
       return likeHelperService.getPhotoLikeCount(bizId, type);
    }

    /**
     * save (保存微信点赞 数据-服务)
     *
     * @param like
     * @return
     * @author rufei.cn
     */
    @RequestMapping("save")
    public Like save(@RequestBody Like like) throws Exception {
        String parms = JSON.toJSONString(like);
        logger.info("save (保存微信点赞 数据-服务) 开始 parms={}", parms);
        Like ret = likeHelperService.save(like);
        logger.info("save (保存微信点赞 数据-服务) 结束");
        return ret;
    }


    /**
     * getWxUserLike(获取微信点赞单条数据-服务)
     *
     * @param like
     * @return
     * @author rufei.cn
     */
    @RequestMapping("getWxUserLike")
    public Like getWxUserLike(@RequestBody Like like) {
        String parms = JSON.toJSONString(like);
        logger.info("getWxUserLike(获取微信点赞单条数据-服务) 开始 parms={}", parms);
        Like ret = null;
        if (like == null) {
            return ret;
        }
        ret = likeHelperService.getSignleWxUserLike(like);
        logger.info("getWxUserLike(获取微信点赞单条数据-服务) 结束 ");
        return ret;
    }

    /**
     * delete(逻辑删除微信点赞数据-服务)
     *
     * @param id
     * @return
     * @author rufei.cn
     */
    @RequestMapping("delete")
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除微信点赞数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        Like dt = likeHelperService.getWxUserLikeById(id);
        if (dt == null) {
            return isSuccess;
        }
        likeDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除微信点赞数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }
}