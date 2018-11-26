package com.cn.xmf.model.user;


import com.cn.xmf.base.model.BaseEntitys;

import java.util.Date;

/**
 * 调度系统用户Entity
 *
 * @author airufei
 * @version 2018-09-18
 */
public class JobUser extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    private String username;        // 账号

    private String password;        // 密码

    private Integer age;        // 年龄

    private String email;        // 邮箱

    private String phone;        // 手机

    private String address;        // 地址

    private String qq;        // QQ

    private String wechart;        // 微信号

    private Date createTime;        // 创建时间

    private Date updateTime;        // 更新时间


    public JobUser() {

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechart() {
        return wechart;
    }

    public void setWechart(String wechart) {
        this.wechart = wechart;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}