<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<%@ taglib prefix="SysUtil" uri="/WEB-INF/tlds/SysUtil.tld"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<link rel="stylesheet" type="text/css" media="all" href="/common/css/dragAndDrop.css" />
<script src="/common/js/dragAndDrop.js"></script>
<script type="text/javascript">
//<![CDATA[
// 이미지가 없어서 error 날시
 function fnImgError(img){
	img.src="<%=imgRoot%>common/no-image.png";
}

//목록
function fnDetail(){
	$("#aform").attr({action:"/admin/quickMenu/"+fnSysMappingCode()+"selectQuickMenuMgt.do", method:'post'}).submit();
}

//수정
function fnUpdate(){
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
		$('input[name=sort_ordr_update]').each(function(){
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

  function showImgSize(){
	  var index = $("[name=quick_zone_code] option").index($("[name=quick_zone_code] option:selected"))-1;
	  if($("[name=quick_zone_code]").val() == '100'){
		  $('#web_size').html('('+webSize[index]+')<br/><br/> 모바일은 배경 및 폰트 색상 변경가능합니다. <br/> 속성2 : 배경색상 / 속성3 : 폰트색상 <br/> ex)속성2 -> #d8efec 속성3 -> #ffffff <br/> 값을 줄 경우 모바일에서 해당 색상이 적용됩니다.');
	  }else{
			$('#web_size').html('('+webSize[index]+')');
	  }
		$('#mobile_size').html('('+mobileSize[index]+')');
  }

  $(function(){
  	if ("${sysMappingCode}" == "") {
  		$("select[name=sys_code]").html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', resultMap.SYS_CODE, '시스템')}");
  	} else {
  		$("select[name=sys_code]").html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code, 'CODE', 'CODE_NM', resultMap.SYS_CODE, '')}");
  	}

   showImgSize();
   $('[name=quick_zone_code]').change(function(){
  		showImgSize();
   });
});

// 업로드 파일 목록 생성
function addFileList(fIndex, fileName, fileSize, uploadZoneNo){
	var html = "";
	html += "<tr id='fileTr_" + uploadZoneNo + "_" + fIndex + "'>";
	html += "	<td class='text-center row-no'></td>"
	html += "	<td>";
	html += "		<span style='color:red'>[new] </span>"+fileName + " / " + fileSize.toFixed(2) + "MB " ;
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
	html += "		<button class='btn bg-gradient-danger btn-sm'  href='#' onclick='deleteFileMulti(" + fIndex +","+ uploadZoneNo + "); return false;'>삭제</button>";
	html += "	</td>"
	html += "</tr>"

	$('#fileTableTbody_' + uploadZoneNo + ' tbody').append(html);
	fnSetNumeric();
	setRowNo(uploadZoneNo);
	lastSortOrdr = Number(lastSortOrdr) + 10;
}

// 파일 등록
function uploadFileAjax(){
	// 등록할 파일 리스트를 formData로 데이터 입력
	var formData = new FormData($('#aform')[0]);

	for(var k=0; k<fileListMulti.length; k++){
		// 등록할 파일 리스트
		var uploadFileList = Object.keys(fileListMulti[k]);

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

	if(confirm("수정하시겠습니까?")){

		$.ajax({
			url:"/admin/quickMenu/"+fnSysMappingCode()+"updateQuickMenuImageAjax.do",
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

				var returnParam = encodeURI(param.resultParam.returnParam);
				var returnUrl = param.resultParam.returnUrl;

				location.replace(returnUrl + returnParam);
			}
		});
	}
}

//서버에 있는 파일 삭제
function fnQuickFileDel(obj, file_id, banner_id){

	if(confirm("삭제하시겠습니까?")){

		var param = { 'file_id' : file_id };

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : "/admin/quickMenu/"+fnSysMappingCode()+"deleteQuickMenuFileAjax.do",
			data : param,
			success : function(param) {
				if(param.resultStats.resultCode == 'error'){
					alert(param.resultStats.resultMsg);
					return;
				}else{
					alert(param.resultStats.resultMsg);
					// 첨부파일 삭제
					$(obj).parent().parent().remove();
					$(banner_id).remove();
				}
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	}
}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/quickMenu/updateQuickMenuMgt.do" enctype="multipart/form-data">
					<input type="hidden" name="quickMenu_seq" value="${requestScope.param.quickMenu_seq}"/>
					<input type="hidden" name="sch_sys_code" value="${requestScope.param.sch_sys_code}"/>
					<input type="hidden" name="sch_quick_zone_code" value="${requestScope.param.sch_quick_zone_code}"/>
					<input type="hidden" name="sch_type" value="${requestScope.param.sch_type}"/>
					<input type="hidden" name="sch_text" value="${requestScope.param.sch_text}"/>
					<input type="hidden" name="currentPage"	value="${requestScope.param.currentPage}"/>
					<input type="hidden" name="web_image_doc_id" value="${resultMap.WEB_IMAGE_DOC_ID}"/>
					<input type="hidden" name="mobile_image_doc_id" value="${resultMap.MOBILE_IMAGE_DOC_ID}"/>
					<!--페이지 당 목록 수 조건-->
					<input type="hidden" name="sch_row_per_page" value="${param.sch_row_per_page}"/>
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
												<select name="quick_zone_code" class="form-control input-sm">
													${CacheCommboUtil:getComboStr(UPCODE_QUICK_ZONE, 'CODE', 'CODE_NM', resultMap.ZONE_CODE, '퀵메뉴구역')}
												</select>
											</td>
										</tr>
										<tr>
											<th class="required_field">퀵메뉴 명</th>
											<td colspan="3">
												<input type="text" class="form-control" name="quickMenu_nm" maxlength="500" value="${resultMap.QUICK_MENU_NM}"/>
											</td>
										</tr>
										<tr>
											<th class="required_field">전시여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="disp_yn" id="disp_yn_y" value="Y" <c:if test="${resultMap.DISP_YN eq 'Y'}">checked="checked"</c:if> />
													<label class="form-check-label" for="disp_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="disp_yn" id="disp_yn_n" value="N" <c:if test="${resultMap.DISP_YN eq 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="disp_yn_n" style="margin-right:10px;">아니오</label>
												</div>
											</td>
											<th>속성1</th>
											<td>
												<input type="text" class="form-control" name="attrb_1" maxlength="500" value="${resultMap.ATTRB_1}"/>
											</td>
										</tr>
										<tr>
											<th>속성2</th>
											<td>
												<input type="text" class="form-control" name="attrb_2" maxlength="500" value="${resultMap.ATTRB_2}" />
											</td>
											<th>속성3</th>
											<td>
												<input type="text" class="form-control" name="attrb_3" maxlength="500" value="${resultMap.ATTRB_3}" />
											</td>
										</tr>
										<tr>
											<th><div class="required_field">웹 이미지 첨부</div><div id="web_size"></div></th>
											<td colspan="3">
												<div class="upload-btn-wrapper">
													<input type="file" multiple="multiple" style="width: 100%;"id="upload_w" accept=".jpg, .jpeg, .gif, .png" />
													<button class="upload-btn btn btn-outline-secondary">파일선택</button>
												</div>
												<div id="dropZone_w" class="margin-bottom dropZone11" style="margin-bottom: 20px;">
													<div id="fileDragDesc">
														파일을 드래그 해주세요.
													</div>
												</div>
												<div class="table-responsive">
													<table class="table text-nowrap" id="fileTableTbody_1">
														<colgroup>
															<col style="width: 20px;">
	  														<col style="width: 15%">
	  														<col style="width: 15%;">
	  														<col style="width: 10%">
	  														<col style="width: 10%;">
	  														<col style="width: 10%;">
	  														<col style="width: 10%;">
	  														<col style="width: 10%;">
	  														<col style="width: 10%;">
														</colgroup>
														<thead>
															<tr>
																<th class="text-center">No</th>
																<th class="text-center">이미지</th>
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
																<c:forEach var="item" items="${webImageList}" varStatus="status">
																	<tr class="projects td">
																		<td class="text-center row-no">${status.index + 1}</td>
																		<td class="text-center">
																			<figure id="quickMenu_w_${status.index}" style="display:inline-block; margin:5px; max-width:100px; vertical-align:top;">
																				<img src="${item.fileRltvPath}${item.fileId}.${item.fileExtNm}" alt="${item.fileNm}" style="max-width:100px" onError="fnImgError(this)" />
																			</figure>
																			</td>
																			<td>
																			${item.fileNm} / ${SysUtil:byteCalculation(item.fileSize)}
																			<input type='hidden' name='file_id_update' class='form-control' value="${item.fileId}">
																		</td>
																		<td><input type='text' name='sort_ordr_update' class='form-control numeric' maxlength='5' value="${item.srtOrd}"></td>
																		<td><input type='text'  name='link_url_update' class='form-control' <c:if test="${item.linkUrl != null}">value="${item.linkUrl}"</c:if> maxlength='500'/></td>
																		<td><input type='text'  name='attrb_1_update' class='form-control' <c:if test="${item.attrb1 != null}">value="${item.attrb1}"</c:if> maxlength='500' /></td>
																		<td><input type='text'  name='attrb_2_update' class='form-control' <c:if test="${item.attrb2 != null}">value="${item.attrb2}"</c:if> maxlength='500' /></td>
																		<td><input type='text'  name='attrb_3_update' class='form-control' <c:if test="${item.attrb3 != null}">value="${item.attrb3}"</c:if> maxlength='500' /></td>
																		<td class="text-center"><button type="button" class="btn bg-gradient-danger btn-sm" onclick="fnQuickFileDel(this, '${item.fileId}', '#quickMenu_w_${status.index}'); return false;">삭제</button></td>
																	</tr>
																</c:forEach>
														</tbody>
													</table>
												</div>
											</td>
										</tr>
										<tr>
											<th><div>모바일 이미지 첨부</div><div id="mobile_size"></div></th>
											<td colspan="3">
												<div class="upload-btn-wrapper">
													<input type="file" multiple="multiple" style="width: 100%;" id="upload_m" accept=".jpg, .jpeg, .gif, .png"/>
													<button class="upload-btn btn btn-outline-secondary">파일선택</button>
												</div>
												<div id="dropZone_m" class="margin-bottom dropZone11" style="margin-bottom: 20px;">
													<div id="fileDragDesc">
														파일을 드래그 해주세요.
													</div>
												</div>
												<div class="table-responsive">
													<table class="table text-nowrap" id="fileTableTbody_2">
														<colgroup>
                              							<col style="width: 20px;">
  														<col style="width: 15%">
  														<col style="width: 15%;">
  														<col style="width: 10%">
  														<col style="width: 10%;">
  														<col style="width: 10%;">
  														<col style="width: 10%;">
  														<col style="width: 10%;">
  														<col style="width: 10%;">
														</colgroup>
														<thead>
															<tr>
																<th class="text-center">No</th>
																<th class="text-center">이미지</th>
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
																<c:forEach var="item" items="${mobileImageList}" varStatus="status">
																	<tr class="projects td">
																		<td class="text-center row-no">${status.index + 1}</td>
																		<td class="text-center">
																			<figure id="quickMenu_m_${status.index}" style="display:inline-block; margin:5px; max-width:100px; vertical-align:top;">
																				<img src="${item.fileRltvPath}${item.fileId}.${item.fileExtNm}" alt="${item.fileNm}" style="max-width:100px" onError="fnImgError(this)" />
																		</figure>
																		</td>
																		<td>
																			${item.fileNm} / ${SysUtil:byteCalculation(item.fileSize)}
																			<input type='hidden' name='file_id_update' class='form-control' value="${item.fileId}">
																		</td>
																		<td><input type='text' name='sort_ordr_update' class='form-control numeric' maxlength='5' value="${item.srtOrd}"></td>
																		<td><input type='text'  name='link_url_update' class='form-control' <c:if test="${item.linkUrl != null}">value="${item.linkUrl}"</c:if> maxlength='500'/></td>
																		<td><input type='text'  name='attrb_1_update' class='form-control' <c:if test="${item.attrb1 != null}">value="${item.attrb1}"</c:if> maxlength='500' /></td>
																		<td><input type='text'  name='attrb_2_update' class='form-control' <c:if test="${item.attrb2 != null}">value="${item.attrb2}"</c:if> maxlength='500' /></td>
																		<td><input type='text'  name='attrb_3_update' class='form-control' <c:if test="${item.attrb3 != null}">value="${item.attrb3}"</c:if> maxlength='500' /></td>
																		<td class="text-center"><button type="button" class="btn bg-gradient-danger btn-sm" onclick="fnQuickFileDel(this, '${item.fileId}', '#quickMenu_m_${status.index}'); return false;">삭제</button></td>
																	</tr>
																</c:forEach>
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
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnDetail(); return false;">취소</button>
									<button type="button" class="btn bg-gradient-success" onclick="fnUpdate(); return false;">확인</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
