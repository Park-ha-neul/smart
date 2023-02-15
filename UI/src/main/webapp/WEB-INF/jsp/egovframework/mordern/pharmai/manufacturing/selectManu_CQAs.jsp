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
				url : '/pharmai/manufacturing/getApi5Ajax.do',
				type : 'post',
				dataType : 'json',
				data : param,
				async : false,
				success: function(response){

					var code = response.resultStats.resultData.code;
					if(code == "000"){

						var result = response.resultStats.resultData.result;
						var cqas_list = result["cqas_list"];
						var cqas = cqas_list[0].split(",");
						console.log
						var tag = "";

						for(var i =0; i < cqas.length; i++){
							var cqa_nm = cqas[i].trim();

							tag+= '<tr>';
							tag+= '	<td class="checkbox_outline"><input type="checkbox" name="filter" value=""></td>';
							tag+= '	<td class="text_outline"><span>'+cqa_nm+'</span><input type="hidden" name="cqas_name" value="'+cqa_nm+'"></td>';
							tag+= '	<input type="hidden" name="checkYn" value="N"/>';
							tag+= '</tr>';

						}

						$("#cqaList").append(tag);





					}else{

					}
				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}

			});
	 	}


		$("input[name=filter]").click(function(){

			var chk = $(this).is(":checked");
			var checkYn = $(this).closest('tr').find("input[name=checkYn]");

			if(chk){
				checkYn.val("Y");
			}else{
				checkYn.val("N");
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
				<form role="form" id="aform" method="post" action="">
					<input type="hidden" name="status" value="07">
					<input type="hidden" name="prjct_id" value="<%=param.getString("prjct_id") %>">
					<input type="hidden" name="step_new" value="<%=param.getString("step_new") %>">
					<input type="hidden" name="next_data" value="<%=param.getString("next_data") %>">

					<div class="card card-info card-outline">
						<div class="card-header">
							<h4>CQAs 선택</h4>
						</div>
						<div class="card-body">
							<div class="row">
								<div class="col-lg-12">
									<div class="table-responsive">
										<table class="table text-nowrap table-hover" id="cqasTable">
											<thead>
												<tr>
													<th></th>
													<th>CQAs List</th>
												</tr>
											</thead>
											<c:if test="${cqaList == null }">
												<tbody id="cqaList">

												</tbody>
											</c:if>

											<c:if test="${cqaList != null }">
												<tbody>
												<c:forEach var="item" items="${cqaList }">
													<tr>
														<td>
															<c:if test="${item.CHECK_YN == 'Y' }">
																<input type="checkbox" name="filter" value="${item.CHECK_YN }" checked>
															</c:if>
															<c:if test="${item.CHECK_YN == 'N' }">
																<input type="checkbox" name="filter" value="${item.CHECK_YN }">
															</c:if>
														</td>

														<td>${item.CQA_NM }</td>

														<input type="hidden" name="checkYn" value="${item.CHECK_YN }">
														<input type="hidden" name="cqas_name" value="${item.CQA_NM }">
													</tr>

												</c:forEach>

												</tbody>


											</c:if>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>