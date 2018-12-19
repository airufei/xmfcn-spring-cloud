package com.cn.xmf.job.admin.role.model;

import com.cn.xmf.base.model.BaseEntitys;

import java.util.Date;
import java.math.BigDecimal;

/**
 * 角色菜单关系Entity
 *
 * @author airufei
 * @version 2018-12-19
 */
public class JobMenuRole extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    private Integer roleId;        // 角色ID

    private Integer menuId;        // 菜单ID


    public JobMenuRole() {

    }


    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}