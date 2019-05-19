<!DOCTYPE html>
<html>
<head>
    <#import "/common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <title>日志搜索</title>
</head>
<body class="hold-transition skin-blue sidebar-mini  <#if cookieMap?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
    <!-- header -->
    <@netCommon.commonHeader />
    <!-- left -->
    <@netCommon.commonLeft "log" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>日志搜索</h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">日志信息</span>
                        <input type="text" class="form-control" id="message" autocomplete="on">
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">日志标识</span>
                        <input type="text" class="form-control" id="traceId" autocomplete="on">
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">系统名称</span>
                        <select class="form-control" id="subSysName">
                            <option value="">--请选择--</option>
                            <#list sysList as sys>
                                <option value="${sys.subSysName}"
                                        <#if subSysName==group.subSysName>selected</#if> >${sys.subSysName}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">堆栈信息</span>
                        <input type="text" class="form-control" id="stackMessage" autocomplete="on">
                    </div>
                </div>
                <br>
                <br>
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">方法名称</span>
                        <input type="text" class="form-control" id="methodName" autocomplete="on">
                    </div>
                </div>

                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">日志级别</span>
                        <select class="form-control" id="level">
                            <option value="">--请选择--</option>
                            <option value="INFO">INFO</option>
                            <option value="ERROR">ERROR</option>
                            <option value="DEBUG">DEBUG</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-1">
                    <button class="btn btn-block btn-info" id="searchBtn">搜索</button>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box">
                            <div class="box-body">
                                <table id="log_table" class="table table-striped table-hover table-condensed"
                                       width="980px" style="white-space: nowrap;">
                                    <thead>
                                    <tr>
                                        <th name="id">id</th>
                                        <th name="sysIp">机器IP</th>
                                        <th name="subSysName">系统名称</th>
                                        <th name="moduleName">模块</th>
                                        <th name="traceId">日志标识</th>
                                        <th name="methodName">方法名称</th>
                                        <th name="message">日志信息</th>
                                        <th name="level">日志级别</th>
                                        <th name="time">时间</th>
                                        <th name="threadName">线程名称</th>
                                        <th name="stackMessage">堆栈信息</th>
                                        <th name="operate"></th>
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

<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-lg" role="document" id="modalDialog">
        <div class="modal-content">
            <div class="modal-header">
                <table width="99%">
                    <tr>
                        <td style="width: 15%">
                            <h4 class="modal-title">日志数据</h4>
                        </td>
                        <td style="width: 77%">
                        </td>
                        <td style="width: 8%">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="modal-body" style="overflow-y: scroll;height: 500px">
                <form class="form-horizontal form" role="form">
                    <div class="form-group">
                        <label for="id" class="col-sm-2 control-label">日志ID</label>
                        <div class="col-sm-10"><label   id="id_deatil"></label></div>
                    </div>
                    <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">机器IP</label>
                        <div class="col-sm-10"><label  id="sysIp"></label></div>
                    </div>
                    <div class="form-group">
                        <label for="subSysName" class="col-sm-2 control-label">系统名称</label>
                        <div class="col-sm-10"><label  id="subSysName_deatil"></label></div>
                    </div>
                    <div class="form-group">
                        <label for="moduleName" class="col-sm-2 control-label">模块名称</label>
                        <div class="col-sm-10"><label  id="moduleName"></label></div>
                    </div>
                    <div class="form-group">
                        <label for="methodName" class="col-sm-2 control-label">方法名称</label>
                        <div class="col-sm-10"><label  id="methodName__deatil"></label></div>
                    </div>

                    <div class="form-group">
                        <label for="traceId" class="col-sm-2 control-label">日志标识</label>
                        <div class="col-sm-10"><label  id="traceId__deatil"></label></div>
                    </div>
                    <div class="form-group">
                        <label for="message" class="col-sm-2 control-label">日志信息</label>
                        <div class="col-sm-10"><label  id="message__deatil"></label></div>
                    </div>
                    <div class="form-group">
                        <label for="time" class="col-sm-2 control-label">线程名称</label>
                        <div class="col-sm-10"><label  id="threadName"></label></div>
                    </div>
                    <div class="form-group">
                        <label for="level" class="col-sm-2 control-label">日志级别</label>
                        <div class="col-sm-10"><label  id="level__deatil"></label></div>
                    </div>
                    <div class="form-group">
                        <label for="time" class="col-sm-2 control-label">日志时间</label>
                        <div class="col-sm-10"><label   id="time"></label></div>
                    </div>
                    <div class="form-group">
                        <label for="time" class="col-sm-2 control-label">堆栈信息</label>
                        <div class="col-sm-10"><label  id="stackMessage"></label></div>
                    </div>
                    <hr>
                    <div class="form-group">
                        <div id="roletreeview"></div>
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
<script src="/jobadmin/adminlte/plugins/jQuery/jquery-ui-1.9.2.custom.min.js"></script>
//拖拽
<script src="/jobadmin/js/log.index.js"></script>

<script>
    $(document).ready(function () {
        $("#modalDialog").draggable();		        //为模态对话框添加拖拽，拖拽区域只在顶部栏
        $("#modalDialog").draggable({handle: ".modal-header"});//为模态对话框添加拖拽
        $("#addModal").css("overflow", "hidden"); // 禁止模态对话框的半透明背景滚动
    })
</script>
</body>
</html>