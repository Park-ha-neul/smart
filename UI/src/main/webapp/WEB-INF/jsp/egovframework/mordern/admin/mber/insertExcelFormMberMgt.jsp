<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	$(function(){

		// 엑셀 파일 업로드
		$(document).on('change', '[name=upload]', function(){
			var files = this.files;
			var fileName = files[0].name;
			var fileNameArr = fileName.split("\.");
			// 확장자
			var ext = fileNameArr[fileNameArr.length - 1];

			//확장자 검사
			if($.inArray(ext.toLowerCase(), ['xls','xlsx']) < 0){
				// 확장자 체크
				alert("'xls','xlxs' 확장자만 업로드 가능합니다.");
				$("[name=upload]").val(null);
				$(".custom-file-label").text("선택된 파일 없음");

				return;
			}

			// 업로드
			$('#aform').ajaxSubmit({
				url : '/admin/mber/selectExcelMberMgtAjax.do',
				type : 'post',
				enctype: 'multipart/form-data',
				dataType : 'json',
				success : function(response){
					if(response.resultStats.resultCode == 'success'){
						var items = response.resultList;
						var innerHtml = '';

						for(var i = 0; i < items.length; i++){
							var item = items[i];

							var danger = item.dupl_yn == 'Y' ? 'bg-danger' : '';
							innerHtml += '<tr class="' + danger + '">';
							innerHtml += '	<td class="text-center">' + (i + 1) + '</td>';
							innerHtml += '	<td>' + item.nm + '</td>';
							innerHtml += '	<td class="text-center">' + item.ihidnum + '</td>';
							innerHtml += '	<td class="text-center">' + item.moblphon_no + '</td>';
							innerHtml += '</tr>';
						}
						console.log(innerHtml);
						$('#userList').html(innerHtml);
					} else {
						ajaxErrorMsg(response);
					}
				},
			});
		});
	});

	//목록
	function fnGoList(){
		document.location.href="/admin/mber/selectPageListMberMgt.do";
	}

	// 등록
	function fnGoInsert(){
		// 중복된 데이터가 있는지 확인
		if($('#userList tr.bg-danger').length > 0){
			if(confirm('중복된 데이터가 있습니다. 등록하실경우 해당 데이터는 등록 되지 않습니다. 그래도 등록하시겠습니까?')){
				$('#aform').attr({action:'/admin/mber/insertExcelMberMgt.do', method:'post'}).submit();
			}
		} else {
			if(confirm('등록하시겠습니까?')){
				$('#aform').attr({action:'/admin/mber/insertExcelMberMgt.do', method:'post'}).submit();
			}
		}
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="#" enctype="multipart/form-data">
						<p>
							1. 샘플 양식을 다운받는다. <br/>
							2. 작성한 파일을 선택한후 파일선택옆의 업로드 버튼을 클릭한다. <br/>
							3. 테이블에 작성한 데이터가 정상적으로 표현되었는지 확인한다. <br/>
							4. 등록버튼을 클릭하여 사용자 등록을 한다. <br/>
							<b class="text-danger">** 동일한 이름,주민번호가 이미 등록된 경우는 등록되지 않는다.</b>
						</p>
					<div class="card card-info card-outline">
						<div class="card-header">
							<div class="form-row justify-content-between">
								<div class="" style="float:left;width: 10%;">
									<label for="excel_file">업로드 파일 : &nbsp;</label>
								</div>
								<div class="" style="float:left;width:90%;">
									<div class="custom-file">
										<input type="file" class="form-control custom-file-input" id="excel_file" name="upload" title="검색어를 입력하세요." value="${requestScope.param.sch_text}" />
										<label class="custom-file-label">선택된 파일 없음</label>
									</div>
								</div>
							</div>
						</div>
						<div class="card-body">
							<div class="table-responsive">
										<table class="table table-hover text-nowrap table-grid">
											<colgroup>
												<col width="5%" />
												<col width="30%" />
												<col width="35%" />
												<col width="30%" />
											</colgroup>
											<thead>
												<tr>
													<th class="text-center">No</th>
													<th class="text-center">이름</th>
													<th class="text-center">주민번호 앞자리</th>
													<th class="text-center">핸드폰 번호</th>
												</tr>
											</thead>
											<tbody>
											<tr>
											<td class="text-center">&nbsp;</td>
											<td>&nbsp;</td>
											<td class="text-center">&nbsp;</td>
											<td class="text-center">&nbsp;</td>
										</tr>
											</tbody>
										</table>
									</div>
								</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnGoList(); return false;">목록</button>
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnGoInsert(); return false;">등록</button>
									<a href="/upload/template/user_ex.xlsx" type="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" class="btn btn-success down_excel" target="_blank"><i class="fa fa-download"></i> 샘플양식 다운로드</a>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
