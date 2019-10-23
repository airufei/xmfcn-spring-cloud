package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class User extends BaseEntitys {
    private static final long serialVersionUID = 1L;
    private String nickName;        // 昵称

    private String age;        // 年龄

    private String city;        // 城市

    private String country;        // 城市

    private String province;        // 省份

    private String photoUrl;        // 头像url

    private String openId;        // 微信用户id

    public User() {

    }

    public String getNickname() {
        return nickName;
    }

    public void setNickname(String nickName) {
        this.nickName = nickName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("nickName", nickName)
                .append("age", age)
                .append("city", city)
                .append("country", country)
                .append("province", province)
                .append("photoUrl", photoUrl)
                .append("openId", openId)
                .toString();
    }
}
