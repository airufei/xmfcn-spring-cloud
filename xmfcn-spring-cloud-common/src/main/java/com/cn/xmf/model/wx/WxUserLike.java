package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 微信点赞Entity
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
public class WxUserLike extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 类型
     */
    private String type;

    /**
     * 头像
     */
    private String photourl;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 业务ID
     */
    private String bizid;

    /**
     * 点赞数
     */
    private long likeCount;

    public WxUserLike() {

    }


    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBizid() {
        return bizid;
    }

    public void setBizid(String bizid) {
        this.bizid = bizid;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("openid", openid)
                .append("type", type)
                .append("photourl", photourl)
                .append("nickname", nickname)
                .append("bizid", bizid)
                .append("likeCount", likeCount)
                .toString();
    }
}