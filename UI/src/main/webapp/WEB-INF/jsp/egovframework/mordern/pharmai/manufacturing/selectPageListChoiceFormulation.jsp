<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	$(function(){
		// 검색 이벤트
		$('[name=sch_text]').on({
			'keyup' : function(e){
				if(e.which == 13){
					fnSearch();
				}
			},
			'keydown' : function(e){
				if(e.which == 13){
					e.preventDefault();
				}
			},
		});


	});

	//게시물 관리검색
	function fnSearch(){
 		$("#currentPage").val("1");
 		$("#aform").attr({action:"/pharmai/chemical/manufacturing/selectPageListChoiceFormulation.do", method:'post'}).submit();
	}
	
	//페이지이동
	function fnGoPage(currentPage){
		$("#currentPage").val(currentPage);
		$("#aform").attr({action:"/pharmai/chemical/manufacturing/selectPageListChoiceFormulation.do", method:'post'}).submit();
	}

	function fnLoadProject(formulation_pjt_id){

		var formulation_pjt_id = formulation_pjt_id;
		var prjct_id = $("[name=prjct_id]").val();
		var step_new = $("[name=step_new]").val();

		var req = {
				formulation_pjt_id : formulation_pjt_id,
				prjct_id : prjct_id,
				step_new : step_new
				};

		jQuery.ajax({
			type : 'POST',
			dataType : 'json',
			url : '/pharmai/manufacturing/projectCreateAjax.do',
			async: false,
			data : req,
			success : function(response) {
				var prjct_id = response.prjct_id;
				document.aform.prjct_id.value = prjct_id;
				document.aform.formulation_pjt_id.value = formulation_pjt_id;
				$("#aform").attr({action:"/pharmai/chemical/manufacturing/selectManu_Method.do", method:'post'}).submit();
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});


	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" name="aform" method="post" action="">
					<input type="hidden" name="step_new" value="N"/>
					<input type="hidden" name="prjct_id" value=""/>
					<input type="hidden" name="method" value=""/>
					<input type="hidden" name="formulation_pjt_id" value="" />

					<div class="card card-info card-outline">
						<div class="card-header">
							<div class="form-row justify-content-between">
								<div class="form-inline">
									<div class="form-group input-group-sm mb-1">
										<h4 class="card-title">Formulation 선택하기</h4>
									</div>
								</div>
								<div class="form-inline">
									<select name="sch_type" class="form-control form-control-sm mr-1 mb-1">
										<option value="sj" <c:if test="${requestScope.param.sch_type == 'sj'}">selected="selected"</c:if> >프로젝트 이름</option>
										<option value="type1" <c:if test="${requestScope.param.sch_type == 'type1'}">selected="selected"</c:if> >Type1</option>
										<option value="type2" <c:if test="${requestScope.param.sch_type == 'type2'}">selected="selected"</c:if> >Type2</option>
									</select>
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control form-control-sm" name="sch_text" title="검색어를 입력하세요." placeholder="검색어를 입력하세요." value="${requestScope.param.sch_text}" />
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
										<col width="*" />
										<col width="15%" />
										<col width="15%" />
										<col width="15%" />
										<col width="15%" />
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">Formulation 프로젝트 이름</th>
											<th class="text-center">Type1</th>
											<th class="text-center">Type2</th>
											<th class="text-center">마지막 수정일</th>
											<th class="text-center">불러오기</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="item" items="${resultList}" varStatus="status">
											<tr>
												<td class="text-center" name="prjct_nm">
													<a href="#" onclick="fnLoadProject('${item.PRJCT_ID}')">
														${item.PRJCT_NM}
													</a>
												</td>
												<td class="text-center" name="routes_type">${item.ROUTES_TYPE}</td>
												<td class="text-center" name="dosage_form_type">${item.DOSAGE_FORM_TYPE}</td>
												<td class="text-center"><fmt:formatDate pattern = "yyyy-MM-dd HH:mm:ss" value = "${item.UPDT_DT}" /></td>
												<td class="text-center">
													<a href="#" onclick="fnLoadProject('${item.PRJCT_ID}')">
													<i class="fas fa-folder"> Load</i></a>
												</td>
											</tr>
										</c:forEach>
										<c:if test="${fn:length(resultList) == 0}">
											<tr>
												<td class="text-center" colspan="8" ><spring:message code="msg.data.empty" /></td>
											</tr>
										</c:if>
									</tbody>
								</table>
							</div>
						</div>
						<div class="page_box">${navigationBar}</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>