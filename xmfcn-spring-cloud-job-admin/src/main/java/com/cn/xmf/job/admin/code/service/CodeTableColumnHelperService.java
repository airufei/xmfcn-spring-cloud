package com.cn.xmf.job.admin.code.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.code.dao.CodeTableColumnDao;
import com.cn.xmf.job.admin.code.model.CodeTableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service(表字段信息)
 * @author rufei.cn
 * @version 2018-12-10
 */
@Service
@SuppressWarnings("all")
public class CodeTableColumnHelperService  {

	@Autowired
	private CodeTableColumnDao codeTableColumnDao;
    private static Logger logger = LoggerFactory.getLogger(CodeTableColumnService.class);
	  /**
	   * 获取分页总记录数
	   * @param map
	   * @return
	   */
	   public int  getTotalCount(JSONObject map)
	   {
	      int resCount=0;
	      Integer totalCount =codeTableColumnDao.getTotalCount(map);
	      if(totalCount!=null)
	      {
	        resCount=totalCount;
	      }
	      return resCount;
	   }


    /**
     * save(保存表字段信息)
     * @param codeTableColumn
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    public CodeTableColumn save(CodeTableColumn codeTableColumn) {
        if (codeTableColumn == null) {
            return codeTableColumn;
        }
        if (codeTableColumn.getId() != null && codeTableColumn.getId() > 0) {
            updateById(codeTableColumn);
        } else {
            codeTableColumnDao.add(codeTableColumn);
        }
        return codeTableColumn;
    }

     /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	 public CodeTableColumn getCodeTableColumnById (long id)
	 {
	      return codeTableColumnDao.getCodeTableColumnById(id);
	 }

    /**
	 * 获取单条数据
	 * @param codeTableColumn
	 * @author rufei.cn
	 * @return
	 */
	 public CodeTableColumn getSignleCodeTableColumn(CodeTableColumn codeTableColumn)
	 {
	      return codeTableColumnDao.getSignleCodeTableColumn(codeTableColumn);
	 }

	 /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(CodeTableColumn codeTableColumn)
	  {
	     codeTableColumnDao.updateById(codeTableColumn);
	  }
	
}