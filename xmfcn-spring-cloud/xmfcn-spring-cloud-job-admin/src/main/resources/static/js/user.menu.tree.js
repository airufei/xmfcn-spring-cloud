var isOk = false;
$(function () {
    if (!isOk) {
        getdata();
    }
});

function getdata() {
    $.ajax({
        type: "POST",
        contentType: 'application/json;charset=utf-8', //设置请求头信息
        url: base_url + "/jobMenu/getTreeList",
        async: false,
        dataType: "json",
        success: function (data) {
            isOk = true;
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
        onNodeChecked: function (event, node) {
            var selectNodes = getChildNodeIdArr(node); //获取所有子节点
            if (selectNodes) { //子节点不为空，则选中所有子节点
                $('#treeview').treeview('checkNode', [selectNodes, {silent: true}]);
            }
            var parentNode = $("#treeview").treeview("getNode", node.parentId);
            setParentNodeCheck(node);
        },
        onNodeSelected: function (event, node) {
            var nodeId = node.nodeId;
            alert("nodeId")
        },
        onNodeUnchecked: function (event, node) { //取消选中节点
            var selectNodes = getChildNodeIdArr(node); //获取所有子节点
            if (selectNodes) { //子节点不为空，则取消选中所有子节点
                $('#treeview').treeview('uncheckNode', [selectNodes, {silent: true}]);
            }
        }
    });
}