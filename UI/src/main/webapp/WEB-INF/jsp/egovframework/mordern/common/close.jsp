<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="egovframework.framework.common.util.SysUtil" %>
<%@ page import="egovframework.framework.common.constant.Const" %>
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<%-- <jsp:useBean id="param" class="egovframework.framework.common.object.DataMap" scope="request"/> --%>

<html>
<head>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>close</title>
	<script type="text/javascript">
<%
	String message = (String)request.getSession().getAttribute("message");
	if(message != null && !message.equals("")){
%>
		alert("<%=message%>");
<%
		request.getSession().removeAttribute("message");
	}
%>
// 		window.open('', '_self', '');
		window.close();

	</script>
</head>

<body>
</body>
</html>