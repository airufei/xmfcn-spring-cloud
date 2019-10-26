package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 参会人员信息登记Entity
 *
 * @author rufen.cn
 * @version 2019-10-26
 */
public class Meeting extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    /**
     * 姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 人数
     */
    private String num;

    /**
     * 用户唯一标识
     */
    private String openId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String photoUrl;


    public Meeting() {

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userName", userName)
                .append("phone", phone)
                .append("num", num)
                .append("openId", openId)
                .append("nickName", nickName)
                .append("photoUrl", photoUrl)
                .toString();
    }
}