function getdata(pageType) {
    $.ajax({
        type: "POST",
        contentType: 'application/json;charset=utf-8', //设置请求头信息
        url: base_url + "/jobMenu/getTreeList?pageType=" + pageType,
        async: false,
        dataType: "json",
        success: function (data) {
            initTree(data)
        }
    });
}

function initTree(data) {
    $('#treeview').treeview({
        data: data,         // data is not optional
        levels: 2,//默认打开的级别
        showCheckbox: false,
        multiSelect: false,
        enableLinks: true,
    });
}