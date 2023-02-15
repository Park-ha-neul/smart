<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	function fnGoPage(currentPage){
		$("#currentPage").val(currentPage);
		$("#aform").attr({action:"/admin/author/selectPageListAuthorMgt.do", method:'post'}).submit();
	}

	function fnInsertForm(){
		document.location.href="/admin/author/insertFormAuthorMgt.do";
	}

	function fnUpdateForm(author_id){
		$("#pageList_currentPage").val($("#currentPage").val());
		$("#pageList_sch_row_per_page").val($("#sch_row_per_page").val());
		$("#currentPage").val("1");
		$("#sch_row_per_page").empty();

		$("#author_id").val(author_id);
		$("#aform").attr({action:"/admin/author/updateFormAuthorMgt.do", method:'post'}).submit();
	}

	function fnSearch(){
		$("#aform").attr({action:"/admin/author/selectPageListAuthorMgt.do", method:'post'}).submit();
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
				<form role="form" id="aform" method="post" action="/admin/author/selectPageListAuthorMgt.do">
					<%-- 검색 조건 --%>
					<input type="hidden" id="pageList_currentPage" name="pageList_currentPage" />
					<input type="hidden" id="pageList_sch_row_per_page" name="pageList_sch_row_per_page" />
					<%-- 권한 아이디 --%>
					<input type="hidden" id="author_id" name="author_id" />

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
									<div class="input-group input-group-sm mr-1">
										<input type="text" class="form-control form-control-sm" name="sch_auth_nm" title="권한명을 입력하세요." placeholder="권한명을 입력하세요." value="${requestScope.param.sch_auth_nm}" />
										<div class="input-group-append">
											<button type="button" class="input-group-btn btn btn-default btn-outline-dark" onclick="fnSearch(); return false;"><i class="fa fa-search"></i></button>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table table-hover text-nowrap table-grid">
									<colgroup>
										<col width="80px" />
										<col width="200px" />
										<col width="*" />
										<col width="150px" />
										<col width="150px" />
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">권한 ID</th>
											<th class="text-center">권한명</th>
											<th class="text-center">등록자</th>
											<th class="text-center">등록일</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="author" items="${resultList}" varStatus="status">
										<tr style="cursor:pointer;cursor:hand;" onclick="fnUpdateForm('${author.AUTHOR_ID}')">
											<td class="text-center"><c:out value="${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}" /></td>
											<td class="text-center">${author.AUTHOR_ID }</td>
											<td>${author.AUTHOR_NM }</td>
											<td class="text-center">${author.REGISTER_NM }</td>
											<td class="text-center">${author.REGIST_YMD }</td>
										</tr>
										</c:forEach>
										<c:if test="${fn:length(resultList) == 0}">
										<tr>
											<td class="text-center" colspan="5" ><spring:message code="msg.data.empty" /></td>
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
