<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<script type="text/javascript">
//<![CDATA[
	function f_GoPage(currentPage){
		$("#currentPage").val(currentPage);
		$("#aform").attr({action:"/admin/code/selectPageListCodeMgt.do", method:'post'}).submit();
	}

	function fnRemoveLine(removeId){
		$("#"+removeId).remove();
	}

	//입력폼 보기
	var removeNo = 0;
	function fnInsertForm(){
		var comboStr = "<select class=\"form-control\" name=\"use_yn\">";
			<c:forEach var="ynCode" items="${ynCodeList}">
			comboStr += "<option value=\"${ynCode.CODE }\" >${ynCode.CODE_NM }</option>";
			</c:forEach>
			comboStr += "</select>";

		var nextSrtOrd = 0;	//next 정렬순서
		var newSrtOrdList = f_getList("sort_ordr");
		for(i = 0; i < newSrtOrdList.length; i++){
			if(Number(nextSrtOrd) < Number(newSrtOrdList[i].value)){
					nextSrtOrd = Number(newSrtOrdList[i].value);
			}
		}
		if(newSrtOrdList.length == 0 ){
				nextSrtOrd = Number($("#new_srt_ord_val").val()) + 10;
		}	else {
				nextSrtOrd = Number(nextSrtOrd) + 10;
		}

		removeNo++;

		var table = $("#codeTable");
		var htmlStr ="<tr id=\"id_new_code_"+removeNo+"\">";
		htmlStr +="	<td><input type=\"text\" class=\"form-control\" name=\"code\" onkeyup=\"fnBlockKorean(this);\" onkeydown=\"cfLengthCheck('코드는', this, 10);\"/></td>";
		htmlStr +="	<td><input type=\"text\" class=\"form-control\" name=\"code_nm\" onkeyup=\"cfLengthCheck('코드명은', this, 100);\" /></td>";
		htmlStr +="	<td><input type=\"text\" class=\"form-control\" name=\"code_nm_eng\" onkeyup=\"cfLengthCheck('코드영문명은', this, 100);\" /></td>";
		htmlStr +="	<td><input type=\"text\" class=\"form-control\" name=\"attrb_1\" onkeyup=\"cfLengthCheck('속성1은', this, 100);\" /></td>";
		htmlStr +="	<td><input type=\"text\" class=\"form-control\" name=\"attrb_2\" onkeyup=\"cfLengthCheck('속성2는', this, 100);\" /></td>";
		htmlStr +="	<td><input type=\"text\" class=\"form-control\" name=\"attrb_3\" onkeyup=\"cfLengthCheck('속성3은', this, 100);\" /></td>";
		htmlStr +="	<td><input type=\"text\" class=\"form-control\" name=\"sort_ordr\"  value=\""+nextSrtOrd+"\"/></td>";
		htmlStr +="	<td>"+comboStr+"</td>";
		htmlStr +="	<td class=\"text-center\"><button type=\"button\" class=\"btn bg-gradient-danger btn-sm \" onclick=\"fnRemoveLine('id_new_code_"+removeNo+"');\">삭제</button></td>";
		htmlStr +="</tr>";

		table.append(htmlStr);
	}

	function fnList(){
		$("#aform").attr({action:"/admin/code/selectPageListGroupCodeMgt.do", method:'post'}).submit();
	}

	function fnUpdate(){

		if($("#group_nm").val() == ""){
			alert("그룹코드명을 입력해 주세요.");
			$("#group_nm").focus();
			return;
		}

		var codeList = f_getList("code");
		var codeNmList = f_getList("code_nm");
		var sortOrdrList = f_getList("sort_ordr");

		var codeDupYn = false;
		var codeDupCnt = 0;

		for(i = 0; i < codeList.length; i++){

			if(codeList[i].value == ""){
				alert("코드를 입력해 주세요.");
				codeList[i].focus();
				return;
			}

			if(codeNmList[i].value == ""){
				alert("코드명을 입력해 주세요.");
				codeNmList[i].focus();
				return;
			}
			if(sortOrdrList[i].value == ""){
				alert("순서를 입력해 주세요.");
				sortOrdrList[i].focus();
				return;
			}

			codeDupCnt = 0;
			for(k = 0; k < codeList.length; k++){

				if(codeList[i].value == codeList[k].value){
					codeDupCnt++;

					if(codeDupCnt > 1){
						codeDupYn = true;
						break;
					}
				}
			}
		}

		if(codeDupYn){
			alert("중복된 코드가 존재합니다.");
			return;
		}

		if(confirm("수정하시겠습니까?")){
			$("#aform").attr({action:"/admin/code/updateCodeMgt.do", method:'post'}).submit();
		}
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/code/selectPageListCodeMgt.do">
					<input type="hidden" name="group_id" id="group_id" value="${param.group_id }"/>
					<input type="hidden" name="new_srt_ord_val" id="new_srt_ord_val" value="${param.LOW_SORT_ORDR}"/>
					<!--페이징조건-->
					<input type = "hidden" name = "currentPage" value ="${param.currentPage }"/>

					<div class="card card-info card-outline">
						<div class="card-header">
							<h3 class="card-title">코드 그룹 및 코드 관리</h3>
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
												<div class="outBox1">
													${resultMap.GROUP_ID}
												</div>
											</td>
										</tr>
										<tr>
											<th class="required_field">그룹코드명</th>
											<td>
												<div class="outBox1">
													<input type="text" class="form-control" id="group_nm" name="group_nm" placeholder="그룹코드명" value="${resultMap.GROUP_NM }" onkeyup="cfLengthCheck('그룹코드명은', this, 50);">
												</div>
											</td>
											<th>영문그룹코드명</th>
											<td>
												<div class="outBox1">
													<input type="text" class="form-control" id="group_nm_eng" name="group_nm_eng" placeholder="영문그룹코드명" value="${resultMap.GROUP_NM_ENG }" onkeyup="cfLengthCheck('영문그룹코드명은', this, 50);">
												</div>
											</td>
										</tr>
										<tr>
											<th>비고</th>
											<td colspan="3">
												<div class="outBox1">
													<textarea class="form-control" rows="3" name="rm" placeholder="비고" onkeyup="cfLengthCheck('비고는', this, 2000);">${resultMap.RM }</textarea>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<br>
							<div class="table-responsive">
								<table id="codeTable" class="table text-nowrap">
									<colgroup>
										<col width="100px">
										<col width="*">
										<col width="15%">
										<col width="15%">
										<col width="15%">
										<col width="15%">
										<col width="100px">
										<col width="100px">
										<col width="100px">
									</colgroup>
									<tr>
										<th class="text-center required_field">코드</th>
										<th class="text-center required_field">코드 명</th>
										<th class="text-center">영문명</th>
										<th class="text-center">속성1</th>
										<th class="text-center">속성2</th>
										<th class="text-center">속성3</th>
										<th class="text-center">순서</th>
										<th class="text-center">사용여부</th>
										<th class="text-center">삭제</th>
									</tr>
									<c:forEach var="code" items="${resultList}" varStatus="status">
										<tr id="id_code_${status.count }">
											<td><input type="text" name="code" class="form-control" value="${code.CODE }" onkeyup="fnBlockKorean(this);" onkeydown="cfLengthCheck('코드는', this, 10);"/></td>
											<td><input type="text" name="code_nm" class="form-control" value="${code.CODE_NM }" onkeyup="cfLengthCheck('코드명은', this, 100);"/></td>
											<td><input type="text" name="code_nm_eng" class="form-control" value="${code.CODE_NM_ENG }" onkeyup="cfLengthCheck('코드영문명은', this, 100);"/></td>
											<td><input type="text" name="attrb_1" class="form-control" value="${code.ATTRB_1 }" onkeyup="cfLengthCheck('속성1은', this, 100);"/></td>
											<td><input type="text" name="attrb_2" class="form-control" value="${code.ATTRB_2 }" onkeyup="cfLengthCheck('속성2는', this, 100);"/></td>
											<td><input type="text" name="attrb_3" class="form-control" value="${code.ATTRB_3 }" onkeyup="cfLengthCheck('속성3은', this, 100);"/></td>
											<td><input type="text" name="sort_ordr" class="form-control" value="${code.SORT_ORDR }" /></td>
											<td>
												<select class="form-control" name="use_yn">
													<c:forEach var="ynCode" items="${ynCodeList}">
														<option value="${ynCode.CODE }" <c:if test="${code.USE_YN == ynCode.CODE }">selected</c:if>>${ynCode.CODE_NM }</option>
													</c:forEach>
												</select>
											</td>
											<td class="text-center"><button type="button" class="btn bg-gradient-danger btn-sm" onclick="fnRemoveLine('id_code_${status.count }');">삭제</button></td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnList(); return false;">목록</button>
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnInsertForm(); return false;">신규등록</button>
									<button type="button" class="btn bg-gradient-success" onclick="fnUpdate(); return false;">저장</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
