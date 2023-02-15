<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[

	//게시판 관리 목록
	function fnList(){
		$("#aform").attr({action:"/admin/bbsmng/"+fnSysMappingCode()+"selectPageListBbsMng.do", method:'post'}).submit();
	}

	//삭제
	function fnDelete(){
		if(confirm("삭제하시겠습니까?")){
			$("#aform").attr({action:"/admin/bbsmng/"+fnSysMappingCode()+"deleteBbsMng.do", method:'post'}).submit();
		}
	}

	//수정폼 이동
	function fnUpdateForm(){
		$("#aform").attr({action:"/admin/bbsmng/"+fnSysMappingCode()+"updateFormBbsMng.do", method:'post'}).submit();
	}

//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/bbsmng/updateFormBbsMng.do" >
					<input type="hidden" name="bbs_code"		value="${requestScope.param.bbs_code}"/>
					<input type="hidden" name="sch_sys_code"		value="${requestScope.param.sch_sys_code}" />
					<input type="hidden" name="sch_bbs_ty_code"		value="${requestScope.param.sch_bbs_ty_code}" />
					<input type="hidden" name="sch_use_yn"		value="${requestScope.param.sch_use_yn}" />
					<input type="hidden" name="sch_text"	value="${requestScope.param.sch_text}" />
					<input type="hidden" name="currentPage"		value="${requestScope.param.currentPage}"/>
					<input type="hidden" name="menu_id"		value="${requestScope.param.MENU_ID}"/>

					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width:13%;">
										<col style="width:87%;">
									</colgroup>
									<tbody>
										<tr>
											<th>게시판 권한</th>
											<td>
											<c:forEach var="item" items="${resultMap.AUTHOR_LIST}" varStatus="status">
												${item.AUTHOR_NM}<br/>
											</c:forEach>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width:13%;">
										<col style="width:12%;">
										<col style="width:13%;">
										<col style="width:12%;">
										<col style="width:13%;">
										<col style="width:12%;">
										<col style="width:13%;">
										<col style="width:12%;">
									</colgroup>
									<tbody>
										<tr>
											<th>시스템</th>
											<td colspan="3">
												${resultMap.SYS_CODE_NM}
											</td>
											<th>게시판 코드</th>
											<td colspan="3">
												${resultMap.BBS_CODE}
											</td>
										</tr>
										<tr>
											<th>게시판 명</th>
											<td colspan="3">
												${resultMap.BBS_NM}
											</td>
											<th>작성자</th>
											<td colspan="3">
												${resultMap.USER_NM}
											</td>
										</tr>
										<tr>
											<th>게시판 유형</th>
											<td colspan="3">
												${resultMap.BBS_TY_CODE_NM}
											</td>
											<th></th>
											<td colspan="3">
											</td>
										</tr>
										<tr>
											<th>등록 여부</th>
											<td>
												${resultMap.REGIST_YN_NM}
											</td>
											<th>에디터 여부</th>
											<td>
												${resultMap.EDITR_NM}
											</td>
											<th>첨부 파일 여부</th>
											<td>
												${resultMap.ATCH_FILE_NM}
											</td>
											<th>링크 URL 여부</th>
											<td>
												${resultMap.LINK_URL_NM}
											</td>
										</tr>
										<tr>
											<th>키워드 여부</th>
											<td>
												${resultMap.KWRD_NM}
											</td>
											<th>동영상 URL 여부</th>
											<td>
												${resultMap.MVP_URL_NM}
											</td>
											<th>답변 여부</th>
											<td>
												${resultMap.ANSWER_NM}
											</td>
											<th>관리자 답변 여부</th>
											<td>
												${resultMap.MNGR_ANSWER_NM}
											</td>
										</tr>
										<tr>
											<th>답변 에디터 여부</th>
											<td>
												${resultMap.ANSWER_EDITR_NM}
											</td>
											<th>답변 첨부 파일 여부</th>
											<td>
												${resultMap.ANSWER_ATCH_FILE_NM}
											</td>
											<th>콘텐츠 여부</th>
											<td>
												${resultMap.CNTNTS_NM}
											</td>
											<th>콘텐츠 위치</th>
											<td>
												${resultMap.CNTNTS_POSITION_NM}
											</td>
											</tr>
										<tr>
											<th>비밀번호 여부</th>
											<td>
												${resultMap.PASSWORD_NM}
											</td>
											<th>비로그인 여부</th>
											<td>
												${resultMap.NONLOGIN_NM}
											</td>
											<th>캡차 여부</th>
											<td>
												${resultMap.CAPTCHA_NM}
											</td>
											<th>사용여부</th>
											<td>
												${resultMap.USE_YN_NM}
											</td>
										</tr>
										<tr>
										<th>사용 메뉴</th>
											<td colspan="7">
												${resultMap.MENU_LIST}
											</td>
										</tr>
									<c:if test="${resultMap.CNTNTS_YN == 'Y'}">
										<tr>
											<th class="align-middle">콘텐츠 내용</th>
											<td colspan="7">
												${resultMap.CNTNTS_CN}
											</td>
										</tr>
									</c:if>
										<tr>
											<th>비고</th>
											<td colspan="7">
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
								<c:if test="${requestScope.param.showDelBtn == 'Y'}">
									<button type="button" class="btn bg-gradient-danger" onclick="fnDelete(); return false;">삭제</button>
								</c:if>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
