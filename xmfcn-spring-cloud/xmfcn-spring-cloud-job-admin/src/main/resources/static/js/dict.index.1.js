$(function () {

    // init date tables
    var dictTable = $("#dict_list").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/dict/pageList",
            type: "post",
            data: function (d) {
                var obj = {};
                obj.dictKey = $('#dictKey').val();
                obj.dictValue = $('#dictValue').val();
                obj.type = $('#type').val();
                obj.id = $('#id').val();
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
                "width": '10%'
            },
            {
                "data": 'dictKey',
                "visible": true,
                "width": '10%'
            },
            {
                "data": 'dictValue',
                "visible": true,
                "width": '20%'
            },
            {
                "data": 'type',
                "width": '20%',
                "visible": true
            },
            {
                "data": 'remark',
                "visible": true,
                "width": '10%'
            },
            {
                "data": 'createtimestr',
                "width": '20%',
                "visible": true
            },
            {
                "data": I18n.system_opt,
                "width": '15%',
                "render": function (data, type, row) {
                    return function () {
                        tableData['key'+row.id] = row;
                        return '<p id="' + row.id + '" >' +
                            '<button class="btn btn-warning btn-xs job_operate"  _type="dict_edit" type="button">' + I18n.system_opt_edit + '</button>  ' +
                            '<button class="btn btn-danger btn-xs job_operate" _type="dict_del" type="button">' + I18n.system_opt_del + '</button>  ' +
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

    // search btn
    $('#searchBtn').on('click', function () {
        dictTable.fnDraw();
    });

    // jobGroup change
    $('#jobGroup').on('change', function () {
        //reload
        var jobGroup = $('#jobGroup').val();
        window.location.href = base_url + "/jobinfo?jobGroup=" + jobGroup;
    });

    // job operate
    $("#dict_list").on('click', '.job_operate', function () {
        var typeName;
        var url;
        var needFresh = false;

        var type = $(this).attr("_type");
        if ("dict_del" == type) {
            typeName = I18n.system_opt_del;
            url = base_url + "/dict/remove";
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
                                    dictTable.fnDraw(false);
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


    $("#jobMenu_list").on('click', '.job_operate', function () {
        var type = $(this).attr("_type");
        if ("dict_edit" == type) {
            edit(this);
        }
    });

    function edit(target) {
        var id = $(this).parent('p').attr("id");
        var row = tableData['key' + id];
        if (row != null && row != undefined) {
            $("#addModal .form input[name='name']").val(row.name);
            $("#addModal .form input[name='url']").val(row.url);
            $("#addModal .form input[name='isbutton']").val(row.isbutton);
            $("#addModal .form input[name='remark']").val(row.remark);
            $("#addModal .form input[name='fid']").val(row.fid);
            $("#addModal .form input[name='level']").val(row.level);
            $("#addModal .form input[name='id']").val(row.id);
        }
        // show
        $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
    }

    // add
    $(".add").click(function () {
        // show
        $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var addModalValidate = $("#addModal .form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: true,
        rules: {
            dictKey: {
                required: true
            },
            dictValue: {
                required: true
            },
            type: {
                required: true
            },
            remark: {
                required: true
            }
        },
        messages: {
            dictKey: {
                required: I18n.system_please_input + "键"
            },
            dictValue: {
                required: I18n.system_please_input + "值"
            },
            type: {
                required: I18n.system_please_input + "类型"
            },
            remark: {
                digits: I18n.system_please_input + "备注"
            }
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

            $.post(base_url + "/dict/save", $("#addModal .form").serialize(), function (data, status) {
                if (data.code === "200") {
                    $('#addModal').modal('hide');
                    layer.open({
                        title: I18n.system_tips,
                        btn: [I18n.system_ok],
                        content: I18n.system_add_suc,
                        icon: '1',
                        end: function (layero, index) {
                            dictTable.fnDraw();
                            //window.location.reload();
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
    $("#addModal").on('hide.bs.modal', function () {
        $("#addModal .form")[0].reset();
        addModalValidate.resetForm();
        $("#addModal .form .form-group").removeClass("has-error");
        $(".remote_panel").show();	// remote

    });


    /**
     * find title by name, GlueType
     */
    function findGlueTypeTitle(glueType) {
        var glueTypeTitle;
        $("#addModal .form select[name=glueType] option").each(function () {
            var name = $(this).val();
            var title = $(this).text();
            if (glueType == name) {
                glueTypeTitle = title;
                return false
            }
        });
        return glueTypeTitle;
    }

});
