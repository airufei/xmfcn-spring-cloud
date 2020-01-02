$(function () {
    //查询表格数据
    var winnersListTable = $("#winnersList_table").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/winnersList/pageList",
            type: "post",
            data: function (d) {
                var obj = {};
                obj.type = $('#type').val();
                obj.name = $('#name').val();
                obj.userId = $('#userId').val();
                obj.start = d.start;
                obj.length = d.length;
                return obj;
            }
        },
        "searching": false,
        "ordering": false,
        //"scrollX": true,	// scroll x，close self-adaption
        "columns": [

            {
                "data": 'id',
                "visible": false,
                "width": '180'
            },
            {
                "data": 'type',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'imgUrl',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'userId',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'nickname',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'photourl',
                 "visible": true,
                "width": '180'
            },
            {
                "data": 'createtime',
                 "visible": true,
                "width": '180'
            },
            {
                "data": I18n.system_opt,
                "width": '15%',
                "render": function (data, type, row) {
                    return function () {
                        tableData['key'+row.id] = row;
                        return '<p id="' + row.id + '" >' +
                            '<button class="btn btn-warning btn-xs job_operate" _type="winnersList_save"" type="button">' + I18n.system_opt_edit + '</button>  ' +
                            '<button class="btn btn-danger btn-xs job_operate" _type="winnersList_del" type="button">' + I18n.system_opt_del + '</button>  ' +
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
        winnersListTable.fnDraw();
    });


    // 操作按钮事件
    $("#winnersList_table").on('click', '.job_operate', function () {
        var typeName;
        var url;
        var needFresh = false;
        var type = $(this).attr("_type");
        if ("winnersList_del" == type) {
            typeName = I18n.system_opt_del;
            url = base_url + "/winnersList/delete";
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
                                    winnersListTable.fnDraw(false);
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
    $("#winnersList_table").on('click', '.job_operate', function () {
        var type = $(this).attr("_type");
        if ("winnersList_save" == type) {
            edit(this);
        }
    });

    //双击弹出编辑
    $('#winnersList_table').on('dblclick','tr',function(){
        edit(this);
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
           $("#addModal .form input[name='type']").val(row.type);
           $("#addModal .form input[name='name']").val(row.name);
           $("#addModal .form input[name='imgUrl']").val(row.imgUrl);
           $("#addModal .form input[name='userId']").val(row.userId);
           $("#addModal .form input[name='nickname']").val(row.nickname);
           $("#addModal .form input[name='photourl']").val(row.photourl);
           $("#addModal .form input[name='createtime']").val(row.createtime);
           $("#addModal .form input[name='updatetime']").val(row.updatetime);
           $("#addModal .form input[name='flag']").val(row.flag);
           $("#addModal .form input[name='remark']").val(row.remark);
          $("#addModal .form input[name='id']").val(row.id);
        }
        // show
        $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
     }

    // 添加页面按钮事件
    $(".add").click(function () {
         $("#addModal .form input[name='id']").val(-1);
         $("#addModal .form")[0].reset();
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
            type: {
                required: true
            },
            name: {
                required: true
            },
            imgUrl: {
                required: true
            },
            userId: {
                required: true
            },
            nickname: {
                required: true
            },
            photourl: {
                required: true
            },
            createtime: {
                required: true
            },
            updatetime: {
                required: true
            },
            flag: {
                required: true
            },
            remark: {
                required: true
            },
        },
        messages: {
            id: {
                required: I18n.system_please_input + "id"
            },
            type: {
                required: I18n.system_please_input + "奖品类型"
            },
            name: {
                required: I18n.system_please_input + "奖品名称"
            },
            imgUrl: {
                required: I18n.system_please_input + "奖品url"
            },
            userId: {
                required: I18n.system_please_input + "用户ID"
            },
            nickname: {
                required: I18n.system_please_input + "昵称"
            },
            photourl: {
                required: I18n.system_please_input + "头像"
            },
            createtime: {
                required: I18n.system_please_input + "创建时间"
            },
            updatetime: {
                required: I18n.system_please_input + "修改时间"
            },
            flag: {
                required: I18n.system_please_input + "删除标记"
            },
            remark: {
                required: I18n.system_please_input + "用户ID"
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
            $.post(base_url + "/winnersList/save", $("#addModal .form").serialize(), function (data, status) {
                if (data.code ==200) {
                    $('#addModal').modal('hide');
                    layer.open({
                        title: I18n.system_tips,
                        btn: [I18n.system_ok],
                        content: I18n.system_add_suc,
                        icon: '1',
                        end: function (layero, index) {
                            winnersListTable.fnDraw();
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