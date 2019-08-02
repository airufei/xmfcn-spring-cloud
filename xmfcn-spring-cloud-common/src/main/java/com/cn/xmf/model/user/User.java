package com.cn.xmf.model.user;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 用户信息Entity
 *
 * @author rufei.cn
 * @version 2018-09-11
 */
public class User extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    private String username;        // username

    private String password;        // password

    private Integer age;        // age

    private String email;        // email

    private String phone;        // phone

    private String address;        // address

    private String weChart;        // wechart

    private Integer type;        // 用户类型: 0是互联网用户，1是管理员用户

    private Integer roleid;        // 角色ID


    public User() {

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public String getWeChart() {
        return weChart;
    }

    public void setWeChart(String weChart) {
        this.weChart = weChart;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("password", password)
                .append("age", age)
                .append("email", email)
                .append("phone", phone)
                .append("address", address)
                .append("weChart", weChart)
                .append("type", type)
                .append("roleid", roleid)
                .toString();
    }
}