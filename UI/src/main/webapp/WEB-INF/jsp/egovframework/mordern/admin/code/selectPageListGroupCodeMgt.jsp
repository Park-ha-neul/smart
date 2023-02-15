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

	function fnGoPage(currentPage) {
		$("#currentPage").val(currentPage);
		$("#aform").attr({
			action : "/admin/code/selectPageListGroupCodeMgt.do",
			method : 'post'
		}).submit();
	}
	$(function() {
		//f_divView('main_0', 'main_0');	//메뉴구조 유지
	});

	function fnInsertForm() {
		document.location.href = "/admin/code/insertFormGroupCodeMgt.do";
	}

	function fnSelectCodeList(groupId) {
		$("#group_id").val(groupId);
		$("#aform").attr({action : "/admin/code/selectListCodeMgt.do", method : 'post'}).submit();
	}

	function fnSearch() {
		$("#aform").attr({action : "/admin/code/selectPageListGroupCodeMgt.do", method : 'post'}).submit();
	}

	function fnDelete() {
		var delGrpCodeList = f_getList("del_grp_code");
		var chkFag = false;

		for (i = 0; i < delGrpCodeList.length; i++) {
			if (delGrpCodeList[i].checked == true) {
				chkFag = true;
				break;
			}
		}

		if (chkFag == false) {
			alert("삭제할 그룹코드를 선택해 주세요.");
			return;
		}

		if (confirm("삭제하시겠습니까?")) {
			$("#aform").attr({action : "/admin/code/deleteGroupCodeMgt.do", method : 'post'}).submit();
		}
	}

	function fnRefreshCache() {
		if (confirm("코드 관리의 모든 변경 사항이 즉시 적용됩니다.\n새로고침 하시겠습니까?")) {
			$("#aform").attr({action : "/admin/code/refreshCacheCode.do",	method : 'get'}).submit();
		}
	}

	$(function() {
		// 목록 수 셀렉트 박스 변경시 조회
		$("#sch_row_per_page").change(function() {
			fnSearch();
		});
	});

	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form id="aform" method="post" action="/admin/code/selectPageListGroupCodeMgt.do">
					<input type="hidden" name="group_id" id="group_id" />
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
									<div class="input-group input-group-sm mr-1">
										<input type="text" class="form-control form-control-sm" name="sch_grp_code_nm" title="그룹코드명을 입력하세요." placeholder="그룹코드명을 입력하세요." value="${requestScope.param.sch_grp_code_nm}" />
										<div class="input-group-append">
											<button type="button" class="input-group-btn btn btn-default btn-outline-dark" onclick="fnSearch(); return false;">
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
										<col width="5%;">
										<col width="5%;">
										<col width="*;">
										<col width="*;">
										<col width="*;">
										<col width="15%;">
										<col width="15%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center"></th>
											<th class="text-center">No</th>
											<th class="text-center">그룹코드</th>
											<th class="text-center">그룹코드 명</th>
											<th class="text-center">영문그룹코드 명</th>
											<th class="text-center">등록자</th>
											<th class="text-center">등록일</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="group" items="${resultList}" varStatus="status">
											<tr style="cursor: pointer; cursor: hand;">
												<td class="text-center">
													<div class="form-group">
														<input type="checkbox" name="del_grp_code" id="chk_del_${status.index}" value="${group.GROUP_ID }" />
														<label for="chk_del_${status.index}">&nbsp;</label>
													</div>
												</td>
												<td class="text-center" onclick="fnSelectCodeList('${group.GROUP_ID}')"><c:out value="${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}" />
												</td>
												<td onclick="fnSelectCodeList('${group.GROUP_ID}')">${group.GROUP_ID }</td>
												<td onclick="fnSelectCodeList('${group.GROUP_ID}')">${group.GROUP_NM }</td>
												<td onclick="fnSelectCodeList('${group.GROUP_ID}')">${group.GROUP_NM_ENG}</td>
												<td onclick="fnSelectCodeList('${group.GROUP_ID}')" class="text-center">${group.REGISTER_NM}</td>
												<td onclick="fnSelectCodeList('${group.GROUP_ID}')" class="text-center">${group.REGIST_YMD}</td>
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
							<button type="button" class="btn bg-gradient-success" onclick="fnRefreshCache(); return false;">캐쉬 새로고침</button>
							<button type="button" class="btn bg-gradient-danger float-right" onclick="fnDelete(); return false;">삭제</button>
							<button type="button" class="btn bg-gradient-success float-right mr-2" onclick="fnInsertForm(); return false;">등록</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
