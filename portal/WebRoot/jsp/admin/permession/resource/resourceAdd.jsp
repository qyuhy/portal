<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		$('#form').form({
			url : '${pageContext.request.contextPath}/resourceController/add',
			onSubmit : function() {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = $(this).form('validate');
				if (!isValid) {
					parent.$.messager.progress('close');
				}
				return isValid;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为resource.jsp页面预定义好了
					parent.layout_west_tree.tree('reload');
					parent.$.modalDialog.handler.dialog('close');
				}
			}
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table class="table-layout" style="width:100%;height:100%;">
				<tr>
					<td class='lebel-right' style='width:25%'>资源名称:</td>
					<td class='lebel-left'  style='width:25%'><input name="id" type="text" class="span2" value="${resource.id}" readonly="readonly"></td>
					<td class='lebel-right' style='width:25%'>资源路径:</th>
					<td class='lebel-left'  style='width:25%'><input name="name" type="text" placeholder="请输入资源名称" class="easyui-validatebox span2" data-options="required:true" value=""></td>
				</tr>
				<tr>
					<td class='lebel-right' style='width:25%'>菜单类型:</td>
					<td class='lebel-left' style='width:25%'><input name="url" type="text" placeholder="请输入资源路径" class="easyui-validatebox span2" value=""></td>
					<td class='lebel-right' style='width:25%'>排序:</td>
					<td class='lebel-left' style='width:25%'><select name="typeId" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${resourceTypeList}" var="resourceType">
								<option value="${resourceType.id}">${resourceType.name}</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td class='lebel-right' style='width:25%'>上级资源:</td>
					<td class='lebel-left' style='width:25%'><input name="seq" value="100" class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="editable:false,min:100"></td>
					<td class='lebel-right' style='width:25%'>图标:</td>
					<td class='lebel-left' style='width:25%'><select id="pid" name="pid" style="width: 140px; height: 29px;"></select></td>
				</tr>
				<tr>
					<td class='lebel-right'>备注</td>
					<td class='lebel-left' colspan='3'><textarea name="remark" style='width:99%;'></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>