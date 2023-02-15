<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<jsp:useBean id="stp2_dtl" class="egovframework.framework.common.object.DataMap" scope="request"/>
<jsp:useBean id="stepData" class="egovframework.framework.common.object.DataMap" scope="request"/>
<script type="text/javascript">
//<![CDATA[

	$(function(){

		$(document).ready(function(e){
			var prjct_id = $('input[name=prjct_id]').val();
			var step_new = $('input[name=step_new]').val();
			var formulation_pjt_id = $('input[name=formulation_pjt_id]').val();
			var param = {
				'formulation_pjt_id' : formulation_pjt_id,
				'prjct_id' : prjct_id
			}

			if(step_new == 'Y'){
				$.ajax({
					url : '/pharmai/manufacturing/getApi1Ajax.do',
					type : 'post',
					dataType : 'json',
					data : param,
					async : false,
					success: function(response){
						console.log(response);
						var code = response.resultStats.resultData.code;
						if(code == "000"){
							var result = response.resultStats.resultData.result;

							var list = result["method"];

							var tag = "";
							tag += '<select class="select form-control inputType" id="manufacturing_method" name="manufacturing_method">';
							for(var i = 0; i < list.length; i++){
								tag += '<option value="' + list[i] + '">'+ list[i] +'</option>';

							}
							tag += '</select>';

							for(var i = 0; i < list.length; i++){
								tag += '<input type="hidden" name="nameList" value="'+list[i]+'">';

							}



							$("#selectRander").html(tag);

						}else{

						}
					},
					error : function(jqXHR, textStatus, thrownError){
						ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
					}

				});
		 	}

		});

	});

//]]>
</script>


<jsp:include page="header.jsp" flush="false"/>

<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" name="aform" id="aform" method="post" action="">
					<input type="hidden" id="status" name="status" value="01"/>
					<input type="hidden" name="prjct_id" value ="<%=param.getString("prjct_id") %>">
					<input type="hidden" name="projectName" value ="">
					<input type="hidden" name="step_new" value="<%=param.getString("step_new") %>">
					<input type="hidden" name="next_data" value="<%=param.getString("next_data") %>">
					<input type="hidden" name="prjct_type" value="M"/>
					<input type="hidden" name="methodeArrLen" value="<%=param.getString("methodeArrLen") %>"/>
					<input type="hidden" name="formulation_pjt_id" value="<%=param.getString("formulation_pjt_id") %>" />
					<div class="card card-info card-outline">
						<div class="card-header">
							<h4>제조 방법 선택</h4>
						</div>
						<div class="card-body">
							<div class="row">

								<c:if test="${pjtData == null}">
									<div class="form-group col-lg-4">
										<label>투여경로</label>
										<div class="input-group">
											<input type="text" class="form-control" name="routes_type" value="<%=stp2_dtl.getString("ROUTES_TYPE")%>" readonly/>
										</div>
									</div>
									<div class="form-group col-lg-4">
										<label>제형</label>
										<div class="input-group">
											<input type="text" class="form-control" name="dosage_form_type" value="<%=stp2_dtl.getString("DOSAGE_FORM_TYPE")%>" readonly/>
										</div>
									</div>
								</c:if>

								<c:if test="${pjtData != null}">
									<div class="form-group col-lg-4">
									<label>투여경로</label>
									<div class="input-group">
										<c:forEach var="list" items="${stepDataList}">
											<c:if test="${'Y' eq list.CHECK_YN}">
												<input type="text" class="form-control" name="routes_type" value="${list.ROUTES_TYPE}" readonly/>
											</c:if>
										</c:forEach>
									</div>
								</div>
								<div class="form-group col-lg-4">
									<label>제형</label>
									<div class="input-group">
										<c:forEach var="list" items="${stepDataList}">
											<c:if test="${'Y' eq list.CHECK_YN}">
												<input type="text" class="form-control" name="dosage_form_type" value="${list.DOSAGE_FORM_TYPE}" readonly/>
											</c:if>
										</c:forEach>
									</div>
								</div>
								</c:if>

								<div class="form-group col-lg-4">
									<label>제조방법</label>
									<c:if test = "${pjtData == null}">
									<div class="input-group manufacturing_method" id="selectRander">

									</div>
									</c:if>

									<c:if test = "${pjtData != null}">
										<div class="input-group manufacturing_method">
											<select class="select form-control inputType" id="manufacturing_method" name="manufacturing_method">
												<c:forEach var="list" items="${stepDataList}">
													<option value="${list.MANUFACTURING_METHOD}" <c:if test="${'Y' eq list.CHECK_YN}">selected ="selected"</c:if>>${list.MANUFACTURING_METHOD}</option>
												</c:forEach>
											</select>
											<c:forEach var="list" items="${stepDataList}">
												<input type="hidden" name=nameList value="${list.MANUFACTURING_METHOD}" />
											</c:forEach>
										</div>
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