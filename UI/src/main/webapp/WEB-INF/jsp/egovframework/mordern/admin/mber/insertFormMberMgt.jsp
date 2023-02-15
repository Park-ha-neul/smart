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

	//목록
	function fnGoList(){
		document.location.href="/admin/mber/selectPageListMberMgt.do";
	}

	// 등록
	function fnGoInsert(){

		// 필수값 확인
		if($('[name=id]').val() == ''){
			alert('아이디를 입력해주세요.');
			$('[name=id]').focus();
			return;
		}

		if($('[name=nm]').val() == ''){
			alert('이름을 입력해주세요.');
			$('[name=nm]').focus();
			return;
		}

		if($('[name=password]').val() == ''){
			alert('비밀번호를 입력해주세요.');
			$('[name=password]').focus();
			return;
		}

		// 중복아이디 체크
		var param = {'id' : $('[name=id]').val()};
		$.ajax({
			url : '/admin/mber/selectIdExistYnAjax.do',
			type : 'post',
			dataType : 'json',
			data : param,
			success : function(response){
				if(response.resultStats.resultCode == 'success'){
					// 이미 존재하는 경우
					if(response.resultMap == 'Y'){
						alert(response.resultStats.resultMsg);
					} else {
						if(confirm('등록하시겠습니까?')){
							$('#aform').attr({action:'/admin/mber/insertMberMgt.do', method:'post'}).submit();
						}
					}
				} else {
					ajaxErrorMsg(response);
				}
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError);
			}
		});
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="/admin/mber/insertMberMgt.do">

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
													<input type="text" class="form-control" name="id" placeholder="아이디" maxlength="50" />
												</td>
												<th class="required_field">이름</th>
												<td>
													<input type="text" class="form-control" name="nm" placeholder="이름" maxlength="50" />
												</td>
											</tr>
											<tr>
												<th class="required_field">비밀번호</th>
												<td>
													<input type="password" class="form-control" name="password" placeholder="비밀번호" maxlength="50" />
												</td>
												<th class="required_field">회원 상태 코드</th>
												<td>
													<select class="form-control" name="user_sttus_code"  id="user_sttus_code" >
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
