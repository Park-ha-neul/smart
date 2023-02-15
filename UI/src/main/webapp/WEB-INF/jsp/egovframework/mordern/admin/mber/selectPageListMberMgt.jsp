<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
<script type="text/javascript">
	//<![CDATA[
	$(function() {
		// 상세화면 조회
		$('table tr td:not(.td_check)').click(function(e) {
			var no = $(this).closest('tr').attr('data-no');
			fnSelect(no);
		});

		// 목록 수 셀렉트 박스 변경시 조회
		$("#sch_row_per_page").change(function() {
			fnSearch();
		});

		// 전체선택시
		$('.all[type=checkbox]').change(function(e) {
			// 활성화시
			if ($(this).prop('checked')) {
				$('.tr_item [type=checkbox]').prop('checked', true);
			} else {
				$('.tr_item [type=checkbox]').prop('checked', false);
			}
		});

		// 체크박스 이벤트
		$('.tr_item [type=checkbox]').change(function(e) {
			// 전체를 모두 고른경우
			if ($('.tr_item [type=checkbox]').length == $('.tr_item [type=checkbox]:checked').length) {
				// 전체선택 활성화
				$('.all[type=checkbox]').prop('checked', true);
			} else {
				// 전체선택 비활성화
				$('.all[type=checkbox]').prop('checked', false);
			}
		});
	});

	// 페이지 이동
	function fnGoPage(currentPage) {
		$("#currentPage").val(currentPage);
		$("#aform").attr({
			action : "/admin/mber/selectPageListMberMgt.do",
			method : 'get'
		}).submit();
	}

	// 등록폼 이동
	function fnInsertForm() {
		document.location.href = "/admin/mber/insertFormMberMgt.do";
	}

	// 상세조회
	function fnSelect(user_no) {
		$("#user_no").val(user_no);
		$("#aform").attr({
			action : "/admin/mber/selectMberMgt.do",
			method : 'get'
		}).submit();
	}
	// 검색
	function fnSearch() {
		$("#currentPage").val("1");
		$("#aform").attr({
			action : "/admin/mber/selectPageListMberMgt.do",
			method : 'post'
		}).submit();
	}
	// 엑셀업로드 폼
	function fnUploadExcelForm() {
		location.href = '/admin/mber/insertExcelFormMberMgt.do';
	}

	//삭제
	function fnDelete() {
		if (confirm("선택된 데이터를 삭제하시겠습니까?")) {
			$("#aform").attr({
				action : "/admin/mber/deleteListMberMgt.do",
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
				<form role="form" id="aform" method="post"
					action="/admin/mber/selectPageListMberMgt.do">
					<input type="hidden" id="user_no" name="user_no" />
					<div class="card card-info card-outline">
						<div class="card-header">
							<div class="form-row justify-content-between">
								<div class="form-inline">
									<div class="form-group input-group-sm mb-1">
										<select id="sch_row_per_page" name="sch_row_per_page"
											class="form-control input-sm">
											${CacheCommboUtil:getComboStr(UPCODE_ROW_PER_PAGE, 'CODE', 'CODE_NM', requestScope.param.sch_row_per_page, '')}
										</select>
									</div>
								</div>
								<div class="form-inline">
									<select id="search_list_st" name="sch_type" class="form-control form-control-sm mr-1 mb-1">
										<option value="id" 1<c:if test="${requestScope.param.sch_type == 'id'}">selected="selected"</c:if>>아이디</option>
									</select>
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control form-control-sm" name="sch_text" title="검색어를 입력하세요." placeholder="검색어를 입력하세요." value="${requestScope.param.sch_text}" />
										<div class="input-group-append">
											<button type="button" class="btn btn-default btn-outline-dark" onclick="fnSearch(); return false;">
												<i class="fa fa-search"></i>
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table table-hover text-nowrap table-grid">
									<colgroup>
										<col width="5%;" />
										<col width="5%;" />
										<col width="*;" />
										<col width="15%;" />
										<col width="15%;" />
										<col width="15%;" />
										<col width="15%;" />
									</colgroup>
									<thead>
										<tr>
											<th></th>
											<th class="text-center">No</th>
											<th class="text-center">아이디</th>
											<th class="text-center">이름</th>
											<th class="text-center">회원 상태</th>
											<th class="text-center">등록자</th>
											<th class="text-center">등록일</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="item" items="${resultList}" varStatus="status">
											<tr class="tr_item" style="cursor: pointer; cursor: hand;" data-no="${item.USER_NO}">
												<td class="text-center td_check">
														<input type="checkbox" id="chk_del_${status.index}" name="del_no" value="${item.USER_NO}" />
														<label for="chk_del_${status.index}">&nbsp;</label>
												</td>
												<td class="text-center">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
												<td class="text-center">${item.ID}</td>
												<td class="text-center">${item.NM}</td>
												<td class="text-center">${item.USER_STTUS_NM}</td>
												<td class="text-center">${item.REGISTER_NM}</td>
												<td class="text-center">${item.REGIST_YMD}</td>
											</tr>
										</c:forEach>
										<c:if test="${fn:length(resultList) == 0}">
											<tr>
												<td class="text-center" colspan="7"><spring:message code="msg.data.empty" /></td>
											</tr>
										</c:if>
									</tbody>
								</table>
							</div>
						</div>
								<div class="page_box">${navigationBar}</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnInsertForm(); return false;">등록</button>
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnUploadExcelForm(); return false;">엑셀업로드</button>
									<button type="button" class="btn bg-gradient-danger" onclick="fnDelete(); return false;">삭제</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
