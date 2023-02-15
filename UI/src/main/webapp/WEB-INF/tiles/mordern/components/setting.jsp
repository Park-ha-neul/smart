<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	$(function(){
		// 글자크기 작게
		$('#chk-font-size-down').click(function(){
			// 활성화시
			if($(this).prop('checked')){
				$('body').addClass($(this).val());
				$(window).scroll();
			} else {
				$('body').removeClass($(this).val());
				$(window).scroll();
			}
		});

		// 상단 글자 크기 작게
		$('#chk-navi-font-size-down').click(function(){
			// 활성화시
			if($(this).prop('checked')){
				$('.main-header').addClass($(this).val());
				$('.control-sidebar.control-sidebar-dark').css('top', '48px');
			// 글자 크기 작게 활성화시
			} else {
				if ($('#chk-font-size-down').prop('checked')) {
					$('.main-header').addClass($(this).val());
					$('.control-sidebar.control-sidebar-dark').css('top', '48px');
				} else {
					$('.main-header').removeClass($(this).val());
					$('.control-sidebar.control-sidebar-dark').css('top', '57px');
				}
			}
		});

		// 좌측메뉴 글자 크기 작게, 좌측메뉴 크기 작게, 좌측메뉴 flat, 좌측메뉴 legacy
		$('#chk-left-font-size-down, #chk-left-compact, #chk-left-flat, #chk-left-legacy').click(function(){
			// 활성화시
			if($(this).prop('checked')){
				$('.nav-sidebar').addClass($(this).val());
			} else {
				$('.nav-sidebar').removeClass($(this).val());
			}
		});

		// 좌측메뉴 축소 후 자동 확장 안함
		$('#chk-left-no-expand').click(function(){
			// 활성화시
			if($(this).prop('checked')){
				$('.main-sidebar').addClass($(this).val());
			} else {
				$('.main-sidebar').removeClass($(this).val());
			}
		});

		$('[name=theme]').change(function(e){
			// 기존에 등록되어있는 클래스 삭제
			$('[name=theme]').each(function(e1){
				var t2 = $(this).val().split(':');
				$('.main-header').removeClass(t2[0]);
				// $('body').removeClass(t2[1]);
				$('.main-sidebar').removeClass(t2[2]);
				$('.brand-link').removeClass(t2[3]);

				// icheck 관련 클래스 삭제
				// $('.icheck-' + t2[4]).addClass('icheck-div').removeClass('icheck-' + t2[4]);
			});

			// 선택된 테마 추가
			var theme = $(this).val().split(':');

			$('.main-header').addClass(theme[0]);
			// $('body').addClass(theme[1]);
			$('.main-sidebar').addClass(theme[2]);
			$('.brand-link').addClass(theme[3]);
			// $('.icheck-div').addClass('icheck-' + theme[4]);
		});

		$("[name='logo']").change(function(){
			var src = $(this).val();
			$(".brand-image").prop('src', src);
		});

	});

	//세팅 화면 닫기
	$(document).click(function(e){
		if($("body").hasClass("control-sidebar-slide-open")){
			if(!$(".os-content").has(e.target).length){
				$("body").removeClass("control-sidebar-slide-open");
			}
		}
	});

	function fnInsertThemaOption(){
		if(confirm('저장하시겠습니까?')){
			$.ajax({
				url : '/common/updateThemaOptionAjax.do',
				type : 'post',
				dataType : 'json',
				data : $('#optionForm').serialize(),
				success : function(param) {
					if(param.resultStats.resultCode == 'error'){
						alert(param.resultStats.resultMsg);
					} else {
						alert(param.resultStats.resultMsg);
						location.reload();
					}
				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}
			});
		}
	}

	//사용자별 셋팅 화면 높이 계산
	function calcHeight(){
		var topMenuHeight = $(".main-header").css("height");
		$(".control-sidebar.control-sidebar-dark div").css('min-height', "calc(100% - "+topMenuHeight+")");
	}
//]]>
</script>
<!-- Control Sidebar -->
<form id="optionForm" name="optionForm" method="post" action="#">
	<aside class="control-sidebar control-sidebar-dark" style="padding:0px;">
		<div class="p-3 control-sidebar-content">
			<h5>사용자별 셋팅</h5>
			<h6>공통</h6>
			<ul class="nav nav-pills flex-column">
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="checkbox" id="chk-font-size-down" name="c1" value="text-sm" <c:if test="${ssThemaOption.c1 != null && ssThemaOption.c1 != ''}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-font-size-down">글자 크기 작게</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="checkbox" id="chk-navi-font-size-down" name="c2" value="text-sm" <c:if test="${ssThemaOption.c2 != null && ssThemaOption.c2 != ''}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-navi-font-size-down">상단 글자 크기 작게</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="checkbox" id="chk-left-font-size-down" name="c3" value="text-sm" <c:if test="${ssThemaOption.c3 != null && ssThemaOption.c3 != ''}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-left-font-size-down">좌측메뉴 글자 크기 작게</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="checkbox" id="chk-left-compact" name="c4" value="nav-compact" <c:if test="${ssThemaOption.c4 != null && ssThemaOption.c4 != ''}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-left-compact">좌측메뉴 크기 작게</label>
					</div>
				</li>
				<li class="nav-item">
					<div class="form-check">
						<input class="form-check-input" type="checkbox" id="chk-left-no-expand" name="c5" value="sidebar-no-expand" <c:if test="${ssThemaOption.c5 != null && ssThemaOption.c5 != ''}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-left-no-expand">좌측메뉴 축소 후 자동 확장 안함</label>
					</div>
				</li>
			</ul>
			<h6>테마</h6>
			<c:set var="themeSetting" value="${ssThemaOption.t1}:${ssThemaOption.t2}:${ssThemaOption.t3}:${ssThemaOption.t4}" />
			<ul class="nav nav-pills flex-column">
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-theme-type1" name="theme" value="navbar-dark navbar-primary:accent-primary:sidebar-dark-primary dark:dark:primary" <c:if test="${themeSetting == 'navbar-dark navbar-primary:accent-primary:sidebar-dark-primary dark:dark'}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-theme-type1">파란색 어두운  테마</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-theme-type2" name="theme" value="navbar-light navbar-white:accent-primary:sidebar-dark-warning white :dark:primary" <c:if test="${themeSetting == 'navbar-light navbar-white:accent-primary:sidebar-dark-warning white :dark'}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-theme-type2">흰색 어두운  테마</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-theme-type3" name="theme" value="navbar-dark navbar-info:accent-primary:sidebar-dark-info dark:dark:primary" <c:if test="${themeSetting == 'navbar-dark navbar-info:accent-primary:sidebar-dark-info dark:dark'}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-theme-type3">민트색 어두운  테마</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-theme-type4" name="theme" value="navbar-light navbar-warning:accent-primary:sidebar-dark-warning dark:dark:primary" <c:if test="${themeSetting == 'navbar-light navbar-warning:accent-primary:sidebar-dark-warning dark:dark'}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-theme-type4">노란색 어두운 테마</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-theme-type5" name="theme" value="navbar-dark navbar-navy:accent-primary:sidebar-light-navy light:navbar-navy dark:primary" <c:if test="${themeSetting == 'navbar-dark navbar-navy:accent-primary:sidebar-light-navy light:navbar-navy dark'}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-theme-type5">남색 밝은 테마</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-theme-type6" name="theme" value="navbar-light navbar-warning:accent-primary:sidebar-light-warning light:navbar-warning light:primary" <c:if test="${themeSetting == 'navbar-light navbar-warning:accent-primary:sidebar-light-warning light:navbar-warning light'}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-theme-type6">노란색 밝은 테마</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-theme-type7" name="theme" value="navbar-dark navbar-info:accent-primary:sidebar-light-info light:navbar-info dark:primary" <c:if test="${themeSetting == 'navbar-dark navbar-info:accent-primary:sidebar-light-info light:navbar-info dark'}"> checked="checked" </c:if> />
						<label class="form-check-label" for="chk-theme-type7">민트색 밝은 테마</label>
					</div>
				</li>
			</ul>


			<h6>로고 이미지</h6>
			<ul class="nav nav-pills flex-column">
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-logo-type1" name="logo" value="/common/images/logo/logo_gl.png" <c:if test="${ssThemaOption.l1 eq '/common/images/logo/logo_gl.png'}">checked</c:if>/>
						<label class="form-check-label" for="chk-logo-type1">검정색 로고</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-logo-type2" name="logo" value="/common/images/logo/logo_gr.png" <c:if test="${ssThemaOption.l1 eq '/common/images/logo/logo_gr.png' }">checked</c:if>/>
						<label class="form-check-label" for="chk-logo-type2">초록색 로고</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-logo-type3" name="logo" value="/common/images/logo/logo_wh.png" <c:if test="${ssThemaOption.l1 eq '/common/images/logo/logo_wh.png' }">checked</c:if>/>
						<label class="form-check-label" for="chk-logo-type3">하얀색 로고</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-logo-type4" name="logo" value="/common/images/logo/logo_bl.png" <c:if test="${ssThemaOption.l1 eq '/common/images/logo/logo_bl.png' }">checked</c:if>/>
						<label class="form-check-label" for="chk-logo-type4">파란색 로고 A</label>
					</div>
				</li>
				<li class="nav-item mb-1">
					<div class="form-check">
						<input class="form-check-input" type="radio" id="chk-logo-type5" name="logo" value="/common/images/logo/logo_bw.png" <c:if test="${ssThemaOption.l1 eq '/common/images/logo/logo_bw.png' }">checked</c:if>/>
						<label class="form-check-label" for="chk-logo-type5">파란색 로고 B</label>
					</div>
				</li>
			</ul>

			<button type="button" class="btn btn-block bg-gradient-success" onclick="fnInsertThemaOption(); return false;" style="margin:0px 16px;width: calc(100% - 32px);">저장</button>
		</div>
	</aside>
</form>
<!-- /.control-sidebar -->