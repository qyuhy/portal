<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>资源列表</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="${request.contextPath}/jsp/common/header.jsp"></jsp:include> 
	<script type="text/javascript">
		var treeGrid;
		$(function(){
			treeGrid = $('#treeGrid').treegrid({
				url : '${pageContext.request.contextPath}/permession/resource/findAll.json',
				idField : 'resId',
				treeField : 'resName',
				parentField : 'resPid',
				fit : true,
				fitColumns : true,
				border : false,
				rootProperty : 'tree',
				frozenColumns : [ [ {
					title : '编号',
					field : 'resId',
					width : 150,
					hidden : true
				} ] ],
				columns : [ [ {
					field : 'resName',
					title : '资源名称',
					width : 200
				}, {
					field : 'resUrl',
					title : '资源路径',
					width : 230
				}, {
					field : 'resType',
					title : '资源类型ID',
					width : 150
				},{
					field : 'resOrder',
					title : '排序',
					width : 40
				}, {
					field : 'resPid',
					title : '上级资源ID',
					width : 150,
					hidden : true
				},{
					field : 'action',
					title : '操作',
					width : 50,
					formatter : function(value, row, index) {
						var str = '';
						if (true) {
							str += $.formatString('<img onclick="editFun(\'{0}\');" src="{1}" title="编辑"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png');
						}
						str += '&nbsp;';
						if (true) {
							str += $.formatString('<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>', row.id, '${pageContext.request.contextPath}/style/images/extjs_icons/cancel.png');
						}
						return str;
					}
				} ] ],
				toolbar : '#toolbar',
				onContextMenu : function(e, row) {
					e.preventDefault();
					$(this).treegrid('unselectAll');
					$(this).treegrid('select', row.id);
					$('#menu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				},
				onLoadSuccess : function() {
					parent.$.messager.progress('close');
					$(this).treegrid('tooltip');
				}
			});
		});		
		
		//展开
		function expand() {
			var node = treeGrid.treegrid('getSelected');
			if (node) {
				treeGrid.treegrid('expandAll', node.id);
			} else {
				treeGrid.treegrid('expandAll');
			}
		}
		
		//折叠
		function collapse() {
			var node = treeGrid.treegrid('getSelected');
			if (node) {
				treeGrid.treegrid('collapseAll', node.id);
			} else {
				treeGrid.treegrid('collapseAll');
			}
		}
		
		//添加资源
		function addFun(){
			parent.$.modalDialog({
				title : '添加资源',
				width : 1024,
				height : 280,
				href : '${pageContext.request.contextPath}/permession/resource/toAddView.do',
				buttons : [ {
					text : '添加',
					handler : function() {
						parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#form');
						f.submit();
					}
				} ]
			});
		}
	</script>
  </head>
  
  <body>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false" title="" style="overflow:hidden;">
			<table id="treeGrid"></table>
		</div>
	</div> 
	
	<div id="toolbar" style="display: none;">
		<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'pencil_add'">添加</a>
		<a onclick="expand();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_next'">展开</a> 
		<a onclick="collapse();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'resultset_previous'">折叠</a>
		<a onclick="treeGrid.treegrid('reload');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'transmit'">刷新</a>
	</div>   
  </body>
</html>
