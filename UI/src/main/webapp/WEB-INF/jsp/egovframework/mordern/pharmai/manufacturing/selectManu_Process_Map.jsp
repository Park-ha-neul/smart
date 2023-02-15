<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[

	$(document).ready(function(e){
		var prjct_id = $('input[name=prjct_id]').val();
		var step_new = $('input[name=step_new]').val();

		var param = {
				'prjct_id' : prjct_id
		}

		if(step_new == 'Y'){
			$.ajax({
				url : '/pharmai/manufacturing/getApi2Ajax.do',
				type : 'post',
				dataType : 'json',
				data : param,
				async : false,
				success: function(response){
					console.log(response);
					var code = response.resultStats.list.code;
					if(code == "000"){
						/* checkboxRender */
						/* manuNameRender */
						/* manuMaterialRender */
						/* manuProcessRender */
						/* manuQuelityRender */
						var result = response.resultStats.list.result;
						var manuList= result["manufacturing list"];

						var checkTag  ="";
						var nameTag = "";
						var materialTag = "";
						var processTag = "";
						var qulityTag = "";

					 	for(key in manuList){
							checkTag += '<div class="divs"><span class="text-center"><input type="checkbox" name="filter" class="filter_'+ key + '" value="Y" checked="checked"></span><input type="hidden" name="checkYn" class="checkYn_' + key + '" value="Y"></div>';
							if((manuList.length -1) != key){
								checkTag += '<span style="width:3px;"></span>';
							}
							var manuNames = Object.keys(manuList[key])[0];


							nameTag+= ' <div class="divs"><span class="menu text-center">'+ manuNames +'</span><input type="hidden" name="process_name" value="'+ manuNames +'"></div>';
							if((manuList.length -1) != key){
								nameTag += '<span style="width:3px;">→</span>';
							}

							var manuValues = Object.values(manuList[key])[0];

							materialTag += '<div class="divs"><textarea name="input_material" cols="10" rows="7" class="text-center" readonly="readonly">'+ manuValues["material"] +'</textarea></div>'
							if((manuList.length -1) != key){
								materialTag += '<span style="width:3px;"></span>'
							}

							processTag += '<div class="divs"><textarea name="process_param" cols="11" rows="4" class="text-center" readonly="readonly">' + manuValues["processing"] + '</textarea></div>';
							if((manuList.length -1) != key){
								processTag += '<span style="width:3px;"></span>'
							}

							qulityTag += '<div class="divs"><textarea name="output_material" cols="11" rows="5" class="text-center" readonly="readonly">'+ manuValues["quaility"]  + '</textarea></div>';
							if((manuList.length -1) != key){
								qulityTag += '<span style="width:3px;"></span>'
							}

						}

						$('#checkboxRender').html(checkTag);
						$('#manuNameRender').html(nameTag);
						$('#manuMaterialRender').html(materialTag);
						$('#manuProcessRender').html(processTag);
						$('#manuQuelityRender').html(qulityTag);

					}else{

					}
				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}

			});
	 	}



		if(${processList == null}){
			if($("[name=filter]").val() == "Y" ){
				$('[name=filter]').attr("checked", true);
			}else{
				$('[name=filter]').attr("checked", false);

			}

		}

		var filterLength = $('[name=filter]').length;

		$('[name=filter]').change(function(){
			var st = $('[name=filter]').index(this);
			var chk = $(this).is(":checked");
			if(chk){
				$(".checkYn_" + st).val("Y");
			}else{
				$(".checkYn_" + st).val("N");

			}

		})


	});



//]]>
</script>

<style>

	.divs{
		width:10%;
	}

	.menu{
		border: 1px solid black;
		padding: 10px 20px;
		border-radius: 15% 15% 15% 15%;
	}

</style>

<jsp:include page="header.jsp" flush="false"/>

<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" name="aform" id="aform" method="post" action="">
					<input type="hidden" id="status" name="status" value="02"/>
					<input type="hidden" name="prjct_id" id="prjct_id" value ="<%=param.getString("prjct_id")%>">
					<input type="hidden" name="projectName" id="projectName" value ="">
 					<input type="hidden" name="step_new" value="<%=param.getString("step_new") %>">
 					<input type="hidden" name="next_data" value="<%=param.getString("next_data") %>">
					<div class="card card-info card-outline">
						<div class="card-header">
<!-- 							<h4>High shear wet granulation Process Map</h4> -->
							<h4>Process Map</h4>
						</div>
						<div class="card-body">
							<c:if test="${processList != null }">
								<div class="row form-group text-center" style="margin-top: 10px;">
									<c:forEach var="item" items="${processList }" varStatus="status">
										<c:if test="${item.CHECK_YN == 'Y' }">
											<div class="divs"><span class="text-center"><input type="checkbox" name="filter" class="filter_${status.index }" value="${item.CHECK_YN }" checked="checked"></span><input type="hidden" name="checkYn" class="checkYn_${status.index }" value="${item.CHECK_YN }"></div>
										</c:if>

										<c:if test="${item.CHECK_YN == 'N' }">
											<div class="divs"><span class="text-center"><input type="checkbox" name="filter" class="filter_${status.index }" value="${item.CHECK_YN }"></span><input type="hidden" name="checkYn" class="checkYn_${status.index }" value="${item.CHECK_YN }"></div>
										</c:if>

										<c:if test="${(processList.size() - status.index) != 1 }">
											<span style="width:3px;"></span>
										</c:if>

									</c:forEach>
								</div>
									<div class="row form-group text-center" style="margin-top: 30px;">
										<c:forEach var="item" items="${processList }" varStatus="status">
											<div class="divs"><span class="menu text-center">${item.PROCESS_NAME }</span><input type="hidden" name="process_name" value="${item.PROCESS_NAME }"></div>
											<c:if test="${(processList.size() - status.index) != 1 }">
												<span style="width:3px;">→</span>
											</c:if>
										</c:forEach>
									</div>

									<div class="row form-group text-center" style="margin-top: 30px;">
										<c:forEach var="item" items="${processList }" varStatus="status">
											<div class="divs"><textarea name="input_material" cols="10" rows="7" class="text-center" readonly="readonly">${item.INPUT_MATERIAL }</textarea></div>
											<c:if test="${(processList.size() - status.index) != 1 }">
												<span style="width:3px;"></span>
											</c:if>
										</c:forEach>
									</div>

									<div class="row form-group text-center" style="margin-top: 30px;">
										<c:forEach var="item" items="${processList }" varStatus="status">
											<div class="divs"><textarea name="process_param" cols="11" rows="4" class="text-center" readonly="readonly">${item.PROCESS_PARAM }</textarea></div>
											<c:if test="${(processList.size() - status.index) != 1 }">
												<span style="width:3px;"></span>
											</c:if>
										</c:forEach>
									</div>

									<div class="row form-group text-center" style="margin-top: 30px;">
										<c:forEach var="item" items="${processList }" varStatus="status">
											<div class="divs"><textarea name="output_material" cols="11" rows="5" class="text-center" readonly="readonly">${item.OUTPUT_MATERIAL }</textarea></div>
											<c:if test="${(processList.size() - status.index) != 1 }">
												<span style="width:3px;"></span>
											</c:if>
										</c:forEach>
									</div>
							</c:if>

							<c:if test="${processList == null }">

								<div id="checkboxRender" class="row form-group text-center" style="margin-top: 30px;">

								</div>

								<div id="manuNameRender" class="row form-group text-center" style="margin-top: 30px;">

								</div>

								<div id="manuMaterialRender" class="row form-group text-center ">

								</div>

								<div id="manuProcessRender" class="row form-group text-center ">

								</div>

								<div id="manuQuelityRender" class="row form-group text-center ">

								</div>

							</c:if>

						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>