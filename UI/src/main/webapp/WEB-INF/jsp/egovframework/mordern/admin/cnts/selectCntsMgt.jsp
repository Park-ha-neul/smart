<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
	//<![CDATA[

		// 목록
		function fnGoList(){
			$("#aform").attr({action:"/admin/cnts/"+fnSysMappingCode()+"selectPageListCntsMgt.do", method:'post'}).submit();
		}
		// 콘텐츠 수정폼 이동
		function fnGoUpdateForm(user_no){
			$("#aform").attr({action:"/admin/cnts/"+fnSysMappingCode()+"updateFormCntsMgt.do", method:'post'}).submit();
			$("#aform").submit();
		}
		// 사용자삭제
		function fnGoDelete(user_no){
			if(confirm("삭제하시겠습니까?")){
				$("#aform").attr({action:"/admin/cnts/"+fnSysMappingCode()+"deleteCntsMgt.do", method:'post'}).submit();
			}
		}
		// 첨부파일 다운로드
		function fnDownload(file_id){
			$('[name=file_id]').val(file_id);
			$('#aform').attr({'action' : '/common/file/FileDown.do'}).submit();
		}
	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/cnts/updateFormCntsMgt.do">
					<input type="hidden" id="cntnts_seq" name="cntnts_seq" value="${resultMap.CNTNTS_SEQ}" />
					<input type="hidden" id="cntnts_id" name="cntnts_id" value="${resultMap.CNTNTS_ID}" />
					<input type="hidden" name="doc_id" value="${resultMap.DOC_ID}" />
					<input type="hidden" name="image_doc_id" value="${resultMap.IMAGE_DOC_ID}" />
					<input type="hidden" name="sch_text" value="${requestScope.param.sch_text}" />
					<input type="hidden" name="sch_type" value="${requestScope.param.sch_type}" />
					<input type="hidden" name="sch_sys_code" value="${requestScope.param.sch_sys_code}" />
					<input type="hidden" name="currentPage" value="${requestScope.param.currentPage}" />
					<input type="hidden" name="last_ver" value="${resultMap.LAST_VER}"/>
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
											<th>버전 정보</th>
											<td>
												${resultMap.LAST_VER}
											</td>
											<th>콘텐츠 아이디</th>
											<td>
												${resultMap.CNTNTS_ID}
											</td>
										</tr>
										<tr>
											<th>제목</th>
											<td>
												${resultMap.SJ}
											</td>
											<th>시스템</th>
											<td>
												${resultMap.SYS_NM }
											</td>
										</tr>
										<tr>
											<th>등록 일시</th>
											<td>
												${resultMap.REGIST_DT}
											</td>
											<th>수정 일시</th>
											<td>
												${resultMap.UPDT_DT}
											</td>
										</tr>
										<tr>
											<th>전시여부</th>
											<td>
												${resultMap.DISP_YN_NM}
											</td>
											<th>삭제여부</th>
											<td>
												${resultMap.DELETE_YN_NM}
											</td>
										</tr>
										<tr>
											<th>조회수</th>
											<td>
												${resultMap.HIT_CNT}
											</td>
											<th>키워드</th>
											<td>
												${resultMap.KWRD}
											</td>
										</tr>
										<tr>
											<th>메뉴명</th>
											<td colspan="3">
												${resultMap.MENU_LIST}
											</td>
										</tr>
										<tr>
											<th>내용</th>
											<td colspan="3">
												${resultMap.CN}
											</td>
										</tr>
										<tr>
											<th>텍스트 내용</th>
											<td colspan="3">
												${resultMap.TEXT_CN}
											</td>
										</tr>
										<tr>
											<th>스크립트</th>
											<td colspan="3">
												<textarea class="form-control" rows="5" placeholder="스크립트">${resultMap.SCRIPT}</textarea>
											</td>
										</tr>
										<tr>
											<th>첨부파일</th>
											<td colspan="3">
										<c:if test="${fn:length(fileList) > 0}">
											<c:forEach var="item" items="${fileList}" varStatus="status">
												<div>
													<a href="#" onclick="fnDownload('${item.fileId}'); return false;">
														<img src="/common/images/file_ext_ico/attach_${fn:toLowerCase(item.fileExtNm)}.gif" width="16" height="16" class="attach_file">
														${item.fileNm} (${item.fileSize / 1000.0}) KB
													</a>
												</div>
											</c:forEach>
										</c:if>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnGoList(); return false;">목록</button>
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnGoUpdateForm(); return false;">수정</button>
									<button type="button" class="btn bg-gradient-danger" onclick="fnGoDelete(); return false;">삭제</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
