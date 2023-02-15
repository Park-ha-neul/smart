<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.framework.common.util.CommboUtil"%>
<%@ page import="egovframework.framework.common.object.DataMap"%>
<%@ page import="egovframework.framework.common.util.StringUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Util" uri="/WEB-INF/tlds/Util.tld"%>

<jsp:useBean id="excipientList"  type="java.util.List" class="java.util.ArrayList" scope="request"/>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<script type="text/javascript">
//<![CDATA[

	$(document).ready(function() {

		<%if(excipientList.size() > 0){%>
			$('#shape_recommendation').css("display", "block");
			$('#nodata').css("display", "none");
			$('#capacity_component').css("display", "block");
		<%}%>

		$('#routes').change(function(event){

			if($('#routes').val() == ""){
				$('#nodata').css("display", "block");
				$('#shape_recommendation').css("display", "none");

			}else{
				$('#capacity_component').css("display", "none");
				fnSelectRoutes();
			}
		});
	});

	$(document).on('click',"input[name='shape_list']",function(){
		var checkCnt = $("input[name='shape_list']:checked").length;

		if(checkCnt > 1) {
			alert("하나의 제형만 선택해주세요.");
			$('input[type="checkbox"][name="shape_list"]').prop('checked', false);
			$(this).prop("checked", true);
			$('#volume').val("");
			$('#unit').val("");
		}

		if(checkCnt > 0) {
			$('#capacity_component').css("display", "block");
			$("#unit option:eq(0)").prop("selected", true);
		} else {
			$('#capacity_component').css("display", "none");
		}

	});

//]]>
</script>

<jsp:include page="headerStep.jsp" flush="false"/>
<div class="content">
	<div class="container-fluid">
		<form role="form" id="aform" name="aform" method="post" action="">
			<input type="hidden" id="status" name="status" value="02"/>
			<input type="hidden" name="step_new" value="<%=param.getString("step_new")%>"/>
			<input type="hidden" name="next_data" value="<%=param.getString("next_data")%>"/>
			<input type="hidden" name="prjct_type" value="<%=param.getString("prjct_type")%>"/>
			<input type="hidden" name="prjct_id" value="<%=param.getString("prjct_id")%>"/>
			<input type="hidden" name="projectName" value ="">
			<div class="row">
				<div class="col-lg-6">
					<div class="card card-outline">
						<div class="card-header">
							<h4 class="card-title">투여 경로 선택</h4>
						</div>
						<div class="card-body">
							<div class="form-group">
								<label for="routesSelection">Routes of administration Selection</label>
								<div class="input-group">
									<select class="select form-control inputType" id="routes" name="routes">
										<option value="">투여경로 선택</option>
										<option value="oral" <%if(param.getString("routes").equals("oral")){%> selected <%}%>>Oral</option>
										<option value="parenteral" <%if(param.getString("routes").equals("parenteral")){%> selected <%}%>>Parenteral</option>
										<option value="local" <%if(param.getString("routes").equals("local")){%> selected <%}%>>Local</option>
									</select>
								</div>
								<div class="form-group mt-4">
									<label for="recommendationRoutes">추천 경로</label>
									<div class="input-group">
										<div class="table-responsive table-fixed">
											<table class="table text-nowrap table-hover" id="recommendationTableTbody">
												<colgroup>
													<col style="width:70%;">
													<col style="width:*%;">
												</colgroup>
												<thead>
													<tr class="projects td">
														<th class="text-center">Route</th>
														<th class="text-center">Count</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td class="text-center">Oral</td>
														<td class="text-center"><%=param.getString("oral_cnt")%></td>
													</tr>
													<tr>
														<td class="text-center">Parenteral</td>
														<td class="text-center"><%=param.getString("parenteral_cnt")%></td>
													</tr>
													<tr>
														<td class="text-center">Local</td>
														<td class="text-center"><%=param.getString("local_cnt")%></td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="card card-outline">
						<div class="card-header">
							<h4 class="card-title">제형 추천/ 선택</h4>
						</div>
						<div class="card-body center" id="nodata">
							<h2 class="nodate_default">No Data</h2>
						</div>
						<div class="card-body" id="shape_recommendation" style="display: none;">
							<div class="form-group">
								<div class="table-responsive-md table-fixed">
									<label for="pharmaceutical dosage form">Pharmaceutical Dosage</label>
									<table class="table table-bordered table-hover table-condensed" id="routeTableTbody">
										<colgroup>
											<col style="width:15%;">
											<col style="width:*%;">
										</colgroup>
										<thead>
											<tr class="projects td">
												<th></th>
												<th class="text-center">Select Dosage Form</th>
											</tr>
										</thead>
										<tbody>

										<%
											DataMap excipientMap = null;
											for(int i = 0; i < excipientList.size(); i++){
												excipientMap = (DataMap)excipientList.get(i);
										%>
											<tr class="">
												<td class="text-center"><input type="checkbox" name="shape_list" value="<%=excipientMap.getString("EXCIPIENT_NM") %>" <%if(excipientMap.getString("CHECK_YN").equals("Y")){%> checked="checked" <%}%>></td>
												<td class="text-left"><span><%=excipientMap.getString("EXCIPIENT_NM")%></span><input type="hidden" name="shape_name" value="<%=excipientMap.getString("EXCIPIENT_NM")%>"></td>
											</tr>
										<%
											}
										%>

										</tbody>
									</table>
								</div>
								<div class="input-group col-sm-12" id="capacity_component" style="display: none;">
									<h5 class="text-center" style="padding: 20px">성분 용량을
										입력해주세요.</h5>
									<div class="input-group mb-3">
										<input type="text" class="form-control col-sm-9 text-center"  placeholder="ex:125" aria-label="Recipient's username" aria-describedby="button-addon2" name="volume" id="volume" value="<%=param.getString("volume")%>">
										<select class="form-control inputType col-sm-3 " name="unit" id="unit">
											<option value="mg" selected <%if(param.getString("unit").equals("mg")){%> selected <%}%>>mg</option>
										</select>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>


<script type="text/javascript">

	function fnSelectRoutes(){

		$('#nodata').hide();
		$('#shape_recommendation').css("display", "block");
		$('#routeTableTbody tbody').html("");
		var inputType = $('#routes').val();

		var param = {
				'route' : inputType
				};

		$.ajax({
			url : '/pharmai/formulation/getApi2Ajax.do',
			type : 'post',
			dataType : 'json',
			data : param,
			async : false,
			success : function(response){
				var code = response.resultStats.list.code;
				if(code == "000"){
					var items = response.resultStats.list;
					var innerHtml = '';

					for(var i = 0; i < items.length; i++){
						var item = items[i];

						innerHtml += '<tr class="">';
						innerHtml += '	<td class="text-center"><input type="checkbox" name="shape_list" value="'+ item + '"></td>';
						innerHtml += '	<td class="text-left"><span>' + item + '</span><input type="hidden" name="shape_name" value="' + item + '"></td>';
						innerHtml += '</tr>';
					}

					$('#routeTableTbody tbody').append(innerHtml);
					$('#volume').val("");

				}else{
					var msg = response.resultStats.list.msg;
					alert(msg);
					return;
				}

				var items = response.resultStats.list.result["excipient list"];
				var innerHtml = '';

				for(var i = 0; i < items.length; i++){
					var item = items[i];

					innerHtml += '<tr class="">';
					innerHtml += '	<td class="text-center"><input type="checkbox" name="shape_list" value="'+ item + '"></td>';
					innerHtml += '	<td class="text-left"><span>' + item + '</span><input type="hidden" name="shape_name" value="' + item + '"></td>';
					innerHtml += '</tr>';
				}

				$('#routeTableTbody tbody').append(innerHtml);
				$('#volume').val("");
				$('#unit').val("");
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});

	}



</script>
