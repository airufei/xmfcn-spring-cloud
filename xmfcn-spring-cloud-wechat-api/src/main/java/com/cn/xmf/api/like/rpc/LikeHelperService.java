package com.cn.xmf.api.like.rpc;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.common.SysCommonService;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.Like;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Service(微信点赞)
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
@SuppressWarnings("all")
@Service
public class LikeHelperService {

    @Autowired
    private SysCommonService sysCommonService;
    @Autowired
    private LikeService likeService;

    public long getWxUserLikeCount(String bizId) {
        long likeCount = 0;
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + "like_" + bizId;
        String cache = sysCommonService.getCache(key);
        if (StringUtil.isNotBlank(cache)) {
            return StringUtil.stringToLong(cache);
        }
        Like like = new Like();
        like.setBizId(bizId);
        Like userLike = likeService.getWxUserLike(like);
        if (userLike != null) {
            likeCount = userLike.getLikeCount();
        }
        sysCommonService.save(key, String.valueOf(likeCount), 60 * 10);
        return likeCount;
    }

}