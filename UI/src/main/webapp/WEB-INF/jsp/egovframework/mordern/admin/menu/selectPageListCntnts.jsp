<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	$(function(){
		// 지점검색 이벤트
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

	//페이지이동
	function fnGoPage(currentPage){
		$("#currentPage").val(currentPage);
		$("#aform").attr({action:"/admin/menu/selectPageListCntnts.do", method:'post'}).submit();
	}

	//콘텐츠상세
	function fnDetail(cntntsSeq){
		$("[name=cntnts_seq]").val(cntntsSeq);
		$("#aform").attr({action:"/admin/menu/selectCntnts.do", method:'post'}).submit();
	}

	//콘텐츠검색
	function fnSearch(){
		$("#currentPage").val("1");
		$("#aform").attr({action:"/admin/menu/selectPageListCntnts.do", method:'post'}).submit();
	}

	function fnSelect(sys_nm, sj, cntnts_id, subpath){
		$(opener.document).find('#${requestScope.param.tbId} th.name').text('컨텐츠 ID');
		$(opener.document).find('#${requestScope.param.tbId} td.code').text(cntnts_id);
		$(opener.document).find('#${requestScope.param.tbId} td.cntnts_nm').html("["+sys_nm+"] "+sj);
		$(opener.document).find('#${requestScope.param.tbId} [name=${requestScope.param.prefix}cntnts_id]').val(cntnts_id);
		if(subpath != ''){
			$(opener.document).find('#${param.tbId} [name=${param.prefix}url]').val("/"+subpath + '${requestScope.param.selectCntntsUrl}' + cntnts_id);
		} else {
			$(opener.document).find('#${param.tbId} [name=${param.prefix}url]').val('${requestScope.param.selectCntntsUrl}' + cntnts_id);
		}

		window.close();
	}
//]]>
</script>
<div class="content-wrapper no-menu">
	<!-- Content Header (Page header) -->
	<div class="content-header">
		<div class="container-fluid">
			<div class="row mb-2">
				<div class="col-sm-6">
					<h1 class="m-0 text-dark">콘텐츠 목록</h1>
				</div><!-- /.col -->
			</div><!-- /.row -->
		</div><!-- /.container-fluid -->
	</div>
	<div class="content">
		<div class="container-fluid">
			<div class="row">
				<div class="col-lg-12">
					<form role="form" id="aform" method="post">
						<input type="hidden" name="cntnts_seq" />
						<input type="hidden" name="prefix" value='${param.prefix}'/>
						<input type="hidden" name="tbId" value='${param.tbId}'/>

						<div class="card card-info card-outline">
							<div class="card-header">
								<div class="form-row justify-content-end">
									<div class="form-inline">
										<select name="sch_sys_code" class="form-control form-control-sm mr-1 mb-1" onchange="fnSearch();">
											${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '시스템')}
										</select>
										<select name="sch_type" class="form-control form-control-sm mr-1 mb-1">
											<option value="sch_sj" <c:if test="${requestScope.param.sch_sj == ''}">selected="selected"</c:if>>제목</option>
											<option value="sch_cn" <c:if test="${requestScope.param.sch_cn == ''}">selected="selected"</c:if>>내용</option>
											<option value="sch_kwrd" <c:if test="${requestScope.param.sch_kwrd == ''}">selected="selected"</c:if>>키워드</option>
											<option value="sch_cntnts_id" <c:if test="${requestScope.param.sch_cntnts_id == ''}">selected="selected"</c:if>>콘텐츠 아이디</option>
										</select>
										<div class="input-group input-group-sm mb-1">
											<input type="text" class="form-control form-control-sm" name="sch_text" title="검색어를 입력하세요." placeholder="검색어를 입력하세요." value="${requestScope.param.sch_text}" />
											<div class="input-group-append">
												<button type="button" class="btn btn-default btn-outline-dark" onclick="fnSearch(); return false;"><i class="fa fa-search"></i></button>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="card-body">
								<div class="table-responsive">
									<table class="table text-nowrap text-nowrap table-grid">
										<colgroup>
											<col width="80px" />
											<col width="20%" />
											<col width="*" />
											<col width="15%" />
											<col width="15%" />
											<col width="100px" />
										</colgroup>
										<thead>
											<tr>
												<th class="text-center">No</th>
												<th class="text-center">시스템</th>
												<th class="text-center">제목</th>
												<th class="text-center">전시여부</th>
												<th class="text-center">컨텐츠ID</th>
												<th class="text-center">선택</th>
											</tr>
										</thead>
										<tbody>
											<c:set value="${pageNavigationVo.getCurrDataNo()}" var="dataNo"></c:set>
											<c:choose>
												<c:when test="${resultList.size() == 0}">
													<td class="text-center" colspan="6" ><spring:message code="msg.data.empty" /></td>
												</c:when>
												<c:otherwise>
													<c:forEach var="resultSet" items="${resultList}" varStatus="status">
														<tr>
															<td class="text-center txt_overflow">${dataNo - status.index} </td>
															<td class="text-center txt_overflow">${resultSet.SYS_NM } </td>
															<td class="title txt_overflow"><a href="#" onclick="fnDetail('${resultSet.CNTNTS_SEQ}')">${resultSet.SJ}</a></td>
															<td class="text-center txt_overflow">${resultSet.DISP_YN_NM}</td>
															<td class="text-center txt_overflow">${resultSet.CNTNTS_ID}</td>
															<td class="text-center">
																<button type="button" class="btn bg-gradient-success btn-sm" onclick="fnSelect('${resultSet.SYS_NM }','${resultSet.SJ}','${resultSet.CNTNTS_ID}','${resultSet.ATTRB_1}')">선택</button>
															</td>
														</tr>
													</c:forEach>
												</c:otherwise>
											</c:choose>
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
</div>
