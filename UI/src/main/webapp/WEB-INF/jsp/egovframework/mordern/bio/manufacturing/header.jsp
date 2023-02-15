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

			if(st == 01){
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
		} else if(st == '09'){
			$("#nextBtn").css("visibility","hidden");
		}

	});

	function f_link_previous() {

		var st = document.forms['aform'].elements['status'].value;
		var preSt = 0;

		switch (st) {
		  case '02':
		    location.href="selectManu_Method.do?prjct_id=" + document.aform.prjct_id.value;
			break;

		  case '03':
		    location.href="selectManu_Process_Map.do";
			break;

		  case '04':
		 	location.href="selectManu_PHA.do";
			break;

		  case '05':
			 location.href="selectManu_FMEA.do";
			break;

		  case '06':
			   location.href="selectManu_UNIT_IMG.do";
			break;

		  case '07':
 			location.href="selectManu_CPP_Factor.do";

		    break;

		  case '08':
 			location.href="selectManu_CQAs.do";

		    break;

		  case '09':
 			location.href="selectManu_Excipent.do";

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
			nextUrl="/pharmai/bio/manufacturing/selectManu_Process_Map.do";
			saveUrl="/pharmai/bio/manufacturing/saveStep01.do";
			var projectName = $('#projectName').val();
			var fo = document.aform;
			fo.projectName.value = projectName;

			if($("[name=step_new]").val() == "Y"){ /* 페이지에 기존 실험 정보가 없을때(저장된 데이터가 없을경우)  */
				if(!fnCheckManufactStep1()) return;
				fnSave(saveUrl);
			}else{ /*  저장되어 있는 정보가 있을경우  */
				if(!fnCheckManufactStep1()) return;
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
			nextUrl="/pharmai/bio/manufacturing/selectManu_PHA.do";
			saveUrl="/pharmai/bio/manufacturing/saveStep02.do";

			if($("[name=step_new]").val() == "Y"){
				if(!fnCheckManufactStep2()) return;

				fnSave(saveUrl);
			}else{
				if(isChange){
					//필수 입력 값 확인
					if(!fnCheckManufactStep2()) return;

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
		  	nextUrl="/pharmai/bio/manufacturing/selectManu_FMEA.do";
			saveUrl="/pharmai/bio/manufacturing/saveStep03.do";

			if($("[name=step_new]").val() == "Y"){
				if(!fnCheckManufactStep3()) return;

				fnSave(saveUrl);
			}else{
				if(isChange){
					//필수 입력 값 확인
					if(!fnCheckManufactStep3()) return;

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

			nextUrl="/pharmai/bio/manufacturing/selectManu_UNIT_IMG.do";
			saveUrl="/pharmai/bio/manufacturing/saveStep04.do";

			if($("[name=step_new]").val() == "Y"){
				if(!fnCheckManufactStep4()) return;
				fnSave(saveUrl);
			}else{
				if(isChange){
					//필수 입력 값 확인
					if(!fnCheckManufactStep4()) return;

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
			nextUrl="/pharmai/bio/manufacturing/selectManu_CPP_Factor.do";
			saveUrl="/pharmai/bio/manufacturing/saveStep05.do";

			if($("[name=step_new]").val() == "Y"){
				if(!fnCheckManufactStep5()) return;

				fnSave(saveUrl);
			}else{
				if(isChange){
					if($("[name=next_data]").val() == "Y"){
						//필수 입력 값 확인
						if(!fnCheckManufactStep5()) return;

						if(confirm("이전에 등록한 실험 data가 존재합니다. 이전 실험data를 초기화 하시겠습니까?")){
							fnSave(saveUrl);
						}
					}else{
						if(!fnCheckManufactStep5()) return;
						fnSave(saveUrl);
					}
				}else{
					location.href=nextUrl;
				}

			}
	  		break;

		  case '06':

		 	nextUrl="/pharmai/bio/manufacturing/selectManu_CQAs.do";
			saveUrl="/pharmai/bio/manufacturing/saveStep06.do";

			if($("[name=step_new]").val() == "Y"){
				if(!fnCheckManufactStep6()) return;

				fnSave(saveUrl);
			}else{
				if(isChange){
					if($("[name=next_data]").val() == "Y"){
						//필수 입력 값 확인
						if(!fnCheckManufactStep6()) return;

						if(confirm("이전에 등록한 실험 data가 존재합니다. 이전 실험data를 초기화 하시겠습니까?")){
							fnSave(saveUrl);
						}
					}else{
						if(!fnCheckManufactStep6()) return;
						fnSave(saveUrl);
					}
				}else{
					location.href=nextUrl;
				}

			}
	  		break;

  			case '07':
			 	nextUrl="/pharmai/bio/manufacturing/selectManu_Excipent.do";
				saveUrl="/pharmai/bio/manufacturing/saveStep07.do";

				if($("[name=step_new]").val() == "Y"){
					if(!fnCheckManufactStep7()) return;

					fnSave(saveUrl);
				}else{
					if(isChange){
						if($("[name=next_data]").val() == "Y"){
							//필수 입력 값 확인
							if(!fnCheckManufactStep7()) return;

							if(confirm("이전에 등록한 실험 data가 존재합니다. 이전 실험data를 초기화 하시겠습니까?")){
								fnSave(saveUrl);
							}
						}else{
							if(!fnCheckManufactStep7()) return;
							fnSave(saveUrl);
						}
					}else{
						location.href=nextUrl;
					}

				}
		  		break;

  			case '08':
			 	nextUrl="/pharmai/bio/manufacturing/selectManu_Result.do";
				saveUrl="/pharmai/bio/manufacturing/saveStep08.do";

				if($("[name=step_new]").val() == "Y"){
					if(!fnCheckManufactStep8()) return;
					fnSave(saveUrl);
				}else{
					if(isChange){
						if($("[name=next_data]").val() == "Y"){
							//필수 입력 값 확인
							if(!fnCheckManufactStep8()) return;

							if(confirm("이전에 등록한 실험 data가 존재합니다. 이전 실험data를 초기화 하시겠습니까?")){
								fnSave(saveUrl);
							}
						}else{
							if(!fnCheckManufactStep8()) return;
							fnSave(saveUrl);
						}
					}else{
						if(!fnCheckManufactStep8()) return;
						location.href=nextUrl;
					}

				}
		  		break;

		}
	}


	function fnCheckManufactStep1() {

		var fo = document.aform;

		var projectName = $('[name=projectName]').val();
		var prjct_id = $("[name=prjct_id]").val();
		var manufacturing_method = $('#manufacturing_method').val();

		if(projectName == ""){
			alert("프로젝트 이름을 입력해 주세요 ");
			$('[name=projectName]').focus();
			return;
		}

		if(prjct_id == null || prjct_id == ""){
			alert("프로젝트 id가 없습니다.");
			return;
		}

		if(manufacturing_method == ""){
			alert("제조 방법을 선택해 주세요.");
			$(".manufacturing_method").focus();
			return;
		}

		return true;

	}



	function fnCheckManufactStep2() {

		var bdLength = $('input[name=bd]').length;
		var tdLength = $('input[name=td]').length;
		var ciLength = $('input[name=ci]').length;

		var count = $("input:checked[name='filter']").length;

		if(count == 0){
			alert("최소 1개 이상 선택해야합니다.");
			return;
		}

		for(var i = 0; i< bdLength; i++ ){
			  if ($("[name=checkYn]").eq(i).val() == 'Y') {

					var readFalseVal = $('input[name=bd]').eq(i).val();
					if(readFalseVal == null || readFalseVal == ""){
						alert("BD 값을 입력해주세요");
						$('input[name=bd]').eq(i).focus();
						return;
					}

					var pattern = /^[0-9]+(.[0-9]+)?$/;
					if(!pattern.test(readFalseVal)){
						alert("숫자 및 실수만 입력 가능합니다.");
						$('input[name=bd]').eq(i).focus();
						return;
					}
			 }

		}

		for(var i = 0; i< tdLength; i++ ){
			  if ($("[name=checkYn]").eq(i).val() == 'Y') {

					var readFalseVal = $('input[name=td]').eq(i).val();
					if(readFalseVal == null || readFalseVal == ""){
						alert("TD 값을 입력해주세요");
						$('input[name=td]').eq(i).focus();
						return;
					}

					var pattern = /^[0-9]+(.[0-9]+)?$/;
					if(!pattern.test(readFalseVal)){
						alert("숫자 및 실수만 입력 가능합니다.");
						$('input[name=td]').eq(i).focus();
						return;
					}
			 }

		}

		for(var i = 0; i< ciLength; i++ ){
			  if ($("[name=checkYn]").eq(i).val() == 'Y') {

					var readFalseVal = $('input[name=ci]').eq(i).val();
					if(readFalseVal == null || readFalseVal == ""){
						alert("CI 값을 입력해주세요");
						$('input[name=ci]').eq(i).focus();
						return;
					}

					var pattern = /^[0-9]+(.[0-9]+)?$/;
					if(!pattern.test(readFalseVal)){
						alert("숫자 및 실수만 입력 가능합니다.");
						$('input[name=ci]').eq(i).focus();
						return;
					}
			 }

		}


  		return true;
	}

	function fnCheckManufactStep3() {

		var selectboxLen = $('.colorStatus').length;

		for(var i = 0; i< selectboxLen; i++){
			if($('.colorStatus').eq(i).val() == ""){
				alert("빈값이 존재합니다. 단위를 선택해 주세요.");
				$('.colorStatus').eq(i).focus();
				return;
			}
		}

  		return true;


	}

	function fnCheckManufactStep4() {

		var prjct_id = $("[name=prjct_id]").val();

		if(prjct_id == null || prjct_id == ""){
			alert("프로젝트 id가 없습니다.");
			return;
		}

  		return true;


	}

	function fnCheckManufactStep5() {
		var count = $("input:checked[name='filter']").length;
		if(count == 0){
			$(this).prop("checked", false);
			alert("1개이상 선택해주세요.");
			return;
		}

  		return true;


	}

	function fnCheckManufactStep6() {
		var count = $("input:checked[name='filter']").length;
// 		if(count == 0 ){
// 			$(this).prop("checked", false);
// 			alert("1개 이상 선택 가능합니다.");
// 			return;
// 		}
		if(count <= 1 ){
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

		for(var i = 0; i < startRangeLength; i++){
			if ($("[name=endRange]").eq(i).prop("readonly") == false && $("[name=startRange]").eq(i).prop("readonly") == false) {
				var startRangeValue = $('input[name=startRange]').eq(i).val();
				var endRangeValue = $('input[name=endRange]').eq(i).val();
				startRangeValue = Number(startRangeValue);
				endRangeValue = Number(endRangeValue);
				if(startRangeValue >= endRangeValue){
					alert("사용범위 시작값이 종료값 보다 클수가 없습니다.");
					$('input[name=startRange]').eq(i).focus();
					return;
				}
			}
		}

  		return true;

	}

	function fnCheckManufactStep7() {

		if($('input[type="checkbox"][name="filter"]:checked').length < 1){
			alert("CQAs를 선택해주세요.");
			return;
		}

  		return true;


	}

	function fnCheckManufactStep8() {
		var inputCqaLen = $('.exprVal').length;
		for(var i = 0; i < inputCqaLen; i++){

			if($('.exprVal').eq(i).val() == ""){
				alert("실험치를 입력해 주세요");
				$('.exprVal').eq(i).focus();
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

	function fnCheckManufactStep9() {

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

		var prjct_id = $("[name=prjct_id]").val();
		var projectName = $("[name=projectName]").val();
		var step_new = $("[name=step_new]").val();

		var req = {
				prjct_id : prjct_id,
				projectName : projectName,
				step_new : step_new
			};

		if(projectName == ""){
			alert("프로젝트 이름을 입력 해주세요.");
			$("#projectName").focus();
			return;
		}

		$(".NmChange").html('프로젝트 이름 변경');

		jQuery.ajax({
			type : 'POST',
			dataType : 'json',
			url : '/pharmai/bio/manufacturing/projectCreateAjax.do',
			async: false,
			data : req,
			success : function(response) {
				var prjct_id = response.prjct_id;
				var projectName = response.projectName;
				var fo = document.aform;

				if(response.resultStats.prjStatus == "Y"){
		            //form 태그 데이터 삽입
					$('input[name=prjct_id]').attr('value',prjct_id);
					$('input[name=projectName]').attr('value',projectName);

					if(response.step_new == "Y") {
						alert("입력되었습니다.");
					}else {
						alert("변경되었습니다.");
					}
				}

			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});

		return;
	}
</script>

<div class="content headerContent" id="headerMenu">
	<div class="stepwizard col-md-offset-12 ">
    	<div class="stepwizard-row setup-panel" id="stepwizard">
   			<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle2" id="step01"><strong>STEP1</strong></button>
	      	</div>
	      	<span><img src="/common/images/common/arrow.png" alt=""/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle2" id="step02" ><strong>STEP2</strong></button>
	      	</div>
	      	<span><img src="/common/images/common/arrow.png" alt="스텝 화살표"/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle2" id="step03" ><strong>STEP3</strong></button>
	      	</div>
	      	<span><img src="/common/images/common/arrow.png" alt="스텝 화살표"/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle2" id="step04"><strong>STEP4</strong></button>
	      	</div>
	      	<span><img src="/common/images/common/arrow.png" alt="스텝 화살표"/></span>
     	 	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle2" id="step05"><strong>STEP5</strong></button>
	      	</div>
	      	<span><img src="/common/images/common/arrow.png" alt="스텝 화살표"/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle2" id="step06"><strong>STEP6</strong></button>
	      	</div>
	      	<span><img src="/common/images/common/arrow.png" alt="스텝 화살표"/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle2" id="step07"><strong>STEP7</strong></button>
	      	</div>
	      	<span><img src="/common/images/common/arrow.png" alt="스텝 화살표"/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle2" id="step08"><strong>STEP8</strong></button>
	      	</div>
			<span><img src="/common/images/common/arrow.png" alt="스텝 화살표"/></span>
	      	<div class="stepwizard-step">
	        	<button type="button" class="btn btn-default btn-outline-dark btn-circle2" id="step09"><strong>STEP9</strong></button>
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
		   				<input class="text_outline_none input_project_nm form-control" type="text" size="50" id="projectName" name="projectName" value="${pjtData.PRJCT_NM }" placeholder="프로젝트명을 입력해주세요." >
		   				<div class="input-group-text" onclick="fnPrjCreate();" style="margin-left: 5px;">
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

