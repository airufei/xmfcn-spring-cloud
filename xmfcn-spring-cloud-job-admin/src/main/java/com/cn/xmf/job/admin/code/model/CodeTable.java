package com.cn.xmf.job.admin.code.model;

import com.cn.xmf.base.model.BaseEntitys;

import java.util.Date;
import java.math.BigDecimal;

/**
 * 数据表信息Entity
 *
 * @author rufei.cn
 * @version 2018-12-10
 */
public class CodeTable extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    private String name;        // 名称

    private String comments;        // 描述

    private String className;        // 实体类名称


    public CodeTable() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}