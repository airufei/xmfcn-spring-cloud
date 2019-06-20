// table data
var tableData = {};
$(function () {
    //查询表格数据
    var jobRoleTable = $("#jobRole_table").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/jobRole/pageList",
            type: "post",
            data: function (d) {
                var obj = {};
                obj.name = $('#name').val();
                obj.roleCode = $('#roleCode').val();
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
                "data": 'name',
                "visible": true,
                "width": '180'
            },
            {
                "data": 'roleCode',
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
                            '<button class="btn btn-warning btn-xs job_operate" _type="jobRole_save"" type="button">' + I18n.system_opt_edit + '</button>  ' +
                            '<button class="btn btn-danger btn-xs job_operate" _type="jobRole_del" type="button">' + I18n.system_opt_del + '</button>  ' +
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


    // 查询按钮事件
    $('#searchBtn').on('click', function () {
        jobRoleTable.fnDraw();
    });


    // 操作按钮事件
    $("#jobRole_table").on('click', '.job_operate', function () {
        var typeName;
        var url;
        var needFresh = false;
        var type = $(this).attr("_type");
        if ("jobRole_del" == type) {
            typeName = I18n.system_opt_del;
            url = base_url + "/jobRole/delete";
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
                                    jobRoleTable.fnDraw(false);
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
    $("#jobRole_table").on('click', '.job_operate', function () {
        var type = $(this).attr("_type");
        if ("jobRole_save" == type) {
            edit(this);
        }
    });

    //双击弹出编辑
    $('#jobRole_table').on('dblclick', 'tr', function () {
        edit(this);
    });


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
            name: {
                required: true
            },
            roleCode: {
                required: true
            },
        },
        messages: {
            name: {
                required: I18n.system_please_input + "角色名称"
            },
            roleCode: {
                required: I18n.system_please_input + "角色代码"
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
            var url = base_url + "/jobRole/save";
            var name = $("#addModal .form input[name='name']").val();
            var remark = $("#addModal .form input[name='remark']").val();
            var roleCode = $("#addModal .form input[name='roleCode']").val();
            var id = $("#addModal .form input[name='id']").val();
            var parms = $('#roletreeview').treeview('getChecked');
            if (parms == null || parms == undefined) {
                return false;
            }
            var len=parms.length;
            var array = new Array(len);
            for (var i = 0; i < len; i++) {
                var obj = parms[i];
                if (obj == null) {
                    continue;
                }
                var json = {"id": obj.nodeid};
                array[i] = json;
            }
            //var list=array.toJSONString();
            var list=JSON.stringify(array);
            var parms = {"name": name, "remark": remark, "roleCode": roleCode, "id": id, "list": list};
            $.post(url, parms, function (data, status) {
                if (data.code == 200) {
                    $('#addModal').modal('hide');
                    layer.open({
                        title: I18n.system_tips,
                        btn: [I18n.system_ok],
                        content: I18n.system_add_suc,
                        icon: '1',
                        end: function (layero, index) {
                            jobRoleTable.fnDraw();
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
        $("#addModal .form .form-group").removeClass("has-error");
        $(".remote_panel").show();	// remote

    });
});

//处理编辑页面的数据
function edit(target) {
    var id = $(target).parent('p').attr("id");
    if (id == null || id == undefined || id < 0) {
        id = $(target).children('td').children('p').attr("id");
    }
    var row = tableData['key' + id];
    var roleId=-1;
    if (row != null && row != undefined) {
        $("#addModal .form input[name='name']").val(row.name);
        $("#addModal .form input[name='remark']").val(row.remark);
        $("#addModal .form input[name='roleCode']").val(row.roleCode);
        $("#addModal .form input[name='id']").val(row.id);
        roleId=row.id;
    }
    var pageType = "role_menu";
    getdata(pageType,roleId);
    $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
}

function getdata(pageType,roleId) {
    $.ajax({
        type: "POST",
        contentType: 'application/json;charset=utf-8', //设置请求头信息
        url: base_url + "/jobMenu/getTreeList?pageType=" + pageType+"&roleId="+roleId,
        async: false,
        dataType: "json",
        success: function (data) {
            initTree(data)
        }
    });
}

function initTree(data) {
    $('#roletreeview').treeview({
        data: data,         // data is not optional
        levels: 2,//默认打开的级别
        showCheckbox: true,
        multiSelect: false,
        enableLinks: false,
        onNodeChecked: function (event, node) {
            var selectNodes = getChildNodeIdArr(node); //获取所有子节点
            if (selectNodes) { //子节点不为空，则选中所有子节点
                $('#roletreeview').treeview('checkNode', [selectNodes, {silent: true}]);
            }
            var parentNode = $("#roletreeview").treeview("getNode", node.parentId);
            setParentNodeCheck(node);
        },
        onNodeSelected: function (event, node) {
            var nodeId = node.nodeId;
            alert("nodeId")
        },
        onNodeUnchecked: function (event, node) { //取消选中节点
            var selectNodes = getChildNodeIdArr(node); //获取所有子节点
            if (selectNodes) { //子节点不为空，则取消选中所有子节点
                $('#roletreeview').treeview('uncheckNode', [selectNodes, {silent: true}]);
            }
        }
    });
}

//选中/取消父节点时选中/取消所有子节点
function getChildNodeIdArr(node) {
    var ts = [];
    if (node.nodes) {
        for (x in node.nodes) {
            ts.push(node.nodes[x].nodeId);
            if (node.nodes[x].nodes) {
                var getNodeDieDai = getChildNodeIdArr(node.nodes[x]);
                for (j in getNodeDieDai) {
                    ts.push(getNodeDieDai[j]);
                }
            }
        }
    } else {
        ts.push(node.nodeId);
    }
    return ts;
}

//选中所有子节点时选中父节点
function setParentNodeCheck(node) {
    var parentNode = $("#roletreeview").treeview("getNode", node.parentId);
    if (parentNode.nodes) {
        var checkedCount = 0;
        for (x in parentNode.nodes) {
            if (parentNode.nodes[x].state.checked) {
                checkedCount++;
            } else {
                break;
            }
        }
        if (checkedCount === parentNode.nodes.length) {
            $("#roletreeview").treeview("checkNode", parentNode.nodeId);
            setParentNodeCheck(parentNode);
        }
    }
}