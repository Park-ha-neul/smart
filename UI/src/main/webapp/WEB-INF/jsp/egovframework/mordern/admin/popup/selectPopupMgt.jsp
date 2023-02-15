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
		$("#aform").attr({action:"/admin/popup/"+fnSysMappingCode()+"selectPageListPopupMgt.do", method:'post'}).submit();
	}

	function fnDelete(qna_seq){
		if(confirm("삭제하시겠습니까?")){
			$("#aform").attr({action:"/admin/popup/"+fnSysMappingCode()+"deletePopupMgt.do", method:'post'}).submit();
		}
	}

	function fnUpdateForm(){
		$("#aform").attr({action:"/admin/popup/"+fnSysMappingCode()+"updateFormPopupMgt.do", method:'post'}).submit();
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/popup/updateFormPopupMgt.do" enctype="multipart/form-data">
					<input type="hidden" name="popup_seq" value="${requestScope.param.popup_seq}"/>
					<input type="hidden" name="sch_sys_code" value="${requestScope.param.sch_sys_code}" />
					<input type="hidden" name="sch_popup_se_code" value="${requestScope.param.sch_popup_se_code}" />
					<input type="hidden" name="sch_disp_begin_de" value="${requestScope.param.sch_disp_begin_de}" />
					<input type="hidden" name="sch_disp_end_de" value="${requestScope.param.sch_disp_end_de}" />
					<input type="hidden" name="sch_text" value="${requestScope.param.sch_text}" />
					<input type="hidden" name="currentPage" value="${requestScope.param.currentPage}"/>
					<input type="hidden" name="image_doc_id" value="${resultMap.IMAGE_DOC_ID}"/>
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
											<th>팝업</th>
											<td>
												${resultMap.POPUP_SE_NM}
											</td>
										</tr>
										<tr>
											<th>제목</th>
											<td colspan="3">
												${resultMap.SJ}
											</td>
										</tr>
										<tr>
											<th>LINK URL</th>
											<td colspan="3">
												${resultMap.LINK_URL}
											</td>
										</tr>
										<tr>
											<th>팝업 사용여부</th>
											<td>
												${resultMap.USE_YN_NM}
											</td>
											<th>모바일 사용여부</th>
											<td>
												${resultMap.MOBILE_USE_YN_NM}
											</td>
										</tr>
										<tr>
											<th>가로 사이즈</th>
											<td>
												<fmt:formatNumber value="${resultMap.WIDTH_SIZE}" pattern="#,###" />
											</td>
											<th>세로 사이즈</th>
											<td>
												<fmt:formatNumber value="${resultMap.VRTICL_SIZE}" pattern="#,###" />
											</td>
										</tr>
										<tr>
											<th>팝업 가로 위치</th>
											<td>
												<fmt:formatNumber value="${resultMap.CRDNT_X}" pattern="#,###" />
											</td>
											<th>팝업 세로 위치</th>
											<td>
												<fmt:formatNumber value="${resultMap.CRDNT_Y}" pattern="#,###" />
											</td>
										</tr>
										<tr>
											<th>전시 시작일</th>
											<td>
												${resultMap.DISP_BEGIN_DE}
											</td>
											<th>전시 종료일</th>
											<td>
												${resultMap.DISP_END_DE}
											</td>
										</tr>
										<tr>
											<th>내용</th>
											<td colspan="3">
												${resultMap.CN}
											</td>
										</tr>
										<tr>
											<th>팝업 이미지</th>
											<td colspan="3">
											<c:forEach var="item" items="${popupImgFileList}" varStatus="status">
												<img src = "${item.fileRltvPath}${item.fileId}.${item.fileExtNm}" class="v_img" />
											</c:forEach>
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
