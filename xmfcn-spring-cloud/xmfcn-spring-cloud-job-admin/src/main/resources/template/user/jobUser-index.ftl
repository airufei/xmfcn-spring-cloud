<!DOCTYPE html>
<html>
<head>
  	 <#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <title>调度系统用户管理</title>
</head>
<body class="hold-transition skin-blue sidebar-mini  <#if cookieMap?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
    <!-- header -->
	<@netCommon.commonHeader />
    <!-- left -->
	<@netCommon.commonLeft "${className}" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>调度系统用户</h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">账号</span>
                        <input type="text" class="form-control" id="username" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">手机</span>
                        <input type="text" class="form-control" id="phone" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">微信号</span>
                        <input type="text" class="form-control" id="wechart" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-1">
                    <button class="btn btn-block btn-info" id="searchBtn">搜索</button>
                </div>

                <div class="col-xs-2">
                    <button class="btn btn-block btn-success add" type="button">新增</button>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body">
                            <table id="jobUser_table" class="table table-striped table-hover table-condensed" width="100%" style="white-space: nowrap;">
                                <thead>
                                <tr>
                                    <th name="id">id</th>
                                    <th name="username">账号</th>
                                    <th name="age">年龄</th>
                                    <th name="email">邮箱</th>
                                    <th name="phone">手机</th>
                                    <th name="address">地址</th>
                                    <th name="qq">QQ</th>
                                    <th name="wechart">微信号</th>
                                    <th name="updatetimestr">更新时间</th>
                                    <th name="operate">操作</th>
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
<div class="modal fade" id="addModal" tabindex="-1"  role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-lg" role="document"  id="modalDialog">
        <div class="modal-content">
            <div class="modal-header">
                <table width="99%">
                    <tr>
                        <td style="width: 15%">
                            <h4 class="modal-title">系统用户</h4>
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
                        <label for="username" class="col-sm-2 control-label">账号<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="username" placeholder="请输入..."
                                                      maxlength="20"></div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">密码<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="password" class="form-control" name="password" placeholder="请输入..."
                                                      maxlength="20"></div>
                    </div>
                    <div class="form-group">
                        <label for="age" class="col-sm-2 control-label">年龄<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="age" placeholder="请输入..."
                                                      maxlength="11"></div>
                    </div>
                    <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">邮箱<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="email" placeholder="请输入..."
                                                      maxlength="50"></div>
                    </div>
                    <div class="form-group">
                        <label for="phone" class="col-sm-2 control-label">手机<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="phone" placeholder="请输入..."
                                                      maxlength="11"></div>
                    </div>
                    <div class="form-group">
                        <label for="address" class="col-sm-2 control-label">地址<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="address" placeholder="请输入..."
                                                      maxlength="50"></div>
                    </div>
                    <div class="form-group">
                        <label for="qq" class="col-sm-2 control-label">QQ<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="qq" placeholder="请输入..."
                                                      maxlength="12"></div>
                    </div>
                    <div class="form-group">
                        <label for="wechart" class="col-sm-2 control-label">微信号<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="wechart" placeholder="请输入..."
                                                      maxlength="35"></div>
                    </div>
                    <div class="form-group">
                        <label for="remark" class="col-sm-2 control-label">备注<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="remark" placeholder="请输入..."
                                                      maxlength="200"></div>
                    </div>
                    <hr>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="submit" class="btn btn-primary">${I18n.system_save}</button>
                            <button type="button" class="btn btn-default"
                                    data-dismiss="modal">${I18n.system_cancel}</button>
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
<script src="/jobadmin/adminlte/plugins/jQuery/jquery-ui-1.9.2.custom.min.js"></script>//拖拽
<script src="/jobadmin/js/jobUser.index.js"></script>

<script>
    $(document).ready(function () {
        $("#modalDialog").draggable();		        //为模态对话框添加拖拽，拖拽区域只在顶部栏
        $("#modalDialog").draggable({handle: ".modal-header"});//为模态对话框添加拖拽
        $("#addModal").css("overflow", "hidden"); // 禁止模态对话框的半透明背景滚动
    })
</script>
</body>
</html>