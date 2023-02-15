<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
<script type="text/javascript">
	//<![CDATA[
	$(function() {

		$('[name=sch_start_de]').datetimepicker({
			locale : 'ko', // 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD',
			useCurrent : false, //Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'left',
				vertical : 'bottom'
			},
		}).on("dp.change", function() {
			fnSearch();
		});

		$('[name=sch_end_de]').datetimepicker({
			locale : 'ko', // 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD',
			useCurrent : false, //Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'right',
				vertical : 'bottom'
			},
		}).on("dp.change", function() {
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

		// 시스템 변경시
		$('[name=sch_sys_code]').change(function(e) {
			$('[name=sch_type]').val("");
			$('[name=sch_text]').val("");
			$("#currentPage").val("1");
			$("#aform").attr({action : "/admin/stats/"+fnSysMappingCode()+"selectListSatisfactionStatus.do", method : 'post'
			}).submit();
		});

		// 목록 수 셀렉트 박스 변경시 조회
		$("#sch_row_per_page").change(function() {
			fnSearch();
		});

		if ("${sysMappingCode}" == "") {
			$('[name=sch_sys_code]').html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '시스템')}");
		} else {
			$('[name=sch_sys_code]').html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code, 'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '')}");
		}

	});

	// 페이지 이동
	function fnGoPage(currentPage) {
		$("#currentPage").val(currentPage);
		$("#aform").attr({action : "/admin/stats/"+fnSysMappingCode()+"selectListSatisfactionStatus.do", method : 'post'
		}).submit();
	}

	// 검색
	function fnSearch() {
		$("#currentPage").val("1");
		$("#aform").attr({action : "/admin/stats/"+fnSysMappingCode()+"selectListSatisfactionStatus.do", method : 'post'
		}).submit();
	}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" accesskey=""action="/admin/stats/selectListSatisfactionStatus.do">
					<input type="hidden" id="user_no" name="user_no" />
					<div class="card card-info card-outline">
						<div class="card-header">
							<div class="form-row justify-content-between">
								<div class="form-inline">
									<div class="form-group input-group-sm mr-1 mb-1">
										<select id="sch_row_per_page" name="sch_row_per_page" class="form-control input-sm">
											${CacheCommboUtil:getComboStr(UPCODE_ROW_PER_PAGE, 'CODE', 'CODE_NM', requestScope.param.sch_row_per_page, '')}
										</select>
									</div>
								</div>
								<div class="form-inline">
									<select name="sch_sys_code" class="form-control form-control-sm mr-1 mb-1" onchange="fnSearch();">
									</select>
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control input-sm" name="sch_start_de" value="${requestScope.param.sch_start_de}" placeholder="로그 시작일" />
										<div class="input-group-prepend center">
											<div class="input-group-text">~</div>
										</div>
										<input type="text" class="form-control input-sm" name="sch_end_de" value="${requestScope.param.sch_end_de}" placeholder="로그 종료일" onchange="fnSearch();" />
									</div>
								</div>
							</div>
						</div>
						<!-- /.card-header -->
						<div class="card-body">
							<div class="table-responsive">
								<table class="table table-hover text-nowrap table-grid">
									<colgroup>
										<col width="80px" />
										<col width="20%" />
										<col width="20%" />
										<col width="10%" />
										<col width="*" />
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">시스템명</th>
											<th class="text-center">메뉴명</th>
											<th class="text-center">만족도점수</th>
											<th class="text-center">URL</th>
											<th class="text-center">등록일자</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="item" items="${resultList}" varStatus="status">
											<tr>
												<td class="text-center">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
												<td class="text-center">${item.SYS_NM}</td>
												<td class="text-center">${item.MENU_NM}</td>
												<td class="text-center">${item.STSFDG_SCORE}</td>
												<td class="text-center"><a href="${item.URL_QUERY}"
													target="_blank">${item.URL_QUERY}</a></td>
												<td class="text-center">${item.REGIST_DT}</td>
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
							<!-- /.box-body -->
							<div class="form-inline">
								<div class="page_box">${navigationBar}</div>
							</div>
						</div>
					</div>
					<!-- /.card-body -->
				</form>
			</div>
			<!--  /.col-xs-12 -->
		</div>
		<!-- ./row -->
	</div>
	<!-- /.container-fluid -->
</div>
