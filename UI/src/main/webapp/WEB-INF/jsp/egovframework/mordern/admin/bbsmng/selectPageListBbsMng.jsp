<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
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

		// 목록 수 셀렉트 박스 변경시 조회
		$("#sch_row_per_page").change(function() {
			fnSearch();
		});

		//게시판 유형 셀렉트 박스 변경시 조회
		$('[name=sch_bbs_ty_code]').on({'change' : function(){
				fnSearch();
			}
		});

		//시스템 선택시 게시판 유형 셀렉트박스 초기화
		$('[name=sch_sys_code]').on({'change' : function(){
				$('[name=sch_bbs_ty_code] option:eq(0)').prop("selected", true);
				fnSearch();
			}
		});

		$('[name=sch_use_yn]').on({'change' : function(){
			fnSearch();
		}
	});

		if ("${sysMappingCode}" == "") {
			$('[name=sch_sys_code]').html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '시스템')}");
		} else {
			$('[name=sch_sys_code]').html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code, 'CODE', 'CODE_NM', requestScope.param.sch_sys_code, '')}");
		}
	});

	//페이지이동
	function fnGoPage(currentPage){
		$("#currentPage").val(currentPage);
		$("#aform").attr({action:"/admin/bbsmng/"+fnSysMappingCode()+"selectPageListBbsMng.do", method:'post'}).submit();
	}

	//게시판 관리상세
	function fnDetail(bbsCode){
		$("[name=bbs_code]").val(bbsCode);
		$("#aform").attr({action:"/admin/bbsmng/"+fnSysMappingCode()+"selectBbsMng.do", method:'post'}).submit();
	}

	//게시판 관리검색
	function fnSearch(){
		$("#currentPage").val("1");
		$("#aform").attr({action:"/admin/bbsmng/"+fnSysMappingCode()+"selectPageListBbsMng.do", method:'post'}).submit();
	}

	//게시판 관리등록
	function fnInsertForm(){
		document.location.href = "/admin/bbsmng/"+fnSysMappingCode()+"insertFormBbsMng.do";
	}

//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/bbsmng/selectPageListBbsMng.do">
					<input type="hidden" name="bbs_code" />
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
									<select name="sch_bbs_ty_code" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_BBS_TY, 'CODE', 'CODE_NM', requestScope.param.sch_bbs_ty_code, '게시판 유형')}
									</select>
									<select name="sch_use_yn" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_USE_YN, 'CODE', 'CODE_NM', requestScope.param.sch_use_yn, '사용 여부')}
									</select>
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control form-control-sm" name="sch_text" title="게시판 코드를 입력하세요." placeholder="게시판 명/코드 입력하세요." value="${requestScope.param.sch_text}" />
										<div class="input-group-append">
											<button type="button" class="btn btn-default btn-outline-dark" onclick="fnSearch(); return false;"><i class="fa fa-search"></i></button>
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
										<col width="20%" />
										<col width="18%" />
										<col width="100px" />
										<col width="12%" />
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">시스템</th>
											<th class="text-center">게시판 유형</th>
											<th class="text-center">게시판 코드</th>
											<th class="text-center">게시판 명</th>
											<th class="text-center">사용중인 메뉴</th>
											<th class="text-center">사용여부</th>
											<th class="text-center">등록일</th>
										</tr>
									</thead>
									<tbody>
									<c:forEach var="item" items="${resultList}" varStatus="status">
										<tr style="cursor:pointer;cursor:hand;" onclick="fnDetail('${item.BBS_CODE}')">
											<td class="text-center">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
											<td class="text-center txt_overflow">${item.SYS_CODE_NM}</td>
											<td class="text-center">${item.BBS_TY_CODE_NM}</td>
											<td class="text-center">${item.BBS_CODE}</td>
											<td class="text-center txt_overflow">${item.BBS_NM}</td>
											<td class="text-center txt_overflow">(${item.MENU_CNT}개) ${item.MENU_NM}</td>
											<td class="text-center">${item.USE_YN_NM}</td>
											<td class="text-center">${item.REGIST_DT}</td>
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
