package com.cn.xmf.job.admin.code.dao;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.code.model.CodeTable;

/**
 * 数据表信息DAO接口
 * @author airufei
 * @version 2018-12-10
 */
@SuppressWarnings("all")
public interface CodeTableDao {
	
	/**
	 * 删除数据（逻辑删除）
	 * @param question
	 * @return
	 */
	public void delete(long id);
    /**
	 * 单条数据增加
	 * @param codeTable
	 * @return
	 */
	public void add(CodeTable codeTable);

    /**
	 * 批量数据增加
	 * @param codeTable
	 * @return
	 */
	 public void addTrainRecordBatch(List<CodeTable> list);

     /**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	 public CodeTable getCodeTableById(long id);

      /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(CodeTable codeTable);

	  /**
	   * 获取分页数据
	   * @param map
	   * @return
	   */
	   public List<CodeTable>  getList(JSONObject map);
	   
	   
	   /**
	   * 获取集合数据，不带分页
	   * @param map
	   * @return
	   */
	   public List<CodeTable>  getCodeTableList(CodeTable codeTable);

	   /**
	   * 获取单条数据
	   * @param map
	   * @return
	   */
	   public CodeTable  getSignleCodeTable(CodeTable codeTable);

	  /**
	   * 获取分页记录总数
	   * @param map
	   * @return
	   */
	   public Integer  getTotalCount(Map map);
	   
}