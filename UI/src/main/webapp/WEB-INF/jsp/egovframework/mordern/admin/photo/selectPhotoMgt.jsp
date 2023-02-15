<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[

	$(function(){
		$('.v_img').on({
			// 이미지가 없어서 error 날시
			'error' : function(){
				$(this).attr('src', '<%=imgRoot %>common/no-image.png');
				$(this).css('width', '150px');
			}
		});
	});

	function fnList(){
		$("#aform").attr({action:"/admin/photo/"+fnSysMappingCode()+"selectPageListPhotoMgt.do", method:'post'}).submit();
	}

	function fnDelete(qna_seq){
		if(confirm("삭제하시겠습니까?")){
			$("#aform").attr({action:"/admin/photo/"+fnSysMappingCode()+"deletePhotoMgt.do", method:'post'}).submit();
		}
	}

	function fnUpdateForm(){
		$("#aform").attr({action:"/admin/photo/"+fnSysMappingCode()+"updateFormPhotoMgt.do", method:'post'}).submit();
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/photo/updateFormPhotoMgt.do" enctype="multipart/form-data">
					<input type="hidden" name="photo_seq"		value="${requestScope.param.photo_seq}"/>
					<input type="hidden" name="sch_sys_code"	value="${requestScope.param.sch_sys_code}" />
					<input type="hidden" name="sch_start_photo_potogrf_de"	value="${requestScope.param.sch_start_photo_potogrf_de}" />
					<input type="hidden" name="sch_end_photo_potogrf_de"	value="${requestScope.param.sch_end_photo_potogrf_de}" />
					<input type="hidden" name="sch_text"	value="${requestScope.param.sch_text}" />
					<input type="hidden" name="currentPage"		value="${requestScope.param.currentPage}"/>
					<input type="hidden" name="image_doc_id"	value="${resultMap.IMAGE_DOC_ID}"/>
					<!--페이지 당 목록 수 조건-->
					<input type="hidden" name="sch_row_per_page" value="${requestScope.param.sch_row_per_page}"/>

					<div class="card card-info card-outline">
						<div class="card-body">
								<div class="table-responsive">
									<table class="table">
										<colgroup>
											<col style="width:15%;">
											<col style="width:35%;">
											<col style="width:15%;">
											<col style="width:35%;">
										</colgroup>
										<tbody>
											<tr class="projects td">
												<th style="word-break: keep-all;">시스템</th>
												<td>
													${resultMap.SYS_NM}
												</td>
												<th>제목</th>
												<td>
													${resultMap.SJ}
												</td>
											</tr>
											<tr class="projects td">
												<th>촬영일</th>
												<td>
													${resultMap.PHOTO_POTOGRF_DE}
												</td>
												<th>등록일</th>
												<td>
													${resultMap.REGIST_YMD}
												</td>
											</tr>
											<c:if test="${fn:length(photoImgFileList) > 0}">
												<c:forEach var="item" items="${photoImgFileList}" varStatus="status">
													<tr class="projects td">
													<c:if test="${status.index == 0}">
														<th rowspan="${fn:length(photoImgFileList)}">사진</th>
													</c:if>
														<td style="height:200px;">
																<div class="media">
																	<div class="media-left mr-2">
																		<img src="${siteDdoaminCms}${item.FILE_RLTV_PATH}${item.FILE_ID}.${item.FILE_EXT_NM}" style="width: 250px;height: auto;" class="v_img">
																	</div>
																	<div class="media-body mr-2" style="margin-left: 20px;">
																		<div class="clearfix" style="width: 150px;">
																			<p><b>캡션 :</b> ${item.CAPTION}</p>
																			<p><b>키워드 :</b> ${item.KWRD}</p>
																			<p><b>사이즈 :</b> ${item.WIDTH} X ${item.HEIGHT}</p>
																		</div>
																	</div>
																</div>
																</td>
																<td colspan="2">
																<div style="height:200px;overflow-y: scroll;">
																	${item.META_INFO}
																</div>
														</td>
													</tr>
												</c:forEach>
											</c:if>
											<tr class="projects td">
												<th>비고</th>
												<td colspan="3">
													${resultMap.RM}
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
