<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[

	$(function(){
		$('.attachment-img').on({
			// 이미지가 없어서 error 날시
			'error' : function(){
				$(this).attr('src', '/common/images/common/no-image.png');
			}
		});

		// 지점검색 이벤트
		$('[name=sch_text]').on({
			'keyup' : function(e){
				if(e.which == 13){
					fnSearch();
				}
			},
			'keydown' : function(e){
				if(e.which == 13){
					e.preventDefault();
				}
			},
		});

		//li 태그 클릭 시 이벤트 발생
		$('.item').click(function(){

			var $imgPath = $(this).find('.attachment-img');
			var src = $imgPath.attr('src');
			var alt = $imgPath.attr('alt');
			var caption = $(this).parent().find('#caption').val();

			var imgData = "<figure class='image'><img class='v_img' src='"+ src + "' alt='" + alt +"'><figcaption>" + caption + "</figcaption></figure>";

			var oEditor = window.opener.CKEDITOR.instances.cn;

			// Check the active editing mode.
			if ( oEditor.mode == 'wysiwyg' ) {
				// Insert the desired HTML.
				oEditor.insertHtml( imgData );
			} else {
				var originData = oEditor.getData();
				window.opener.CKEDITOR.instances.cn.setData(originData + imgData);
			}
			window.close();
		});

	});

	//페이지이동
	function fnGoPage(currentPage){
		$("#currentPage").val(currentPage);
		$("#aform").attr({action:"/admin/cnts/"+fnSysMappingCode()+"selectPageListPhotoMng.do", method:'post'}).submit();
	}

	//검색
	function fnSearch(){
		$("#currentPage").val("1");
		$("#aform").attr({action:"/admin/cnts/"+fnSysMappingCode()+"selectPageListPhotoMng.do", method:'post'}).submit();
	}

	function f_GoList(){
		window.close();
	}

//]]>
</script>
<div class="content-wrapper no-menu">
	<!-- Content Header (Page header) -->
	<div class="content-header">
		<div class="container-fluid">
			<div class="row mb-2">
				<div class="col-sm-6">
					<h1 class="m-0 text-dark">포토 매니저</h1>
				</div><!-- /.col -->
			</div><!-- /.row -->
		</div><!-- /.container-fluid -->
	</div>
	<div class="content">
		<div class="container-fluid">
			<div class="row">
				<div class="col-lg-12">
					<form role="form" id="aform" method="post" action="/admin/cnts/selectPageListPhotoMng.do">
						<input type="hidden" name="cntnts_seq" />

						<div class="card card-info card-outline">
							<div class="card-header">
								<div class="form-row justify-content-end">
									<div class="form-inline">
										<select name="sch_type" class="form-control form-control-sm mr-1">
											<option value="sch_all" <c:if test="${param.sch_type eq 'sch_all'}" >selected="selected"</c:if> >전체</option>
											<option value="sch_kwrd" <c:if test="${param.sch_type eq 'sch_kwrd'}" >selected="selected"</c:if> >키워드</option>
											<option value="sch_sj" <c:if test="${param.sch_type eq 'sch_sj'}" >selected="selected"</c:if> >제목</option>
											<option value="sch_caption" <c:if test="${param.sch_type eq 'sch_caption'}" >selected="selected"</c:if> >캡션</option>
										</select>
										<div class="input-group input-group-sm">
											<input type="text" class="form-control form-control-sm" name="sch_text" title="검색어를 입력하세요." placeholder="검색어를 입력하세요." value="${requestScope.param.sch_text}" />
											<div class="input-group-append">
												<button type="button" class="btn btn-default btn-outline-dark" onclick="fnSearch(); return false;"><i class="fa fa-search"></i></button>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="card-body">
								<div class="table-responsive">
									<table class="table text-nowrap">
										<colgroup>
											<col width="200px" />
											<col width="*" />
										</colgroup>
										<thead>
											<tr>
												<th class="text-center">이미지</th>
												<th class="text-center">내용</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${resultList.size() == 0}">
													<tr>
														<td class="text-center" colspan="2" ><spring:message code="msg.data.empty" /></td>
													</tr>
												</c:when>
												<c:otherwise>
													<c:forEach var="list" items="${resultList}" varStatus="status">
														<tr>
															<td class="txt_overflow item">
																<img src="${list.FILE_PARTN_COURS}${list.FILE_ID}.${list.FILE_EXTSN_NM}" class="attachment-img w-100" alt="${list.FILE_NM }" />
															</td>
															<td class="txt_overflow">
																<ol class="info">
																	<li><span>제목</span><c:if test="${list.SJ == '' || list.SJ eq null}" >Not Found</c:if>${list.SJ}</li>
																	<li><span>키워드</span><c:if test="${list.KWRD == '' || list.KWRD eq null}">Not Found</c:if>${list.KWRD}</li>
																	<li><span>캡션</span><c:if test="${list.CAPTION == '' || list.CAPTION eq null}">Not Found</c:if>${list.CAPTION}</li>
																	<li><span>사이즈</span>${list.WIDTH} * ${list.HEIGHT}</li>
																	<li><span>파일 이름</span><c:if test="${list.FILE_NM == '' || list.FILE_NM eq null}">Not Found</c:if>${list.FILE_NM}</span>
																</ol>
																<input type="hidden" name="caption" value="${list.CAPTION}" id="caption" />
															</td>
														</tr>
													</c:forEach>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
									<div class="page_box">${navigationBar}</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
