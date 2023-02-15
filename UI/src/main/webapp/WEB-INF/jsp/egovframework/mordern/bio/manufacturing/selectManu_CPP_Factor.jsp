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
				url : '/pharmai/bio/manufacturing/getApi4_1Ajax.do',
				type : 'post',
				dataType : 'json',
				data : param,
				async : false,
				success: function(response){
					console.log(response);
					var code = response.resultStats.resultData.code;
					var msg = response.resultStats.resultData.msg;
					if(code == "000"){

						var result = response.resultStats.resultData.result;
						var factor = result["factor"][0];
						var factorLen = Object.keys(factor).length;
						var tag="";

						var temp = 0;
						for(var i =0; i < factorLen; i++){
							var tag="";

							for(var j = 0 +temp; j < Object.values(factor)[i].length; j++){

								tag+= '<tr class="text-center">';
								tag+= '	<td><input type="checkbox" name="filter"></td>';
								tag+= '	<td>'+Object.keys(factor)[i]+'</td>';
								tag+= '	<td>'+Object.values(factor)[i][j]+'</td>';
								tag+= '	<td>낮음/높음</td>';
								tag+= '	<td><input type="text"  name="startRange" class="text-center" value="" size="3" readonly /> ~ <input type="text" name="endRange" class="text-center" value="" size="3" readonly />&nbsp<span>%</span></td>';
								tag+= '	<input type="hidden" name="process" value = "'+Object.keys(factor)[i]+'" />';
								tag+= '	<input type="hidden" name="cpp_factor" value = "'+Object.values(factor)[i][j]+'" />';
								tag+= ' <input type="hidden" name="use_range_s" value = "낮음" />';
								tag+= ' <input type="hidden" name="use_range_e" value ="높음" />';
								tag+= '	<input type="hidden" name="checkYn" value="N" />';
								tag+= '<tr/>';

							}

							$("#renderExcipient").append(tag);
						}





					}else{
						alert(msg);
					}
				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}

			});
	 	}

		$("input[name=filter]").click(function(){

			var count = $("input:checked[name='filter']").length;

			if(count > 3){
				$(this).prop("checked", false);
				alert("선택은 3개까지만 할 수 있습니다.");
				return;
			}

			var chk = $(this).is(":checked");

			var startRange = $(this).closest('tr').find("input[name=startRange]");
			var endRange = $(this).closest('tr').find("input[name=endRange]");
			var filter = $(this).closest('tr').find("input[name=filter]");
			var checkYn = $(this).closest('tr').find("input[name=checkYn]");

			if(chk){
				filter.val("Y");
				checkYn.val("Y");
				startRange.prop("readonly", false);
				endRange.prop("readonly", false);
				//빈값 입력 안할 시 focus 를 위한 class 추가
				startRange.attr('class', 'readonlyFalse text-center');
				endRange.attr('class', 'readonlyFalse text-center');
			}else{
				filter.val("N");
				checkYn.val("N");
				startRange.prop("readonly", true);
				endRange.prop("readonly", true);
				//빈값 입력 안할 시 focus 를 위한 class 추가
				startRange.attr('class', 'readonlyTrue');
				endRange.attr('class', 'readonlyTrue');

				for(var i = 0; i< startRange.length; i++){
				 	startRange[i].value = "";
					endRange[i].value = "";
				}
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
				<form role="form" id="aform" name="aform" method="post" action="">
					<input type="hidden" id="status" name="status" value="06"/>
					<input type="hidden" name="step_new" value="<%=param.getString("step_new")%>"/>
					<input type="hidden" name="next_data" value="<%=param.getString("next_data")%>"/>
					<input type="hidden" name="prjct_id" value="<%=param.getString("prjct_id")%>"/>
					<input type="hidden" name="projectName" value ="">

					<div class="card card-info card-outline">
						<div class="card-header">
							<h4 class="card-title">Excipient Input</h4>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table id="excipientTable" class="table text-nowrap table-hover">
									<thead>
										<tr>
											<th></th>
											<th class="text-center">공정</th>
											<th class="text-center">CPP Factor</th>
											<th class="text-center">Use range</th>
											<th class="text-center">사용범위 입력</th>
										</tr>
									</thead>

									<c:if test="${cqaList == null }">
										<tbody id="renderExcipient">

										</tbody>
									</c:if>


									<c:if test="${cqaList != null }">
										<tbody>
											<c:forEach var="item" items="${cqaList }">
												<tr class="text-center">
													<td>
														<c:if test="${item.CHECK_YN == 'Y' }">
															<input type="checkbox" name="filter" value="${item.CHECK_YN }" checked>
														</c:if>
														<c:if test="${item.CHECK_YN == 'N' }">
															<input type="checkbox" name="filter" value="${item.CHECK_YN }">
														</c:if>
													</td>
													<td>${item.PROCESS }</td>
													<td>${item.CPP_FACTOR }</td>
													<td>${item.USE_RANGE_S } / ${item.USE_RANGE_E }</td>
													<td>
														<c:if test="${item.CHECK_YN == 'Y' }">
															<input type="text" name="startRange" class="text-center" value="${item.IPT_USE_RANGE_S }" size="3" /> ~ <input type="text" name="endRange" class="text-center" value="${item.IPT_USE_RANGE_E }" size="3" />&nbsp<span>%</span>
														</c:if>

														<c:if test="${item.CHECK_YN == 'N' }">
															<input type="text"  name="startRange" class="text-center" value="${item.IPT_USE_RANGE_S }" size="3" readonly="readonly"/> ~ <input type="text" name="endRange" class="text-center" value="${item.IPT_USE_RANGE_E }" size="3" readonly="readonly" />&nbsp<span>%</span>

														</c:if>
													</td>
													<input type="hidden" name="checkYn" value="${item.CHECK_YN }" />
													<input type="hidden" name="process" value="${item.PROCESS }" />
													<input type="hidden" name="cpp_factor" value="${item.CPP_FACTOR }" />
													<input type="hidden" name="use_range_s" value = "${item.USE_RANGE_S }" />
													<input type="hidden" name="use_range_e" value ="${item.USE_RANGE_E }" />
												</tr>
											</c:forEach>
										</tbody>
									</c:if>
								</table>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
