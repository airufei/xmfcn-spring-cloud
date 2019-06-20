package com.cn.xmf.model.dict;



import com.cn.xmf.base.model.BaseEntitys;

import java.util.Date;

/**
 * 数据字典表Entity
 *
 * @author 数据字典表
 * @version 2017-11-23
 */
public class Dict extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    private String dictKey;        // 值

    private String dictValue;        // 名称

    private String fid;        // 父级ID

    private Date createTime;        // 创建时间

    private Date updateTime;        // 修改时间

    private String sort;        // 排序

    private String type;        // 类型


    public Dict() {

    }


    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "dictKey='" + dictKey + '\'' +
                ", dictValue='" + dictValue + '\'' +
                ", fid='" + fid + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", sort='" + sort + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}