<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="egovframework.framework.common.object.DataMap"%>
<%@ page import="egovframework.framework.common.util.CommboUtil"%>

<jsp:useBean id="param"
	class="egovframework.framework.common.object.DataMap" scope="request" />
<jsp:useBean id="resultMap"
	class="egovframework.framework.common.object.DataMap" scope="request" />
<%-- <jsp:useBean id="now" class="java.util.Date" />

<c:set value="${resultMap.LOG_STRE_PD * (1000*60*60*24)}" var="delDate" /> <!-- 로그 보관 기간 -->
<fmt:formatDate value="${now}" pattern="yyyyMMdd" var="nowDate" /> <!-- 현재 날짜 -->
<fmt:formatDate value="${delDate}" pattern="ss" var="delDate" />
<fmt:parseNumber value="${nowDate}" integerOnly="true" var="logDate"/> --%>

<script type="text/javascript">
	//<![CDATA[

	$(function() {

	});

	function fnGoUpdate() {
		if ($("[name=storage_pd]").val() == "") {
			alert("로그 보관 기간을 입력해 주세요.");
			return;
		}

		if (confirm("로그 보관 기간을 수정하시겠습니까?")) {
			$("#aform").attr({
				action : "/admin/logstrepd/updateLogStreMgt.do",
				method : 'post'
			}).submit();
		}
	}
	//]]>
</script>
<section class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform">
					<div class="card card-info card-outline">
						<div class="card-header">
							<h3 class="card-title">로그 보관 기간 설정</h3>
						</div>
						<div class="card-header">
							<br>
							<h4 class="text-center">
								현재 로그 보관 기간은 <b> ${resultMap.LOG_STRE_PD} 일
								</b> 입니다.
							</h4>
							<p class="text-center">
								( ${resultMap.LOG_DT} 일자 이후의 데이터를 보관 중입니다. )
							</p>
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table text-nowrap">
									<colgroup>
										<col style="width: 15%;">
										<col style="width: *;">
									</colgroup>
									<tbody>
										<tr>
											<th class="text-center required_field">로그 보관 기간</th>
											<td>
												<input type="text" name="storage_pd" class="form-control numeric"  value='${resultMap.LOG_STRE_PD}' style="display: inline-block; width: 150px"> 일
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="box-footer clearfix">
							<div class="card-footer">
								<button type="button" class="btn bg-gradient-success float-right" onclick="fnGoUpdate(); return false;">
									<i class="fa fa-pencil"></i>저장
								</button>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>
<!-- /.content -->
