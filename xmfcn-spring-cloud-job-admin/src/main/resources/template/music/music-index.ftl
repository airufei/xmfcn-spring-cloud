<!DOCTYPE html>
<html>
<head>
    <#import "/common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="/job/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <link rel="stylesheet" href="/job/js/upload/fileinput.min.css">
    <title>微信音乐管理</title>
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
            <h1>微信音乐</h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">类型</span>
                        <input type="text" class="form-control" id="type" autocomplete="on">
                    </div>
                </div>
                <div class="col-xs-3">

                    <div class="input-group">
                        <span class="input-group-addon">音乐名称</span>
                        <input type="text" class="form-control" id="title" autocomplete="on">
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
                            <table id="wxMusic_table" class="table table-striped table-hover table-condensed"
                                   width="100%" style="white-space: nowrap;">
                                <thead>
                                <tr>
                                    <th name="id">id</th>
                                    <th name="type">类型</th>
                                    <th name="title">音乐名称</th>
                                    <th name="url">音乐地址</th>
                                    <th name="updatetimestr">修改时间</th>
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
                            <h4 class="modal-title">保存微信音乐</h4>
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
                        <label for="type" class="col-sm-2 control-label">类型<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" id="save_type" class="form-control" name="type" placeholder="请输入..."
                                                      maxlength="50"></div>
                    </div>
                    <div class="form-group">
                        <label for="remark" class="col-sm-2 control-label">备注<font color="red">*</font></label>
                        <div class="col-sm-10"><input id="save_remark"  type="text" class="form-control" name="remark"
                                                      placeholder="请输入..."
                                                      maxlength="150"></div>
                    </div>
                    <div class="form-group">
                        <label for="file" class="col-sm-2 control-label">选择文件<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input id="uploadMusic" name="file" class="file" type="file" enctype="multipart/form-data"
                                   multiple data-min-file-count="1" data-theme="fas">
                        </div>
                    </div>
                    <hr>
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
<script src="/job/adminlte/plugins/jQuery/jquery-ui-1.9.2.custom.min.js"></script>
<script src="/job/js/upload/piexif.js"></script>
<script src="/job/js/upload/zh.js"></script>
<script src="/job/js/upload/fileinput.min.js"></script>
<script src="/job/js/music.index.js"></script>

<script>
    $(document).ready(function () {
        $("#modalDialog").draggable();		        //为模态对话框添加拖拽，拖拽区域只在顶部栏
        $("#modalDialog").draggable({handle: ".modal-header"});//为模态对话框添加拖拽
        $("#addModal").css("overflow", "hidden"); // 禁止模态对话框的半透明背景滚动
    });
    $("#uploadMusic").fileinput({
        uploadUrl: base_url + "/music/save", // server upload action
        language: 'zh',
        minFileCount: 0,
        uploadAsync: true,
        maxFileCount: 400,
        allowedFileExtensions: ['mp3'],//可以可选择的违建格式
        enctype: 'multipart/form-data',
        maxFileSize: 909600,//限制上传大小KB
        showBrowse: true,
        browseOnZoneClick: true,
        ajaxSettings: {
            contentType: false
        },
        uploadExtraData: function (previewId, index) {
            var data = {
                remark: $("#save_remark").val(),
                type: $("#save_type").val()
            };
            return data;
        }
    }).on("filepredelete", function (jqXHR) {
        var abort = true;
        if (confirm("确定删除此图片?")) {
            abort = false;
        }
        return abort; // 您还可以发送任何数据/对象，你可以接收` filecustomerror
    }).on('filebatchpreupload', function (event, data) {
        var n = data.files.length, files = n > 1 ? n + ' files' : 'one file';
        if (!window.confirm("确定上传选择的文件吗 ?")) {
            return {
                message: "上传失败!", // upload error message
                data: {} // any other data to send that can be referred in `filecustomerror`
            };
        }
    }).on('fileuploaded', function (event, data, id, index) {//上传成功之后的处理
        $('#addModal').modal('hide');
        alert("上传成功")
    }).on('filebatchpreupload', function (event, data, id, index) {
        $('#kv-success-1').html('<h4>上传状态</h4><ul></ul>').hide();
    })
</script>
</body>
</html>