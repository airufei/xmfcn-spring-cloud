<!DOCTYPE html>
<html>
<head>
  	 <#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="/job/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <title>代码生成方案管理</title>
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
            <h1>代码生成方案</h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">名称</span>
                        <input type="text" class="form-control" id="name" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">表名</span>
                        <input type="text" class="form-control" id="tableName" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">备注信息</span>
                        <input type="text" class="form-control" id="remark" autocomplete="on">
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
                            <table id="codeScheme_table" class="table table-striped table-hover table-condensed" width="100%" style="white-space: nowrap;">
                                <thead>
                                <tr>
                                    <th name="id">编号</th>
                                    <th name="name">名称</th>
                                    <th name="category">分类</th>
                                    <th name="packageName">生成包路径</th>
                                    <th name="moduleName">生成模块名</th>
                                    <th name="subModuleName">生成子模块名</th>
                                    <th name="functionName">生成功能名</th>
                                    <th name="functionNameSimple">生成功能名（简写）</th>
                                    <th name="functionAuthor">生成功能作者</th>
                                    <th name="tableName">表名</th>
                                    <th name="tableId">生成表编号</th>
                                    <th name="updatetimestr">更新时间</th>
                                    <th name="remark">备注信息</th>
                                    <th name="modulePageName">页面模块</th>
                                    <th name="subPageName">子模块</th>
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
                            <h4 class="modal-title">代码生成方案</h4>
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
                <form class="form-horizontal form"  role="form">
                    <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">名称<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="name" placeholder="请输入..."
                           maxlength="200"></div>
                    </div>
                    <div class="form-group">
                        <label for="category" class="col-sm-2 control-label">分类<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="category" placeholder="请输入..."
                           maxlength="2000"></div>
                    </div>
                    <div class="form-group">
                        <label for="packageName" class="col-sm-2 control-label">生成包路径<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="packageName" placeholder="请输入..."
                           maxlength="500"></div>
                    </div>
                    <div class="form-group">
                        <label for="moduleName" class="col-sm-2 control-label">生成模块名<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="moduleName" placeholder="请输入..."
                           maxlength="30"></div>
                    </div>
                    <div class="form-group">
                        <label for="subModuleName" class="col-sm-2 control-label">生成子模块名<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="subModuleName" placeholder="请输入..."
                           maxlength="30"></div>
                    </div>
                    <div class="form-group">
                        <label for="functionName" class="col-sm-2 control-label">生成功能名<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="functionName" placeholder="请输入..."
                           maxlength="500"></div>
                    </div>
                    <div class="form-group">
                        <label for="functionNameSimple" class="col-sm-2 control-label">生成功能名（简写）<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="functionNameSimple" placeholder="请输入..."
                           maxlength="100"></div>
                    </div>
                    <div class="form-group">
                        <label for="functionAuthor" class="col-sm-2 control-label">生成功能作者<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="functionAuthor" placeholder="请输入..."
                           maxlength="100"></div>
                    </div>
                    <div class="form-group">
                        <label for="tableName" class="col-sm-2 control-label">表名<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="tableName" placeholder="请输入..."
                           maxlength="100"></div>
                    </div>
                    <div class="form-group">
                        <label for="remark" class="col-sm-2 control-label">备注信息<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="remark" placeholder="请输入..."
                           maxlength="255"></div>
                    </div>
                    <div class="form-group">
                        <label for="modulePageName" class="col-sm-2 control-label">页面模块<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="modulePageName" placeholder="请输入..."
                           maxlength="100"></div>
                    </div>
                    <div class="form-group">
                        <label for="subPageName" class="col-sm-2 control-label">子模块<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="subPageName" placeholder="请输入..."
                           maxlength="50"></div>
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
<script src="/job/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/job/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="/job/plugins/jquery/jquery.validate.min.js"></script>
<!-- moment -->
<script src="/job/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script src="/job/adminlte/plugins/jQuery/jquery-ui-1.9.2.custom.min.js"></script>//拖拽
<script src="/job/js/codeScheme.index.js"></script>

<script>
    $(document).ready(function () {
        $("#modalDialog").draggable();		        //为模态对话框添加拖拽，拖拽区域只在顶部栏
        $("#modalDialog").draggable({handle: ".modal-header"});//为模态对话框添加拖拽
        $("#addModal").css("overflow", "hidden"); // 禁止模态对话框的半透明背景滚动
    })
</script>
</body>
</html>