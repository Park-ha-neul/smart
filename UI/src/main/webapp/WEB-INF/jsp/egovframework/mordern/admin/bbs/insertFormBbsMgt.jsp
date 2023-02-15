<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<%@ taglib prefix="CacheCommboUtil"
	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
<script type="text/javascript" src="/common/ckeditor/ckeditor.js"
	charset="UTF-8"></script>
<script type="text/javascript" src="/common/ckeditor/adapters/jquery.js"
	charset="UTF-8"></script>
<link rel="stylesheet" type="text/css" media="all"
	href="/common/css/dragAndDrop.css" />
<script src="/common/js/dragAndDrop.js"></script>
<script type="text/javascript">
//<![CDATA[
	var editor_on = "";
	$(function(){
		fnComboStrFile('.fileBoxWrap', 0, 5);

		//게시판 코드 초기에 선택 불가
		$('select[name=bbs_code]').attr('disabled',true);

		//기관 셀렉트 박스 변경시 게시판 코드 조회
		$('[name=sys_code]').on({'change' : function(){
				searchBbsCode($(this).val())
			}
		});

		//게시판코드 셀렉트 박스 변경시 입력폼 레이아웃수정
		$('[name=bbs_code]').on({'change' : function(){
				selectBbsMngInfo($(this).val())
			}
		});

		if(<c:out value="${requestScope.param.ss_author_id != CODE_AUTHOR_ADMIN}" />){
			searchBbsCode($('[name=sys_code]').val());
		}

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

		if ("${sysMappingCode}" == "") {
			$('[name=sys_code]').html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sys_code, '시스템')}");
		} else {
			$('[name=sys_code]').html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code, 'CODE', 'CODE_NM', requestScope.param.sys_code, '')}");
			searchBbsCode($('[name=sys_code]').val());
		}
	});

	//게시판 코드 조회
	function searchBbsCode(sysCode){
		var req = {
				sys_code : sysCode
		};

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/admin/bbs/'+fnSysMappingCode()+'selectListBbsCodeAjax.do',
			data : req,
			success : function(param) {
				if(param.resultStats.resultCode == "error"){
					alert(param.resultStats.resultMsg);
					return;
				}

				if(param.bbsCodeList.length == 0){
					alert('해당 기관에 등록된 게시판이 없습니다.')
					$('[name=sys_code] option:eq(0)').prop("selected", true);
					$("select[name=bbs_code]").empty().append('<option value="">선택</option>') ;
					$('select[name=bbs_code]').attr('disabled',true);
					return;
				}else{
					$("select[name=bbs_code]").empty().append('<option value="">선택</option>') ;
					$('select[name=bbs_code]').attr('disabled',false);
					var bbsMngInfo;
					var selectOption;
					for(var i = 0; i < param.bbsCodeList.length; i++){
							bbsMngInfo = param.bbsCodeList[i];
							$('select[name=bbs_code]').append("<option value='" + bbsMngInfo.BBS_CODE + "'>" + bbsMngInfo.BBS_NM + " (" + bbsMngInfo.BBS_CODE  + ")</option>");
					}
				}
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	}

	//게시판 관리 정보 조회
	function selectBbsMngInfo(bbsCode){

		cntsDisplayState();

		var req = {
				bbs_code : bbsCode
		};

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/admin/bbs/'+fnSysMappingCode()+'selectBbsMngInfoAjax.do',
			data : req,
			success : function(param) {
				if(param.resultStats.resultCode == "error"){
					alert(param.resultStats.resultMsg);
					return;
				}
				cntsDisplayState(param.bbsMngInfo);
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	}

	//게시판 관리 정보에 따라 입력 필드를 보여줌
	function cntsDisplayState(obj){

		if(obj == undefined){
			editor_on="N";
			//ckeditor 제거후 textarea 다시 그림
			$('#cke_cn').remove();
			$('#cn_row').find('td').html('<textarea class="form-control" rows="7" id="cn" name="cn" style="resize: vertical;"></textarea>');
			//ckeditor삭제시  에디터에서 추가된 img path정보를 비움
			$('#imageBox').empty();

			//입력필드 초기화
			$('input[type=text]').val("");
			$('textarea').val("");

			//첨부 파일관련 초기화
			$('#thumb_file_row').find('td').html('<div class="custom-file"><input type="file" class="custom-file-input" name="thumb_file" accept=".gif, .jpg, .png"/><label class="custom-file-label" for="customFile">선택된 파일 없음</label></div><p style="color:#888888">(적정사이즈는 320x250 입니다.)</p>');
			$('#sel_file_box0').val('1').trigger('change');

			$('#text_cn_row').hide();
			$('#thumb_file_row').hide();
			$('#atch_file_row').hide();
			$('#kwrd_row').hide();
			$('#link_url_row').hide();
			$('#mvp_url_row').hide();
			$('#imprtnc_yn_row').hide();
			$('#btn_preview').hide();

		}else{

			if(obj.EDITR_YN == 'Y'){
				$('#text_cn_row').show();
				CKEDITOR.replace('cn',{
			  		filebrowserUploadUrl: '${pageContext.request.contextPath}/photoServerUpload.do'
			  	});
				editor_on ="Y";

				//미리보기용 세팅
				var html = "<button type=\"button\" class=\"btn btn-info\" onclick=\"fnGoPreview('web'); return false;\">웹 사이즈 미리보기</button>&nbsp;";
				html += "<button type=\"button\" class=\"btn btn-info\" onclick=\"fnGoPreview('mobile'); return false;\">모바일 사이즈 미리보기</button>";
				html += "<p style=\"color:red\">* 미리보기의 내용이외의 세부항목은 샘플이므로 실제와 다를 수 있습니다. 에디터의 내용부분 스타일만 확인하시기 바랍니다.</p>";
				$('#btn_preview').html(html);
				$('#btn_preview').show();
				$('[name=BBS_NM]').val(obj.BBS_NM);
				$('[name=EDITR_YN]').val(obj.EDITR_YN);
				$('[name=ANSWER_YN]').val(obj.ANSWER_YN);
				$('[name=BBS_TY_CODE]').val(obj.BBS_TY_CODE);
				$('[name=REGIST_YN]').val(obj.REGIST_YN);
			}else{
				$('#btn_preview').html("");
			}

			if(obj.BBS_TY_CODE == '20' || obj.BBS_TY_CODE == '30' || obj.BBS_TY_CODE == '31'){

				$('#thumb_file_row').find('p').html("(적정사이즈는 350x240 입니다.)");
				$('#thumb_file_row').show();
			}
			if(obj.ATCH_FILE_YN == 'Y'){
				$('#atch_file_row').show();
			}
			if(obj.KWRD_YN == 'Y'){
				$('#kwrd_row').show();
			}
			if(obj.LINK_URL_YN == 'Y'){
				$('#link_url_row').show();
			}
			if(obj.MVP_URL_YN == 'Y'){
				$('#mvp_url_row').show();
			}
			if(obj.BBS_TY_CODE == '10' || obj.BBS_TY_CODE == '11' ){
				$('#imprtnc_yn_row').show();
			}

		}
	}

	//게시물 관리 목록
	function fnList(){
		document.location.href="/admin/bbs/"+fnSysMappingCode()+"selectPageListBbsMgt.do";
	}

	//게시물 관리 등록
	function fnInsert(){

		if($("[name=sys_code]").val() == ""){
			alert("시스템을 선택해 주세요.");
			$("[name=sys_code]").focus();
			return;
		}
		if($("[name=bbs_code]").val() == ""){
			alert("게시판 코드를 선택해 주세요.");
			$("[name=bbs_code]").focus();
			return;
		}
		if($("[name=sj]").val() == ""){
			alert("제목 입력해 주세요.");
			$("[name=sj]").focus();
			return;
		}
		if(editor_on == 'Y'){
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

		if(confirm("등록하시겠습니까?")){
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
					url:"/admin/bbs/"+fnSysMappingCode()+"insertBbsMgt.do",
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
		}

	//미리보기
 	/* function fnGoPreview(device){
		if($("[name=sys_code]").val() == ""){
			alert("시스템을 선택해 주세요.");
			$("[name=sys_code]").focus();
			return;
		}

		var subpath = $('[name=sys_code] option:selected').attr("attr_1");

		var url = "";

		if(subpath == "admin"){
			url = "${Globals.PROTOCOL}${Globals.DOMAIN_ADMIN}" + "/" + subpath + "/common/bbs/selectBBSPreview.do";
		}else{
			url = "${Globals.PROTOCOL}${Globals.DOMAIN_CENTER}" + "/" + subpath + "/common/bbs/selectBBSPreview.do";
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
	}  */

	//미리보기 관리자 페이지
	function fnGoPreview(device){
		var fo = document.aform;

		if($("[name=sys_code]").val() == ""){
			alert("시스템을 선택해 주세요.");
			$("[name=sys_code]").focus();
			return;
		}
		if($("[name=bbs_code]").val() == ""){
			alert("게시판 코드를 선택해 주세요.");
			$("[name=bbs_code]").focus();
			return;
		}
		if($("[name=sj]").val() == ""){
			alert("제목 입력해 주세요.");
			$("[name=sj]").focus();
			return;
		}
		if(editor_on == 'Y'){
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

		var url = "/admin/bbs/selectBBSPreview.do";

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

		var filesName = new Array();
		for(var i = 0; i < data.length; i++){
			filesName.push(data[i].name);
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
				<form id="img_upload_form" enctype="multipart/form-data"
					method="post" style="display: none;">
					<input type='file' id="img_file" multiple="multiple"
						name='imgfile[]' accept="image/*">
				</form>
				<form role="form" id="aform" name="aform" method="post"
					enctype="multipart/form-data">
					<input type="hidden" name="USER_NM" value="${ssUserNm}" /> <input
						type="hidden" name="BBS_NM" value="" /> <input type="hidden"
						name="EDITR_YN" value="" /> <input type="hidden" name="ANSWER_YN"
						value="" /> <input type="hidden" name="BBS_TY_CODE" value="" />
					<input type="hidden" name="REGIST_YN" value="" />
					<input type="hidden" name="fileName" value= "" />
					<div id="imageBox"></div>
					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
								<table class="table inlineBlock text-nowrap">
									<colgroup>
										<col style="width: 15%;">
										<col style="width: 35%;">
										<col style="width: 15%;">
										<col style="width: 35%;">
									</colgroup>
									<tbody>
										<tr class='projects td'>
											<th class="required_field">시스템</th>
											<td><select name="sys_code" class="form-control">
											</select></td>
											<th class="required_field">게시판 코드</th>
											<td><select name="bbs_code" class="form-control">
													<option value="">선택</option>
											</select></td>
										</tr>
										<tr class='projects td'>
											<th class="required_field">제목</th>
											<td colspan="3"><input type="text" class="form-control"
												id="sj" name="sj" maxlength="100" /></td>
										</tr>
										<tr id="cn_row" class='projects td'>
											<th class="required_field">내용</th>
											<td colspan="3"><textarea class="form-control" rows="7"
													id="cn" name="cn" style="resize: vertical;"></textarea></td>
										</tr>
										<tr id="text_cn_row" class='projects td'
											style="display: none;">
											<th>텍스트 내용</th>
											<td colspan="3"><textarea class="form-control" rows="7"
													id="text_cn" name="text_cn" style="resize: vertical;"></textarea>
											</td>
										</tr>
										<tr class='projects td'>
											<th>비고</th>
											<td colspan="3"><textarea class="form-control" rows="7"
													id="rm" name="rm" style="resize: vertical;"
													maxlength="2000"></textarea></td>
										</tr>
										<tr id="thumb_file_row" class='projects td'
											style="display: none;">
											<th>썸네일 이미지</th>
											<td colspan="3">
												<div class="custom-file">
													<input type="file" class="custom-file-input"
														name="thumb_file" multiple="multiple"
														accept=".gif, .jpg, .png" /> <label
														class="custom-file-label" for="customFile">선택된 파일
														없음</label>
												</div>
												<p style="color: #888888">(적정사이즈는 300x200 입니다.)</p>
											</td>
										</tr>
										<tr id="atch_file_row" class='projects td'
											style="display: none;">
											<th>첨부파일</th>
											<td colspan="3">
												<div class="upload-btn-wrapper">
													<input type="file" id="input_file" name="fileList[]"
														multiple="multiple" style="width: 100%;" />
													<button class="upload-btn btn btn-outline-secondary">파일선택</button>
												</div>
												<div id="dropZone" class="margin-bottom dropZone11"
													style="margin-bottom: 20px;">
													<div id="fileDragDesc">파일을 드래그 해주세요.</div>
												</div>
												<div class="table-responsive">
													<table class="table text-nowrap" id="fileTableTbody">
														<colgroup>
															<col style="width: *%;">
															<col style="width: 100px;">
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
										<tr id="kwrd_row" class='projects td' style="display: none;">
											<th>키워드</th>
											<td colspan="3"><input type="text" class="form-control"
												id="kwrd" name="kwrd" maxlength="2000"
												placeholder="키워드는 쉼표(,)로 구분 50자까지 입력 가능합니다. ex)키워드1,키워드2" />
											</td>
										</tr>
										<tr id="link_url_row" class='projects td'
											style="display: none;">
											<th>링크 URL</th>
											<td colspan="3"><input type="text" class="form-control"
												id="link_url" name="link_url" maxlength="500" /></td>
										</tr>
										<tr id="mvp_url_row" class='projects td'
											style="display: none;">
											<th>동영상 URL</th>
											<td colspan="3"><textarea class="form-control" rows="5"
													id="mvp_url" name="mvp_url" maxlength="500"
													style="resize: vertical;"></textarea></td>
										</tr>
										<tr id="imprtnc_yn_row" class='projects td'
											style="display: none;">
											<th>중요 여부</th>
											<td><label class="animated-radio-button mr-2"> <input
													type="radio" name="imprtnc_yn" id="imprtnc_y" value="Y" />
													<span class="label-text">예</span>
											</label> <label class="animated-radio-button"> <input
													type="radio" name="imprtnc_yn" id="imprtnc_n" value="N"
													checked="checked" /> <span class="label-text">아니오</span>
											</label></td>
											<th>중요 순서</th>
											<td><input type="text" class="form-control numeric"
												id="imprtnc_ordr" name="imprtnc_ordr" maxlength="3"
												disabled="disabled" /></td>
										</tr>
										<tr class='projects td'>
											<th>공개 여부</th>
											<td colspan="3" class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="open_yn"
														id="open_y" value="Y" checked="checked" /> <label
														class="form-check-label" for="open_y">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="open_yn"
														id="open_n" value="N" /> <label class="form-check-label"
														for="open_n">아니오</label>
												</div>
											</td>
										</tr>
										<tr class='projects td'>
											<th>전시 여부</th>
											<td colspan="3" class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="disp_yn"
														id="disp_y" value="Y" checked="checked" /> <label
														class="form-check-label" for="disp_y">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="disp_yn"
														id="disp_n" value="N" /> <label class="form-check-label"
														for="disp_n">아니오</label>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
								<div class="pt-3" id=btn_preview></div>
							</div>
						</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-secondary mr-2"
										onclick="fnList(); return false;">목록</button>
									<button type="button" class="btn bg-gradient-success"
										onclick="fnInsert(); return false;">확인</button>
								</div>
							</div>
						</div>
					</div>
				</form>
				<form id="img_upload_form" enctype="multipart/form-data"
					method="post" style="display: none;">
					<input type='file' id="img_file" multiple="multiple"
						name='imgfile[]' accept="image/*">
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
