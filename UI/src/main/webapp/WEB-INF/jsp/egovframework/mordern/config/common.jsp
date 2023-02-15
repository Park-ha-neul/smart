<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="org.springframework.web.util.UrlPathHelper"%>
<%@ page import="egovframework.framework.common.util.StringUtil"%>
<%@ page import="egovframework.framework.common.util.SysUtil"%>
<%@ page import="egovframework.framework.common.util.SessionUtil" %>
<%@ page import="egovframework.admin.common.vo.UserInfoVo" %>
<%@ page import="egovframework.framework.common.constant.Globals"%>
<%@ page import="egovframework.framework.common.constant.Const"%>
<%@ page import="egovframework.framework.common.object.DataMap"%>

<jsp:useBean id="param" class="egovframework.framework.common.object.DataMap" scope="request"/>

<%
	String jsRoot = Globals.JS_ROOT;
	String imgRoot = Globals.IMG_ROOT;
	String cssRoot = Globals.CSS_ROOT;
	String siteName = Globals.SITE_NAME;
	String headTitle = Globals.SITE_NAME;
	String domainAdmin = Globals.DOMAIN_ADMIN;
	String domainCenter = Globals.DOMAIN_CENTER;
	String protocol = Globals.PROTOCOL;
	String siteDdoaminCms = Globals.SITE_DOMAIN_CMS;
	String naverMapClientId = Globals.NAVERMAP_CLIENT_ID;
	UserInfoVo ssUserInfoVo = SessionUtil.getSessionUserInfoVo(request);
	String ssUserId = "";
	String ssUserNo = "";
	String ssUserNm = "";
	// String ssAuthorId = ""; List 로 수정해야 됨
	DataMap ssThemaOption = new DataMap();

	if(ssUserInfoVo != null){
		ssUserId = StringUtil.nvl(ssUserInfoVo.getId());
		ssUserNo = StringUtil.nvl(ssUserInfoVo.getUserNo());
		ssUserNm = StringUtil.nvl(ssUserInfoVo.getUserNm());
		// ssAuthorId = StringUtil.nvl(ssUserInfoVo.getAuthorId());
		ssThemaOption = SysUtil.jsonStringToDataMap(StringUtil.nvl(ssUserInfoVo.getThemaOption()));
	}

	// 각페이지 타이틀 이름
	String PAGE_TITLE = "";

	UrlPathHelper urlPathHelper = new UrlPathHelper();
	String reqUrl = urlPathHelper.getOriginatingRequestUri(request);
%>

<c:set var="ssUserId" value="<%=ssUserId %>" />
<c:set var="ssUserNo" value="<%=ssUserNo %>" />
<%-- <c:set var="ssAuthorId" value="<%=ssAuthorId %>" /> --%>
<c:set var="ssThemaOption" value="<%=ssThemaOption %>" />
<c:set var="siteDdoaminCms" value="<%=siteDdoaminCms %>" />
<c:set var="siteName" value="<%=siteName %>" />
<c:set var="protocol" value="<%=protocol %>" />
<c:set var="domainAdmin" value="<%=domainAdmin %>" />
<c:set var="domainCenter" value="<%=domainAdmin %>" />

<c:set var="CODE_AUTHOR_ADMIN" value="<%=Const.CODE_AUTHOR_ADMIN %>" />

<%-- Const --%>
<c:set var="UPCODE_USE_YN" value="<%=Const.UPCODE_USE_YN%>"/>
<c:set var="UPCODE_SYS_CODE" value="<%=Const.UPCODE_SYS_CODE%>"/>
<c:set var="UPCODE_USER_SE" value="<%=Const.UPCODE_USER_SE%>"/>
<c:set var="UPCODE_BBS_TY" value="<%=Const.UPCODE_BBS_TY%>"/>
<c:set var="UPCODE_MENU_TYPE_CODE" value="<%=Const.UPCODE_MENU_TYPE_CODE%>"/>
<c:set var="UPCODE_MENU_SE_CODE" value="<%=Const.UPCODE_MENU_SE_CODE%>"/>
<c:set var="UPCODE_ROW_PER_PAGE" value="<%=Const.UPCODE_ROW_PER_PAGE%>"/>
<c:set var="UPCODE_BANNER_ZONE" value="<%=Const.UPCODE_BANNER_ZONE%>"/>
<c:set var="UPCODE_POPUP_SE" value="<%=Const.UPCODE_POPUP_SE%>"/>
<c:set var="UPCODE_BBS_CNTNTS_POSITION" value="<%=Const.UPCODE_BBS_CNTNTS_POSITION%>"/>
<c:set var="UPCODE_QUICK_ZONE" value="<%=Const.UPCODE_QUICK_ZONE%>"/>
<c:set var="UPCODE_USER_STTUS" value="<%=Const.UPCODE_USER_STTUS %>"/>
<c:set var="UPCODE_CTTPC_SE" value="<%=Const.UPCODE_CTTPC_SE %>"/>


<%-- wild card sysCode mapping logic --%>
<%
	String sysMappingCode = param.getString("sys_mapping_code").isEmpty()?"":param.getString("sys_mapping_code");
%>

<c:set var="sysMappingCode" value="<%=sysMappingCode%>" />

<script type="text/javascript">
	function fnSysMappingCode(){
		if(${empty sysMappingCode}) {
			return "";
		} else {
			return "${sysMappingCode}"+"/";
		}
	}
</script>
