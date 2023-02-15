<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	$(function(){
	});
	// 상세
	function fnDetail(){
		$('#aform').attr({action:'/admin/access/'+fnSysMappingCode()+'selectAccessMgt.do', method:'post'}).submit();
	}
	// 수정
	function fnGoUpdate(){

		// 필수값 확인
		if($('[name=nm]').val() == ''){
			alert('이름을 입력해주세요.');
			$('[name=nm]').focus();
			return;
		}

		if(confirm('수정하시겠습니까?')){
			$('#aform').attr({action:'/admin/access/'+fnSysMappingCode()+'updateAccessMgt.do', method:'post'}).submit();
		}
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/access/updateAccessMgt.do">
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
											<th><div class="required_field">IP</div></th>
											<td>
												<input type="text" class="form-control" name="up_ip" placeholder="아이피" maxlength="100" value="${resultMap.IP}" />
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="card-footer">
							<div class="form-row justify-content-end">
								<div class="form-inline">
									<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnDetail(); return false;">취소</button>
									<button type="button" class="btn bg-gradient-success" onclick="fnGoUpdate(); return false;">확인</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
