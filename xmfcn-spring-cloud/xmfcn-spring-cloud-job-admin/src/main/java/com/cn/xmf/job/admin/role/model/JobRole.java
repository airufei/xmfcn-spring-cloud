package com.cn.xmf.job.admin.role.model;

import com.cn.xmf.base.model.BaseEntitys;

/**
 * 角色数据Entity
 *
 * @author airufei
 * @version 2018-12-19
 */
public class JobRole extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    private String name;        // 角色名称

    public JobRole() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}