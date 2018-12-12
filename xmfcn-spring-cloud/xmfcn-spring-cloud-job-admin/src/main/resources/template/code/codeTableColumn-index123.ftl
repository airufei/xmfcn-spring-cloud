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
	<@netCommon.commonLeft "del" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <label>shift+鼠标滑轮上下可以实现左右滚动</label>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body" style="overflow-x: scroll;">
                            <table id="codeTableColumn_table" class="table table-striped table-hover table-condensed"
                                   width: 100%
                                   style="white-space: nowrap;">
                                <thead>
                                <tr>
                                    <th name="tableName">表名称</th>
                                    <th name="comments">字段简介</th>
                                    <th name="jdbcType">列的数据类型</th>
                                    <th name="javaType">JAVA类型</th>
                                    <th name="javaField">JAVA字段名</th>
                                    <th name="isPk">是否主键</th>
                                    <th name="isNull">是否可为空</th>
                                    <th name="isInsert">是否为插入字段</th>
                                    <th name="isEdit">是否编辑字段</th>
                                    <th name="isList">是否列表字段</th>
                                    <th name="isQuery">是否查询字段</th>
                                    <th name="queryType">查询方式</th>
                                    <th name="isEditpage">编辑字段</th>
                                    <th name="isinsertrequiredfield">插入必须字段</th>
                                    <th name="isupdaterequiredfield">插入必须字段</th>
                                    <th name="isSort">是否排序</th>
                                    <th name="sort">排序（升序）</th>
                                    <th name="showType">编辑页控件</th>
                                </tr>
                                </thead>
                                <form action="/jobadmin/codeTableColumn/saveList" method="post">
                                    <tbody>
                                <#list colist as  li>
                                <tr>
                                    <td name="tableName"><input type="text"  name="tableName"  value="${li.tableName}" readonly="readonly"/></td>
                                    <td name="comments"><input type="text" name="comments"  value="${li.comments}"/></td>
                                    <td name="jdbcType"><input type="text" name="jdbcType"  value="${li.jdbcType}" readonly="readonly"/></td>
                                    <td name="javaField"><input type="text" name="javaField"  value="${li.javaField}"/></td>
                                    <td name="javaType"> <select name="javaType" class="required input-mini" style="width:75px">
                                        <option value="java.math.BigDecimal" <#if javaType?? && javaType == "java.math.BigDecimal">selected</#if> title="java.math.BigDecimal">BigDecimal
                                        </option>
                                        <option value="java.lang.String" <#if javaType?? && javaType == "java.lang.String">selected</#if> title="java.lang.String">String
                                        </option>
                                        <option value="java.lang.Long" <#if javaType?? && javaType == "java.lang.Long">selected</#if> title="java.lang.Long">Long
                                        </option>
                                        <option value="java.lang.Integer" <#if javaType?? && javaType == "java.lang.Integer">selected</#if> title="java.lang.Integer">Integer
                                        </option>
                                        <option value="java.util.Date" <#if javaType?? && javaType == "java.util.Date">selected</#if> title="java.util.Date">DateTime
                                        </option>
                                    </select></td>
                                    <td name="isPk">
                                        <select name="isPk" class="required input-mini" style="width:75px">
                                            <option value="0" <#if isPk?? && isPk == "0">selected</#if> title="否">否
                                            </option>
                                            <option value="1" <#if isPk?? && isPk == "1">selected</#if> title="是">是
                                            </option>
                                        </select>
                                    </td>
                                    <td name="isNull">
                                        <select name="isNull" class="required input-mini" style="width:75px">
                                            <option value="0" <#if isNull?? && isNull == "0">selected</#if> title="否">
                                                否
                                            </option>
                                            <option value="1" <#if isNull?? && isNull == "1">selected</#if> title="是">
                                                是
                                            </option>
                                        </select>
                                    </td>
                                    <td name="isInsert">
                                        <select name="isInsert" class="required input-mini" style="width:75px">
                                            <option value="0" <#if isInsert?? && isInsert == "0">selected</#if>
                                                    title="否">否
                                            </option>
                                            <option value="1" <#if isInsert?? && isInsert == "1">selected</#if>
                                                    title="是">是
                                            </option>
                                        </select></td>
                                    <td name="isEdit"><select name="isEdit" class="required input-mini"
                                                              style="width:75px">
                                        <option value="0" <#if isEdit?? && isEdit == "0">selected</#if> title="否">否
                                        </option>
                                        <option value="1" <#if isEdit?? && isEdit == "1">selected</#if> title="是">是
                                        </option>
                                    </select></td>
                                    <td name="isList"><select name="isList" class="required input-mini"
                                                              style="width:75px">
                                        <option value="0" <#if isList?? && isList == "0">selected</#if> title="否">否
                                        </option>
                                        <option value="1" <#if isList?? && isList == "1">selected</#if> title="是">是
                                        </option>
                                    </select></td>
                                    <td name="isQuery"><select name="isQuery" class="required input-mini"
                                                               style="width:75px">
                                        <option value="0" <#if isQuery?? && isQuery == "0">selected</#if> title="否">否
                                        </option>
                                        <option value="1" <#if isQuery?? && isQuery == "1">selected</#if> title="是">是
                                        </option>
                                    </select></td>
                                    <td name="queryType"><select name="queryType" class="required input-mini"
                                                                 style="width:75px">
                                        <option value="0" <#if queryType?? && queryType == "0">selected</#if> title="否">
                                            否
                                        </option>
                                        <option value="1" <#if queryType?? && queryType == "1">selected</#if> title="是">
                                            是
                                        </option>
                                    </select></td>
                                    <td name="sort"><select name="sort" class="required input-mini" style="width:75px">
                                        <option value="0" <#if sort?? && sort == "0">selected</#if> title="否">否</option>
                                        <option value="1" <#if sort?? && sort == "1">selected</#if> title="是">是</option>
                                    </select></td>
                                    <td name="isEditpage"><select name="isEditpage" class="required input-mini"
                                                                  style="width:75px">
                                        <option value="0" <#if isEditpage?? && isEditpage == "0">selected</#if>
                                                title="否">否
                                        </option>
                                        <option value="1" <#if isEditpage?? && isEditpage == "1">selected</#if>
                                                title="是">是
                                        </option>
                                    </select></td>
                                    <td name="isinsertrequiredfield">
                                        <select name="isinsertrequiredfield"
                                                class="required input-mini"
                                                style="width:75px">
                                            <option value="0"
                                                <#if isinsertrequiredfield?? && isinsertrequiredfield == "0">selected</#if>title="否">
                                                否
                                            </option>
                                            <option value="1"
                                                <#if isinsertrequiredfield?? && isinsertrequiredfield == "1">selected</#if>
                                                    title="是">是
                                            </option>
                                        </select></td>
                                    <td name="isupdaterequiredfield"><select name="isupdaterequiredfield"
                                                                             class="required input-mini"
                                                                             style="width:75px">
                                        <option value="0"
                                                <#if isupdaterequiredfield?? && isupdaterequiredfield == "0">selected</#if>
                                                title="否">否
                                        </option>
                                        <option value="1"
                                                <#if isupdaterequiredfield?? && isupdaterequiredfield == "1">selected</#if>
                                                title="是">是
                                        </option>
                                    </select></td>
                                    <td name="isSort"><select name="isSort" class="required input-mini"
                                                              style="width:75px">
                                        <option value="0" <#if isSort?? && isSort == "0">selected</#if> title="否">否
                                        </option>
                                        <option value="1" <#if isSort?? && isSort == "1">selected</#if> title="是">是
                                        </option>
                                    </select></td>
                                    <td name="showType"><select name="showType" class="required input-mini"
                                                                style="width:75px">
                                        <option value="input" <#if showType?? && showType == "input">selected</#if> title="input">input
                                        </option>
                                        <option value="select" <#if showType?? && showType == "select">selected</#if> title="select">select
                                        </option>
                                        <option value="dateBox" <#if showType?? && showType == "dateBox">selected</#if> title="dateBox">dateBox
                                        </option>
                                        <option value="redioButton" <#if showType?? && showType == "redioButton">selected</#if> title="redioButton">redioButton
                                        </option>
                                    </select></td>

                                </tr>
                                </#list>
                                    <tr style="text-align: center;margin-top: 30px">
                                        <td colspan="8"> <button type="submit" class="btn btn-primary">${I18n.system_save}</button></td>
                                        <td><button type="button" class="btn btn-default"
                                                    data-dismiss="modal">${I18n.system_cancel}</button></button></td>
                                    </tr>

                                    </tbody>
                                </form>
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

 <@netCommon.commonScript />
<!-- DataTables -->
<script src="/jobadmin/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="/jobadmin/plugins/jquery/jquery.validate.min.js"></script>
<!-- moment -->
<script src="/jobadmin/adminlte/plugins/daterangepicker/moment.min.js"></script>
</body>
</html>