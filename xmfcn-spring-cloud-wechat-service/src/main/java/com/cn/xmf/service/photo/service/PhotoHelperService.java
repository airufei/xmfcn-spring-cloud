package com.cn.xmf.service.photo.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Photo;
import com.cn.xmf.service.like.service.LikeHelperService;
import com.cn.xmf.service.comment.service.CommentHelperService;
import com.cn.xmf.service.photo.dao.PhotoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service(微信照片)
 *
 * @author airufei
 * @version 2019-10-11
 */
@Service
@SuppressWarnings("all")
public class PhotoHelperService {
    private static Logger logger = LoggerFactory.getLogger(PhotoHelperService.class);
    @Autowired
    private PhotoDao photoDao;
    @Autowired
    private CommentHelperService commentHelperService;
    @Autowired
    private LikeHelperService likeHelperService;


    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public long getTotalCount(JSONObject map) {
        long resCount = 0;
        Long totalCount = photoDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }

    /**
     * 数据补全
     *
     * @param map
     * @return
     */
    public List<Photo> ListToList(List<Photo> list) {
        List<Photo> newList = null;
        if (list == null) {
            return newList;
        }
        newList = new ArrayList<>();
        for (Photo p : list) {
            Long id = p.getId();
            long commentCount = commentHelperService.getPhotoCommentCount(String.valueOf(id), "photo_comment");
			long likeCount = likeHelperService.getPhotoLikeCount(String.valueOf(id), "photo_like");
			p.setCommentCount(commentCount);
			p.setLikeCount(likeCount);
			newList.add(p);
		}
        return newList;
    }


    /*
     * save(保存微信照片)
     * @param photo
     * @author airufei
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.REPEATABLE_READ, timeout = 30, rollbackFor = Exception.class)
    public Photo save(Photo photo) throws Exception {
        Photo ret = null;
        if (photo == null) {
            return ret;
        }
        if (photo.getId() != null && photo.getId() > 0) {
            updateById(photo);
            ret = photo;
        } else {
            photo.setId(null);
            photoDao.add(photo);
            ret = photo;
        }
        return ret;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public Photo getWxPhotoById(long id) {
        return photoDao.getWxPhotoById(id);
    }

    /**
     * 获取单条数据
     *
     * @param photo
     * @return
     * @author airufei
     */
    public Photo getSignleWxPhoto(Photo photo) {
        return photoDao.getSignleWxPhoto(photo);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    private void updateById(Photo photo) {
        photoDao.updateById(photo);
    }

}