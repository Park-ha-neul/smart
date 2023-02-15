<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
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

		// 목록 수 셀렉트 박스 변경시 조회
		$("#sch_row_per_page").change(function() {
			fnSearch();
    });

		//게시판 셀렉트 박스 변경시 조회
		$('[name=sch_imprtnc_yn]').on({'change' : function(){
				fnSearch();
			}
		});

		if(${requestScope.param.sch_sys_code == ''}){
			//sch_sys_code가 없으면 게시판 선택 불가
			$('select[name=sch_bbs_code]').attr('disabled',true);
		}else{
			// 게시판 명(sch_bbs_code)가 있을 경우
			searchBbsCode("${requestScope.param.sch_sys_code}");
		}

		//기관 셀렉트 박스 변경시 게시판 코드 조회
		$('[name=sch_sys_code]').on({'change' : function(){
				searchBbsCode($(this).val());
				fnSearch();
			}
		});


		// 메뉴매핑여부, 전시여부 변경시
		$('[name=sch_menu_yn], [name=sch_disp_yn]').change(function(e) {
			fnSearch();
		});

		//게시판 셀렉트 박스 변경시 조회
		$('[name=sch_bbs_code]').on({'change' : function(){
				fnSearch();
			}
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

	//페이지이동
	function fnGoPage(currentPage){
		$("#currentPage").val(currentPage);
		$("#aform").attr({action:"/admin/bbs/"+fnSysMappingCode()+"selectPageListBbsMgt.do", method:'post'}).submit();
	}

	//게시물 관리상세
	function fnDetail(bbsSeq){
		$("[name=bbs_seq]").val(bbsSeq);
		$("#aform").attr({action:"/admin/bbs/"+fnSysMappingCode()+"selectBbsMgt.do", method:'post'}).submit();
	}

	//게시물 관리검색
	function fnSearch(){
		$("#currentPage").val("1");
		$("#aform").attr({action:"/admin/bbs/"+fnSysMappingCode()+"selectPageListBbsMgt.do", method:'post'}).submit();
	}

	//게시물 관리등록
	function fnInsertForm(){
		document.location.href = "/admin/bbs/"+fnSysMappingCode()+"insertFormBbsMgt.do";
	}

	var bbs_code = "${requestScope.param.sch_bbs_code}";

	//게시판 코드 조회
	function searchBbsCode(sysCode){
		var req = {
				sys_code : sysCode
		};

		var selected = "";

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/admin/bbs/'+fnSysMappingCode()+'selectListBbsCodeAjax.do',
			data : req,
			async: false,
			success : function(param) {
				if(param.resultStats.resultCode == "error"){
					alert(param.resultStats.resultMsg);
					return;
				}

				if(param.bbsCodeList.length == 0){
					$("select[name=sch_bbs_code]").empty().append('<option value="">게시판</option>') ;
					$('select[name=sch_bbs_code]').attr('disabled',true);
					return;
				}else{
					$("select[name=sch_bbs_code]").empty().append('<option value="">게시판</option>') ;
					$('select[name=sch_bbs_code]').attr('disabled',false);
					var bbsMngInfo;
					var selected;
					for(var i = 0; i < param.bbsCodeList.length; i++){
							bbsMngInfo = param.bbsCodeList[i];
							if(bbs_code == bbsMngInfo.BBS_CODE){
								selected = "selected";
							}else{
								selected = "";
							}
							$('select[name=sch_bbs_code]').append("<option value='" + bbsMngInfo.BBS_CODE + "'" + selected + " data-tab_se_group_id='" + bbsMngInfo.TAB_SE_GROUP_ID + "'>" + bbsMngInfo.BBS_NM + "</option>");
					}
				}

				//검색 조건 초기화
				bbs_code = "";
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
				<form role="form" id="aform" method="post" action="/admin/bbs/selectPageListBbsMgt.do">
					<input type="hidden" name="bbs_seq" />
					<div class="card card-info card-outline">
						<div class="card-header">
							<div class="form-row justify-content-between">
								<div class="form-inline">
									<div class="form-group input-group-sm mb-1">
										<select id="sch_row_per_page" name="sch_row_per_page" class="form-control input-sm ">
											${CacheCommboUtil:getComboStr(UPCODE_ROW_PER_PAGE, 'CODE', 'CODE_NM', requestScope.param.sch_row_per_page, '')}
										</select>
									</div>
								</div>
								<div class="form-inline">
								<c:set var="UPCODE_USE_YN" value="${Const.UPCODE_USE_YN }"/>
									<select name="sch_imprtnc_yn" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_USE_YN, 'CODE', 'CODE_NM', requestScope.param.sch_imprtnc_yn, '중요여부')}
									</select>
									<select name="sch_disp_yn" class="form-control form-control-sm mr-1 mb-1">
										${CacheCommboUtil:getComboStr(UPCODE_USE_YN, 'CODE', 'CODE_NM', requestScope.param.sch_disp_yn, '전시 여부')}
									</select>
									<select name="sch_sys_code" class="form-control form-control-sm mr-1 mb-1">
									</select>
									<select name="sch_bbs_code" class="form-control form-control-sm mr-1 mb-1">
										<option value="">게시판</option>
									</select>
									<select name="sch_type" class="form-control form-control-sm mr-1 mb-1">
										<option value="sj" <c:if test="${requestScope.param.sch_type == 'sj'}">selected="selected"</c:if> >제목</option>
										<option value="cn" <c:if test="${requestScope.param.sch_type == 'cn'}">selected="selected"</c:if> >내용</option>
										<option value="kwrd" <c:if test="${requestScope.param.sch_type == 'kwrd'}">selected="selected"</c:if> >키워드</option>
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
										<col width="80px" />
										<col width="16%" />
										<col width="20%" />
										<col width="*" />
										<col width="80px" />
										<col width="80px" />
										<col width="12%" />
										<col width="10%" />
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">No</th>
											<th class="text-center">시스템</th>
											<th class="text-center">게시판 명</th>
											<th class="text-center">제목</th>
											<th class="text-center">중요순서</th>
											<th class="text-center">전시여부</th>
											<th class="text-center">등록자</th>
											<th class="text-center">등록일</th>
										</tr>
									</thead>
									<tbody>
									<c:forEach var="item" items="${resultList}" varStatus="status">
										<tr style="cursor:pointer;cursor:hand;" onclick="fnDetail('${item.BBS_SEQ}'); return false;">
											<td class="text-center">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
											<td class="text-center txt_overflow">${item.SYS_CODE_NM}</td>
											<td class="text-left">${item.BBS_NM}</td>
											<td class="title txt_overflow">${item.SJ}[${item.ANSWER_CNT}]</td>
											<td class="text-center">${item.IMPRTNC_ORDR}</td>
											<td class="text-center">${item.DISP_YN_NM}</td>
											<td class="text-center">${item.USER_NM}</td>
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
