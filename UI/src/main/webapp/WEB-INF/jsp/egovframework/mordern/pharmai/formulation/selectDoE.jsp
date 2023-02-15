<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>

<script type="text/javascript">
	//<![CDATA[
	var target_Yn = false;
	var result_step_status;
	$(document).ready(function(){

		var step_new = $('input[name=step_new]').val();
		result_step_status = $('input[name=result_step_status]').val();

		if(step_new == 'N'){
			target_Yn = true;
			$('.loading-container').hide();
		}
		//nav 탭 전환
		$('.navALink').click(function(){
			var tab_id = $(this).attr("data-tab");
			$('.nav-item a').removeClass('active');
			$('.tab-content').css('display', 'none');
			$(this).addClass('active');
			$("#"+tab_id).css('display', 'block');


			if(tab_id == 'tab-5' && target_Yn == false) {
				$('.nav-item a').removeClass('active');
				$('.tab-content').css('display', 'none');
				$('.nav-item.tab-4 > a').addClass('active');
				$("#tab-4").css('display', 'block');
				$("#design_img").hide();
				alert("개별 응답 값에 대한 목표치 값을 입력해주세요.");
			}

		});

		var prjct_id = $('input[name=prjct_id]').val();
		var status = $('input[name=status]').val();
		var common_path = $('input[name=common_path]').val();
		var path = "/home/data/t3q/uploads/pharmAi/formulation/" + prjct_id +"/api6/csv/api6_experiment.csv" //하드코딩 수정.

		var param =
		{

			'experiment data' : path,
			'pareto-path' : '/home/data/t3q/uploads/pharmAi/formulation/' + prjct_id +  '/api6/tab-1/pareto/',
			'contour-path' : '/home/data/t3q/uploads/pharmAi/formulation/' + prjct_id + '/api6/tab-2/contour/',
			'response-path' : '/home/data/t3q/uploads/pharmAi/formulation/' + prjct_id + '/api6/tab-3/response/',
			'prjct_id': prjct_id,
			'status': status

		};

		if(step_new == 'Y'){

			$(window).ajaxStart(function(){
				$(".loading-container").show();
			})
			.ajaxStop(function(){
				$(".loading-container").hide();
			});

			$.ajax({
				url: "/pharmai/formulation/getApi6Ajax.do",
				type: 'post',
				data: param,
				dataType : 'json',
				async : false,
				success: function(response){

					var code = response.resultStats.resultData.code;
					if(code == "000"){
						var data = response.resultStats.resultData;
						var pareto = data.result["pareto"];
						var contour = data.result["contour"];
						var response = data.result["response"];
						var effects = data.result.effects;

						var tag = "";
						
						// globals.properties의 env 값 받아오기
						var system_env = data.system_env;
						console.log('api6번 system_env : ', system_env);
						
// 						기존
						for(var i = 0; i< pareto.length; i++){

							var splitUrl  = pareto[i].split('/');
							var urlLength = splitUrl.length;
							var pngName = splitUrl[urlLength-1];
							var splitName = pngName.split('.');
							var variable = splitName[0]

								tag +='	<div class="image mt-4 col-md-4">';
								// env에 따라 분기처리 (local인 경우는 replace를 진행할 필요가 없음)
								if (system_env == "LOCAL"){
									tag +='		<img style="width:80%;" src="'+ pareto[i];	
								}else{
									tag +='		<img style="width:80%;" src="'+ pareto[i].replace('/home/data/t3q/uploads/','/upload/') + '">';
								}
								tag +='		<div class="mt-2">';
								tag +='			<h4>'+variable+'</h4>';
								tag +='		</div>';
								tag +='	</div>';
							}

						$("#container_image_1").html(tag);

						var tag = "";
						for(var i = 0; i< contour.length; i++){

							var splitUrl  = contour[i].split('/');
							var urlLength = splitUrl.length;
							var pngName = splitUrl[urlLength-1];
							var pos = pngName.lastIndexOf(".");
							var variable = pngName.substring(pngName,pos);

								tag += '<div class="mt-4 col-md-4">';
								// env에 따라 분기처리 (local인 경우는 replace를 진행할 필요가 없음)
								if (system_env == "LOCAL"){
									tag += '	<img style="width:80%;" src="'+ contour[i]	
								}else{
									tag += '	<img style="width:80%;" src="'+ contour[i].replace('/home/data/t3q/uploads/','/upload/') +'">'
								}
								tag +='		<div class="mt-2">';
								tag +='			<h4>'+variable+'</h4>';
								tag +='		</div>';
								tag += '</div>';
							}

						$("#container_image_2").html(tag);

						var tag = "";
						for(var i = 0; i< response.length; i++){

							var splitUrl  = response[i].split('/');
							var urlLength = splitUrl.length;
							var pngName = splitUrl[urlLength-1];
							var pos = pngName.lastIndexOf(".");
							var variable = pngName.substring(pngName,pos);

								tag += '<div class="mt-4 col-md-4">';
								// env에 따라 분기처리 (local인 경우는 replace를 진행할 필요가 없음)
								if (system_env == "LOCAL"){
									tag += '	<img style="width:80%;" src="'+ response[i]	
								}else{
									tag += '	<img style="width:80%;" src="'+ response[i].replace('/home/data/t3q/uploads/','/upload/') +'">'
								}
								tag +='		<div class="mt-2">';
								tag +='			<h4>'+variable+'</h4>';
								tag +='		</div>';
								tag += '</div>';
							}

						$("#container_image_3").html(tag);

						var tag = "";
						var keys = Object.keys(effects); // 키 가져오기.
						var values = Object.values(effects); // value 가져오기.
						var rowCount = keys.length;
						var responseName = Object.values(values);
					    tag += '	<tr>';
					    tag += '		<th colspan="2">Responses (Effects)</th>';
					    tag += '		<th colspan="2">개별 응답 값에 대한 목표치</th>';
					    tag += '		<td rowspan=\"' + (rowCount + 1) + '\"><button type="button" class="select btn btn-grey" type="button" id="findSelect" name="findSelect">조회</button></td>';
					    tag += '	</tr>';
						for(var i = 0; i <keys.length; i++){
							tag += '<tr>';
							tag += '	<td>'+ Object.keys(values[i]) + '</td>';
							tag += '	<td>' + Object.keys(responseName[i]["Y" + (i+1)]) +'</td>';
							tag += '	<td colspan="2"><input type="text" class="target_score text-center" name="start_Y"  value="' + Object.values(Object.values(responseName)[i]["Y" + (i+1)])[0].min + '" size="15"/>~<input type="text" class="target_score text-center" name="end_Y" value="' + Object.values(Object.values(responseName)[i]["Y" + (i+1)])[0].max + '" size="15"/></td>';
							tag += '	<input type="hidden" name="y_variable" value="'+ Object.keys(values[i]) +'">';
							tag += '	<input type="hidden" name="effect" value="' + Object.keys(responseName[i]["Y" + (i+1)]) + '">';
							tag += '</tr>';


						}

						$("#effects_body").html(tag);

					}else{
						var prjct_id = $('input[name=prjct_id]').val();
						var msg = response.resultStats.resultData.msg;
						var redirectUrl = "/pharmai/chemical/formulation/selectExperiment.do?prjct_id="+prjct_id;
						switch (code){
							//스텝5
							case '001':
							case '002':
							case '007':
							case '999':
								break;
							default:
								break;
						}
						alert(msg);
						location.href = redirectUrl;
					}

				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}

			});

		}

	});

	$(function(){

		if($("[name=result_step_status]").val() == 'N'){
			//조회 버튼 클릭시 이미지가 바뀌지 않아 reload후 tab-4이동
			$('.nav-item a').removeClass('active');
			$('.tab-content').css('display', 'none');
			$('.nav-item.tab-4 > a').addClass('active');
			$("#tab-4").css('display', 'block');
		}

	});

	$(document).on("click","#findSelect", function(){

		var prjct_id = $('input[name=prjct_id]').val();
		var common_path = $('input[name=common_path]').val();
		var path = "/home/data/t3q/uploads/pharmAi/formulation/" + prjct_id +"/api6/csv/api6_experiment.csv" //하드코딩 수정.
		var status = $('input[name=status]').val();
		
		if(!fnCheckFormulationStep6()) return;
		this.setAttribute("disabled", "disabled");

	 	$('.image_div').remove();

 		var y_variable_len = $('input[name=y_variable]').length;
 		var effect_len = $('input[name=effect]').length
 		var start_y_len = $('input[name=start_Y]').length;
 		var end_y_len = $('input[name=end_Y]').length;

 		var y_variable_arr = new Array(y_variable_len);
 		var effect_arr = new Array(effect_len);
 		var start_y_arr = new Array(start_y_len);
 		var end_y_arr = new Array(end_y_len);

 		for(var i = 0; i <y_variable_len; i++){
 			y_variable_arr[i] = $("input[name=y_variable]").eq(i).val();
 			effect_arr[i] = $("input[name=effect]").eq(i).val();
 			start_y_arr[i] = $("input[name=start_Y]").eq(i).val();
 			end_y_arr[i] = $("input[name=end_Y]").eq(i).val();
 		}


	 	var result =
		{
	 		'experiment_data' : path,
			'design-path' : '/home/data/t3q/uploads/pharmAi/formulation/' + prjct_id +  '/api7/tab-1/design/',
			'result-path' : '/home/data/t3q/uploads/pharmAi/formulation/' + prjct_id + '/api7/tab-2/result/',
			'prjct_id': prjct_id,
			'y_variables' : y_variable_arr,
			'effects' : effect_arr,
			'ipt_start_ys' : start_y_arr,
			'ipt_end_ys' : end_y_arr,
			'status' : status
		};

	 	$('.loading-container').css('display','block');
	 	setTimeout(function(){
	 		
	 		$.ajax({
				url: "/pharmai/formulation/getApi7Ajax.do",
				type: 'post',
				data: result,
				async : false,
				success: function(response){
					
					console.log('response check : ', response);
					var code = response.resultStats.resultData.code;
					if(code == "000"){
						var data = response.resultStats.resultData;
						var selectStp_03 = response.resultStats.selectStp_03;
						var selectStp_06 = response.resultStats.selectStp_06;
						var design = data.result["design"];
						var excipient = data.result["excipient"];
						var factor = data.result["factor"];

						var tag = "";

// 						globals.properties의 env 값 받아오기
						var system_env = data.system_env;
						console.log('system_env : ', system_env);
						
						for(var i=0; i< design.length; i++){

							var splitUrl  = design[i].split('/');
							var urlLength = splitUrl.length;
							var pngName = splitUrl[urlLength-1];
							var pos = pngName.lastIndexOf(".");
							var variable = pngName.substring(pngName,pos);

							tag +='	<div class="image_div mt-4 col-md-4">';
							// env에 따라 분기처리(local인 경우는 replace를 진행할 필요가 없음)
							if (system_env == "LOCAL"){
								tag +='		<img style="width:80%;" name="design_img" src="'+ design[i];	
							}else{
								tag +='		<img style="width:80%;" name="design_img" src="'+ design[i].replace('/home/data/t3q/uploads/','/upload/') + '">';
							}
							tag +='		<div class="mt-2">';
							tag +='			<h4>'+variable+'</h4>';
							tag +='		</div>';
							tag +='	</div>';

						}

						$("#design_div").html(tag);


						var tag = "";
						var keys = Object.keys(excipient);
						var values = Object.values(excipient);
						var excipient_nm = Object.values(values);

						for(var i=0; i< excipient.length; i++){
							tag +='		<tr>';
							tag +='			<th colspan="3">Responses (Effects)</th>';
							tag +='			<th >개별응답 값에 대한 목표치</th>';
							tag +='		</tr>';

								for(var i=0; i <keys.length; i++){

									tag +='		<tr>';
									tag +='			<td colspan="1"><font size="2">Y'+(i+1)+'</font></td>';
									tag +='			<td colspan="2"><font size="2">'+Object.keys(excipient_nm[i])+'</font></td>';
									tag += '		<td ><font size="2">'+ Object.values(Object.values(excipient)[i])[0].min+'<span>~</span>'+Object.values(Object.values(excipient)[i])[0].max+'</font></td>';
									tag +='		</tr>';

								}

						$("#excipient_data").html(tag);

						}

						var tag = "";
						var keys = Object.keys(factor);
						var values = Object.values(factor);
						var factor_nm = Object.values(values);

						for(var i=0; i< factor.length; i++){

							tag +='		<tr>';
							tag +='			<th colspan="3">요인 (변수)</th>';
							tag +='			<th>Knowledge space</th>';
							tag +='			<th>Desian space</th>';
							tag +='			<th>Control space</th>';
							tag +='		</tr>';

							for(var i=0; i <keys.length; i++){

								tag +='		<tr>';
								tag +='			<td colspan="1"><font size="2">'+Object.keys(factor_nm[i])[0]+'</font></td>';
								tag +='			<td colspan="2"><font size="2">'+Object.keys(Object.values(factor_nm[i])[0])[0]+'</font></td>';
								tag +='			<td colspan="1" name="knowledge_space"><font size="2">'+ Object.values(Object.values(Object.values(factor_nm[i])[0])[0])[1].min+'<span>~</span>'+Object.values(Object.values(Object.values(factor_nm[i])[0])[0])[1].max+'</font></td>';
								tag +='			<td colspan="1" name="design_space"><font size="2">'+ Object.values(Object.values(Object.values(factor_nm[i])[0])[0])[0].min+'<span>~</span>'+Object.values(Object.values(Object.values(factor_nm[i])[0])[0])[0].max+'</font></td>';
								tag +='			<td colspan="1" name="design_space"><font size="2">'+ Object.values(Object.values(Object.values(factor_nm[i])[0])[0])[2].min+'<span>~</span>'+Object.values(Object.values(Object.values(factor_nm[i])[0])[0])[2].max+'</font></td>';
								tag +='		</tr>';

							}

						$("#factor_data").html(tag);

						}

						//tab-5 이미지 뿌리기
						var tag = "";
						var final_result = data.result.final;
						var result_contour = final_result["contour"];
						var result_design = final_result["design"];

						for(var i = 0; i< result_contour.length; i++){

							var splitUrl  = result_contour[i].split('/');
							var urlLength = splitUrl.length;
							var pngName = splitUrl[urlLength-1];
							var pos = pngName.lastIndexOf(".");
							var variable = pngName.substring(pngName,pos);

								tag +='<div class="image_div mt-4 col-md-3">';
								// env에 따라 분기처리(local인 경우는 replace를 진행할 필요가 없음)
								if (system_env == "LOCAL"){
									tag +='	<img style="width:80%;" src="'+ result_contour[i]	
								}else{
									tag +='	<img style="width:80%;" src="'+ result_contour[i].replace('/home/data/t3q/uploads/','/upload/') +'">'
								}
								tag +='		<div class="mt-2">';
								tag +='			<h4>'+variable+'</h4>';
								tag +='		</div>';
								tag +='</div>';
						}

						$("#result_contour").html(tag);

						var tag = "";

						for(var i = 0; i< result_design.length; i++){

							var splitUrl  = result_design[i].split('/');
							var urlLength = splitUrl.length;
							var pngName = splitUrl[urlLength-1];
							var pos = pngName.lastIndexOf(".");
							var variable = pngName.substring(pngName,pos);

								tag +='<div class="image_div mt-4 col-4" style="display: inline-block; top: 20%;">';
								// env에 따라 분기처리(local인 경우는 replace를 진행할 필요가 없음)
								if (system_env == "LOCAL"){
									tag +='	<img style="width:100%;" src="'+ result_design[i]
								}else{
									tag +='	<img style="width:100%;" src="'+ result_design[i].replace('/home/data/t3q/uploads/','/upload/') +'">'
								}
								tag +='		<div class="mt-2">';
								tag +='			<h4>'+variable+'</h4>';
								tag +='		</div>';
								tag +='</div>';
						}

						$("#result_design").html(tag);

						var tag = "";

							tag+='	<tr>';
							tag+='		<th>Kind</th>';
							tag+='		<th>excipients</th>';
							tag+='		<th>결과값</th>';
							tag+='	</tr>';

								for(var i = 0; i < selectStp_03.length; i++) {
									tag+='	<tr>';
									tag+='		<td>'+selectStp_03[i].KIND+'</td>';
									tag+='		<td>'+selectStp_03[i].EXCIPIENT+'</td>';
									tag+='		<td>'+selectStp_03[i].IPT_USE_RANGE_S+'<span>~</span>'+selectStp_03[i].IPT_USE_RANGE_E+' %</td>';
									tag+='	</tr>';
								}

						$("#Excipient_table").html(tag);

						var tag = "";

							tag+='	<tr>';
							tag+='		<th>CQAs List</th>';
							tag+='		<th>CQAs 범위</th>';
							tag+='	</tr>';

							for(var i = 0; i < selectStp_06.length; i++) {

								tag+='	<tr>';
								tag+='		<td>'+selectStp_06[i].EFFECT+'</td>';
								tag+='		<td>'+selectStp_06[i].IPT_START_Y+'<span>~</span>'+selectStp_06[i].IPT_END_Y+' %</td>';
								tag+='	</tr>';
							}

						$("#DOE_table").html(tag);


						$("#design_div").show();
						$("#comment").hide();
					 	target_Yn = true;
					}else{
						alert(msg);
					}
				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				},
				complete:function(){
					$(".loading-container").css('display','none');
					location.reload(); //조회 버튼 클릭시 이미지 변경을 위해
				}
			});

	 	},100); //End SetTime(function()

	});


	function fnPrint(printDiv) {

       	window.print();
       	location.reload();
	}

	function excelDownload(){
		console.log("excelDownload function call");
		$('#aform').attr({action:'/pharmai/formulation/excelDown.do'}).submit();
	}





	//]]>
</script>

<script type="text/css">

</script>

<jsp:include page="headerStep.jsp" flush="false"/>


<div class="content">
	<div class="container-fluid">
		<form role="form" id="aform" method="post" name="fo" action="">
			<input type="hidden" id="status" name="status" value="06"/>
			<input type="hidden" name="return_smiles" value="">
			<input type="hidden" name="step_new" value="<%=param.getString("step_new")%>"/>
			<input type="hidden" name="next_data" value="<%=param.getString("next_data")%>"/>
			<input type="hidden" name="prjct_type" value="<%=param.getString("prjct_type")%>"/>
			<input type="hidden" name="prjct_id" value="<%=param.getString("prjct_id")%>"/>
			<input type="hidden" name="common_path" value ="<%=param.getString("common_path") %>">
			<input type="hidden" name="projectName" value ="">
			<input type="hidden" name="result_step_status" value="<%=param.getString("result_step_status") %>" />

			<div class="row">
				<div class="col-lg-12">
					<div class="card card-outline">
						<div class="card-header">
							<h3 class="card-title">Design of Experiments(DoE)</h3>
						</div>
						<div class="card-body">
							<div class="card-primary card-tabs">
								<ul class="nav nav-tabs">
						      		<li class="nav-item tab-1" >
							        	<a class="nav-link navALink active" data-tab="tab-1" href="#">Pareto chart and residual plot</a>
							      	</li>
							      	<li class="nav-item tab-2">
							        	<a class="nav-link navALink"  data-tab="tab-2" href="#">Contour plots</a>
							      	</li>
							      	<li class="nav-item tab-3" >
							        	<a class="nav-link navALink" data-tab="tab-3" href="#">Response surface plots</a>
							      	</li>

					        		<li class="nav-item tab-4" >
							        	<a class="nav-link navALink" data-tab="tab-4" href="#">Design space</a>
							      	</li>

					        		<li class="nav-item tab-5" >
							        	<a class="nav-link navALink" data-tab="tab-5" href="#">Result</a>
							      	</li>
							    </ul>
							</div>

							<c:if test="${selectStep6Graph_preto == null}">
								<div class="loading-container" style="position: absolute; left: 50%; top: 50%; display: none;">
								    <div class="loading"></div>
								    <div id="loading-text"><h5>데이터 조회중</h5><h6>(최대 10분  소요)</h6></div>
								</div>
							</c:if>
							<c:if test="${selectStep6Graph_preto != null}">
								<div class="loading-container" style="position: absolute; left: 50%; top: 50%; display: none;">
								    <div class="loading"></div>
								    <div id="loading-text"><h5>데이터 조회중</h5><h6>(최대 10분  소요)</h6></div>
								</div>
							</c:if>


							<div id="tab-1" class="card-body tab-content text-center">
								<c:if test="${selectStep6Graph_preto == null}">
									<div class="container_image_1" id="container_image_1" style="display: flex; flex-wrap: wrap;"></div>
								</c:if>

								<c:if test="${selectStep6Graph_preto != null}">
									<div class="container_image_1" id="container_image_1" style="display: flex; flex-wrap: wrap;">
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_preto.PRETO_PATH_1} "/>
											<div class="mt-2">
												<h4>${preto_file_nm.PRETO_PATH_1_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_preto.PRETO_PATH_2} "/>
											<div class="mt-2">
												<h4>${preto_file_nm.PRETO_PATH_2_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_preto.PRETO_PATH_3} "/>
											<div class="mt-2">
												<h4>${preto_file_nm.PRETO_PATH_3_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_preto.PRETO_PATH_4} "/>
											<div class="mt-2">
												<h4>${preto_file_nm.PRETO_PATH_4_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_preto.PRETO_PATH_5} "/>
											<div class="mt-2">
												<h4>${preto_file_nm.PRETO_PATH_5_NM}</h4>
											</div>
										</div>
									</div>
								</c:if>
							</div>

							<div id="tab-2" class="card-body tab-content text-center" style="display: none;">
								<c:if test="${selectStep6Graph_contour == null}">
									<div class="container_image_2" id="container_image_2" style="display: flex; margin-left: auto; margin-right: auto; flex-wrap: wrap;"></div>
								</c:if>

								<c:if test="${selectStep6Graph_contour != null}">
									<div class="container_image_2" id="container_image_2" style="display: flex; flex-wrap: wrap;">
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_1} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_1_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_2} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_2_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_3} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_3_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_4} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_4_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_5} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_5_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_6} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_6_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_7} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_7_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_8} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_8_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_9} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_9_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_10} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_10_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_11} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_11_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_12} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_12_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_13} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_13_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_14} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_14_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_contour.CONTOUR_PATH_15} "/>
											<div class="mt-2">
												<h4>${contour_file_nm.CONTOUR_PATH_15_NM}</h4>
											</div>
										</div>
									</div>
								</c:if>
							</div>

							<div id="tab-3" class="card-body tab-content text-center" style="display: none;">
								<c:if test="${selectStep6Graph_response == null}">
									<div class="container_image_3" id="container_image_3" style="display: flex; flex-wrap: wrap;"></div>
								</c:if>

								<c:if test="${selectStep6Graph_response != null}">
									<div class="container_image_3" id="container_image_3" style="display: flex; flex-wrap: wrap;">
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_1} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_1_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_2} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_2_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_3} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_3_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_4} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_4_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_5} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_5_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_6} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_6_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_7} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_7_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_8} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_8_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_9} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_9_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_10} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_10_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_11} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_11_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_12} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_12_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_13} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_13_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_14} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_14_NM}</h4>
											</div>
										</div>
										<div class="image mt-4 col-md-4">
											<img style="width:80%;" src="${selectStep6Graph_response.RESPONSE_PATH_15} "/>
											<div class="mt-2">
												<h4>${response_file_nm.RESPONSE_PATH_15_NM}</h4>
											</div>
										</div>
									</div>
								</c:if>
							</div>

							<div id="tab-4" class="card-body tab-content text-center" style="display: none;">

								<table class="table table-bordered text-center" id ="effects_body">
									<c:if test="${selectStep6Design != null}">
											<tr>
												<th colspan="2">Responses (Effects)</th>
												<th colspan="2">개별 응답 값에 대한 목표치</th>
												<td rowspan="${Design_length+1}"><button type="button" class="select btn btn-grey" type="button" id="findSelect" name="findSelect" >조회</button></td>
											</tr>
										<c:forEach var="item" items="${selectStep6Design}" varStatus="status">
											<tr>
												<td>${item.Y_VARIABLE}</td>
												<td>${item.EFFECT}</td>
												<td colspan="2">
													<input type="text" class="target_score text-center" name="start_Y"  value="${item.IPT_START_Y}" size="15"/>
													~
													<input type="text" class="target_score text-center" name="end_Y" value="${item.IPT_END_Y}" size="15"/>
												</td>
												<input type="hidden" name="y_variable" value="${item.Y_VARIABLE}"/>
												<input type="hidden" name="effect" value="${item.EFFECT}"/>
											</tr>
										</c:forEach>
									</c:if>

							  	</table>

								<c:if test="${selectStep6Design == null}">
									<div class="col-sm-12 mt-4" id="comment">
										<h4>"개별 응답값을 입력 후 조회를 선택해 주세요."</h4>
									</div>
								</c:if>
								<c:if test="${selectStep6Design == null}">
									<div class=design_div id="design_div" style="display: flex; flex-wrap: wrap;"></div>
								</c:if>
								<c:if test="${selectStep6Design != null }">
									<div class="design_div" id="design_div" style="display: flex; flex-wrap: wrap;">
										<div class="image_div mt-4 col-md-4">
											<img style="width:80%;" name="design_img" src="${selectStep6Design_img.DESIGN_IMG_PATH_1} "/>
											<div class="mt-2">
												<h4>${design_file_nm.DESIGN_IMG_PATH_1_NM}</h4>
											</div>
										</div>
										<div class="image_div mt-4 col-md-4">
											<img style="width:80%;" name="design_img" src="${selectStep6Design_img.DESIGN_IMG_PATH_2} "/>
											<div class="mt-2">
												<h4>${design_file_nm.DESIGN_IMG_PATH_2_NM}</h4>
											</div>
										</div>
										<div class="image_div mt-4 col-md-4">
											<img style="width:80%;" name="design_img" src="${selectStep6Design_img.DESIGN_IMG_PATH_3} "/>
											<div class="mt-2">
												<h4>${design_file_nm.DESIGN_IMG_PATH_3_NM}</h4>
											</div>
										</div>
									</div>
								</c:if>
							</div>

							<div id="tab-5" class="card-body tab-content text-center" style="display: none;">

<!-- 								<div style="float: right">
									<a href="#"
										class="btn btn-tool btn-sm buttons-excel buttons-html5">
										<i class="fas fa-file-excel"></i>
									</a>
									<a href="#"
										class="btn btn-tool btn-sm buttons-html5">
									<ion-icon name="print-outline"></ion-icon>
									</a>
								</div> -->

								<div class="card-tools" style="float: right">
									<button type="button" class="btn btn-icon btn-sm daterange excel" onclick="excelDownload();" >
										<i class="fas fa-file-excel" name="excel"></i>
									</button>
									<button type="button" class="btn btn-icon btn-sm daterange print-outline" onclick="fnPrint('Properties'); return false;" >
										<ion-icon name="print-outline"></ion-icon>
									</button>
								</div>


							  <table class="table table-bordered text-center" id="excipient_data">
								  <c:if test="${selectStep6ResultList != null }">
										<tr>
								  			<th colspan="3">Responses (Effects)</th>
								  			<th>개별응답 값에 대한 목표치</th>
								  		</tr>
								  		<c:forEach var="item" items="${selectStep6ResultList}" varStatus="status" >
								  			<tr>
								  				<td colspan="1">Y${status.index + 1 }</td>
								  				<td colspan="2">${item.EXCIPENT_NM }</td>
								  				<td>${item.EXCIPENT_MIN } ~ ${item.EXCIPENT_MAX }</td>
								  			</tr>
								  		</c:forEach>
								  	</c:if>

							  </table>
							  <table class="table table-bordered text-center" id="factor_data">
								  <c:if test="${selectStep6ResultList != null }">
										<tr>
								  			<th colspan="3">요인(변수)</th>
								  			<th>Knowledge space</th>
								  			<th>Design space</th>
								  			<th>Control space</th>
								  		</tr>
								  		<c:forEach var="item" items="${selectStep6ResultList}" varStatus="status"  >
								  			<tr>
								  				<td colspan="1">X${status.index + 1 }</td>
								  				<td colspan="2">${item.EXCIPENT_NM }</td>
								  				<td>${item.KNOWLEDGE_SPACE_MIN } ~ ${item.KNOWLEDGE_SPACE_MAX }</td>
								  				<td>${item.DESIGN_SPACE_MIN } ~ ${item.DESIGN_SPACE_MAX }</td>
								  				<td>${item.CONTROL_SPACE_MIN } ~ ${item.CONTROL_SPACE_MAX }</td>
								  			</tr>
								  		</c:forEach>
								  	</c:if>
							  </table>
							  <div class="row" id="result_contour">
								  <c:if test="${selectStepResultImgData != null }">
								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_1 != '' }">
			  			  				<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_1 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_1_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_2 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_2}"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_2_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_3 != '' }">

								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_3 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_3_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_4 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_4 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_4_NM}</h4>
											</div>
										</div>

								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_5 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_5 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_5_NM}</h4>
											</div>
										</div>

								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_6 != '' }">

								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_6 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_6_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_7 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_7 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_7_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_8 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_8 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_8_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_9 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_9 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_9_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_10 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_10 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_10_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_11 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_11 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_11_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_12 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_12 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_12_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_13 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_13 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_13_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_14 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_14 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_14_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_15 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_15 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_15_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_16 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_16 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_16_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_17 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_17 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_17_NM}</h4>
											</div>
										</div>
								  	</c:if>

								  	<c:if test="${selectStepResultImgData.CONTOUR_IMG_PATH_18 != '' }">
								  		<div class="image_div mt-4 col-md-3">
											<img style="width:80%;" src="${selectStepResultImgData.CONTOUR_IMG_PATH_18 }"/>
											<div class="mt-2">
												<h4>${result_file_nm.CONTOUR_IMG_PATH_18_NM}</h4>
											</div>
										</div>
								  	</c:if>


					  		  	 </c:if>
							  </div>
							  <div class="row mt10 col-12">
							  	<div class="row col-6" id="result_design">
							  		<c:if test="${selectStepResultImgData != null }">
							  			<c:if test="${selectStepResultImgData.DESIGN_IMG_PATH_1 != '' }">
							  				<div class="image_div mt-4 col-4" style="top: 20%">
												<img style="width:100%;" src="${selectStepResultImgData.DESIGN_IMG_PATH_1 }"/>
												<div class="mt-2">
													<h4>${result_file_nm.DESIGN_IMG_PATH_1_NM}</h4>
												</div>
											</div>
							  			</c:if>

						  				<c:if test="${selectStepResultImgData.DESIGN_IMG_PATH_2 != '' }">
							  				<div class="image_div mt-4 col-4" style="top: 20%">
												<img style="width:100%;" src="${selectStepResultImgData.DESIGN_IMG_PATH_2 }"/>
												<div class="mt-2">
													<h4>${result_file_nm.DESIGN_IMG_PATH_2_NM}</h4>
												</div>
											</div>
							  			</c:if>


							  			<c:if test="${selectStepResultImgData.DESIGN_IMG_PATH_3 != '' }">
							  				<div class="image_div mt-4 col-4" style="top: 20%">
												<img style="width:100%;" src="${selectStepResultImgData.DESIGN_IMG_PATH_3 }"/>
												<div class="mt-2">
													<h4>${result_file_nm.DESIGN_IMG_PATH_3_NM}</h4>
												</div>
											</div>
							  			</c:if>

									</c:if>
							  	</div>
							  	<div class="col-6 mt-4">
									<table class="table table-bordered text-center" id="Excipient_table">
										<c:if test="${selectStp_03 != null }">
											<tr>
												<th>kind</th>
												<th>excipients</th>
												<th>결과값(부형제 결과 범위)</th>
											</tr>

											<c:forEach var="item" items="${selectStp_03 }">
												<tr>
													<td>${item.KIND }</td>
													<td>${item.EXCIPIENT }</td>
													<td>${item.IPT_USE_RANGE_S } ~ ${item.IPT_USE_RANGE_E } %</td>
												</tr>
											</c:forEach>
										</c:if>
									</table>

									<table class="table table-bordered text-center" id="DOE_table">
										<c:if test="${selectStp_06 != null }">
											<tr>
												<th>CQAs List</th>
												<th>CQAs범위</th>
											</tr>
											<c:forEach var="item" items="${selectStp_06 }">
												<tr>
													<td>${item.EFFECT }</td>
													<td>${item.IPT_START_Y } ~ ${item.IPT_END_Y } %</td>
												</tr>
											</c:forEach>
										</c:if>
									</table>
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
