package com.cn.xmf.service.photo.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Photo;
import com.cn.xmf.service.photo.dao.PhotoDao;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Service(微信照片)
 *
 * @author airufei
 * @version 2019-10-11
 */
@RestController
@RequestMapping(value = "/server/photo/")
@SuppressWarnings("all")
public class PhotoService {

    @Autowired
    private PhotoDao photoDao;
    @Autowired
    private PhotoHelperService photoHelperService;
    @Autowired
    private static Logger logger = LoggerFactory.getLogger(PhotoService.class);

    /**
     * getList(获取微信照片带分页数据-服务)
     *
     * @param json
     * @return
     * @author airufei
     */
    @RequestMapping("getList")
    public Partion getList(@RequestBody JSONObject json) {
        logger.info("getList(获取微信照片带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        long totalcount = photoHelperService.getTotalCount(json);
        List<Photo> list = null;
        if (totalcount > 0) {
            list = photoDao.getList(json);
        }
        if(list!=null)
        {
            list= photoHelperService.ListToList(list);
        }
        Partion pt = new Partion(json, list, totalcount);
        return pt;
    }

    /**
     * getWxPhotoList(获取微信照片 不带分页数据-服务)
     *
     * @param photo
     * @return
     * @author airufei
     */
    @RequestMapping("getWxPhotoList")
    public List<Photo> getWxPhotoList(@RequestBody Photo photo) {
        String parms = JSON.toJSONString(photo);
        List<Photo> list = null;
        logger.info("getWxPhotoList(获取微信照片 不带分页数据-服务) 开始 parms={}", parms);
        if (photo == null) {
            return list;
        }
        list = photoDao.getWxPhotoList(photo);
        return list;
    }


    /**
     * save (保存微信照片 数据-服务)
     *
     * @param photo
     * @return
     * @author airufei
     */
    @RequestMapping("save")
    public Photo save(@RequestBody Photo photo) {
        logger.info("save (保存微信照片 数据-服务) 开始 photo={}", photo);
        Photo ret = null;
        if (photo == null) {
            return ret;
        }
        try {
            ret = photoHelperService.save(photo);
        } catch (Exception e) {
            logger.error("save (保存微信照片 数据-服务) 异常={}", StringUtil.getExceptionMsg(e));
            ret = null;
        }
        return ret;
    }


    /**
     * getWxPhoto(获取微信照片单条数据-服务)
     *
     * @param photo
     * @return
     * @author airufei
     */
    @RequestMapping("getWxPhoto")
    public Photo getWxPhoto(@RequestBody Photo photo) {
        Photo ret = null;
        String parms = JSON.toJSONString(photo);
        List<Photo> list = null;
        logger.info("getWxPhoto(获取微信照片单条数据-服务) 开始 parms={}", parms);
        if (photo == null) {
            return ret;
        }
        ret = photoHelperService.getSignleWxPhoto(photo);
        logger.info("getWxPhoto(获取微信照片单条数据-服务) 结束 ");
        return ret;
    }

    /**
     * delete(逻辑删除微信照片数据-服务)
     *
     * @param id
     * @return
     * @author airufei
     */
    @RequestMapping("delete")
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除微信照片数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        Photo dt = photoHelperService.getWxPhotoById(id);
        if (dt == null) {
            return isSuccess;
        }
        photoDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除微信照片数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }
}