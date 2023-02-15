<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
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

		// 지점검색 이벤트
		$('[name=sch_text]').on({
			'keyup' : function(e) {
				if (e.which == 13) {
					fnSearch();
				}
			},
			'keydown' : function(e) {
				if (e.which == 13) {
					e.preventDefault();
				}
			},
		});

		//검색 onchange event
		$('[name=sch_sys_code], [name=sch_zone_code]').change(function(e) {
			fnSearch();
		});

		if ("${sysMappingCode}" == "") {
			$("select[name=sch_sys_code]").html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '시스템')}");
		} else {
			$("select[name=sch_sys_code]").html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code, 'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '')}");
		}

	});

	//페이지이동
	function fnGoPage(currentPage) {
		$("#currentPage").val(currentPage);
		$("#aform").attr({
			action : "/admin/banners/"+fnSysMappingCode()+"selectPageListBannersMgt.do",
			method : 'post'
		}).submit();
	}

	//등록
	function fnInsertForm() {
		document.location.href = "/admin/banners/"+fnSysMappingCode()+"insertFormBannersMgt.do";
	}

	//상세조회
	function fnSelect(banner_seq, sch_sys_code, sch_zone_code) {
		$("#banner_seq").val(banner_seq);
		$("#aform").attr({action:"/admin/banners/"+fnSysMappingCode()+"selectBannersMgt.do", method:'post'}).submit();
	}

	//검색
	function fnSearch() {
		$("#currentPage").val("1");
		$("#aform").attr({
			action : "/admin/banners/"+fnSysMappingCode()+"selectPageListBannersMgt.do",
			method : 'post'
		}).submit();
	}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/banners/selectPageListBannersMgt.do">
					<input type="hidden" id="banner_seq" name="banner_seq" />
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
									<select id="sch_sys_code" name="sch_sys_code" class="form-control form-control-sm mr-1 mb-1">
									</select>
									<select id="sch_zone_code" name="sch_zone_code" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_BANNER_ZONE, 'CODE', 'CODE_NM', requestScope.param.sch_zone_code, '배너구역')}
									</select>
									<select id="search_list_st" name="sch_type" class="form-control form-control-sm mr-1 mb-1">
										<option value="banner_nm" <c:if test="${requestScope.param.sch_type == 'banner_nm'}">selected="selected"</c:if>>배너 명</option>
										<option value="attrb_1" <c:if test="${requestScope.param.sch_type == 'attrb_1'}">selected="selected"</c:if>>속성 1</option>
										<option value="attrb_2" <c:if test="${requestScope.param.sch_type == 'attrb_2'}">selected="selected"</c:if>>속성 2</option>
										<option value="attrb_3" <c:if test="${requestScope.param.sch_type == 'attrb_3'}">selected="selected"</c:if>>속성 3</option>
										<option value="attrb_123" <c:if test="${requestScope.param.sch_type == 'attrb_123'}">selected="selected"</c:if>>속성 1+속성 2+속성 3</option>
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
											<col width="80" />
											<col width="20%" />
											<col width="10%" />
											<col width="20%" />
											<col width="*" />
											<col width="10%" />
											<col width="10%" />
										</colgroup>
										<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">시스템</th>
											<th class="text-center">배너구역코드</th>
											<th class="text-center">배너구역</th>
											<th class="text-center">배너 명</th>
											<th class="text-center">전시여부</th>
											<th class="text-center">등록일</th>
											</tr>
										</thead>
										<tbody>
										<c:forEach var="item" items="${resultList}" varStatus="status">
											<tr style="cursor: pointer; cursor: hand;"
												onclick="fnSelect('${item.BANNER_SEQ}', '${item.SYS_CODE}', '${item.ZONE_CODE}')">
												<td class="text-center">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
												<td class="text-center txt_overflow">${item.SYS_NM}</td>
												<td class="text-center">${item.ZONE_CODE}</td>
												<td class="text-center txt_overflow">${item.ZONE_NM}</td>
												<td class="title txt_overflow">${item.BANNER_NM}</td>
												<td class="text-center">${item.DISP_YN_NM}</td>
												<td class="text-center">${item.REGIST_YMD}</td>
											</tr>
											</c:forEach>
											<c:if test="${fn:length(resultList) == 0}">
												<tr>
													<td class="text-center" colspan="10"><spring:message code="msg.data.empty" /></td>
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
