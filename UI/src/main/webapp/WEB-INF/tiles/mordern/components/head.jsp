<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"			uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		 uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, width=device-width" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="robots" content="NONE" />

<title>${siteName}</title>

<!-- <link rel="shortcut icon" type="image/x-icon" href="/common/images/nt_favicon-16.png"> -->
<!-- <link rel="shortcut icon" type="image/x-icon" href="/common/images/nt_favicon-32.png"> -->
<link rel="shortcut icon" type="image/x-icon" href="/common/images/ph_icon-64.png">

<script type="text/javascript" src="/webjars/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript" src="/webjars/jquery-form/4.2.2/jquery.form.min.js"></script>
<script type="text/javascript" src="/webjars/popper.js/1.16.1/dist/umd/popper.min.js"></script>
<script type="text/javascript" src="/webjars/bootstrap/4.4.1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/webjars/momentjs/2.10.3/min/moment-with-locales.min.js"></script>
<script type="text/javascript" src="/webjars/Eonasdan-bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>

<!-- <link rel="stylesheet" href="/webjars/bootstrap/4.4.1/css/bootstrap.min.css" /> -->
<link rel="stylesheet" href="/webjars/Eonasdan-bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css" />

<link rel="stylesheet" href="/webjars/font-awesome/5.13.0/css/all.min.css">
<link rel="stylesheet" href="/webjars/icheck-bootstrap/3.0.1/icheck-bootstrap.min.css">

<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.min.css" />
<script type="text/javascript" src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>


<!-- adminLte3 관련 -->
<link rel="stylesheet" href="/webjars/AdminLTE/3.0.5/dist/css/adminlte.min.css">
<script type="text/javascript" src="/webjars/AdminLTE/3.0.5/dist/js/adminlte.min.js"></script>

<link rel="stylesheet" href="/webjars/overlayscrollbars/1.11.0/css/OverlayScrollbars.min.css">
<script type="text/javascript" src="/webjars/overlayscrollbars/1.11.0/js/OverlayScrollbars.min.js"></script>


<!-- jsGrid -->
<link rel="stylesheet" href="/webjars/AdminLTE/3.0.5/plugins/jsgrid/jsgrid.min.css">
<link rel="stylesheet" href="/webjars/AdminLTE/3.0.5/plugins/jsgrid/jsgrid-theme.min.css">
<!-- jsGrid -->
<script src="/webjars/AdminLTE/3.0.5/plugins/jsgrid/demos/db.js"></script>
<script src="/webjars/AdminLTE/3.0.5/plugins/jsgrid/jsgrid.min.js"></script>

<!-- 사용자 정의 -->
<script type="text/javascript" src="/common/js/mordern/common.js"></script>
<link rel="stylesheet" href="/common/css/mordern/common.css">

<script src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0"></script>

<script type="text/javascript">
//<[CDATA[
$(function(){
	$(document).ajaxStart(function () {
		$('#loading-wrap').removeClass('d-none');
	});
	$(document).ajaxStop(function () {
		$('#loading-wrap').addClass('d-none');
	});
	$(document).ajaxError(function () {
		$('#loading-wrap').addClass('d-none');
	});
});

// 좌측메뉴 그린후 실행된다.
function fnSetTitle(){
	// title 설정
	var title = '${siteName}';
	// <li class="breadcrumb-item"><a href="/">Home</a></li>
	// 콘텐츠 네비게이션 html
	var nav = '';
	// content title
	var content_title = '';

	// 상단메뉴
	var $t = $('.navbar-nav .nav-item .active');
	title += ' > ' + $t.text();
	nav += '<li class="breadcrumb-item"><a href="' + $t.attr('href') + '">' + $t.text() + '</a></li>';

	// 좌측메뉴
	$t = $('#sidebar > .nav-item > a.active');
	title += ' > ' + $t.children('p').text();
	content_title = $t.children('p').text();

	// 하위메뉴가 있는경우
	if($('#sidebar > .nav-item > a.active').parent().hasClass('has-treeview')){
		// 2depth 셋팅
		nav += '<li class="breadcrumb-item"><a href="' + $t.attr('href') + '">' + $t.children('p').text() + '</a></li>';

		// 3depth 셋팅
		$t = $('#sidebar > .nav-item > a.active').parent().find('.nav-treeview > .nav-item > a.active');
		title += ' > ' + $t.children('p').text();
		content_title = $t.children('p').text();
		nav += '<li class="breadcrumb-item">' + $t.children('p').text() + '</li>';
	}
	// 하위메뉴가 없는경우
	else {
		// 2depth 셋팅
		nav += '<li class="breadcrumb-item">' + $t.children('p').text() + '</li>';
	}

	$('title').text(title);
	$('.content-header .content_title').text(content_title);
	$('.content-header .breadcrumb').append(nav);
}
//]]>
</script>
<div id="loading-wrap" class="d-none">
	<div>
		<div>
			<div class="spinner-border text-${ssThemaOption.t5}" role="status">
				<span class="sr-only">Loading...</span>
			</div>
		</div>
	</div>
</div>
<div id="loading-dim"></div>
