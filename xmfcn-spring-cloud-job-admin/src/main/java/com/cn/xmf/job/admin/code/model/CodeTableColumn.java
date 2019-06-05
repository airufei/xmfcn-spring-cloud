package com.cn.xmf.job.admin.code.model;

import com.cn.xmf.base.model.BaseEntitys;

import java.math.BigDecimal;

/**
 * 表字段信息Entity
 *
 * @author rufei.cn
 * @version 2018-12-10
 */
public class CodeTableColumn extends BaseEntitys {
    public CodeTableColumn() {

    }
    private static final long serialVersionUID = 1L;
    private String tableId;        // 归属表编号

    private String name;        // 名称

    private String comments;        // 描述

    private String jdbcType;        // 列的数据类型的字节长度

    private String javaType;        // JAVA类型

    private String javaField;        // JAVA字段名

    private String isPk;        // 是否主键

    private String isNull;        // 是否可为空

    private String isInsert;        // 是否为插入字段

    private String isEdit;        // 是否编辑字段

    private String isList;        // 是否列表字段

    private String isQuery;        // 是否查询字段

    private String queryType;        // 查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）

    private String showType;        // 字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）

    private String dictType;        // 字典类型

    private String settings;        // 其它设置（扩展字段JSON）

    private BigDecimal sort;        // 排序（升序）

    private String tableName;        // 表名称

    private String isEditpage;        // 编辑字段

    private String isinsertrequiredfield;        // 插入必须字段 1 非必须0

    private String isupdaterequiredfield;        // 插入必须字段 1 非必须0


    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
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

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaField() {
        return javaField;
    }

    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }

    public String getIsPk() {
        return isPk;
    }

    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    public String getIsInsert() {
        return isInsert;
    }

    public void setIsInsert(String isInsert) {
        this.isInsert = isInsert;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getIsList() {
        return isList;
    }

    public void setIsList(String isList) {
        this.isList = isList;
    }

    public String getIsQuery() {
        return isQuery;
    }

    public void setIsQuery(String isQuery) {
        this.isQuery = isQuery;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIsEditpage() {
        return isEditpage;
    }

    public void setIsEditpage(String isEditpage) {
        this.isEditpage = isEditpage;
    }

    public String getIsinsertrequiredfield() {
        return isinsertrequiredfield;
    }

    public void setIsinsertrequiredfield(String isinsertrequiredfield) {
        this.isinsertrequiredfield = isinsertrequiredfield;
    }

    public String getIsupdaterequiredfield() {
        return isupdaterequiredfield;
    }

    public void setIsupdaterequiredfield(String isupdaterequiredfield) {
        this.isupdaterequiredfield = isupdaterequiredfield;
    }
}