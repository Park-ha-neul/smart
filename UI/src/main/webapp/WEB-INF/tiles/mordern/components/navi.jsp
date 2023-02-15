<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"			uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		 uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<!-- <style>
	.nav-item:hover {
		background-color: rgba(0, 0, 0, 0.1);
	}

	.active {
		background-color: rgba(0, 0, 0, 0.1);
	}

	a.active {
		background-color: transparent;
	}
</style> -->

<script type="text/javascript">
$(document).ready(function(){

	$("#topMenuWrap li").on("click", function(){
		$("#topMenuWrap li").removeClass("active");
		$(this).addClass("active");
	});

	var emailList=["직접입력", "naver.com", "hanmail.net", "gmail.com", ,"nate.com", "hotmail.com"];
	var str="";
	for(var i in emailList){
		str+='<option value="'+emailList[i]+'">'+emailList[i]+'</option>';
	}
	$(".emailSelect").html(str);

	//비밀번호 수정 checkBox 이벤트
	$("#modal_change_password_YN").change(function(){
		if($("#modal_change_password_YN").is(":checked")){
			$("[name = modal_password]").show();
			$("[name = modal_password_check]").show();
		}else{
			$("[name = modal_password]").hide();
			$("[name = modal_password_check]").hide();
			$("[name = modal_password]").val("");
			$("[name = modal_password_check]").val("");
		}
	});


	// 회원정보 수정 모달창이 뜰때
	$("#myInfoModal").on("show.bs.modal", function(e){
		// 회원정보 가지고 온다.
		$.ajax({
			url : '/admin/user/selectUserMgtAjax.do',
			data : { 'user_no' : '${ssUserNo}'},
			type : 'post',
			dataType : 'json',
			success : function(response){
				if(response.resultStats.resultCode == "error"){
					alert(response.resultStats.resultMsg);
					return;
				}

				// 연락처 구분
				var cttpcSeComboStr = getComboStr(response.cttpcSeComboStr, 'CODE', 'CODE_NM', response.resultMap.CTTPC_SE_CODE, '');
				$('[name=modal_cttpc_se_code]').html(cttpcSeComboStr);

				// 데이터 셋팅
				$('#myInfoModal .modal_user_id').val(response.resultMap.USER_ID);
				$('#myInfoModal .table_user_id').text(response.resultMap.USER_ID);
				$('#myInfoModal .modal_user_nm').html(response.resultMap.USER_NM);
				$('#myInfoModal [name=modal_organ_code]').val(response.resultMap.ORGAN_NM);
				var email = response.resultMap.EMAIL;
				if(email!=null){
					email = email.split('@');
					$('#myInfoModal [name=modal_email1]').val(email[0]);
					$('#myInfoModal [name=modal_email2]').val(email[1]);
				}
				var cttpc =response.resultMap.CTTPC;
				if(cttpc!=null){
					$('#myInfoModal [name=modal_cttpc]').val(cttpc);

				}
			},
		});

	});

});

// 수정
function fnGoUpdateModal(){

	if($("#modal_change_password_YN").is(":checked")){
		if($("[name=modal_password]").val() == ""){
			alert("비밀번호를 입력해 주세요.");
			$("[name=modal_password]").focus();
			return;
		}

		if($("[name=modal_password]").val() != $("[name=modal_password_check]").val()){
			alert("비밀번호가 일치하지 않습니다.");
			$("[name=modal_password_check]").focus();
			return;
		}
	}

	if($("[name=modal_cttpc]").val()!="" && $("[name=modal_cttpc]").val()!=""){
		// 전화번호 validation
		var cttpcCheck = $("[name=modal_cttpc]").val().replace(/-/gi, "");

		var regExp = /^[0-9]+$/;
		if(!regExp.test(cttpcCheck)) {
			alert("연락처를 다시 입력하세요. (숫자 및 하이픈(-) 외 입력제한)");
			$("[name=modal_cttpc]").focus();
			return;
		}

		regExp = /^\d{10,13}$/;
		if(!regExp.test(cttpcCheck)) {
			alert("연락처 형식으로 다시 입력하세요.");
			$("[name=modal_cttpc]").focus();
			return;
		}
	}

	if($("[name=modal_email1]").val()!="" && $("[name=modal_email2]").val()!=""){
		$("[name=modal_email]").val($("[name=modal_email1]").val() + "@" + $("[name=modal_email2]").val());
	}


	if(confirm("수정하시겠습니까?")){
		// 회원정보 가지고 온다.
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

				alert('수정 하였습니다.');
				location.reload();
			}
		});
	}
}

function fnEmailSelect(v){
	if(v=='직접입력')	$('[name=modal_email2]').val("");
	else $('[name=modal_email2]').val(v);
}
</script>

<!-- Navbar -->
<nav class="main-header navbar navbar-expand ${ssThemaOption.c2} ${ssThemaOption.t1}" style=" padding-top: 10px; padding-bottom: 10px; padding-left: 20px; padding-right: 0px;">
	<!-- Left navbar links -->
	<ul class="navbar-nav">
		<li class="nav-item" style="padding-top:12px; padding-right:20px;">
			<a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
		</li>
	<c:forEach var="result" items="${sessionScope.topMenuList}" varStatus="status">
		<c:set var="act" value="" />
		<c:if test="${(empty top_menu_id && status.count == 1) || top_menu_id == result.MENU_ID}">
			<c:set var="act" value="active" />
		</c:if>
		<li class="nav-item d-none d-sm-inline-block ${act} text-center" style="width:200px; padding-top:10px; padding-bottom:10px; padding-left:5px; padding-right: 5px; border-radius:30px;">
			<a class="nav-link ${act}" href="${result.URL}">${result.MENU_NM }</a>
		</li>
	</c:forEach>
	</ul>

	<!-- SEARCH FORM -->
<!-- 	<form class="form-inline ml-3"> -->
<!-- 		<div class="input-group input-group-sm"> -->
<!-- 			<input class="form-control form-control-navbar" type="search" placeholder="Search" aria-label="Search"> -->
<!-- 			<div class="input-group-append"> -->
<!-- 				<button class="btn btn-navbar" type="submit"> -->
<!-- 					<i class="fas fa-search"></i> -->
<!-- 				</button> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</form> -->

	<!-- Right navbar links -->
	<ul class="navbar-nav ml-auto">
		<!-- Notifications Dropdown Menu -->
		<li class="nav-item dropdown" style="padding-top:8px; padding-bottom:8px; margin-right:250px;">
			<a class="nav-link" data-toggle="dropdown" href="#">
				<i class="fa fa-user fa-lg" style="padding-top:4px; padding-left:20px;"></i>
			</a>
			<div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
				<span class="dropdown-item dropdown-header"><a class="dropdown-item" href="#" data-toggle="modal" data-target="#myInfoModal"><i class="fa fa-user fa-lg mr-2"></i>Profile</a></span>
				<div class="dropdown-divider"></div>
				<span class="dropdown-item dropdown-header"><a class="dropdown-item" href="/admin/logout.do"><i class="fas fa-sign-out-alt fa-lg mr-2"></i>Logout</a></span>
			</div>
		</li>
<!-- 		<li class="nav-item" style="padding-top:8px; padding-bottom:8px;">
			<a class="nav-link" data-widget="control-sidebar" data-slide="true" href="#" role="button" onclick="calcHeight();">
				<i class="fas fa-cog fa-lg" style="padding-top:4px;"></i>
			</a>
		</li> -->
	</ul>
</nav>
<!-- /.navbar -->

<!-- 나의정보 수정창 -->
<div class="modal fade" id="myInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">나의 정보</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			</div>
			<div class="modal-body">
				<form id="modalInfo" action="/admin/user/updateUserMgtAjax.do" method="post">
					<!-- <input type="hidden" id="modal_cttpc" name="modal_cttpc" /> -->
					<input type="hidden" id="modal_email" name="modal_email" />
					<input type="hidden" id="modal_change_password_check" name="modal_chagne_password_check" value="on">
					<input type="hidden" id="modal_user_id" name="modal_user_id"/>
					<table class="table">
						<colgroup>
							<col style="width:15%;">
							<col style="width:35%;">
							<col style="width:15%;">
							<col style="width:35%;">
						</colgroup>
						<tbody>
							<tr>
								<th>사용자ID</th>
								<td class="table_user_id">
								</td>
								<th>비밀번호</th>
								<td>

										<input type="checkbox" id="modal_change_password_YN" name="modal_change_password_YN" value="Y" >
										<label for="modal_change_password_YN">수정</label>

									<input type="password" class="form-control" name="modal_password" maxlength="100" placeholder="변경 비밀번호" style="display:none;"/>
									<input type="password" class="form-control mt-1" name="modal_password_check" maxlength="100" placeholder="변경 비밀번호 확인" style="display:none;"/>
								</td>
							</tr>
							<tr>
								<th>이름</th>
								<td colspan="3" class = 'modal_user_nm'>
								</td>
							</tr>
							<tr>
								<th>연락처</th>
								<td colspan="3" class="cttpc">
									<div class="form-inline">
										<select class="form-control custom_form-control" name="modal_cttpc_se_code" style="width:80px; background:#f9f9f9; margin-right:15px;">
										</select>
										<div class="form-group" style="text-align:left;">
														<input type="text" name="modal_cttpc" class="form-control" maxlength="13" title="전화번호 입력칸입니다" style="width:235px;"/>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<th>이메일</th>
								<td colspan="3">
									<div class="form-inline">
										<div class="input-group input-group mr-1">
											<input type="text" class="form-control" name="modal_email1" placeholder="이메일" maxlength="20" style="display:inline-block; width:150px"/>
											<div class="input-group-prepend center">
												<div class="input-group-text">@</div>
											</div>
											<input type="text" class="form-control" name="modal_email2" placeholder="도메인" maxlength="20" style="display:inline-block; width:150px"/>
										</div>
										<select class="emailSelect form-control custom_form-control" onchange="fnEmailSelect(this.value)" style="display:inline-block; width:150px; margin-left:5px; background:#f9f9f9;"></select>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn bg-gradient-secondary" data-dismiss="modal">취소</button>
				<button type="button" class="btn bg-gradient-success" onclick="fnGoUpdateModal(); return false;">확인</button>
			</div>
		</div>
	</div>
</div>