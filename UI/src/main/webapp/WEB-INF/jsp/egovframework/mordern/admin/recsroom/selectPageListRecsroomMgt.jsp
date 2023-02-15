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
		//활동을 기본으로 검색
		$('[name=sch_user_se_code], [name=sch_user_sttus_code]').change(
				function(e) {
					fnSearch();
				});

		// 목록 수 셀렉트 박스 변경시 조회
		$("#sch_row_per_page").change(function() {
			fnSearch();
		});
	});

	//콘텐츠검색
	function fnSearch() {
		$("#currentPage").val("1");
		$("#aform").attr({
			action : "/admin/recsroom/"+fnSysMappingCode()+"selectPageListRecsroomMgt.do",
			method : 'post'
		}).submit();
	}

	//콘텐츠 상세
	function fnDetail(recsroomSeq) {
		$("[name=recsroom_seq]").val(recsroomSeq);
		$("#aform").attr({
			action : "/admin/recsroom/"+fnSysMappingCode()+"selectRecsroomMgt.do",
			method : 'post'
		}).submit();


	}

	//게시물 관리등록
	function fnInsertForm(){
		document.location.href = "/admin/recsroom/"+fnSysMappingCode()+"insertFormRecsroomMgt.do";
	}

//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post"
					action="/admin/recsroom/selectPageListRecsroomMgt.do">
					<input type="hidden" name="recsroom_seq" />
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
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control form-control-sm" name="sch_text" title="제목을 입력하세요." placeholder="제목을 입력하세요."
											value="${requestScope.param.sch_text}" />
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
										<col width="80px" />
										<col width="*" />
										<col width="15%" />
										<col width="10%" />
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">제목</th>
											<th class="text-center">작성자</th>
											<th class="text-center">등록일</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="item" items="${resultList}" varStatus="status">
											<tr style="cursor: pointer; cursor: hand;" onclick="fnDetail('${item.RECSROOM_SEQ}')">
												<td class="text-center">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
												<td class="text-center txt_overflow">${item.SJ}</td>
												<td class="text-center">${item.REGISTER_NM}</td>
												<td class="text-center">${item.REGIST_DT}</td>
											</tr>
										</c:forEach>
										<c:if test="${fn:length(resultList) == 0}">
											<tr>
												<td class="text-center" colspan="8"><spring:message
														code="msg.data.empty" /></td>
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
									<button type="button" class="btn bg-gradient-success"
										onclick="fnInsertForm(); return false;">등록</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
