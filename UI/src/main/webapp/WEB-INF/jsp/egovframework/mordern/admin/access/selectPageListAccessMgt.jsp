<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"
	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
<script type="text/javascript">
	//<![CDATA[
	$(function() {
		// 목록 수 셀렉트 박스 변경시 조회
		$("#sch_row_per_page").change(function() {
			fnSearch();
		});
	});

	// 페이지 이동
	function fnGoPage(currentPage) {
		$("#currentPage").val(currentPage);
		$("#aform").attr({action : "/admin/access/" + fnSysMappingCode() + "selectPageListAccessMgt.do", method : 'get'}).submit();
	}

	// 등록폼 이동
	function fnInsertForm() {
		document.location.href = "/admin/access/" + fnSysMappingCode() + "insertFormAccessMgt.do";
	}

	// 상세조회
	function fnSelect(ip) {
		$("[name=ip]").val(ip);
		$("#aform").attr({action : "/admin/access/" + fnSysMappingCode()+ "selectAccessMgt.do",method : 'get'}).submit();
	}
	// 검색
	function fnSearch() {
		$("#currentPage").val("1");
		$("#aform").attr({action : "/admin/access/" + fnSysMappingCode() + "selectPageListAccessMgt.do", method : 'post'}).submit();
	}
	// 엑셀업로드 폼
	function fnUploadExcelForm() {
		location.href = '/admin/access/"+fnSysMappingCode()+"insertExcelFormAccessMgt.do';
	}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/access/selectPageListAccessMgt.do">
					<input type="hidden" name="ip" />
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
										<h5>IP 접근 제어 :
										<c:choose>
											<c:when test="${Globals.ACCESS_IP_YN == 'Y'}">
												ON</h5>
											</c:when>
											<c:when test="${Globals.ACCESS_IP_YN == 'N'}">
												OFF</h5>
											</c:when>
										</c:choose>
									</div>
								</div>
							</div>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table table-hover text-nowrap table-grid">
									<colgroup>
										<col width="50px" />
										<col width="*" />
										<col width="150px" />
										<col width="150px" />
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">IP</th>
											<th class="text-center">등록자</th>
											<th class="text-center">등록일</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="item" items="${resultList}" varStatus="status">
											<tr style="cursor: pointer; cursor: hand;" onclick="fnSelect('${item.IP}')">
												<td class="text-center">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
												<td>${item.IP}</td>
												<td class="text-center">${item.REGISTER_NM}</td>
												<td class="text-center">${item.REGIST_YMD}</td>
											</tr>
										</c:forEach>
										<c:if test="${fn:length(resultList) == 0}">
											<tr>
												<td class="text-center" colspan="4"><spring:message	code="msg.data.empty" /></td>
											</tr>
										</c:if>
									</tbody>
								</table>
							</div>
						</div>
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
