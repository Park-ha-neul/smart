<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"			uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		 uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<script type="text/javascript">

$(document).ready(function(){
	//초기메뉴 세팅(운영관리)
	fnChgLeftMenu("${top_menu_id}");
	fnSetTitle();
	var target = document.getElementById("top_menu");

	// 모바일 화면에서 탑메뉴 선택 바꿀시에
	$('.top_menu select').change(function(e){
		fnChgLeftMenu(target.options[target.selectedIndex].value);
//		location.href = $(this).val();
	});
})

function fnChgLeftMenu(menu_id){

	var param = {
		'up_menu_id' : menu_id
	};

	$.ajax({
		url : '/common/inc/selectLeftMenuListAjax.do',
		type : 'post',
		dataType : 'json',
		data : param,
		async: false,
		success : function(response){
			if(response.resultStats.resultCode == 'ok'){

				var subMenuList = response.resultStats.leftMenuList;
				var sideHtml = '';
				var menuClassStr = '';
				var menuExpend = '';
				var menuNum = 0;
				var viewSub = '';
				var menu = '';

				//메뉴 초기화
				$("#sidebar").html("");
				$("#tmpMenu").html("");
				//===========================================
				for(var i = 0; i < subMenuList.length; i++){

					//tmpMenu에서 DOM 로딩
					sideHtml = $("#tmpMenu").html();
					menuClassStr = '';
					menuExpend = '';

					// 메뉴가 4depth(16자리에서 사이드메뉴로 빠짐)에서 부터 활성화 클래스가 나온다.
					// 총메뉴가 6depth(24자리)까지 나오는데 6depth는 기능메뉴로서 5depth메뉴와 4depth메뉴를 활성화를 시켜주어야한다.
					// 그래서 0, 16자리까지 자른게 같으면 클래스를 활성화를 시켜준다.

					// 현재 URL 에 맵핑된 menu_id
					var c_menu = '${menu_id}';
					// 4depth메뉴가 같다면 활성화
					if(subMenuList[i].MENU_ID == c_menu.substring(0, 16)){
						menuClassStr = 'active';
						menuExpend = 'menu-open';
					}

					// 메뉴 아이콘
					var menuIcon = "";
					if (subMenuList[i].ICON_NM == null || subMenuList[i].ICON_NM == "") {
						menuIcon = '<ion-icon name="chevron-forward-outline"></ion-icon>';
					}else{
						menuIcon = '<ion-icon name="'+ subMenuList[i].ICON_NM + '"></ion-icon>';
					}

					if(subMenuList[i].MENU_LEVEL == '4'){

						//메뉴유형(MENU_TY_CODE)
						//카테고리	10
						//메뉴	20
						//기능	30
						if(subMenuList[i].MENU_TY_CODE == '10'){
							sideHtml += '<li class="nav-item has-treeview ' + menuExpend + '">';
							sideHtml += '	<a href="#" class="nav-link ' + menuClassStr + '">';
							sideHtml += menuIcon;
							sideHtml += '		<p>' + subMenuList[i].MENU_NM;
							sideHtml += '			<i class="right fas fa-angle-left"></i>';
							sideHtml += '		</p>';
							sideHtml += '	</a>';
							sideHtml += '	<ul class="nav nav-treeview">';
							sideHtml += '	</ul>';
							sideHtml += '</li>';

							menu = true;
						}else{
							sideHtml += '<li class="nav-item">';
							sideHtml += '	<a href="javascript:fnGoUrl(\'' + subMenuList[i].URL + '\', \'' + menu_id + '\');" class="nav-link ' + menuClassStr + '">';
							sideHtml += menuIcon;
							sideHtml += '		<p class="nav-item-p">' + subMenuList[i].MENU_NM + '</p>';
							sideHtml += '	</a>';
							sideHtml += '</li>';

							menu = false;
						}

						viewSub = menuClassStr;

						//tmpMenu에서 DOM 저장
						$("#tmpMenu").html(sideHtml);
					}
					else if(subMenuList[i].MENU_LEVEL == '5'){
						if(menu)
						{
							var off = '';
							// 5depth 메뉴까지 존재하는경우 20자리까지 잘라서 맞는경우만 active해준다.
							if(subMenuList[i].MENU_ID == c_menu.substring(0, 20)){
								off = 'active';
							}

							//tmpMenu에서 4뎁스 생성
							var str4 = '';

							str4 += '<li class="nav-item">';
							str4 += '	<a class="nav-link ' + off + '" href="javascript:fnGoUrl(\'' + subMenuList[i].URL + '\', \'' + menu_id + '\');">';
							str4 += '		<ion-icon name="ellipse"></ion-icon>';
							str4 += '		<p>' + subMenuList[i].MENU_NM + '</p>';
							str4 += '	</a>';
							str4 += '</li>';

							$("#tmpMenu").find("ul").eq(-1).append(str4);
						}
					}
				}
				$("#sidebar").append($("#tmpMenu").html());

				// left menu 모두 그린후 타이틀 설정
				//fnSetTitle();
			} else {
			}
		},
		error : function(){
		}
	});
}

function fnGoUrl(url, top_menu_id) {
	var f = document.menuForm;
	f.action = url;
	f.submit();

}
</script>

<aside class="main-sidebar elevation-4 ${ssThemaOption.t3} ${ssThemaOption.c5}">
	<!-- Brand Logo -->
	<a href="/" class="${ssThemaOption.t4}">
		<img src="/common/images/logo/logo_gl.svg" alt="NTSYS Logo" class="brand-image" style="height: 71px; margin-left: 20px; width: 160px;" onerror="/common/images/logo/logo_gl.svg">
		<span class="brand-text font-weight-light" id="logo_w"></span>
	</a>

	<!-- Sidebar -->
	<div class="sidebar">
		<!-- Sidebar user panel (optional) -->
		<div class="user-panel mt-3 pb-3 mb-3 d-flex">
			<div class="info top_menu">
				<select class="form-control input-sm" id="top_menu">
					${CommboUtil:getComboStr(topMenuList, 'MENU_ID', 'MENU_NM', top_menu_id, '')}
				</select>
			</div>
		</div>

		<!-- Sidebar Menu -->
		<nav class="mt-3">
			<ul id="sidebar" class="nav nav-pills nav-sidebar flex-column nav-flat nav-child-indent ${ssThemaOption.c3} ${ssThemaOption.c4} " data-widget="treeview" role="menu" data-accordion="false">
			</ul>
		</nav>
		<!-- /.sidebar-menu -->
	</div>
	<!-- /.sidebar -->
</aside>

<div id="tmpMenu" style="display:none;"></div>
<form name="menuForm" method="POST" action="" style="display:none;"></form>
