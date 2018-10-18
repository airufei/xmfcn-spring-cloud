<!DOCTYPE html>
<html>
<head>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "jobinfo" />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>${I18n.jobinfo_name}</h1>
		</section>
		
		<!-- Main content -->
	    <section class="content">
	    
	    	<div class="row">
	    		<div class="col-xs-3">
	              	<div class="input-group">
	                	<span class="input-group-addon">${I18n.jobinfo_field_jobgroup}</span>
                		<select class="form-control" id="jobGroup" >
                			<#list JobGroupList as group>
                				<option value="${group.id}" <#if jobGroup==group.id>selected</#if> >${group.title}</option>
                			</#list>
	                  	</select>
	              	</div>
	            </div>
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">${I18n.jobinfo_field_jobdesc}</span>
                        <input type="text" class="form-control" id="jobDesc" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">JobHandler</span>
                        <input type="text" class="form-control" id="executorHandler" autocomplete="on" >
                    </div>
                </div>
	            <div class="col-xs-1">
	            	<button class="btn btn-block btn-info" id="searchBtn">${I18n.system_search}</button>
	            </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-success add" type="button">${I18n.jobinfo_field_add}</button>
	            </div>
          	</div>
	    	
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-body" >
			              	<table id="job_list" class="table table-striped table-hover table-condensed"
                                   width="100%" style="white-space: nowrap;">
				                <thead>
					            	<tr>
					            		<th name="id" >${I18n.jobinfo_field_id}</th>
					                	<th name="jobGroup" >${I18n.jobinfo_field_jobgroup}</th>
					                  	<th name="jobDesc" >${I18n.jobinfo_field_jobdesc}</th>
                                        <th name="glueType" >${I18n.jobinfo_field_gluetype}</th>
					                  	<th name="executorParam" >${I18n.jobinfo_field_executorparam}</th>
                                        <th name="jobCron" >Cron</th>
					                  	<th name="addTime" >addTime</th>
					                  	<th name="updateTime" >updateTime</th>
					                  	<th name="author" >${I18n.jobinfo_field_author}</th>
					                  	<th name="alarmEmail" >${I18n.jobinfo_field_alarmemail}</th>
					                  	<th name="jobStatus" >${I18n.system_status}</th>
					                  	<th>${I18n.system_opt}</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
				                <tfoot></tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
	    </section>
	</div>
	
	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<!-- job新增.模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document" id="modalDialog">
		<div class="modal-content">
			<div class="modal-header">
                <table width="99%">
                    <tr>
                        <td style="width: 15%">
                            <h4 class="modal-title">保存-${I18n.jobinfo_field_add}</h4>
                        </td>
                        <td style="width: 77%">
                        </td>
                        <td style="width: 8%">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
						<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_jobgroup}<font color="red">*</font></label>
						<div class="col-sm-4">
							<select class="form-control" name="jobGroup" >
		            			<#list JobGroupList as group>
		            				<option value="${group.id}" <#if jobGroup==group.id>selected</#if> >${group.title}</option>
		            			</#list>
		                  	</select>
						</div>
                        <label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_jobdesc}<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="jobDesc" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_jobdesc}" maxlength="50" ></div>
					</div>
                    <div class="form-group">
                        <label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorRouteStrategy}<font color="red">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="executorRouteStrategy" >
							<#list ExecutorRouteStrategyEnum as item>
                                <option value="${item}" >${item.title}</option>
							</#list>
                            </select>
                        </div>
                        <label for="lastname" class="col-sm-2 control-label">Cron<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="jobCron" placeholder="${I18n.system_please_input}Cron" maxlength="128" ></div>
                    </div>
                    <div class="form-group">
                        <label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_gluetype}<font color="red">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control glueType" name="glueType" >
								<#list GlueTypeEnum as item>
									<option value="${item}" >${item.desc}</option>
								</#list>
                            </select>
                        </div>
                        <label for="firstname" class="col-sm-2 control-label">JobHandler<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="executorHandler" placeholder="${I18n.system_please_input}JobHandler" maxlength="100" ></div>
                    </div>
                    <div class="form-group">
                        <label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorparam}<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="executorParam" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_executorparam}" maxlength="512" ></div>
                        <label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_childJobId}<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="childJobId" placeholder="${I18n.jobinfo_field_childJobId_placeholder}" maxlength="100" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_timeout}<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="executorTimeout" placeholder="${I18n.jobinfo_field_executorTimeout_placeholder}" maxlength="6" ></div>
                        <label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorBlockStrategy}<font color="red">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="executorBlockStrategy" >
								<#list ExecutorBlockStrategyEnum as item>
                                    <option value="${item}" >${item.title}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorFailRetryCount}<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="executorFailRetryCount" placeholder="${I18n.jobinfo_field_executorFailRetryCount_placeholder}" maxlength="4" ></div>
                        <label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_alarmemail}<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="alarmEmail" placeholder="${I18n.jobinfo_field_alarmemail_placeholder}" maxlength="100" ></div>
                    </div>
					<div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_author}<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="author" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_author}" maxlength="50" ></div>
					</div>

                    <hr>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
                            <input type="hidden" name="id">
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>
<@netCommon.commonScript />
<!-- DataTables -->
<script src="/jobadmin/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="/jobadmin/plugins/jquery/jquery.validate.min.js"></script>
<!-- moment -->
<script src="/jobadmin/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script src="/jobadmin/js/jobinfo.index.1.js"></script>
<script src="/jobadmin/adminlte/plugins/jQuery/jquery-ui-1.9.2.custom.min.js"></script><#--//拖拽-->
<script>
    $(document).ready(function () {
        $("#modalDialog").draggable();		        //为模态对话框添加拖拽，拖拽区域只在顶部栏
        $("#modalDialog").draggable({handle: ".modal-header"});//为模态对话框添加拖拽
        $("#addModal").css("overflow", "hidden"); // 禁止模态对话框的半透明背景滚动
    })
</script>
</body>
</html>
