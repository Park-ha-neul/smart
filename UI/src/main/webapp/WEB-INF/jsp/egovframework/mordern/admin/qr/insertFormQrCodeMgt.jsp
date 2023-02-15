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
		//확장자 체크
		$('input[name=upload]').on({'change' : function(){
			if(!f_CheckAcceptFileExt('upload', "JPG|JPEG|GIF|PNG")){
				alert('배경 이미지는 (JPG,JPEG,GIF,PNG)파일만 업로드 가능합니다. ');
				var agent = navigator.userAgent.toLowerCase();
				if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
					$(this).replaceWith( $(this).clone(true) );
				}else{
					$(this).val("");
				}
				return;
			}
		}
		});

		if ("${sysMappingCode}" == "") {
			$('[name=sys_code]').html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sys_code, '시스템')}");
		} else {
			$('[name=sys_code]').html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code, 'CODE', 'CODE_NM', requestScope.param.sys_code, '')}");
		}
	});

		function fnList(){
			document.location.href="/admin/qr/"+fnSysMappingCode()+"selectPageListQrCodeMgt.do";
		}

		function fnInsert(){
			if($("[name=sys_code]").val() == ""){
				alert("시스템을 선택해 주세요.");
				$("[name=sys_code]").focus();
				return;
			}
			if($("[name=sj]").val() == ""){
				alert("제목을 입력해 주세요.");
				$("[name=sj]").focus();
				return;
			}
			if(CKEDITOR.instances['code_cn'].getData().length < 1){
				alert("코드 내용을 입력해 주세요.");
				$("[name=code_cn]").focus();
				return;
			}
			if(confirm("등록하시겠습니까?")){
				$("#aform").attr({action:"/admin/qr/"+fnSysMappingCode()+"insertQrCodeMgt.do", method:'post'}).submit();
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
								<table class="table text-nowrap">
									<colgroup>
										<col style="width:15%;">
										<col style="width:*;">
									</colgroup>
									<tbody>
										<tr>
											<th class="required_field">시스템</th>
											<td>
												<select name="sys_code" class="form-control input-sm">
												</select>
											</td>
										</tr>
										<tr>
											<th class="required_field">제목</th>
											<td>
												<input type="text" class="form-control" id="sj" name="sj" maxlength="100"/>
											</td>
										</tr>
										<tr>
											<th>배경 이미지</th>
											<td>
												<div class="custom-file">
													<input type="file" class="form-control custom-file-input" name="upload" accept=".gif, .jpg, .png"/>
													<label class="custom-file-label">선택된 파일 없음</label>
												</div>
											</td>
										</tr>
										<tr>
											<th class="required_field">코드 내용</th>
											<td>
												<textarea class="form-control" rows="10" id="code_cn" name="code_cn" maxlength="100"></textarea>
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
	  	CKEDITOR.replace('code_cn',{
	  		filebrowserUploadUrl: '${pageContext.request.contextPath}/photoServerUpload.do'
	  	});
	  });
	  //객체 생성
	  var ajaxImage = {};
	  // ckeditor textarea id
	  ajaxImage["id"] = "code_cn";
	  // 업로드 될 디렉토리
	  ajaxImage["uploadDir"] = "upload";
	  // 한 번에 업로드할 수 있는 이미지 최대 수
	  ajaxImage["imgMaxN"] = 10;
	  // 허용할 이미지 하나의 최대 크기(MB)
	  ajaxImage["imgMaxSize"] = 10;
</script>
