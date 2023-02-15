<%@page import="egovframework.framework.common.util.file.vo.NtsysFileVO"%>
<%@page import="egovframework.framework.common.util.CommboUtil"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<%@ page import="egovframework.framework.common.object.DataMap" %>
<%-- <jsp:useBean id="param" class="egovframework.framework.common.object.DataMap" scope="request"/> --%>

<html>
<head>
	<title><%=headTitle%></title>

	<script type="text/javascript">
		$(function(){

		});
	</script>
</head>
<body id="subBody">
	<div class="printArea">
		<%=param.getString("html") %>
	</div>
</body>
</html>
