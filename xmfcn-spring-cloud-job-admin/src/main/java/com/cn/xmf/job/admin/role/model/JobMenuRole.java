package com.cn.xmf.job.admin.role.model;

import com.cn.xmf.base.model.BaseEntitys;

import java.util.Date;
import java.math.BigDecimal;

/**
 * 角色菜单关系Entity
 *
 * @author rufei.cn
 * @version 2018-12-19
 */
public class JobMenuRole extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    private Long roleId;        // 角色ID
    private String roleCode;        // 角色Code

    private Long menuId;        // 菜单ID


    public JobMenuRole() {

    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}