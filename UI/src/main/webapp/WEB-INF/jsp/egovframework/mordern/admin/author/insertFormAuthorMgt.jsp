<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	$(function(){
		//f_divView('main_2', 'main_2');	//메뉴구조 유지
	});
	function f_GoList(){
		document.location.href="/admin/author/selectPageListAuthorMgt.do";
	}

	function f_GoInsert(){

		if($("#author_nm").val() == ""){
			alert("권한명을 입력해 주세요.");
			$("#author_nm").focus();
			return;
		} else if($("#author_id").val() == ""){
			alert("권한ID를 입력해 주세요.");
			$("#author_id").focus();
			return;
		}

		var param = { 'author_id' : $('#author_id').val() };
		$.ajax({
			url : '/admin/author/selectExistYnAuthorMgtAjax.do',
			data : param,
			dataType : 'json',
			type : 'post',
			success : function(response){
				if(response.resultStats.resultCode == 'success'){
					if(response.resultStats.resultMsg.existYn == 'N'){
						if(confirm("등록하시겠습니까?")){
							$("#aform").attr({action:"/admin/author/insertAuthorMgt.do", method:'post'}).submit();
						}
					} else {
						alert('이미 등록된 권한 ID 입니다.');
						$("#auth_id").focus();
						return;
					}
				}
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/author/insertAuthorMgt.do">
					<input type="hidden" name="popup_seq" />

					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width:20%;">
										<col style="width:30%;">
										<col style="width:20%;">
										<col style="width:30%;">
									</colgroup>
									<tbody>
										<tr>
											<th class="required_field">권한ID</th>
											<td>
												<div class="outBox1">
													<input type="text" class="form-control" id="author_id" name="author_id" placeholder="권한ID" onkeyup="fnBlockKorean(this);" onkeydown="cfLengthCheck('권한ID는', this, 10);">
												</div>
											</td>
											<th class="required_field">권한명</th>
											<td>
												<div class="outBox1">
													<input type="text" class="form-control" id="author_nm" name="author_nm" placeholder="권한명" onkeyup="cfLengthCheck('권한명은', this, 100);">
												</div>
											</td>
										</tr>
										<tr>
											<th>내용</th>
											<td colspan="3">
												<div class="outBox1">
													<textarea class="form-control" rows="3" name="rm" placeholder="내용..." onkeyup="cfLengthCheck('내용은', this, 100);"></textarea>
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
