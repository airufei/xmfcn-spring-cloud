$(function () {
    var pageType = "left_menu";//"left_menu";
    getSysMenudata(pageType);
});

function getSysMenudata(pageType) {
    $.ajax({
        type: "POST",
        contentType: 'application/json;charset=utf-8', //设置请求头信息
        url: base_url + "/jobMenu/getTreeList?pageType=" + pageType,
        async: false,//异步为true
        dataType: "json",
        success: function (data) {
            initMeunTree(data)
        }
    });
}

function initMeunTree(data) {
    console.log(data);
    $('#treeview').treeview({
        data: data,         // data is not optional
        levels: 2,//默认打开的级别
        icon: "glyphicon glyphicon-stop",
        selectedIcon: "glyphicon glyphicon-stop",
        collapseIcon: "glyphicon glyphicon-minus",
        expandIcon: "glyphicon glyphicon-plus",
        showCheckbox: false,
        multiSelect: false,
        enableLinks: true,
    });
}