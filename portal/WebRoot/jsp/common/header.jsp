<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<!-- 判断浏览器类型 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/extBrowser.js" charset="utf-8"></script>

<!-- 引入jQuery -->
<script src="${pageContext.request.contextPath}/jslib/jquery/jquery-1.8.3.js" type="text/javascript" charset="utf-8"></script>

<!-- 引入EasyUI -->
<link id="easyuiTheme" rel="stylesheet" href="${pageContext.request.contextPath}/jslib/easyui/jquery-easyui-1.3.3/themes/<c:out value="${cookie.easyuiThemeName.value}" default="gray"/>/easyui.css" type="text/css">
<!-- <link rel="stylesheet" href="${pageContext.request.contextPath}/jslib/easyui/jquery-easyui-1.3.3/themes/icon.css" type="text/css"> -->
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/easyui/jquery-easyui-1.3.3/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/easyui/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/easyui/jquery-easyui-1.3.3/plugins/jquery.layout.js" charset="utf-8"></script> <!-- 修复EasyUI1.3.3中layout组件的BUG -->

<!-- 引入EasyUI Portal插件 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/jslib/easyui/jquery-easyui-portal/portal.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/easyui/jquery-easyui-portal/jquery.portal.js" charset="utf-8"></script>


<!-- 扩展EasyUI -->
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/easyui/ext/extEasyUI.js?v=201305241044" charset="utf-8"></script>

<!-- 扩展jQuery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/jquery/ext/extJquery.js?v=201305301341" charset="utf-8"></script>

<!-- 扩展EasyUI Icon -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/extEasyUIIcon.css?v=201305301906" type="text/css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/style/base/base.css" type="text/css"></link>




