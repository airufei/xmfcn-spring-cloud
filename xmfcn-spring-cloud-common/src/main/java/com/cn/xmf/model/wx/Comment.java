package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 微信留言Entity
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
public class Comment extends BaseEntitys {

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
     * 内容
     */
    private String content;

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


    public Comment() {

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("openId", openId)
                .append("type", type)
                .append("content", content)
                .append("photoUrl", photoUrl)
                .append("nickName", nickName)
                .append("bizId", bizId)
                .toString();
    }
}