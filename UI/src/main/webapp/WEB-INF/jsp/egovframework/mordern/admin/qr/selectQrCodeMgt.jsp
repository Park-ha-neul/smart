<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Util" uri="/WEB-INF/tlds/Util.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
	//<![CDATA[
		$(function(){
			$('.v_img').on({
				// 이미지가 없어서 error 날시
				'error' : function(){
					$(this).attr('src', '<%=imgRoot %>common/no-image.png');
				}
			});
		});

		function fnList(){
			$("#aform").attr({action:"/admin/qr/"+fnSysMappingCode()+"selectPageListQrCodeMgt.do", method:'post'}).submit();
		}

		function fnDelete(){
			if(confirm("삭제하시겠습니까?")){
				$("#aform").attr({action:"/admin/qr/"+fnSysMappingCode()+"deleteQrCodeMgt.do", method:'post'}).submit();
			}
		}

		function fnUpdateForm(){
			$("#aform").attr({action:"/admin/qr/"+fnSysMappingCode()+"updateFormQrCodeMgt.do", method:'post'}).submit();
		}

		//파일 다운로드
		function fnDownload(fileId){
			$("[name=file_id]").val(fileId)
			$('#aform').attr({'action' : '/common/file/FileDown.do'}).submit();
		}

	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/qr/updateFormQrCodeMgt.do" enctype="multipart/form-data">
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
											<th>작성자</th>
											<td>
												${resultMap.USER_NM}
											</td>
										</tr>
										<tr>
											<th>제목</th>
											<td colspan="3">
												${resultMap.SJ}
											</td>
										</tr>
										<tr>
											<th>배경이미지</th>
											<td colspan="3">
												<c:if test="${fn:length(imageFileList) > 0}">
													<c:forEach var="item" items="${imageFileList}" varStatus="status">
														<div>
															<a href="#" onclick="fnDownload('${item.fileId}'); return false;">
																<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(item.fileExtNm)}.gif" width="16" height="16" class="v_img attach_file">
																${item.fileNm} (${item.fileSize / 1000.0}) KB
															</a><br>
														</div>
													</c:forEach>
												</c:if>
											</td>
										</tr>
										<tr>
											<th>코드 내용</th>
											<td colspan="3">
												${resultMap.CODE_CN}
											</td>
										</tr>
										<tr>
											<th>QR코드 URL</th>
											<td colspan="3">
												${resultMap.QR_CODE_URL}
											</td>
										</tr>
										<tr>
											<th>QR코드</th>
											<td colspan="3">
												<img class="v_img" src="${resultMap.QR_CODE_URL}" />
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
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnDownload('${resultMap.QR_FILE_ID}'); return false;"><i class="fa fa-download"></i> QR코드 다운로드</button>
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnUpdateForm(); return false;">수정</button>
									<button type="button" class="btn bg-gradient-danger" onclick="fnDelete(); return false;">삭제</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
