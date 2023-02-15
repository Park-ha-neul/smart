<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>

<script type="text/javascript">
	//<![CDATA[
	//이메일 도메인 select setting
	$(function() {
		var emailList = [ "직접입력", "naver.com", "hanmail.net", "gmail.com", "nate.com", "hotmail.com" ];
		var str = "";
		for ( var i in emailList) {
			str += '<option value="'+emailList[i]+'">' + emailList[i] + '</option>';
		}
		$(".emailSelect").html(str);
	});

	function fnEmailSelect_b(v) {
		if (v == '직접입력') {
			$('[name=email2]').val("");
		} else {
			$('[name=email2]').val(v);
		}
	}

	//목록
	function fnGoList() {
		document.location.href = "/admin/user/selectPageListUserMgt.do";
	}

	//등록
	function fnGoInsert() {
		if ($("input:checkbox[name='author_id']").is(":checked") == false) {
			alert("사용자권한을 선택해 주세요.");
			$("[name=author_id]").focus();
			return;
		}
		if ($("[name=user_id]").val() == "") {
			alert("사용자ID를 입력해 주세요.");
			$("[name=user_id]").focus();
			return;
		}
		if ($("[name=user_id]").attr("duplicate") != "Y") {
			alert("사용자ID 중복검사를 해주세요.");
			$("[name=user_id]").focus();
			return;
		}
		if ($("[name=user_id]").attr("duplicate") == "Y"
				&& $("[name=user_id]").val() != $("[name=user_id]").attr("compareVal")) {
			alert("중복검사한 사용자ID가 아닙니다.");
			$("[name=user_id]").focus();
			return;
		}

		fnIdCheck('insert');

		if ($("[name=user_nm]").val() == "") {
			alert("이름을 입력해 주세요.");
			$("[name=user_nm]").focus();
			return;
		}
		if ($("[name=password]").val() == "") {
			alert("비밀번호를 입력해 주세요.");
			$("[name=password]").focus();
			return;
		}
		if ($("[name=password]").val() != $("[name=check_password]").val()) {
			alert("비밀번호가 일치하지 않습니다.");
			$("[name=password]").focus();
			return;
		}
		if ($("[name=user_sttus_code]").val() == "") {
			alert("사용자상태를 선택해 주세요.");
			$("[name=user_sttus_code]").focus();
			return;
		}
		if ($("[name=user_se_code]").val() == "") {
			alert("사용자구분을 선택해 주세요.");
			$("[name=user_se_code]").focus();
			return;
		}
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
		if (confirm("등록하시겠습니까?")) {
			$("#aform").attr({action : "/admin/user/insertUserMgt.do", method : 'post'
			}).submit();
		}
	}
	// 아이디 중복 체크
	function fnIdCheck(flag) {
		if ($("#user_id").val() == "") {
			alert("아이디를 입력해주세요.");
		} else {
			var param = {
				'user_id' : $('[name=user_id]').val()
			};
			$.ajax({
				url : '/admin/user/selectIdExistYnAjax.do',
				type : 'post',
				dataType : 'json',
				data : param,
				async : false,
				success : function(response) {
					if (response.resultStats.resultCode == 'success') {
						if (response.resultMap == 'Y') {
							alert(response.resultStats.resultMsg);
							$("#user_id").attr("duplicate", "N");
							return;
						} else {
							if (flag == 'btn')
								alert(response.resultStats.resultMsg);

							$("#user_id").attr("duplicate", "Y");
							$("#user_id").attr("compareVal", param["user_id"]);
						}
					} else {
						ajaxErrorMsg(response)
					}
				},
				error : function(jqXHR, textStatus, thrownError) {
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}
			});
		}
	}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/user/insertUserMgt.do">
					<input type="hidden" id="email" name="email" />
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
											<th class="required_field">사용자권한</th>
											<td colspan="3">
												<div class="form-group">
													<c:forEach var="auth" items="${authList}" varStatus="status">
														<c:set var="authorName" value="author${status.index}" />
														<div class="form-check">
																<input class="form-check-input" type="checkbox" name="author_id" id="${authorName}" value="${auth.AUTHOR_ID}">
																<label class="form-check-label" for="${authorName}">${auth.AUTHOR_NM}
																</label>
															</div>
													</c:forEach>
												</div>
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
											<td >
												<div class="input-group">
													<input type="text" class="form-control" id="user_id" name="user_id" placeholder="사용자ID" onkeyup="cfLengthCheck('사용자ID는', this, 100);" />
													<div class="input-group-append">
														<button type="button" class="btn bg-gradient-success" onclick="fnIdCheck('btn'); return false;">사용자ID 중복체크</button>
													</div>
												</div>
											</td>
											<th class="required_field">비밀번호</th>
											<td>
												<input type="password" class="form-control" id="password" name="password" placeholder="비밀번호" onkeyup="cfLengthCheck('비밀번호는', this, 100);" />
											</td>
										</tr>
										<tr>
											<th class="required_field">이름</th>
											<td>
												<input type="text" class="form-control" id="user_nm" name="user_nm" placeholder="이름" onkeyup="cfLengthCheck('이름은', this, 250);" />
											</td>
											<th class="required_field">비밀번호 확인</th>
											<td>
												<input type="password" class="form-control" id="check_password" name="check_password" placeholder="비밀번호 확인" onkeyup="cfLengthCheck('비밀번호는', this, 100);" />
											</td>
										</tr>
										<tr>
											<th class="required_field">사용자 상태</th>
											<td>
												<select class="form-control" id="user_sttus_code" name="user_sttus_code">
													${CacheCommboUtil:getComboStr(UPCODE_USER_STTUS, 'CODE', 'CODE_NM', requestScope.param.sch_user_sttus_code, '사용자 상태')}
												</select>
											</td>
											<th class="required_field">사용자 구분</th>
											<td>
												<select class="form-control" id="user_se_code" name="user_se_code">
													${CacheCommboUtil:getComboStr(UPCODE_USER_SE, 'CODE', 'CODE_NM', requestScope.param.sch_user_se_code, '사용자 구분')}
												</select>
											</td>
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
													<select class="form-control mr-3" name="cttpc_se_code" style="width: 80px; background: #f9f9f9; margin-right: 15px;">
														${CacheCommboUtil:getComboStr(UPCODE_CTTPC_SE, 'CODE', 'CODE_NM', '', 'C')}
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
													<input type="text" class="form-control" name="email1" placeholder="이메일" maxlength="40" style="display: inline-block; width: 150px" />
														<span style="margin: 5px">@</span> <input type="text" class="form-control" name="email2" placeholder="도메인" maxlength="40" style="display: inline-block; width: 150px" />
													<select class="emailSelect form-control" onchange="fnEmailSelect_b(this.value)" style="display: inline-block; width: 150px; margin-left: 5px; background: #f9f9f9;"></select>
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
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnGoList(); return false;">목록</button>
									<button type="button" class="btn bg-gradient-success" onclick="fnGoInsert(); return false;">등록</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
