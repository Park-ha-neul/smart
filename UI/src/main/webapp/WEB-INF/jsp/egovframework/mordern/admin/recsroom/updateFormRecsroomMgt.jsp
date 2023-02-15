<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript" src="/common/ckeditor/ckeditor.js" charset="UTF-8" ></script>
<script type="text/javascript" src="/common/ckeditor/adapters/jquery.js" charset="UTF-8" ></script>
<link rel="stylesheet" type="text/css" media="all" href="/common/css/dragAndDrop.css" />
<script src="/common/js/dragAndDrop.js"></script>
<script type="text/javascript">
//<![CDATA[

	$(function(){
		//f_divView('main_2', 'main_2');	//메뉴구조 유지
		fnComboStrFile('.fileBoxWrap', ${fn:length(fileList)}, 5);

		//파일 확장자 체크
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

	function fnDetail(){
		$("#aform").attr({action:"/admin/recsroom/"+fnSysMappingCode()+"selectRecsroomMgt.do", method:'post'}).submit();
	}

	function fnUpdate(){
		if($("[name=sj]").val() == ""){
			alert("제목을 입력해 주세요.");
			$("[name=sj]").focus();
			return;
		}

		$('[name=cn]').val(CKEDITOR.instances.cn.getData());

		if(confirm("수정하시겠습니까?")){
			// 등록할 파일 리스트
			var uploadFileList = Object.keys(fileList);

			// 용량을 500MB를 넘을 경우 업로드 불가
			if(totalFileSize > maxUploadSize){
				// 파일 사이즈 초과 경고창
				alert("총 용량 초과\n총 업로드 가능 용량 : " + maxUploadSize + " MB");
				return;
			}
			var formData = new FormData($('#aform')[0]);
			for(var i = 0; i < uploadFileList.length; i++){
				formData.append('upload', fileList[uploadFileList[i]]);
			}

			$.ajax({
				url:"/admin/recsroom/"+fnSysMappingCode()+"updateRecsroomMgt.do",
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


	// 첨부파일 다운로드
	function fnDownload(file_id){
		$('[name=file_id]').val(file_id);
		$('#aform').attr({'action' : '/common/file/FileDown.do'}).submit();
	}

	// 업로드 파일 목록 생성
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


	function fnGoPreview(device){

		if($("[name=sys_code]").val() == ""){
			alert("시스템을 선택해 주세요.");
			$("[name=sys_code]").focus();
			return;
		}

		if($("#sj").val() == ""){
			alert("제목을 입력해 주세요.");
			$("#sj").focus();
			return;
		}

		if($('[name=cntnts_id_yn]').val() == "N"){
			alert("콘텐츠 아이디 중복 확인을 해 주세요.");
			$("[name=cntnts_id]").focus();
			return;
		}

		var url = "/admin/cnts/selectCNTSPreview.do";

		var mobileX = (window.screen / 2) - (420 /2);
		var mobileY = (window.screen.height / 2) - (640 /2);
		var target = "";

		var webX = (window.screen / 2) - (1200 /2);
		var webY = (window.screen /2) - (800 /2);

		if(device == 'web'){
			window.open("", "web", "width=1200, height=800, left=" + webX + ", top=" + webY);
			target = "web";
		}else{
			window.open("","mobile","width=420, height=640, left="+ mobileX +', top='+ mobileY + ', scrollbars=yes');
			target = "mobile";
		}

		var uploadFileList = Object.keys(fileList);

		var data = document.getElementById("input_file").files;

		//추가한 파일
		var filesName = new Array();
		for(var i = 0; i < data.length; i++){
			filesName.push(data[i].name);
		}

		//기존파일
		var extFileCnt = $('.extFile').length;
		var extFileText = "";
		for(var i = 0; i < extFileCnt; i++){
			filesName.push($('.extFile').eq(i).text());
		}

		document.aform.fileName.value = filesName;

		$('#aform').attr({'action' : url, target:target, method:"post"}).submit();
		$('#aform').attr({target: "_blank "});




	}

//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form id="img_upload_form" enctype="multipart/form-data" method="post" style="display:none;">
					<input type="file" id="img_file" multiple="multiple" name="imgfile[]" accept="image/*">
				</form>
				<form role="form" id="aform" name="aform" method="post" enctype="multipart/form-data">
					<input type="hidden" name="recsroom_seq"		value="${requestScope.param.recsroom_seq}"/>
					<input type="hidden" name="doc_id"			value="${resultMap.DOC_ID}"/>
					<input type="hidden" name="image_doc_id"	value="${resultMap.IMAGE_DOC_ID}"/>
					<input type="hidden" name="currentPage"		value="${requestScope.param.currentPage}"/>
					<input type="hidden" name="sch_text"		value="${requestScope.param.sch_text}" />
					<input type="hidden" name="regist_dt" value="${ resultMap.REGIST_DT }">
					<input type="hidden" name="updt_dt" value="${resultMap.UPDT_DT }">
					<input type="hidden" name="hit_cnt" value="${resultMap.HIT_CNT }">
					<input type="hidden" name="file_id" />
					<input type="hidden" name="fileName" value= "" />

					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap inlineBlock">
									<colgroup>
										<col style="width:15%;">
										<col style="width:85%;">
									</colgroup>
									<tbody>
										<tr>
											<th class="required_field">제목</th>
											<td>
												<input type="text" class="form-control" id="sj" name="sj" value="${resultMap.SJ}" placeholder="제목" onkeyup="cfLengthCheck('제목은', this, 100);">
											</td>
										</tr>
										<tr>
											<th>내용</th>
											<td>
												<textarea class="form-control" rows="3" id="cn" name="cn" placeholder="내용">${resultMap.CN}</textarea>
											</td>
										</tr>
										<tr>
											<th>첨부파일 목록</th>
											<td>
												<div id="attach_file">
											<c:if test="${fn:length(fileList) > 0}">
												<c:forEach var="item" items="${fileList}" varStatus="status">
													<div>
														<a href="#" onclick="fnDownload('${item.fileId}'); return false;">
															<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(item.fileExtNm)}.gif" width="16" height="16" class="attach_file">
															<span class="extFile">${item.fileNm}</span> (${item.fileSize / 1000.0}) KB
														</a>
														<a href="#" onclick="fnFileDel(this, '${item.fileId}'); return false;">[삭제]</a><br/>
													</div>
												</c:forEach>
											</c:if>
												</div>
											</td>
										</tr>
										<tr>
											<th>첨부파일</th>
											<td>
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
													<table class="table table-bordered" id="fileTableTbody">
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
										<tr>
											<th>전시여부</th>
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
										</tr>
										<tr>
											<th>삭제여부</th>

											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="delete_yn" id="delete_yn_y" value="Y" <c:if test="${resultMap.DELETE_YN eq 'Y'}">checked="checked"</c:if> />
													<label class="form-check-label" for="delete_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="delete_yn" id="delete_yn_n" value="N" <c:if test="${resultMap.DELETE_YN eq 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="delete_yn_n" style="margin-right:10px;">아니오</label>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div id="imageBox" style="margin-top:1.25em">
								<div class="pull-left">
									<button type="button" class="btn btn-info" onclick="fnGoPreview('web'); return false;">웹 사이즈 미리보기</button>
									<button type="button" class="btn btn-info" onclick="fnGoPreview('mobile'); return false;">모바일 사이즈 미리보기</button>
									<p style="color:red">* 미리보기의 내용이외의 세부항목은 샘플이므로 실제와 다를 수 있습니다. 에디터의 내용부분 스타일만 확인하시기 바랍니다.</p>
								</div>
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
<script>
	$(function(){
	  	CKEDITOR.replace('cn',{
	  		filebrowserUploadUrl: '${pageContext.request.contextPath}/photoServerUpload.do'
	  	});
	  });
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
