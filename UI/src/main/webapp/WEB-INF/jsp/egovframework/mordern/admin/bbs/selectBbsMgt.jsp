<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<%@ taglib prefix="Util" uri="/WEB-INF/tlds/Util.tld"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript" src="/common/ckeditor/ckeditor.js" charset="UTF-8" ></script>
<script type="text/javascript" src="/common/ckeditor/adapters/jquery.js" charset="UTF-8" ></script>
<link rel="stylesheet" type="text/css" media="all" href="/common/css/dragAndDrop.css" />

<script type="text/javascript">
//<![CDATA[
	var sysMappingCode = "${empty requestScope.param.sys_mapping_code ? null : requestScope.param.sys_mapping_code}";
	window.name="main"

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

		fnComboStrFile('.fileBoxWrap', 0, 5);

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

	//목록
	function fnList(){
		$("#aform").attr({action:"/admin/bbs/"+fnSysMappingCode()+"selectPageListBbsMgt.do", method:'post'}).submit();
	}

	//메인 글 삭제
	function fnDelete(){
		if(confirm("삭제하시겠습니까?")){
			$("#aform").attr({action:"/admin/bbs/"+fnSysMappingCode()+"deleteBbsMgt.do", method:'post'}).submit();
		}
	}

	//메인 글 수정
	function fnUpdateForm(){
		$("#aform").attr({action:"/admin/bbs/"+fnSysMappingCode()+"updateFormBbsMgt.do", method:'post'}).submit();
	}

	//답변 등록
	function fnAnswerInsert(){
		if("${requestScope.resultMap.ANSWER_EDITR_YN == 'Y'}"){
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

		if(confirm("등록하시겠습니까?")){
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
				url:"/admin/bbs/"+fnSysMappingCode()+"/insertBbsAnswer.do",
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
					location.reload(true);
				}
			});
		}
	}

	//답변 글 수정폼 이동
	function fnAnsUpdateForm(answerSeq, tableNo){
			$('#answerForm [name=answer_seq]').val(answerSeq);
			$('#answerForm [name=table_no]').val(tableNo);
			$("#answerForm").attr({action:"/admin/bbs/"+fnSysMappingCode()+"updateBbsAnswerForm.do", method:'post'}).submit();
	}

	//답변 글 삭제
	function fnAnswerDelete(answerSeq, ansImageDocId, ansAtchDocId){
		if(confirm("삭제하시겠습니까?")){
			$('#answerForm [name=answer_seq]').val(answerSeq);
			$('#answerForm [name=ans_image_doc_id]').val(ansImageDocId);
			$('#answerForm [name=ans_atch_doc_id]').val(ansAtchDocId);

			$("#answerForm").attr({action:"/admin/bbs/"+fnSysMappingCode()+"deleteBbsAnswer.do", method:'post'}).submit();
		}
	}

	//답변 페이지이동
	function fnGoPage(currentPage){
		$("#currentPage").val(currentPage);
		$("#answerForm").attr({action:"/admin/bbs/"+fnSysMappingCode()+"selectBbsMgt.do", method:'post'}).submit();
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

						//첨부파일을 다 삭제하면 첨부파일 목록태그까지 삭제
						if( $('.ans_attach_file').length == 1){
							$(obj).closest('div').remove();
						}else{
							//첨부파일이 남아 있을경우 삭제
							$(obj).parent().remove();
						}
					}
				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}
			});
		}
	}

	//게시물 이동 팝업
	function fnselectListSysBbs(){
		var popupX = (window.screen.width / 2) - (700 / 2);
		// 만들 팝업창 좌우 크기의 1/2 만큼 보정값으로 빼주었음

		var popupY= (window.screen.height /2) - (800 / 2);
		// 만들 팝업창 상하 크기의 1/2 만큼 보정값으로 빼주었음

		var bbs_seq = $('[name=bbs_seq]').val();
		var sys_code = $('[name=sys_code]').val();
		var bbs_code = $('[name=bbs_code]').val();
		window.open('/admin/bbs/'+fnSysMappingCode()+'selectListSysBbs.do?sys_code=' + sys_code + '&bbs_code=' + bbs_code + '&bbs_seq=' + bbs_seq, '게시판 정보 조회', 'width=500, height=300, left='+ popupX + ', top='+ popupY + ', screenX='+ popupX + ', screenY= '+ popupY);
	}

//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/bbs/updateFormBbsMgt.do" enctype="multipart/form-data">
					<input type="hidden" name="bbs_seq" value="${resultMap.BBS_SEQ}"/>
					<input type="hidden" name="bbs_code" value="${resultMap.BBS_CODE}"/>
					<input type="hidden" name="sys_code" value="${resultMap.SYS_CODE}"/>
					<input type="hidden" name="image_doc_id" value="${resultMap.IMAGE_DOC_ID}" />
					<input type="hidden" name="atch_doc_id" value="${resultMap.DOC_ID}" />
					<input type="hidden" name="thumb_doc_id" value="${resultMap.THUMB_DOC_ID}" />

					<input type="hidden" name="sch_imprtnc_yn" value="${requestScope.param.sch_imprtnc_yn}" />
					<input type="hidden" name="sch_sys_code" value="${requestScope.param.sch_sys_code}" />
					<input type="hidden" name="sch_bbs_code" value="${requestScope.param.sch_bbs_code}" />
					<input type="hidden" name="sch_type" value="${requestScope.param.sch_type}" />
					<input type="hidden" name="sch_text" value="${requestScope.param.sch_text}" />
					<input type="hidden" name="sch_currentPage" value="${requestScope.param.sch_currentPage}"/>

					<input type="hidden" name="ans_atch_doc_id" value="" />
					<input type="hidden" name="ans_image_doc_id" value="" />
					<input type="hidden" name="answer_seq"		value=""/>

					<input type="hidden" name="file_id"/>
					<!--페이지 당 목록 수 조건-->
					<input type="hidden" name="sch_row_per_page" value="${requestScope.param.sch_row_per_page}"/>

					<div class="card card-info card-outline">
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
											<th>시스템</th>
											<td>
												${resultMap.SYS_CODE_NM}
											</td>
											<th>게시판 명</th>
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
												<c:choose>
													<c:when test="${resultMap.REGISTER_CTTPC eq ''}">
													<td></td>
													</c:when>
													<c:when test="${resultMap.REGISTER_CTTPC eq null}">
													<td></td>
													</c:when>
													<c:otherwise>
													<c:if test="${resultMap.REGISTER_CTTPC ne ''}">
													<td>[${resultMap.CTTPC_SE_NM}]
													${resultMap.REGISTER_CTTPC}</td>
												</c:if>
													</c:otherwise>
												</c:choose>
										</tr>
										<tr>
											<th>조회수</th>
											<td>
												${resultMap.HIT_CNT}
											</td>
											<th>등록일자</th>
											<td>
												${resultMap.REGIST_DT}
											</td>
										</tr>
										<tr>
											<th>제목</th>
											<td colspan="3">
												${resultMap.SJ}
											</td>
										</tr>
										<tr>
											<th>내용</th>
											<td colspan="3">
												${Util:getHtml(resultMap.CN)}
											</td>
										</tr>
									<c:if test="${resultMap.EDITR_YN == 'Y'}">
										<tr>
											<th>텍스트 내용</th>
											<td colspan="3">
												${resultMap.TEXT_CN}
											</td>
										</tr>
									</c:if>
										<tr>
											<th>비고</th>
											<td colspan="3">
												${resultMap.RM}
											</td>
										</tr>
									<c:if test="${resultMap.BBS_TY_CODE == '20' || resultMap.BBS_TY_CODE == '30' || resultMap.BBS_TY_CODE == '31'}">
										<tr>
											<th>썸네일 이미지 파일</th>
											<td colspan="3">
												<div class="outBox1">
											<c:if test="${fn:length(thumbImgFileList) > 0}">
												<c:forEach var="item" items="${thumbImgFileList}" varStatus="status">
													<div>
														<a href="#" onclick="fnDownload('${item.fileId}'); return false;">
															<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(item.fileExtNm)}.gif" width="16" height="16" class="v_img thumb_file" />
															${item.fileNm} (${item.fileSize / 1000.0}) KB
															<br />
														</a>
													</div>
												</c:forEach>
											</c:if>
												</div>
											</td>
										</tr>
										<c:if test="${fn:length(thumbImgFileList) > 0}">
										<tr>
											<th>썸네일 이미지</th>
											<td colspan="5">
												<img src="${resultMap.THUMB_PATH_URL}" style="width:250px;" />
											</td>
										</tr>
										</c:if>
									</c:if>
									<c:if test="${resultMap.ATCH_FILE_YN == 'Y'}">
										<tr>
											<th>첨부파일</th>
											<td colspan="5">
												<div class="outBox1">
											<c:if test="${fn:length(atchFileList) > 0}">
												<c:forEach var="item" items="${atchFileList}" varStatus="status">
													<div>
														<a href="#" onclick="fnDownload('${item.fileId}'); return false;">
															<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(item.fileExtNm)}.gif" width="16" height="16" class="v_img attach_file" onerror="this.src='/common/images/file_ext_ico/attach_none.gif';">
															${item.fileNm} (${item.fileSize / 1000.0}) KB
														</a>
														<br>
													</div>
												</c:forEach>
											</c:if>
												</div>
											</td>
										</tr>
									</c:if>
									<c:if test="${resultMap.KWRD_YN == 'Y'}">
										<tr>
											<th>키워드</th>
											<td colspan="3">
												${resultMap.KWRD}
											</td>
										</tr>
									</c:if>
									<c:if test="${resultMap.LINK_URL_YN == 'Y'}">
										<tr>
											<th>링크 URL</th>
											<td colspan="3">
												${resultMap.LINK_URL}
											</td>
										</tr>
									</c:if>
									<c:if test="${resultMap.MVP_URL_YN == 'Y'}">
										<tr>
											<th>동영상 URL</th>
											<td colspan="3">
												${resultMap.MVP_URL}
											</td>
										</tr>
									</c:if>
									<c:if test="${resultMap.IMPRTNC_YN != ''}">
										<tr>
											<th>중요 여부</th>
											<td>
												${resultMap.IMPRTNC_YN_NM}
											</td>
											<th>중요 순서</th>
											<td>
												${resultMap.IMPRTNC_ORDR}
											</td>
										</tr>
									</c:if>
										<tr>
											<th>공개 여부</th>
											<td colspan="3">
												${resultMap.OPEN_YN_NM}
											</td>
										</tr>
										<tr>
											<th>전시 여부</th>
											<td colspan="3">
												${resultMap.DISP_YN_NM}
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
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnUpdateForm(); return false;">수정</button>
									<c:if test="${requestScope.param.ss_author_id eq CODE_AUTHOR_ADMIN}">
										<button type="button" class="btn bg-gradient-success mr-2" onclick="fnselectListSysBbs(); return false;">게시물 이동</button>
									</c:if>
									<button type="button" class="btn bg-gradient-danger" onclick="fnDelete(); return false;">삭제</button>
								</div>
							</div>
						</div>
					</div>
				</form>

				<%-- 답변 여부 혹은 관리자 답변 여부가 Y일 경우에 답변 페이지 include; 중요가부로 등록되어있는경우 관리자가 등록한 경우라서 답변 등록폼을 보여주지 않는다. --%>
				<c:if test="${resultMap.IMPRTNC_YN != 'Y' && (resultMap.MNGR_ANSWER_YN == 'Y' || resultMap.ANSWER_YN == 'Y')}">
					<%@ include file="/WEB-INF/jsp/egovframework/mordern/admin/bbs/selectListBbsAnswer.jsp" %>
				</c:if>
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
