<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="DateUtil" uri="/WEB-INF/tlds/DateUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[

	$(function(){
		//날짜선택 달력
		$('[name=sch_date]').datetimepicker({
			locale : 'ko', 	// 화면에 출력될 언어를 한국어로 설정한다.
			format : 'YYYY',
			useCurrent: false, 	//Important! See issue #1075
			sideBySide : false,
		});
	});

	//검색
	function fnSearch(){
		$("#aform").attr({action:"/admin/stats/selectListVisitMonthPV.do", method:'get'}).submit();
	}

	// 엑셀다운로드
	function fnDownloadExcel(){
		$('#aform').attr({action:'/admin/stats/selectListVisitMonthExcelPV.do', method:'get'}).submit();
	}
//]]>
</script>
<div class="wrapper">

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>행사일정</h1>
		</section>

		<!-- Main content -->
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<form role="form" id="aform" method="post" action="/admin/diet/month/selectPageListMonthMgt.do">
						<input type="hidden" name="ym" value=""/>
						<input type="hidden" name="diet_se_code" value=""/>

						<div class="box box-primary">
							<div class="box-header">
								<div class="text-right form-inline">
									<div class="form-group">
										<div class="input-group input-group-sm">
											<input type="text" class="form-control" name="sch_date" value="${requestScope.param.sch_date}" />
											<div class="input-group-btn">
												<button type="button" class="btn btn-sm btn-default" onclick="fnSearch(); return false;"><i class="fa fa-search"></i></button>
											</div>
										</div>
									</div>
								</div>
							</div><!-- /.box-header -->
							<div class="box-body no-pad-top table-responsive">
								<table class="table table-hover table-bordered">
									<colgroup>
										<col width="100px" />
										<col width="100px" />
										<col width="*" />
									</colgroup>
									<thead>
										<tr>
											<th class="text-center">년</th>
											<th class="text-center">월</th>
											<th class="text-center">방문수</th>
										</tr>
									</thead>
									<tbody>
									<c:forEach var="item" items="${resultList}" varStatus="status">
										<tr>
											<td class="text-center">${item.YY}</td>
											<td class="text-center">${item.MM}</td>
											<td class="text-center"><fmt:formatNumber value="${item.CNT}" pattern="#,###,###" /></td>
										</tr>
									</c:forEach>
									<c:if test="${fn:length(resultList) == 0}">
										<tr>
											<td class="text-center" colspan="3" ><spring:message code="msg.data.empty" /></td>
										</tr>
									</c:if>
									</tbody>
								</table>
							</div><!-- /.box-body -->
							<div class="box-footer clearfix text-center">
								${navigationBar}
							</div>
							<div class="box-footer clearfix no-pad-top">
								<div class="pull-right">
									<button type="button" class="btn btn-success" onclick="fnDownloadExcel(); return false;"><i class="fa fa-file-excel-o"></i> 엑셀 다운로드</button>
								</div>
							</div>
						</div><!-- /.box -->
					</form>
				</div><!--  /.col-xs-12 -->
			</div><!-- ./row -->
		</section><!-- /.content -->
	</div><!-- /.content-wrapper -->

</div><!-- ./wrapper -->
