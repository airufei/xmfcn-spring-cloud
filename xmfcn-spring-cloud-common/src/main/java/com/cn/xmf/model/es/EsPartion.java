/**
 * Project Name:CooxinPro
 * File Name:Pantion.java
 * Package Name:com.cn.cooxin.pojo
 * Date:2016年7月6日下午1:51:37
 * Copyright (c) 2016, hukailee@163.com All Rights Reserved.
 */

package com.cn.xmf.model.es;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 *  ES 分页结果数据集
 * @Author rufei.cn
 * @see
 */
@SuppressWarnings("all")
public class EsPartion<T> implements java.io.Serializable {

    private int totalCount;// 总的记录数
    private List<T> list;// 查询的数据集
    private int currentCount = 1;// 当前页页码
    private int pageSize = 10;// 每页记录数
    private int pageCount = 0;// 总页数
    private int nevPage = 0;// 上一页页码
    private int nextPage = 0;// 下一页页码
    private int isNext = -1;// 是否显示下一页按钮(-1显示  1不显示)
    private int isNev = 1;// 是否显示下一页按钮(-1显示  1不显示)

    public EsPartion() {

    }

    /**
     * 构造函数
     *
     * @param map        查询参数
     * @param list       数据集
     * @param totalcount 总记录数
     */
    public EsPartion(EsPage esPage, List<T> list, int totalcount) {
        if (esPage != null) {
            currentCount = esPage.pageNo;
            pageSize = esPage.pageSize;
        }
        if (list == null) {
            list = new ArrayList();
        }
        if (totalcount > 0) {
            pageCount = (totalcount + pageSize - 1) / pageSize;
            this.setPageCount(pageCount);
            nextPage = currentCount + 1;
            nevPage = currentCount - 1;
            if (nevPage < 1) {
                nevPage = 1;
            }
            this.setNextPage(nextPage);
            this.setNevPage(nevPage);
        }
        if (nextPage > pageCount) {
            this.setIsNext(1);
            nextPage = pageCount;
        }
        if (currentCount > 1) {
            this.setIsNev(-1);
        }
        this.setList(list);
        this.setTotalCount(totalcount);
    }

    /**
     * getCurrentCount:(当前页页码)
     *
     * @return
     * @Author rufei.cn
     */
    public int getCurrentCount() {
        return currentCount;
    }

    /**
     * setCurrentCount:(当前页页码)
     *
     * @return
     * @Author rufei.cn
     */
    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    /**
     * getPageSize:(每页记录数)
     *
     * @return
     * @Author rufei.cn
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * setPageSize:(每页记录数)
     *
     * @return
     * @Author rufei.cn
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * getPageCount:(总页数)
     *
     * @return
     * @Author rufei.cn
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * setPageCount:(总页数)
     *
     * @return
     * @Author rufei.cn
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * getNevPage:(上一页页码)
     *
     * @return
     * @Author rufei.cn
     */
    public int getNevPage() {
        return nevPage;
    }

    /**
     * setNevPage:(上一页页码)
     *
     * @return
     * @Author rufei.cn
     */
    public void setNevPage(int nevPage) {
        this.nevPage = nevPage;
    }

    /**
     * getNextPage:(下一页页码)
     *
     * @return
     * @Author rufei.cn
     */
    public int getNextPage() {
        return nextPage;
    }

    /**
     * setNextPage:(下一页页码)
     *
     * @return
     * @Author rufei.cn
     */
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    /**
     * getIsNext:(是否显示下一页按钮 -1显示  1不显示)
     *
     * @return
     * @Author rufei.cn
     */
    public int getIsNext() {
        return isNext;
    }

    /**
     * setIsNext:(是否显示下一页按钮)
     *
     * @return
     * @Author rufei.cn
     */
    public void setIsNext(int isNext) {
        this.isNext = isNext;
    }

    /**
     * getIsNev:(是否显示上一页按钮 -1显示  1不显示)
     *
     * @return
     * @Author rufei.cn
     */
    public int getIsNev() {
        return isNev;
    }

    /**
     * setIsNev:(是否显示上一页按钮)
     *
     * @return
     * @Author rufei.cn
     */
    public void setIsNev(int isNev) {
        this.isNev = isNev;
    }

    /**
     * getList:(查询数据集)
     *
     * @return
     * @Author rufei.cn
     */
    public List<T> getList() {
        return list;
    }

    /**
     * setList:(查询数据集)
     *
     * @return
     * @Author rufei.cn
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    /**
     * getTotalCount:(总记录数)
     *
     * @return
     * @Author rufei.cn
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * setTotalCount:(总记录数)
     *
     * @return
     * @Author rufei.cn
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("totalCount", totalCount)
                .append("list", list)
                .append("currentCount", currentCount)
                .append("pageSize", pageSize)
                .append("pageCount", pageCount)
                .append("nevPage", nevPage)
                .append("nextPage", nextPage)
                .append("isNext", isNext)
                .append("isNev", isNev)
                .toString();
    }
}
