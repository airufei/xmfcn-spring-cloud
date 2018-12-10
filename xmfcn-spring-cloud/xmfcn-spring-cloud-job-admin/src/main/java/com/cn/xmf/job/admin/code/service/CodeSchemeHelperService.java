package com.cn.xmf.job.admin.code.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.code.dao.CodeSchemeDao;
import com.cn.xmf.job.admin.code.model.CodeScheme;
import com.cn.xmf.job.admin.common.SysCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service(代码生成方案)
 * @author airufei
 * @version 2018-12-10
 */
@Service
@SuppressWarnings("all")
public class CodeSchemeHelperService  {

	@Autowired
	private CodeSchemeDao codeSchemeDao;
	@Autowired
	private SysCommonService sysCommonService;
    private static Logger logger = LoggerFactory.getLogger(CodeSchemeService.class);
	  /**
	   * 获取分页总记录数
	   * @param map
	   * @return
	   */
	   public int  getTotalCount(JSONObject map)
	   {
	      int resCount=0;
	      Integer totalCount =codeSchemeDao.getTotalCount(map);
	      if(totalCount!=null)
	      {
	        resCount=totalCount;
	      }
	      return resCount;
	   }


    /*
     * save(保存代码生成方案)
     * @param codeScheme
     * @author airufei
     * @date 2018/1/30 14:59
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    public CodeScheme save(CodeScheme codeScheme) {
        if (codeScheme == null) {
            return codeScheme;
        }
        if (codeScheme.getId() != null && codeScheme.getId() > 0) {
            updateById(codeScheme);
        } else {
            codeSchemeDao.add(codeScheme);
        }
        return codeScheme;
    }

     /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	 public CodeScheme getCodeSchemeById (long id)
	 {
	      return codeSchemeDao.getCodeSchemeById(id);
	 }

    /**
	 * 获取单条数据
	 * @param codeScheme
	 * @author airufei
	 * @return
	 */
	 public CodeScheme getSignleCodeScheme(CodeScheme codeScheme)
	 {
	      return codeSchemeDao.getSignleCodeScheme(codeScheme);
	 }

	 /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  public void updateById(CodeScheme codeScheme)
	  {
	     codeSchemeDao.updateById(codeScheme);
	  }
	
}