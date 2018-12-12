$(function () {
    //查询表格数据
    var codeTableColumnTable = $("#codeTableColumn_table").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/codeTableColumn/pageList",
            type: "post",
            data: function (d) {
                var obj = {};
                obj.name = $('#name').val();
                obj.flag = $('#flag').val();
                obj.tableName = $('#tableName').val();
                obj.start = d.start;
                obj.length = d.length;
                return obj;
            }
        },
        "searching": false,
        "ordering": false,
        "scrollX": true,	// scroll x，close self-adaption
        "columns": [

            {
                "data": 'id',
                "visible": false,
                "width": '180'

            },
            {
                "data": 'tableId',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'comments',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'jdbcType',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'javaType',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'javaField',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'isPk',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'isNull',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'isInsert',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'isEdit',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'isList',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'isQuery',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'queryType',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'showType',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'dictType',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'settings',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'sort',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'updatetimestr',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'remark',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'tableName',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'isEditpage',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'isinsertrequiredfield',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'isupdaterequiredfield',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'isSort',
                 "visible": true,
                "width": '180',
                "sCellType":"input"
            }

        ],
        "language": {
            "sProcessing": I18n.dataTable_sProcessing,
            "sLengthMenu": I18n.dataTable_sLengthMenu,
            "sZeroRecords": I18n.dataTable_sZeroRecords,
            "sInfo": I18n.dataTable_sInfo,
            "sInfoEmpty": I18n.dataTable_sInfoEmpty,
            "sInfoFiltered": I18n.dataTable_sInfoFiltered,
            "sInfoPostFix": "",
            "sSearch": I18n.dataTable_sSearch,
            "sUrl": "",
            "sEmptyTable": I18n.dataTable_sEmptyTable,
            "sLoadingRecords": I18n.dataTable_sLoadingRecords,
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": I18n.dataTable_sFirst,
                "sPrevious": I18n.dataTable_sPrevious,
                "sNext": I18n.dataTable_sNext,
                "sLast": I18n.dataTable_sLast
            },
            "oAria": {
                "sSortAscending": I18n.dataTable_sSortAscending,
                "sSortDescending": I18n.dataTable_sSortDescending
            }
        }
    });

    // table data
    var tableData = {};

    // 查询按钮事件
    $('#searchBtn').on('click', function () {
        codeTableColumnTable.fnDraw();
    });


    // 操作按钮事件
    $("#codeTableColumn_table").on('click', '.job_operate', function () {
        var typeName;
        var url;
        var needFresh = false;
        var type = $(this).attr("_type");
        if ("codeTableColumn_del" == type) {
            typeName = I18n.system_opt_del;
            url = base_url + "/codeTableColumn/delete";
            needFresh = true;
        } else {
            return;
        }
        var id = $(this).parent('p').attr("id");
        layer.confirm(I18n.system_ok + typeName + '?', {
            icon: 3,
            title: I18n.system_tips,
            btn: [I18n.system_ok, I18n.system_cancel]
        }, function (index) {
            layer.close(index);
            $.ajax({
                type: 'POST',
                url: url,
                data: {
                    "id": id
                },
                dataType: "json",
                success: function (data) {
                    if (data.code === 200) {
                        layer.open({
                            title: I18n.system_tips,
                            btn: [I18n.system_ok],
                            content: typeName + I18n.system_success,
                            icon: '1',
                            end: function (layero, index) {
                                if (needFresh) {
                                    //window.location.reload();
                                    codeTableColumnTable.fnDraw(false);
                                }
                            }
                        });
                    } else {
                        layer.open({
                            title: I18n.system_tips,
                            btn: [I18n.system_ok],
                            content: (data.msg || typeName + I18n.system_fail),
                            icon: '2'
                        });
                    }
                }
            });
        });
    });


   //编辑按钮事件
    $("#codeTableColumn_table").on('click', '.job_operate', function () {
        var type = $(this).attr("_type");
        if ("codeTableColumn_save" == type) {
            edit(this);
        }
    });

    $("#codeTableColumn_table").on("dblclick","tr",function(){
        var tds=$(this).children();
        $.each(tds, function(i,val){
            var jqob=$(val);
            if(i < 1 || jqob.has('button').length ){return true;}//跳过第1项 序号,按钮
            var txt=jqob.text();
            var put=$("<input type='text'>");
            put.val(txt);
            jqob.html(put);
        });
        //$(this).html("保存");
        //$(this).toggleClass("edit-btn");
       // $(this).toggleClass("save-btn");
    });

    //处理编辑页面的数据
    function edit(target) {
        var id = $(target).parent('p').attr("id");
        if(id==null||id==undefined||id<0) {
            id = $(target).children('td').children('p').attr("id");
        }
        var row = tableData['key' + id];
        if (row != null && row != undefined) {
           $("#addModal .form input[name='id']").val(row.id);
           $("#addModal .form input[name='tableId']").val(row.tableId);
           $("#addModal .form input[name='name']").val(row.name);
           $("#addModal .form input[name='comments']").val(row.comments);
           $("#addModal .form input[name='jdbcType']").val(row.jdbcType);
           $("#addModal .form input[name='javaType']").val(row.javaType);
           $("#addModal .form input[name='javaField']").val(row.javaField);
           $("#addModal .form input[name='isPk']").val(row.isPk);
           $("#addModal .form input[name='isNull']").val(row.isNull);
           $("#addModal .form input[name='isInsert']").val(row.isInsert);
           $("#addModal .form input[name='isEdit']").val(row.isEdit);
           $("#addModal .form input[name='isList']").val(row.isList);
           $("#addModal .form input[name='isQuery']").val(row.isQuery);
           $("#addModal .form input[name='queryType']").val(row.queryType);
           $("#addModal .form input[name='showType']").val(row.showType);
           $("#addModal .form input[name='dictType']").val(row.dictType);
           $("#addModal .form input[name='settings']").val(row.settings);
           $("#addModal .form input[name='updatetime']").val(row.updatetime);
           $("#addModal .form input[name='flag']").val(row.flag);
           $("#addModal .form input[name='tableName']").val(row.tableName);
           $("#addModal .form input[name='isEditpage']").val(row.isEditpage);
           $("#addModal .form input[name='isinsertrequiredfield']").val(row.isinsertrequiredfield);
           $("#addModal .form input[name='isupdaterequiredfield']").val(row.isupdaterequiredfield);
           $("#addModal .form input[name='isSort']").val(row.isSort);
          $("#addModal .form input[name='id']").val(row.id);
        }
        // show
        $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
     }

    // 添加页面按钮事件
    $(".add").click(function () {
         edit(this);
    });

    //验证保存数据并且提交
    var addModalValidate = $("#addModal .form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: true,
        rules: {
            id: {
                required: true
            },
            tableId: {
                required: true
            },
            name: {
                required: true
            },
            comments: {
                required: true
            },
            jdbcType: {
                required: true
            },
            javaType: {
                required: true
            },
            javaField: {
                required: true
            },
            isPk: {
                required: true
            },
            isNull: {
                required: true
            },
            isInsert: {
                required: true
            },
            isEdit: {
                required: true
            },
            isList: {
                required: true
            },
            isQuery: {
                required: true
            },
            queryType: {
                required: true
            },
            showType: {
                required: true
            },
            dictType: {
                required: true
            },
            settings: {
                required: true
            },
            updatetime: {
                required: true
            },
            flag: {
                required: true
            },
            tableName: {
                required: true
            },
            isEditpage: {
                required: true
            },
            isinsertrequiredfield: {
                required: true
            },
            isupdaterequiredfield: {
                required: true
            },
            isSort: {
                required: true
            },
        },
        messages: {
            id: {
                required: I18n.system_please_input + "编号"
            },
            tableId: {
                required: I18n.system_please_input + "归属表编号"
            },
            name: {
                required: I18n.system_please_input + "名称"
            },
            comments: {
                required: I18n.system_please_input + "描述"
            },
            jdbcType: {
                required: I18n.system_please_input + "列的数据类型的字节长度"
            },
            javaType: {
                required: I18n.system_please_input + "JAVA类型"
            },
            javaField: {
                required: I18n.system_please_input + "JAVA字段名"
            },
            isPk: {
                required: I18n.system_please_input + "是否主键"
            },
            isNull: {
                required: I18n.system_please_input + "是否可为空"
            },
            isInsert: {
                required: I18n.system_please_input + "是否为插入字段"
            },
            isEdit: {
                required: I18n.system_please_input + "是否编辑字段"
            },
            isList: {
                required: I18n.system_please_input + "是否列表字段"
            },
            isQuery: {
                required: I18n.system_please_input + "是否查询字段"
            },
            queryType: {
                required: I18n.system_please_input + "查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）"
            },
            showType: {
                required: I18n.system_please_input + "字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）"
            },
            dictType: {
                required: I18n.system_please_input + "字典类型"
            },
            settings: {
                required: I18n.system_please_input + "其它设置（扩展字段JSON）"
            },
            updatetime: {
                required: I18n.system_please_input + "更新时间"
            },
            flag: {
                required: I18n.system_please_input + "删除标记（0：正常；1：删除）"
            },
            tableName: {
                required: I18n.system_please_input + "表名称"
            },
            isEditpage: {
                required: I18n.system_please_input + "编辑字段"
            },
            isinsertrequiredfield: {
                required: I18n.system_please_input + "插入必须字段 1 非必须0"
            },
            isupdaterequiredfield: {
                required: I18n.system_please_input + "插入必须字段 1 非必须0"
            },
            isSort: {
                required: I18n.system_please_input + "是否排序 1排序，0不排序"
            },
        },
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success: function (label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement: function (error, element) {
            element.parent('div').append(error);
        },
        submitHandler: function (form) {
            $.post(base_url + "/codeTableColumn/save", $("#addModal .form").serialize(), function (data, status) {
                if (data.code ==200) {
                    $('#addModal').modal('hide');
                    layer.open({
                        title: I18n.system_tips,
                        btn: [I18n.system_ok],
                        content: I18n.system_add_suc,
                        icon: '1',
                        end: function (layero, index) {
                            codeTableColumnTable.fnDraw();
                        }
                    });
                } else {
                    layer.open({
                        title: I18n.system_tips,
                        btn: [I18n.system_ok],
                        content: (data.msg || I18n.system_add_fail),
                        icon: '2'
                    });
                }
            });
        }
    });
    //重置编辑页面数据
    $("#addModal").on('hide.bs.modal', function () {
        $("#addModal .form")[0].reset();
        addModalValidate.resetForm();
        $("#addModal .form .form-group").removeClass("has-error");
        $(".remote_panel").show();	// remote

    });
});