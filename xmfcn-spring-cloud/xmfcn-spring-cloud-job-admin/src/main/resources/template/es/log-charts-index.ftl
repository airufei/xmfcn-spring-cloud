<!DOCTYPE html>
<html>
<head>
    <#import "/common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <!-- daterangepicker -->
    <link rel="stylesheet" href="/jobadmin/adminlte/plugins/daterangepicker/daterangepicker.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if> ">
<div class="wrapper">
    <!-- header -->
    <@netCommon.commonHeader />
    <!-- left -->
    <@netCommon.commonLeft "index" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">
            <#-- 调度报表：时间区间筛选，左侧折线图 + 右侧饼图 -->
            <div class="row">
                <div class="col-md-12">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">日志数据报表</h3>
                            <#--<input type="text" class="form-control" id="filterTime" readonly >-->

                            <!-- tools box -->
                            <div class="pull-right box-tools">
                                <button type="button" class="btn btn-primary btn-sm daterange pull-right"
                                        data-toggle="tooltip" id="filterTime">
                                    <i class="fa fa-calendar"></i>
                                </button>
                            </div>
                            <!-- /. tools -->

                        </div>
                        <div class="box-body">
                            <div class="row">
                                <#-- 左侧折线图 -->
                                <div class="col-md-8">
                                    <div id="lineChart" style="height: 600px;"></div>
                                </div>
                                <#-- 右侧饼图 -->
                                <div class="col-md-4">
                                    <div id="pieChart" style="height: 600px;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- footer -->
    <@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
<!-- daterangepicker -->
<script src="/jobadmin/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script src="/jobadmin/adminlte/plugins/daterangepicker/daterangepicker.js"></script>
<#-- echarts -->
<script src="/jobadmin/plugins/echarts/echarts.common.min.js"></script>
<script src="/jobadmin/js/log.charts.index.js"></script>
</body>
</html>
