package com.cn.xmf.job.admin.job.controller;

import com.alibaba.fastjson.JSON;
import com.cn.xmf.job.admin.core.model.XxlJobGroup;
import com.cn.xmf.job.admin.core.model.XxlJobInfo;
import com.cn.xmf.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.cn.xmf.job.admin.core.thread.JobTriggerPoolHelper;
import com.cn.xmf.job.admin.core.trigger.TriggerTypeEnum;
import com.cn.xmf.job.admin.core.util.I18nUtil;
import com.cn.xmf.job.admin.job.dao.XxlJobGroupDao;
import com.cn.xmf.job.admin.job.service.XxlJobService;
import com.cn.xmf.job.admin.menu.model.JobMenu;
import com.cn.xmf.job.admin.menu.service.JobMenuService;
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
import java.util.Date;
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
	 * @Author airufei
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public ReturnT<String> save(XxlJobInfo jobInfo) {
		ReturnT<String> returnT = new ReturnT<>(ReturnT.FAIL_CODE, "保存数据失败");
		String parms = null;
		try {
			parms = JSON.toJSONString(jobInfo);
			logger.info("save:(保存job-任务) 开始  parms={}", parms);
			if (jobInfo == null) {
				returnT.setMsg("参数为空");
				return returnT;
			}
			Integer id = jobInfo.getId();
			if (id == null||id<=0) {
				returnT= xxlJobService.add(jobInfo);
			}else {
					returnT= xxlJobService.update(jobInfo);
			}
			returnT.setCode(ReturnT.SUCCESS_CODE);
			returnT.setMsg("成功");
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "save:(保存job-任务) error===>" + StringUtil.getExceptionMsg(e);
			logger.error(msg);
			//sysCommonService.sendDingMessage("save", parms, JSON.toJSONString(returnT), msg, this.getClass());
			returnT.setMsg("服务器繁忙，请稍后再试");
		}
		logger.info("save 结束============>" + JSON.toJSONString(returnT));
		return returnT;
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
