<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
<script type="text/javascript">
	//<![CDATA[
		$(document).ready(function(e){

			var primaryDtl = {
					'value' : '<%=param.getString("value")%>',
					'unit' : '<%=param.getString("unit")%>'
					}
			var param = {
					'smiles' : '<%=param.getString("smiles")%>',
					'formulation' : '<%=param.getString("formulation") %>',
					'primary' : primaryDtl,
					};

			//if(step_new 가 Y일때)
			<%if(param.getString("step_new") == "Y"){%>
				$.ajax({
					url: "/pharmai/bio/formulation/getApi3Ajax.do",
					type: 'post',
					datatType: 'json',
					data: param,   //inputData: return smiles, formulation: ex)Capsule, Oral Capsule , volume: ex)12
					async : false,
					success: function(response){
						var code = response.resultStats.resultData.code;
						if(code == "000"){
							var data = response.resultStats.resultData;
							var formuList = data.result["formulation_list"];

							var tag ="";

							for(var i = 0; i< formuList.length; i++){
								tag += '<tr>';
								tag += '	<td><input type="checkbox" name="filter" value="N"></td>';
								tag += '	<td>' + formuList[i].kind + '</td>';
								tag += '	<td>' + formuList[i].excipients  +'</td>';
	   							tag += '	<td>' + formuList[i]["use range"].min + " ~ " + formuList[i]["use range"].max + '</td>';
								tag += '	<td>' + formuList[i].max.value + "" + formuList[i].max.unit + '</td>';
								tag += '	<td><input type="text" class="text-center" name="startRange"  value="" size="3" readonly /> ~ <input type="text" class="text-center" name="endRange" value="" size="3" readonly />&nbsp<span>%</span></td>'
								tag += '	<input type="hidden" name="kind" value= "' + formuList[i].kind + '">';
								tag += '	<input type="hidden" name="excipients" value="' + formuList[i].excipients + '">';
								tag += '	<input type="hidden" name="use_range_s" value="' + formuList[i]["use range"].min + '">';
								tag += '	<input type="hidden" name="use_range_e" value="' + formuList[i]["use range"].max + '">';
								tag += '	<input type="hidden" name="maximum" value="' + formuList[i].max.value + '">';
								tag += '	<input type="hidden" name="unit" value="' +formuList[i].max.unit + '">';
								tag += '	<input type="hidden" name="checkYn" value="N">';
								tag += '</tr>';
							}

							$('#renderExcipient').append(tag);
						}else{
							var prjct_id = $('input[name=prjct_id]').val();
							var msg = response.resultStats.resultData.msg;
							var code = response.resultStats.resultData.code;
							var redirectUrl = "/pharmai/bio/formulation/";

							switch (code){
								case '001':
								case '002':
								case '005':
								case '008':
								case '009':
								case '999':
									redirectUrl += "selectRoutes.do?prjct_id="+prjct_id;
									break;

							}
							alert(msg);
							location.href = redirectUrl;
							return;
						}
					},
					error : function(jqXHR, textStatus, thrownError){
						ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
					}
				});

			<%} %>

			//체크박스 클릭시 disable삭제
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

<jsp:include page="headerStep.jsp" flush="false"/>

<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" name="aform" method="post" action="">
					<input type="hidden" id="status" name="status" value="03"/>
					<input type="hidden" name="return_smiles" value="">
					<input type="hidden" name="step_new" value="<%=param.getString("step_new")%>"/>
					<input type="hidden" name="next_data" value="<%=param.getString("next_data")%>"/>
					<input type="hidden" name="prjct_type" value="<%=param.getString("prjct_type")%>"/>
					<input type="hidden" name="prjct_id" value="<%=param.getString("prjct_id")%>"/>
					<input type="hidden" name="projectName" value ="">
					<input type="hidden" name="return_smailes" value="<%=param.getString("smiles")%>">
					<input type="hidden" name="stp_02_seq"  value="<%=param.getString("stp_02_seq")%>">

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
											<th>Kind</th>
											<th>excipients</th>
											<th>Use range</th>
											<th>최대치용량</th>
											<th>사용범위 입력</th>
										</tr>
									</thead>

									<c:if test="${resultList == null }">
										<tbody id="renderExcipient"></tbody>
									</c:if>

									<c:if test="${resultList != null }">
										<tbody>
											<c:forEach var="item" items="${resultList }" varStatus="status">
												<tr>
													<td>
														<c:if test="${item.CHECK_YN == 'Y' }">
															<input type="checkbox" name="filter" value="${item.CHECK_YN }" checked>
														</c:if>
														<c:if test="${item.CHECK_YN == 'N' }">
															<input type="checkbox" name="filter" value="${item.CHECK_YN }">
														</c:if>

													</td>
													<td>${item.KIND}</td>
													<td>${item.EXCIPIENT }</td>
													<td>${item.USE_RANGE_S } ~ ${item.USE_RANGE_E }</td>
													<td>${item.MAXIMUM }${item.UNIT }</td>
													<td>
														<c:if test="${item.CHECK_YN == 'Y' }">
															<input type="text"  name="startRange" class="text-center"  value="${item.IPT_USE_RANGE_S }" size="3" /> ~ <input type="text" class="text-center" name="endRange" value="${item.IPT_USE_RANGE_E }" size="3" />
															<span>%</span>
														</c:if>

														<c:if test="${item.CHECK_YN == 'N' }">
															<input type="text"  name="startRange" class="text-center" value="${item.IPT_USE_RANGE_S }" size="3" readonly /> ~ <input type="text" class="text-center" name="endRange" value="${item.IPT_USE_RANGE_E }" size="3" readonly />
															<span>%</span>
														</c:if>
													</td>
													<!-- tr 안에서 동작. -->
													<input type="hidden" name="kind" value="${item.KIND }">
													<input type="hidden" name="excipients" value="${item.EXCIPIENT }">
													<input type="hidden" name="use_range_s" value="${item.USE_RANGE_S }">
													<input type="hidden" name="use_range_e" value="${item.USE_RANGE_E }">
													<input type="hidden" name="maximum" value="${item.MAXIMUM }">
													<input type="hidden" name="unit" value="${item.UNIT }">
													<input type="hidden" name="checkYn" value="${item.CHECK_YN }">

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

