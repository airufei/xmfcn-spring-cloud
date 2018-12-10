package com.cn.xmf.job.admin.code.model;

import com.cn.xmf.base.model.BaseEntitys;

import java.util.Date;
import java.math.BigDecimal;
 /**
 * 代码生成方案Entity
 * @author airufei
 * @version 2018-12-10
 */
public class CodeScheme extends BaseEntitys {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
			
	private String category;		// 分类
			
	private String packageName;		// 生成包路径
			
	private String moduleName;		// 生成模块名
			
	private String subModuleName;		// 生成子模块名
			
	private String functionName;		// 生成功能名
			
	private String functionNameSimple;		// 生成功能名（简写）
			
	private String functionAuthor;		// 生成功能作者
			
	private String tableName;		// 表名
			
	private Integer tableId;		// 生成表编号
			
	private String modulePageName;		// 页面模块
			
	private String subPageName;		// 子模块
			
	
	
	public CodeScheme() {
		
	}


			
         public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
			
         public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
			
         public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
			
         public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
			
         public String getSubModuleName() {
		return subModuleName;
	}

	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}
			
         public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
			
         public String getFunctionNameSimple() {
		return functionNameSimple;
	}

	public void setFunctionNameSimple(String functionNameSimple) {
		this.functionNameSimple = functionNameSimple;
	}
			
         public String getFunctionAuthor() {
		return functionAuthor;
	}

	public void setFunctionAuthor(String functionAuthor) {
		this.functionAuthor = functionAuthor;
	}
			
         public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
			
         public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
			
         public String getModulePageName() {
		return modulePageName;
	}

	public void setModulePageName(String modulePageName) {
		this.modulePageName = modulePageName;
	}
			
         public String getSubPageName() {
		return subPageName;
	}

	public void setSubPageName(String subPageName) {
		this.subPageName = subPageName;
	}
}