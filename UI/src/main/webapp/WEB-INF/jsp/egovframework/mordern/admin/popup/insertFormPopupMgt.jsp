<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript" src="/common/ckeditor/ckeditor.js" charset="UTF-8" ></script>
<script type="text/javascript" src="/common/ckeditor/adapters/jquery.js" charset="UTF-8" ></script>

<script type="text/javascript">
//<![CDATA[
	$(function(){
		$('[name=disp_begin_de]').datetimepicker({
			locale : 'ko', 	// 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD HH:mm',
			useCurrent: false, 	//Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'left',
				vertical : 'bottom'
			},
		});

		$('[name=disp_end_de]').datetimepicker({
			locale : 'ko', 	// 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD HH:mm',
			useCurrent: false, 	//Important! See issue #1075
			sideBySide : true,
			widgetPositioning : {
				horizontal : 'right',
				vertical : 'bottom'
			},
		});

		//팝업 이미지 확장자 체크
		$(document).on('change', '[name=upload]', function(){
			if(!f_CheckAcceptFileExt('upload', "JPG|JPEG|GIF|PNG")){
				alert('팝업 이미지는 (JPG,JPEG,GIF,PNG)파일만 업로드 가능합니다. ');
				var agent = navigator.userAgent.toLowerCase();
				if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
					$(this).replaceWith( $(this).clone(true) );
				}else{
					$(this).val("");
				}
				return;
			}
		});

		if ("${sysMappingCode}" == "") {
			$('[name=sys_code]').html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sys_code, '시스템')}");
		} else {
			$('[name=sys_code]').html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code, 'CODE', 'CODE_NM', requestScope.param.sys_code, '')}");
		}
	});

	function fnList(){
		document.location.href="/admin/popup/"+fnSysMappingCode()+"selectPageListPopupMgt.do";
	}

	function fnInsert(){
		if($("[name=sys_code]").val() == ""){
			alert("시스템을 선택해 주세요.");
			$("[name=sys_code]").focus();
			return;
		}

		if($("[name=popup_se_code]").val() == ""){
			alert("팝업 구분을 선택해 주세요.");
			$("[name=popup_se_code]").focus();
			return;
		}
		if($("[name=sj]").val() == ""){
			alert("제목을 입력해 주세요.");
			$("[name=sj]").focus();
			return;
		}
		if($("[name=popup_se_code]").val() == "W"){	//Web 일경우
			if($("[name=width_size]").val() == ""){
				alert("가로사이즈를 입력해 주세요.");
				$("[name=width_size]").focus();
				return;
			}

			if($("[name=vrticl_size]").val() == ""){
				alert("세로사이즈를 입력해 주세요.");
				$("[name=vrticl_size]").focus();
				return;
			}
		}
		if($("[name=disp_begin_de]").val() == ""){
			alert("전시 시작일을 입력해 주세요.");
			$("[name=disp_begin_de]").focus();
			return;
		}
		if($("[name=disp_end_de]").val() == ""){
			alert("전시 종료일을 입력해 주세요.");
			$("[name=disp_end_de]").focus();
			return;
		}

		if(confirm("등록하시겠습니까?")){
			$("#aform").attr({action:"/admin/popup/"+fnSysMappingCode()+"insertPopupMgt.do", method:'post'}).submit();
		}
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form id="img_upload_form" enctype="multipart/form-data" method="post" style="display:none;">
					<input type="file" id="img_file" multiple="multiple" name="imgfile[]" accept="image/*">
				</form>
				<form role="form" id="aform" method="post" enctype="multipart/form-data">
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
											<th class="required_field">시스템</th>
											<td>
												<select name="sys_code" class="form-control input-sm">
												</select>
											</td>
											<th class="required_field">팝업 구분</th>
											<td>
												<select name="popup_se_code" class="form-control input-sm">
													${CacheCommboUtil:getComboStr(UPCODE_POPUP_SE, 'CODE', 'CODE_NM', requestScope.param.sch_popup_se_code, 'C')}
												</select>
											</td>
										</tr>
										<tr>
											<th class="required_field">제목</th>
											<td colspan="3">
												<input type="text" class="form-control" name="sj" maxlength="100"/>
											</td>
										</tr>
										<tr>
											<th>Link URL</th>
											<td colspan="3">
												<input type="text" class="form-control" name="link_url" maxlength="100"/>
											</td>
										</tr>
										<tr>
											<th>팝업 사용여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="use_yn" id="use_yn_y" value="Y" checked="checked" />
													<label class="form-check-label" for="use_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="use_yn" id="use_yn_n" value="N" />
													<label class="form-check-label" for="use_yn_n" style="margin-right:10px;">아니오</label>
												</div>
											</td>
											<th>모바일 팝업 사용여부</th>
											<td class="align-middle">
												<div class="form-check">
													<input class="form-check-input" type="radio" name="mobile_use_yn" id="mobile_use_yn_y" value="Y" checked="checked" />
													<label class="form-check-label" for="mobile_use_yn_y" style="margin-right:10px;">예</label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio" name="mobile_use_yn" id="mobile_use_yn_n" value="N" />
													<label class="form-check-label" for="mobile_use_yn_n" style="margin-right:10px;">아니오</label>
												</div>
											</td>
										</tr>
										<tr>
											<th>가로 사이즈</th>
											<td>
												<div>
													<input type="text" class="form-control numeric" name="width_size" maxlength="4" value="800"/>
												</div>
											</td>
											<th>세로 사이즈</th>
											<td>
												<div>
													<input type="text" class="form-control numeric" name="vrticl_size" maxlength="4" value="600"/>
												</div>
											</td>
										</tr>
										<tr>
											<th>팝업 가로 위치</th>
											<td>
												<div>
													<input type="text" class="form-control numeric" name="crdnt_x" maxlength="4" />
												</div>
											</td>
											<th>팝업 세로 위치</th>
											<td>
												<div>
													<input type="text" class="form-control numeric" name="crdnt_y" maxlength="4" />
												</div>
											</td>
										</tr>
										<tr>
											<th class="required_field">전시 시작일</th>
											<td class="datetimepicker-td">
												<div>
													<input type="text" class="form-control" name="disp_begin_de" />
												</div>
											</td>
											<th class="required_field">전시 종료일</th>
											<td class="datetimepicker-td">
												<div>
													<input type="text" class="form-control" name="disp_end_de" />
												</div>
											</td>
										</tr>
										<tr>
											<th>내용</th>
											<td colspan="3">
												<textarea class="form-control" rows="10" id="cn" name="cn" placeholder="내용"></textarea>
											</td>
										</tr>
										<tr>
											<th>팝업 이미지</th>
											<td colspan="3">
												<div class="custom-file">
													<input type="file" class="form-control custom-file-input" name="upload" accept=".gif, .jpg, .png"/>
													<label class="custom-file-label">선택된 파일 없음</label>
												</div>
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
									<button type="button" class="btn bg-gradient-success" onclick="fnInsert(); return false;">확인</button>
								</div>
							</div>
						</div>
					</div>
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
