package com.cn.xmf.job.admin.job.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.core.model.XxlJobGroup;
import com.cn.xmf.job.admin.core.model.XxlJobInfo;
import com.cn.xmf.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.cn.xmf.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.cn.xmf.job.admin.core.util.I18nUtil;
import com.cn.xmf.job.admin.job.dao.XxlJobGroupDao;
import com.cn.xmf.job.admin.job.dao.XxlJobInfoDao;
import com.cn.xmf.job.admin.job.dao.XxlJobLogDao;
import com.cn.xmf.job.admin.job.dao.XxlJobLogGlueDao;
import com.cn.xmf.job.admin.job.service.XxlJobService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.job.core.enums.ExecutorBlockStrategyEnum;
import com.cn.xmf.job.core.glue.GlueTypeEnum;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.DateUtil;
import com.cn.xmf.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * core job action for xxl-job
 *
 * @author xuxueli 2016-5-28 15:30:33
 */
@Service
public class XxlJobServiceImpl implements XxlJobService {
    private static Logger logger = LoggerFactory.getLogger(XxlJobServiceImpl.class);

    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobInfoDao xxlJobInfoDao;
    @Resource
    public XxlJobLogDao xxlJobLogDao;
    @Resource
    private XxlJobLogGlueDao xxlJobLogGlueDao;
    @Autowired
    private SysCommonService sysCommonService;


    @Override
    public Map<String, Object> pageList(int start, int length, int jobGroup, String jobDesc, String executorHandler, String filterTime) {

        // page list
        List<XxlJobInfo> list = null;
        int listCount = xxlJobInfoDao.pageListCount(start, length, jobGroup, jobDesc, executorHandler);
        if (listCount >= 0) {
            list = xxlJobInfoDao.pageList(start, length, jobGroup, jobDesc, executorHandler);
        }
        // fill job info
        if (list != null && list.size() > 0) {
            for (XxlJobInfo jobInfo : list) {
                XxlJobDynamicScheduler.fillJobInfo(jobInfo);
            }
        }

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", listCount);        // 总记录数
        maps.put("recordsFiltered", listCount);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        return maps;
    }

    @Override
    public ReturnT<String> add(XxlJobInfo jobInfo) {
        // valid
        XxlJobGroup group = xxlJobGroupDao.load(jobInfo.getJobGroup());
        if (group == null) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("system_please_choose") + I18nUtil.getString("jobinfo_field_jobgroup")));
        }
        if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, I18nUtil.getString("jobinfo_field_cron_unvalid"));
        }
        if (StringUtils.isBlank(jobInfo.getJobDesc())) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_jobdesc")));
        }
        if (StringUtils.isBlank(jobInfo.getAuthor())) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_author")));
        }
        if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy") + I18nUtil.getString("system_unvalid")));
        }
        if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy") + I18nUtil.getString("system_unvalid")));
        }
        if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("jobinfo_field_gluetype") + I18nUtil.getString("system_unvalid")));
        }
        if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType()) && StringUtils.isBlank(jobInfo.getExecutorHandler())) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("system_please_input") + "JobHandler"));
        }

        // fix "\r" in shell
        if (GlueTypeEnum.GLUE_SHELL == GlueTypeEnum.match(jobInfo.getGlueType()) && jobInfo.getGlueSource() != null) {
            jobInfo.setGlueSource(jobInfo.getGlueSource().replaceAll("\r", ""));
        }

        // ChildJobId valid
        if (StringUtils.isNotBlank(jobInfo.getChildJobId())) {
            String[] childJobIds = StringUtils.split(jobInfo.getChildJobId(), ",");
            for (String childJobIdItem : childJobIds) {
                if (StringUtils.isNotBlank(childJobIdItem) && StringUtils.isNumeric(childJobIdItem)) {
                    XxlJobInfo childJobInfo = xxlJobInfoDao.loadById(Integer.valueOf(childJobIdItem));
                    if (childJobInfo == null) {
                        return new ReturnT<String>(ResultCodeMessage.FAILURE,
                                MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_not_found")), childJobIdItem));
                    }
                } else {
                    return new ReturnT<String>(ResultCodeMessage.FAILURE,
                            MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_unvalid")), childJobIdItem));
                }
            }
            jobInfo.setChildJobId(StringUtils.join(childJobIds, ","));
        }

        // add in db
        xxlJobInfoDao.save(jobInfo);
        if (jobInfo.getId() < 1) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("jobinfo_field_add") + I18nUtil.getString("system_fail")));
        }

        // add in quartz
        String qzGroup = String.valueOf(jobInfo.getJobGroup());
        String qzName = String.valueOf(jobInfo.getId());
        try {
            XxlJobDynamicScheduler.addJob(qzName, qzGroup, jobInfo.getJobCron());
            clearCache();
            //XxlJobDynamicScheduler.pauseJob(qzName, qzGroup);
            return ReturnT.SUCCESS;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            try {
                xxlJobInfoDao.delete(jobInfo.getId());
                XxlJobDynamicScheduler.removeJob(qzName, qzGroup);
            } catch (SchedulerException e1) {
                logger.error(e.getMessage(), e1);
            }
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("jobinfo_field_add") + I18nUtil.getString("system_fail")) + ":" + e.getMessage());
        }
    }

    @Override
    public ReturnT<String> update(XxlJobInfo jobInfo) {

        // valid
        if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, I18nUtil.getString("jobinfo_field_cron_unvalid"));
        }
        if (StringUtils.isBlank(jobInfo.getJobDesc())) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_jobdesc")));
        }
        if (StringUtils.isBlank(jobInfo.getAuthor())) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_author")));
        }
        if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy") + I18nUtil.getString("system_unvalid")));
        }
        if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy") + I18nUtil.getString("system_unvalid")));
        }

        // ChildJobId valid
        if (StringUtils.isNotBlank(jobInfo.getChildJobId())) {
            String[] childJobIds = StringUtils.split(jobInfo.getChildJobId(), ",");
            for (String childJobIdItem : childJobIds) {
                if (StringUtils.isNotBlank(childJobIdItem) && StringUtils.isNumeric(childJobIdItem)) {
                    XxlJobInfo childJobInfo = xxlJobInfoDao.loadById(Integer.valueOf(childJobIdItem));
                    if (childJobInfo == null) {
                        return new ReturnT<String>(ResultCodeMessage.FAILURE,
                                MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_not_found")), childJobIdItem));
                    }
                } else {
                    return new ReturnT<String>(ResultCodeMessage.FAILURE,
                            MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_unvalid")), childJobIdItem));
                }
            }
            jobInfo.setChildJobId(StringUtils.join(childJobIds, ","));
        }

        // stage job info
        XxlJobInfo existsJobinfo = xxlJobInfoDao.loadById(jobInfo.getId());
        if (existsJobinfo == null) {
            return new ReturnT<String>(ResultCodeMessage.FAILURE, (I18nUtil.getString("jobinfo_field_id") + I18nUtil.getString("system_not_found")));
        }
        //String old_cron = existsJobinfo.getJobCron();

        existsJobinfo.setJobCron(jobInfo.getJobCron());
        existsJobinfo.setJobDesc(jobInfo.getJobDesc());
        existsJobinfo.setAuthor(jobInfo.getAuthor());
        existsJobinfo.setAlarmEmail(jobInfo.getAlarmEmail());
        existsJobinfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
        existsJobinfo.setExecutorHandler(jobInfo.getExecutorHandler());
        existsJobinfo.setExecutorParam(jobInfo.getExecutorParam());
        existsJobinfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        existsJobinfo.setExecutorTimeout(jobInfo.getExecutorTimeout());
        existsJobinfo.setExecutorFailRetryCount(jobInfo.getExecutorFailRetryCount());
        existsJobinfo.setChildJobId(jobInfo.getChildJobId());
        xxlJobInfoDao.update(existsJobinfo);

        // fresh quartz
        String qzGroup = String.valueOf(existsJobinfo.getJobGroup());
        String qzName = String.valueOf(existsJobinfo.getId());
        try {
            boolean ret = XxlJobDynamicScheduler.rescheduleJob(qzGroup, qzName, existsJobinfo.getJobCron());
            clearCache();
            return ret ? ReturnT.SUCCESS : ReturnT.FAIL;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }

        return ReturnT.FAIL;
    }

    @Override
    public ReturnT<String> remove(int id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
        String group = String.valueOf(xxlJobInfo.getJobGroup());
        String name = String.valueOf(xxlJobInfo.getId());

        try {
            XxlJobDynamicScheduler.removeJob(name, group);
            xxlJobInfoDao.delete(id);
            xxlJobLogDao.delete(id);
            xxlJobLogGlueDao.deleteByJobId(id);
            clearCache();
            return ReturnT.SUCCESS;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
        return ReturnT.FAIL;
    }

    @Override
    public ReturnT<String> pause(int id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
        String group = String.valueOf(xxlJobInfo.getJobGroup());
        String name = String.valueOf(xxlJobInfo.getId());

        try {
            boolean ret = XxlJobDynamicScheduler.pauseJob(name, group);    // jobStatus do not store
            return ret ? ReturnT.SUCCESS : ReturnT.FAIL;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return ReturnT.FAIL;
        }
    }

    @Override
    public ReturnT<String> resume(int id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
        String group = String.valueOf(xxlJobInfo.getJobGroup());
        String name = String.valueOf(xxlJobInfo.getId());

        try {
            boolean ret = XxlJobDynamicScheduler.resumeJob(name, group);
            return ret ? ReturnT.SUCCESS : ReturnT.FAIL;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return ReturnT.FAIL;
        }
    }

    public void clearCache() {
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + "jobadmin_dashboardInfo";
        sysCommonService.delete(key);
    }

    @Override
    public JSONObject dashboardInfo() {
        JSONObject map = null;
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + "jobadmin_dashboardInfo";
        String redisCache = sysCommonService.getCache(key);
        if (StringUtil.isNotBlank(redisCache)) {
            map = JSONObject.parseObject(redisCache);
        }
        if (map != null && map.size() > 0) {
            return map;
        }
        int jobInfoCount = xxlJobInfoDao.findAllCount();
        int jobLogCount = xxlJobLogDao.triggerCountByHandleCode(-1);
        int jobLogSuccessCount = xxlJobLogDao.triggerCountByHandleCode(ResultCodeMessage.SUCCESS);

        // executor count
        Set<String> executerAddressSet = new HashSet<String>();
        List<XxlJobGroup> groupList = xxlJobGroupDao.findAll();
        if (groupList != null) {
            for (XxlJobGroup group : groupList) {
                if (group.getRegistryList() != null && group.getRegistryList().size() > 0) {
                    executerAddressSet.addAll(group.getRegistryList());
                }
            }
        }
        int executorCount = executerAddressSet.size();
        map = new JSONObject();
        map.put("jobInfoCount", jobInfoCount);
        map.put("jobLogCount", jobLogCount);
        map.put("jobLogSuccessCount", jobLogSuccessCount);
        map.put("executorCount", executorCount);
        sysCommonService.save(key, JSON.toJSONString(map), 60 * 5);
        return map;
    }


    @Override
    public ReturnT<JSONObject> chartInfo(Date startDate, Date endDate) {
        JSONObject map = null;
        endDate = DateUtil.afterDayDateTime(endDate, 1);
        String startDateStr = DateUtil.formatDate(startDate, "yyyy-MM-dd");
        String endDateStr = DateUtil.formatDate(endDate, "yyyy-MM-dd");
        String cacheKey = ConstantUtil.CACHE_SYS_BASE_DATA_ + "_" + startDateStr + "_" + endDateStr;
        String redisCache = sysCommonService.getCache(cacheKey);
        if (StringUtil.isNotBlank(redisCache)) {
            map = JSONObject.parseObject(redisCache);
        }
        if (map != null && map.size() > 0) {
            new ReturnT<JSONObject>(map);
        }
        // process
        List<String> triggerDayList = new ArrayList<String>();
        List<Integer> triggerDayCountRunningList = new ArrayList<Integer>();
        List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
        List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
        int triggerCountRunningTotal = 0;
        int triggerCountSucTotal = 0;
        int triggerCountFailTotal = 0;
        List<Map<String, Object>> triggerCountMapAll = xxlJobLogDao.triggerCountByDay(startDateStr, endDateStr);
        if (triggerCountMapAll != null && triggerCountMapAll.size() > 0) {
            for (Map<String, Object> item : triggerCountMapAll) {
                String day = String.valueOf(item.get("triggerDay"));
                int triggerDayCount = Integer.valueOf(String.valueOf(item.get("triggerDayCount")));
                int triggerDayCountRunning = Integer.valueOf(String.valueOf(item.get("triggerDayCountRunning")));
                int triggerDayCountSuc = Integer.valueOf(String.valueOf(item.get("triggerDayCountSuc")));
                int triggerDayCountFail = triggerDayCount - triggerDayCountRunning - triggerDayCountSuc;

                triggerDayList.add(day);
                triggerDayCountRunningList.add(triggerDayCountRunning);
                triggerDayCountSucList.add(triggerDayCountSuc);
                triggerDayCountFailList.add(triggerDayCountFail);

                triggerCountRunningTotal += triggerDayCountRunning;
                triggerCountSucTotal += triggerDayCountSuc;
                triggerCountFailTotal += triggerDayCountFail;
            }
        } else {
            for (int i = 4; i > -1; i--) {
                triggerDayList.add(FastDateFormat.getInstance("yyyy-MM-dd").format(DateUtils.addDays(new Date(), -i)));
                triggerDayCountSucList.add(0);
                triggerDayCountFailList.add(0);
            }
        }
        map = new JSONObject();
        map.put("triggerDayList", triggerDayList);
        map.put("triggerDayCountRunningList", triggerDayCountRunningList);
        map.put("triggerDayCountSucList", triggerDayCountSucList);
        map.put("triggerDayCountFailList", triggerDayCountFailList);

        map.put("triggerCountRunningTotal", triggerCountRunningTotal);
        map.put("triggerCountSucTotal", triggerCountSucTotal);
        map.put("triggerCountFailTotal", triggerCountFailTotal);
        sysCommonService.save(cacheKey, map.toString(), 60 * 5);
        return new ReturnT<JSONObject>(map);
    }
}
