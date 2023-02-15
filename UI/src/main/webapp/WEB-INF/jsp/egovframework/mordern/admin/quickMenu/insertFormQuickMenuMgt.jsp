<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"			uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		 uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<link rel="stylesheet" type="text/css" media="all" href="/common/css/dragAndDrop.css" />
<script src="/common/js/dragAndDrop.js"></script>
<script type="text/javascript">
//<![CDATA[
//목록보기
function fnList(){
	document.location.href="/admin/quickMenu/"+fnSysMappingCode()+"selectPageListQuickMenuMgt.do";
}

//배너등록
function fnInsert(){
	if($("select[name=sys_code]").val() == ""){
		alert("시스템을 선택해 주세요.");
		$("select[name=sys_code]").focus();
		return;
	}

	if($("select[name=quick_zone_code]").val() == ""){
		alert("퀵메뉴구역을 선택해 주세요.");
		$("select[name=quick_zone_code]").focus();
		return;
	}

	if($("input[name=quickMenu_nm]").val() == ""){
		alert("퀵메뉴 명을 입력해 주세요.");
		$("input[name=quickMenu_nm]").focus();
		return;
	}

	var bool = true;

	$('input[name=sort_ordr_1]').each(function(){
		if($(this).val() == ""){
			alert("정렬순서를 입력해 주세요.");
			$(this).focus();
			bool = false;
			return false;
		}
	});

	if(bool){
		$('input[name=sort_ordr_2]').each(function(){
			if($(this).val() == ""){
				alert("정렬순서를 입력해 주세요.");
				$(this).focus();
				bool = false;
				return false;
			}
		});
	}

	if(bool){
		uploadFileAjax();
	}

}

var webSize = new Array();
var mobileSize = new Array();
<c:forEach var="item" items="${zoneComboStr}">
	webSize.push("${item.ATTRB_1}");
	mobileSize.push("${item.ATTRB_2}");
</c:forEach>

$(function(){
	$('[name=quick_zone_code]').change(function(){
		if($(this).val()!=""){
			var index = $("[name=quick_zone_code] option").index($("[name=quick_zone_code] option:selected"))-1;
			$('#web_size').html('('+webSize[index]+')');
			$('#mobile_size').html('('+mobileSize[index]+')');
		}else{
			$('#web_size').html('');
			$('#mobile_size').html('');
		}
	});

	if ("${sysMappingCode}" == "") {
		$('[name=sys_code]').html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sys_code, '시스템')}");
	} else {
		$('[name=sys_code]').html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code, 'CODE', 'CODE_NM', requestScope.param.sys_code, '')}");
	}
});

// 업로드 파일 목록 생성
function addFileList(fIndex, fileName, fileSize, uploadZoneNo){
	var html = "";
	html += "<tr id='fileTr_" + uploadZoneNo + "_" + fIndex + "'>";
	html += "	<td class='text-center row-no'></td>"
	html += "	<td>";
	html +=	fileName + " / " + fileSize.toFixed(2) + "MB " ;
	html += "	</td>"
	html += "	<td class='text-center'>";
	html += "		<input type='text' class='form-control numeric' name='sort_ordr_"+ uploadZoneNo + "' maxlength='5' value='"+ lastSortOrdr +"'/>";
	html += "	</td>"
	html += "	<td class='text-center'>";
	html += "		<input type='text' class='form-control' name='link_url_"+ uploadZoneNo + "' maxlength='500' />";
	html += "	</td>"
	html += "	<td class='text-center'>";
	html += "		<input type='text' class='form-control' name='attrb_1_"+ uploadZoneNo + "' maxlength='500' />";
	html += "	</td>"
	html += "	<td class='text-center'>";
	html += "		<input type='text' class='form-control' name='attrb_2_"+ uploadZoneNo + "' maxlength='500' />";
	html += "	</td>"
	html += "	<td class='text-center'>";
	html += "		<input type='text' class='form-control' name='attrb_3_"+ uploadZoneNo + "' maxlength='500' />";
	html += "	</td>"
	html += "	<td class='text-center'>";
	html += "		<input type='hidden' name='disp_yn_"+ uploadZoneNo + "' value='Y' />";
	html += "		<button class='btn bg-gradient-danger btn-sm' href='#' onclick='deleteFileMulti(" + fIndex +","+ uploadZoneNo + "); return false;'>삭제</button>";
	html += "	</td>"
	html += "</tr>"

	$('#fileTableTbody_' + uploadZoneNo + ' tbody').append(html);
	fnSetNumeric();
	setRowNo(uploadZoneNo);
	lastSortOrdr = Number(lastSortOrdr) + 10;
}

// 파일 등록
function uploadFileAjax(){
	//등록할 파일 리스트를 formData로 데이터 입력
	var formData = new FormData($('#aform')[0]);
	var bool = true;
	for(var k=0; k<fileListMulti.length; k++){
		// 등록할 파일 리스트
		var uploadFileList = Object.keys(fileListMulti[k]);

		// 파일이 있는지 체크
		if(uploadFileList.length > 0){
			bool = true;
		}else{// 파일등록 경고창
			if(k==0){
				alert("웹 배너 이미지를 첨부해주세요.");
				return;
			}else{
				if(confirm("모바일 배너 이미지가 없습니다. 이대로 등록하시겠습니까?")){
					bool = false;
				}else{
					return;
				}
			}
		}

		// 용량이 maxUploadSize를 넘을 경우 업로드 불가
		if(totalFileSize > maxUploadSize){
			// 파일 사이즈 초과 경고창
			alert("총 용량 초과\n총 업로드 가능 용량 : " + maxUploadSize + " MB");
			return;
		}

		for(var i = 0; i < uploadFileList.length; i++){
			formData.append('upload_'+k, fileListMulti[k][uploadFileList[i]]);
		}
	}

	if(bool){
		if(!confirm("등록하시겠습니까?")){
			return;
		}
	}

	$.ajax({
		url:"/admin/quickMenu/"+fnSysMappingCode()+"insertQuickMenuImageAjax.do",
		data:formData,
		type:'POST',
		enctype:'multipart/form-data',
		processData:false,
		contentType:false,
		dataType:'json',
		cache:false,
		success:function(param){
			if(param.resultStats.resultCode == "error"){
				alert(param.resultStats.resultMsg);
				return;
			}
			fnList();
		}
	});
}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="#">
					<!-- 이미지 파일만 업로드 -->
					<input type="hidden" id="fileType" value="img"/>
					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap inlineBlock">
									<colgroup>
										<col style="width:15%;">
										<col style="width:35%;">
										<col style="width:15%;">
										<col style="width:35%;">
									</colgroup>
									<tbody>
										<tr>
											<th class="required_field">시스템</th>
											<td>
												<select name="sys_code" class="form-control input-sm">
												</select>
											</td>
											<th class="required_field">퀵메뉴구역</th>
											<td>
												<select id="quick_zone_code" name="quick_zone_code" class="form-control form-control-sm mr-1">
													${CacheCommboUtil:getComboStr(UPCODE_QUICK_ZONE, 'CODE', 'CODE_NM', '', '퀵메뉴구역')}
												</select>
											</td>
										</tr>
										<tr>
											<th class="required_field">퀵메뉴 명</th>
											<td colspan="3">
												<input type="text" class="form-control" name="quickMenu_nm" maxlength="50" />
											</td>
										</tr>
										<tr>
											<th class=" required_field">전시여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="disp_yn" id="disp_yn_y" value="Y" checked="checked" />
													<label class="form-check-label" for="disp_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="disp_yn" id="disp_yn_n" value="N" />
													<label class="form-check-label" for="disp_yn_n" style="margin-right:10px;">아니오</label>
												</div>
											</td>
											<th>속성1</th>
											<td>
												<input type="text" class="form-control" name="attrb_1" maxlength="500"/>
											</td>
										</tr>
										<tr>
											<th>속성2</th>
											<td>
												<input type="text" class="form-control" name="attrb_2" maxlength="500" />
											</td>
											<th>속성3</th>
											<td>
												<input type="text" class="form-control" name="attrb_3" maxlength="500"/>
											</td>
										</tr>
										<tr>
											<th><div class="required_field">웹 이미지 첨부</div><div id="web_size"></div></th>
											<td colspan="3">
												<div class="upload-btn-wrapper">
													<input type="file" name="fileList[]" multiple="multiple" style="width: 100%;"id="upload_w" accept=".jpg, .jpeg, .gif, .png" />
													<button class="upload-btn btn btn-outline-secondary">파일선택</button>
												</div>
												<div id="dropZone_w" class="margin-bottom dropZone11" style="margin-bottom: 20px;">
													<div id="fileDragDesc">
														파일을 드래그 해주세요.
													</div>
												</div>
												<div class="table-responsive">
													<table class="table text-nowrap mt-1" id="fileTableTbody_1">
														<colgroup>
															<col style="width:60px;">
															<col style="width:15%">
															<col style="width:130px;">
															<col style="width:*;">
															<col style="width:15%;">
															<col style="width:15%;">
															<col style="width:15%;">
															<col style="width:100px;">
														</colgroup>
														<thead>
															<tr class="projects td">
																<th class="text-center">No</th>
																<th class="text-center">파일명</th>
																<th class="text-center required_field">정렬순서</th>
																<th class="text-center">link url</th>
																<th class="text-center">속성1</th>
																<th class="text-center">속성2</th>
																<th class="text-center">속성3</th>
																<th></th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
												</div>
											</td>
										</tr>
										<tr>
											<th><div>모바일 이미지 첨부</div><div id="mobile_size"></div></th>
											<td colspan="3">
												<div class="upload-btn-wrapper">
													<input type="file" name="fileList[]" multiple="multiple" style="width: 100%;" id="upload_m" accept=".jpg, .jpeg, .gif, .png"/>
													<button class="upload-btn btn btn-outline-secondary">파일선택</button>
												</div>
												<div id="dropZone_m" class="margin-bottom dropZone11" style="margin-bottom: 20px;">
													<div id="fileDragDesc">
														파일을 드래그 해주세요.
													</div>
												</div>
												<div class="table-responsive">
													<table class="table text-nowrap mt-1" id="fileTableTbody_2">
														<colgroup>
															<col style="width:60px;">
															<col style="width:15%">
															<col style="width:130px;">
															<col style="width:*;">
															<col style="width:15%;">
															<col style="width:15%;">
															<col style="width:15%;">
															<col style="width:100px;">
														</colgroup>
														<thead>
															<tr>
																<th class="text-center">No</th>
																<th class="text-center">파일명</th>
																<th class="text-center required_field">정렬순서</th>
																<th class="text-center">link url</th>
																<th class="text-center">속성1</th>
																<th class="text-center">속성2</th>
																<th class="text-center">속성3</th>
																<th></th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnList(); return false;">목록</button>
									<button type="button" class="btn bg-gradient-success" onclick="fnInsert(); return false;">확인</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
