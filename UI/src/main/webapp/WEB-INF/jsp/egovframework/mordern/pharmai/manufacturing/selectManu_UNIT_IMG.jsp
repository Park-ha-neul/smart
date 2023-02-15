<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[

	$(document).ready(function(){


		$(document).on('click', 'input[name="filter"]', function(){
			$(".chk_yn").val("N");
			if($(this).prop('checked')){
				$('input[type="checkbox"][name="filter"]').prop('checked',false);
				$(this).prop('checked',true);

				var st = $('[name=filter]').index(this);
				$(".checkYn_" + st).val("Y");
			}

		})



		var step_new = $('input[name=step_new]').val();
		var prjct_id = $('input[name=prjct_id]').val();

		var param = {

			"prjct_id" : prjct_id,
			"common_path" : '/home/data/t3q/uploads/pharmAi/manufacturing/' + prjct_id +'/api4/fmea_img/'
		}

		if(step_new == "Y"){
			$.ajax({
				url: "/pharmai/manufacturing/getApi4Ajax.do",
				type: 'post',
				dataType : 'json',
				data: param,
				success: function(response){
					console.log(response);
					var code = response.resultStats.resultData.code;
					var msg = response.resultStats.resultData.msg;

					if(code == "000"){
						var result = response.resultStats.resultData.result;
						var process_img = result["process img"];

						var tag ="";

						for(key in process_img) {
							var cpp = Object.values(process_img[key])[0];
							var unitNames = Object.values(process_img[key])[1];
							var imgSrc = Object.values(process_img[key])[2][0];
							var risks = Object.values(process_img[key])[3];
							/* 경로 변경*/
							imgSrc = imgSrc.replace('/home/data/t3q/uploads/','/upload/');

							tag+= '<div class="row" style="border: 1px solid #999999;">';

							/*unit 시작 */
							tag+= '	<div class="col-4" style="margin-left: auto; margin-right: auto; margin-bottom: auto; margin-top: auto;">';

							tag+= '		<div class="card-body box-profile">';
							tag+= '			<input type="checkbox" name="filter" class="filter" value="N">'
							tag+= '			<input type="hidden" name="check_yn" class="chk_yn checkYn_'+ key +'" value="N">'
							tag+= '			<div class="profile-user-img img-fluid ">';
							tag+= '				<img class="center-block mw-100" src="'+imgSrc+'">';
							tag+= '				<input type="hidden" name="img_src" value="'+imgSrc+'">';
							tag+= '			</div>';

							tag+= '			<ul class="list-unstyled list-group mt-2" style="width: 250px; margin-left: auto; margin-right: auto;">';
							tag+= '				<li class="text-left list-group-item" style="font-weight: bold;">Unit : '+unitNames+'</li>';
							tag+= '				<input type="hidden" name="unit" value="'+unitNames+'"/>';
							tag+= '				<li class="text-left list-group-item" style="font-weight: bold;">CPP</li>';

												for(var i = 0; i < cpp.length; i++){
													tag+= '<li class="text-left list-group-item">- ' +cpp[i] +'</li>';
												}

							tag+= '			</ul>';
							tag+= '		</div>';
							tag+= '	</div>';

							/*risk 시작 */
							tag+= '	<div class="col-8 mb-2">';
							tag+= '		<h3 class="risk mt-2">Risks</h3>';
							tag+= '		<input type="hidden" name="cpp_length" value="'+Object.values(risks).length+'"/>';
									for(var j = 0; j < Object.values(risks).length; j++ ){
										tag+= '	<h3 class="mt-2">'+Object.keys(risks)[j]+'</h3>';
										tag+= '	<input type="hidden" name="cpp" value="'+Object.keys(risks)[j]+'"/>';
										tag+= '	<ul class="list-group">';
										for(var k = 0; k < Object.values(risks)[j].length; k++ ){
											tag+= '	<li class="list-group-item">- '+Object.values(risks)[j][k]+'</li>';
											tag+= '	<input type="hidden" name="risk_dtl" value="'+Object.values(risks)[j][k]+'" />';
										}
											tag+= '	<input type="hidden" name="risk_length" value="'+Object.values(risks)[j].length+'" />';
										tag+= '	</ul>';
									}


							tag+= '	</div>';
							tag+= '</div>';

						}



						$("#unit_img").append(tag);



					}else{
						alert(msg);
					}
				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}

			})
		}


	});


//]]>
</script>

<jsp:include page="header.jsp" flush="false"/>

<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="">
					<input type="hidden" name="status" value="05">
					<input type="hidden" name="prjct_id" value="<%=param.getString("prjct_id") %>">
					<input type="hidden" name="step_new" value="<%=param.getString("step_new") %>">
					<input type="hidden" name="next_data" value="<%=param.getString("next_data") %>">
					<div class="card card-info card-outline">
						<div class="card-header">
							<h4>유닛 공정 이미지</h4>
						</div>
						<div class="card-body">
							<div class="row">
								<div class="col-12" id="unit_img">
									<c:if test="${ stp05List !=null}">
										<c:forEach var="item" items="${distinctUnitList}" varStatus="status1">

											<div class="row" style="border: 1px solid #999999;">
												<div class="col-4" style="margin-left: auto; margin-right: auto; margin-bottom: auto; margin-top: auto;">
													<div class="card-body box-profile">
														<c:if test="${item.CHECK_YN == 'Y' }">
															<input type="checkbox" name="filter" class="filter" value="Y" checked="checked">
															<input type="hidden" name="check_yn" class="chk_yn checkYn_${status1.index }" value="Y">
														</c:if>
														<c:if test="${item.CHECK_YN == 'N' }">
															<input type="checkbox" name="filter" class="filter" value="N">
															<input type="hidden" name="check_yn" class="chk_yn checkYn_${status1.index }" value="N">
														</c:if>

														<div class="profile-user-img img-fluid ">
															<img class="center-block mw-100" src="${item.UNIT_PNG }">
															<input type="hidden" name="img_src" value="${item.UNIT_PNG }">
														</div>
														<ul class="list-unstyled list-group mt-2" style="width: 250px; margin-left: auto; margin-right: auto;">
															<li class="text-left list-group-item" style="font-weight: bold;">Unit : ${item.UNIT }</li>
															<input type="hidden" name="unit" value="${item.UNIT }"/>
															<li class="text-left list-group-item" style="font-weight: bold;">CPP</li>
															<c:forEach var="item2" items="${distinctCppList }" varStatus="status2">
																<c:if test="${item.UNIT == item2.UNIT }">
																	<li class="text-left list-group-item">${item2.CPP_TYPE }</li>
																</c:if>
															</c:forEach>

														</ul>
													</div>
												</div>

												<div class="col-8 mb-2">
													<h3 class="risk mt-2">Risks</h3>
													<input type="hidden" name="cpp_length" value="${status1.index + 1 }"/>
													<c:forEach var="item2" items="${distinctCppList }" varStatus="status2">
														<c:if test="${item.UNIT == item2.UNIT }">
															<h3 class="mt-2">${item2.CPP_TYPE }</h3>
															<input type="hidden" name="cpp" value="${item2.CPP_TYPE }"/>
															<ul class="list-group">
																<c:forEach var="item3" items="${stp05List }" varStatus="status3">
																	<c:if test="${item3.CPP_TYPE == item2.CPP_TYPE && item.UNIT == item3.UNIT }">
																		<li class="list-group-item"> - ${item3.RISK }</li>
																		<input type="hidden" name="risk_dtl" value="${item3.RISK} " />
																	</c:if>
																</c:forEach>
																<input type="hidden" name="risk_length" value="${status3.index + 1 }" />
															</ul>
														</c:if>
													</c:forEach>
												</div>
											</div>
										</c:forEach>
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