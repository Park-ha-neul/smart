<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
<script type="text/javascript">
	$(function() {
		//활동을 기본으로 검색
		$('[name=sch_user_se_code], [name=sch_user_sttus_code]').change(function(e) {
			fnSearch();
		});

		// 목록 수 셀렉트 박스 변경시 조회
		$("#sch_row_per_page").change(function() {
			fnSearch();
		});
	});

	// 페이지 이동
	function fnGoPage(currentPage) {
		$("#currentPage").val(currentPage);
		$("#aform").attr({action : "/admin/user/selectPageListUserMgt.do", method : 'post'
		}).submit();
	}

	// 사용자등록폼 이동
	function fnInsertForm() {
		document.location.href = "/admin/user/insertFormUserMgt.do";
	}

	// 상세조회
	function fnSelect(user_no) {
		$("#user_no").val(user_no);
		$("#aform").attr({action : "/admin/user/selectUserMgt.do", method : 'post'}).submit();
	}

	// 검색
	function fnSearch() {
		$("#currentPage").val("1");
		$("#aform").attr({action : "/admin/user/selectPageListUserMgt.do", method : 'post'
		}).submit();
	}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/user/selectPageListUserMgt.do">
					<input type="hidden" id="user_no" name="user_no" />
					<div class="card card-info card-outline">
						<div class="card-header">
							<div class="form-row justify-content-between">
								<div class="form-inline">
									<div class="form-group input-group-sm mb-1">
										<select id="sch_row_per_page" name="sch_row_per_page" class="form-control input-sm">
											${CacheCommboUtil:getComboStr(UPCODE_ROW_PER_PAGE, 'CODE', 'CODE_NM', requestScope.param.sch_row_per_page, '')}
										</select>
									</div>
								</div>
								<div class="form-inline">
									<select id="sch_user_se_code" name="sch_user_se_code" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_USER_SE, 'CODE', 'CODE_NM', requestScope.param.sch_user_se_code, '사용자 구분')}
									</select>
									<select id="sch_user_sttus_code" name="sch_user_sttus_code" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_USER_STTUS, 'CODE', 'CODE_NM', requestScope.param.sch_user_sttus_code, '사용자 상태')}
									</select>
									<select id="search_list_st" name="sch_type" class="form-control form-control-sm mr-1 mb-1">
										<option value="user_nm" <c:if test="${requestScope.param.sch_type == 'user_nm'}">selected="selected"</c:if>>이름</option>
										<option value="user_id" <c:if test="${requestScope.param.sch_type == 'user_id'}">selected="selected"</c:if>>사용자ID</option>
									</select>
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control form-control-sm" name="sch_text" placeholder="검색어를 입력하세요." placeholder="검색어를 입력하세요." value="${requestScope.param.sch_text}" />
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
											<th class="text-center">이름</th>
											<th class="text-center">사용자 상태</th>
											<th class="text-center">등록자</th>
											<th class="text-center">등록일</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="item" items="${resultList}" varStatus="status">
											<tr style="cursor: pointer; cursor: hand;" onclick="fnSelect('${item.USER_NO}')">
												<td class="text-center">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
												<td class="text-center">${item.USER_ID}</td>
												<td class="text-center">${item.USER_NM}</td>
												<td class="text-center">${item.USER_STTUS_NM}</td>
												<td class="text-center">${item.REGISTER_NM}</td>
												<td class="text-center">${item.REGIST_YMD}</td>
											</tr>
										</c:forEach>
										<c:if test="${fn:length(resultList) == 0}">
											<tr>
												<td class="text-center" colspan="6"><spring:message code="msg.data.empty" /></td>
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
									<button type="button" class="btn bg-gradient-success" onclick="fnInsertForm(); return false;">등록</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
