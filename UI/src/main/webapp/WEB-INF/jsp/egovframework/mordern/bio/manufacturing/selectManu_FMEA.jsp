<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[

	$(document).ready(function(){


		var step_new = $('input[name=step_new]').val();
		var prjct_id = $('input[name=prjct_id]').val();
		var common_path = $('input[name=common_path]').val();

		var param = {
			"prjct_id" : prjct_id,
			"common_path" : '/home/data/t3q/uploads/pharmAi/manufacturing/' + prjct_id +'/api4/fmea_img/'

		}

		if(step_new == "Y"){
			$.ajax({
				url: "/pharmai/bio/manufacturing/getApi4Ajax.do",
				type: 'post',
				dataType : 'json',
				data: param,
				async : false,
				success: function(response){
					console.log(response);
					var code = response.resultStats.resultData.code;
					var msg = response.resultStats.resultData.msg;

					if(code == "000"){
						var result = response.resultStats.resultData.result;
						var checkList = response.resultStats.resultData.result.check_list;
						var fmea = result["fmea"];
						var tag ="";

						for(key in fmea){
							for(key2 in checkList){
								if(Object.values(fmea[key])[0] == Object.keys(checkList[key2]) && Object.values(checkList[key2]) == 'x'){
									tag+= '<tr>'
										tag+= '<td class="text-center" name="Unit">'+Object.values(fmea[key])[0]+'</td>'
										tag+= '<td class="text-center" name="CPPs">'+Object.values(fmea[key])[3]+'</td>'
										tag+= '<td class="text-center" name="Failure_Mode">'+Object.values(fmea[key])[1]+'</td>'
										tag+= '<td class="text-center" name="IP_FP_CQAs">'+Object.values(fmea[key])[2]+'</td>'

										tag+= '<input type="hidden" name="riskStatus" value="N">';
										tag+= '<input type="hidden" name="Unit" value="'+Object.values(fmea[key])[0]+'"/>'
										tag+= '<input type="hidden" name="CPPs" value="'+Object.values(fmea[key])[3]+'" />'
										tag+= '<input type="hidden" name="Failure_Mode" value="'+Object.values(fmea[key])[1]+'"/>'
										tag+= '<input type="hidden" name="IP_FP_CQAs" value="'+Object.values(fmea[key])[2]+'"/>'

									tag+= '</tr>'


								}else if(Object.values(fmea[key])[0] == Object.keys(checkList[key2]) && Object.values(checkList[key2]) == 'o'){
									tag+= '<tr>'
										tag+= '<td class="text-center" name="Unit" style="background-color:#FF9090;">'+Object.values(fmea[key])[0]+'</td>'
										tag+= '<td class="text-center" name="CPPs">'+Object.values(fmea[key])[3]+'</td>'
										tag+= '<td class="text-center" name="Failure_Mode">'+Object.values(fmea[key])[1]+'</td>'
										tag+= '<td class="text-center" name="IP_FP_CQAs">'+Object.values(fmea[key])[2]+'</td>'

										tag+= '<input type="hidden" name="riskStatus" value="Y">';
										tag+= '<input type="hidden" name="Unit" value="'+Object.values(fmea[key])[0]+'"/>'
										tag+= '<input type="hidden" name="CPPs" value="'+Object.values(fmea[key])[3]+'" />'
										tag+= '<input type="hidden" name="Failure_Mode" value="'+Object.values(fmea[key])[1]+'"/>'
										tag+= '<input type="hidden" name="IP_FP_CQAs" value="'+Object.values(fmea[key])[2]+'"/>'

									tag+= '</tr>'
									break;
								}
							}


						}

						$('#FMEA_Table_body').append(tag);

					}else{
						alert(msg);
					}

				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}
			})

		}

		$(function(){
		    $('#forRowspan').each(function() {
		        var table = this;
		        $.each([1,2,3,4] /* 합칠 칸 번호 */, function(c, v) {
		            var tds = $('>tbody>tr>td:nth-child(' + v + ')', table).toArray(), i = 0, j = 0;
		            console.log(tds);
		            for(j = 1; j < tds.length; j ++) {
		                if(tds[i].innerHTML != tds[j].innerHTML) {
		                    $(tds[i]).attr('rowspan', j - i);
		                    i = j;
		                    continue;
		                }
		                $(tds[j]).hide();
		            }
		            j --;
		            if(tds[i].innerHTML == tds[j].innerHTML) {
		                $(tds[i]).attr('rowspan', j - i + 1);
		            }
		        });
		    });
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
					<input type="hidden" id="status" name="status" value="04"/>
					<input type="hidden" name="prjct_id" value ="<%=param.getString("prjct_id")%>">
					<input type="hidden" name="projectName" value ="<%=param.getString("projectName")%>">
 					<input type="hidden" name="step_new" value="<%=param.getString("step_new") %>">
 					<input type="hidden" name="next_data" value="<%=param.getString("next_data") %>">
 					<input type="hidden" name="common_path" value="<%=param.getString("common_path") %>">

					<div class="card card-info card-outline">
						<div class="card-header">
							<h4>FMEA 테이블</h4>
						</div>
						<div class="card-body">
							<div class="row">
								<div class="col-lg-12">
									<div class="table-responsive">
										<table class="table text-nowrap" id="forRowspan">
											<thead>
												<tr>
													<th class="text-center">Unit</th>
													<th class="text-center">CPPs</th>
													<th class="text-center">Failure Mode(Critical Event)</th>
													<th class="text-center">Effect on IP &amp; FP CQAs with respect to QTTP</th>
												</tr>
											</thead>
											<tbody>
												<c:if test="${resultList != null}">
													<tbody id="FMEA_Table_body">
														<c:forEach var="item" items="${resultList}" varStatus="status">
														<tr>
															<c:if test="${item.RISK_STATUS == 'Y' }">
																<td class="text-center" name="Unit" style="background-color:#FF9090; ">${item.FMEA_UNIT}</td>
															</c:if>
															<c:if test="${item.RISK_STATUS == 'N' }">
																<td class="text-center" name="Unit">${item.FMEA_UNIT}</td>
															</c:if>

															<td class="text-center" name="CPPs">${item.FMEA_CPPS}</td>
															<td class="text-center" name="Failure_Mode">${item.FMEA_FAILURE_MODE}</td>
															<td class="text-center" name="IP_FP_CQAs">${item.FMEA_CQAS_QTTP}</td>
														</tr>
														</c:forEach>
													</tbody>
												</c:if>
											</tbody>

											<c:if test="${ resultList == null }">
												<tbody id="FMEA_Table_body">

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