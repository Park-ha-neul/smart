<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
<script type="text/javascript">
	//<![CDATA[
	$(function() {

		$('[name=sch_start_event_de]').datetimepicker({
			locale : 'ko', // 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD',
			useCurrent : false, //Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'left',
				vertical : 'bottom'
			},
		});

		$('[name=sch_end_event_de]').datetimepicker({
			locale : 'ko', // 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD',
			useCurrent : false, //Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'right',
				vertical : 'bottom'
			},
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

		// 목록 수 셀렉트 박스 변경시 조회
		$("#sch_row_per_page").change(function() {
			fnSearch();
		});
	});

	// 페이지 이동
	function fnGoPage(currentPage) {
		$("#currentPage").val(currentPage);
		$("#aform").attr({action : "/admin/stats/selectPageListAdminEventLog.do",method : 'post'
		}).submit();
	}

	// 검색
	function fnSearch() {
		$("#currentPage").val("1");
		$("#aform").attr({action : "/admin/stats/selectPageListAdminEventLog.do",method : 'post'
		}).submit();
	}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/stats/selectPageListAdminEventLog.do">
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
									<div class="input-group input-group-sm mr-1">
										<input type="text" class="form-control input-sm" name="sch_start_event_de" value="${requestScope.param.sch_start_event_de}" placeholder="로그 시작일" />
										<div class="input-group-prepend center">
											<div class="input-group-text">~</div>
										</div>
										<input type="text" class="form-control input-sm" name="sch_end_event_de" value="${requestScope.param.sch_end_event_de}" placeholder="로그 종료일" />
									</div>
									<div class="input-group input-group-sm mr-1">
										<input type="text" class="form-control form-control-sm" name="sch_text" title="사용자명을 입력하세요." placeholder="사용자명을 입력하세요." value="${requestScope.param.sch_text}" />
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
								<table class="table text-nowrap table-grid">
									<colgroup>
										<col width="80px">
										<col width="12%">
										<col width="8%">
										<col width="15%">
										<col width="*">
										<col width="15%">
										<col width="10%">
										<col width="10%">
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">이벤트일시</th>
											<th class="text-center">메뉴유형</th>
											<th class="text-center">메뉴명</th>
											<th class="text-center">요청 url</th>
											<th class="text-center">상세정보</th>
											<th class="text-center">사용자명</th>
											<th class="text-center">IP</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="item" items="${resultList}" varStatus="status">
											<tr>
												<input type="hidden" id="paramVal" value="${item.PARAM_VALUE}">
												<td class="text-center">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
												<td class="text-center">${item.EVENT_YMDHMS}</td>
												<td class="text-center">${item.MENU_TY_NM}</td>
												<td class="text-left">${item.MENU_NM}</td>
												<td class="text-left">${item.URL}</td>
												<td class="text-center">
													<button type="button" class="btn bg-gradient-secondary modal-btn btn-sm" data-param-val="${item.PARAM_VALUE}" data-toggle="modal" data-target="#modal-open">PARAM</button>
												</td>
												<td class="text-center">${item.USER_NM}</td>
												<td class="text-center">${item.IP}</td>
											</tr>
										</c:forEach>
										<c:if test="${fn:length(resultList) == 0}">
											<tr>
												<td class="text-center" colspan="8"><spring:message code="msg.data.empty" /></td>
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

	<!-- param 확인창 -->
	<div class="modal fade" id="modal-open" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-lg" role="document" style="width: 70%">
			<div class="modal-content">
				<div class="modal-header">
					PARAM
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body" style="word-break: break-all">
					<div class="modal-inner"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<script>
		//모달에 param 값 넘기기
		$(document).ready(function() {
			$('.modal-btn').on('click', function() {
				$(".modal-body .modal-inner").html($(this).data('param-val'));
			});
		});
	</script>
</div>
