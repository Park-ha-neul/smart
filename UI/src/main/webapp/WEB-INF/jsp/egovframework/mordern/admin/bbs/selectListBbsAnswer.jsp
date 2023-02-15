<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<script src="/common/js/dragAndDrop.js"></script>
<script>
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
</script>
<form role="form" id="answerForm" method="post" enctype="multipart/form-data">
	<input type="hidden" name="answer_seq"		value=""/>
	<input type="hidden" name="table_no" value=""/>
	<input type="hidden" name="bbs_seq"		value="${resultMap.BBS_SEQ}"/>
	<input type="hidden" name="sys_code"		value="${resultMap.SYS_CODE}" />
	<input type="hidden" name="ans_atch_doc_id" value="" />
	<input type="hidden" name="ans_image_doc_id" value="" />
	<input type="hidden" name="bbs_code"		value="${resultMap.BBS_CODE}"/>

	<!-- 목록 페이지 검색 조건 -->
	<input type="hidden" name="sch_sys_code"		value="${requestScope.param.sch_sys_code}" />
	<input type="hidden" name="sch_bbs_code"		value="${requestScope.param.sch_bbs_code}" />
	<input type="hidden" name="sch_type"	value="${requestScope.param.sch_type}" />
	<input type="hidden" name="sch_text"	value="${requestScope.param.sch_text}" />
	<input type="hidden" name="sch_currentPage"		value="${requestScope.param.sch_currentPage}"/>

	<input type="hidden" name="is_answer" value="Y"/>
	<input type="hidden" name="ANSWER_YN" value="${resultMap.ANSWER_YN}"/>
	<input type="hidden" name="BBS_NM" value="${resultMap.BBS_NM}"/>

<%-- 관리자 답변여부가 Y가 아니면 답변등록폼을 노출하지 않는다. --%>
<c:if test="${'Y' eq resultMap.MNGR_ANSWER_YN }">
	<div class="card card-info card-outline">
		<div class="card-header">
			<h3 class="card-title">관리자 답변</h3>
		</div>
		<div class="card-body">
			<div id="imageBox"></div>

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
							<th class="required_field">내용</th>
							<td colspan="2">
								<textarea class="form-control" rows="7" id="cn" name="cn" style="resize: vertical;"></textarea>
							</td>
						</tr>
					<c:if test="${'Y' == resultMap.ANSWER_ATCH_FILE_YN }">
						<tr>
							<th>첨부파일</th>
							<td colspan="2">
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
					</c:if>
					</tbody>
				</table>
			</div>
		</div>
		<div id="imageBox" style="margin-left:1.25em;">
			<c:if test="${'Y' == resultMap.ANSWER_EDITR_YN}">
				<button type="button" class="btn btn-info" onclick="fnGoPreview('web'); return false;">웹 사이즈 미리보기</button>
				<button type="button" class="btn btn-info" onclick="fnGoPreview('mobile'); return false;">모바일 사이즈 미리보기</button>
				<p style="color:red">* 미리보기의 내용이외의 세부항목은 샘플이므로 실제와 다를 수 있습니다. 에디터의 내용부분 스타일만 참고하시기 바랍니다.</p>
			</c:if>
		</div>
		<div class="card-footer">
			<div class="form-row justify-content-end">
				<div class="form-inline">
					<button type="button" class="btn bg-gradient-success" onclick="fnAnswerInsert(); return false;">답변등록</button>
				</div>
			</div>
		</div>
	</div>
</c:if>

<%-- 답변리스트 --%>
<c:if test="${fn:length(resultAnsList) > 0}">
	<div class="card card-info card-outline">
		<div class="card-header">
			<h3 class="card-title">답변 목록</h3>
		</div>
		<div class="card-body">
			<div id="imageBox"></div>
				<c:forEach var="item" items="${resultAnsList}" varStatus="status">
					<div class="table-responsive" style="margin-bottom: 10;">
						<table class="table">
							<colgroup>
								<col style="width:5%;">
								<col style="width:10%;">
								<col style="width:40%;">
								<col style="width:10%;">
								<col style="width:30%;">
								<col style="width:10%;">
							</colgroup>
							<tbody>
								<tr>
									<th>No</th>
									<th>작성자</th>
									<td>${item.USER_NM}</td>
									<th>등록일</th>
									<td colspan="2">${item.REGIST_DT}</td>
								</tr>
								<tr>
									<td rowspan="2">${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}</td>
									<th>내용</th>
										<td colspan="3">
									<div style="height:200px;overflow-y: scroll;">
											<c:out escapeXml="true" value="${item.CN}" />
									</div>
										</td>
									<td rowspan="2">
										<div class="form-row justify-content-center">
											<div class="form-inline">
												<button type="button" class="btn bg-gradient-success" onclick="fnAnsUpdateForm('${item.ANSWER_SEQ}', '${pageNavigationVo.totalCount - (pageNavigationVo.currentPage-1) * pageNavigationVo.rowPerPage - status.index}'); return false;" style="margin-bottom: 5px;">수정</button>
											</div>
										</div>
										<div class="form-row justify-content-center">
											<div class="form-inline">
												<button type="button" class="btn bg-gradient-danger mt-1" onclick="fnAnswerDelete('${item.ANSWER_SEQ}', '${item.IMAGE_DOC_ID}', '${item.DOC_ID}'); return false;">삭제</button>
											</div>
<!-- 										</div> -->
									</td>
								</tr>
								<tr>
									<th>첨부파일</th>
									<td colspan="3">
										<c:if test="${fn:length(item.ansFileList) > 0}">
											<c:forEach var="file" items="${item.ansFileList}" varStatus="status">
												<div>
													<a href="#" onclick="fnDownload('${file.fileId}'); return false;">
														<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(file.fileExtNm)}.gif" width="16" height="16" class="v_img ans_attach_file">
														${file.fileNm} (${file.fileSize / 1000.0}) KB
													</a>
													<br>
												</div>
											</c:forEach>
										</c:if>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</c:forEach>
		</div>
		<div class="card-footer">
			<div class="page_box">
				${navigationBar}
			</div>
		</div>
	</div>
</c:if>
</form>
<form id="img_upload_form" enctype="multipart/form-data" method="post" style="display:none;">
	<input type='file' id="img_file" multiple="multiple" name='imgfile[]' accept="image/*">
</form>
