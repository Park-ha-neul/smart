<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<jsp:useBean id="experimentList"  type="java.util.List" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="stp07List"  type="java.util.List" class="java.util.ArrayList" scope="request"/>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[

	$(document).ready(function(e){
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

		  // object를 키 이름으로 정렬하여 반환
        function sortObject2(obj){
           return obj.sort(function(a,b){
              return a.Sifting - b.Sifting;
           });
        }

		var prjct_id = $('input[name=prjct_id]').val();
		var step_new = $('input[name=step_new]').val();

		var param = {
				'prjct_id' : prjct_id
		}

		if(step_new == 'Y'){
			$.ajax({
				url : '/pharmai/bio/manufacturing/getApi6Ajax.do',
				type : 'post',
				dataType : 'json',
				data : param,
				async : false,
				success: function(response){

					var code = response.resultStats.list.code;
					if(code == "000"){
						var data = response.resultStats.list.result;
						var header = data["header"];
						var experimentData = data["experiment data"];

						var headerData = new Array();
						var exprData = new Array();

						$('input[name=exprLength]').val(experimentData.length);

						//array
						headerData = sortObject(header);

						var tag = "";
						tag += '<thead>';
						tag += '<tr>';
						tag += '	<th></th>';
						for(var i = 0; i < Object.keys(headerData).length; i++){
							if(headerData[Object.keys(headerData)[i]] != ""){
								tag += '<th class="text-center"><input type="hidden" name="exprHeader" value="' + headerData[Object.keys(headerData)[i]] + '">' + headerData[Object.keys(headerData)[i]] + '</th>'
							}
						}

						tag += '</tr>';
						tag += '</thead>';

						tag += '<tbody>';

						var exprData = sortObject2(experimentData);

						for(var i = 0; i < exprData.length; i++ ){
							var exprDatas =  exprData[i];
							tag += '<tr class="">';
 							tag += '	<td>' + (i + 1) + '</td>';

 							for(var j = 0; j < Object.keys(headerData).length; j++){
								if(headerData[Object.keys(headerData)[j]] != ""){
 									if( j < Object.keys(experimentData[i]).length){
 										tag += '	<td><input type="hidden" name="exprVal_'+i+'" value="' + exprDatas[Object.keys(exprDatas)[j]] + '">'+ exprDatas[Object.keys(exprDatas)[j]] +'</td>';
 									}else{
 										tag += '	<td><input type="text" style="border:none; text-align:center;" name="exprVal_'+i+'" class="exprVal form-control"></td>';
 									}
 								}
							}
 							tag += '</tr>';

						}

						tag += '</tbody>';

						$("#api7render").append(tag);


					}else{
						var prjct_id = $('input[name=prjct_id]').val();
						var msg = response.resultStats.list.msg;
						var redirectUrl = "/pharmai/bio/manufacturing/";
						switch (code){
							//스텝3
							case '010':
							case '011':
							case '012':
							case '013':
							case '015':
							case '016':
							case '999':
								redirectUrl += "selectManu_CPP_Factor.do?prjct_id="+prjct_id;
								break;
							//스텝4
							case '001':
							case '002':
							case '014':
								redirectUrl += "selectManu_CQAs.do?prjct_id="+prjct_id;
								break;
							default:
								redirectUrl += "selectManu_CQAs.do?prjct_id="+prjct_id;
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
				$('#aform').attr({action:'/pharmai/bio/manufacturing/saveTemporary.do'}).submit();
			}
		}else{
			$('#aform').attr({action:'/pharmai/bio/manufacturing/saveTemporary.do'}).submit();
		}

	}

//]]>
</script>


<jsp:include page="header.jsp" flush="false"/>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="">
					<input type="hidden" name="status" value="08">
					<input type="hidden" name="prjct_id" value="<%=param.getString("prjct_id") %>">
					<input type="hidden" name="step_new" value="<%=param.getString("step_new") %>">
					<input type="hidden" name="next_data" value="<%=param.getString("next_data") %>">
					<input type="hidden" name="exprLength" value ="<%=experimentList.size() -1 %>">

					<div class="card card-info card-outline">
						<div class="card-header">
							<h4 class="card-title">실험치 입력</h4>
							<div class="card-tools">
								<button type="button" class="btn btn-icon btn-sm" onclick="temporarySave();">중간저장</button>
							</div>

						</div>
						<div class="card-body">
							<div class="table-responsive">
								<%
									DataMap experimentMap = null;
									if(experimentList.size() != 0){
								%>
									<table class="table text-nowrap table-hover">
										<thead>
											<tr>
												<th></th>
												<%
													for(int i = 0; i < experimentList.size(); i++ ){
														experimentMap = (DataMap) experimentList.get(i);
														if(experimentMap.getString("TYPE").equals("H")){
															for(int j = 1; j <=13; j++){
																if(!experimentMap.getString("C" + j).equals("NULL")){
												%>
																	<th class="text-center"><input type="hidden" name="exprHeader" value="<%= experimentMap.getString("C" + j) %>"><%= experimentMap.getString("C" + j) %></th>
												<%
																}
															}

														}

													}
												%>


											</tr>
										</thead>

										<tbody>
											<%
												experimentMap = new DataMap();
												DataMap stp07DataMap = new DataMap();

												for(int i = 0; i< experimentList.size(); i++ ){
													experimentMap = (DataMap) experimentList.get(i);
													if(experimentMap.getString("TYPE").equals("V")){
											%>
														<tr>
															<td class="text-center"><%= i %></td>
											<%
														for(int j = 1; j <= 13; j++){
															String experiment = experimentMap.getString("C" + j);
															if(!experiment.equals("NULL")){
																if(j < param.getInt("stp06count") +1 ){
											%>
																	<td><input type="hidden" name="exprVal_<%=i -1 %>" value="<%=experiment %>"><%=experiment %></td>
											<%
																}else{
											%>
																	<td><input type="text" class="form-control text-center exprVal" name="exprVal_<%=i -1 %>" value="<%=experiment %>" ></td>
											<%
																}
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
								<%

									}
								%>

								<c:if test="${experimentList == null || experimentList.size() == 0 }">
									<table id="api7render" class="table text-nowrap table-hover"></table>
								</c:if>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>