<!DOCTYPE html>
<html>
<head>
  	 <#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <title>redis运行监控</title>
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
            <h1>redis运行监控</h1>
        </section>
        <section class="content">
            <table style="width:98%; border-width: 1px" >
                <#assign length=0 />
                <#assign ban=0 />
                <#assign ban=ban/2 />
                <#list list as item>
                        <#assign length=length+1 /> <#--变量length复制-->
                        <#if length%2!=0>
                        <tr>
                        </#if>
                        <td style="text-align:right;"> <label
                                style="font-family:'Source Sans Pro', 'Helvetica Neue', Helvetica, Arial, sans-serif; color: #333;">${item.key}
                            :</label></td>
                        <td text-align:left;">
                            <label
                                    style="font-family:'Source Sans Pro', 'Helvetica Neue', Helvetica, Arial, sans-serif;color: #3300aa;">  ${item.value}</label>
                        </td>
                        <#if length%2==0>
                        </tr>
                        </#if>
                </#list>
            </table>
        </section>
    </div>

    <!-- footer -->
	<@netCommon.commonFooter />
</div>
 <@netCommon.commonScript />

</body>
</html>