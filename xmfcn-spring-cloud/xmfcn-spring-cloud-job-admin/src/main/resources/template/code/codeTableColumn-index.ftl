<!DOCTYPE html>
<html>
<head>
  	 <#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <title>表字段信息管理</title>
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
            <h1>表字段信息</h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">归属表编号</span>
                        <input type="text" class="form-control" id="tableId" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">名称</span>
                        <input type="text" class="form-control" id="name" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">描述</span>
                        <input type="text" class="form-control" id="comments" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">列的数据类型的字节长度</span>
                        <input type="text" class="form-control" id="jdbcType" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">JAVA类型</span>
                        <input type="text" class="form-control" id="javaType" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">JAVA字段名</span>
                        <input type="text" class="form-control" id="javaField" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">是否主键</span>
                        <input type="text" class="form-control" id="isPk" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">是否可为空</span>
                        <input type="text" class="form-control" id="isNull" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">是否为插入字段</span>
                        <input type="text" class="form-control" id="isInsert" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">是否编辑字段</span>
                        <input type="text" class="form-control" id="isEdit" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">是否列表字段</span>
                        <input type="text" class="form-control" id="isList" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">是否查询字段</span>
                        <input type="text" class="form-control" id="isQuery" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）</span>
                        <input type="text" class="form-control" id="queryType" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）</span>
                        <input type="text" class="form-control" id="showType" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">字典类型</span>
                        <input type="text" class="form-control" id="dictType" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">其它设置（扩展字段JSON）</span>
                        <input type="text" class="form-control" id="settings" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">排序（升序）</span>
                        <input type="text" class="form-control" id="sort" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">备注信息</span>
                        <input type="text" class="form-control" id="remark" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">表名称</span>
                        <input type="text" class="form-control" id="tableName" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">编辑字段</span>
                        <input type="text" class="form-control" id="isEditpage" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">插入必须字段 1 非必须0</span>
                        <input type="text" class="form-control" id="isinsertrequiredfield" autocomplete="on">
                    </div>
              </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">插入必须字段 1 非必须0</span>
                        <input type="text" class="form-control" id="isupdaterequiredfield" autocomplete="on">
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
                            <table id="codeTableColumn_table" class="table table-striped table-hover table-condensed" width="100%" style="white-space: nowrap;">
                                <thead>
                                <tr>
                                    <th name="name">名称</th>
                                    <th name="isPk">是否主键</th>
                                    <th name="createtime">创建时间</th>
                                    <th name="remark">备注信息</th>
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
                            <h4 class="modal-title">保存表字段信息</h4>
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
                        <label for="tableId" class="col-sm-2 control-label">归属表编号<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="tableId" placeholder="请输入..."
                           maxlength="64"></div>
                    </div>
                    <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">名称<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="name" placeholder="请输入..."
                           maxlength="200"></div>
                    </div>
                    <div class="form-group">
                        <label for="tableName" class="col-sm-2 control-label">表名称<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="tableName" placeholder="请输入..."
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
<script src="/jobadmin/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="/jobadmin/plugins/jquery/jquery.validate.min.js"></script>
<!-- moment -->
<script src="/jobadmin/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script src="/jobadmin/adminlte/plugins/jQuery/jquery-ui-1.9.2.custom.min.js"></script>//拖拽
<script src="/jobadmin/js/codeTableColumn.index.js"></script>

<script>
    $(document).ready(function () {
        $("#modalDialog").draggable();		        //为模态对话框添加拖拽，拖拽区域只在顶部栏
        $("#modalDialog").draggable({handle: ".modal-header"});//为模态对话框添加拖拽
        $("#addModal").css("overflow", "hidden"); // 禁止模态对话框的半透明背景滚动
    })
</script>
</body>
</html>