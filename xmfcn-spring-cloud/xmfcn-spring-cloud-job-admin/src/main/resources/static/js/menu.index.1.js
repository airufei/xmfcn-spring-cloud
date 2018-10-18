//页面初始化
$(function () {
    initTable();
});

//创建表格
function initTable() {
    $("#menu_list").bootstrapTable({
        url: base_url + "/jobMenu/pageList",
        method: 'post',
        dataType: "json",
        contentType:"application/x-www-form-urlencoded",
        toolbar: '#toolbar',
        striped: true,//设置为 true 会有隔行变色效果
        undefinedText: "暂无数据",//当数据为 undefined 时显示的字符
        pagination: true, //分页
        showToggle: false,//是否显示 切换试图（table/card）按钮
        showColumns: false,//是否显示 内容列下拉框
        pageNumber: 1,//如果设置了分页，首页页码
        pageSize: 3,//如果设置了分页，页面数据条数
        pageList: [10, 15, 20, 50, 100, 200],  //如果设置了分页，设置可供选择的页面数据条数。设置为All 则显示所有记录。
        paginationPreText: '<上一页',//指定分页条中上一页按钮的图标或文字,这里是<
        paginationNextText: '下一页>',//指定分页条中下一页按钮的图标或文字,这里是>
        paginationLoop: false,
        search: false, //显示搜索框
        data_local: "zh-US",//表格汉化
        //height: '600',
        sidePagination: "server", //服务端处理分页
        fixedColumns: true,//是否冻结列
        fixedNumber: 1,//冻结列数
        singleSelect: true, //开启单选
        showRefresh: true,//是否显示刷新按钮
        showExport: true,   //是否显示导出
        exportDataType: "basic",              //basic', 'all', 'selected'.
        buttonsAlign:"right",  //按钮位置
        exportTypes:['excel'],  //导出文件类型
        Icons:'glyphicon-export',
        exportOptions:{
            //ignoreColumn: [0,1],  //忽略某一列的索引
            fileName: '菜单数据',  //文件名称设置
            worksheetName: '菜单数据',  //表格工作区名称
            tableName: '菜单数据',
            excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],
        },
        queryParams: function (params) {
            //自定义参数，这里的参数是传给后台的
            return {
                //这里的params是table提供的
                offset: params.offset,//从数据库第几条记录开始
                pageSize: params.limit,//找多少条
                type: "2" // 自定义的参数，后台需要的
            };
        },
        onLoadSuccess: function () { //加载成功时执行

        },
        onLoadError: function () { //加载错误
            showTips("数据加载失败！");
        },
        onDblClickRow: function (row) {//双击表格行时执行
            savePage(row, false)
        },
        idField: "id",//指定主键列
        columns: [
            {
                title: '操作',//表的列名
                field: 'url',//json数据中rows数组中的属性名
                align: 'center',//水平居中,
                visible: true,
                width: 150,
                formatter: operatorFormatter
            },
            {
                title: '名称',//表的列名
                field: 'name',//json数据中rows数组中的属性名
                align: 'center',//水平居中,
                visible: true,
                width: 150,
                sortable: true
            },
            {
                title: '地址',//表的列名
                field: 'url',//json数据中rows数组中的属性名
                align: 'center',//水平居中,
                visible: true,
                sortable: true
            },
            {
                title: '备注',//表的列名
                field: 'remark',//json数据中rows数组中的属性名
                align: 'center',//水平居中,
                visible: true,
                sortable: true
            },
            {
                title: '更新时间',//表的列名
                field: 'updatetimestr',//json数据中rows数组中的属性名
                align: 'center',//水平居中,
                visible: true,
                sortable: true
            },
            {
                title: '菜单等级',//表的列名
                field: 'level',//json数据中rows数组中的属性名
                align: 'center',//水平居中,
                visible: true,
                sortable: true
            }
        ]
    });
}

//操作列
function operatorFormatter(value, row, index) {
    return [
        '<a class="btn btn-warning btn-xs" href="javascript:void(0);" onclick="savePage(' + index + ',true)">编辑</a> &nbsp;&nbsp;',
        '<a class="btn btn-danger btn-xs" href="javascript:void(0);" onclick="deleted(' + row.id + ')">删除</a>',
    ].join('');
}

//弹出编辑、添加页面
function savePage(row, isbubbon) {
    if (isbubbon) {
        row = $("#menu_list").bootstrapTable('getData')[row];
    }
    if (row != null && row != undefined) {
        $("#addModal .form input[name='name']").val(row.name);
        $("#addModal .form input[name='url']").val(row.url);
        $("#addModal .form input[name='isbutton']").val(row.isbutton);
        $("#addModal .form input[name='remark']").val(row.remark);
        $("#addModal .form input[name='fid']").val(row.fid);
        $("#addModal .form input[name='level']").val(row.level);
        $("#addModal .form input[name='id']").val(row.id);
    } else {
        $("#addModal .form")[0].reset();
    }
    // show
    $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
}

//保存数据
function save() {
    var url = base_url + "/jobMenu/save";
    var parms = $("#addModal .form").serialize();
    $.ajax({
        type: "POST",
        contentType: 'application/x-www-form-urlencoded;charset=utf-8', //设置请求头信息
        url: url,
        data: parms,
        async: false,
        success: function (data) {
            if (data == null || data == undefined || data == "undefined") {
                return;
            }
            var code = data.code;
            var msg = data.msg;
            alert(msg);
            if (code == 200) {
                $('#addModal').modal('hide');
                $("#addModal .form")[0].reset();
                $("#menu_list").bootstrapTable('destroy');
                initTable();
            }
        }
    });
}

//删除数据
function deleted(dataId) {
    if (dataId < 0) {
        alert("请选择删除数据");
    }
    var url = base_url + "/jobMenu/delete?id=" + dataId;
    $.ajax({
        type: "POST",
        contentType: 'application/json;charset=utf-8', //设置请求头信息
        dataType: "json",
        url: url,
        async: false,
        success: function (data) {
            if (data == null || data == undefined || data == "undefined") {
                return;
            }
            var code = data.code;
            var msg = data.msg;
            alert(msg);
            if (code == 200) {
                $("#menu_list").bootstrapTable('destroy');
                initTable();
            }
        }
    });
}