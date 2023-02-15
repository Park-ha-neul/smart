<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	$(function(){
	});

	// 목록
	function fnGoList(){
		$("#aform").attr({action:"/admin/access"+fnSysMappingCode()+"/selectPageListAccessMgt.do", method:'post'}).submit();
	}

	// 수정폼 이동
	function fnGoUpdateForm(){
		$("#aform").attr({action:"/admin/access/"+fnSysMappingCode()+"updateFormAccessMgt.do", method:'get'}).submit();
	}

	// 삭제
	function fnGoDelete(){
		if(confirm("삭제하시겠습니까?")){
			$("#aform").attr({action:"/admin/access/"+fnSysMappingCode()+"deleteAccessMgt.do", method:'post'}).submit();
		}
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/access/updateFormAccessMgt.do">
					<input type="hidden" name="ip" value="${resultMap.IP}" />
					<input type="hidden" name="currentPage" value="${requestScope.param.currentPage}"/>
					<%-- 접근 IP --%>
					<div class="card card-info card-outline">
						<div class="card-header">
							<h3 class="card-title">접근 IP</h3>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
										<colgroup>
											<col style="width:15%;">
											<col style="width:85%;">
										</colgroup>
										<tbody>
											<tr>
												<th>IP</th>
												<td>
													${resultMap.IP}
												</td>
											</tr>
										</tbody>
									</table>
							</div>
						</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnGoList(); return false;">목록</button>
									<button type="button" class="btn bg-gradient-success mr-2" onclick="fnGoUpdateForm(); return false;">수정</button>
									<button type="button" class="btn bg-gradient-danger" onclick="fnGoDelete(); return false;">삭제</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
