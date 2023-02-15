<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	$(function(){
	});
	// 상세
	function fnDetail(){
		$('#aform').attr({action:'/admin/mber/selectMberMgt.do', method:'post'}).submit();
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
			$('#aform').attr({action:'/admin/mber/updateMberMgt.do', method:'post'}).submit();
		}
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/mber/updateMberMgt.do">
					<input type="hidden" name="user_no" value="${resultMap.USER_NO}" />
					<input type="hidden" name="sch_type" value="${requestScope.param.sch_type}" />
					<input type="hidden" name="sch_text" value="${requestScope.param.sch_text}" />
					<input type="hidden" name="currentPage" value="${requestScope.param.currentPage}"/>

					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="table-responsive">
									<table class="table text-nowrap">
										<colgroup>
											<col style="width:15%;">
											<col style="width:35%;">
											<col style="width:15%;">
											<col style="width:35%;">
										</colgroup>
										<tbody>
											<tr>
												<th class="required_field">아이디</th>
												<td>
													<input type="text" class="form-control" name="id" placeholder="아이디" maxlength="50" value="${resultMap.ID}" />
												</td>
												<th class="required_field">이름</th>
												<td>
													<input type="text" class="form-control" name="nm" placeholder="이름" maxlength="50" value="${resultMap.NM}" />
												</td>
											</tr>
											<tr>
												<th class="required_field">비밀번호</th>
												<td>
													<input type="password" class="form-control" name="password" placeholder="비밀번호" maxlength="50" />
												</td>
												<th class="required_field">회원 상태 코드</th>
												<td>
													<select class="form-control" name="user_sttus_code" id="user_sttus_code" >
														${CacheCommboUtil:getComboStr(UPCODE_USER_STTUS, 'CODE', 'CODE_NM', resultMap.USER_STTUS_CODE, '')}
													</select>
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
