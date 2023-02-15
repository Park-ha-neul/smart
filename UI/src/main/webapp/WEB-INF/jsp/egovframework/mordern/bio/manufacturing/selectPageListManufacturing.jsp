<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix ="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[

	$(function(){

		$("#checkAll").click(function(){
			if($("#checkAll").is(":checked")){
				$("[name=filter]").prop("checked", true);
			}else{
				$("[name=filter]").prop("checked", false);
			}
		})


		$('[name=sch_disp_begin_de]').datetimepicker({
			locale : 'ko', // 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD',
			useCurrent : false, //Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'left',
				vertical : 'bottom'
			},
		});

		$('[name=sch_disp_end_de]').datetimepicker({
			locale : 'ko', // 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD',
			useCurrent : false, //Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'right',
				vertical : 'bottom'
			},
		});

	})

	//페이지이동
	function fnGoPage(currentPage){
		$("#currentPage").val(currentPage);
		$("#aform").attr({action:"/pharmai/bio/manufacturing/selectPageListManufacturing.do", method:'post'}).submit();
	}

	//게시물 관리검색
	function fnSearch(){
 		$("#currentPage").val("1");
 		$("#aform").attr({action:"/pharmai/bio/manufacturing/selectPageListManufacturing.do", method:'post'}).submit();
	}

	function fnGoProject(prjct_id, prjct_type, status){

		var url = "";

		//사용자 session에 선택한 프로젝트 번호/타입/상태로 update
		fnChoicePrjct(prjct_id, prjct_type, status);

		if(status == "01"){
			url = "/pharmai/bio/manufacturing/selectManu_Method.do?prjct_id=" + prjct_id + "&prjct_type=" + prjct_type + "status=" + status;
		}else if(status == "02"){
			url = "/pharmai/bio/manufacturing/selectManu_Process_Map.do";
		}else if(status == "03"){
			url = "/pharmai/bio/manufacturing/selectManu_PHA.do";
		}else if(status == "04"){
			url = "/pharmai/bio/manufacturing/selectManu_FMEA.do";
		}else if(status == "05"){
			url = "/pharmai/bio/manufacturing/selectManu_UNIT_IMG.do";
		}else if(status == "06"){
			url = "/pharmai/bio/manufacturing/selectManu_CPP_Factor.do";
		}else if(status == "07"){
			url = "/pharmai/bio/manufacturing/selectManu_CQAs.do";
		}else if(status == "08"){
			url = "/pharmai/bio/manufacturing/selectManu_Excipent.do";
		}else if(status == "09"){
			url = "/pharmai/bio/manufacturing/selectManu_Result.do";
		}else{
			return;
		}

		location.href=url;
	}



	function fnCopyProject(prjct_id, prjct_type, step_cd) {

		if (step_cd == "03" || step_cd == "04" || step_cd == "05"
				|| step_cd == "06" || step_cd == "07" || step_cd == "08" || step_cd == "09") {

			var confirms = confirm("프로젝트를 복사 하시겠습니까 ?");

			if (confirms) {
				$("[name=c_prjct_id]").val(prjct_id);
				$("[name=c_prjct_type]").val(prjct_type);

				$("#currentPage").val("1");
				$("#cform").attr({
					action : "/pharmai/bio/manufacturing/copyPrjct.do",
					method : 'post'
				}).submit();
			}

		} else {
			alert("단계 STEP_03 이상 진행된 프로젝트만 복사 가능합니다. ");
			return;
		}

	}

	function fnChoicePrjct(prjct_id, prjct_type, status){
		var req = {
			prjct_id : prjct_id,
			prjct_type : prjct_type,
			status : status
		};

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/pharmai/bio/formulation/updateChoicePrjctAjax.do',
			async: false,
			data : req,
			success : function(response) {
				if(response.resultStats.resultCode == "error"){
					alert(response.resultStats.resultMsg);
					return;
				}
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	}

	function deletePrjctFunc(){

		var checkLen = $('[name=filter]:checked').length;

		if(checkLen == 0){
			alert("삭제할 프로젝트를 한개 이상 선택해 주세요");
			return;
		}

		var confirms = confirm("프로젝트를 삭제 하시겠습니까?");

		if(confirms){
			$("#aform").attr({action : "/pharmai/bio/formulation/deleteFormulation.do", method : 'post'}).submit();
		}


	}

//]]>
</script>

<form role="form" id="cform" method="post" action="">
	<input type="hidden" id="c_status" name="c_status" value="02"/>
	<input type="hidden" name="c_prjct_type" value=""/>
	<input type="hidden" name="c_prjct_id" value=""/>
</form>

<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="">
					<input type="hidden" name="menuName" value="manufacturing">
					<div class="card card-info card-outline">
						<div class="card-header">
							<div class="form-row justify-content-between">
								<div class="form-inline">
									<div class="form-group input-group-sm mb-1">
										<h4 class="card-title">Manufacturing 프로젝트 리스트</h4>
									</div>
								</div>
								<div class="form-inline">
									<div class="input-group input-group-sm mb-1">
										<div class="input-group-append">
											<div class="input-group-text"><i class="fa fa-calendar"></i></div>
										</div>
										<input type="text" name="sch_disp_begin_de" class="form-control" value="${requestScope.param.sch_disp_begin_de}">
										<div class="input-group-prepend center mr-2 ml-2">
											<span> ~ </span>
										</div>
									</div>
									<div class="input-group input-group-sm mb-1 mr-2">
										<div class="input-group-append">
											<div class="input-group-text"><i class="fa fa-calendar"></i></div>
										</div>
										<input type="text"  name="sch_disp_end_de" class="form-control" value="${requestScope.param.sch_disp_end_de}">
									</div>
									<div class="input-group input-group-sm mr-1 mb-1">
										<input type="text" class="form-control form-control-sm" name="sch_text" title="검색어를 입력하세요." placeholder="검색: 프로젝트명" value="${requestScope.param.sch_text}" />
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
										<col width="5%">
										<col width="*" />
										<col width="15%" />
										<col width="15%" />
										<col width="15%" />
										<col width="15%" />
									</colgroup>
									<thead>
										<tr>
											<th><input type="checkbox" id="checkAll" ></th>
											<th class="text-center">프로젝트 이름</th>
											<th class="text-center">타입</th>
											<th class="text-center">진행상황</th>
											<th class="text-center">마지막 수정일</th>
											<th class="text-center">Copy Project</th>
										</tr>
									</thead>
									<tbody>

										<c:forEach var="item" items="${resultList}" varStatus="status">
											<tr>
												<td class="text-center"><input type="checkbox" name="filter" value="${item.PRJCT_ID }"></td>
												<td class="text-center"><a href="#" onclick=" fnGoProject('${item.PRJCT_ID}', '${item.PRJCT_TYPE}', '${item.STEP_CD}')">${item.PRJCT_NM}</a></td>
												<td class="text-center">${item.PRJCT_TYPE == 'F' ? 'Formulation' : 'Manufacturing'}</td>
												<td class="text-center">STEP_${item.STEP_CD}</td>
												<td class="text-center"><fmt:formatDate pattern = "yyyy-MM-dd HH:mm:ss" value = "${item.UPDT_DT}" /></td>
												<td class="text-center">
													<a href="#" onclick=" fnCopyProject('${item.PRJCT_ID}', '${item.PRJCT_TYPE}', '${item.STEP_CD}')">
													<i class="far fa-file-alt mr-1"> Copy</i></a>
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
						<div class="card-footer">
							<button type="button" class="btn btn bg-gradient-danger float-right" onclick="deletePrjctFunc(); return false;">삭제</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>