<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<script type="text/javascript">
//<![CDATA[
	//이동
	function fnManufacturingStart(){
		$("#aform").attr({action:"/pharmai/bio/manufacturing/selectPageListChoiceFormulation.do", method:'post'}).submit();
	}

	//목록
	function fnManufacturingList(){
		url = "/pharmai/bio/manufacturing/selectPageListManufacturing.do"
		location.href=url;
	}


//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="">
					<div class="card card-info card-outline">
						<div class="card-header">
							<h4>Manufacturing 시작하기</h4>
						</div>
						<div class="card-body">
							<div class="row">
								<div class="col-lg-6">
									<div class="card" style="border-radius : 30px;">
									  <div class="card-body d-flex flex-column formulation-start overlay1" style="height: 510px; background-color : #444444; border-radius: 30px; ">
									    <img class="card-img-top" src="/common/images/logo/logo_gl.svg" alt="Card image cap" height="100px" style="width: 50%; margin-left:30px;">
									    <h1 class="card-title" style="margin-left:50px;">Manufacturing 불러오기</h1>
									    <p class="card-text mt10" style="margin-left:50px;font-size: 18px;">Manufacturing 프로젝트를 불러 올 수 있습니다.</p>
									    <a href="#" class="btn btn-block btn-default btn-sm mt-auto" style=" border-radius: 15px;" onclick="fnManufacturingList();"><font size="4px">불러오기</font></a>
									  </div>
									</div>
								</div>
								<div class="col-lg-6">
									<div class="card" style="border-radius : 30px;">
									  <div class="card-body d-flex flex-column formulation-start overlay3" style="height: 510px; background-color : #444444; border-radius: 30px; ">
									    <img class="card-img-top" src="/common/images/logo/logo_gl.svg" alt="Card image cap" height="100px" style="width: 50%; margin-left:30px;">
									    <h1 class="card-title" style="margin-left:50px;">Manufacturing 시작하기</h1>
									    <p class="card-text mt10" style="margin-left:50px;font-size: 18px;">Manufacturing 프로젝트를 시작 할 수 있습니다.</p>
									    <a href="#" class="btn btn-block btn-default btn-sm mt-auto" style=" border-radius: 15px;" onclick="fnManufacturingStart();"><font size="4px">시작하기</font></a>
									  </div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>