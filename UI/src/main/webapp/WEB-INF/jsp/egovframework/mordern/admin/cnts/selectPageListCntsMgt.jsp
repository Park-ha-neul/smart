<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

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

	$(function() {
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

		// 시스템 변경시
		$('[name=sch_sys_code]').change(function(e) {
			$('[name=sch_type]').val("");
			$('[name=sch_text]').val("");
			$("#currentPage").val("1");
			$("#aform").attr({
				action : "/admin/cnts/selectPageListCntsMgt.do",
				method : 'post'
			}).submit();
		});

		// 메뉴매핑여부, 전시여부 변경시
		$('[name=sch_menu_yn], [name=sch_disp_yn]').change(function(e) {
			fnSearch();
		});

		if ("${sysMappingCode}" == "") {
			$('[name=sch_sys_code]').html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '시스템')}");
		} else {
			$('[name=sch_sys_code]').html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code, 'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '')}");
		}
	});

	//페이지이동
	function fnGoPage(currentPage) {
		$("#currentPage").val(currentPage);
		$("#aform").attr({
			action : "/admin/cnts/"+fnSysMappingCode()+"selectPageListCntsMgt.do",
			method : 'post'
		}).submit();
	}

	//콘텐츠상세
	function fnDetail(cntntsSeq) {
		$("[name=cntnts_seq]").val(cntntsSeq);
		$("#aform").attr({
			action : "/admin/cnts/"+fnSysMappingCode()+"selectCntsMgt.do",
			method : 'post'
		}).submit();
	}

	//콘텐츠검색
	function fnSearch() {
		$("#currentPage").val("1");
		$("#aform").attr({
			action : "/admin/cnts/"+fnSysMappingCode()+"selectPageListCntsMgt.do",
			method : 'post'
		}).submit();
	}

	//콘텐츠등록
	function fnInsertForm() {
		$("#aform").attr({
			action : "/admin/cnts/"+fnSysMappingCode()+"insertFormCntsMgt.do",
			method : 'post'
		}).submit();
	}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/cnts/selectPageListCntsMgt.do">
					<input type="hidden" name="cntnts_seq" />
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
									<select name="sch_sys_code" class="form-control form-control-sm mr-1 mb-1">
									</select>
									<select name="sch_menu_yn" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_USE_YN, 'CODE', 'CODE_NM', requestScope.param.sch_menu_yn, '메뉴 매핑 여부')}
									</select>
									<select name="sch_disp_yn" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_USE_YN, 'CODE', 'CODE_NM', requestScope.param.sch_disp_yn, '전시 여부')}
									</select>
									<select name="sch_type" class="form-control form-control-sm mr-1 mb-1">
										<option value="sch_sj"
											<c:if test="${param.sch_type eq 'sch_sj'}" >selected="selected"</c:if>>제목</option>
										<option value="sch_cn"
											<c:if test="${param.sch_type eq 'sch_cn'}" >selected="selected"</c:if>>내용</option>
										<option value="sch_kwrd"
											<c:if test="${param.sch_type eq 'sch_kwrd'}" >selected="selected"</c:if>>키워드</option>
										<option value="sch_cntnts_id"
											<c:if test="${param.sch_type eq 'sch_cntnts_id'}" >selected="selected"</c:if>>콘텐츠	아이디</option>
									</select>
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control form-control-sm"	name="sch_text" title="검색어를 입력하세요." placeholder="검색어를 입력하세요." value="${requestScope.param.sch_text}" />
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
										<col width="15%" />
										<col width="10%" />
										<col width="*" />
										<col width="20%" />
										<col width="100px" />
										<col width="120px" />
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">시스템</th>
											<th class="text-center">콘텐츠 아이디</th>
											<th class="text-center">제목</th>
											<th class="text-center">사용중인 메뉴명</th>
											<th class="text-center">전시여부</th>
											<th class="text-center">등록일</th>
										</tr>
									</thead>
									<tbody>
										<c:set value="${pageNavigationVo.getCurrDataNo()}" var="dataNo"></c:set>
										<c:choose>
											<c:when test="${resultList.size() == 0}">
												<tr>
													<td class="text-center" colspan="7"><spring:message code="msg.data.empty" />
													</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="resultSet" items="${resultList}" varStatus="status">
													<tr style="cursor: pointer; cursor: hand;" onclick="fnDetail('${resultSet.CNTNTS_SEQ}')">
														<td class="text-center">
															${dataNo - status.index}
														</td>
														<td class="text-center">
															${resultSet.SYS_NM }
														</td>
														<td class="text-center">
															${resultSet.CNTNTS_ID }
														</td>
														<td class="title">
															${resultSet.SJ}</td>
														<td class="text-center">
															(${resultSet.MENU_USE_CNT}개)${resultSet.MENU_NM}
														</td>
														<td class="text-center">
															${resultSet.DISP_YN_NM}
														</td>
														<td class="text-center">
															${resultSet.REGIST_DT}
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
