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

	//목록
	function fnGoList(){
		document.location.href="/admin/access/"+fnSysMappingCode()+"selectPageListAccessMgt.do";
	}

	// 등록
	function fnGoInsert(){

		// 필수값 확인
		if($('[name=ip]').val() == ''){
			alert('아이피를 입력해주세요.');
			$('[name=ip]').focus();
			return;
		}

		if(confirm('등록하시겠습니까?')){
			$('#aform').attr({action:'/admin/access/'+fnSysMappingCode()+'insertAccessMgt.do', method:'post'}).submit();
		}
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/access/insertAccessMgt.do">
					<input type="hidden" name="bbs_seq" />
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
												<input type="text" class="form-control" name="ip" placeholder="아이피" maxlength="100" />
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
									<button type="button" class="btn bg-gradient-success" onclick="fnGoInsert(); return false;">확인</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
