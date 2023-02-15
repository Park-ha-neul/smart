<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"	  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"	 uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<link rel="stylesheet" type="text/css" media="all" href="/common/css/dragAndDrop.css" />

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script src="/common/js/dragAndDrop.js"></script>
<script type="text/javascript">
//<![CDATA[
	//초기화
	$(function(){
		//데이트피커 설정
		$('[name=photo_potogrf_de]').datetimepicker({
			locale : 'ko', 	// 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY.MM.DD',
			useCurrent: false, 	//Important! See issue #1075
			sideBySide : true,
		});

		if ("${sysMappingCode}" == null || "${sysMappingCode}" == "") {
			$('[name=sys_code]').html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sys_code, '시스템')}");
		} else {
			$('[name=sys_code]').html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code,'CODE', 'CODE_NM', requestScope.param.sys_code, '')}");
		}
	});

	//목록 이동
	function fnList(){
		document.location.href="/admin/photo/"+fnSysMappingCode()+"selectPageListPhotoMgt.do";
	}

	// 업로드 파일 목록 생성
	function addFileList(fIndex, fileName, fileSize){
		var html = "";
		html += "<tr class='projects td' id='fileTr_" + fIndex + "'>";
		html += "	<td>";
		html +=			fileName + " / " + fileSize.toFixed(2) + "MB " ;
		html += "	</td>"
		html += "	<td>";
		html +=	"		<input type='text' name='caption' class='form-control' maxlength='100'>";
		html += "	</td>"
		html += "	<td>";
		html +=	"		<input type='text' name='kwrd' class='form-control' maxlength='100'>";
		html += "	</td>"
		html += "	<td class='text-center'>";
		html +=	"		<button class='btn bg-gradient-danger btn-sm' onclick='deleteFile(" + fIndex + "); return false;'>삭제</button>";
		html += "	</td>"
		html += "</tr>"

		$('#fileTableTbody tbody').append(html);
	}5

	// 파일 등록
	function uploadFileAjax(){
		// 등록할 파일 리스트
		var uploadFileList = Object.keys(fileList);

		// 파일이 있는지 체크
		if(uploadFileList.length == 0){
			// 파일등록 경고창
			alert("파일이 없습니다.");
			return;
		}

		// 용량을 500MB를 넘을 경우 업로드 불가
		if(totalFileSize > maxUploadSize){
			// 파일 사이즈 초과 경고창
			alert("총 용량 초과\n총 업로드 가능 용량 : " + maxUploadSize + " MB");
			return;
		}

		if($("[name=sys_code]").val() == ""){
			alert("시스템을 선택해 주세요.");
			$("[name=sys_code]").focus();
			return;
		}

		if($("[name=sj]").val() == ""){
			alert("제목을 선택해 주세요.");
			$("[name=sj]").focus();
			return;
		}

		if(confirm("등록하시겠습니까?")){
			// 등록할 파일 리스트를 formData로 데이터 입력
			//var form = $('#aform');
			var formData = new FormData($('#aform')[0]);
			for(var i = 0; i < uploadFileList.length; i++){
				formData.append('upload', fileList[uploadFileList[i]]);
			}

			$.ajax({
				url:"/admin/photo/"+fnSysMappingCode()+"insertPhotoImageAjax.do",
				data:formData,
				type:'POST',
				enctype:'multipart/form-data',
				processData:false,
				contentType:false,
				dataType:'json',
				cache:false,
				success:function(param){

					if(param.resultStats.resultCode == "error"){
						alert(param.resultStats.resultMsg);
						return;
					}

					fnList();
				}
			});
		}
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/photo/insertPhotoMgt.do" enctype="multipart/form-data">
					<input type="hidden" name="bbs_seq" />
					<!-- 이미지 파일만 업로드 -->
					<input type="hidden" id="fileType" value="img"/>

					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width:15%;">
										<col style="width:85%;">
									</colgroup>
									<tbody>
										<tr class="projects td">
											<th class="required_field">시스템</th>
											<td>
												<select name="sys_code" class="form-control input-sm">
												</select>
											</td>
										</tr>
										<tr class="projects td">
											<th class="required_field">제목</th>
											<td>
												<input type="text" class="form-control" name="sj" maxlength="100"/>
											</td>
										</tr>
										<tr class="projects td">
											<th>촬영 일자</th>
											<td class="datetimepicker-td">
												<input type="text" class="form-control" name="photo_potogrf_de" maxlength="10"/>
											</td>
										</tr>
										<tr class="projects td">
											<th>사진</th>
											<td>
												<div class="upload-btn-wrapper">
													<input type="file" id="input_file" name="fileList[]" multiple="multiple" style="width: 100%;" accept=".gif, .jpg, .png"/>
													<button class="upload-btn btn btn-outline-secondary">파일선택</button>
												</div>
												<div id="dropZone" class="margin-bottom dropZone11" style="margin-bottom: 20px;">
													<div id="fileDragDesc">
														파일을 드래그 해주세요.
													</div>
												</div>
													<div class="table-responsive">
														<table class="table text-nowrap" id="fileTableTbody">
															<colgroup>
																<col style="width:*%;">
																<col style="width:30%;">
																<col style="width:30%;">
																<col style="width:100px;">
															</colgroup>
															<thead>
																<tr class="projects td">
																	<th class="text-center">파일명</th>
																	<th class="text-center">캡션</th>
																	<th class="text-center">검색 키워드</th>
																	<th></th>
																</tr>
															</thead>
															<tbody>
															</tbody>
														</table>
													</div>
											</td>
										</tr>
										<tr class="projects td">
											<th>비고</th>
											<td>
												<textarea class="form-control" rows="10" id="rm" name="rm" maxlength="4000"></textarea>
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
									<button type="button" class="btn bg-gradient-success" onclick="uploadFileAjax(); return false;">확인</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
