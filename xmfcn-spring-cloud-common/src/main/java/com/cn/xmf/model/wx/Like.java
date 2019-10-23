package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 微信点赞Entity
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
public class Like extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    /**
     * 用户唯一标识
     */
    private String openId;

    /**
     * 类型
     */
    private String type;

    /**
     * 头像
     */
    private String photoUrl;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 业务ID
     */
    private String bizId;

    /**
     * 点赞数
     */
    private long likeCount;

    public Like() {

    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
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
                .append("openId", openId)
                .append("type", type)
                .append("nickName", nickName)
                .append("bizId", bizId)
                .append("likeCount", likeCount)
                .toString();
    }
}