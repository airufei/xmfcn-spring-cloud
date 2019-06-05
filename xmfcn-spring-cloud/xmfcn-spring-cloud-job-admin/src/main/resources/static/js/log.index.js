// table data
var tableData = {};
$(function () {
    //查询表格数据
    var logTable = $("#log_table").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
         bLengthChange: true,
         aLengthMenu: [30, 50, 100, 200, 400],
        "searching": false,
        "ordering": false,
        "scrollX": true,	// scroll x，close self-adaption
        "ajax": {
            url: base_url + "/log/search",
            type: "post",
            data: function (d) {
                var obj = {};
                obj.start = d.start;
                obj.length = d.length;
                obj.message = $('#message').val();
                obj.subSysName = $('#subSysName').val();
                obj.traceId = $('#traceId').val();
                obj.methodName = $('#methodName').val();
                obj.level = $('#level').val();
                obj.stackMessage = $('#stackMessage').val();
                return obj;
            }
        },
        "columns": [
            {
                "data": 'id',
                "visible": false,
                "width": '180'
            },
            {
                "data": 'time',
                "visible": true,
                "width": '160'
            },

            {
                "data": 'subSysName',
                "visible": true,
                "width": '80'
            },
            {
                "data": 'moduleName',
                "visible": true,
                "width": '180'
            },
            {
                "data": 'traceId',
                "visible": true,
                "width": '120'
            },
            {
                "data": 'methodName',
                "visible": true,
                "width": '180'
            },
            {
                "data": 'level',
                "visible": true,
                "width": '80'
            },
            {
                "data": 'message',
                "visible": true,
                "width": '250',
                "render": function (data, type, row) {
                    return function () {
                        var message = row.message;
                        if (message != null && message.length > 120) {
                            message = message.substr(0, 120) + "...";
                        }
                        return message;
                    };
                }
            },
            {
                "data": 'threadName',
                "visible": true,
                "width": '160'
            },
            {
                "data": 'sysIp',
                "visible": true,
                "width": '180'
            },
            {
                "data": 'operate',
                "width": '15%',
                "render": function (data, type, row) {
                    return function () {
                        tableData['key' + row.id] = row;
                        return '<p id="' + row.id + '" >' + '</p>';
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
        logTable.fnDraw();
    });

    // 查询按钮事件
    $('#goToReport').on('click', function () {
        window.location.href = base_url+"/logcharts";
    });

    //双击弹出编辑
    $('#log_table').on('dblclick', 'tr', function () {
        deatil(this);
    });
});

//处理编辑页面的数据
function deatil(target) {
    var id = $(target).parent('p').attr("id");
    if (id == null || id == undefined || id == "") {
        id = $(target).children('td').children('p').attr("id");
    }
    $.ajax({
        type: "post",
        url: base_url + "/log/getLogDetailById?id=" + id,
        dataType: "json",
        success: function (data) {
            if (data == null || undefined == data) {
                return;
            }
            var row = data;
            $("#sysIp").html(row.sysIp);
            $("#subSysName_deatil").html(row.subSysName);
            $("#moduleName").html(row.moduleName);
            $("#methodName__deatil").html(row.methodName);
            $("#message__deatil").html(row.message);
            $("#level__deatil").html(row.level);
            $("#time").html(row.time);
            $("#stackMessage_deatil").html(row.stackMessage);
            $("#threadName").html(row.threadName);
            $("#traceId__deatil").html(row.traceId);
            $("#id_deatil").html(row.id);
            $("#mdcMessage_deatil").html(row.traceMap);
            $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
        },
        error: function (jqXHR) {
            console.log("Error: " + jqXHR.status);
        }
    });
    $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
}