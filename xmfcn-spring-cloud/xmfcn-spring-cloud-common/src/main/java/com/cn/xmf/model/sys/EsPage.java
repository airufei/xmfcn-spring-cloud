package com.cn.xmf.model.sys;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * ES 分页相关数据
 */
public class EsPage implements Serializable {
    public int pageNo;
    public int startIndex;
    public int pageSize;
    public int totalCount;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pageNo", pageNo)
                .append("startIndex", startIndex)
                .append("pageSize", pageSize)
                .append("totalCount", totalCount)
                .toString();
    }
}
