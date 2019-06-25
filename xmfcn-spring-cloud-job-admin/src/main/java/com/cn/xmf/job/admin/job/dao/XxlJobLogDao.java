package com.cn.xmf.job.admin.job.dao;

import com.cn.xmf.job.admin.core.model.XxlJobLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * job user
 * @author xuxueli 2016-1-12 18:03:06
 */
public interface XxlJobLogDao {
	
	public List<XxlJobLog> pageList(@Param("offset") int offset,
									@Param("pagesize") int pagesize,
									@Param("jobGroup") int jobGroup,
									@Param("jobId") int jobId,
									@Param("triggerTimeStart") Date triggerTimeStart,
									@Param("triggerTimeEnd") Date triggerTimeEnd,
									@Param("executorHandler") String executorHandler,
									@Param("logStatus") int logStatus);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("jobId") int jobId,
							 @Param("triggerTimeStart") Date triggerTimeStart,
							 @Param("triggerTimeEnd") Date triggerTimeEnd,
							 @Param("executorHandler") String executorHandler,
							 @Param("logStatus") int logStatus);
	
	public XxlJobLog load(@Param("id") int id);

	public int save(XxlJobLog xxlJobLog);

	public int updateTriggerInfo(XxlJobLog xxlJobLog);

	public int updateHandleInfo(XxlJobLog xxlJobLog);
	
	public int delete(@Param("jobId") int jobId);

	public int triggerCountByHandleCode(@Param("handleCode") int handleCode);

	public List<Map<String, Object>> triggerCountByDay(@Param("startDate") String startDate,
													   @Param("endDate") String endDate);

	public int clearLog(@Param("jobGroup") int jobGroup,
						@Param("jobId") int jobId,
						@Param("clearBeforeTime") Date clearBeforeTime,
						@Param("clearBeforeNum") int clearBeforeNum);

}
