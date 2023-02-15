<%@page import="org.springframework.web.context.annotation.RequestScope"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="egovframework.framework.common.util.CommboUtil"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<jsp:useBean id="resultList" type="java.util.List" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="fileList" type="java.util.List" class="java.util.ArrayList" scope="request"/>

<script type="text/javascript">

//<![CDATA[
	$(document).ready(function(){

				$('[name=sch_text]').on({
					'keydown' : function(e){
						if(e.which == 13){
							e.preventDefault();
						}
					},
					'keyup' : function(e){
						if(e.which == 13){
							fnSearch();
						}
					}
				})

			})

	function fnInsertForm() {

		$("#aform").attr({action : "/admin/stdTerm/insertFormStdTermMgt.do", method : 'post'}).submit();
	}

	function fnSearch() {
		$('#currentPage').val('1');
		$("#aform").attr({action : "/admin/stdTerm/selectPageListStdTermMgt.do", method : 'post'}).submit();
	}

	function fnGoPage(currentPage) {
		$("#currentPage").val(currentPage);
		$("#aform").attr({
			action : "/admin/stdTerm/selectPageListStdTermMgt.do",
			method : 'post'
		}).submit();
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
				<form id="aform" method="post" action="/admin/stdTerm/selectPageListStdTermMgt.do">
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
								<div class="form-inline" style = "padding: 5px;">
								    <div class="form-group input-group-sm " style = "padding: 5px;" >
										<select id="fileList" name="fileCode" class="form-control form-control-sm" style = "padding: 5px;" onchange="fnSearch();">
											 <%=CommboUtil.getComboStr(fileList, "FILE_ID", "FILE_NAME", param.getString("fileCode"), "")%>
										</select>
									</div>
									<div class="input-group input-group-sm mr-1" >
										<input type="text" class="form-control form-control-sm" name="sch_text" title="용어명, 영문명, 약어명" placeholder="용어명, 영문명, 약어명" value="${requestScope.param.sch_text}" />
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
										<col width="10%;">
										<col width="10%;">
										<col width="*%;">
										<col width="5%;">
										<col width="20%;">
										<col width="5%;">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">구분</th>
											<th class="text-center">용어명</th>
											<th class="text-center">용어영문명</th>
											<th class="text-center">영문약어명</th>
											<th class="text-center">등록일</th>
											<th class="text-center">상태</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="list" items="${resultList}" varStatus="status">
											<tr style="cursor: pointer; cursor: hand;">
												<td class="text-center"><c:out value="${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}" /></td>
												<td class="text-center">${list.TERM_CODE}</td>
												<td class="text-center">${list.TERM_NAME}</td>
												<td class="text-center">${list.TERM_ENG_NAME}</td>
												<td class="text-center">${list.ENG_ABBREVIATION}</td>
												<td class="text-center">${list.REGIST_DT}</td>
												<td class="text-center">${list.USE_YN}</td>
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
							<button type="button" class="btn bg-gradient-success float-right mr-2" onclick="fnInsertForm(); return false;">등록</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>