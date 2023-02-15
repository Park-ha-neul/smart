<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>

<script type="text/javascript">
	//<![CDATA[
	$(function() {
		// 사용자 권한 체크
		<c:forEach var="userAuth" items="${userAuthList}" varStatus="status">
		<c:set var="authorId" value="${userAuth.AUTHOR_ID}" />
		$("[value=${authorId}]").eq(0).attr("checked", "true");
		</c:forEach>

		// 비밀번호 수정 checkBox 이벤트
		$("#change_password_YN").change(function() {
			if ($("#change_password_YN").is(":checked")) {
				$("[name = password]").show();
				$("[name = check_password]").show();
			} else {
				$("[name = password]").hide();
				$("[name = check_password]").hide();
				$("[name = password]").val("");
				$("[name = check_password]").val("");
			}
		});
		// 데이터 셋팅
		var cttpc = '${resultMap.CTTPC}';
		$('input[name=cttpc]').val(cttpc);
		var email = '${resultMap.EMAIL}'.split('@');
		$('input[name=email1]').val(email[0]);
		$('input[name=email2]').val(email[1]);
		var emailList = [ "직접입력", "naver.com", "hanmail.net", "gmail.com", "nate.com", "hotmail.com" ];
		var str = "";
		for ( var i in emailList) {
			str += '<option value="'+emailList[i]+'">' + emailList[i] + '</option>';
		}
		$(".emailSelect").html(str);
	});
	function fnEmailSelect_b(v) {
		if (v == '직접입력')
			$('[name=email2]').val("");
		else
			$('[name=email2]').val(v);
	}
	// 상세
	function fnDetail() {
		$("#aform").attr({action : "/admin/user/selectUserMgt.do", method : 'post'
		}).submit();
	}
	// 수정
	function fnGoUpdate() {
		if ($("[name=cttpc]").val() != "" && $("[name=cttpc]").val() != "") {
			// 전화번호 validation
			var cttpcCheck = $("[name=cttpc]").val().replace(/-/gi, "");
			var regExp = /^[0-9]+$/;
			if (!regExp.test(cttpcCheck)) {
				alert("연락처를 다시 입력하세요. (숫자 및 하이픈(-) 외 입력제한)");
				$("[name=cttpc]").focus();
				return;
			}
			regExp = /^\d{10,13}$/;
			if (!regExp.test(cttpcCheck)) {
				alert("연락처 형식으로 다시 입력하세요.");
				$("[name=cttpc]").focus();
				return;
			}
		}
		if ($("[name=email1]").val() != "" && $("[name=email2]").val() != "") {
			$("[name=email]").val(
					$("[name=email1]").val() + "@" + $("[name=email2]").val());
		}
		if ($("[name=user_nm]").val() == "") {
			alert("이름을 입력해 주세요.");
			$("[name=user_nm]").focus();
			return;
		}
		if ($("[name=password]").val() == ""
				&& ($("#change_password_YN").is(":checked"))) {
			alert("비밀번호를 입력해 주세요.");
			$("[name=password]").focus();
			return;
		}
		if ($("input:checkbox[name='author_id']").is(":checked") == false) {
			alert("사용자 권한을 체크해 주세요");
			return;
		}
		if ($("[name=user_se_code]").val() == "") {
			alert("사용자구분을 선택해 주세요.");
			$("[name=user_se_code]").focus();
			return;
		}
		if ($("#change_password_YN").is(":checked")) {
			if ($("[name=password]").val() != $("[name=check_password]").val()) {
				alert("비밀번호가 일치하지 않습니다.");
				$("[name=password]").focus();
				return;
			}
		}
		if (confirm("수정하시겠습니까?")) {
			$("#aform").attr({
				action : "/admin/user/updateUserMgt.do",
				method : 'post'
			}).submit();
		}
	}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/user/updateUserMgt.do">
					<input type="hidden" id="user_id" name="user_id" value="${resultMap.USER_ID}" />
					<input type="hidden" id="user_no" name="user_no" value="${resultMap.USER_NO}" />
					<input type="hidden" id="email" name="email" />
					<input type="hidden" name="sch_user_se_code" value="${requestScope.param.sch_user_se_code}" />
					<input type="hidden" name="sch_user_sttus_code" value="${requestScope.param.sch_user_sttus_code}" />
					<input type="hidden" name="sch_type" value="${requestScope.param.sch_type}" />
					<input type="hidden" name="sch_text" value="${requestScope.param.sch_text}" />
					<input type="hidden" name="currentPage" value="${requestScope.param.currentPage}" />
					<%-- 사용자 권한 --%>
					<div class="card card-info card-outline">
						<div class="card-header">
							<h3 class="card-title">사용자 권한</h3>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width: 15%;">
										<col style="width: 35%;">
										<col style="width: 15%;">
										<col style="width: 35%;">
									</colgroup>
									<tbody>
										<tr>
											<th class="required_field">사용자 권한</th>
											<td colspan="3">
												<c:forEach var="auth" items="${authList}" varStatus="status">
													<c:set var="authorName" value="author${status.index}" />
													<div class="form-check">
														<input class="form-check-input" type="checkbox" name="author_id" id="${authorName}" value="${auth.AUTHOR_ID}">
														<label class="form-check-label" for="${authorName}">${auth.AUTHOR_NM}</label>
													</div>
												</c:forEach>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<%-- 필수정보 --%>
					<div class="card card-info card-outline">
						<div class="card-header">
							<h3 class="card-title">필수정보</h3>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width: 15%;">
										<col style="width: 35%;">
										<col style="width: 15%;">
										<col style="width: 35%;">
									</colgroup>
									<tbody>
										<tr>
											<th class="required_field">사용자ID</th>
											<td>
												${resultMap.USER_ID}
											</td>
											<th class="required_field">이름</th>
											<td>
												<input type="text" class="form-control" id="user_nm" name="user_nm" placeholder="이름" onkeyup="cfLengthCheck('이름은', this, 250);" value="${resultMap.USER_NM}" />
											</td>
										</tr>
										<tr>
											<th>비밀번호</th>
											<td>
												<div class="form-check">
													<input class="form-check-input" type="checkbox" id="change_password_YN" name="change_password_YN" />
													<label class="form-check-label" for="change_password_YN">수정</label>
												</div>
													<input type="password" class="form-control" name="password" placeholder="변경 비밀번호" onkeyup="cfLengthCheck('비밀번호는', this, 100);" style="display: none;" />
													<input type="password" class="form-control" name="check_password" onkeyup="cfLengthCheck('비밀번호는', this, 100);" style="display: none; margin-top: 10px;" placeholder="변경 비밀번호 확인" />
											</td>
											<th class="required_field">사용자구분</th>
											<td>
												<select class="form-control" id="user_se_code" name="user_se_code">
													${CacheCommboUtil:getComboStr(UPCODE_USER_SE, 'CODE', 'CODE_NM', resultMap.USER_SE_CODE, '')}
												</select>
											</td>
										</tr>
										<tr>
											<th class="required_field">사용자 상태</th>
											<td>
												<div class="form-group">
													<select class="form-control" id="user_sttus_code" name="user_sttus_code">
														${CacheCommboUtil:getComboStr(UPCODE_USER_STTUS, 'CODE', 'CODE_NM', resultMap.USER_STTUS_CODE, '')}
													</select>
												</div>
											</td>
											<th></th>
											<td></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<%-- 부가정보 --%>
					<div class="card card-info card-outline">
						<div class="card-header">
							<h3 class="card-title">부가정보</h3>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width: 15%;">
										<col style="width: 85%;">
									</colgroup>
									<tbody>
										<tr>
											<th>연락처</th>
											<td class="cttpc">
												<div class="form-inline">
													<select class="form-control mr-3" name="cttpc_se_code" style="width: 80px; background: #f9f9f9;">
														${CacheCommboUtil:getComboStr(UPCODE_CTTPC_SE, 'CODE', 'CODE_NM', resultMap.CTTPC_SE_CODE, '')}
													</select>
													<div class="form-group" style="text-align: left;">
														<input type="text" name="cttpc" class="form-control" maxlength="13" title="전화번호 입력칸입니다" style="width: 235px;" />
													</div>
												</div>
											</td>
										</tr>
										<tr>
											<th>이메일</th>
											<td>
												<div class="form-group" style="text-align: left">
													<input type="text" class="form-control" name="email1"	placeholder="이메일" maxlength="40" style="display: inline-block; width: 150px" />
													<span style="margin: 5px">@</span>
													<input type="text" class="form-control" name="email2" placeholder="도메인" maxlength="40" style="display: inline-block; width: 150px" />
													<select class="emailSelect form-control" onChange="fnEmailSelect_b(this.value)" style="display: inline-block; width: 150px; margin-left: 5px; background: #f9f9f9;"></select>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnDetail(); return false;">취소</button>
									<button type="button" class="btn bg-gradient-success" onclick="fnGoUpdate(); return false;">확인</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
