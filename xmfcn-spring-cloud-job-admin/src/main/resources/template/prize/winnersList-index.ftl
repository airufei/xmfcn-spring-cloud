<!DOCTYPE html>
<html>
<head>
    <#import "/common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <title>获奖名单管理</title>
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
            <h1>获奖名单</h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">奖品类型</span>
                        <input type="text" class="form-control" id="type" autocomplete="on">
                    </div>
                </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">奖品名称</span>
                        <input type="text" class="form-control" id="name" autocomplete="on">
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">用户ID</span>
                        <input type="text" class="form-control" id="userId" autocomplete="on">
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
                            <table id="winnersList_table" class="table table-striped table-hover table-condensed"
                                   width="100%" style="white-space: nowrap;">
                                <thead>
                                <tr>
                                    <th name="id">id</th>
                                    <th name="type">奖品类型</th>
                                    <th name="imgUrl">奖品url</th>
                                    <th name="userId">用户ID</th>
                                    <th name="nickname">昵称</th>
                                    <th name="photourl">头像</th>
                                    <th name="createtime">创建时间</th>
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
<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-lg" role="document" id="modalDialog">
        <div class="modal-content">
            <div class="modal-header">
                <table width="99%">
                    <tr>
                        <td style="width: 15%">
                            <h4 class="modal-title">保存获奖名单</h4>
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
                <form class="form-horizontal form" role="form">
                    <div class="form-group">
                        <label for="type" class="col-sm-2 control-label">奖品类型<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" disabled="true" name="type"
                                                      placeholder="请输入..."
                                                      maxlength="50"></div>
                    </div>
                    <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">奖品名称<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" disabled="true" name="name"
                                                      placeholder="请输入..."
                                                      maxlength="50"></div>
                    </div>
                    <div class="form-group">
                        <label for="imgUrl" class="col-sm-2 control-label">奖品url<font color="red">*</font></label>

                        <div class="col-sm-10">
                            <img src="${imgUrl}" width="100px" height="100px"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userId" class="col-sm-2 control-label">用户ID<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" disabled="true" name="userId"
                                                      placeholder="请输入..."
                                                      maxlength="11"></div>
                    </div>
                    <div class="form-group">
                        <label for="nickname" class="col-sm-2 control-label">昵称<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" disabled="true" name="nickname"
                                                      placeholder="请输入..."
                                                      maxlength="100"></div>
                    </div>
                    <div class="form-group">
                        <label for="photourl" class="col-sm-2 control-label">头像<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" disabled="true" name="photourl"
                                                      placeholder="请输入..."
                                                      maxlength="500"></div>
                    </div>
                    <div class="form-group">
                        <label for="remark" class="col-sm-2 control-label">用户ID<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" disabled="true" name="remark"
                                                      placeholder="请输入..."
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
<script src="/jobadmin/adminlte/plugins/jQuery/jquery-ui-1.9.2.custom.min.js"></script>
//拖拽
<script src="/jobadmin/js/winnersList.index.js"></script>

<script>
    $(document).ready(function () {
        $("#modalDialog").draggable();		        //为模态对话框添加拖拽，拖拽区域只在顶部栏
        $("#modalDialog").draggable({handle: ".modal-header"});//为模态对话框添加拖拽
        $("#addModal").css("overflow", "hidden"); // 禁止模态对话框的半透明背景滚动
    })
</script>
</body>
</html>