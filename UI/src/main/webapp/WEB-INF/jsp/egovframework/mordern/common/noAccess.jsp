<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<html lang="ko">
<head>
<%-- <%@ include file="/common/inc/meta.jspf" %> --%>
<title><%=headTitle%></title>
<link rel="stylesheet" type="text/css" href="/common/css/common.css"/>
</head>
<body>
<div class="box">
	<div class="error">
		<h2><img src="/common/images/not.png" alt="" style="vertical-align: middle;" /> 접근 불가</h2>
		<p>해당 아이피(${requestScope.param.ip})에서는 해당 사이트에 접근하실수 없습니다. <br/>관리자에게 문의 바랍니다.</p>
	</div>
</div>
</body>
</html>
