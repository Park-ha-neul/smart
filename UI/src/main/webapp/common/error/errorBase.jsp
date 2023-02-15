<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, width=device-width" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Expires" content="0"/>
	<meta http-equiv="Pragma" content="no-cache"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="robots" content="NONE" />

	<title><%=headTitle%></title>

	<link rel="shortcut icon" type="image/x-icon" href="/common/images/wg_favi.ico">
	<script type="text/javascript" src="/webjars/jquery/3.4.1/jquery.min.js"></script>
	<script type="text/javascript" src="/webjars/bootstrap/4.4.1/js/bootstrap.min.js"></script>
	<link rel="stylesheet" href="/webjars/bootstrap/4.4.1/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/webjars/font-awesome/5.13.0/css/all.min.css">

	<!-- theme 관련 -->
	<link rel="stylesheet" href="/common/theme/css/main.css">
	<script type="text/javascript" src="/common/theme/js/main.js"></script>

	<!-- 사용자 정의 -->
	<link rel="stylesheet" href="/common/css/mordern/common.css">
	<script type="text/javascript" src="/common/js/mordern/common.js"></script>
</head>
<body>
<div class="app-content no-menu">
	<div class="page-error tile">
		<h1><i class="fa fa-exclamation-circle"></i> Error 500</h1>
		<p>서버에러가 발생하였습니다.</p>
		<p><button type="button" class="btn btn-outline-primary" onclick="document.location.href='/'">HOME</button></p>
	</div>
</div>
</body>
</html>