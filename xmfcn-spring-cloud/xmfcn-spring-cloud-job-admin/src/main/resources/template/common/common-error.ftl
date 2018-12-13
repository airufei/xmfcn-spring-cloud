<!DOCTYPE html>
<html>
<head>
  	 <#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <title>错误页面</title>
</head>
<body class="hold-transition skin-blue sidebar-mini  <#if cookieMap?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
    <!-- header -->
	<@netCommon.commonHeader />
    <!-- left -->
	<@netCommon.commonLeft/>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <section class="content-header">
            <h1>错误信息</h1>
        </section>
        <section class="content">
        <div style="color: red"> ${errorMsg}</div>
        </section>
        <section class="content" style="text-align: center">
            <a href="/jobadmin/">返回首页</a>
        </section>
    </div>

    <!-- footer -->
	<@netCommon.commonFooter />
</div>
 <@netCommon.commonScript />

</body>
</html>