<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript" src="/common/ckeditor/ckeditor.js" charset="UTF-8" ></script>
<script type="text/javascript" src="/common/ckeditor/adapters/jquery.js" charset="UTF-8" ></script>
<link rel="stylesheet" type="text/css" media="all" href="/common/css/dragAndDrop.css" />
<script src="/common/js/dragAndDrop.js"></script>
<script type="text/javascript">
$(function(){
	$('.v_img').on({
		// 이미지가 없어서 error 날시
		'error' : function(){
			$(this).attr('src', '<%=imgRoot %>common/no-image.png');
		}
	});

	// 답변 에디터 사용 가부가 Y일 경우 답변 에디터 생성
	if(<c:out value="${resultMap.ANSWER_EDITR_YN == 'Y'}" />){
		CKEDITOR.replace('cn',{
	  		filebrowserUploadUrl: '${pageContext.request.contextPath}/photoServerUpload.do'
	  	});
	}

	fnComboStrFile('.fileBoxWrap', ${fn:length(ansFileList)}, 5);

	//답변 글 파일 확장자 체크
	$(document).on('change', '[name=upload]', function(){
		var msg = f_CheckExceptFileExt('upload');
		if(msg != ""){
			alert('파일에 ' + msg);
			var agent = navigator.userAgent.toLowerCase();
			if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
				$(this).replaceWith( $(this).clone(true) );
			}else{
				$(this).val("");
			}
			return;
		}
	});

});

//상세화면 이동
function fnDetail(){
	$("#answerForm").attr({action:"/admin/bbs/"+fnSysMappingCode()+"selectBbsMgt.do", method:'post'}).submit();
}

//수정
function fnUpdate(){

	if("${'Y' eq resultMap.ANSWER_EDITR_YN }"){
		$('[name=cn]').val(CKEDITOR.instances.cn.getData());

		if(CKEDITOR.instances['cn'].getData().length < 1){
			alert("내용을 입력해 주세요.");
			$("[name=cn]").focus();
			return;
		}
	}else{
		if($("[name=cn]").val() == ""){
			alert("내용 입력해 주세요.");
			$("[name=cn]").focus();
			return;
		}
	}

	if(confirm("수정하시겠습니까?")){// 등록할 파일 리스트
		var uploadFileList = Object.keys(fileList);

		// 용량을 500MB를 넘을 경우 업로드 불가
		if(totalFileSize > maxUploadSize){
			// 파일 사이즈 초과 경고창
			alert("총 용량 초과\n총 업로드 가능 용량 : " + maxUploadSize + " MB");
			return;
		}
		var formData = new FormData($('#answerForm')[0]);
		for(var i = 0; i < uploadFileList.length; i++){
			formData.append('upload', fileList[uploadFileList[i]]);
		}

		$.ajax({
			url:"/admin/bbs/"+fnSysMappingCode()+"updateBbsAnswer.do",
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

//파일 다운로드
function fnDownload(file_id){
	$('[name=file_id]').val(file_id);
	$('#answerForm').attr({'action' : '/common/file/FileDown.do'}).submit();
}

//미리보기
function fnGoPreview(device){
	var subpath = "${resultMap.SYS_SUBPATH}";

	var url = "";
	if (subpath.indexOf("admin") == 0) {
		url = "${protocol}${domainAdmin}" + "/" + subpath + "/common/bbs/selectBBSPreview.do";
	}else{
		url = "${protocol}${domainCenter}" + "/" + subpath + "/common/bbs/selectBBSPreview.do";
	}

	var x = (window.screen.width / 2) - (360 / 2);
	var y = (window.screen.height / 2) - (640 / 2);
	var target="";

	if(device=='web'){
		window.open("","web");
		target = "web";
	}else{
		window.open("","mobile","width=360, height=640, left="+ x +', top='+ y + ', scrollbars=yes');
		target = "mobile";
	}
	$('#answerForm').attr({'action' : url, target: target , method:"post"}).submit();
	$('#answerForm').attr({target: "_self"});
}
//업로드 파일 목록 생성
function addFileList(fIndex, fileName, fileSize){
	var html = "";
	html += "<tr class='projects td' id='fileTr_" + fIndex + "'>";
	html += "	<td>";
	html +=			fileName + " / " + fileSize.toFixed(2) + "MB " ;
	html += "	</td>"
	html += "	<td class='text-center'>";
	html +=	"		<button class='btn bg-gradient-danger btn-sm' onclick='deleteFile(" + fIndex + "); return false;'>삭제</button>";
	html += "	</td>"
	html += "</tr>"

	$('#fileTableTbody tbody').append(html);
}
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form id="img_upload_form" enctype="multipart/form-data" method="post" style="display:none;">
					<input type="file" id="img_file" multiple="multiple" name="imgfile[]" accept="image/*">
				</form>
				<form role="form" id="answerForm" method="post" enctype="multipart/form-data">
					<input type="hidden" name="answer_seq"		value="${resultMap.ANSWER_SEQ}"/>
					<input type="hidden" name="bbs_seq"		value="${resultMap.BBS_SEQ}"/>
					<input type="hidden" name="ans_atch_doc_id" value="${resultMap.DOC_ID}" />
					<input type="hidden" name="ans_image_doc_id" value="${resultMap.IMAGE_DOC_ID}" />
					<input type="hidden" name="bbs_code"		value="${resultMap.BBS_CODE}"/>
					<input type="hidden" name="file_id"		value="" />

					<input type="hidden" name="sch_imprtnc_yn"		value="${requestScope.param.sch_imprtnc_yn}" />
					<input type="hidden" name="sch_bbs_code"		value="${requestScope.param.sch_bbs_code}" />
					<input type="hidden" name="sch_type"	value="${requestScope.param.sch_type}" />
					<input type="hidden" name="sch_text"	value="${requestScope.param.sch_text}" />
					<input type="hidden" name="sch_currentPage"		value="${requestScope.param.sch_currentPage}"/>
					<input type="hidden" name="ans_currentPage"		value="${requestScope.param.currentPage}"/>

					<input type="hidden" name="is_answer" value="Y"/>
					<input type="hidden" name="ANSWER_YN" value="${resultMap.ANSWER_YN}"/>

					<div id="imageBox"></div>

					<div class="card card-info card-outline">
						<div class="card-header">
							<h3 class="card-title">답변 No. ${resultMap.ANSWER_SEQ}</h3>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
								<colgroup>
									<col style="width:15%;">
									<col style="width:35%;">
									<col style="width:15%;">
									<col style="width:35%;">
								</colgroup>
								<tbody>
									<tr>
										<th>작성자</th>
										<td>
											${resultMap.USER_NM}
										</td>
										<th>등록일</th>
										<td>
											${resultMap.REGIST_DT}
										</td>
									</tr>
									<tr>
										<th class="required_field">내용</th>
										<td colspan="3">
											<textarea class="form-control" rows="7" id="cn" name="cn" style="resize: vertical;">${resultMap.CN}</textarea>
										</td>
									</tr>
								<c:if test="${'Y' eq resultMap.ANSWER_ATCH_FILE_YN}">
									<tr>
										<th>첨부파일</th>
										<td colspan="3">
											<div class="upload-btn-wrapper">
												<input type="file" id="input_file" name="fileList[]" multiple="multiple" style="width: 100%;"/>
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
														<col style="width:100px;">
													</colgroup>
													<thead>
														<tr class="projects td">
															<th class="text-center">파일명</th>
															<th></th>
														</tr>
													</thead>
													<tbody>
													</tbody>
												</table>
											</div>
										</td>
									</tr>
									<c:if test="${fn:length(ansFileList) > 0}">
									<tr>
										<th>첨부파일 목록</th>
										<td colspan="3">
											<div class="outBox1">
											<c:forEach var="file" items="${ansFileList}" varStatus="status">
												<div>
													<a href="#" onclick="fnDownload('${file.fileId}'); return false;">
														<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(file.fileExtNm)}.gif" width="16" height="16" class="ans_attach_file v_img">
														${file.fileNm} (${file.fileSize / 1000.0}) KB
														<a href="#" onclick="fnFileDel(this, '${file.fileId}'); return false;">[삭제]</a>
													</a>
													<br/>
												</div>
											</c:forEach>
											</div>
										</td>
									</tr>
									</c:if>
								</c:if>
								</tbody>
							</table>
							</div>
						</div>
						<div id="imageBox" style="margin-left:1.25em;">
							<c:if test="${'Y' eq resultMap.ANSWER_EDITR_YN }">
								<div class="pull-left">
									<button type="button" class="btn btn-info" onclick="fnGoPreview('web'); return false;">웹 사이즈 미리보기</button>
									<button type="button" class="btn btn-info" onclick="fnGoPreview('mobile'); return false;">모바일 사이즈 미리보기</button>
									<p style="color:red">* 미리보기의 내용이외의 세부항목은 샘플이므로 실제와 다를 수 있습니다. 에디터의 내용부분 스타일만 확인하시기 바랍니다.</p>
								</div>
							</c:if>
						</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnDetail(); return false;">취소</button>
									<button type="button" class="btn bg-gradient-success" onclick="fnUpdate(); return false;">수정</button>
								</div>
							</div>
						</div>
					</div>
				</form>
				<form id="img_upload_form" enctype="multipart/form-data" method="post" style="display:none;">
					<input type='file' id="img_file" multiple="multiple" name='imgfile[]' accept="image/*">
				</form>
			</div>
		</div>
	</div>
</div>
<script>
	 //객체 생성
	 var ajaxImage = {};
	 // ckeditor textarea id
	 ajaxImage["id"] = "cn";
	 // 업로드 될 디렉토리
	 ajaxImage["uploadDir"] = "upload";
	 // 한 번에 업로드할 수 있는 이미지 최대 수
	 ajaxImage["imgMaxN"] = 10;
	 // 허용할 이미지 하나의 최대 크기(MB)
	 ajaxImage["imgMaxSize"] = 10;
</script>
