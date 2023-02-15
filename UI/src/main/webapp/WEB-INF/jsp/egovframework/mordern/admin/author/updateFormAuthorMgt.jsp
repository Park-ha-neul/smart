<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
<script type="text/javascript">
	//<![CDATA[
	$(function() {

		$('#bform [name="currentPage"]').val("1");

		// 활동을 기본으로 검색
		$('[name=sch_user_se_code], [name=sch_user_sttus_code]').change(function(e) {
			fnSearch();
		});

		// 목록 수 셀렉트 박스 변경시 조회
		$('#bform [name="sch_row_per_page"]').change(function() {
			fnSearch();
		});

		fnSearch();
	});

	// 수정
	function fnGoUpdate() {
		if ($("#author_nm").val() == "") {
			alert("권한명을 입력해 주세요.");
			$("#author_nm").focus();
			return;
		}

		if (confirm("수정하시겠습니까?")) {
			$("#aform").attr({action : "/admin/author/updateAuthorMgt.do", method : 'post'}).submit();
		}
	}

	// 삭제
	function fnGoDelete() {
		var msg = "삭제하시겠습니까?";
		if(<c:out value="${fn:length(resultList)}"/> > 0){
			msg = "해당 권한을 가진 사용자가 존재합니다.\n그래도 삭제하시겠습니까?";
		}

		if (confirm(msg)) {
			$("#aform").attr({action : "/admin/author/deleteAuthorMgt.do", method : 'post'}).submit();
		}
	}

	// 페이지 이동
	function fnGoPage(currentPage) {
		$('#aform [name="currentPage"]').val($('#bform [name="currentPage"]').val());
		$('#aform [name="sch_row_per_page"]').val($('#bform [name="sch_row_per_page"]').val());
		$("#aform").attr({action : "/admin/author/updateFormAuthorMgt.do", method : 'post'}).submit();
	}

	// 목록
	function fnGoList() {
		$("#aform").attr({action : "/admin/author/selectPageListAuthorMgt.do", method : 'post'}).submit();
	}
	
	// 검색 ajax
	function fnSearch() {

		var searchData = {
				sch_user_se_code : $("#sch_user_se_code").val(),
				sch_user_sttus_code : $("#sch_user_sttus_code").val(),
// 				sch_type : $("#sch_type").val(),
				sch_type : $("#search_list_st").val(),
				sch_text : $("#sch_text").val(),
				author_id : $("#author_id").val()
		};
		
		$.ajax({
			url : "/admin/author/updateFormAuthorMgtAjax.do",
			data : searchData,
			type : 'POST',
			dataType : 'json',
			success : function(param) {

				if (param.resultStats.resultCode == "error") {
					alert(param.resultStats.resultMsg);
					return;
				}

				var html="";

				if(param.resultList.length == 0){
					html += "<tr>";
					html += 	"<td class='text-center' colspan='10'>";
					html += 		"<spring:message code='msg.data.empty'/>";
					html += 	"</td>";
					html += "</tr>";

				} else {

					for (i=0 ; i < param.resultList.length ; i++) {
						html +=	"<tr>";
						html +=		"<td class='text-center'>"+(i+1)+"</td>";
						html +=		"<td class='text-center'>"+param.resultList[i].USER_ID+"</td>";
						html +=		"<td class='text-center'>"+param.resultList[i].USER_NM+"</td>";
						html +=		"<td class='text-center'>"+param.resultList[i].USER_STTUS_NM+"</td>";
						html +=		"<td class='text-center'>"+param.resultList[i].REGISTER_NM+"</td>";
						html +=		"<td class='text-center'>"+param.resultList[i].REGIST_YMD+"</td>";
						html +=	"</tr>";
					}
				}
				$("#tbody").html(html);
			}
		});
	}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/author/insertAuthorMgt.do">
					<!--검색조건-->
					<input type="hidden" name="author_id" value="${param.author_id}" />
					<input type="hidden" name="sch_auth_nm" value="${param.sch_auth_nm}" />
					<!--페이징조건-->
					<input type="hidden" name="currentPage" value="${param.currentPage}" />

					<div class="card card-info card-outline">
						<div class="card-header">
							<h3 class="card-title">권한</h3>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width: 20%;">
										<col style="width: 30%;">
										<col style="width: 20%;">
										<col style="width: 30%;">
									</colgroup>
									<tbody>
										<tr>
											<th>권한ID</th>
											<td>
												<div class="outBox1">${resultMap.AUTHOR_ID}</div>
											</td>
											<th class="required_field">권한명</th>
											<td>
												<div class="outBox1">
													<input type="text" class="form-control" id="author_nm" name="author_nm" value="${resultMap.AUTHOR_NM }" placeholder="권한명" onkeyup="cfLengthCheck('권한명은', this, 100);">
												</div>
											</td>
										</tr>
										<tr>
											<th>내용</th>
											<td colspan="3">
												<div class="outBox1">
													<textarea class="form-control" rows="3" name="rm" placeholder="내용..." onkeyup="cfLengthCheck('내용은', this, 100);">${resultMap.RM}</textarea>
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
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnGoUpdate(); return false;">수정</button>
									<button type="button" class="btn bg-gradient-danger" onclick="fnGoDelete(); return false;">삭제</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="col-lg-12">
				<!-- form start -->
				<form role="form" id="bform" method="post" action="/admin/author/updateFormAuthorMgt.do">
					<input type="hidden" id="author_id" name="author_id" value="${resultMap.AUTHOR_ID}"/>
					<div class="card card-info card-outline">
						<div class="card-header">
							<h3 class="card-title">권한 사용자</h3>
						</div>
						<div class="card-header">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<!--  사용자구분 -->
									<select id="sch_user_se_code" name="sch_user_se_code" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_USER_SE, 'CODE', 'CODE_NM', requestScope.param.sch_user_se_code, '사용자 구분')}
									</select>
									<!--  사용자상태 -->
									<select id="sch_user_sttus_code" name="sch_user_sttus_code" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_USER_STTUS, 'CODE', 'CODE_NM', requestScope.param.sch_user_sttus_code, '사용자 상태')}
									</select>
									<select id="search_list_st" name="sch_type" class="form-control form-control-sm mr-1 mb-1">
										<option value="user_nm"
											<c:if test="${requestScope.param.sch_type == 'user_nm'}">selected="selected"</c:if>>이름</option>
										<option value="user_id"
											<c:if test="${requestScope.param.sch_type == 'user_id'}">selected="selected"</c:if>>사용자ID</option>
									</select>
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control" id="sch_text" name="sch_text" placeholder="검색어를 입력하세요." onKeydown="if(event.keyCode==13){fnSearch(); return false;}" value="${requestScope.param.sch_text}" />
										<div class="input-group-append">
											<button type="button" class="btn btn-sm btn-default" onclick="fnSearch(); return false;">
												<i class="fa fa-search"></i>
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
						<!-- /.box-header -->
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col width="80" />
										<col width="20%" />
										<col width="*" />
										<col width="10%" />
										<col width="15%" />
										<col width="15%" />
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">사용자ID</th>
											<th class="text-center txt_overflow">이름</th>
											<th class="text-center">사용자 상태</th>
											<th class="text-center txt_overflow">등록자</th>
											<th class="text-center">등록일</th>
										</tr>
									</thead>
									<tbody id="tbody">
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
