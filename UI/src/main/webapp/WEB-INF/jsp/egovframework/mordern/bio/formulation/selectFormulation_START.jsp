<!-- /** -->
<!--  * -->
<!--  * <PRE> -->
<!--  * 1. ClassName : 
<!--  * 2. FileName  : bio_selectFormulation_START.do-->
<!--  * 3. Package  : 
<!--  * 4. Comment  : 바이오의약품 제형설계 시작 화면 -->
<!--  * 5. 작성자   : : hnpark -->
<!--  * 6. 작성일   : 2022. 4. 21 -->
<!--  * </PRE> -->
<!--  */ -->

<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>
<script type="text/javascript">
	//<![CDATA[

	// 바이오의약품 Formulation 시작하기 
	function fnFormulationStart(){
		$("#aform").attr({action:"/pharmai/bio/formulation/selectInput.do", method:'post'}).submit();
	}

	// 바이오의약품 Formulation 불러오기
	function fnFormulationList(){
		url = "/pharmai/bio/formulation/selectPageListProject.do"
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
							<h4>Formulation 시작하기</h4>
						</div>
						<div class="card-body">
							<div class="row">
								<div class="col-lg-6">
									<div class="card" style="border-radius: 30px;">
									  <div class="card-body d-flex flex-column formulation-start overlay1" style="height: 510px; background-color: #444444; border-radius: 30px;">
									    <img class="card-img-top" src="/common/images/logo/logo_gl.svg" alt="Card image cap" height="100px" style=" width: 50%; margin-left:30px;">
									    <h1 class="card-title" style="margin-left:50px;">Formulation 불러오기</h1>
									    <p class="card-text mt10" style="margin-left:50px;font-size: 18px;">Formulation 프로젝트를 불러 올 수 있습니다.</p>
									    <a href="#" class="btn btn-block btn-default btn-sm mt-auto" style=" border-radius: 15px;" onclick="fnFormulationList();"><font size="4px">불러오기</font></a>
									  </div>
									</div>
								</div>
								<div class="col-lg-6">
									<div class="card" style="border-radius: 30px;">
									  <div class="card-body d-flex flex-column formulation-start overlay2" style="height: 510px; background-color: #444444; border-radius: 30px; background-repeat : no-repeat; background-size:100%; background-image: url('/common/images/common/pharmai_img(2).jpg') ">
									    <img class="card-img-top" src="/common/images/logo/logo_gl.svg" alt="Card image cap" height="100px" style=" width: 50%; margin-left:30px;">
									    <h1 class="card-title" style="margin-left:50px;">Formulation 시작하기</h1>
									    <p class="card-text mt10" style="margin-left:50px;font-size: 18px;">Formulation 프로젝트를 시작 할 수 있습니다.</p>
									    <a href="#" class="btn btn-block btn-default btn-sm mt-auto" style=" border-radius: 15px;" onclick="fnFormulationStart();"><font size="4px">시작하기</font></a>
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


