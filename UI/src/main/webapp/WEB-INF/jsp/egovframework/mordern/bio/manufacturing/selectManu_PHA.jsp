<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[

	$(function(){
		var step_new = $('input[name=step_new]').val();
		var prjct_id = $('input[name=prjct_id]').val();

		var param = {
				"prjct_id" : '<%=param.getString("prjct_id") %>'
		}

		if(step_new == "Y"){

			$(window).ajaxStart(function(){
				$(".loading-container").show();
			})
			.ajaxStop(function(){
				$(".loading-container").hide();
			});

			$.ajax({
				url: "/pharmai/bio/manufacturing/getApi3Ajax.do",
				type: 'post',
				dataType : 'json',
				async : false,
				data: param,
				success: function(response){

					var code = response.resultStats.resultData.code;
					var msg = response.resultStats.resultData.msg;
					var result = response.resultStats.resultData.result;

					if(code == "000"){
					 	var guideList = result["guide"];

						var header_tag ="";
						header_tag += '<tr>';
						header_tag += '<th>FP CQAs</th>';
							for(var i = 0; i < Object.keys(guideList).length; i++){
								header_tag+= '<th class="text-center">'+Object.keys(guideList)[i]+'</th>';
							}
						header_tag += '</tr>';
						header_tag += '<input type="hidden" name="cqas" value="CQAs"/>';
							for(var i = 0; i < Object.keys(guideList).length; i++){
								header_tag += '<input type="hidden" name="pha_col_'+(i+1)+'" value="'+Object.keys(guideList)[i]+'"/>';
							}
						$("#cqas_header").html(header_tag);

						var guideList = Object.keys(guideList);
						var tag = '';

						//api 랜더링.
						var cqas = result.guide[guideList[0]];
						var cqasArr = new Array();
						for(key in cqas){
							cqasArr.push(Object.keys(cqas[key])[0]);
						}

						for(var i = 0; i < cqasArr.length; i++){
							tag += '<tr>';
							tag += '	<td>' + cqasArr[i] + '</td>';
							tag += '	<input type="hidden" name="cqaStatus" value="'+cqasArr[i]+'" />';

							for(var j = 0; j < guideList.length; j++){
								 var cqaName = guideList[j];
								 var row = result.guide[cqaName];
								 /* tag += '<td>' + Object.values(result.guide[cqaName][i]) + '</td>'; */
								 tag += '<td class="text-center">';

								if(Object.values(result.guide[cqaName][i]) == '낮음'){
									tag += '<select name="PHA_ICON_'+(j+1)+'" class="form-controls2 text-center colorStatus">';
									tag += '		<option value="">선택</option>';
									tag += '		<option value="L" selected="selected">낮음</option>';
									tag += '		<option value="M">중간</option>';
									tag += '		<option value="H">높음</option>';
									tag += '</td>';
								}

								if(Object.values(result.guide[cqaName][i]) == '중간'){
									tag += '<select name="PHA_ICON_'+(j+1)+'" class="form-controls2 text-center colorStatus">';
									tag += '		<option value="">선택</option>';
									tag += '		<option value="L">낮음</option>';
									tag += '		<option value="M" selected="selected">중간</option>';
									tag += '		<option value="H">높음</option>';
									tag += '</td>';
								}

								if(Object.values(result.guide[cqaName][i]) == '높음'){
									tag += '<select name="PHA_ICON_'+(j+1)+'" class="form-controls2 text-center colorStatus">';
									tag += '		<option value="">선택</option>';
									tag += '		<option value="L">낮음</option>';
									tag += '		<option value="M">중간</option>';
									tag += '		<option value="H" selected="selected">높음</option>';
									tag += '</td>';
								}
							}

							tag += '</tr>';
						}

						$("#cqas_body1").append(tag);

					// --끝

					}
				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}
			})

		}

		//th 갯수구하기
		var th_length = $("#manu_table >thead th").length -1;
		$("[name=th_length]").val(th_length);

		$(".colorStatus").each(function(index, item){

			if($(this).val() == "H"){
			 	$(this).css("background", "red");
				$(this).css("color", "white");

				$('.colorStatus').find("option").each(function(i){
					$('.colorStatus option:eq("' + i + '")').css("background", "#666666");
					$('.colorStatus option:eq("' + i + '")').css("color", "white");
				});


			} else if ($(this).val() == "M") {
				$(this).css("background", "orange");
				$(this).css("color", "white");

				$('.colorStatus').find("option").each(function(i){
					$('.colorStatus option:eq("' + i + '")').css("background", "#666666");
					$('.colorStatus option:eq("' + i + '")').css("color", "white");
				});

			} else if ($(this).val()== "L") {
				$(this).css("background", "ForestGreen");
				$(this).css("color", "white");

				$('.colorStatus').find("option").each(function(i){
					$('.colorStatus option:eq("' + i + '")').css("background", "#666666");
					$('.colorStatus option:eq("' + i + '")').css("color", "white");
				});

			} else {
				$(this).css("color", "#ffffff");
				$(this).css("background", "#444444");
			}

		});

		$('.colorStatus').on({'change' : function(){

				var click_id = $(this).attr('id');

				if($("option:selected", this).val() == "H"){
				 	$(this).css("background", "red");
					$(this).css("color", "white");

					$('.colorStatus').find("option").each(function(i){
						$('.colorStatus option:eq("' + i + '")').css("background", "#666666");
						$('.colorStatus option:eq("' + i + '")').css("color", "white");
					});



				} else if ($("option:selected", this).val() == "M") {
 					$(this).css("background", "orange");
					$(this).css("color", "white");

					$('.colorStatus').find("option").each(function(i){
						$('.colorStatus option:eq("' + i + '")').css("background", "#666666");
						$('.colorStatus option:eq("' + i + '")').css("color", "white");
					});
				} else if ($("option:selected", this).val() == "L") {
 					$(this).css("background", "ForestGreen");
					$(this).css("color", "white");

					$('.colorStatus').find("option").each(function(i){
						$('.colorStatus option:eq("' + i + '")').css("background", "#666666");
						$('.colorStatus option:eq("' + i + '")').css("color", "white");
					});
				} else {
					alert("선택된 값이 없습니다.")
					$(this).css("color", "#ffffff");
					$(this).css("background", "#444444");
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
				<form role="form" id="aform" method="post" action="">
					<input type="hidden" name="status" value="03">
					<input type="hidden" name="prjct_id" value="<%=param.getString("prjct_id") %>">
					<input type="hidden" name="step_new" value="<%=param.getString("step_new") %>">
					<input type="hidden" name="next_data" value="<%=param.getString("next_data") %>">
					<input type="hidden" id="th_length" name="th_length" value ="">
					<div class="card card-info card-outline">
						<div class="card-header">
							<h5>PHA 테이블</h5>
						</div>
						<div class="card-body">
							<div class="col-lg-12">
		<%-- 						<c:if test="${pHaList != null }">
									<div class="mb30">
										<h3>&lt; CQAs 입력 예시 &gt;</h3>
										<img alt="CQAs 이미지 사진" style="width: 70%; text-align:center;"  src="/common/images/guid_ex2.JPG">
									</div>
								</c:if> --%>
								<div class="table-responsive">
									<c:if test="${pHaList != null }">
										<h3>&lt; 저장된 PHA 데이터 &gt;</h3>
									</c:if>
									<table class="table text-nowrap table-hover" id="manu_table">
										<c:if test="${pHaList != null }">
											<thead>
												<c:forEach var="item" items="${pHaList}">
													<c:if test="${item.TYPE == 'H' }">
														<tr>
																<th class="text-center">${item.FP_CQAS_STATUS }</th>
																<c:if test="${!empty item.PHA_COL_1}">
																	<th class="text-center">${item.PHA_COL_1 }</th>
																</c:if>
																<c:if test="${!empty item.PHA_COL_2}">
																	<th class="text-center">${item.PHA_COL_2 }</th>
																</c:if>
																<c:if test="${!empty item.PHA_COL_3}">
																	<th class="text-center">${item.PHA_COL_3 }</th>
																</c:if>
																<c:if test="${!empty item.PHA_COL_4}">
																	<th class="text-center">${item.PHA_COL_4 }</th>
																</c:if>
																<c:if test="${!empty item.PHA_COL_5}">
																	<th class="text-center">${item.PHA_COL_5 }</th>
																</c:if>
																<c:if test="${!empty item.PHA_COL_6}">
																	<th class="text-center">${item.PHA_COL_6 }</th>
																</c:if>
																<c:if test="${!empty item.PHA_COL_7}">
																	<th class="text-center">${item.PHA_COL_7 }</th>
																</c:if>
																<c:if test="${!empty item.PHA_COL_8}">
																	<th class="text-center">${item.PHA_COL_8 }</th>
																</c:if>
														</tr>

														<input type="hidden" name="cqas" value="${item.FP_CQAS_STATUS }">
														<input type="hidden" name="pha_col_1" value="${item.PHA_COL_1 }">
														<input type="hidden" name="pha_col_2" value="${item.PHA_COL_2 }">
														<input type="hidden" name="pha_col_3" value="${item.PHA_COL_3 }">
														<input type="hidden" name="pha_col_4" value="${item.PHA_COL_4 }">
														<input type="hidden" name="pha_col_5" value="${item.PHA_COL_5 }">
														<input type="hidden" name="pha_col_6" value="${item.PHA_COL_6 }">
														<input type="hidden" name="pha_col_7" value="${item.PHA_COL_7 }">
														<input type="hidden" name="pha_col_8" value="${item.PHA_COL_8 }">
													</c:if>
												</c:forEach>

											</thead>

											<tbody>
												<c:forEach var="item" items="${pHaList }" varStatus="index">
													<c:if test="${item.TYPE == 'V' }">
														<tr>
															<td class="text-center">${item.FP_CQAS_STATUS }</td>
															<input type="hidden" name="cqaStatus" value="${item.FP_CQAS_STATUS }">

															<c:if test="${!empty item.PHA_COL_1}">
																<td class="text-center">
																	<select name="PHA_ICON_1" class="form-controls2 text-center colorStatus">
																		<c:if test="${item.PHA_COL_1 == 'L'}">
																			<option value="">선택</option>
																			<option value="L" selected="selected">낮음</option>
																			<option value="M">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_1 == 'M'}">
																			<option value="">선택</option>
																			<option value="L" >낮음</option>
																			<option value="M" selected="selected">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_1 == 'H'}">
																			<option value="">선택</option>
																			<option value="L">낮음</option>
																			<option value="M">중간</option>
																			<option value="H" selected="selected">높음</option>
																		</c:if>
																	</select>
																</td>
															</c:if>

															<c:if test="${!empty item.PHA_COL_2}">
																<td class="text-center">
																	<select name="PHA_ICON_2" class="form-controls2 text-center colorStatus">
																		<c:if test="${item.PHA_COL_2 == 'L'}">
																			<option value="">선택</option>
																			<option value="L" selected="selected">낮음</option>
																			<option value="M">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_2 == 'M'}">
																			<option value="">선택</option>
																			<option value="L" >낮음</option>
																			<option value="M" selected="selected">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_2 == 'H'}">
																			<option value="">선택</option>
																			<option value="L">낮음</option>
																			<option value="M">중간</option>
																			<option value="H" selected="selected">높음</option>
																		</c:if>
																	</select>
																</td>
															</c:if>

															<c:if test="${!empty item.PHA_COL_3}">
																<td class="text-center">
																	<select name="PHA_ICON_3" class="form-controls2 text-center colorStatus">
																		<c:if test="${item.PHA_COL_3 == 'L'}">
																			<option value="">선택</option>
																			<option value="L" selected="selected">낮음</option>
																			<option value="M">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_3 == 'M'}">
																			<option value="">선택</option>
																			<option value="L" >낮음</option>
																			<option value="M" selected="selected">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_3 == 'H'}">
																			<option value="">선택</option>
																			<option value="L">낮음</option>
																			<option value="M">중간</option>
																			<option value="H" selected="selected">높음</option>
																		</c:if>
																	</select>
																</td>
															</c:if>

															<c:if test="${!empty item.PHA_COL_4}">
																<td class="text-center">
																	<select name="PHA_ICON_4" class="form-controls2 text-center colorStatus">
																		<c:if test="${item.PHA_COL_4 == 'L'}">
																			<option value="">선택</option>
																			<option value="L" selected="selected">낮음</option>
																			<option value="M">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_4 == 'M'}">
																			<option value="">선택</option>
																			<option value="L" >낮음</option>
																			<option value="M" selected="selected">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_4 == 'H'}">
																			<option value="">선택</option>
																			<option value="L">낮음</option>
																			<option value="M">중간</option>
																			<option value="H" selected="selected">높음</option>
																		</c:if>
																	</select>
																</td>
															</c:if>

															<c:if test="${!empty item.PHA_COL_5}">
																<td class="text-center">
																	<select name="PHA_ICON_5" class="form-controls2 text-center colorStatus">
																		<c:if test="${item.PHA_COL_5 == 'L'}">
																			<option value="">선택</option>
																			<option value="L" selected="selected">낮음</option>
																			<option value="M">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_5 == 'M'}">
																			<option value="">선택</option>
																			<option value="L" >낮음</option>
																			<option value="M" selected="selected">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_5 == 'H'}">
																			<option value="">선택</option>
																			<option value="L">낮음</option>
																			<option value="M">중간</option>
																			<option value="H" selected="selected">높음</option>
																		</c:if>
																	</select>
																</td>
															</c:if>

															<c:if test="${!empty item.PHA_COL_6}">
																<td class="text-center">
																	<select name="PHA_ICON_6" class="form-controls2 text-center colorStatus">
																		<c:if test="${item.PHA_COL_6 == 'L'}">
																			<option value="">선택</option>
																			<option value="L" selected="selected">낮음</option>
																			<option value="M">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_6 == 'M'}">
																			<option value="">선택</option>
																			<option value="L" >낮음</option>
																			<option value="M" selected="selected">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_6 == 'H'}">
																			<option value="">선택</option>
																			<option value="L">낮음</option>
																			<option value="M">중간</option>
																			<option value="H" selected="selected">높음</option>
																		</c:if>
																	</select>
																</td>
															</c:if>

															<c:if test="${!empty item.PHA_COL_7}">
																<td class="text-center">
																	<select name="PHA_ICON_7" class="form-controls2 text-center colorStatus">
																		<c:if test="${item.PHA_COL_7 == 'L'}">
																			<option value="">선택</option>
																			<option value="L" selected="selected">낮음</option>
																			<option value="M">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_7 == 'M'}">
																			<option value="">선택</option>
																			<option value="L" >낮음</option>
																			<option value="M" selected="selected">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_7 == 'H'}">
																			<option value="">선택</option>
																			<option value="L">낮음</option>
																			<option value="M">중간</option>
																			<option value="H" selected="selected">높음</option>
																		</c:if>
																	</select>
																</td>
															</c:if>

															<c:if test="${!empty item.PHA_COL_8}">
																<td class="text-center">
																	<select name="PHA_ICON_8" class="form-controls2 text-center colorStatus">
																		<c:if test="${item.PHA_COL_8 == 'L'}">
																			<option value="">선택</option>
																			<option value="L" selected="selected">낮음</option>
																			<option value="M">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_8 == 'M'}">
																			<option value="">선택</option>
																			<option value="L" >low</option>
																			<option value="M" selected="selected">중간</option>
																			<option value="H">높음</option>
																		</c:if>

																		<c:if test="${item.PHA_COL_8 == 'H'}">
																			<option value="">선택</option>
																			<option value="L">low</option>
																			<option value="M">중간</option>
																			<option value="H" selected="selected">높음</option>
																		</c:if>
																	</select>
																</td>
															</c:if>

														</tr>
													</c:if>
												</c:forEach>
											</tbody>
										</c:if>


										<c:if test="${ pHaList == null }">
<!-- 											<div class="mb30">
												<h3>&lt; CQAs 입력 예시 &gt;</h3>
												<img alt="CQAs 이미지 사진" style="width: 70%; text-align: center;"  src="/common/images/guid_ex2.JPG">
											</div> -->
											<div class="loading-container" style="position: absolute; left: 50%; top: 50%;">
								    			<div class="loading"></div>
								    			<div id="loading-text"><h5>데이터 조회중</h5><h6>(최대 5분  소요)</h6></div>
											</div>
											<h3>&lt; api에서 호출된 PHA 데이터 &gt;</h3>
											<thead id="cqas_header">

											</thead>
											<tbody id="cqas_body1">

											</tbody>
										</c:if>
									</table>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>