 <%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<script type="text/javascript" src="/common/ckeditor/ckeditor.js" charset="UTF-8" ></script>
<script type="text/javascript" src="/common/ckeditor/custom.js" charset="UTF-8" ></script>
<script type="text/javascript" src="/common/ckeditor/adapters/jquery.js" charset="UTF-8" ></script>

<link rel="stylesheet" type="text/css" media="all" href="/common/css/dragAndDrop.css" />
<script src="/common/js/dragAndDrop.js"></script>
<script type="text/javascript">
	 //<![CDATA[
	$(function(){

	});

	function f_GoList(){
		$("#aform").attr({action:"/admin/stdTerm/"+fnSysMappingCode()+"selectPageListStdTermMgt.do", method:'post'}).submit();
	}

	function f_GoInsert(){

		//////////첨부파일 용량 체크///////////
		// 등록할 파일 리스트
		var uploadFileList = Object.keys(fileList);
		var uploadFileListCount = uploadFileList.length -1

		uploadFileList.splice(1,uploadFileList.length);

		// 용량을 500MB를 넘을 경우 업로드 불가
		if(totalFileSize > maxUploadSize){
			// 파일 사이즈 초과 경고창
			alert("총 용량을 초과하였습니다. \n남은 총 업로드 가능 용량 : " + maxUploadSize + " MB");
			return;
		}
		////////////////////////////////

		if(confirm("등록하시겠습니까?")){
			//일반 업로드용
			//$("#aform").attr({action:"/admin/stdTerm/insertStdTermMgt.do", method:'post'}).submit();

			//////////////첨부파일 멀티 업로드 //////////////

			var formData = new FormData($('#aform')[0]);

			for(var i = 0; i < uploadFileList.length; i++){
				formData.append('upload', fileList[uploadFileList[i]]);
			}

			$.ajax({
				url:"/admin/stdTerm/insertStdTermMgt.do",
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
					}else{
						location.href = '/admin/stdTerm/selectPageListStdTermMgt.do';
					}
				}
			});

		}
	}

	// 업로드 파일 목록 생성
	function addFileList(fIndex, fileName, fileSize){


		var html = "";
		html += "<tr class='projects td' id='fileTr_" + fIndex + "'>";
		html += "	<td>";
		html +=			fileName + " / " + fileSize.toFixed(2) + "MB " ;
		html += "	</td>"
		html += "	<td class='text-center'>";
		html +=	"		<button class='btn bg-gradient-danger btn-sm' onclick='deleteFile(" + fIndex + "); return false;'>삭제</button>";
		html += "	</td>"
		html += "</tr>"

		$('#fileTableTbody tbody').append(html);

		var addFileListLength = $('.projects').length;
		if(addFileListLength > 2){

			alert("표준용어 사전 파일은 한 파일만 업로드 가능합니다.");
			$('.projects').eq(addFileListLength-1).remove();
			return;
		}


	}

	//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" enctype="multipart/form-data">
					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap inlineBlock">
									<colgroup>
										<col style="width:15%;">
										<col style="width:85%;">
									</colgroup>
									<tbody>
										<tr>
											<th>첨부파일</th>
											<td>
												<div class="upload-btn-wrapper">
													<input type="file" id="input_file" name="fileList[]" multiple="multiple" style="width: 100%;" accept=".xls , .xlsx" />
													<button class="upload-btn btn btn-outline-secondary">파일선택</button>
												</div>
												<div id="dropZone" class="margin-bottom dropZone11" style="margin-bottom: 20px;">
													<div id="fileDragDesc">
														파일을 드래그 해주세요.
													</div>
												</div>
												<div class="table-responsive">
													<table class="table table-bordered" id="fileTableTbody">
														<colgroup>
															<col style="width:*%;">
															<col style="width:100px;">
														</colgroup>
														<thead>
															<tr class="projects td">
																<th class="text-center">파일명</th>
																<th></th>
															</tr>
														</thead>
														<tbody >
														</tbody>
													</table>
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
								<div style="position: absolute; left: 20px;">
										<a href="/upload/template/std_term.xls" type="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" class="btn btn-success down_excel" align="left" target="_blank">
										<i class="fa fa-download"></i> 샘플양식 다운로드</a>
									</div>
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="f_GoList(); return false;">목록</button>
									<button type="button" class="btn bg-gradient-success" onclick="f_GoInsert(); return false;">등록</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
