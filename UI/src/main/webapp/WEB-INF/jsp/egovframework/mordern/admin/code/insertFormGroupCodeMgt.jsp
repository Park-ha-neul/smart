<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	$(function(){
		//group_id 자동 채번
		var newGroupId = Number($("#new_group_id").val()) + 10;
		var temp = newGroupId.toString();
		var	newGroupId_length = temp.length;
			if ($("#new_group_id").val() == null)	//초기상태
			$("#group_id").val("R010010");
			if (newGroupId_length == 5)
			$("#group_id").val("R0"+newGroupId);
			if (newGroupId_length > 5)
				$("#group_id").val("R"+newGroupId);
	});

	function fnGoInsert(){
		if($("#group_id").val() == ""){
			alert("그룹ID를 입력해 주세요.");
			$("#group_id").focus();
			return;
		}

		if($("[name=group_id]").attr("duplicate") != "Y"){
			alert("그룹ID 중복검사를 해주세요.");
			$("[name=group_id]").focus();
			return;
		}

		if($("[name=group_id]").attr("duplicate") == "Y" && $("[name=group_id]").val() != $("[name=group_id]").attr("compareVal")){
			alert("중복검사한 그룹ID가 아닙니다.");
			$("[name=group_id]").focus();
			return;
		}

		if($("#group_nm").val() == ""){
			alert("그룹코드명을 입력해 주세요.");
			$("#group_nm").focus();
			return;
		}

		if(confirm("등록하시겠습니까?")){
			$("#aform").attr({action:"/admin/code/insertGroupCodeMgt.do", method:'post'}).submit();
		}
	}

	function fnGoList(){
		document.location.href="/admin/code/selectPageListGroupCodeMgt.do";
	}

	//그룹ID 중복 체크
	function fnGroupIdCheck(){

		if($("#group_id").val() == ""){
			alert("그룹ID를 입력해 주세요.");
			return;
		}

		var req = {
				group_id : $("#group_id").val()
		};

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/admin/code/selectExistYnGroupIdAjax.do',
			data : req,
			success : function(param) {

				if(param.resultStats.resultCode == "error"){
					alert(param.resultStats.resultMsg);
					return;
				}

				if(param.resultMap.EXIST_YN == "Y"){
					alert("그룹ID가 존재합니다.");
					$("#bbs_code").attr("duplicate","N");
				}
				else{
					alert("사용가능한 그룹ID 입니다.");
					$("#group_id").attr("duplicate","Y");
					$("#group_id").attr("compareVal",req["group_id"]);
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
				<input type="hidden" name="new_group_id" id="new_group_id" value="${resultMap.MAX_GROUP_ID}"/>
				<form role="form" id="aform" method="post" action="/admin/code/selectPageListCodeMgt.do">
					<div class="card card-info card-outline">
						<div class="card-header">
							<h3 class="card-title">코드 그룹 등록</h3>
						</div>
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
											<th class="required_field">그룹ID</th>
											<td colspan="3">
												<div class="input-group">
													<input type="text" class="form-control" id="group_id" name="group_id" placeholder="그룹ID" onkeyup="fnBlockKorean(this);" onkeydown="cfLengthCheck('그룹ID는', this, 10);">
													<div class="input-group-append">
														<button type="button" class="btn bg-gradient-success" onclick="fnGroupIdCheck(); return false;">중복확인</button>
													</div>
												</div>
											</td>
										</tr>
										<tr>
											<th class="required_field">그룹코드명</th>
											<td>
												<div class="outBox1">
													<input type="text" class="form-control" id="group_nm" name="group_nm" placeholder="그룹코드명" onkeyup="cfLengthCheck('그룹코드명은', this, 50);">
												</div>
											</td>
											<th>영문그룹코드명</th>
											<td>
												<div class="outBox1">
													<input type="text" class="form-control" id="group_nm_eng" name="group_nm_eng" placeholder="영문그룹코드명" onkeyup="cfLengthCheck('영문그룹코드명은', this, 50);">
												</div>
											</td>
										</tr>
										<tr>
											<th>비고</th>
											<td colspan="3">
												<div class="outBox1">
													<textarea class="form-control" rows="3" name="rm" placeholder="비고" onkeyup="cfLengthCheck('비고는', this, 100);"></textarea>
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
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnGoList(); return false;">목록</button>
									<button type="button" class="btn bg-gradient-success" onclick="fnGoInsert(); return false;">등록</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
