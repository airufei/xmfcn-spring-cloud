package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 微信留言Entity
 *
 * @author rufei.cn
 * @version 2019-10-15
 */
public class WxUserMessage extends BaseEntitys {

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
     * 内容
     */
    private String content;

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


    public WxUserMessage() {

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("openid", openid)
                .append("type", type)
                .append("content", content)
                .append("photourl", photourl)
                .append("nickname", nickname)
                .append("bizid", bizid)
                .toString();
    }
}