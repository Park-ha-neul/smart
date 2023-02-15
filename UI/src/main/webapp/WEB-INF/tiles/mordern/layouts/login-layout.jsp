<%@ page pageEncoding="utf-8" session="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html>
	<head>
		<tiles:insertAttribute name="head" />
		<link rel="stylesheet" href="/common/css/login.css">
		<!-- jquery 충돌제거 -->
<!-- 		<script src="../common/js/jquery-1.11.1.min.js"></script>-->
 		<script src="../common/js/publish.js"></script>
	</head>
	<body class="app sidebar-mini">
		<tiles:insertAttribute name="content" />

		<tiles:insertAttribute name="msg" />
	</body>
</html>