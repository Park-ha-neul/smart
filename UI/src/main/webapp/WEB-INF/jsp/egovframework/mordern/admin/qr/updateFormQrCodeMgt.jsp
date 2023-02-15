<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript" src="/common/ckeditor/ckeditor.js" charset="UTF-8" ></script>
<script type="text/javascript" src="/common/ckeditor/adapters/jquery.js" charset="UTF-8" ></script>

<script type="text/javascript">
	//<![CDATA[
	$(function(){
		$('.v_img').on({
			// 이미지가 없어서 error 날시
			'error' : function(){
				$(this).attr('src', '<%=imgRoot %>common/no-image.png');
			}
		});

		//확장자 체크
		$('input[name=upload]').on({'change' : function(){
			if(!f_CheckAcceptFileExt('upload', "JPG|JPEG|GIF|PNG")){
				alert('배경 이미지는 (JPG,JPEG,GIF,PNG)파일만 업로드 가능합니다. ');
				var agent = navigator.userAgent.toLowerCase();
				if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
					$(this).replaceWith( $(this).clone(true) );
				}else{
					$(this).val("");
				}
				return;
			}
		}
		});
	});

		function fnDetail(){
			$("#aform").attr({action:"/admin/qr/"+fnSysMappingCode()+"selectQrCodeMgt.do", method:'post'}).submit();
		}

		function fnUpdate(){
			if($("[name=sj]").val() == ""){
				alert("제목을 입력해 주세요.");
				$("[name=sj]").focus();
				return;
			}
			if(CKEDITOR.instances['code_cn'].getData().length < 1){
				alert("코드 내용을 입력해 주세요.");
				$("[name=code_cn]").focus();
				return;
			}
			if(confirm("수정하시겠습니까?")){
				$("#aform").attr({action:"/admin/qr/"+fnSysMappingCode()+"updateQrCodeMgt.do", method:'post'}).submit();
			}
		}

		//파일 다운로드
		function fnDownload(fileId){
			$("[name=file_id]").val(fileId)
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

							//배경이미지 삭제후 파일 박스 그리기
							$(obj).parent().remove();
							var html='<input type="file" class="form-control custom-file-input" name="upload" accept=".jpeg, .jpg, .png"/>';
							$('.imgBoxWrap').html(html);
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
				<form role="form" id="aform" method="post" action="/admin/qr/updateQrCodeMgt.do" enctype="multipart/form-data">
					<input type="hidden" name="qr_seq"						value="${requestScope.param.qr_seq}"/>
					<input type="hidden" name="sch_sys_code"			value="${requestScope.param.sch_sys_code}"/>
					<input type="hidden" name="sch_text"					value="${requestScope.param.sch_text}"/>
					<input type="hidden" name="currentPage"				value="${requestScope.param.currentPage}"/>
					<input type="hidden" name="image_doc_id"			value="${resultMap.IMAGE_DOC_ID}" />
					<input type="hidden" name="qr_image_doc_id" 	value="${resultMap.QR_IMAGE_DOC_ID}" />
					<input type="hidden" name="file_id" />

					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width:15%;">
										<col style="width:*;">
									</colgroup>
									<tbody>
										<tr>
											<th>시스템</th>
											<td>
												${resultMap.SYS_CODE_NM}
											</td>
										</tr>
										<tr>
											<th class="required_field">제목</th>
											<td>
												<input type="text" class="form-control" id="sj" name="sj" maxlength="100" value="${resultMap.SJ}" />
											</td>
										</tr>
										<tr>
											<th>배경 이미지</th>
											<td>
												<div class="imgBoxWrap">
													<c:forEach var="item" items="${imageFileList}" varStatus="status">
														<div>
															<a href="#" onclick="fnDownload('${item.fileId}'); return false;">
																<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(item.fileExtNm)}.gif" width="16" height="16" class="img_file v_img">
																${item.fileNm} (${item.fileSize / 1000.0}) KB
																<a href="#" onclick="fnFileDel(this, '${item.fileId}'); return false;">[삭제]</a>
															</a>
															<br/>
														</div>
													</c:forEach>
													<c:if test="${fn:length(imageFileList) == 0}">
														<div class="custom-file">
															<input type="file" class="form-control custom-file-input" name="upload" accept=".gif, .jpg, .jpeg, .png"/>
															<label class="custom-file-label">선택된 파일 없음</label>
														</div>
													</c:if>
												</div>
											</td>
										</tr>
										<tr>
											<th class="required_field">코드 내용</th>
											<td>
												<textarea class="form-control" rows="10" id="code_cn" name="code_cn" onkeyup="cfLengthCheck('내용은', this, 4000);">${resultMap.CODE_CN}</textarea>
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
	  	CKEDITOR.replace('code_cn',{
	  		filebrowserUploadUrl: '${pageContext.request.contextPath}/photoServerUpload.do'
	  	});
	  });
	  //객체 생성
	  var ajaxImage = {};
	  // ckeditor textarea id
	  ajaxImage["id"] = "code_cn";
	  // 업로드 될 디렉토리
	  ajaxImage["uploadDir"] = "upload";
	  // 한 번에 업로드할 수 있는 이미지 최대 수
	  ajaxImage["imgMaxN"] = 10;
	  // 허용할 이미지 하나의 최대 크기(MB)
	  ajaxImage["imgMaxSize"] = 10;
</script>
