<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"			uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		 uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:if test="${sessionScope.message != null && sessionScope.message != ''}">
	<script>
		window.onload = function(){
			alert('${sessionScope.message}');
		};
	</script>

	<c:remove var="message" scope="session" />
</c:if>


