<!DOCTYPE html>
<html>
<head>
  	 <#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="/jobadmin/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <link rel="stylesheet" href="/jobadmin/treeview/bootstrap-treeview.css">
    <title>角色数据管理</title>
</head>
<body>
<div class="wrapper">
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>角色数据</h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <div id="treeview"></div>

        </section>
    </div>
    <script src="/jobadmin/treeview/jquery.js"></script>
    //拖拽
    <script src="/jobadmin/treeview/bootstrap-treeview.js"></script>

    <script type="text/javascript">
        $(function () {
            getdata();
        });

        function getdata() {
            $.ajax({
                type: "POST",
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                url: "/jobadmin/jobMenu/getTreeList",
                async: false,
                dataType: "json",
                success: function (data) {
                    initTree(data)
                }
            });
        }

        function initTree(data) {
            console.log(data);
            $('#treeview').treeview({
                data: data,         // data is not optional
                levels: 1,//默认打开的级别
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
                onNodeSelected:function(event, node)
                {
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
                        checkedCount++;
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
    </script>
</body>
</html>