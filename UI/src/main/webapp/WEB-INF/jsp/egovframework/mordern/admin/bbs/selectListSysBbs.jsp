<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<%-- 해당 페이지는 사용하지 않아 기존소스에서 변경하지 않음 추후 사용할시에는 맞게끔 변경필요 --%>
<script type="text/javascript" src="/common/ckeditor/ckeditor.js" charset="UTF-8" ></script>
<script type="text/javascript">
//<![CDATA[
	$(function(){
		searchBbsCode("${requestScope.param.sys_code}");

		//기관 셀렉트 박스 변경시 게시판 코드 조회
		$('[name=sys_code]').on({'change' : function(){
				searchBbsCode($(this).val());
			}
		});


		if ("${sysMappingCode}" == "") {
			$('[name=sys_code]').html("${CacheCommboUtil:getComboStr(UPCODE_SYS_CODE, 'CODE', 'CODE_NM', requestScope.param.sys_code, '시스템')}");
			$("form").attr("action", "/admin/bbs/changeSysBbs.do");
		} else {
			$('[name=sys_code]').html("${CacheCommboUtil:getEqulesComboStr(UPCODE_SYS_CODE, requestScope.param.sys_mapping_code, 'CODE', 'CODE_NM', requestScope.param.sys_code, '')}");
			$("form").attr("action", "/admin/bbs/"+fnSysMappingCode()+"changeSysBbs.do");
			searchBbsCode($('[name=sys_code]').val());
		}
	});

	var bbs_code = "${requestScope.param.bbs_code}";
	console.log("bbsseq = [${requestScope.param.bbs_seq}]");

	//게시판 코드 조회
	function searchBbsCode(sysCode){
		var req = {
				sys_code : sysCode
		};

		var selected = "";

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/admin/bbs/'+fnSysMappingCode()+'selectListBbsCodeAjax.do',
			data : req,
			success : function(param) {
				if(param.resultStats.resultCode == "error"){
					alert(param.resultStats.resultMsg);
					return;
				}

				if(param.bbsCodeList.length == 0){
					$("select[name=bbs_code]").empty().append('<option value="">게시판</option>') ;
					$('select[name=bbs_code]').attr('disabled',true);
					return;
				}else{
					$("select[name=bbs_code]").empty().append('<option value="">게시판</option>') ;
					$('select[name=bbs_code]').attr('disabled',false);
					var bbsMngInfo;
					var selectOption;
					for(var i = 0; i < param.bbsCodeList.length; i++){
							bbsMngInfo = param.bbsCodeList[i];
							if(bbs_code == bbsMngInfo.BBS_CODE){
								selected = "selected";
							}else{
								selected = "";
							}
							$('select[name=bbs_code]').append("<option value='" + bbsMngInfo.BBS_CODE + "'" + selected + ">" + bbsMngInfo.BBS_NM + "</option>");
					}
				}
				//검색 조건 초기화
				bbs_code = "";
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	}

	function fnClose(){
		window.close();
	}

	//게시물  이동 버튼 클릭
	function fnSelect(){
		if($("[name=sys_code]").val() == ""){
			alert("시스템을 선택해 주세요.");
			$("[name=sys_code]").focus();
			return;
		}
		if($("[name=bbs_code]").val() == ""){
			alert("게시판을 선택해 주세요.");
			$("[name=bbs_code]").focus();
			return;
		}

		if(confirm("게시물을 해당 기관 및 게시판으로 변경 하시겠습니까?\n변경시에는 데이터 손실이 있을 수 있습니다.")){
			document.aform.target = "main"; document.aform.submit();
			window.close();
		}
	}
//]]>
</script>
<div class="wrapper">
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>게시판 조회</h1>
		</section>
		<!-- Main content -->
		<section class="content">
			<div class="row">
				<div class="col-lg-12">
					<form role="form" id="aform" name="aform" method="post" action="/admin/bbs/changeSysBbs.do">
						<input type="hidden" name="bbs_seq" value="${param.bbs_seq}" />
						<div class="card card-info card-outline">
							<div class="card-body">
								<div class="text-right">
									<div class="form-group col-lg-12">
										<select name="sys_code" class="form-control ">
										</select>
									</div>
								</div>
								<div class="text-right">
									<div class="form-group col-lg-12">
										<select name="bbs_code" class="form-control ">
											<option value="">게시판</option>
										</select>
									</div>
								</div>
							</div>
							<div class="card-footer">
								<div class="form-row justify-content-end">
									<div class="form-inline">
										<button type="button" class="btn bg-gradient-success mr-2" onclick="fnSelect(); return false;">선택</button>
										<button type="button" class="btn bg-gradient-secondary" onclick="fnClose(); return false;">닫기</button>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</section>
	</div>
</div>
