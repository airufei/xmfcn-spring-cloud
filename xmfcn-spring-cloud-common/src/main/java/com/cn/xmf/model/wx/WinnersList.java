package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 获奖名单Entity
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
public class WinnersList extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    /**
     * 奖品类型
     */
    private String type;

    /**
     * 奖品名称
     */
    private String name;

    /**
     * 奖品url
     */
    private String imgUrl;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String photourl;


    public WinnersList() {

    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("name", name)
                .append("imgUrl", imgUrl)
                .append("userId", userId)
                .append("nickname", nickname)
                .append("photourl", photourl)
                .toString();
    }
}