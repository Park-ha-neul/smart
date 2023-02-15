<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<style>

.modal {
        text-align: center;
}

@media screen and (min-width: 768px) {
        .modal:before {
                display: inline-block;
                vertical-align: middle;
                content: " ";
                height: 100%;
        }
}

.modal-dialog {
        display: inline-block;
        text-align: left;
        vertical-align: middle;
        width:500px;
}

</style>

<script type="text/javascript">

function checkUserId() {
	return new Promise(function(resolve, reject){
		$.ajax({
			url : '/admin/registerCheckUserIdAjax.do',
			data : {"id": $("#r_user_id").val()},
			type : 'post',
			dataType : 'json',
			success : function(response){
				
				var exist = false;
				
				if(response.resultMsg == 'exist'){
					exist = true;
				} 
				
				resolve(exist);

			}
		});
	})
}

// 회원가입
async function fnRegister(){
	
	console.log($('#rform').serialize());
	
	var idLen = 20;
	var exist = false;
	
	if($("#r_user_id").val() == ""){
		alert("아이디를 입력해주세요.");
		$("#r_user_id").focus();
		return;
	}
	
	if($("#r_user_id").val().length > idLen){
		alert("아이디는 20자리 이내로 생성해주세요.");
		$("#r_user_id").focus();
		return;
	}
	
	var resultExist = await checkUserId();
	
	if(resultExist) {
		alert("이미 존재하는 아이디 입니다.");
		$('#r_user_id').focus();
		return;
	}
	
	// 비밀번호 유효성 검사
	var pw = $("[name=r_password]").val(); 					//비밀번호
	var pw2 = $("[name=r_password_check]").val(); 	// 확인 비밀번호
	var id = $("[name=r_user_id]").val();				 	// 아이디

	var pattern1 = /[0-9]/;								// 숫자
	var pattern2 = /[a-zA-Z]/;						// 영문
	var pattern3 = /[~!@\#$%<>^&*]/;			// 원하는 특수문자 추가 제거

	var pwLen = 8;		// 생성 비밀번호 길이
	
	if ($("[name=r_password]").val() == "") {
		alert("비밀번호를 입력해주세요.");
		$("[name=r_password]").focus();
		return;
	}

	if ($("[name=r_password_check]").val() == "") {
		alert("비밀번호 확인란을 입력해주세요.");
		$("[name=r_password_check]").focus();
		return;
	}

	if($("[name=r_password]").val() != $("[name=r_password_check]").val()){
		alert("비밀번호가 일치하지 않습니다.");
		$("[name=r_password_check]").focus();
		return;
	}

	if(!pattern1.test(pw)||!pattern2.test(pw)||!pattern3.test(pw)||pw.length<pwLen||pw.length>50){
		alert("특수문자 / 문자 / 숫자 포함 "+pwLen+"자리 이상으로 구성하여야 합니다.");
		return;
	}
	
	if($("#r_user_name").val() == ""){
		alert("이름을 입력해주세요.");
		$("#r_user_name").focus();
		return;
	}
	
	if(confirm("가입하시겠습니까?")){
		$.ajax({
			url : '/admin/registerUserAjax.do',
			data : $('#rform').serialize(),
			type : 'post',
			dataType : 'json',
			success : function(response){
				
				if(response.resultMsg == "success"){
					$('#registerModal').find('form')[0].reset();
					$('#registerModal').modal('hide');
					alert('가입 완료 되었습니다.');
					
					return;
				} else {
					alert("회원 가입 신청 오류");
					return;
				}
				
// 				if(response.resultMsg == 'exist'){
// 					alert('이미 존재하는 아이디 입니다.');
// 					$("#r_user_id").focus();
// 					return;
// 				} else if(response.resultMsg == "success"){
// 					$('#registerModal').find('form')[0].reset();
// 					$('#registerModal').modal('hide');
// 					alert('가입 완료 되었습니다.');
					
// 					return;
// 				} else {
// 					alert("회원 가입 신청 오류");
// 					return;
// 				}

			}
		});
	}
	
}


//<![CDATA[
function fnLogin(){
	if($("#user_id").val() == ""){
		$("#idFail").modal();
		$("#user_id").focus();
		return;
	}

	if($("#user_pwd").val() == "" || $("#user_pwd").val() == null){
		$("#pwFail").modal();
		$("#user_pwd").focus();
		return;
	}

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
	var initlYn;
	$.ajax({
		url : '/admin/initlUserPasswordCheckAjax.do',
		type : 'post',
		dataType : 'json',
		data : { 'user_id' : $('[name=id]').val()},
		async : false,
		success : function(response){
			initlYn = response.resultStats.userInfoMap.PASSWORD_INITL_YN;
			modal_user_id = response.resultStats.userInfoMap.USER_ID;
			modal_user_no = response.resultStats.userInfoMap.USER_NO;

			$("[name=modal_user_id]").val(modal_user_id);
			$("[name=user_no]").val(modal_user_no);
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

$(document).ready(function(){
	
	<c:if test="${msg != '' && msg != null}">
		$('#loginFail').modal();
		return;
	</c:if>
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
				$('#initlPasswordModal [name=modal_table_user_id]').text(response.resultMap.USER_ID);
				$('#initlPasswordModal [name=modal_table_user_nm]').text(response.resultMap.USER_NM);
			},
		});
	});
});

// ]]>
</script>
<section class="material-half-bg">
	<div class="cover"></div>
</section>

	<div id="wrapper">
		<div class="cell" style="display: flex;flex-direction: row;align-content: center;justify-content: center;align-items: center;">
			<div class="login-bg">
				<video loop autoplay id="videoPlayer" muted="muted" style="width:100vw; height:100vh">
				<source src="/common/video/login_vod.mp4" type="video/mp4">
					"Your browser does not support the video tag."
				</video>
			</div>
			<div class="contents" style="top: auto;left: auto;">
					<form id="aform" class="login-form" name="aform" action ="/admin/login.do" method="post">
						 <fieldset>
						 	<legend>login page</legend>
							<div class="input_box">
								<!-- <span>ID</span> -->
						 		<div>
									<img src="../common/images/login/input.png" alt="">
									<input type="text"  id="user_id" name="id" title="아이디 입력" placeholder="ID*" class="form_control id_input" size="50">
									<span class="clear_btn">텍스트삭제버튼</span>
						 		</div>
						 		<!-- <span>PASSWORD</span> -->
						 		 <div>
									<img src="../common/images/login/input2.png" alt="">
									<input type="password" id="user_pwd" name="pwd" title="비밀번호 입력" placeholder="PASSWORD*" class="form_control pwd_input">
									<span class="clear_btn2">텍스트삭제버튼</span>
								</div>
							</div>
							<button type="button" onclick="fnLogin(); return false;">LOGIN</button>
							<button type="button" class="btn-success" id="registerModalbtn" data-toggle="modal" data-target="#registerModal">REGISTER</button>
						</fieldset>
					</form>
			</div>
		</div>
	</div>

<div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby=”myModalLabel“>
	<div class="modal-dialog" role="document" style="width:70%">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">회원가입</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			</div>
			<div class="modal-body">
				<form id="rform" name="rform" action="/admin/user/registerUserMgtAjax.do" method="post">
					<table class="table table-bordered">
						<colgroup>
							<col style="width:30%;">
							<col style="width:70%;">
						</colgroup>
						<tbody>
							<tr>
								<th>사용자ID*</th>
								<td>
									<input type="text" class="form-control" name="r_user_id" id="r_user_id" maxlength="100" placeholder="아이디"/>
								</td>
							</tr>
							<tr>
								<th>비밀번호*</th>
								<td>
									<input type="password" class="form-control" name="r_password" maxlength="100" placeholder="비밀번호"/>
								</td>
							</tr>
							<tr>
								<th>비밀번호 확인*</th>
								<td>
									<input type="password" class="form-control" name="r_password_check" maxlength="100" placeholder="비밀번호 확인" onKeyDown="javascript:if(event.keyCode == 13) {fnRegister();}"/>
								</td>
							</tr>
							<tr>
								<th>이름*</th>
								<td>
									<input type="text" class="form-control" name="r_user_name" id="r_user_name" maxlength="10" placeholder="이름"/>
								</td>
							</tr>
						</tbody>
					</table>
					<p class="text-center" style="color:red;font-weight: bold"> ※ 비밀번호 : 특수문자 / 문자 / 숫자 포함 8자리 이상의 암호로 생성 </p>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-reply"></i> 취소</button>
				<button type="button" class="btn btn-info" onclick="fnRegister(); return false;"><i class="fa fa-pencil"></i>가입 </button>
			</div>		
		</div>
	</div>
</div>

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
					<input type="hidden" id="modal_user_id" name="modal_user_id"/>
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
								<td name="modal_table_user_id">
								</td>
								<th rowspan="2">비밀번호</th>
								<td rowspan="2">
									<input type="password" class="form-control" name="modal_password" maxlength="100" placeholder="변경 비밀번호"/>
									<input type="password" class="form-control" name="modal_password_check" maxlength="100" placeholder="변경 비밀번호 확인" style="margin-top:10px;" onKeyDown="javascript:if(event.keyCode == 13) {fnGoUpdateModal();}"/>
								</td>
							</tr>
							<tr>
								<th>이름</th>
								<td name="modal_table_user_nm" >
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




<div class="modal" tabindex="-1" role="dialog" id="idFail">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p style="color:white;">ID를 입력해 주세요.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>




<div class="modal" tabindex="-1" role="dialog" id="pwFail">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p style="color:white;">패스워드를 입력해 주세요.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>





<div class="modal" tabindex="-1" role="dialog" id="loginFail">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p style="color:white;">${msg }</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>



