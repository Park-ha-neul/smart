<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
function fnLogin(){

	if($("#user_id").val() == ""){
		alert("ID를 입력해 주세요.");
		$("#user_id").focus();
		return;
	}

	if($("#user_pwd").val() == ""){
		alert("패스워드를 입력해 주세요.");
		$("#user_pwd").focus();
		return;
	}

	saveid();

	// 비밀번호 초기화 여부 확인
	var initlYn = fnInitlPasswordCheck();
	if (initlYn == "Y") {
		// 비밀번호 변경 모달
		alert("비밀번호가 초기화 되었습니다. 비밀번호 변경창으로 이동됩니다.")
		$("#initlPasswordModal").modal();
	} else {
		$("#aform").attr({action:"/admin/login.do", method:'post'}).submit();
	}
}

function fnInitlPasswordCheck(){
	var initlYn
	$.ajax({
		url : '/admin/initlUserPasswordCheckAjax.do',
		type : 'post',
		dataType : 'json',
		data : { 'user_id' : $('[name=id]').val()},
		async : false,
		success : function(response){
			initlYn = response.resultStats.userInfoMap.PASSWORD_INITL_YN;
			$("[name=user_no]").val(response.resultStats.userInfoMap.USER_NO);
		},
		error : function(jqXHR, textStatus, thrownError){
			ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
		}
	});
	return initlYn;
}

//수정
function fnGoUpdateModal(){
	if ($("[name=modal_password]").val() == "") {
		alert("비밀번호를 입력해주세요.");
		$("[name=modal_password]").focus();
		return;
	}

	if ($("[name=modal_password_check]").val() == "") {
		alert("비밀번호를 입력해주세요.");
		$("[name=modal_password_check]").focus();
		return;
	}

	if($("[name=modal_password]").val() != $("[name=modal_password_check]").val()){
		alert("비밀번호가 일치하지 않습니다.");
		$("[name=modal_password_check]").focus();
		return;
	}

	// 비밀번호 유효성 검사
	var pw = $("[name=modal_password]").val(); 					//비밀번호
	var pw2 = $("[name=modal_password_check]").val(); 	// 확인 비밀번호
	var id = 	$("[name=modal_user_id]").val();				 	// 아이디

	var pattern1 = /[0-9]/;								// 숫자
	var pattern2 = /[a-zA-Z]/;						// 영문
	var pattern3 = /[~!@\#$%<>^&*]/;			// 원하는 특수문자 추가 제거

	var pwLen = 8;		// 생성 비밀번호 길이

	if(!pattern1.test(pw)||!pattern2.test(pw)||!pattern3.test(pw)||pw.length<pwLen||pw.length>50){
		alert("특수문자 / 문자 / 숫자 포함 "+pwLen+"자리 이상으로 구성하여야 합니다.");
		return;
	}

	/* 비밀번호 정규식 ***********************************************************************************/

	// 비밀번호에 id 포함 여부 정규식
	/*
	if(pw.indexOf(id) > -1) {
		alert("비밀번호는 ID를 포함할 수 없습니다.");
		return;
	}
	*/

	// 동일 문자 및 연속성 문자 정규식
	/*
	var samePass_0 = 0; //동일문자 카운트
	var samePass_1 = 0; //연속성(+) 카운드
	var samePass_2 = 0; //연속성(-) 카운드

	for(var i=0; i < pw.length; i++) {
		var chr_pass_0;
		var chr_pass_1;
		var chr_pass_2;

		if(i >= 2) {
			chr_pass_0 = pw.charCodeAt(i-2);
			chr_pass_1 = pw.charCodeAt(i-1);
			chr_pass_2 = pw.charCodeAt(i);
			//동일문자 카운트
			if((chr_pass_0 == chr_pass_1) && (chr_pass_1 == chr_pass_2)) {
				samePass_0++;
			} else {
				samePass_0 = 0;
			}
			//연속성(+) 카운드
			if(chr_pass_0 - chr_pass_1 == 1 && chr_pass_1 - chr_pass_2 == 1) {
				samePass_1++;
			} else {
				samePass_1 = 0;
			}
			//연속성(-) 카운드
			if(chr_pass_0 - chr_pass_1 == -1 && chr_pass_1 - chr_pass_2 == -1) {
				samePass_2++;
			} else {
				samePass_2 = 0;
			}
		}

		if(samePass_0 > 0) {
			alert("동일문자를 3자 이상 연속 입력할 수 없습니다.");
			return;
		}

		if(samePass_1 > 0 || samePass_2 > 0 ) {
			alert("문자, 숫자는 3자 이상 연속 입력할 수 없습니다.");
			return;
		}
	}
	*/
	/********************************************************************************************************/

	// 비밀번호 변경 모달창에서 pw 수정 여부 'Y'로 지정
	$("[name=modal_change_password_check]").val('on');
	// 비밀번호 초기화 여부 'N'으로 지정
	$("[name=modal_password_initl_yn]").val('N');


	if(confirm("수정하시겠습니까?")){
		var target = $("#modalInfo *");
		var disabled = target.find(':disabled').removeAttr('disabled');
		$.ajax({
			url : '/admin/user/updateUserMgtAjax.do',
			data : $('#modalInfo').serialize(),
			type : 'post',
			dataType : 'json',
			success : function(response){
				if(response.resultStats.resultCode == "error"){
					alert(response.resultStats.resultMsg);
					return;
				}

				alert('수정 하였습니다. 변경된 비밀번호로 다시 로그인해주세요.');
				$("#user_pwd").val();

				location.reload();
			}
		});
		disabled.attr('disabled', 'disabled');
	}
}

function setCookie (name, value, expires) {
	document.cookie = name + "=" + escape (value) + "; path=/; expires=" + expires.toGMTString();
}

function getCookie(Name) {
	var search = Name + "=";
	if (document.cookie.length > 0) { // 쿠키가 설정되어 있다면
		offset = document.cookie.indexOf(search);
		if (offset != -1) { // 쿠키가 존재하면
			offset += search.length;
			// set index of beginning of value
			end = document.cookie.indexOf(";", offset);
			// 쿠키 값의 마지막 위치 인덱스 번호 설정
			if (end == -1)
				end = document.cookie.length;
			return unescape(document.cookie.substring(offset, end));
		}
	}
	return "";
}

function saveid() {

	var form = document.aform;
	var expdate = new Date();
	// 기본적으로 30일동안 기억하게 함. 일수를 조절하려면 * 30에서 숫자를 조절하면 됨
	if (form.checkId.checked)
		expdate.setTime(expdate.getTime() + 1000 * 3600 * 24 * 30); // 30일
	else
		expdate.setTime(expdate.getTime() - 1); // 쿠키 삭제조건
	setCookie("saveid", form.id.value, expdate);
}



function getid(form) {
	form.checkId.checked = ((form.id.value = getCookie("saveid")) != "");
}

function fnInit() {
	getid(document.aform);
}

$(document).ready(function(){
	fnInit();

	// Login Page Flipbox control
	$('.login-content [data-toggle="flip"]').click(function() {
		$('.login-box').toggleClass('flipped');
		return false;
	});

	var agent = navigator.userAgent.toLowerCase();
	// ie 경우
	if((navigator.appName == 'Netscape' && agent.indexOf('trident') != -1) || (agent.indexOf("msie") != -1)){
		var h = ($('.logo').outerHeight(true) + $('.login-box').outerHeight(true)) / 2;
		$('.login-content').css('margin-top', h);
	}
	// ie 아닌 경우
	else {
	}

	$('.login-content').removeClass('invisible');

	// 비밀번호에서 엔터 치는경우
	$('#user_pwd').on({
		'keyup' : function(e){
			if(e.which == 13){
				fnLogin();
			}
		},
		'keydown' : function(e){
			if(e.which == 13){
				e.preventDefault();
			}
		},
	});
	// 회원정보 수정 모달창이 뜰때
	$("#initlPasswordModal").on("show.bs.modal", function(e){
		// 회원정보 가지고 온다.
		var param = {'user_no' : $('[name=user_no]').val()};
		$.ajax({
			url : '/admin/user/selectUserMgtAjax.do',
			data : param,
			type : 'post',
			dataType : 'json',
			success : function(response){
				if(response.resultStats.resultCode == "error"){
					alert(response.resultStats.resultMsg);
					return;
				}
				// 데이터 셋팅
				$('#initlPasswordModal .modal_user_id').html(response.resultMap.USER_ID);
				$('#initlPasswordModal .modal_user_nm').html(response.resultMap.USER_NM);
			},
		});
	});
});

// ]]>
</script>
<section class="material-half-bg">
	<div class="cover"></div>
</section>
<section class="login-content invisible">
	<div class="logo">
		<h1>${siteName}</h1>
	</div>
	<div class="login-box">
		<form id="aform" class="login-form" name="aform" action ="/admin/login.do" method="post">
			<h3 class="login-head"><i class="fa fa-lg fa-fw fa-user"></i>LOGIN</h3>
			<div class="form-group">
				<label class="control-label">ID</label>
				<input class="form-control" type="text" name="id" id="user_id" placeholder="ID" autofocus>
			</div>
			<div class="form-group">
				<label class="control-label">PASSWORD</label>
				<input class="form-control" type="password" name="pwd" id="user_pwd" placeholder="Password">
			</div>
			<div class="form-group">
				<div class="utility">
					<div class="animated-checkbox">
						<label>
							<input type="checkbox" id="checkId" name="checkId">
							<span class="label-text">ID 기억하기</span>
						</label>
					</div>
					<div class="animated-checkbox">
						<label>
							<input type="checkbox" id="checkAutoLogin" name="checkAutoLogin">
							<span class="label-text">자동 로그인</span>
						</label>
					</div>
				</div>
			</div>
			<div class="form-group btn-container">
				<button class="btn bg-gradient-success btn-block" onclick="fnLogin(); return false;"><i class="fa fa-sign-in fa-lg fa-fw"></i>로그인</button>
			</div>
		</form>
	</div>
</section>
<!-- 비밀번호 수정창 -->
<div class="modal fade" id="initlPasswordModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document" style="width:70%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">비밀번호 수정</h4>
			</div>
			<div class="modal-body">
				<form id="modalInfo" action="/admin/user/updateUserMgtAjax.do" method="post">
					<input type="hidden" id="modal_change_password_check" name="modal_change_password_check" />
					<input type="hidden" id="modal_password_initl_yn" name="modal_password_initl_yn" />
					<input type="hidden" id="user_no"name="user_no"/>
					<table class="table table-bordered">
						<colgroup>
							<col style="width:15%;">
							<col style="width:35%;">
							<col style="width:15%;">
							<col style="width:35%;">
						</colgroup>
						<tbody>
							<tr>
								<th>사용자ID</th>
								<td class="modal_user_id">
								</td>
								<th rowspan="2">비밀번호</th>
								<td rowspan="2">
									<input type="password" class="form-control" name="modal_password" maxlength="100" placeholder="변경 비밀번호"/>
									<input type="password" class="form-control" name="modal_password_check" maxlength="100" placeholder="변경 비밀번호 확인" style="margin-top:10px;" onKeyDown="javascript:if(event.keyCode == 13) {fnGoUpdateModal();}"/>
								</td>
							</tr>
							<tr>
								<th>이름</th>
								<td class="modal_user_nm">

								</td>
							</tr>
						</tbody>
					</table>
					<p class="text-center" style="color:red;font-weight: bold"> ※ 비밀번호 : 특수문자 / 문자 / 숫자 포함 8자리 이상의 암호로 생성 </p>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-reply"></i> 취소</button>
				<button type="button" class="btn btn-info" onclick="fnGoUpdateModal(); return false;"><i class="fa fa-pencil"></i> 확인</button>
			</div>
		</div>
	</div>
</div>
