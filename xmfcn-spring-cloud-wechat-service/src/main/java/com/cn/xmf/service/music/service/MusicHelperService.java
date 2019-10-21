package com.cn.xmf.service.music.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.Music;
import com.cn.xmf.service.music.dao.MusicDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Music Service(微信音乐辅助类)
 *
 * @author rufei.cn
 * @version 2019-10-21
 */
@Service
@SuppressWarnings("all")
public class MusicHelperService {
    private static Logger logger = LoggerFactory.getLogger(MusicHelperService.class);

    @Autowired
    private MusicDao musicDao;

    /**
     * 获取分页总记录数
     *
     * @param map
     * @return
     */
    public int getTotalCount(JSONObject map) {
        int resCount = 0;
        Integer totalCount = musicDao.getTotalCount(map);
        if (totalCount != null) {
            resCount = totalCount;
        }
        return resCount;
    }


    /*
     * save(保存微信音乐)
     * @param music
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.REPEATABLE_READ, timeout = 30, rollbackFor = Exception.class)
    public Music save(Music music) throws Exception {
        Music ret = null;
        if (music == null) {
            return ret;
        }
        if (music.getId() != null && music.getId() > 0) {
            updateById(music);
            ret = music;
        } else {
            music.setId(null);
            musicDao.add(music);
            ret = music;
        }
        return ret;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public Music getMusicById(long id) {
        return musicDao.getMusicById(id);
    }

    /**
     * 获取单条数据
     *
     * @param music
     * @return
     * @author rufei.cn
     */
    public Music getSignleMusic(Music music) {
        return musicDao.getSignleMusic(music);
    }

    /**
     * 修改单条数据
     *
     * @param id
     * @return
     */
    private void updateById(Music music) {
        musicDao.updateById(music);
    }

}