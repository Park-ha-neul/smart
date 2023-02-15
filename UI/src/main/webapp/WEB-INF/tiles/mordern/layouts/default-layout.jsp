<%@ page pageEncoding="utf-8" session="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<tiles:insertAttribute name="head" />
	</head>
	<body class="sidebar-mini layout-fixed ${ssThemaOption.c1} ${ssThemaOption.t2}">
		<div class="wrapper">
			<!-- 헤더  -->
			<tiles:insertAttribute name="navi" />
			<!-- 좌측 메뉴 -->
			<tiles:insertAttribute name="left" />

			<div class="content-wrapper">
				<!-- Content Header (Page header) -->
				<div class="content-header">
					<div class="container-fluid">
						<div class="row mb-2">
							<div class="col-sm-6">
								<h1 class="m-0 text-dark content_title"></h1>
							</div><!-- /.col -->
							<div class="col-sm-6">
								<ol class="breadcrumb float-sm-right">
									<li class="breadcrumb-item"><a href="/">Home</a></li>
								</ol>
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.container-fluid -->
				</div>
				<tiles:insertAttribute name="content" />
			</div>

			<tiles:insertAttribute name="setting" />

			<tiles:insertAttribute name="footer" />

			<tiles:insertAttribute name="msg" />
		</div>
		<!-- ionicons -->
		<script src="/webjars/ionicons/5.0.0/dist/ionicons/ionicons.js"></script>
	</body>
</html>