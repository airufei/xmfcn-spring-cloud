package com.cn.xmf.job.admin.role.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.role.dao.JobMenuRoleDao;
import com.cn.xmf.job.admin.role.model.JobMenuRole;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
/**
 * Service(角色菜单关系)
 * @author rufei.cn
 * @version 2018-12-19
 */
@Service
@SuppressWarnings("all")
public class JobMenuRoleService  {

	@Autowired
	private JobMenuRoleDao jobMenuRoleDao;
	@Autowired
	private JobMenuRoleHelperService  jobMenuRoleHelperService;
    @Autowired
    private SysCommonService sysCommonService;
     private static Logger logger = LoggerFactory.getLogger(JobMenuRoleService.class);
      /**
	   * getList(获取角色菜单关系带分页数据-服务)
       * @author rufei.cn
       * @param json
	   * @return
	   */
	   public Partion getList(@RequestBody JSONObject json)
	   {
          logger.info("getList(获取角色菜单关系带分页数据-服务) 开始 json={}", json);
          if(json==null||json.size()<1)
          {
             return null;
          }
          Partion pt =null;
          try
           {
              int totalcount =jobMenuRoleHelperService.getTotalCount(json);
              List<JobMenuRole> list= null;
              if(totalcount>0)
              {
                list= jobMenuRoleDao.getList(json);
              }
               pt = new Partion(json, list, totalcount);
		   } catch (Exception e) {
            String msg = "getList(获取角色菜单关系 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            String parms = null;
            if (json != null) {
                parms = json.toString();
            }
              sysCommonService.sendDingTalkMessage("base-service[getList]", parms, null, msg, this.getClass());

          }
		  logger.info("getList(获取角色菜单关系带分页数据-服务) 结束 ");
		  return pt;
	   }

	   /**
	   * getJobMenuRoleList(获取角色菜单关系 不带分页数据-服务)
       * @author rufei.cn
       * @param jobMenuRole
	   * @return
	   */
	   public List<JobMenuRole>  getJobMenuRoleList(@RequestBody JobMenuRole jobMenuRole)
	   {
	      String parms = JSON.toJSONString(jobMenuRole);
	      List<JobMenuRole> list=null;
          logger.info("getJobMenuRoleList(获取角色菜单关系 不带分页数据-服务) 开始 parms={}", parms);
          if(jobMenuRole==null)
          {
             return list;
          }
          try {
	          list=jobMenuRoleDao.getJobMenuRoleList(jobMenuRole);
	       } catch (Exception e) {
            String msg = "getJobMenuRoleList 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingTalkMessage("base-service[getJobMenuRoleList]", parms, null, msg, this.getClass());

          }
	      logger.info("getJobMenuRoleList(获取角色菜单关系 不带分页数据-服务) 结束");
	      return list;
	   }


    /**
	 * save (保存角色菜单关系 数据-服务)
	 * @param jobMenuRole
     * @author rufei.cn
	 * @return
	 */
	public JobMenuRole save(@RequestBody JobMenuRole jobMenuRole)
	{
	   String parms = JSON.toJSONString(jobMenuRole);
        logger.info("save (保存角色菜单关系 数据-服务) 开始 parms={}", parms);
         JobMenuRole ret=null;
        if (jobMenuRole == null) {
            return ret;
        }
        try {
	         ret=jobMenuRoleHelperService.save(jobMenuRole);
	     } catch (Exception e) {
            String msg = "save (保存角色菜单关系 数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            ret=null;
            sysCommonService.sendDingTalkMessage("base-service[save]", parms, null, msg, this.getClass());

          }
	    logger.info("save (保存角色菜单关系 数据-服务) 结束");
	    return  ret;
	}

	   /**
	   * getJobMenuRole(获取角色菜单关系单条数据-服务)
	   * @author rufei.cn
	   * @param jobMenuRole
	   * @return
	   */
	   public JobMenuRole  getJobMenuRole(@RequestBody JobMenuRole jobMenuRole)
	   {
	      JobMenuRole ret=null;
	      String parms = JSON.toJSONString(jobMenuRole);
	      List<JobMenuRole> list=null;
          logger.info("getJobMenuRole(获取角色菜单关系单条数据-服务) 开始 parms={}", parms);
           if(jobMenuRole==null)
           {
             return ret;
           }
           try{
	            ret=jobMenuRoleHelperService.getSignleJobMenuRole(jobMenuRole);
	       } catch (Exception e) {
            String msg = "getJobMenuRole(获取角色菜单关系单条数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingTalkMessage("base-service[getJobMenuRole]", parms, null, msg, this.getClass());

          }
	       logger.info("getJobMenuRole(获取角色菜单关系单条数据-服务) 结束 ");
	      return ret;
	   }
    /**
	 * delete(逻辑删除角色菜单关系数据-服务)
	 * @param id
	 * @author rufei.cn
	 * @return
	 */
	 public boolean delete(Long id)
	 {
	    logger.info("delete(逻辑删除角色菜单关系数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
	    jobMenuRoleDao.delete(id);
	    isSuccess = true;
        logger.info("delete(逻辑删除角色菜单关系数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
	 }
}