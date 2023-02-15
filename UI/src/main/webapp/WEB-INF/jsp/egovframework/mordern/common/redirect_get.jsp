<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="egovframework.framework.common.util.SysUtil" %>
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<%-- <jsp:useBean id="param" class="egovframework.framework.common.object.DataMap" scope="request"/> --%>
<script type="text/javascript">
	$(function(){
		setTimeout(function(){
			document.aform.target = "_self";
			document.aform.submit();
		}, 0);
	});
</script>
<form name = "aform" method = "get" action = "<%=param.getString("redirectUrl")%>">
<%=SysUtil.createInputObj(param)%>
</form>