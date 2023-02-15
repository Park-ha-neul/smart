<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Util" uri="/WEB-INF/tlds/Util.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<link rel="stylesheet" type="text/css" media="all" href="/common/css/dragAndDrop.css" />
<script src="/common/js/jquery.ajax-cross-origin.min.js"></script>
<script src="/common/js/dragAndDrop.js"></script>
<script type="text/javascript" src="/common/js/chart/Chart.js"></script>
<script type="text/javascript">


var dbpath = "";
//<![CDATA[
	$(document).ready(function(){
		$('.loading-container').hide();

		//프로젝트 div영역  비활성화
		if($('[name=step_new]').val() == 'Y') {
			$('.all_CardContent').find(':input').prop('disabled', true);
			$('.excel').css("display", "none");
			$('.print-outline').css("display", "none");
		} else {
			$('.all_CardContent').find(':input').prop('disabled', false);
			$('.all_CardContent a').click(function(e) {
				e.preventDefault();
			});
		}

	 	if(${fileData != null}) {
			dbpath = "${fileData.FILE_RLTV_PATH}" + "${fileData.FILE_NM}";
			$("#attach_file").hide();
		}

		var logP = new Array();
		var logD = new Array();

		//데이터 베이스 에서 가져온 데이터 차트 만들기
		<c:if test="${propList.size() > 0}">
			<c:forEach var ="item" items="${propList}">
				<c:if test="${item.PROP_TYPE == 'MS' }">
					logP.push(${item.PH1});
					logP.push(${item.PH2});
					logP.push(${item.PH3});
					logP.push(${item.PH4});
					logP.push(${item.PH5});
					logP.push(${item.PH6});
					logP.push(${item.PH7});
					logP.push(${item.PH8});
					logP.push(${item.PH9});
					logP.push(${item.PH10});
				</c:if>
			</c:forEach>

			<c:forEach var ="item" items="${propList}">
				<c:if test="${item.PROP_TYPE == 'LO' }">
					logD.push(${item.PH1});
					logD.push(${item.PH2});
					logD.push(${item.PH3});
					logD.push(${item.PH4});
					logD.push(${item.PH5});
					logD.push(${item.PH6});
					logD.push(${item.PH7});
					logD.push(${item.PH8});
					logD.push(${item.PH9});
					logD.push(${item.PH10});
				</c:if>
			</c:forEach>
		</c:if>

		//pH Mass_Solubility line 차트
		var cnvs = document.getElementById('pmsChart').getContext('2d');
		var data = {
			type: 'line',
			data: {
				labels: ["ph1", "ph2", "ph3", "ph4", "ph5", "ph6", "ph7", "ph8", "ph9", "ph10"],
				datasets: [{
					backgroundColor: '#BDBDBD',
					fill:false,
					borderColor: '#8C8C8C',
					lineTension:0.1,
					data: logP
				}]
			},
			// Configuration options go here
			options: {
				responsive: false,
				legend: {
					display: false
				},
				scales:{
					xAxes:[{
						ticks:{
							fontColor: "white",
							fontSize: 12
						},
						gridLines:{
							color: "#4C4C4C"
						}
					}],

					yAxes:[{
						ticks:{
							fontColor: "white",
							fontSize: 12
						},
						gridLines:{
							color: "#4C4C4C"
						}
					}]
				}
			}
		}

		pmsChart = new Chart(cnvs, data);

		// 차트 logD line 차트
		var cnvs2 = document.getElementById('logDChart').getContext('2d');
		var data2 = {
			type: 'line',
			data: {
				labels: ["ph1", "ph2", "ph3", "ph4", "ph5", "ph6", "ph7", "ph8", "ph9", "ph10"],
				datasets: [{
					backgroundColor: '#BDBDBD',
					fill:false,
					borderColor: '#8C8C8C',
					lineTension:0.1,
					data: logD
				}]
			},
			// Configuration options go here
			options: {
				responsive: false,
				legend: {
					display: false,
				},
				scales:{
					xAxes:[{
						ticks:{
							fontColor: "white",
							fontSize: 12
						},
						gridLines:{
							color: "#4C4C4C"
						}
					}],

					yAxes:[{
						ticks:{
							fontColor: "white",
							fontSize: 12
						},
						gridLines:{
							color: "#4C4C4C"
						}
					}]
				}

			}
		}
		logDchart = new Chart(cnvs2, data2);

		//nav 탭 전환
		$('.navALink').click(function(){
			var tab_id = $(this).attr("data-tab");
			$('.nav-item a').removeClass('active');

			$('.tab-content').css('display', 'none');
			$(this).addClass('active');
			$("#"+tab_id).css('display', 'block');
		});
		if(${stepData != null}){
			$('#noData').hide();
			$('#property_Detail').css("display", "block");
		}

	})

	function excelDownload(){
		$('#aform').attr({action:'/pharmai/formulation/propExcelDownLoad.do'}).submit();
	}

	function fnPrint(printDiv) {

		/* headerMenu.style.display = "none"; */

		/* if (printDiv == 'Properties') {

			var print_body = document.getElementById('Properties').innerHTML;
	        window.onafterprint = function(ev) {
	           document.body.innerHTML = print_body;
	        };

		}else {
	           	var print_body = document.getElementById('printDosage').innerHTML;
		        window.onafterprint = function(ev) {
		           document.body.innerHTML = print_body;
		        };
	    }; */

       	window.print();
       	location.reload();
	}



//]]>
</script>

<style>

</style>

 <jsp:include page="headerStep.jsp" flush="false"/>

<div class="content">
	<div class="container-fluid">
		<form role="form" id="aform" name="aform" method="post" action="" enctype="multipart/form-data">
			<input type="hidden" id="status" name="status" value="01"/>
			<input type="hidden" name="pka" value="">
			<input type="hidden" name="pkb" value="">
			<input type="hidden" name="moleWeight" value="">
			<input type="hidden" name="lipskiRule" value="">
			<input type="hidden" name="caPerm" value="">
<!-- 			<input type="hidden" name="dosageForm" value=""> -->
			<input type="hidden" name="water" value="">
			<input type="hidden" name="melting" value="">
			<input type="hidden" name="smiles" value ="${stepData.SMILE_STRING }">
			<input type="hidden" name="return_smiles" value="${stepData.RETURN_SMILE_STRING }">
			<input type="hidden" name="svg_url" value="">
			<input type="hidden" name="sdf_nm" value="">
			<input type="hidden" name="chem_nm" value="${stepData.CHEM_NM}">
			<input type="hidden" name="logP" value="">
			<input type="hidden" name="bolPoint" value="">
			<input type="hidden" name="bio" value="">
			<input type="hidden" name="chemical" value ="">
			<input type="hidden" name="next_data" value="<%=param.getString("next_data")%>" >
			<input type="hidden" name="prjct_type" value="F"/>
			<input type="hidden" name="prjct_id" value="<%=param.getString("prjct_id") %>">
			<input type="hidden" name="projectName" value ="">
			<input type="hidden" name="step_new" value="<%=param.getString("step_new") %>">
			<input type="hidden" name="ntsysFileDocId" value="<%=param.getString("doc_id") %>">

			<div class="row all_CardContent" id="Properties">
				<div class="col-lg-6">
					<div class="card card-info card-outline">
						<div class="card-header">
							<h4 class="card-title">Property Input Type</h4>
						</div>
						<div class="card-body" style="display: block;">
							<div class="form-group">
								<label for="inputType">Input Type</label>
								<div class="input-group">
									<select class="form-control inputType" name="inputType" onchange="fnSelectChg(); return false;" onfocus="this.selectedIndex = -1">
										<c:if test="${stepData != null }">
											<c:if test="${stepData.INPUT_TYPE == 'smiles' }">
												<option value="" disabled="disabled" >구조입력</option>
												<option value="sdf">File upload: SDF</option>
												<option value="smiles" selected>SMILES string</option>
												<option value="chemical">Chemical name</option>
											</c:if>
											<c:if test="${stepData.INPUT_TYPE == 'sdf' }">

												<option value="sdf" selected>File upload: SDF</option>
												<option value="smiles">SMILES string</option>
												<option value="chemical">Chemical name</option>

											</c:if>
											<c:if test="${stepData.INPUT_TYPE == 'chemical' }">
												<option value="sdf">File upload: SDF</option>
												<option value="smiles">SMILES string</option>
												<option value="chemical" selected>Chemical name</option>
											</c:if>
										</c:if>

										<c:if test="${stepData == null }">
											<option value="" selected disabled="disabled" >구조입력</option>
											<option value="sdf">File upload: SDF</option>
											<option value="smiles">SMILES string</option>
											<option value="chemical">Chemical name</option>
										</c:if>

									</select> <span class="input-group-append">
										<button type="button" class="btn btn-grey input-property" id="input-property-btn" onclick="fnFormulationPrediction(); return false;">Property prediction</button>
									</span>
								</div>
							</div>
							<div class="form-group inputsmiles">
								<c:if test="${stepData != null }">
									<c:if test="${stepData.INPUT_TYPE == 'sdf' }">
										<label for="inputsdf">INPUT SDF :</label> <span id="inputsdf" name="inputsdf">${stepData.SDF_NM }</span><br>
										<label for="smiles">RETURN SMILES :</label> <span id="smiles">${stepData.RETURN_SMILE_STRING }</span>
									</c:if>
									<c:if test="${stepData.INPUT_TYPE == 'smiles' }">
										<label for="inputsmiles">INPUT SMILES :</label> <span id="inputsmiles">${stepData.SMILE_STRING }</span><br>
										<label for="smiles">RETURN SMILES :</label> <span id="smiles">${stepData.RETURN_SMILE_STRING }</span>

									</c:if>
									<c:if test="${stepData.INPUT_TYPE == 'chemical' }">
										<label for="inputchem">INPUT CHEMICAL :</label> <span id="inputchem">${stepData.CHEM_NM }</span><br>
										<label for="smiles">RETURN SMILES :</label> <span id="smiles">${stepData.RETURN_SMILE_STRING }</span>

									</c:if>
								</c:if>

								<c:if test="${stepData == null }">
									<label for="inputsmiles">INPUT SMILES :</label> <span id="inputsmiles"></span><br>
									<label for="smiles">RETURN SMILES :</label> <span id="smiles"></span>
								</c:if>

							</div>
							<div class="form-group">
								<label for="generatedMolecule">Generated Molecule</label>
								<div id="svg-div" class="mt10" style="text-align: center">
									<div class="loading-container">
									    <div class="loading"></div>
									    <div id="loading-text"><h5>데이터 조회중</h5><h6>(최대 10분  소요)</h6></div>
									</div>
									<c:if test="${stepData.SVG_URL != null}">
										<img id="svg-img" src="${stepData.SVG_URL.replace('/home/data/t3q','')}" onError="this.src='/common/images/common/no-image.png'" alt="이미지 파일" />
									</c:if>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!--  /.card -->

				<div class="col-lg-6">
					<div class="card card-outline">
						<div class="card-header">
							<h4 class="card-title">Property Detail</h4>
							<div class="card-tools">
								<button type="button" class="btn btn-icon btn-sm daterange excel" onclick="excelDownload();">
									<i class="fas fa-file-excel" name="excel"></i>
								</button> 
								<button type="button" class="btn btn-icon btn-sm daterange print-outline" onclick="fnPrint('Properties'); return false;" >
									<ion-icon name="print-outline"></ion-icon>
								</button>
							</div>
						</div>

						<div class="card-body center" id="noData">
							<h2 class="nodate_default">No Data</h2>
						</div>

						<div class="card-body" id="property_Detail" style="display: none;">
							<div class="card-primary card-tabs">
								<ul class="nav nav-tabs">
									<li class="nav-item tab-1"><a class="nav-link navALink active" data-tab="tab-1" href="#">Properties</a>
									</li>
									<li class="nav-item tab-2">
										<a class="nav-link navALink" data-tab="tab-2" href="#">Dosage</a>
									</li>
								</ul>
							</div>

							<div id="tab-1" class="tab-content">
								<div class="scroll-active">
									<div class="mt-4">
										<table class="table text-nowrap table-hover">
											<thead>
												<tr class="center" role="row">
													<th colspan="10" scope="col">pH Mass_Solubility1 (g/L)</th>
												</tr>
											</thead>
											<!--  로직 구현 -->
											<tbody>
												<tr>
													<td>ph1</td>
													<td>ph2</td>
													<td>ph3</td>
													<td>ph4</td>
													<td>ph5</td>
													<td>ph6</td>
													<td>ph7</td>
													<td>ph8</td>
													<td>ph9</td>
													<td>ph10</td>
												</tr>


												<c:if test="${ propList != null }">
													<tr id="selectPms">
														<c:forEach var ="item" items="${propList }">
															<c:if test="${item.PROP_TYPE.equals('MS') }">
																	<td>${item.PH1 }</td>
																	<td>${item.PH2 }</td>
																	<td>${item.PH3 }</td>
																	<td>${item.PH4 }</td>
																	<td>${item.PH5 }</td>
																	<td>${item.PH6 }</td>
																	<td>${item.PH7 }</td>
																	<td>${item.PH8 }</td>
																	<td>${item.PH9 }</td>
																	<td>${item.PH10 }</td>
															</c:if>
														</c:forEach>
													</tr>
												</c:if>

												<c:if test="${propList == null }">
													<tr id="selectPms"></tr>
												</c:if>

											</tbody>
										</table>
									</div>
									<div class="mt-4">
										<canvas id="pmsChart"></canvas>
									</div>
								</div>
								<div class="scroll-active">
									<div class=" mt-4">
										<table
											class="table text-nowrap table-hover">
											<thead>
												<tr class="center">
													<th colspan="10" scope="col">pH logD</th>
												</tr>
											</thead>
											<!--  로직 구현 -->
											<tbody>
												<tr>
													<td>ph1</td>
													<td>ph2</td>
													<td>ph3</td>
													<td>ph4</td>
													<td>ph5</td>
													<td>ph6</td>
													<td>ph7</td>
													<td>ph8</td>
													<td>ph9</td>
													<td>ph10</td>
												</tr>


													<c:if test="${ propList != null }">
														<tr id="selectLogD">
															<c:forEach var ="item" items="${propList }">
																<c:if test="${item.PROP_TYPE.equals('LO') }">

																		<td>${item.PH1 }</td>
																		<td>${item.PH2 }</td>
																		<td>${item.PH3 }</td>
																		<td>${item.PH4 }</td>
																		<td>${item.PH5 }</td>
																		<td>${item.PH6 }</td>
																		<td>${item.PH7 }</td>
																		<td>${item.PH8 }</td>
																		<td>${item.PH9 }</td>
																		<td>${item.PH10 }</td>

																</c:if>
															</c:forEach>
														</tr>
													</c:if>

												<c:if test="${propList == null }">
														<tr id="selectLogD"></tr>
												</c:if>
											</tbody>
										</table>
									</div>
									<div class="mt-4">
										<canvas id="logDChart"></canvas>
									</div>
								</div>

								<div class="scroll-active">
									<div class="mt-4">
										<table class="table text-nowrap table-hover">
											<!-- 로직 구현 -->
											<c:if test="${propDtl == null }">
												<tr>
													<th>pKa</th>
													<td><span id="pKa"></span></td>
												</tr>
												<tr>
													<th>pKb</th>
													<td><span id="pKb"></span></td>
												</tr>
												<tr>
													<th>Caco2 Permeability</th>
													<td><span id="caPerm"></span></td>
												</tr>
<!-- 												<tr> -->
<!-- 													<th>Dosage Form</th> -->
<!-- 													<td><span id="dosageForm"></span></td> -->
<!-- 												</tr> -->
												<tr>
													<th>Molecular wight(g/mol)</th>
													<td><span id="molecularWeight"></span></td>
												</tr>
												<tr>
													<th>Lipinski's Rule of 5</th>
													<td><span id="rule"></span></td>
												</tr>
												<tr>
													<th>logP</th>
													<td><span id="logP"></span></td>
												</tr>
												<tr>
													<th>Boiling point(°C)</th>
													<td><span id="bollingPoint"></span></td>
												</tr>
												<tr>
													<th>Bioavailability</th>
													<td><span id="bioavailability"></span></td>
												</tr>
												<tr>
													<th>Melting Point (°C)</th>
													<td><span id="melting"></span></td>
												</tr>
												<tr>
													<th>Water Solubility</th>
													<td><span id="water"></span></td>
												</tr>
											</c:if>

											<c:if test="${propDtl != null }">
												<tr>
													<th>pKa</th>
													<td><span>${propDtl.PKA }</span></td>
												</tr>
												<tr>
													<th>pKb</th>
													<td><span>${propDtl.PKB }</span></td>
												</tr>
												<tr>
													<th>Caco2 Permeability</th>
													<td><span>${propDtl.CA_PERM }</span></td>
												</tr>
<!-- 												<tr> -->
<!-- 													<th>Dosage Form</th> -->
<%-- 													<td><span></span>${propDtl.DOSAGE_FORM }</td> --%>
<!-- 												</tr> -->
												<tr>
													<th>Molecular wight(g/mol)</th>
													<td><span>${propDtl.MOL_WEI }</span></td>
												</tr>
												<tr>
													<th>Lipinski's Rule of 5</th>
													<td><span>${propDtl.LIP_RULE }</span></td>
												</tr>
												<tr>
													<th>logP</th>
													<td><span>${propDtl.LOGP }</span></td>
												</tr>
												<tr>
													<th>Bolling point(°C)</th>
													<td><span>${propDtl.BOL_POINT }</span></td>
												</tr>
												<tr>
													<th>Bioavailability</th>
													<td><span>${propDtl.BIO }</span></td>
												</tr>
<!-- 											추가된 특성 2가지 (melting, water) -->
												<tr>
													<th>Melting Point (°C)</th>
													<td><span>${propDtl.MELTING }</span></td>
												</tr>
												<tr>
													<th>Water Solubility</th>
													<td><span>${propDtl.WATER }</span></td>
												</tr>
											</c:if>
										</table>
									</div>
								</div>
							</div>
							<!--  /tab-1 -->

							<div id="tab-2" class="tab-content dosage_card" style="display: none;">
								<div class="mt-4" id="printDosage">
									<table class="table text-nowrap table-hover">
										<!-- 로직 구현 -->
										<thead>
											<tr>
												<th>#</th>
												<th>oral</th>
												<th>parenteral</th>
												<th>local</th>
											</tr>
										</thead>
										<c:if test="${dosageList != null }">
											<tbody id="selectDosage">
												<c:forEach var="item" items="${dosageList}">
													<tr>
														<td>${item.PROPERTIES_NM }</td>
														<td>${item.ORAL_YN == "Y"? "O" : "X"  }</td>
														<td>${item.PARENTERAL_YN == "Y"? "O" : "X"  }</td>
														<td>${item.LOCAL_YN == "Y"? "O" : "X"  }</td>
													</tr>
												</c:forEach>
											</tbody>
										</c:if>
										<c:if test="${dosageList == null }">
											<tbody id="selectDosage">
											</tbody>
										</c:if>
									</table>
								</div>
							</div>
						</div>
						<!-- /card-body  -->
					</div>
					<!--  /card card-outline -->
				</div>
				<!--  /.card -->
			</div>
			<!--  /row -->

			<!-- File upload:sdf 모달 -->
			<div class="modal fade" id="md_sdf" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				<div class="modal-dialog" role="document" style="width:70%">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title" id="myModalLabel">File Upload</h4>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						</div>
						<div class="modal-body">
							<div id="attach_file">
								<div class="upload-btn-wrapper">
									<input type="file" id="input_file" name="input_file" style="width: 100%;" onchange="fnHideDragAndDrop()"/>

									<button class="upload-btn btn">파일선택</button>
								</div>
								<div id="dropZone" class="margin-bottom dropZone11" style="margin-bottom: 20px;">
									<div id="fileDragDescModal">파일을 드래그 해주세요.</div>
								</div>
							</div>
							<div class="table-responsive">
								<table class="table text-nowrap" id="fileTableTbody">
									<colgroup>
										<col style="width: *%;">
										<col style="width: 100px;">
									</colgroup>
									<thead>
										<tr class="projects td">
											<th class="text-center">파일</th>
											<th></th>
										</tr>
									</thead>
									<tbody>
									<c:if test="${fileData != null}">
										<tr id="dbLoadFile" class="projects">
											<td class="text-center">
												${fileData.FILE_NM} / (${fileData.FILE_SIZE / 1000.0}) KB
											</td>
											<td class="text-center">
												<button class="btn bg-gradient-danger btn-sm" onclick="deleteFile('0', '1'); return false;">삭제</button>
											</td>
										</tr>
									</c:if>

									</tbody>
								</table>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn bg-gradient-success" onclick="fnFormulationTpye('F'); return false;">Confirm</button>
							<button type="button" class="btn bg-gradient-danger" data-dismiss="modal">Cancel</button>
						</div>
					</div>
				</div>
			</div>

			<!-- SMILES string 모달 -->
			<div class="modal fade" id="md_smiles" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				<div class="modal-dialog" role="document" style="width:70%">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title" id="myModalLabel">smiles Upload</h4>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						</div>
						<div class="modal-body">
							<textarea class="upload-dialog-textarea" name="smiles_string">${stepData.SMILE_STRING }</textarea>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn bg-gradient-success" id="smiles_btn" onclick="fnFormulationTpye('S'); return false;">Confirm</button>
							<button type="button" class="btn bg-gradient-danger" data-dismiss="modal">Cancel</button>
						</div>
					</div>
				</div>
			</div>

			<!-- chemical name 모달 -->
			<div class="modal fade" id="md_name" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				<div class="modal-dialog" role="document" style="width:70%">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title" id="myModalLabel">Chemical name Upload</h4>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						</div>
						<div class="modal-body">
							<textarea class="upload-dialog-textarea" name="chemical_name">${stepData.CHEM_NM}</textarea>
						</div>
						<div class="modal-footer">
<!-- 							<button type="button" class="btn bg-gradient-success" onclick="fnFormulationTpye('N'); return false;">Confirm</button> -->
							<button type="button" class="btn bg-gradient-success" onclick="fnFormulationTpye('N'); return true;">Confirm</button>
							<button type="button" class="btn bg-gradient-danger" data-dismiss="modal">Cancel</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<script type="text/javascript">

	//chemical name을 입력값으로 받은 경우 DB 조회, javascript promise로 다시 구현
	function chemicalDBcheck(input_chemical) {
		console.log('chemicalDBcheck 함수');
		return new Promise(function(resolve, reject) {
			$.ajax({
				url:"/pharmai/formulation/chemicalToSmiles.do",
				data:{chemical : input_chemical},
				type:'POST',
				success:function(result){
					// 필요한 값만 전달
					resolve(result.list.smiles);
				}
			})
		})
		
	}

	function fnSelectChg(){

		var val = $("[name=inputType]").val();

		if(val == "sdf"){
			$("#md_sdf").modal();
		}else if (val=="smiles"){
			$("#md_smiles").modal();
		}else if (val=="chemical"){
			$("#md_name").modal();
		}

	}





	 // 모달창 값 전달
	function fnFormulationTpye(obj){
		var file_length = $('.projects').length;
		var str ='';
		var FileText = $(".projects > td").text();
		var idx = FileText.indexOf('/')
		var fileName = FileText.substring(0,idx);

		var md_sdf = fileName;
		var md_smiles = $("[name=smiles_string]").val();
		var md_chemical = $("[name=chemical_name]").val();

		if ( obj == "F") {
			//legnth가 '1' 파일이 없는 상태
			 if(file_length == 1) {
				 alert("sdf파일을 첨부 해주세요");
				 return;
				$(".inputsmiles").children().remove();
				str += '<label for=\"inputsdf\">INPUT SDF :</label> <span id=\"inputsdf\"></span><br>'
				str += '<label for=\"smiles\">RETURN SMILES :</label> <span id=\"smiles\"></span>'
				$(".inputsmiles").append(str);

			} else {//legnth가 '1' 이외 파일이 있는 상태
				$(".inputsmiles").children().remove();
				str += '<label for=\"inputsdf\">INPUT SDF :</label> <span id=\"inputsdf\">' + md_sdf + '</span><br>'
				str += '<label for=\"smiles\">RETURN SMILES :</label> <span id=\"smiles\"></span>'
				$(".inputsmiles").append(str);

			 }

		} else if ( obj == 'S') {

			$(".inputsmiles").children().remove();
			str += '<label for=\"inputsmiles\">INPUT SMILES :</label> <span id=\"inputsmiles\">' + md_smiles + '</span><br>'
			str += '<label for=\"smiles\">RETURN SMILES :</label> <span id=\"smiles\"></span>'
			$(".inputsmiles").append(str);
		} else {
			console.log('N은 여기를 타야함');
			$(".inputsmiles").children().remove();
			str += '<label for=\"inputchem_nm\">INPUT CHEM :</label> <span id=\"inputchem\">' + md_chemical + '</span><br>'
			str += '<label for=\"smiles\">RETURN SMILES :</label> <span id=\"smiles\"></span>'
			chemicalDBcheck(md_chemical);
			$(".inputsmiles").append(str);
		}

		$("#md_sdf").modal('hide');
		$("#md_smiles").modal('hide');
		$("#md_name").modal('hide');
	}

	function fnHideDragAndDrop() {
		var FileListLength = $('.projects').length;

		if(FileListLength == 1){
			$("#attach_file").hide();
		}else {
			$("#attach_file").show();
		}

	}
	// 업로드 파일 목록 생성
	function addFileList(fIndex, fileName, fileSize){

		var addFileListLength = $('.projects').length;

			$('#fileTableTbody tbody tr').remove();
			var html = "";
			html += "<tr class='projects td' id='fileTr_" + fIndex + "'>";
			html += "	<td class='text-center'>";
			html +=			fileName + " / " + (fileSize * 1000).toFixed(2) + "KB " ;
			html += "	</td>"
			html += "	<td class='text-center'>";
			html +=	"		<button class='btn bg-gradient-danger btn-sm' onclick='deleteFile(" + fIndex + ", 0); return false;'>삭제</button>";
			html += "	</td>"
			html += "</tr>"
			$('#fileTableTbody tbody').append(html);

	}

	//등록된 파일 삭제
	function deleteFile(fIndex, dbStatus){
		if(dbStatus == 1){
			if(confirm("삭제하시겠습니까?")){
				$("#dbLoadFile").remove();
				$("#attach_file").show();

			}
		}else{
			if(confirm("삭제하시겠습니까?")){
				// 파일 배열에서 삭제
				delete fileList[fIndex];
				// 업로드 파일 테이블 목록에서 삭제
				$("#fileTr_" + fIndex).remove();
				$("#attach_file").show();


			}

	 	}
		$('#input_file').val("");

	}

	//모달창 Close버튼 클릭시 초기화
	var pmsChart;
	var logDchart;
	
	function fnFormulationPrediction(){

		var step_new = $("[name=step_new]").val();
		var inputType = $("[name=inputType]").val();
		var param_data ="";
		// type이 sdf인 경우 sdf to base64 변수
		var sdfPath = "";
		var prjct_id =$("[name=prjct_id]").val();
		var path = "/home/data/t3q/uploads/pharmAi/formulation/" + prjct_id +"/api1/png/";

		var fileType = true;

		if(inputType == "sdf"){
			var file_length = $('.projects').length;

			if(file_length == 1){
				alert("입력된 sdf파일이 없습니다. sdf파일을 첨부해 주세요.");
				return;
			}

			var uploadFileList = Object.keys(fileList);

		 	if(uploadFileList.length == 0){
		 		param_data = dbpath;
		 	}else{
				var formData = new FormData($('#aform')[0]);

				for(var i = 0; i < uploadFileList.length; i++){
					formData.append('upload', fileList[uploadFileList[i]]);
				}

				var start = new Date();
				//파일읽는부분
				$.ajax({
					url:"/pharmai/formulation/getFileAjax.do",
					data:formData,
					type:'POST',
					async : false,
					enctype:'multipart/form-data',
					global: false, // 추가
					processData:false,
					contentType:false,
					dataType:'json',
					cache:false,
					success:function(param){
						
						if(param.resultStats.resultCode == "error"){
							alert(param.resultStats.resultMsg);
							fileType = false;
						}else{
							var saveFileNm = param.resultStats.reNtsysFile.fileNm;
							var savefileRltvPath = param.resultStats.reNtsysFile.fileRltvPath
							var ntsysFileDocId = param.resultStats.reNtsysFile.docId

// 							param_data = savefileRltvPath + saveFileNm;
							// path에 전달
							sdfPath = savefileRltvPath + saveFileNm;

							var fo = document.aform;
							fo.ntsysFileDocId.value = ntsysFileDocId;

						}

					}
				});


		 	}


		}else if(inputType == "smiles"){
			if($('[name=smiles_string]').val()== ""){
				alert("입력된 smiles스트링 값이 없습니다. smiles스트링 값을 입력해 주세요.");
				return;
			}else {
				param_data = $('[name=smiles_string]').val();
			}

		}else if(inputType == "chemical"){
			// chemical name이 db에 존재하지 않는 경우도 필요
			if($('[name=chemical_name]').val() == ""){
				alert("chemical 스트링이 존재하지않습니다. chemical 스트링 값을 입력해 주세요.");
				return;
			}else {
// 				param_data = $('[name=chemical_name]').val();
				inputType = "smiles";
				input_data = $('[name=chemical_name]').val();
				// resolve()의 결과 값 data를 resolvedData로 받음 (smiles 값만 받음)
				callAjax = chemicalDBcheck(input_data).then(function(resolvedData){
					param_data = resolvedData;
					console.log('param_data check : ', param_data);
					if (!param_data){
						var returnValue = confirm('입력된 화학명이 존재하지 않습니다.\n참고 사이트에 접속해 해당하는 화학명의 smiles 값을 입력해주세요.');
						// 확인 버튼 클릭 시 
						if (returnValue == true){
							window.open('https://pubchem.ncbi.nlm.nih.gov/');
						}else{
							fileType = false;
						}
						
						// 기존 alert창만 띄울때 (링크 이동 안됨)
// 						alert("입력된 화학명이 존재하지 않습니다. 공식 사이트(https://pubchem.ncbi.nlm.nih.gov/)에 접속해 해당하는 화학명의 smiles 값을 입력해주세요.");
						fileType = false;
					}else{
						// 추후 수정 필요
	 					param = {
	 							'type' : inputType,
	 							'value' : param_data,
	 							'prjct_id' : prjct_id,
	 							'path' : path,
	 							'sdfPath' : sdfPath
	 					};
					}
				});
			}

		}else{
			alert("input type error!!");
			return;
		}

		// 추후 수정 필요
		var param = {
				'type' : inputType,
				'value' : param_data,
				'prjct_id' : prjct_id,
				'path' : path,
				'sdfPath' : sdfPath
		};

		$("#svg-img").remove();
		$('.loading-container').css('display','block');

		setTimeout(function(){
			console.log('api1번에 전달하는 data : ', param);

			if(fileType) {
				$.ajax({
					url : '/pharmai/formulation/getApi1Ajax.do',
					type : 'post',
					dataType : 'json',
					data : param,
					async : false,
					success : function(response){
						var code = response.resultStats.list.code;
						if(code == "000"){
							// 테이블 데이터, 차트 데이터 초기화
							$("#selectLogD").empty();
							$("#selectPms").empty();
							$('#selectDosage').empty();
							if (pmsChart) {
						    	pmsChart.destroy();
						      }
						    if (logDchart) {
						    	logDchart.destroy();
						      }

							$("#smiles-div").html(response.resultStats.list.result);

							var dosage = response.resultStats.list.result.dosage;
							var tag = "";
							var connDosage ="";

							//Dosage Rendering
							for(key in dosage){
								tag += '<tr>';
								tag += '<td>' + key + '</td>';
								tag += '<input type="hidden" name="dosage_name" value="' + key + '">';
								var dosageData = dosage[key];
								for(var i = 0; i< dosageData.length; i++){
									connDosage += dosageData[i];
								}

								if(connDosage.indexOf("Oral") >= 0){
									tag += '<td>O</td>';
									tag += '<input type="hidden" name="oral" value="Y">'
								}else if(connDosage.indexOf("oral") == -1){
									tag += '<td>X</td>';
									tag += '<input type="hidden" name="oral" value="N">'
								}

								if(connDosage.indexOf("Parenteral") >= 0){
									tag += '<td>O</td>';
									tag += '<input type="hidden" name="parenteral" value="Y">'
								}else if(connDosage.indexOf("parenteral") == -1){
									tag += '<td>X</td>';
									tag += '<input type="hidden" name="parenteral" value="N">'
								}

								if(connDosage.indexOf("Local") >= 0){
									tag += '<td>O</td>';
									tag += '<input type="hidden" name="local" value="Y">'
								}else if(connDosage.indexOf("Local") == -1){
									tag += '<td>X</td>';
									tag += '<input type="hidden" name="local" value="N">'
								}

								connDosage = "";
								tag += '</tr>';
							}

							$('#selectDosage').append(tag); //랜더링

							// globals.properties의 env 값 받아오기
							var system_env = response.resultStats.list.system_env;
							
							//Properties
							var prop = response.resultStats.list.result.properties;
								var pms = prop['pH Mass Solubility'];
								var tag = "";
								//pH Mass_Solubility rendering
							for(key in pms){
								tag += '<td>' + pms[key] + '</td>';
								tag += '<input type="hidden" name="pms" value="' + pms[key] + '"/>'
							}


							$('#selectPms').append(tag); //랜더링

							//pH Mass_Solubility line 차트
							var cnvs = document.getElementById('pmsChart').getContext('2d');
							var data = {
								type: 'line',
								data: {
									labels: ["ph1", "ph2", "ph3", "ph4", "ph5", "ph6", "ph7", "ph8", "ph9", "ph10"],
									datasets: [{
										backgroundColor: '#BDBDBD',
										fill:false,
										borderColor: '#8C8C8C',
										lineTension:0.1,
										data: pms
									}]
								},
								// Configuration options go here
								options: {
									responsive: false,
									legend: {
										display: false
									},
									scales:{
										xAxes:[{
											ticks:{
												fontColor: "white",
												fontSize: 12
											},
											gridLines:{
												color: "#4C4C4C"
											}
										}],

										yAxes:[{
											ticks:{
												fontColor: "white",
												fontSize: 12
											},
											gridLines:{
												color: "#4C4C4C"
											}
										}]
									}
								}
							}

							pmsChart = new Chart(cnvs, data);

						    var logD = prop['pH LogD'];
						    var logTag = "";

								//pH Mass_Solubility rendering
							for(key in logD){
								logTag += '<td>' + logD[key] + '</td>';
								logTag += '<input type="hidden" name="logD" value=" ' + logD[key] + ' ">';
							}
							$('#selectLogD').append(logTag);

							// 차트 logD line 차트
							var cnvs2 = document.getElementById('logDChart').getContext('2d');
							var data2 = {
								type: 'line',
								data: {
									labels: ["ph1", "ph2", "ph3", "ph4", "ph5", "ph6", "ph7", "ph8", "ph9", "ph10"],
									datasets: [{
										backgroundColor: '#BDBDBD',
										fill:false,
										borderColor: '#8C8C8C',
										lineTension:0.1,
										data: logD
									}]
								},
								// Configuration options go here
								options: {
									responsive: false,
									legend: {
										display: false,
									},
									scales:{
										xAxes:[{
											ticks:{
												fontColor: "white",
												fontSize: 12
											},
											gridLines:{
												color: "#4C4C4C"
											}
										}],

										yAxes:[{
											ticks:{
												fontColor: "white",
												fontSize: 12
											},
											gridLines:{
												color: "#4C4C4C"
											}
										}]
									}

								}
							}
							logDchart = new Chart(cnvs2, data2);

							//API 데이터
						    var prop_pka = prop['pKa'];
						   	var prop_pkb = prop['pKb'];
						   	var prop_moleWeight = prop['Molecular weight(g/mol)'];
						   	var prop_lipskiRule = prop["Lipinski's Rule of 5"];
// 						   	var prop_caco_list = prop['Caco-2 Permeability'];
							var prop_caco_list = prop['Caco-2 permeability'];
// 							var prop_dosageForm_list = prop['Dosage Form'];
							var smiles_string = $('[name=smiles_string]').val();
							var chemical_name = $('[name=chemical_name]').val();
							var molecule = response.resultStats.list.result.molecule;
							var return_smiles = molecule['smiles'];
							var generated_Molecule = molecule['generated Molecule'];
							var prop_logP = prop['LogP'];
// 							var prop_bolingPoint = prop["Boiling point(Â°C)"];
							var prop_bolingPoint = prop["Boiling Point (°C)"];
							var prop_bioavailability = prop['Bioavailability'];
							// 새로 추가된 특성
							var prop_water = prop['Water Solubility'];
							var prop_melting = prop['Melting Point (°C)'];
							
							// env에 따라 분기처리 (local인 경우는 replace를 진행할 필요가 없음)
							if (system_env == "LOCAL"){
								generated_Molecuule = generated_Molecule;								
							}else{
								generated_Molecuule = generated_Molecule.replace('/home/data/t3q/uploads/','/upload/');	
							}
							// 기존
// 							generated_Molecuule = generated_Molecule.replace('/home/data/t3q/uploads/','/upload/');

							//SDF(file_upload)
							var FileText = $(".projects > td").text();
							var idx = FileText.indexOf('/')
							var fileName = FileText.substring(0,idx);

							//페이지에 데이터 랜더링
							$('#pKa').text(prop_pka != "" ? prop_pka : "no data");
							$('#pKb').text(prop_pkb != "" ? prop_pkb : "no data");
							$('#molecularWeight').text(prop_moleWeight != "" ? prop_moleWeight : "no data");
							$('#rule').text(prop_lipskiRule != "" ? prop_lipskiRule : "no data");
							// 수정
							if (prop_caco_list >= 0.5){
								$('#caPerm').text(prop_caco_list = "" ? prop_caco_list : "Permeable with Papp > 8 * 10^-6 (cm/s), " + prop_caco_list); // caco2 str 형태
								
							}else{
								$('#caPerm').text(prop_caco_list = "" ? prop_caco_list : "Non-permeable with Papp < 8 * 10^-6 (cm/s), " + prop_caco_list); // caco2 str 형태
							}
							// 기존
// 							$('#caPerm').text(prop_caco_list != "" ? prop_caco_list[0] + ", " + prop_caco_list[1] : "no data" );
							// 삭제
// 							$('#dosageForm').text(prop_dosageForm_list != "" ? prop_dosageForm_list[0] + ", " + prop_dosageForm_list[1] : "no data" );
				            $('#smiles').text(return_smiles != "" ? return_smiles : "no data");
					     	$('#logP').text(prop_logP != "" ? prop_logP : "no data");
							$('#bollingPoint').text(prop_bolingPoint != "" ? prop_bolingPoint: "no data");
							// 수정
							if (prop_bioavailability >= 1){
								$('#bioavailability').text(prop_bioavailability = "" ? prop_bioavailability : "Orally bioavailable, " + prop_bioavailability); // caco2 str 형태
								
							}else{
								$('#bioavailability').text(prop_bioavailability = "" ? prop_bioavailability : "Orally non-bioavailable, " + prop_bioavailability); // caco2 str 형태
							}
							// 기존
// 							$('#bioavailability').text(prop_bioavailability != "" ? prop_bioavailability[0] + ", " + prop_bioavailability[1] : "no data");
							
							// 새로 추가된 특성
							$('#water').text(prop_water != "" ? prop_water : "no data");
							$('#melting').text(prop_melting != "" ? prop_melting : "no data");

							var fo = document.aform;

				            //form 태그 데이터 삽입
							fo.pka.value = prop_pka; 				//pKa
							fo.pkb.value = prop_pkb; 				//pKb
							fo.moleWeight.value = prop_moleWeight;  //Molecular wight(g/mol)
							fo.lipskiRule.value = prop_lipskiRule;  //Lipinski's Rule of 5
							// 수정
							fo.caPerm.value = prop_caco_list; //Caco2 Permeability str 형식
// 							fo.caPerm.value = prop_caco_list[0] + ", " + prop_caco_list[1]; //Caco2 Permeability
							// 삭제
// 							fo.dosageForm.value = prop_dosageForm_list[0] + ", " + prop_dosageForm_list[1]; //Dosage Form
							fo.smiles.value = smiles_string; //입력 smiles 데이터
							fo.return_smiles.value = return_smiles; // API smiles 데이터
							fo.svg_url.value = generated_Molecuule; // SVG 파일 경로
						   	fo.sdf_nm.value = fileName;  //SDF 파일명
							fo.chem_nm.value = chemical_name;  //chemical_name
							fo.logP.value = prop_logP; 		   //logP
							fo.bolPoint.value = prop_bolingPoint; //bolling_point
							// 수정
							fo.bio.value = prop_bioavailability; //bioavailability
// 							fo.bio.value = prop_bioavailability[0] + ", " + prop_bioavailability[1]; //bioavailability

							// 새로 추가한 특성
							fo.water.value = prop_water; //water solubility
							fo.melting.value = prop_melting; //melting point

				            //molecule이미지
							$("#svg-div").empty();
							var html = "";
					        html += "<div class='loading-container'>"
				            html += "<div class='loading'></div>"
							html += "<div id='loading-text'><h5>데이터 조회중</h5><h6>(최대 10분  소요)</h6></div>"
							html += "</div>"
							$("#svg-div").append(html);
							// 원본
// 				            $("#svg-div").append("<img src='"+generated_Molecuule+"' id=\'svg-img\' onError=\"this.src='/common/images/common/no-image.png'\" alt=\"Molecule png\" />");
							
							// 동일한 URL에 DATE 추가
				            $("#svg-div").append("<img src='"+generated_Molecuule + "?v=" + new Date().getTime()+"' id=\'svg-img\' onError=\"this.src='/common/images/common/no-image.png'\" alt=\"Molecule png\" />");


						}else{
							var msg = response.resultStats.list.msg;
							switch (code){
								case '001':
								case '002':
								case '003':
								case '004':
								case '005':
								case '006':
								case '007':
								case '999':
							}
							alert(msg);
							return;
						}

					},
					error : function(jqXHR, textStatus, thrownError){
						$(".modal").modal('hide');
						ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
					},
					complete:function(){

						$('#noData').hide();
						$('#property_Detail').css("display", "block");
						$(".loading-container").css('display','none');
					}
				});
			}else{
				// ajax 호출을 진행하지 않은 경우에는 로딩바가 생기지 않게 수정
				$(".loading-container").css('display','none');
			}
		},200); //End SetTime(function()



	}

</script>

