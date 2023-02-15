<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	$(function(){
	});

	// 목록
	function fnGoList(){
		$("#aform").attr({action:"/admin/mber/selectPageListMberMgt.do", method:'post'}).submit();
	}

	// 수정폼 이동
	function fnGoUpdateForm(){
		$("#aform").attr({action:"/admin/mber/updateFormMberMgt.do", method:'get'}).submit();
	}

	// 삭제
	function fnGoDelete(){
		if(confirm("삭제하시겠습니까?")){
			$("#aform").attr({action:"/admin/mber/deleteMberMgt.do", method:'post'}).submit();
		}
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/mber/updateFormMberMgt.do">
					<input type="hidden" name="user_no" value="${resultMap.USER_NO}" />
					<input type="hidden" name="sch_type" value="${requestScope.param.sch_type}" />
					<input type="hidden" name="sch_text" value="${requestScope.param.sch_text}" />
					<input type="hidden" name="currentPage" value="${requestScope.param.currentPage}"/>

					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
									<table class="table text-nowrap">
										<colgroup>
											<col style="width:15%;">
											<col style="width:35%;">
											<col style="width:15%;">
											<col style="width:35%;">
										</colgroup>
										<tbody>
											<tr>
												<th>아이디</th>
												<td>
													${resultMap.ID}
												</td>
												<th>이름</th>
												<td>
													${resultMap.NM}
												</td>
											</tr>
											<tr>
												<th>회원 상태 코드</th>
												<td colspan="3">
													${resultMap.USER_STTUS_NM}
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
