<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>
<%@ taglib prefix="Util" uri="/WEB-INF/tlds/Util.tld"%>
<%@ taglib prefix="SysUtil" uri="/WEB-INF/tlds/SysUtil.tld"%>

<link rel="stylesheet" type="text/css" media="all" href="/common/css/dragAndDrop.css" />
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script src="/common/js/dragAndDrop.js"></script>
<script type="text/javascript">
//<![CDATA[
	//초기화
	$(function(){

		//데이트피커 설정
		$('[name=photo_potogrf_de]').datetimepicker({
			locale : 'ko', 	// 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD',
			useCurrent: false, 	//Important! See issue #1075
			sideBySide : true,
		});


	});

	//사진상세
	function fnDetail(photoSeq){
		$("#aform").attr({action:"/admin/photo/"+fnSysMappingCode()+"selectPhotoMgt.do", method:'post'}).submit();
	}
	// 업로드 파일 목록 생성
	function addFileList(fIndex, fileName, fileSize){
		var html = "";
		html += "<tr class='projects td' id='fileTr_" + fIndex + "'>";
		html += "	<td>";
		html +=			fileName + " / " + fileSize.toFixed(2) + "MB " ;
		html += "	</td>"
		html += "	<td>";
		html +=	"		<input type='text' name='caption' class='form-control' maxlength='100'>";
		html += "	</td>"
		html += "	<td>";
		html +=	"		<input type='text' name='kwrd' class='form-control' maxlength='100'>";
		html += "	</td>"
		html += "	<td class='text-center'>";
		html +=	"		<button class='btn bg-gradient-danger btn-sm' onclick='deleteFile(" + fIndex + "); return false;'>삭제</button>";
		html += "	</td>"
		html += "</tr>"

		$('#fileTableTbody tbody').append(html);
	}
	// 파일 등록
	function uploadFileAjax(){
		// 등록할 파일 리스트
		var uploadFileList = Object.keys(fileList);

		// 파일이 있는지 체크
		/*
		if(uploadFileList.length == 0){
			// 파일등록 경고창
			alert("파일이 없습니다.");
			return;
		}
		*/

		// 용량을 500MB를 넘을 경우 업로드 불가
		if(totalFileSize > maxUploadSize){
			// 파일 사이즈 초과 경고창
			alert("총 용량 초과\n총 업로드 가능 용량 : " + maxUploadSize + " MB");
			return;
		}

		if($("[name=sys_code]").val() == ""){
			alert("시스템을 선택해 주세요.");
			$("[name=sys_code]").focus();
			return;
		}

		if($("[name=sj]").val() == ""){
			alert("제목을 선택해 주세요.");
			$("[name=sj]").focus();
			return;
		}

		if(confirm("저장하시겠습니까?")){
			// 등록할 파일 리스트를 formData로 데이터 입력
			//var form = $('#aform');
			var formData = new FormData($('#aform')[0]);
			for(var i = 0; i < uploadFileList.length; i++){
				formData.append('upload', fileList[uploadFileList[i]]);
			}

			$.ajax({
				url:"/admin/photo/"+fnSysMappingCode()+"updatePhotoImageAjax.do",
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
	//서버에 있는 파일 삭제 - 공통 사용 시 기존 파일 길이로 찌꺼기 로우가 남음
	function fnPhotoFileDel(obj, file_id){

		if(confirm("삭제하시겠습니까?")){

			var param = { 'file_id' : file_id };

			jQuery.ajax( {
				type : 'POST',
				dataType : 'json',
				url : '/common/file/deleteFileInfAjax.do',
				data : param,
				success : function(param) {

					if(param.resultStats.resultCode == 'error'){
						alert(param.resultStats.resultMsg);
						return;
					}else{
						alert(param.resultStats.resultMsg);
						// 첨부파일 삭제
						$(obj).parent().parent().remove();
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
				<form role="form" id="aform" method="post" action="/admin/photo/insertPhotoMgt.do" enctype="multipart/form-data">
					<input type="hidden" name="photo_seq"	value="${requestScope.param.photo_seq}"/>
					<input type="hidden" name="sch_sys_code"	value="${requestScope.param.sch_sys_code}" />
					<input type="hidden" name="sch_start_photo_potogrf_de"	value="${requestScope.param.sch_start_photo_potogrf_de}" />
					<input type="hidden" name="sch_end_photo_potogrf_de"	value="${requestScope.param.sch_end_photo_potogrf_de}" />
					<input type="hidden" name="sch_text"	value="${requestScope.param.sch_text}" />
					<input type="hidden" name="currentPage"		value="${requestScope.param.currentPage}"/>
					<input type="hidden" name="image_doc_id"	value="${resultMap.IMAGE_DOC_ID}"/>
					<!--페이지 당 목록 수 조건-->
					<input type="hidden" name="sch_row_per_page" value="${requestScope.param.sch_row_per_page}"/>
					<!-- 이미지 파일만 업로드 -->
					<input type="hidden" id="fileType" value="img"/>
					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width:15%;">
										<col style="width:85%;">
									</colgroup>
									<tbody>
										<tr class="projects td">
											<th class="required_field">시스템</th>
											<td>
												<input type="hidden" class="form-control" name="sys_code" maxlength="100" value="${resultMap.SYS_CODE}"/>
												${resultMap.SYS_NM }
											</td>
										</tr>
										<tr class="projects td">
											<th class="required_field">제목</th>
											<td>
												<input type="text" class="form-control" name="sj" maxlength="100" value="${resultMap.SJ}"/>
											</td>
										</tr>
										<tr class="projects td">
											<th>촬영 일자</th>
											<td class="datetimepicker-td">
												<input type="text" class="form-control" name="photo_potogrf_de" maxlength="10" value="${resultMap.PHOTO_POTOGRF_DE}"/>
											</td>
										</tr>
										<tr class="projects td">
											<th>사진</th>
											<td>
												<div class="upload-btn-wrapper">
													<input type="file" id="input_file" name="fileList[]" multiple="multiple" style="width: 100%;" accept=".gif, .jpg, .png"/>
													<button class="upload-btn btn btn-outline-secondary">파일선택</button>
												</div>
												<div id="dropZone" class="margin-bottom dropZone11" style="margin-bottom: 20px;">
													<div id="fileDragDesc">
														파일을 드래그 해주세요.
													</div>
												</div>
													<div class="table-responsive">
															<table class="table text-nowrap" id="fileTableTbody">
																<colgroup>
																	<col style="width:*%;">
																	<col style="width:30%;">
																	<col style="width:30%;">
																	<col style="width:100px;">
																</colgroup>
																<thead>
																	<tr class="projects td">
																		<th class="text-center">파일명</th>
																		<th class="text-center">캡션</th>
																		<th class="text-center">검색 키워드</th>
																		<th></th>
																	</tr>
																</thead>
																<tbody>
																<c:forEach var="item" items="${photoImgFileList}" varStatus="status">
																	<tr class="projects td">
																		<td>${item.FILE_NM} / ${SysUtil:byteCalculation(item.FILE_SIZE)}</td>
																		<td>
																			<input type='hidden' name='file_id_update' class='form-control' value="${item.FILE_ID}">
																			<input type='text' name='caption_update' class='form-control' maxlength='100' value="${item.CAPTION}"></td>
																		<td><input type='text' name='kwrd_update' class='form-control' maxlength='100' value="${item.KWRD}"></td>
																		<td class='text-center'>
																			<button class="btn bg-gradient-danger btn-sm" onclick="fnPhotoFileDel(this, '${item.FILE_ID}'); return false;">삭제</button>
																		</td>
																	</tr>
																</c:forEach>
																</tbody>
															</table>
														</div>
											</td>
										</tr>
										<tr class="projects td">
											<th>비고</th>
											<td>
												<textarea class="form-control" rows="10" id="rm" name="rm" maxlength="4000">${Util:getHtml(resultMap.RM)}</textarea>
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
									<button type="button" class="btn bg-gradient-success" onclick="uploadFileAjax(); return false;">저장</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
