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
			//게시판 유형 라디오 버튼에 css가 적용되는 class 추가
			$('[name=bbs_ty_code]').parent("div").addClass('form-check');

			//갤러리게시판 선택시 editor사용
			$('[name=bbs_ty_code]').on({'change' : function(){
				if($(this).val() == 30 || $(this).val() == 31){
					$('#editr_yn_y').prop("checked",true);
					$('#editr_yn_n').prop("checked",false);
					$('#cntnts_yn_n').prop("checked",true);
					$('#cntnts_yn_y').prop("checked",false);
					//에디터 숨기기
					$('[name=cntnts_cn]').parent().parent().hide();
					$('#cntnts_preview').hide();
					$('[name=cntnts_cn]').val("");
				}else{
					$('#editr_yn_y').prop("checked",false);
					$('#editr_yn_n').prop("checked",true);
				}
			}
			});
			//게시판 유형이 갤러리게시판일 경우 editor사용 여부 변경 불가
			$('[name=editr_yn]').on({'change' : function(){
					if($('[name=bbs_ty_code]:checked').val() == 30 || $('[name=bbs_ty_code]:checked').val() == 31){
						alert("갤러리 게시판은 ediotor 사용이 필수 입니다.")
						$('#editr_yn_y').prop("checked",true);
						$('#cntnts_yn_n').prop("checked",true);
					}
				}
			});
			//게시판 유형이 갤러리게시판일 경우 editor사용 여부 변경 불가 /// 포토 갤러리일 경우 콘텐츠 여부 N으로 변경
			$('[name=editr_yn],[name=cntnts_yn]').on({'change' : function(){
				if($('[name=bbs_ty_code]:checked').val() == 30 || $('[name=bbs_ty_code]:checked').val() == 31){
					alert("갤러리 게시판은 에디터 사용이 필수이고 콘텐츠 사용이 불가능합니다.")
					$('#editr_yn_y').prop("checked",true);
					$('input:radio[id=cntnts_yn_n]').prop("checked", true);
					//에디터  숨기기
					$('[name=cntnts_cn]').parent().parent().hide();
					$('#cntnts_preview').hide();
					$('[name=cntnts_cn]').val("");
				}
			}});

			//답변 여부 '아니오' 선택시 답변 관련 셀렉트 값 N값 고정
			if( ${resultMap.ANSWER_YN eq 'N'} && ${resultMap.MNGR_ANSWER_YN eq 'N'}){
				$('[name=answer_editr_yn]').attr("disabled", true);
				$('[name=answer_atch_file_yn]').attr("disabled", true);
			}

			$('[name=answer_yn], [name=mngr_answer_yn]').on({'change' : function(){
				if($('[name=answer_yn]:checked').val() == 'N' && $('[name=mngr_answer_yn]:checked').val() == 'N'){
					$('#answer_editr_yn_n').prop("checked",true);
					$('[name=answer_editr_yn]').attr("disabled", true);
					$('#answer_atch_file_yn_n').prop("checked",true);
					$('[name=answer_atch_file_yn]').attr("disabled", true);
				}else{
					$('[name=answer_editr_yn]').attr("disabled", false);
					$('[name=answer_editr_yn]').prop("selected", true);
					$('[name=answer_atch_file_yn]').attr("disabled", false);
					$('[name=answer_atch_file_yn]').prop("selected", true);
				}
			}});

			$(function() {
				if($('[name=answer_yn]:checked').val() == 'N' && $('[name=mngr_answer_yn]:checked').val() == 'N'){
					$('#answer_editr_yn_n').prop("checked",true);
					$('[name=answer_editr_yn]').attr("disabled", true);
					$('#answer_atch_file_yn_n').prop("checked",true);
					$('[name=answer_atch_file_yn]').attr("disabled", true);
				}else{
					$('[name=answer_editr_yn]').attr("disabled", false);
					$('[name=answer_editr_yn]').prop("selected", true);
					$('[name=answer_atch_file_yn]').attr("disabled", false);
					$('[name=answer_atch_file_yn]').prop("selected", true);
				}
			});

			// 콘텐츠여부에 따른 에디터 보여주기
			$("input:radio[name=cntnts_yn]").change(function(){
				if($('[name=cntnts_yn]:checked').val() == "Y"){

					$('[name=cntnts_position]').val("").prop("selected", true);
					$('[name=cntnts_position]').attr("disabled", false);

					$('[name=cntnts_cn]').parent().parent().show();
					$('#cntnts_preview').show();
				} else {
					$('[name=cntnts_position]').val("").prop("selected", true);
					$('[name=cntnts_position]').attr("disabled", true);

					$('[name=cntnts_cn]').parent().parent().hide();
					$('#cntnts_preview').hide();
					$('[name=cntnts_cn]').val("");
				}
			});
		});

		function fnDetail(){
			$("#aform").attr({action:"/admin/bbsmng/"+fnSysMappingCode()+"selectBbsMng.do", method:'post'}).submit();
		}

		function fnUpdate(){

			if($("[name=author_id]:checked").length < 1){
				alert("하나이상의 권한을 선택해주세요.");
				$("[name=author_id]").focus();
				return;
			}

			if($("[name=bbs_nm]").val() == ""){
				alert("게시판 명을 입력해주세요.");
				$("[name=bbs_nm]").focus();
				return;
			}

			// 콘텐츠 여부 validate
			if($("[name=cntnts_yn]").val()!="" ){
				if($("[name=cntnts_yn]:checked").val()=="Y" ){
					if($("[name=cntnts_position]").val()=="" ){
						alert("콘텐츠 위치를 선택해주세요.");
						$("[name=cntnts_position]").focus();
						return;
					}

					if($("[name=cntnts_yn]:checked").val()=="Y"){
						$('[name=cntnts_cn]').val(CKEDITOR.instances['cn'].getData());

						if(CKEDITOR.instances['cn'].getData().length < 1){
							alert("내용을 입력해 주세요.");
							$("[name=cn]").focus();
							return;
						}
					}
				}
			}

			var comfirmMsg = "수정하시겠습니까?";

			if(${requestScope.param.existBbs == 'Y'}){
				comfirmMsg = "해당 게시판에 게시물이 존재합니다. \n수정할 경우 데이터 손실이 있을 수 있습니다. \n수정하시겠습니까?";
			}

			if(confirm(comfirmMsg)){
				$('[name=answer_editr_yn]').attr("disabled", false);
				$('[name=answer_atch_file_yn]').attr("disabled", false);

				$("#aform").attr({action:"/admin/bbsmng/"+fnSysMappingCode()+"updateBbsMng.do", method:'post'}).submit();
			}
		}

		function fnGoPreview(device){
			if($("[name=sys_code]").val() == ""){
				alert("시스템을 선택해 주세요.");
				$("[name=sys_code]").focus();
				return;
			}

			if($("[name=cntnts_position]").val()=="" ){
				alert("콘텐츠 위치를 선택해주세요.");
				$("[name=cntnts_position]").focus();
				return;
			}

			var subpath = "${resultMap.SUBPATH}";

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
			$('#aform').attr({'action' : url, target: target , method:"post"}).submit();
			$('#aform').attr({target: "_self"});
		}

	//]]>
	</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/bbsmng/updateBbsMng.do" >
					<input type="hidden" name="bbs_code"		value="${requestScope.param.bbs_code}"/>
					<input type="hidden" name="sch_sys_code"		value="${requestScope.param.sch_sys_code}" />
					<input type="hidden" name="sch_bbs_ty_code"		value="${requestScope.param.sch_bbs_ty_code}" />
					<input type="hidden" name="sch_use_yn"		value="${requestScope.param.sch_use_yn}" />
					<input type="hidden" name="sch_text"	value="${requestScope.param.sch_text}" />
					<input type="hidden" name="currentPage"		value="${requestScope.param.currentPage}"/>
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
											<th class="required_field align-middle">게시판권한</th>
											<td colspan="3">
											<c:set var="checkAuthorIdList" />
											<c:forEach var="item" items="${resultMap.AUTHOR_LIST}" varStatus="status">
												<c:set var="checkAuthorIdList" value="${checkAuthorIdList},${item.AUTHOR_ID}" />
											</c:forEach>
											<c:forEach var="item" items="${authList}" varStatus="status">
												<c:set var="authorId" value=",${item.AUTHOR_ID}" />
												<div class="form-check">
													<input class="form-check-input" type="checkbox" name="author_id" id="author${status.index}" value="${item.AUTHOR_ID}" <c:if test="${fn:contains(checkAuthorIdList, authorId)}"> checked="checked" </c:if> />
													<label class="form-check-label" for="author${status.index}">${item.AUTHOR_NM}</label>
												</div>
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
								<table class="table text-nowrap inlineBlock">
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
											<th class="required_field align-middle">시스템</th>
											<td class="align-middle" colspan="3">
												${resultMap.SYS_CODE_NM}
											</td>
											<th class="required_field align-middle">게시판 코드</th>
											<td class="align-middle" colspan="3">
												${resultMap.BBS_CODE}
											</td>
										</tr>
										<tr>
											<th class="required_field align-middle">게시판 명</th>
											<td colspan="7">
												<input type="text" class="form-control" id="bbs_nm" name="bbs_nm" maxlength="50" value="${resultMap.BBS_NM}"/>
											</td>
										</tr>
										<tr>
											<th class="required_field align-middle">게시판 유형</th>
											<td colspan="7">
												${CacheCommboUtil:getRadioStrAttrImg(UPCODE_BBS_TY, 'CODE', 'CODE_NM', resultMap.BBS_TY_CODE, 'bbs_ty_code')}
											</td>
										</tr>
										<tr>
											<th class="required_field align-middle">등록 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="regist_yn" id="regist_yn_y" value="Y" <c:if test="${resultMap.REGIST_YN eq 'Y'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="regist_yn_y" style="margin-right:10px;"> 예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="regist_yn" id="regist_yn_n" value="N" <c:if test="${resultMap.REGIST_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="regist_yn_n"> 아니오</label>
												</div>
											</td>
											<th class="required_field align-middle">에디터 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="editr_yn" id="editr_yn_y" value="Y" <c:if test="${resultMap.EDITR_YN eq 'Y'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="editr_yn_y" style="margin-right:10px;"> 예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="editr_yn" id="editr_yn_n" value="N" <c:if test="${resultMap.EDITR_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="editr_yn_n"> 아니오</label>
												</div>
											</td>
											<th class="required_field">첨부 파일 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="atch_file_yn" id="atch_file_yn_y" value="Y" <c:if test="${resultMap.ATCH_FILE_YN eq 'Y'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="atch_file_yn_y" style="margin-right:10px;"> 예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="atch_file_yn" id="atch_file_yn_n" value="N" <c:if test="${resultMap.ATCH_FILE_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="atch_file_yn_n"> 아니오</label>
												</div>
											</td>
											<th class="required_field align-middle">링크 URL 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="link_url_yn" id="link_url_yn_y" value="Y" <c:if test="${resultMap.LINK_URL_YN eq 'Y'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="link_url_yn_y" style="margin-right:10px;"> 예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="link_url_yn" id="link_url_yn_n" value="N" <c:if test="${resultMap.LINK_URL_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="link_url_yn_n"> 아니오</label>
												</div>
											</td>
										</tr>
										<tr>
											<th class="required_field align-middle">키워드 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="kwrd_yn" id="kwrd_yn_y" value="Y" <c:if test="${resultMap.KWRD_YN eq 'Y'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="kwrd_yn_y" style="margin-right:10px;"> 예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="kwrd_yn" id="kwrd_yn_n" value="N" <c:if test="${resultMap.KWRD_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="kwrd_yn_n"> 아니오</label>
												</div>
											</td>
											<th class="required_field align-middle">동영상 URL 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="mvp_url_yn" id="mvp_url_yn_y" value="Y" <c:if test="${resultMap.MVP_URL_YN eq 'Y'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="mvp_url_yn_y" style="margin-right:10px;"> 예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="mvp_url_yn" id="mvp_url_yn_n" value="N" <c:if test="${resultMap.MVP_URL_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="mvp_url_yn_n"> 아니오</label>
												</div>
											</td>
											<th class="required_field align-middle">답변 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="answer_yn" id="answer_yn_y" value="Y" <c:if test="${resultMap.ANSWER_YN eq 'Y'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="answer_yn_y" style="margin-right:10px;"> 예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="answer_yn" id="answer_yn_n" value="N" <c:if test="${resultMap.ANSWER_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="answer_yn_n"> 아니오</label>
												</div>
											</td>
											<th class="required_field align-middle">관리자 답변 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="mngr_answer_yn" id="mngr_answer_yn_y" value="Y" <c:if test="${resultMap.MNGR_ANSWER_YN eq 'Y'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="mngr_answer_yn_y" style="margin-right:10px;"> 예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="mngr_answer_yn" id="mngr_answer_yn_n" value="N" <c:if test="${resultMap.MNGR_ANSWER_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="mngr_answer_yn_n"> 아니오</label>
												</div>
											</td>
										</tr>
										<tr>
											<th class="required_field align-middle">답변 에디터 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="answer_editr_yn" id="answer_editr_yn_y" value="Y" <c:if test="${resultMap.ANSWER_EDITR_YN eq 'Y'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="answer_editr_yn_y" style="margin-right:10px;"> 예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="answer_editr_yn" id="answer_editr_yn_n" value="N" <c:if test="${resultMap.ANSWER_EDITR_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="answer_editr_yn_n"> 아니오</label>
												</div>
											</td>
											<th class="required_field align-middle">답변 첨부 파일 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="answer_atch_file_yn" id="answer_atch_file_yn_y" value="Y" <c:if test="${resultMap.ANSWER_ATCH_FILE_YN eq 'Y'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="answer_atch_file_yn_y" style="margin-right:10px;"> 예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="answer_atch_file_yn" id="answer_atch_file_yn_n" value="N" <c:if test="${resultMap.ANSWER_ATCH_FILE_YN eq 'N'}">checked="checked"</c:if>/>
													<label class="form-check-label" for="answer_atch_file_yn_n"> 아니오</label>
												</div>
											</td>
											<th class="required_field align-middle">콘텐츠 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="cntnts_yn" id="cntnts_yn_y" value="Y" <c:if test="${resultMap.CNTNTS_YN == 'Y'}">checked="checked"</c:if> />
													<label class="form-check-label" for="cntnts_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="cntnts_yn" id="cntnts_yn_n" value="N" <c:if test="${resultMap.CNTNTS_YN == 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="cntnts_yn_n">아니오</label>
												</div>
											</td>
											<th class="align-middle">콘텐츠 위치</th>
											<td class="align-middle">
												<select name="cntnts_position" class="form-control input-sm"  <c:if test="${resultMap.CNTNTS_YN == 'N'}"> disabled="disabled" </c:if> >
													${CacheCommboUtil:getComboStr(UPCODE_BBS_CNTNTS_POSITION, 'CODE', 'CODE_NM', resultMap.CNTNTS_POSITION_CODE, 'C')}
												</select>
											</td>
										</tr>
										<tr>
											<th class="required_field align-middle">비밀번호 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="password_yn" id="password_yn_y" value="Y" <c:if test="${resultMap.PASSWORD_YN == 'Y'}">checked="checked"</c:if> />
													<label class="form-check-label" for="password_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="password_yn" id="password_yn_n" value="N" <c:if test="${resultMap.PASSWORD_YN == 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="password_yn_n">아니오</label>
												</div>
											</td>
											<th class="required_field align-middle">비로그인 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="nonlogin_yn" id="nonlogin_yn_y" value="Y" <c:if test="${resultMap.NONLOGIN_YN == 'Y'}">checked="checked"</c:if> />
													<label class="form-check-label" for="nonlogin_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="nonlogin_yn" id="nonlogin_yn_n" value="N" checked="checked" <c:if test="${resultMap.NONLOGIN_YN == 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="nonlogin_yn_n">아니오</label>
												</div>
											</td>
											<th class="required_field align-middle">캡차 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="captcha_yn" id="captcha_yn_y" value="Y" <c:if test="${resultMap.CAPTCHA_YN == 'Y'}">checked="checked"</c:if> />
													<label class="form-check-label" for="captcha_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="captcha_yn" id="captcha_yn_n" value="N" checked="checked" <c:if test="${resultMap.CAPTCHA_YN == 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="captcha_yn_n">아니오</label>
												</div>
											</td>
											<th class="required_field align-middle">사용 여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="use_yn" id="use_yn_y" value="Y" <c:if test="${resultMap.USE_YN == 'Y'}">checked="checked"</c:if> />
													<label class="form-check-label" for="use_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="use_yn" id="use_yn_n" value="N" <c:if test="${resultMap.USE_YN == 'N'}">checked="checked"</c:if> />
													<label class="form-check-label" for="use_yn_n">아니오</label>
												</div>
											</td>
										</tr>
										<tr>
											<th class="align-middle">비고</th>
											<td class="align-middle" colspan="7">
												<input type="text" class="form-control" id="rm" name="rm" maxlength="100" value="${resultMap.RM}" />
											</td>
										</tr>
										<c:if test="${resultMap.CNTNTS_YN == 'Y'}">
											<tr>
												<th class="align-middle">콘텐츠 내용</th>
												<td class="align-middle" colspan="7">
													<textarea class="form-control" rows="3" id="cn" name="cntnts_cn" placeholder="내용">${resultMap.CNTNTS_CN}</textarea>
												</td>
											</tr>
										</c:if>
									</tbody>
								</table>
							</div>
						</div>
						<div id="imageBox" style="margin-left:1.25em;">
							<c:if test="${resultMap.CNTNTS_YN == 'Y'}">
								<div class="pull-left" id="cntnts_preview">
								</div>
							</c:if>
							<c:if test="${resultMap.CNTNTS_YN == 'N'}">
								<div class="pull-left" id="cntnts_preview" style="display: none">
								</div>
							</c:if>
							<button type="button" class="btn btn-info" onclick="fnGoPreview('web'); return false;">웹 사이즈 미리보기</button>
							<button type="button" class="btn btn-info" onclick="fnGoPreview('mobile'); return false;">모바일 사이즈 미리보기</button>
							<p style="color:red">* 해당 페이지는 퍼블리싱된 콘텐츠가 올라가는곳으로 html 태그없이 텍스트만 입력할시 원하는 스타일대로 적용되지 않을 수 있습니다. </p>
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
				<!-- ckeditor 이미지 업로드에 필요 -->
				<form id="img_upload_form" enctype="multipart/form-data" method="post" style="display:none;">
					<input type="file" id="img_file" multiple="multiple" name="imgfile[]" accept="image/*">
				</form>
			</div>
			</div>
		</div>
	</div>

<script>
   $(function(){
	  	CKEDITOR.replace('cn',{
	  		filebrowserUploadUrl: '${pageContext.request.contextPath}/photoServerUpload.do'
	  	});
	  });
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
