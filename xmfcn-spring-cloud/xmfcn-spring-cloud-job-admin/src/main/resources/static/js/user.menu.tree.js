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
    var parentNode = $("#treeview").treeview("getNode", node.parentId);
    if (parentNode.nodes) {
        var checkedCount = 0;
        for (x in parentNode.nodes) {
            if (parentNode.nodes[x].state.checked) {
                checkedCount ++;
            } else {
                break;
            }
        }
        if (checkedCount === parentNode.nodes.length) {
            $("#treeview").treeview("checkNode", parentNode.nodeId);
            setParentNodeCheck(parentNode);
        }
    }
}

$(function () {
    createTree();
});

//创建菜单树
function createTree() {
    $.ajax({
        type: "post",
        url: base_url + "/jobMenu/pageList",
        dataType: "json",
        success: function (result) {
            $('#treeview').treeview({
                data: result.returnlist,         // 数据源
                showCheckbox: true,   //是否显示复选框
                highlightSelected: true,    //是否高亮选中
                //nodeIcon: 'glyphicon glyphicon-user',
                emptyIcon: 'glyphicon glyphicon-minus',    //没有子节点的节点图标
                selectedIcon: "glyphicon glyphicon-home",
                multiSelect: true,    //多选
                levels : 2,
                enableLinks : true,//必须在节点属性给出href属性
                color: "#010A0E",
                //backColor: "#8FACBA",
                //showBorder:false,
                onNodeChecked : function (event,node) {
                    var selectNodes = getChildNodeIdArr(node); //获取所有子节点
                    if (selectNodes) { //子节点不为空，则选中所有子节点
                        $('#treeview').treeview('checkNode', [selectNodes, { silent: true }]);
                    }
                    var parentNode = $("#treeview").treeview("getNode", node.parentId);
                    setParentNodeCheck(node);
                },
                onNodeUnchecked : function(event, node) { //取消选中节点
                    var selectNodes = getChildNodeIdArr(node); //获取所有子节点
                    if (selectNodes) { //子节点不为空，则取消选中所有子节点
                        $('#treeview').treeview('uncheckNode', [selectNodes, { silent: true }]);
                    }
                },
                onNodeExpanded : function(event, data) {

                },
                onNodeSelected: function (event, data) {
                    //alert(data.nodeId);
                }
            });
        },
        error: function () {
            alert("树形结构加载失败！")
        }
    });
}