/**
 * Project Name:CooxinPro
 * File Name:GenUtils.java
 * Package Name:com.cn.cooxin.util
 * Date:2017年1月21日下午10:14:37
 * Copyright (c) 2017, hukailee@163.com All Rights Reserved.
 *
*/

package com.cn.xmf.job.admin.util;

import com.cn.xmf.job.admin.code.model.CodeTableColumn;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.List;
import java.util.Map;


/**
 * ClassName:GenUtils (用一句话描述这个变量表示什么)
 * Date:     2017年1月21日 下午10:14:37
 * @Author   airufei
 * @Version  1.0
 * @see 	 
 */
public class TableColumnUtils {

	private static Logger logger = LoggerFactory.getLogger(TableColumnUtils.class);

	/**
	 * 初始化列属性字段
	 * @param column
	 */
	public static void initColumnField(CodeTableColumn column){
			// 设置字段说明
			if (StringUtil.isBlank(column.getComments())){
				column.setComments(column.getName());
			}
			// 设置java类型
			if (StringUtil.startsWithIgnoreCase(column.getJdbcType(), "CHAR")
					|| StringUtil.startsWithIgnoreCase(column.getJdbcType(), "VARCHAR")
					|| StringUtil.startsWithIgnoreCase(column.getJdbcType(), "NARCHAR")){
				column.setJavaType("String");
			}else if (StringUtil.startsWithIgnoreCase(column.getJdbcType(), "DATETIME")
					|| StringUtil.startsWithIgnoreCase(column.getJdbcType(), "DATE")
					|| StringUtil.startsWithIgnoreCase(column.getJdbcType(), "TIMESTAMP")){
				column.setJavaType("java.util.Date");
				column.setShowType("dateselect");
			}else if (StringUtil.startsWithIgnoreCase(column.getJdbcType(), "BIGINT")
					|| StringUtil.startsWithIgnoreCase(column.getJdbcType(), "NUMBER")){
				// 如果是浮点型
				String[] ss = StringUtil.split(StringUtil.substringBetween(column.getJdbcType(), "(", ")"), ",");
				if (ss != null && ss.length == 2 && Integer.parseInt(ss[1])>0){
					column.setJavaType("Double");
				}
				// 如果是整形
				else if (ss != null && ss.length == 1 && Integer.parseInt(ss[0])<=10){
					column.setJavaType("Integer");
				}
				// 长整形
				else{
					column.setJavaType("Long");
				}
			}
			// 设置java字段名
			column.setJavaField(StringUtil.toCamelCase(column.getName()));
			// 是否是主键
			column.setIsPk("0");
			// 插入字段
			column.setIsInsert("1");
			// 编辑字段
			if (!StringUtil.equalsIgnoreCase(column.getName(), "id")
					&& !StringUtil.equalsIgnoreCase(column.getName(), "create_by")
					&& !StringUtil.equalsIgnoreCase(column.getName(), "create_date")
					&& !StringUtil.equalsIgnoreCase(column.getName(), "del_flag")){
				column.setIsEdit("1");
			}
			// 列表字段
			if (StringUtil.equalsIgnoreCase(column.getName(), "name")
					|| StringUtil.equalsIgnoreCase(column.getName(), "title")
					|| StringUtil.equalsIgnoreCase(column.getName(), "remarks")
					|| StringUtil.equalsIgnoreCase(column.getName(), "update_date")){
				column.setIsList("1");
			}
			// 查询字段
			if (StringUtil.equalsIgnoreCase(column.getName(), "name")
					|| StringUtil.equalsIgnoreCase(column.getName(), "title")){
				column.setIsQuery("1");
			}
			
			// 查询字段类型
			if (StringUtil.equalsIgnoreCase(column.getName(), "name")
					|| StringUtil.equalsIgnoreCase(column.getName(), "title")){
				column.setQueryType("like");
			}
			// 创建时间、更新时间
			else if (StringUtil.startsWithIgnoreCase(column.getName(), "createtime")
					|| StringUtil.startsWithIgnoreCase(column.getName(), "updatetime")){
				column.setShowType("dateselect");
			}
			// 备注、内容
			else if (StringUtil.equalsIgnoreCase(column.getName(), "remark")
					|| StringUtil.equalsIgnoreCase(column.getName(), "content")){
				column.setShowType("textarea");
			}
			// 父级ID
			else if (StringUtil.equalsIgnoreCase(column.getName(), "parent_id")){
				column.setJavaType("This");
				column.setJavaField("parent.id|name");
				column.setShowType("treeselect");
			}
			// 所有父级ID
			else if (StringUtil.equalsIgnoreCase(column.getName(), "parent_ids")){
				column.setQueryType("like");
			}
			// 删除标记
			else if (StringUtil.equalsIgnoreCase(column.getName(), "flag")){
				column.setShowType("radiobox");
				column.setDictType("flag");
			}
		
	}
}

