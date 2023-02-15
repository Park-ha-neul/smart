<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="SysUtil" uri="/WEB-INF/tlds/SysUtil.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	// 이미지가 없어서 error 날시
	function fnImgError(img){
		img.src="<%=imgRoot%>common/no-image.png";
	}

	//목록
	function fnList(){
		$("#aform").attr({action:"/admin/banners/"+fnSysMappingCode()+"selectPageListBannersMgt.do", method:'post'}).submit();
	}

	//삭제
	function fnDelete(){
		if(confirm("삭제하시겠습니까?")){
			$("#aform").attr({action:"/admin/banners/"+fnSysMappingCode()+"deleteBannersMgt.do", method:'post'}).submit();
		}
	}

	//수정
	function fnUpdateForm(){
		$("#aform").attr({action:"/admin/banners/"+fnSysMappingCode()+"updateFormBannersMgt.do", method:'post'}).submit();
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/banners/updateFormBannersMgt.do" enctype="multipart/form-data">
					<input type="hidden" name="banner_seq" value="${requestScope.param.banner_seq}"/>
					<input type="hidden" name="sch_sys_code" value="${requestScope.param.sch_sys_code}"/>
					<input type="hidden" name="sch_zone_code" value="${requestScope.param.sch_zone_code}"/>
 					<input type="hidden" name="sch_type" value="${requestScope.param.sch_type}"/>
					<input type="hidden" name="sch_text" value="${requestScope.param.sch_text}"/>
					<input type="hidden" name="currentPage"	value="${requestScope.param.currentPage}"/>
					<input type="hidden" name="web_image_doc_id" value="${resultMap.WEB_IMAGE_DOC_ID}"/>
					<input type="hidden" name="mobile_image_doc_id" value="${resultMap.MOBILE_IMAGE_DOC_ID}"/>

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
												${resultMap.SYS_NM}
											</td>
											<th>배너구역</th>
											<td>
												${resultMap.ZONE_NM}
											</td>
										</tr>
										<tr>
											<th>배너 명</th>
											<td colspan="3">
												${resultMap.BANNER_NM}
											</td>
										</tr>
										<tr>
											<th>전시여부</th>
											<td>
												${resultMap.DISP_YN_NM}
											</td>
											<th>속성1</th>
											<td>
												${resultMap.ATTRB_1}
											</td>
										</tr>
										<tr>
											<th>속성2</th>
											<td>
												${resultMap.ATTRB_2}
											</td>
											<th>속성3</th>
											<td>
												${resultMap.ATTRB_3}
											</td>
										</tr>
										<tr>
											<th>등록자</th>
											<td>
												${resultMap.REGISTER_NM}
											</td>
											<th>등록일</th>
											<td>
												${resultMap.REGIST_YMD}
											</td>
										</tr>
										<tr>
											<th>수정자</th>
											<td>
												${resultMap.UPDUSR_NM}
											</td>
											<th>수정일</th>
											<td>
												${resultMap.UPDT_YMD}
											</td>
										</tr>
										<c:if test="${fn:length(webImageList) > 0}">
											<tr>
												<th rowspan="${fn:length(webImageList)}">웹 배너 이미지</th>
												<c:forEach var="item" items="${webImageList}" varStatus="status">
													<td colspan="3">
															<table class="table text-nowrap" style="margin:5px">
																<colgroup>
																	<col style="width: 5px;">
																	<col style="width: 38%;">
																	<col style="width: 12%;">
																	<col style="width: 12%;">
																	<col style="width: 12%;">
																	<col style="width: 12%;">
																	<col style="width: 12%;">
																</colgroup>
																<tr>
																	<th class="text-center">No</th>
																	<th class="text-center">이미지</th>
																	<th class="text-center required_field">정렬순서</th>
																	<th class="text-center">link url</th>
																	<th class="text-center">속성1</th>
																	<th class="text-center">속성2</th>
																	<th class="text-center">속성3</th>
																</tr>
																<tr>
																	<td class="text-center">${status.index+1}</td>
																	<td class="text-center"><img src="${item.fileRltvPath}${item.fileId}.${item.fileExtNm}" style="height:98px" onError="fnImgError(this)" />
																		<br><b>${item.fileNm}</b> (${SysUtil:byteCalculation(item.fileSize)})
																	</td>
																	<td class="text-center">${item.srtOrd}</td>
																	<td class="text-center">${item.linkUrl}</td>
																	<td class="text-center">${item.attrb1}</td>
																	<td class="text-center">${item.attrb2}</td>
																	<td class="text-center">${item.attrb3}</td>
																</tr>
															</table>
														</td>
													</tr>
												</c:forEach>
											</c:if>
											<c:if test="${fn:length(mobileImageList) > 0}">
												<tr>
													<th rowspan="${fn:length(mobileImageList)}">모바일 배너 이미지</th>
													<c:forEach var="item" items="${mobileImageList}" varStatus="status">
														<td colspan="3" >
															<table class="table text-nowrap" style="margin:5px">
																<colgroup>
																	<col style="width:5px;">
																	<col style="width:38%;">
																	<col style="width:12%;">
																	<col style="width:12%;">
																	<col style="width:12%;">
																	<col style="width:12%;">
																	<col style="width:12%;">
																</colgroup>
																<tr>
																<th class="text-center">No</th>
																<th class="text-center">이미지</th>
																<th class="text-center required_field">정렬순서</th>
																<th class="text-center">url</th>
																<th class="text-center">속성1</th>
																<th class="text-center">속성2</th>
																<th class="text-center">속성3</th>
															</tr>
															<tr>
																<td class="text-center">${status.index+1}</td>
																<td class="text-center"><img src="${item.fileRltvPath}${item.fileId}.${item.fileExtNm}" style="height:120px" onError="fnImgError(this)" />
																<br><b>${item.fileNm}</b> (${SysUtil:byteCalculation(item.fileSize)})
																</td>
																<td class="text-center">${item.srtOrd}</td>
																<td class="text-center">${item.linkUrl}</td>
																<td class="text-center">${item.attrb1}</td>
																<td class="text-center">${item.attrb2}</td>
																<td class="text-center">${item.attrb3}</td>
															</tr>
														</table>
													</td>
												</tr>
											</c:forEach>
										</c:if>
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
