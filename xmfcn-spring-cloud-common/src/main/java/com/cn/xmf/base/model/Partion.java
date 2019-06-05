/**
 * Project Name:CooxinPro
 * File Name:Pantion.java
 * Package Name:com.cn.cooxin.pojo
 * Date:2016年7月6日下午1:51:37
 * Copyright (c) 2016, hukailee@163.com All Rights Reserved.
 *
 */

package com.cn.xmf.base.model;

import com.cn.xmf.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName:Partion Date: 2016年7月6日 下午1:51:37
 * 
 * @author rufei.cn
 * @Version 1.0
 * @see
 */
@SuppressWarnings("all")
public class Partion implements java.io.Serializable {

	private int totalCount;// 总的记录数
	private Object list;// 查询的数据集
	private int currentCount = 1;// 当前页页码
	private int pageSize = 10;// 每页记录数
	private int pageCount = 0;// 总页数
	private int nevPage = 0;// 上一页页码
	private int nextPage = 0;// 下一页页码
	private int isNext = -1;// 是否显示下一页按钮(-1显示  1不显示)
	private int isNev =1;// 是否显示下一页按钮(-1显示  1不显示)

	public Partion() {

	}

	/**
	 * 构造函数
	 * @param map 查询参数
	 * @param list 数据集
	 * @param totalcount 总记录数
	 */
	public Partion(Map map, List list, int totalcount) {
		if (map != null) {
			currentCount = StringUtil.objToInt(map.get("currentCount"));
			pageSize = StringUtil.objToInt(map.get("pageSize"));
		}
		if (list == null) {
			list = new ArrayList();
		}
		if (totalcount >0) {
			pageCount = (totalcount + pageSize - 1) / pageSize;
			this.setPageCount(pageCount);
			nextPage= currentCount + 1;
			nevPage=currentCount- 1;
			if (nevPage<1) {
				nevPage=1;
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
	 * @author rufei.cn
	 * @return
	 */
	public int getCurrentCount() {
		return currentCount;
	}

	/**
	 * setCurrentCount:(当前页页码)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}

	/**
	 * getPageSize:(每页记录数)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * setPageSize:(每页记录数)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * getPageCount:(总页数)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * setPageCount:(总页数)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * getNevPage:(上一页页码)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public int getNevPage() {
		return nevPage;
	}

	/**
	 * setNevPage:(上一页页码)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public void setNevPage(int nevPage) {
		this.nevPage = nevPage;
	}

	/**
	 * getNextPage:(下一页页码)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public int getNextPage() {
		return nextPage;
	}

	/**
	 * setNextPage:(下一页页码)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * getIsNext:(是否显示下一页按钮 -1显示  1不显示)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public int getIsNext() {
		return isNext;
	}

	/**
	 * setIsNext:(是否显示下一页按钮)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public void setIsNext(int isNext) {
		this.isNext = isNext;
	}

	/**
	 * getIsNev:(是否显示上一页按钮 -1显示  1不显示)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public int getIsNev() {
		return isNev;
	}

	/**
	 * setIsNev:(是否显示上一页按钮)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public void setIsNev(int isNev) {
		this.isNev = isNev;
	}

	/**
	 * getList:(查询数据集)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public Object getList() {
		return list;
	}

	/**
	 * setList:(查询数据集)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public void setList(Object list) {
		this.list = list;
	}

	/**
	 * getTotalCount:(总记录数)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * setTotalCount:(总记录数)
	 * 
	 * @author rufei.cn
	 * @return
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return "Partion{" +
				"totalCount=" + totalCount +
				", list=" + list +
				", currentCount=" + currentCount +
				", pageSize=" + pageSize +
				", pageCount=" + pageCount +
				", nevPage=" + nevPage +
				", nextPage=" + nextPage +
				", isNext=" + isNext +
				", isNev=" + isNev +
				'}';
	}
}
