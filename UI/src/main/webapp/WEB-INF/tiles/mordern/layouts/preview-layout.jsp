<%@ page pageEncoding="utf-8" session="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
	<head>
		<tiles:insertAttribute name="head" />

		<style type="text/css">
		.bd-navbar {
			position: sticky;
			top: 0;
		}

		.bd-sidebar {
			padding-left: 0;
			height:auto;

		}

		.bd-sidebar .nav {
			display: block;
		}


		</style>

	</head>
	<body>
		<tiles:insertAttribute name="content" />
		<tiles:insertAttribute name="setting" />

		<tiles:insertAttribute name="msg" />
	</body>
</html>