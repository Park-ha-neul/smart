<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="egovframework.framework.common.util.SysUtil" %>
<%@ page import="egovframework.framework.common.constant.Const" %>
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<jsp:useBean id="returnParam" class="egovframework.framework.common.object.DataMap" scope="request"/>
<jsp:useBean id="formParam" class="egovframework.framework.common.object.DataMap" scope="request"/>

<html>
<head>
	<script type="text/javascript">
		$(function(){
			setTimeout(function(){
				document.aform.target = "_self";
				document.aform.submit();
			}, 10);
		});
	</script>
</head>

<body>
<form name = "aform" method = "<%=formParam.getString("method")%>" action = "<%=formParam.getString("redirectUrl")%>">
<div style="display:none">
<%=SysUtil.createInputObj(returnParam)%>
</div>
</form>
</body>
</html>