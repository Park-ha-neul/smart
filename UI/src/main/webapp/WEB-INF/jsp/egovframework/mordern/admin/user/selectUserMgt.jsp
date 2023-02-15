<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<script type="text/javascript">
//<![CDATA[
	// 목록
	function fnGoList(){
		$("#aform").attr({action:"/admin/user/selectPageListUserMgt.do", method:'post'}).submit();
	}
	// 사용자수정폼 이동
	function fnGoUpdateForm(user_no) {
		$("#aform").action = "/admin/user/updateFormUserMgt.do";
		$("#aform").submit();
	}
	// 사용자삭제
	function fnGoDelete(user_no){
		if(confirm("삭제하시겠습니까?")){
			$("#aform").attr({action:"/admin/user/deleteUserMgt.do", method:'post'}).submit();
		}
	}
	// 비밀번호 초기화
	function fnInitl(){
		if(confirm("비밀번호를 초기화하시겠습니까?")){
			var param = {'user_no' : $('[name=user_no]').val()};
			$.ajax({
				url : '/admin/user/initlUserPasswordAjax.do',
				type : 'post',
				dataType : 'json',
				data : param,
				async : false,
				success : function(response){
					if(response.resultStats.resultCode == 'success'){
							alert(response.resultStats.resultMsg);
					} else {
						ajaxErrorMsg(response)
					}
				},
				error : function(jqXHR, textStatus, thrownError){
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
				<form role="form" id="aform" method="post" action="/admin/user/updateFormUserMgt.do">
					<input type="hidden" id="user_no" name="user_no" value="${resultMap.USER_NO}" />
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
										<th>사용자 권한</th>
											<td colspan="3"><c:forEach var="auth" items="${authList}" varStatus="status">
												${auth.AUTHOR_NM}
												<c:if test="${status.index != fn:length(authList) - 1}"> ,&nbsp;
												</c:if>
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
											<th>사용자ID</th>
											<td>${resultMap.USER_ID}</td>
											<th>비밀번호</th>
											<td>
												<div class="pull-left">
													<button class="btn bg-gradient-success btn-sm" type="button" onclick="fnInitl(); return false;">비밀번호 초기화</button>
												</div>
											</td>
										</tr>
										<tr>
											<th>이름</th>
											<td>${resultMap.USER_NM}</td>
											<th>사용자 구분</th>
											<td>${resultMap.USER_SE_NM}</td>
										</tr>
										<tr>
											<th>사용자 상태</th>
											<td>${resultMap.USER_STTUS_NM}</td>
											<th>비밀번호 초기화 여부</th>
											<td><c:if test="${resultMap.PASSWORD_INITL_YN eq 'N'}">
												아니오
												</c:if>
												<c:if test="${resultMap.PASSWORD_INITL_YN eq 'Y'}">
												예
												</c:if></td>
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
										<col style="width: 35%;">
										<col style="width: 15%;">
										<col style="width: 35%;">
									</colgroup>
									<tbody>
										<tr>
											<th>연락처</th>
											<c:choose>
												<c:when test="${resultMap.CTTPC_SE_NM eq ''}">
													<td></td>
												</c:when>
												<c:when test="${resultMap.CTTPC_SE_NM eq null}">
													<td></td>
												</c:when>
												<c:when test="${resultMap.CTTPC eq ''}">
													<td></td>
												</c:when>
												<c:when test="${resultMap.CTTPC eq null}">
													<td></td>
												</c:when>
												<c:otherwise>
											<td>[${resultMap.CTTPC_SE_NM}] ${resultMap.CTTPC}</td>
												</c:otherwise>
											</c:choose>
											<th>이메일</th>
											<td>${resultMap.EMAIL}</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnGoList(); return false;">목록</button>
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnGoUpdateForm(); return false;">수정</button>
									<button type="button" class="btn bg-gradient-danger" onclick="fnGoDelete(); return false;">삭제</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
