package com.cn.xmf.job.admin.role.model;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.BaseEntitys;
import com.cn.xmf.job.admin.menu.model.JobMenu;

import java.util.List;

/**
 * 角色数据Entity
 *
 * @author airufei
 * @version 2018-12-19
 */
public class JobRole extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    private String name;        // 角色名称
    private String roleCode;        // 角色代码
    private List<String> list;//菜单数据

    public JobRole() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}