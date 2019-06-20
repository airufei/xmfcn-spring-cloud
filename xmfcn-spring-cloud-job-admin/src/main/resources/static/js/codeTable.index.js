$(function () {
    //查询表格数据
    var codeTableTable = $("#codeTable_table").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/codeTable/pageList",
            type: "post",
            data: function (d) {
                var obj = {};
                obj.name = $('#name').val();
                obj.start = d.start;
                obj.length = d.length;
                return obj;
            }
        },
        "searching": false,
        "ordering": false,
        scrollX: true,
        //"scrollX": true,	// scroll x，close self-adaption
        "columns": [

            {
                "data": 'id',
                "visible": false,
                "width": '180'
            },
            {
                "data": 'name',
                "visible": true,
                "width": '180'
            },
            {
                "data": 'comments',
                "visible": true,
                "width": '180'
            },
            {
                "data": 'className',
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
                "data": I18n.system_opt,
                "width": '15%',
                "render": function (data, type, row) {
                    return function () {
                        tableData['key' + row.id] = row;
                        return '<p id="' + row.id + '" >' +
                            '<button class="btn btn-warning btn-xs job_operate" _type="codeTable_save"" type="button">编辑表</button>  ' +
                            '<button class="btn btn-danger btn-xs job_operate" _type="codeTable_save_col" type="button">编辑列</button>' +
                            '<button class="btn btn-danger btn-xs job_operate" _type="codeTable_del" type="button">' + I18n.system_opt_del + '</button>  ' +
                            '</p>';
                    };
                }
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
        codeTableTable.fnDraw();
    });


    // 操作按钮事件
    $("#codeTable_table").on('click', '.job_operate', function () {
        var typeName;
        var url;
        var needFresh = false;
        var type = $(this).attr("_type");
        var id = $(this).parent('p').attr("id");
        if ("codeTable_del" == type) {
            typeName = I18n.system_opt_del;
            url = base_url + "/codeTable/delete";
            needFresh = true;
            deleteById(id,typeName,url,needFresh);
        } else if ("codeTable_save" == type){
            edit(this);
        }else if ("codeTable_save_col" == type) {
            goToPage(this);
        }
    });

    //删除
    function deleteById(id,typeName,url,needFresh)
    {
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
                                    codeTableTable.fnDraw(false);
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
    }

    //双击弹出编辑
    $('#codeTable_table').on('dblclick', 'tr', function () {
        edit(this);
    });


    //跳转列编辑页面
    function goToPage(target) {
        var id = $(target).parent('p').attr("id");
        if (id == null || id == undefined || id < 0) {
            id = $(target).children('td').children('p').attr("id");
        }
        var row = tableData['key' + id];
        if (row != null && row != undefined) {
            tableName = row.name;
            var url = base_url + "/codeTableColumn?tableName="+tableName;
            window.location.href = url;
        }
    }

    //处理编辑页面的数据
    function edit(target) {
        var id = $(target).parent('p').attr("id");
        if (id == null || id == undefined || id < 0) {
            id = $(target).children('td').children('p').attr("id");
        }
        var row = tableData['key' + id];
        if (row != null && row != undefined) {
            $("#addModal .form input[name='name']").val(row.name);
            $("#addModal .form input[name='comments']").val(row.comments);
            $("#addModal .form input[name='className']").val(row.className);
            $("#addModal .form input[name='remark']").val(row.remark);
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
            name: {
                required: true
            },
            comments: {
                required: true
            },
            className: {
                required: true
            },
            remark: {
                required: true
            },
        },
        messages: {
            name: {
                required: I18n.system_please_input + "名称"
            },
            comments: {
                required: I18n.system_please_input + "描述"
            },
            className: {
                required: I18n.system_please_input + "实体类名称"
            },
            remark: {
                required: I18n.system_please_input + "备注信息"
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
            $.post(base_url + "/codeTable/save", $("#addModal .form").serialize(), function (data, status) {
                if (data.code == 200) {
                    $('#addModal').modal('hide');
                    layer.open({
                        title: I18n.system_tips,
                        btn: [I18n.system_ok],
                        content: I18n.system_add_suc,
                        icon: '1',
                        end: function (layero, index) {
                            codeTableTable.fnDraw();
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