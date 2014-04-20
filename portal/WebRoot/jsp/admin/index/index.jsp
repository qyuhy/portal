<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Portal</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<jsp:include page="${request.contextPath}/jsp/common/header.jsp"></jsp:include>  
	<script type="text/javascript">
		var portalLayout;
		var portal;
		
		$(function(){
		
			//初始化布局
			portalLayout = $('#portalLayout').layout({
				fit : true
			});
			
			//窗口改变大小，重新渲染窗口
			$(window).resize(function() {
				portalLayout.layout('panel', 'center').panel('resize', {
					width : 1,
					height : 1
				});
			});
			
			panels = [{
				id : 'p1',
				title : '关于',
				height : 200,
				collapsible : true,
				content : '<h3>关于</h3>'
			},{
				id : 'p2',
				title : '待处理消息',
				height : 200,
				collapsible : true,
				content : '<h3>待处理消息</h3>'
			},{
				id : 'p3',
				title : '开发进度说明',
				height : 200,
				collapsible : true,
				content : '<h3>开发进度说明</h3>'
			},{
				id : 'p4',
				title : '项目组说明',
				height : 200,
				collapsible : true,
				content : '<h3>项目组说明</h3>'
			}]; 
			
			portal = $('#portal').portal({
				border : false,
				fit : true,
				onStateChange : function() {
					$.cookie('portal-state', getPortalState(), {
						expires : 7
					});
				}
			});
			
			var state = $.cookie('portal-state');
			if (!state) {
				state = 'p1,p2:p3,p4';/*冒号代表列，逗号代表行*/
			}
			
			//添加
			addPortalPanels(state);
			
			portal.portal('resize');
		});
		
		
		function getPortalState(){
			var aa = [];
			for ( var columnIndex = 0; columnIndex < 2; columnIndex++) {
				var cc = [];
				var panels = portal.portal('getPanels', columnIndex);
				for ( var i = 0; i < panels.length; i++) {
					cc.push(panels[i].attr('id'));
				}
				aa.push(cc.join(','));
			}
			return aa.join(':');
		}
		
		function getPanelOptions(id){
			for ( var i = 0; i < panels.length; i++) {
				if (panels[i].id == id) {
					return panels[i];
				}
			}
			return undefined;
		}
		
		function addPortalPanels(portalState) {
			var columns = portalState.split(':');
			for ( var columnIndex = 0; columnIndex < columns.length; columnIndex++) {
				var cc = columns[columnIndex].split(',');
				for ( var j = 0; j < cc.length; j++) {
					var options = getPanelOptions(cc[j]);
					if (options) {
						var p = $('<div/>').attr('id', options.id).appendTo('body');
						p.panel(options);
						portal.portal('add', {
							panel : p,
							columnIndex : columnIndex
						});
					}
				}
			}
		}
		
	</script> 
	 
  </head>
  <body>
	<div id="portalLayout">
		<div data-options="region:'center',border:false">
			<div id="portal" style="position: relative">
				<div></div>
				<div></div>
			</div>
		</div>
	</div>
  </body>
</html>
