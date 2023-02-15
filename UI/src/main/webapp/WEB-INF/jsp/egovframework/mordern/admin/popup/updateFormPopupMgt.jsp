<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript" src="/common/ckeditor/ckeditor.js" charset="UTF-8" ></script>
<script type="text/javascript" src="/common/ckeditor/adapters/jquery.js" charset="UTF-8" ></script>

<script type="text/javascript">
//<![CDATA[
	$(function(){
		$('[name=disp_begin_de]').datetimepicker({
			locale : 'ko', 	// 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD HH:mm',
			useCurrent: false, 	//Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'left',
				vertical : 'bottom'
			},
		});

		$('[name=disp_end_de]').datetimepicker({
			locale : 'ko', 	// 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD HH:mm',
			useCurrent: false, 	//Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'right',
				vertical : 'bottom'
			},
		});

		//팝업 이미지 확장자 체크
		$(document).on('change', '[name=upload]', function(){
			if(!f_CheckAcceptFileExt('upload', "JPG|JPEG|GIF|PNG")){
				// .gif, .jpg, .png
				alert('팝업 이미지는 (JPG,JPEG,GIF,PNG)파일만 업로드 가능합니다. ');
				var agent = navigator.userAgent.toLowerCase();
				if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
					$(this).replaceWith( $(this).clone(true) );
				}else{
					$(this).val("");
					$(".custom-file-label").html("선택된 파일 없음");
				}
				return;
			}
		});
	});

	function fnDetail(){
		$("#aform").attr({action:"/admin/popup/"+fnSysMappingCode()+"selectPopupMgt.do", method:'post'}).submit();
	}

	function fnUpdate(){
		if($("[name=sj]").val() == ""){
			alert("제목을 입력해 주세요.");
			$("[name=sj]").focus();
			return;
		}
		if($("[name=popup_se_code]").val() == "W"){	//Web 일경우

			if($("[name=width_size]").val() == ""){
				alert("가로사이즈를 입력해 주세요.");
				$("[name=width_size]").focus();
				return;
			}

			if($("[name=vrticl_size]").val() == ""){
				alert("세로사이즈를 입력해 주세요.");
				$("[name=vrticl_size]").focus();
				return;
			}
		}
		if($("[name=disp_begin_de]").val() == ""){
			alert("전시 시작일을 입력해 주세요.");
			$("[name=disp_begin_de]").focus();
			return;
		}
		if($("[name=disp_end_de]").val() == ""){
			alert("전시 종료일을 입력해 주세요.");
			$("[name=disp_end_de]").focus();
			return;
		}

		if(confirm("수정하시겠습니까?")){
			$("#aform").attr({action:"/admin/popup/"+fnSysMappingCode()+"updatePopupMgt.do", method:'post'}).submit();
		}
	}

	//파일 다운로드
	function fnDownload(file_id){
		$('[name=file_id]').val(file_id);
		$('#aform').attr({'action' : '/common/file/FileDown.do'}).submit();
	}

	//서버에 있는 파일 삭제
	function fnFileDel(obj, file_id, name){

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
						// outBox1
						var $wrap = $(obj).parent().parent();
						// 첨부파일 삭제
						$(obj).parent().remove();

						// 첨부파일 다시 그림
						var str = '';
						str += '<div class="custom-file">';
						str += '	<input type="file" class="form-control custom-file-input" name="upload" accept=".gif, .jpg, .jpeg, .png"/>';
						str += '	<label class="custom-file-label">선택된 파일 없음</label>';
						str += '</div>';

						$wrap.html(str);
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
				<form id="img_upload_form" enctype="multipart/form-data" method="post" style="display:none;">
					<input type="file" id="img_file" multiple="multiple" name="imgfile[]" accept="image/*">
				</form>
				<form role="form" id="aform" method="post" action="/admin/popup/updatePopupMgt.do" enctype="multipart/form-data">
					<input type="hidden" name="popup_seq" value="${requestScope.param.popup_seq}">
					<input type="hidden" name="sch_sys_code" value="${requestScope.param.sch_sys_code}">
					<input type="hidden" name="sch_popup_se_code" value="${requestScope.param.sch_popup_se_code}">
					<input type="hidden" name="sch_disp_begin_de" value="${requestScope.param.sch_disp_begin_de}">
					<input type="hidden" name="sch_disp_end_de" value="${requestScope.param.sch_disp_end_de}">
					<input type="hidden" name="sch_text" value="${requestScope.param.sch_text}">
					<input type="hidden" name="currentPage" value="${requestScope.param.currentPage}">
					<input type="hidden" name="image_doc_id" value="${resultMap.IMAGE_DOC_ID}">
					<input type="hidden" name="file_id">
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
											<th>시스템</th>
											<td>
												${resultMap.SYS_CODE_NM}
											</td>
											<th class="required_field">팝업</th>
											<td>
												<select name="popup_se_code" class="form-control input-sm">
													${CacheCommboUtil:getComboStr(UPCODE_POPUP_SE, 'CODE', 'CODE_NM', resultMap.POPUP_SE_CODE, 'C')}
												</select>
											</td>
										</tr>
										<tr>
											<th class="required_field">제목</th>
											<td colspan="3">
												<input type="text" class="form-control" name="sj" maxlength="100" value="${resultMap.SJ}">
											</td>
										</tr>
										<tr>
											<th>Link URL</th>
											<td colspan="3">
												<input type="text" class="form-control" name="link_url" maxlength="100" value="${resultMap.LINK_URL}">
											</td>
										</tr>
										<tr>
											<th>팝업 사용여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="use_yn" id="use_yn_y" value="Y" <c:if test="${resultMap.USE_YN eq 'Y'}">checked="checked"</c:if> />
													<label class="form-check-label" for="use_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="use_yn" id="use_yn_n" value="N" <c:if test="${resultMap.USE_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="use_yn_n" style="margin-right:10px;">아니오</label>
												</div>
											</td>
											<th>모바일 팝업 사용여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="mobile_use_yn" id="mobile_use_yn_y" value="Y" <c:if test="${resultMap.MOBILE_USE_YN eq 'Y'}">checked="checked"</c:if> />
													<label class="form-check-label" for="mobile_use_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="mobile_use_yn" id="mobile_use_yn_n" value="N" <c:if test="${resultMap.MOBILE_USE_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="mobile_use_yn_n" style="margin-right:10px;">아니오</label>
												</div>
											</td>
										</tr>
										<tr>
											<th>가로 사이즈</th>
											<td>
												<div>
													<input type="text" class="form-control numeric" name="width_size" maxlength="4" value="${resultMap.WIDTH_SIZE}">
												</div>
											</td>
											<th>세로 사이즈</th>
											<td>
												<div>
													<input type="text" class="form-control numeric" name="vrticl_size" maxlength="4" value="${resultMap.VRTICL_SIZE}">
												</div>
											</td>
										</tr>
										<tr>
											<th>팝업 가로 위치</th>
											<td>
												<div>
													<input type="text" class="form-control numeric" name="crdnt_x" maxlength="4" value="${resultMap.CRDNT_X}">
												</div>
											</td>
											<th>팝업 세로 위치</th>
											<td>
												<div>
													<input type="text" class="form-control numeric" name="crdnt_y" maxlength="4" value="${resultMap.CRDNT_Y}">
												</div>
											</td>
										</tr>
										<tr>
											<th class="required_field">전시 시작일</th>
											<td class="datetimepicker-td">
												<div>
													<input type="text" class="form-control" name="disp_begin_de" maxlength="6" value="${resultMap.DISP_BEGIN_DE}">
												</div>
											</td>
											<th class="required_field">전시 종료일</th>
											<td class="datetimepicker-td">
												<div>
													<input type="text" class="form-control" name="disp_end_de" maxlength="6" value="${resultMap.DISP_END_DE}">
												</div>
											</td>
										</tr>
										<tr>
											<th>내용</th>
											<td colspan="3">
												<textarea class="form-control" rows="10" id="cn" name="cn" onkeyup="cfLengthCheck('내용은', this, 4000);">${resultMap.CN}</textarea>
											</td>
										</tr>
										<tr>
											<th>팝업 이미지</th>
											<td colspan="3">
												<div id="attach_file">
												<c:forEach var="item" items="${popupImgFileList}" varStatus="status">
													<div>
														<a href="#" onclick="fnDownload('${item.fileId}'); return false;">
															<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(item.fileExtNm)}.gif" width="16" height="16" class="img_file v_img">
															${item.fileNm} (${item.fileSize / 1000.0}) KB
															<a href="#" onclick="fnFileDel(this, '${item.fileId}'); return false;">[삭제]</a>
														</a>
														<br>
													</div>
												</c:forEach>
												<c:if test="${fn:length(popupImgFileList) == 0}">
													<div class="custom-file">
														<input type="file" class="form-control custom-file-input" name="upload" accept=".gif, .jpg, .jpeg, .png">
														<label class="custom-file-label">선택된 파일 없음</label>
													</div>
												</c:if>
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
