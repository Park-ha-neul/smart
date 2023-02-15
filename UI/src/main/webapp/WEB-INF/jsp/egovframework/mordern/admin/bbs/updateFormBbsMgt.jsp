<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript" src="/common/ckeditor/ckeditor.js" charset="UTF-8" ></script>
<script type="text/javascript" src="/common/ckeditor/adapters/jquery.js" charset="UTF-8" ></script>
<link rel="stylesheet" type="text/css" media="all" href="/common/css/dragAndDrop.css" />
<script src="/common/js/dragAndDrop.js"></script>
<script type="text/javascript">
//<![CDATA[
	$(function(){

		$('.v_img').on({
			// 이미지가 없어서 error 날시
			'error' : function(){
				$(this).attr('src', '<%=imgRoot %>common/no-image.png');
			}
		});

		fnComboStrFile('#atch_file_row .fileBoxWrap', ${fn:length(atchFileList)}, 5);

		<%-- 자료실인경우 첨부파일 1개만 허용 --%>
		<c:if test="${resultMap.BBS_TY_CODE == BBS_TY_30}">
			fnComboStrFile('#atch_file_row .fileBoxWrap', ${fn:length(atchFileList)}, 1);
		</c:if>

		//에디터 사용 가부가 Y일 경우 답변 에디터 생성
		if(<c:out value="${resultMap.EDITR_YN == 'Y'}" />){
			CKEDITOR.replace('cn',{
		  		filebrowserUploadUrl: '${pageContext.request.contextPath}/photoServerUpload.do'
		  	});
		}

		$('[name=regist_dt]').datetimepicker({
			locale : 'ko', 	// 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD HH:mm',
			useCurrent: false, 	//Important! See issue #1075
			sideBySide : true,
		});

		var agent = navigator.userAgent.toLowerCase();
		var checkIE = (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1);

		//썸네일 확장자 체크
		$(document).on('change', '[name=thumb_file]', function(){
			if(!f_CheckAcceptFileExt('thumb_file', "JPG|JPEG|GIF|PNG")){
				alert('썸네일 이미지는 (JPG,JPEG,GIF,PNG)파일만 업로드 가능합니다. ');
				if ( checkIE ){
					$(this).replaceWith( $(this).clone(true) );
				}else{
					$(this).val("");
				}
				return;
			}

			//썸네일 가로,세로 길이 체크
			var _URL = window.URL;
			var file, img;
			var w = 0;
			var h = 0;
			if ((file = this.files[0])) {
				img = new Image();
				img.onload = function () {
					w = this.width;
					h = this.height;

					if(w > 500){
						if(!confirm("이미지가 적정사이즈 보다 큽니다. \n등록하시겠습니까?")){
							if ( checkIE ){
								$('[name=thumb_file]').replaceWith( $('[name=thumb_file]').clone(true) );
							}else{
								$('[name=thumb_file]').val("");
							}
							return;
						}
					}
				};
				img.src = _URL.createObjectURL(file);
			}
		});

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

		$(document).on('change', '[name=imprtnc_yn]', function(){
			if($(this).val() == 'Y'){
				$('[name=imprtnc_ordr]').attr("disabled", false);
			}else if($(this).val() == 'N'){
				$('[name=imprtnc_ordr]').attr("disabled", true);
				$('[name=imprtnc_ordr]').val("");
			}
		});


	});

	//상세화면 이동
	function fnDetail(){
		$("#aform").attr({action:"/admin/bbs/"+fnSysMappingCode()+"selectBbsMgt.do", method:'post'}).submit();
	}

	//수정
	function fnUpdate(){
		if($("[name=sj]").val() == ""){
			alert("제목 입력해 주세요.");
			$("[name=sj]").focus();
			return;
		}

		if(${requestScope.resultMap.EDITR_YN == 'Y'}){
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
		if($("[name=imprtnc_yn]:checked").val() == "Y" && $("[name=imprtnc_ordr]").val() == ""){
			alert("중요순서를 입력해 주세요.");
			$("[name=imprtnc_ordr]").focus();
			return;
		}
		if($("[name=regist_dt]").val() == ""){
			alert("등록일자를 입력해 주세요.");
			$("[name=regist_dt]").focus();
			return;
		}

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
				url:"/admin/bbs/"+fnSysMappingCode()+"updateBbsMgt.do",
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
		$('#aform').attr({'action' : '/common/file/FileDown.do'}).submit();
	}

	//서버에 있는 파일 삭제
	function fnFileDel(obj, file_id){

		if(confirm('삭제하시겠습니까?')){

			var param = { 'file_id' : file_id };

			$.ajax( {
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

						// 썸네일 이미지 삭제인경우
						if($(obj).parent().parent().hasClass('thumbBoxWrap')){
							$(obj).parent().remove();
							var fileSizeStr = "(적정사이즈는 350x240 입니다.)";
								var html='<input type="file" name="thumb_file"/> <p style="color:#888888">'+fileSizeStr+'</p>';
							$('.thumbBoxWrap').html(html);
						}
						// 첨부파일인 경우
						else if($(obj).closest('tr').hasClass('atach_tr')){
							var $tr = $(obj).closest('tr');

							//첨부파일을 다 삭제하면 첨부파일 목록태그까지 삭제
							if($tr.find('.attach_file').length == 1){
								$tr.remove();
								$tr = $('');
							} else {
								// 해당 첨부파일만 삭제
								$(obj).parent().remove();
							}

							// 첨부파일 선택 창 다시 그리기
							var n = $tr.find('.attach_file').length;

						// 자료실인경우
						<c:if test="${resultMap.BBS_TY_CODE == BBS_TY_30}">
							fnComboStrFile('#atch_file_row .fileBoxWrap', n, 1);
						</c:if>
						<c:if test="${resultMap.BBS_TY_CODE != BBS_TY_30}">
							fnComboStrFile('#atch_file_row .fileBoxWrap', n, 5);
						</c:if>
						}
					}
				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}
			});
		}
	}
	//미리보기 사용자 페이지
/* 	function fnGoPreview(device){
		var subpath ="${resultMap.SYS_SUBPATH}"

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
		$('#aform').attr({'action' : url, target: target , method:"post"}).submit();
		$('#aform').attr({target: "_self"});
	}  */

	//미리보기 관리자 페이지
	function fnGoPreview(device){
		var fo = document.aform;

		if(fo.sj.value == ""){
			alert("제목을 입력해 주세요");
			$("[name=sj]").focus();
			return;
		}

		if(fo.cn.value == ""){
			alert("내용을 입력해주세요");
			$("[name=cn]").focus();
			return;
		}

		if(fo.regist_dt.value == ""){
			alert("등록일자를 입력해주세요");
			$("[name=regist_dt]").focus();
			return;
		}

		var subpath ="${resultMap.SYS_SUBPATH}";

		var url = "/admin/bbs/selectBBSPreview.do";

		var mobileX = (window.screen / 2) - (420 /2);
		var mobileY = (window.screen.height / 2) - (640 /2);
		var target = "";

		var webX = (window.screen / 2) - (1200 /2);
		var webY = (window.screen /2) - (800 /2);

		if(device == 'web'){
			window.open("", "web", "width=1200, height=800, left=" + webX + ", top=" + webY );
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
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form id="img_upload_form" enctype="multipart/form-data" method="post" style="display:none;">
					<input type='file' id="img_file" multiple="multiple" name='imgfile[]' accept="image/*">
				</form>
				<form role="form" id="aform" name="aform" method="post" action="/admin/bbs/updateBbsMgt.do" enctype="multipart/form-data" >
					<input type="hidden" name="bbs_seq"		value="${requestScope.param.bbs_seq}"/>
					<input type="hidden" name="bbs_code"		value="${requestScope.param.bbs_code}"/>
					<input type="hidden" name="sch_imprtnc_yn"		value="${requestScope.param.sch_imprtnc_yn}" />
					<input type="hidden" name="sch_organ_code"		value="${requestScope.param.sch_organ_code}" />
					<input type="hidden" name="sch_bbs_code"		value="${requestScope.param.sch_bbs_code}" />
					<input type="hidden" name="sch_type"	value="${requestScope.param.sch_type}" />
					<input type="hidden" name="sch_text"	value="${requestScope.param.sch_text}" />
					<input type="hidden" name="currentPage"		value="${requestScope.param.sch_currentPage}"/>
					<input type="hidden" name="image_doc_id" value="${resultMap.IMAGE_DOC_ID}" />
					<input type="hidden" name="atch_doc_id" value="${resultMap.DOC_ID}" />
					<input type="hidden" name="thumb_doc_id" value="${resultMap.THUMB_DOC_ID}" />
					<input type="hidden" name="file_id"/>
					<input type="hidden" name="BBS_NM" value="${resultMap.BBS_NM}"/>
					<input type="hidden" name="EDITR_YN" value="${resultMap.EDITR_YN}"/>
					<input type="hidden" name="ANSWER_YN" value="${resultMap.ANSWER_YN}"/>
					<input type="hidden" name="USER_NM" value="${resultMap.USER_NM}"/>
					<input type="hidden" name="REGIST_YMD" value="${resultMap.REGIST_YMD}"/>
					<input type="hidden" name="BBS_TY_CODE" value="${resultMap.BBS_TY_CODE}"/>
					<input type="hidden" name="REGIST_YN" value="${resultMap.REGIST_YN}"/>
					<input type="hidden" name="HIT_CNT" value="${resultMap.HIT_CNT }" />
					<input type="hidden" name="SYS_CODE_NM" value="${resultMap.SYS_CODE_NM }" />
					<input type="hidden" name="CTTPC_SE_NM" value="${resultMap.CTTPC_SE_NM}">
					<input type="hidden" name="REGISTER_CTTPC" value="${ resultMap.REGISTER_CTTPC}" />
					<input type="hidden" name="fileName" value= "" />

					<!--페이지 당 목록 수 조건-->
					<input type="hidden" name="sch_row_per_page" value="${requestScope.param.sch_row_per_page}"/>

					<div id="imageBox"></div>

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
												${resultMap.SYS_CODE_NM}
											</td>
											<th class="required_field">게시판 명</th>
											<td>
												${resultMap.BBS_NM}
											</td>
										</tr>
										<tr>
											<th>작성자</th>
											<td>
												${resultMap.USER_NM}
											</td>
											<th>연락처</th>
											<td>
												<c:if test="${resultMap.REGISTER_CTTPC ne ''}">
													[${resultMap.CTTPC_SE_NM}]
													${resultMap.REGISTER_CTTPC}
												</c:if>
											</td>
										</tr>
										<tr>
											<th>조회수</th>
											<td>
												${resultMap.HIT_CNT}
											</td>
											<th>등록일자</th>
											<td class="datetimepicker-td">
												<input type="text" class="form-control" name="regist_dt" value="${resultMap.REGIST_DT}"  />
											</td>
										</tr>
										<tr>
											<th class="required_field">제목</th>
											<td colspan="3">
												<input type="text" class="form-control" id="sj" name="sj" maxlength="100" value="${resultMap.SJ}" />
											</td>
										</tr>
										<tr>
											<th class="required_field">내용</th>
											<td colspan="3">
												<textarea class="form-control" rows="7" id="cn" name="cn" style="resize: vertical;">${resultMap.CN}</textarea>
											</td>
										</tr>
									<c:if test="${resultMap.EDITR_YN == 'Y'}">
										<tr>
											<th>텍스트 내용</th>
											<td colspan="3">
												<textarea class="form-control" rows="7" id="text_cn" name="text_cn" style="resize: vertical;">${resultMap.TEXT_CN}</textarea>
											</td>
										</tr>
									</c:if>
										<tr>
											<th>비고</th>
											<td colspan="3">
												<textarea class="form-control" rows="7" id="rm" name="rm" style="resize: vertical;" maxlength="2000">${resultMap.RM}</textarea>
											</td>
										</tr>
									<c:if test="${resultMap.BBS_TY_CODE == BBS_TY_20 || resultMap.BBS_TY_CODE == BBS_TY_30 || resultMap.BBS_TY_CODE == BBS_TY_31}">
										<tr>
											<th>썸네일 이미지</th>
											<td colspan="3">
												<div class="thumbBoxWrap">
											<c:if test="${fn:length(thumbImgFileList) > 0}">
												<c:forEach var="file" items="${thumbImgFileList}" varStatus="status">
													<div>
														<a href="#" onclick="fnDownload('${file.fileId}'); return false;">
															<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(file.fileExtNm)}.gif" width="16" height="16" class="thumb_file v_img">
															${file.fileNm} (${file.fileSize / 1000.0}) KB
															<a href="#" onclick="fnFileDel(this, '${file.fileId}'); return false;">[삭제]</a>
														</a>
														<br/>
													</div>
												</c:forEach>
											</c:if>
											<c:if test="${fn:length(thumbImgFileList) == 0}">
													<input type="file" name="thumb_file" /> <p style="color:#888888">(적정사이즈는 300x200 입니다.)</p>
											</c:if>
												</div>
											</td>
										</tr>
									</c:if>
									<c:if test="${resultMap.ATCH_FILE_YN == 'Y'}">
										<tr id="atch_file_row">
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
										<c:if test="${fn:length(atchFileList) > 0}">
										<tr class="atach_tr">
											<th>첨부파일 목록</th>
											<td colspan="3">
												<div class="outBox1">
											<c:forEach var="file" items="${atchFileList}" varStatus="status">
													<div>
														<a href="#" onclick="fnDownload('${file.fileId}'); return false;">
															<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(file.fileExtNm)}.gif" width="16" height="16" class="attach_file v_img">
															<span class="extFile">${file.fileNm} </span> (${file.fileSize / 1000.0}) KB
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
									<c:if test="${resultMap.KWRD_YN == 'Y'}">
										<tr>
											<th>키워드</th>
											<td colspan="3">
												<input type="text" class="form-control" id="kwrd" name="kwrd" maxlength="2000" value="${resultMap.KWRD}" />
											</td>
										</tr>
									</c:if>
									<c:if test="${resultMap.LINK_URL_YN == 'Y'}">
										<tr>
											<th>링크 URL</th>
											<td colspan="3">
												<input type="text" class="form-control" id="link_url" name="link_url" maxlength="500" value="${resultMap.LINK_URL}" />
											</td>
										</tr>
									</c:if>
									<c:if test="${resultMap.MVP_URL_YN == 'Y'}">
										<tr>
											<th>동영상 URL</th>
											<td colspan="3">
												<textarea class="form-control" rows="5" id="mvp_url" name="mvp_url" maxlength="500" style="resize: vertical;">${resultMap.MVP_URL}</textarea>
											</td>
										</tr>
									</c:if>
									<c:if test="${resultMap.BBS_TY_CODE == BBS_TY_10}">
										<tr>
											<th>중요 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input"  type="radio" name="open_yn" id="open_y" value="Y" <c:if test="${resultMap.OPEN_YN != 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="open_y">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="open_yn" id="open_n" value="N" <c:if test="${resultMap.OPEN_YN == 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="open_n">아니오</label>
												</div>
											</td>
											<th>중요 순서</th>
											<td>
												<input type="text" class="form-control numeric" id="imprtnc_ordr" name="imprtnc_ordr" maxlength="3" value="<c:if test="${resultMap.IMPRTNC_ORDR == ''}">${resultMap.IMPRTNC_ORDR}</c:if>" <c:if test="${resultMap.IMPRTNC_YN != 'Y'}">disabled="disabled"</c:if> />
											</td>
										</tr>
									</c:if>
										<tr>
											<th>공개 여부</th>
											<td colspan="3" class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="open_yn" id="open_y" value="Y" <c:if test="${resultMap.OPEN_YN != 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="open_y">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="open_yn" id="open_n" value="N" <c:if test="${resultMap.OPEN_YN == 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="open_n">아니오</label>
												</div>
											</td>
										</tr>
										<tr>
											<th>전시 여부</th>
											<td colspan="3" class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="disp_yn" id="disp_y" value="Y" <c:if test="${resultMap.DISP_YN != 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="disp_y">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="disp_yn" id="disp_n" value="N" <c:if test="${resultMap.DISP_YN == 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="disp_n">아니오</label>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div id="imageBox" style="margin-left:1.25em;">
							<c:if test="${resultMap.EDITR_YN == 'Y'}">
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
									<button type="button" class="btn bg-gradient-success" onclick="fnUpdate(); return false;">확인</button>
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
