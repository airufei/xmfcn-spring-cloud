package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WxUser extends BaseEntitys {
    private static final long serialVersionUID = 1L;
    private String nickname;        // 昵称

    private String age;        // 年龄

    private String city;        // 城市

    private String country;        // 城市

    private String province;        // 省份

    private String photourl;        // 头像url

    private String openid;        // 微信用户id


    public WxUser() {

    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("nickname", nickname)
                .append("age", age)
                .append("city", city)
                .append("country", country)
                .append("province", province)
                .append("photourl", photourl)
                .append("openid", openid)
                .toString();
    }
}
