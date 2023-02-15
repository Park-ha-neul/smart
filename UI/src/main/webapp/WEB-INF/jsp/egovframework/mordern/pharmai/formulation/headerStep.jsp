<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Util" uri="/WEB-INF/tlds/Util.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">

<script type="text/javascript">
	var isChange = false;

	function arr2Create(rows, columns){
		var arr = new Array(rows);
		for(var i =0; i< arr.length; i++){
			arr[i] = new Array(columns);
		}

		return arr;
	}

	$(document).ready(function(){
		isChange = false;

		// input text에 키가 눌렸을 경우
		$("input[type=text]").keypress(function(event){
			isChange = true;
		});

		$("input[name^=exprVal]").keydown(function(event){
			isChange = true;
		});

 		$("input[name=startRange]").keydown(function(event){
			isChange = true;
		});

 		$("input[name=endRange]").keydown(function(event){
			isChange = true;
		});

		//select에 change event가 일어날 경우
		$("select").change(function(){
			isChange  = true;
		});

		// checkbox 에 change event가 일어날 경우
		$("input[type=checkbox]").on('change', function(){
			isChange =true;
		});

		var st = document.forms['aform'].elements['status'].value;
		var step_new = document.forms['aform'].elements['step_new'].value;
		var prjct_id = document.forms['aform'].elements['prjct_id'].value;

		if (step_new == 'Y'){

			if(prjct_id == null || prjct_id == ""){
				$(".NmChange").html('프로젝트 생성');
			}else{
				$(".NmChange").html('프로젝트 이름 변경');
			}

		}else{
			$(".NmChange").html('프로젝트 이름 변경');
		}

		$("#step"+st).removeClass('btn-default');
		$("#step"+st).addClass('btn-location');

		if(st == '01'){
			$("#prevBtn").css("visibility","hidden");
		} else if(st == '06'){
			$("#nextBtn").css("visibility","hidden");
		}

	});

	function f_link_previous() {

		var st = document.forms['aform'].elements['status'].value;
		var preSt = 0;

		switch (st) {
		  case '02':
		    location.href="selectInput.do?prjct_id=" + document.aform.prjct_id.value;
			break;

		  case '03':
		    location.href="selectRoutes.do";
			break;

		  case '04':
		 	location.href="selectExcipient.do";
			break;

		  case '05':
			 location.href="selectCQAsList.do";
			break;

		  case '06':
			   location.href="selectExperiment.do";
			break;

		  default:

		}

	}

	function f_link_next() {

		var st = document.forms['aform'].elements['status'].value;
		var nextSt = 0;
		var nextUrl;
		var saveUrl;

		switch (st) {
		  case '01':
			nextUrl="/pharmai/chemical/formulation/selectRoutes.do";
			saveUrl="/pharmai/formulation/saveStep1.do";
			document.aform.projectName.value = $('#projectName').val();

			if($("[name=step_new]").val() == "Y"){ /* 페이지에 기존 실험 정보가 없을때(저장된 데이터가 없을경우)  */
				if(!fnCheckFormulationStep1()) return;

				fnSave(saveUrl);
			}else{ /*  저장되어 있는 정보가 있을경우  */
				if(!fnCheckFormulationStep1()) return;
				if(isChange){ /* 저장되어있는 정보가 변경 됐을때 */
					if($("[name=next_data]").val() == "Y"){
						if(confirm("이전에 등록한 실험 data가 존재합니다. 이전 실험data를 초기화 하시겠습니까?")){
							fnSave(saveUrl);
						}
					}else{
						fnSave(saveUrl);
					}
				}else{
					location.href=nextUrl;
				}

			}
			break;

		  case '02':
			nextUrl="/pharmai/chemical/formulation/selectExcipient.do";
			saveUrl="/pharmai/formulation/saveStep2.do";

			if($("[name=step_new]").val() == "Y"){
				if(!fnCheckFormulationStep2()) return;

				fnSave(saveUrl);
			}else{
				if(isChange){
					//필수 입력 값 확인
					if(!fnCheckFormulationStep2()) return;

					if($("[name=next_data]").val() == "Y"){
						if(confirm("이전에 등록한 실험 data가 존재합니다. 이전 실험data를 초기화 하시겠습니까?")){
							fnSave(saveUrl);
						}
					}else{
						fnSave(saveUrl);
					}
				}else{
					location.href = nextUrl;
				}

			}
			break;

		  case '03':
		  	nextUrl="/pharmai/chemical/formulation/selectCQAsList.do";
			saveUrl="/pharmai/formulation/saveStep3.do";

			if($("[name=step_new]").val() == "Y"){
				if(!fnCheckFormulationStep3()) return;

				fnSave(saveUrl);
			}else{
				if(isChange){
					//필수 입력 값 확인
					if(!fnCheckFormulationStep3()) return;

					if($("[name=next_data]").val() == "Y"){
						if(confirm("이전에 등록한 실험 data가 존재합니다. 이전 실험data를 초기화 하시겠습니까?")){
							fnSave(saveUrl);
						}
					}else{
						fnSave(saveUrl);
					}
				}else{
					location.href=nextUrl;
				}

			}
			break;

		  case '04':

			nextUrl="/pharmai/chemical/formulation/selectExperiment.do";
			saveUrl="/pharmai/formulation/saveStep4.do";

			if($("[name=step_new]").val() == "Y"){
				if(!fnCheckFormulationStep4()) return;

				fnSave(saveUrl);
			}else{
				if(isChange){
					//필수 입력 값 확인
					if(!fnCheckFormulationStep4()) return;

					if($("[name=next_data]").val() == "Y"){
						if(confirm("이전에 등록한 실험 data가 존재합니다. 이전 실험data를 초기화 하시겠습니까?")){
							fnSave(saveUrl);
						}
					}else{
						fnSave(saveUrl);
					}
				}else{
					location.href=nextUrl;
				}

			}
			break;

		  case '05':
			nextUrl="/pharmai/chemical/formulation/selectDoE.do";
			saveUrl="/pharmai/formulation/saveStep5.do";

			if($("[name=step_new]").val() == "Y"){
				if(!fnCheckFormulationStep5()) return;

				fnSave(saveUrl);
			}else{
				if(isChange){
					if($("[name=next_data]").val() == "Y"){
						//필수 입력 값 확인
						if(!fnCheckFormulationStep5()) return;

						if(confirm("이전에 등록한 실험 data가 존재합니다. 이전 실험data를 초기화 하시겠습니까?")){
							fnSave(saveUrl);
						}
					}else{
						if(!fnCheckFormulationStep5()) return;
						fnSave(saveUrl);
					}
				}else{
					if(!fnCheckFormulationStep5()) return;
					location.href=nextUrl;
				}

			}
	  		break;

		  default:

		}
	}


	function fnCheckFormulationStep1() {

		var fo = document.aform;

		var projectName = $('#projectName').val();
		var prjct_id = $("[name=prjct_id]").val();

		if(projectName == ""){
			alert("프로젝트 이름을 입력해 주세요 ");
			$("#projectName").focus();
			return;
		}

		if(prjct_id == "") {
			alert("프로젝트 생성 버튼을 클릭해 주세요.");
			$('.input-group-text').attr('style', 'border: 1px solid #82D580 !important');
			return;
		}

  		if(fo.inputType.value == ""){
  			alert("입력값이 없습니다. 구조를 선택해주세요");
  			return;
  		}else{
  			if(fo.inputType.value == 'sdf'){

				if(fo.ntsysFileDocId.value == ""){
					alert('파일을 등록해주세요.');
					returns;
				}
  			}else if(fo.inputType.value == 'smiles'){
  				if(fo.smiles.value == ""){
  					alert("smiles String 값을 입력해주세요");
  					return;
  				}
  				if(fo.return_smiles.value == ""){
  					alert("해당 smiles String 에 근사한 smiles 가 존재하지 않습니다.\n 다시 시도해 주세요.");
  					return;
  				}
  			}else{
  				if(fo.chem_nm.value == ""){
  					alert("chemical name을 입력해 주세요");
  					return;
  				}
  				if(fo.return_smiles.value == ""){
  					alert("해당 smiles String 에 근사한 smiles 가 존재하지 않습니다.\n 다시 시도해 주세요.");
  					return;
  				}

  			}
  		}

  		return true;


	}


	function fnCheckFormulationStep2(){

		if(!$('[name=routes] > option:selected').val()) {
			alert("투여 경로를 선택해주세요.");
			$("[name=routes]").focus();
			return;
		}

		if($('input[type="checkbox"][name="shape_list"]:checked').length < 1){
			alert("부형제를 선택해주세요.");
			$("[name=shape_recommendation]").focus();
			return;
		}

		if($("[name=volume]").val() == ""){
			alert("용량을 입력해 주세요.");
			$("[name=volume]").focus();
			return;
		}

		if($("[name=unit]").val() == ""){
			alert("용량 단위를 선택해 주세요.");
			$("[name=unit]").focus();
			return;
		}


		var volume = document.aform.volume.value;
		var pattern = /^[0-9]+(.[0-9]+)?$/;

		if(!pattern.test(volume)){
			alert("숫자 및 실수만 입력 가능합니다.");
			$("[name=volume]").focus();
			return;
		}

		return true;
	}

	function fnCheckFormulationStep3(){
		var count = $("[name=filter]:checked").length;
		if(count <= 1){
			$(this).prop("checked", false);
			alert("2개 이상 선택 가능합니다.");
			return;
		}
		var startRangeLength = $('input[name=startRange]').length;
		var endRangeLength = $('input[name=endRange]').length;

		for(var i = 0; i< startRangeLength; i++ ){
			  if ($("[name=startRange]").eq(i).prop("readonly") == false) {
					var readFalseVal = $('input[name=startRange]').eq(i).val();
					if(readFalseVal == null || readFalseVal == ""){
						alert("사용범위 시작 값을 입력해주세요");
						$('input[name=startRange]').eq(i).focus();
						return;
					}

					var pattern = /^[0-9]+(.[0-9]+)?$/;
					if(!pattern.test(readFalseVal)){
						alert("숫자 및 실수만 입력 가능합니다.");
						$('input[name=startRange]').eq(i).focus();
						return;
					}
			 }


		}

		for(var i = 0; i< endRangeLength; i++ ){
			  if ($("[name=endRange]").eq(i).prop("readonly") == false) {
					var readFalseVal = $('input[name=endRange]').eq(i).val();
					if(readFalseVal == null || readFalseVal == ""){
						alert("사용범위 종료 값을 입력해주세요.");
						$('input[name=endRange]').eq(i).focus();
						return;
					}

					var pattern = /^[0-9]+(.[0-9]+)?$/;
					if(!pattern.test(readFalseVal)){
						alert("숫자 및 실수만 입력 가능합니다.");
						$('input[name=endRange]').eq(i).focus();
						return;
					}
			 }

		}

		for(var i = 0; i < startRangeLength; i++){
			 if ($("[name=endRange]").eq(i).prop("readonly") == false && $("[name=startRange]").eq(i).prop("readonly") == false) {
					var startRangeValue = $('input[name=startRange]').eq(i).val();
					var endRangeValue = $('input[name=endRange]').eq(i).val();
					startRangeValue = Number(startRangeValue);
					endRangeValue = Number(endRangeValue);
					if(startRangeValue >= endRangeValue){
						alert("사용범위 시작 값이 종료 값 보다 클수가 없습니다.");
						$('input[name=startRange]').eq(i).focus();
						return;
					}
			 }

		}


		return true;

	}

	function fnCheckFormulationStep4(){

		if($('input[type="checkbox"][name="cqas_list"]:checked').length < 1){
			alert("CQAs를 선택해주세요.");
			return;
		}

		return true;
	}

	function fnCheckFormulationStep5(){
		var exprVal = $(".exprVal").length;

		for(var i=0; i<exprVal; i++){

			if($(".exprVal").eq(i).val() == ""){
				alert("실측 실험치를 입력해 주세요.");
				$(".exprVal").eq(i).focus();
				return;
			}

			var check = $(".exprVal").eq(i).val();
			var pattern = /^[0-9]+(.[0-9]+)?$/;

			if(!pattern.test(check)){
				alert("숫자 및 실수만 입력 가능합니다.");
				$('.exprVal').eq(i).focus();
				return;
			}

			if(check.indexOf('.') != -1){
				var decimal_point = check.substring(check.indexOf('.') + 1);

				if(decimal_point.length > 2) {
					alert('소수 둘째자리까지만 입력됩니다.');
					$(".exprVal").eq(i).focus();
					return;
				}
			 }
		}


		return true;
	}

	function fnCheckFormulationStep6(){
		var target_scoreLen = $(".target_score").length;

	 	for(var i = 0; i < target_scoreLen; i ++) {

	 		if($(".target_score").eq(i).val() == "") {
	 			alert("빈값이 존재 합니다. 입력해 주세요.");
	 			$(".target_score").eq(i).focus();
	 			return;
	 		}

	 		var check = $(".target_score").eq(i).val();
			var pattern = /^[0-9]+(.[0-9]+)?$/;

	 		if(!pattern.test(check)){
				alert("숫자 및 실수만 입력 가능합니다.");
				$('.target_score').eq(i).focus();
				return;
			}

	 	}


		return true;
	}


	function fnSave(saveUrl){
		$("#aform").attr({action : saveUrl, method : 'post'}).submit();
	}


	function fnPrjCreate(){
		var projectName = $("#projectName").val();
		var step_new = $("[name=step_new]").val();
		var prjct_id = $("[name=prjct_id]").val();

		if(projectName == ""){
			alert("프로젝트 이름을 입력해 주세요 ");
			$("#projectName").focus();
			return;
		}

		$(".NmChange").html('프로젝트 이름 변경');

		$('.input-group-text').attr('style', 'border: 1px solid #999999 !important');

		var req = {
				projectName : projectName,
				step_new : step_new,
				prjct_id : prjct_id
			};

		jQuery.ajax({
			type : 'POST',
			dataType : 'json',
			url : '/pharmai/formulation/projectCreateAjax.do',
			data : req,
			global: false, // 추가
			success : function(response) {

				var prjct_id = response.prjct_id;
				var fo = document.aform;

				if(response.resultStats.prjStatus == "N"){
					$('.all_CardContent').find(':input').prop('disabled', false);
					fo.prjct_id.value = prjct_id;
					fo.projectName.value = projectName;
				}else{
					alert("변경되었습니다.");
					fo.prjct_id.value = prjct_id;
					fo.projectName.value = projectName;
				}

			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});


	}
</script>

<div class="content headerContent" id="headerMenu">
	<div class="stepwizard col-md-offset-12 ">
    	<div class="stepwizard-row setup-panel" id="stepwizard">
   			<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle1" id="step01"><strong>STEP1</strong></button>
	      	</div>
				<span><img src="/common/images/common/arrow.png" alt=""/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle1" id="step02" ><strong>STEP2</strong></button>
	      	</div>
	      		<span><img src="/common/images/common/arrow.png" alt=""/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle1" id="step03" ><strong>STEP3</strong></button>
	      	</div>
	      		<span><img src="/common/images/common/arrow.png" alt=""/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle1" id="step04"><strong>STEP4</strong></button>
	      	</div>
	      		<span><img src="/common/images/common/arrow.png" alt=""/></span>
     	 	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle1" id="step05"><strong>STEP5</strong></button>
	      	</div>
	      		<span><img src="/common/images/common/arrow.png" alt=""/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle1" id="step06"><strong>STEP6</strong></button>
	      	</div>
    	</div>
 	</div>
    <div class="stepwizard col-md-12" style="margin-top: 20px;">
		<div class="form-row justify-content-between">
	    	<div class="form-inline">
	    		<div class="input-group input-group-sm mb-1" align="left">
		   			<div>
		   				<button class="btn hover1 prevBtn btn-grey btn-sm" type="button" id="prevBtn" onclick="f_link_previous(); return false;"> <span> < </span> Previous</button>
					</div>
		   		</div>
		   	</div>
		   	<div class="form-inline">
		   		<div class="input-group input-group-sm mb-1">
		   			<div class="input-group-append">
		   				<input class="text_outline_none input_project_nm form-control" size="50" type="text" id="projectName" name="projectName" value="${pjtData.PRJCT_NM }" placeholder="프로젝트 이름을 입력해주세요." >
		   				<div class="input-group-text" onclick="fnPrjCreate();">
				   			<a href="#" class="a" role="button" style="width: 100%;">
				   				<span class="NmChange">프로젝트 생성</span>
							</a>
						</div>
					</div>
		   		</div>
		   	</div>
		   	<div class="form-inline">
		   		<div class="input-group input-group-sm mb-1" align="right">
		   			<div>
		   				<button class="btn hover2 nextBtn btn-grey btn-sm" type="button" id="nextBtn" onclick="f_link_next(); return false;"> Next <span> > </span></button>
					</div>
		   		</div>
		   	</div>
		</div>
	</div>
</div>

