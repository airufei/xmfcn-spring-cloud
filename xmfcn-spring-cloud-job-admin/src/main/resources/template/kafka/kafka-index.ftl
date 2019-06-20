<!DOCTYPE html>
<html>
<head>
    <#import "/common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="/job/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <title>发送数据到kafka</title>
    <style>
        .login-button { /* 按钮美化 */
            width: 270px; /* 宽度 */
            height: 40px; /* 高度 */
            border-width: 0px; /* 边框宽度 */
            border-radius: 3px; /* 边框半径 */
            background: #1E90FF; /* 背景颜色 */
            cursor: pointer; /* 鼠标移入按钮范围时出现手势 */
            outline: none; /* 不显示轮廓线 */
            font-family: Microsoft YaHei; /* 设置字体 */
            color: white; /* 字体颜色 */
            font-size: 17px; /* 字体大小 */
        }
        .login-button:hover { /* 鼠标移入按钮范围时改变颜色 */
            background: #5599FF;
        }
    </style>
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
           <div align="center" style="margin-top: 20%"><input type="button" class="login-button"  onclick="showWind()" value="开始处理"></div>
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
                            <h4 class="modal-title">发送数据到kafka</h4>
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
                        <label for="kafkaData" class="col-sm-2 control-label">发送数据<font color="red">*</font></label>
                        <div class="col-sm-10"><textarea type="text" class="form-control" rows="8" name="kafkaData"
                                                         placeholder="请输入JSON格式的数据..."></textarea></div>
                    </div>
                    <div class="form-group">
                        <label for="topic" class="col-sm-2 control-label">主题topic<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="topic" placeholder="请输入kafka主题topic"  maxlength="100">
                        </div>
                    </div>
                    <hr color="red" />
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
<script src="/job/adminlte/plugins/jQuery/jquery-ui-1.9.2.custom.min.js"></script>
<script src="/job/js/kafka.index.js"></script>
<script>
    $(document).ready(function () {
        $("#modalDialog").draggable();		        //为模态对话框添加拖拽，拖拽区域只在顶部栏
        $("#modalDialog").draggable({handle: ".modal-header"});//为模态对话框添加拖拽
        $("#addModal").css("overflow", "hidden"); // 禁止模态对话框的半透明背景滚动
    })
    function showWind() {
        $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
    }
</script>
</body>
</html>