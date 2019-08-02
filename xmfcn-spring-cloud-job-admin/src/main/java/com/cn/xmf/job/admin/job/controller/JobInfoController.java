package com.cn.xmf.job.admin.job.controller;

import com.alibaba.fastjson.JSON;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.job.admin.core.model.XxlJobGroup;
import com.cn.xmf.job.admin.core.model.XxlJobInfo;
import com.cn.xmf.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.cn.xmf.job.admin.core.thread.JobTriggerPoolHelper;
import com.cn.xmf.job.admin.core.trigger.TriggerTypeEnum;
import com.cn.xmf.job.admin.job.dao.XxlJobGroupDao;
import com.cn.xmf.job.admin.job.service.XxlJobService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.job.core.enums.ExecutorBlockStrategyEnum;
import com.cn.xmf.job.core.glue.GlueTypeEnum;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {

	@Resource
	private XxlJobGroupDao xxlJobGroupDao;
	@Resource
	private XxlJobService xxlJobService;
	private static Logger logger = LoggerFactory.getLogger(JobInfoController.class);
	@RequestMapping
	public String index(Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

		// 枚举-字典
		model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());	// 路由策略-列表
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());								// Glue类型-字典
		model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());	// 阻塞处理策略-字典

		// 任务组
		List<XxlJobGroup> jobGroupList =  xxlJobGroupDao.findAll();
		model.addAttribute("JobGroupList", jobGroupList);
		model.addAttribute("jobGroup", jobGroup);

		return "jobinfo/jobinfo.index";
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,  
			@RequestParam(required = false, defaultValue = "10") int length,
			int jobGroup, String jobDesc, String executorHandler, String filterTime) {
		
		return xxlJobService.pageList(start, length, jobGroup, jobDesc, executorHandler, filterTime);
	}


	/**
	 * save:(保存job-任务)
	 *
	 * @param jobInfo
	 * @return
	 * @author rufei.cn
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public ReturnT<String> save(XxlJobInfo jobInfo) {
		ReturnT<String> retData = new ReturnT<>(ResultCodeMessage.FAILURE, "保存数据失败");
		String parms = null;
		try {
			parms = JSON.toJSONString(jobInfo);
			logger.info("save:(保存job-任务) 开始  parms={}", parms);
			if (jobInfo == null) {
				retData.setMsg(ResultCodeMessage.PARMS_ERROR_MESSAGE);
				return retData;
			}
			Integer id = jobInfo.getId();
			if (id == null||id<=0) {
				retData= xxlJobService.add(jobInfo);
			}else {
					retData= xxlJobService.update(jobInfo);
			}
			retData.setCode(ResultCodeMessage.SUCCESS);
			retData.setMsg(ResultCodeMessage.SUCCESS_MESSAGE);
		} catch (Exception e) {
			String msg = "save:(保存job-任务) error===>" + StringUtil.getExceptionMsg(e);
			logger.error(msg);
			retData.setMsg(ResultCodeMessage.EXCEPTION_MESSAGE);
		}
		logger.info("save 结束============>" + JSON.toJSONString(retData));
		return retData;
	}
	
	@RequestMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(int id) {
		return xxlJobService.remove(id);
	}
	
	@RequestMapping("/pause")
	@ResponseBody
	public ReturnT<String> pause(int id) {
		return xxlJobService.pause(id);
	}
	
	@RequestMapping("/resume")
	@ResponseBody
	public ReturnT<String> resume(int id) {
		return xxlJobService.resume(id);
	}
	
	@RequestMapping("/trigger")
	@ResponseBody
	public ReturnT<String> triggerJob(int id,String executorParam) {
		JobTriggerPoolHelper.trigger(id, TriggerTypeEnum.MANUAL, -1, null, executorParam);
		return ReturnT.SUCCESS;
	}
	
}
