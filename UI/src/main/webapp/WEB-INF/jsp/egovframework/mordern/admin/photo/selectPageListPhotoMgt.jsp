<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
<script type="text/javascript">
	//<![CDATA[

	$(function() {

		$('[name=sch_start_photo_potogrf_de]').datetimepicker({
			locale : 'ko', // 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD',
			useCurrent : false, //Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'left',
				vertical : 'bottom'
			},
		});

		$('[name=sch_end_photo_potogrf_de]').datetimepicker({
			locale : 'ko', // 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD',
			useCurrent : false, //Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'right',
				vertical : 'bottom'
			},
		});

		$('[name=sch_start_photo_potogrf_de]').on("dp.change", function (e) {
		     $('[name=sch_end_photo_potogrf_de]').data("DateTimePicker").minDate(e.date);
		});

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

		if ("${sysMappingCode}" == null || "${sysMappingCode}" == "") {
			$('[name=sch_sys_code]').html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '시스템')}");
		} else {
			$('[name=sch_sys_code]').html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code,'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '')}");
		}
	});

	//페이지이동
	function fnGoPage(currentPage) {
		$("#currentPage").val(currentPage);
		$("#aform").attr({action : "/admin/photo/"+fnSysMappingCode()+"selectPageListPhotoMgt.do", method : 'post'}).submit();
	}

	//사진상세
	function fnDetail(photoSeq) {
		$("[name=photo_seq]").val(photoSeq);
		$("#aform").attr({action : "/admin/photo/"+fnSysMappingCode()+"selectPhotoMgt.do", method : 'post'}).submit();
	}

	//사진검색
	function fnSearch() {
		$("#currentPage").val("1");
		$("#aform").attr({action : "/admin/photo/"+fnSysMappingCode()+"selectPageListPhotoMgt.do", method : 'post'}).submit();
	}

	//사진등록
	function fnInsertForm() {
		document.location.href = "/admin/photo/"+fnSysMappingCode()+"insertFormPhotoMgt.do";
	}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/photo/selectPageListPhotoMgt.do">
					<input type="hidden" name="photo_seq" />
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
									<select name="sch_sys_code" class="form-control form-control-sm mr-1 mb-1" onchange="fnSearch();">
									</select>
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control input-sm" name="sch_start_photo_potogrf_de" placeholder="촬영 시작일" value="${requestScope.param.sch_start_photo_potogrf_de}" />
										<div class="input-group-prepend center">
											<div class="input-group-text">~</div>
										</div>
										<input type="text" class="form-control input-sm" name="sch_end_photo_potogrf_de" placeholder="촬영 종료일" value="${requestScope.param.sch_end_photo_potogrf_de}" />
									</div>
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control form-control-sm" name="sch_text" title="제목을 입력하세요." placeholder="제목을 입력하세요" value="${requestScope.param.sch_text}" />
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
												<col width="230" />
												<col width="*" />
												<col width="150" />
												<col width="150" />
											</colgroup>
											<thead>
												<tr>
													<th class="text-center">No</th>
													<th class="text-center">시스템</th>
													<th class="text-center">제목</th>
													<th class="text-center">촬영일</th>
													<th class="text-center">등록일</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="item" items="${resultList}" varStatus="status">
													<tr style="cursor: pointer; cursor: hand;" onclick="fnDetail('${item.PHOTO_SEQ}')">
															<td class="text-center">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
															<td class="text-center">${item.SYS_NM}</td>
															<td class="text-left">${item.SJ}</td>
															<td class="text-center">${item.PHOTO_POTOGRF_DE}</td>
															<td class="text-center">${item.REGIST_YMD}</td>
														</tr>
												</c:forEach>
												<c:if test="${fn:length(resultList) == 0}">
													<tr>
														<td class="text-center" colspan="5"><spring:message code="msg.data.empty" /></td>
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
									<button type="button" class="btn btn-success" onclick="fnInsertForm(); return false;">등록</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
