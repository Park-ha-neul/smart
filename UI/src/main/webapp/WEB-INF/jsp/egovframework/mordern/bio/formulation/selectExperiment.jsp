<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Util" uri="/WEB-INF/tlds/Util.tld"%>

<jsp:useBean id="exprDataList"  type="java.util.List" class="java.util.ArrayList" scope="request"/>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<script type="text/javascript">
	//<![CDATA[
		$(document).ready(function(e){
			console.log('여기를 타긴함')
			// object를 키 이름으로 정렬하여 반환
			function sortObject(obj){
				var sorted = {},
				key, a = [];
				// 키이름을 추출하여 배열에 집어넣음
				for (key in obj) {
					if (obj.hasOwnProperty(key)) a.push(key);
				}
				// 키이름 배열을 정렬
				a.sort();
				// 정렬된 키이름 배열을 이용하여 object 재구성
				for (key=0; key<a.length; key++) {
					sorted[a[key]] = obj[a[key]];
				}
				return sorted;
			}

			var prjct_id = $('input[name=prjct_id]').val();

			var param = {
				'prjct_id': prjct_id
			};

			var step_new = $('input[name=step_new]').val();

			//DB 불러올시 로딩바 감추기
			if(step_new == 'N'){
				$(".loading-container").hide();
			}

			if(step_new == 'Y'){

				$(window).ajaxStart(function(){
					$(".loading-container").show();
				})
				.ajaxStop(function(){
					$(".loading-container").hide();
				});

				$.ajax({
					url: "/pharmai/bio/formulation/getApi5Ajax.do",
					type: 'post',
					data: param,
					async : false,
					success: function(response){
						var st = response.resultStats.resultData.result.header;
						var code = response.resultStats.resultData.code;
						if(code == "000"){
							var data = response.resultStats.resultData;
							var header = data.result["header"];
							var experimentData = data.result["experiment data"];

							$('input[name=exprLength]').val(experimentData.length);

							var headerData = new Array();
							var exprData = new Array();

							// object를 키 이름으로 정렬
							headerData  = sortObject(header);

							var thead = '';
							var theadData = '';
							var tbody = '';

							thead  += '<tr class="">';
							theadData += '<tr class="">';
							theadData += '	<th></th>';

							//header
							for(var i = 0; i < Object.keys(headerData).length; i++){
								if(headerData[Object.keys(headerData)[i]] != ""){
									theadData += '	<th><input type="hidden" name="exprHeader" value="' + headerData[Object.keys(headerData)[i]] + '">'+ headerData[Object.keys(headerData)[i]] +'</th>'; // 표준순서 , 런 순서, 점 유형, ...
								}
							}

							thead  += '</tr>';
							theadData += '</tr>';
							$('#experimentDataTable thead').append(thead + theadData);

							//experimentData
							for(var i = 0; i < experimentData.length; i++){
								// object를 키 이름으로 정렬
								exprData = sortObject(experimentData[i]);

								tbody += '<tr class="">';
								tbody += '	<td>' + (i+1) + '</td>';

								for(var j = 0; j < Object.keys(headerData).length; j++){
									if(headerData[Object.keys(headerData)[j]] != ""){
										if(j < Object.keys(experimentData[i]).length){
											tbody += '	<td><input type="hidden" name="exprVal_'+i+'" value="' + exprData[Object.keys(exprData)[j]] + '">'+ exprData[Object.keys(exprData)[j]] +'</td>';
										}else{
											tbody += '	<td><input type="text" style="border:none; text-align:center;" name="exprVal_'+i+'" class="exprVal form-control"></td>';
										}
									}
								}
								tbody += '</tr>';
							}
							$('#experimentDataTable tbody').append(tbody);
						}else{
							var prjct_id = $('input[name=prjct_id]').val();
							var msg = response.resultStats.resultData.msg;
							var redirectUrl = "/pharmai/formulation/";
							switch (code){
								//스텝3
								case '010':
								case '011':
								case '012':
								case '013':
								case '015':
								case '016':
								case '999':
									redirectUrl += "selectExcipient.do?prjct_id="+prjct_id;
									break;
								//스텝4
								case '001':
								case '002':
								case '014':
									redirectUrl += "selectCQAsList.do?prjct_id="+prjct_id;
									break;
								default:
									redirectUrl += "selectCQAsList.do?prjct_id="+prjct_id;
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


		function temporarySave(){
			if($("[name=next_data]").val() == "Y"){
				if(confirm("이전에 등록한 실험 data가 존재합니다. 이전 실험data를 초기화 하시겠습니까?")){
					$('#aform').attr({action:'/pharmai/bio/formulation/saveFormulTemporary.do'}).submit();
				}
			}else{
				$('#aform').attr({action:'/pharmai/bio/formulation/saveFormulTemporary.do'}).submit();
			}

		}

	//]]>
</script>

<jsp:include page="headerStep.jsp" flush="false"/>

<div class="content">
	<div class="container-fluid">
		<form role="form" id="aform" method="post" action="">
			<input type="hidden" id="status" name="status" value="05"/>
			<input type="hidden" name="step_new" value="<%=param.getString("step_new")%>"/>
			<input type="hidden" name="next_data" value="<%=param.getString("next_data")%>"/>
			<input type="hidden" name="prjct_type" value="<%=param.getString("prjct_type")%>"/>
			<input type="hidden" name="prjct_id" value="<%=param.getString("prjct_id")%>"/>
			<input type="hidden" name="exprLength" value ="<%=exprDataList.size()-1%>">

			<div class="row">
				<div class="col-lg-12">
					<div class="card card-outline">
						<div class="card-header">
							<h4 class="card-title">Experimental Values Input</h4>
							<div class="card-tools">
								<button type="button" class="btn btn-icon btn-sm" onclick="temporarySave();">중간저장</button>
							</div>
						</div>

						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap table-hover" id="experimentDataTable">
									<c:if test="${exprDataList == null}">
										<div class="loading-container" style="position: absolute; left: 50%; top: 50%;">
										    <div class="loading"></div>
										    <div id="loading-text"><h5>데이터 조회중</h5><h6>(최대 10분  소요)</h6></div>
										</div>
									</c:if>

									<c:if test="${exprDataList != null}">
										<div class="loading-container" style="position: absolute; left: 50%; top: 50%; display: none;">
										    <div class="loading"></div>
										    <div id="loading-text"><h5>데이터 조회중</h5><h6>(최대 10분  소요)</h6></div>
										</div>
									</c:if>
									<thead>
										<%
											DataMap exprDataMap = null;
											int stpColumncount = param.getInt("stpColumncount");
											int selectStp3TotYn = param.getInt("selectStp3TotYn");
											if(exprDataList != null && exprDataList.size() > 0){

										%>
										<tr class="">
											<th class="text-center"></th>
											<%
												exprDataMap = (DataMap)exprDataList.get(0);

												for(int i=1; i <= 13; i++){
													String exprHeader = exprDataMap.getString("C"+i);
													if(!exprHeader.equals("NULL")){
											%>
														<th class="text-center"><input type="hidden" name="exprHeader" value="<%=exprHeader %>"><%=exprHeader %></th>
											<%
													}
												}
											%>
										</tr>
										<%
											}
										%>
									</thead>
									<tbody>
										<%
											for(int i = 1; i < exprDataList.size(); i++){
												exprDataMap = (DataMap)exprDataList.get(i);
										%>
											<tr class="">
												<td class="text-center"><%=i %></td>
											<%
												for(int j=1; j <= 13 ; j++){
													String exprData = exprDataMap.getString("C"+j);
													if(!exprData.equals("NULL")){
														if(j < selectStp3TotYn +1){
											%>
															<td class="text-center"><input type="hidden" name="exprVal_<%=i-1 %>" value="<%=exprData %>"><%=exprData %></td>
											<%
														} else{
											%>
															<td class="text-center"><input type="text" class="form-control text-center exprVal"  name="exprVal_<%=i-1 %>" class="exprVal" value="<%=exprData %>"></td>
											<%
														}
													}
												}
											%>
											</tr>
										<%
											}
										%>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>