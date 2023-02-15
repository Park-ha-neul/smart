<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>
<%@ taglib prefix="CacheCommboUtil" uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<jsp:useBean id="resultList" type="java.util.List" 	class="java.util.ArrayList" scope="request" />
<jsp:useBean id="parentMenuIdComboStr" type="java.util.List" class="java.util.ArrayList" scope="request" />
<jsp:useBean id="menuComboStr" type="java.util.List"	class="java.util.ArrayList" scope="request" />

<link rel="stylesheet" href="/webjars/jstree/3.3.8/themes/default/style.min.css" />
<script type="text/javascript" src="/webjars/jstree/3.3.8/jstree.min.js"></script>

<style type="text/css">
	/* webjars 로 바꾸면서 이미지를 따로 지정해준다. */
	.jstree-default .jstree-node, .jstree-default .jstree-icon {background-image: url(/common/images/jsTree/32px.png);}
</style>

<script type="text/javascript">
	//<![CDATA[

		$(function() {
			$('#jstree').jstree({
				"core" : {
					"themes" : {
						"icons" : false,
					}
				},
				"plugins" : [
					"state", "wholerow"
				]
			});

			//url 유형 코드 select 이벤트
			$('#url_ty_code').on('change', function(){
				$(this).closest('table').find('th.name').text('');
				$(this).closest('table').find('td.code').text('');
				$(this).closest('table').find('[name=url]').val('');

				if($(this).val()=="10"){
					fnSelectBbsCode("", $(this), "");
					return false;
				}
				if($(this).val()=="20"){
					fnSelectCntntsId($(this), "");
					return false;
				}
				if($(this).val()=="30"){
					$('#defaultTb .ty_info').html("");
					return false;
				}
			});

			$("#id_resize").css("height", $(window).height()-200);
			$(window).resize(function(){
				$("#id_resize").css("height", $(window).height()-200);
			});

			// 메뉴 유형 코드 select 이벤트
			$('#menu_type_code').on('change', function(){
				if (($(this).val()=="10" || $(this).val()=="20") && $('#menu_lv').val()=="4") {
					$('#new_icon_nm').attr("disabled", false);
				} else {
					$('#new_icon_nm').val("");
					$('#new_icon_nm').attr("disabled", true);
				}
			});
		});

	//]]>
</script>

<script type="text/javascript">
	// 메뉴 이동 모달 초기화
	function fnRemoveModal(){
		$("#menu_level_2 option:eq(0)").prop("selected", true);

		var html = '<option value="">선택</option>';
		$("#menu_level_3").html(html);
		$("#menu_level_3").attr("disabled", true);
	}

	// 메뉴 이동 모달 확인 버튼
	function fnMoveMenu(){
		if ($("#menu_lv").val() == 3) {
			if($("#menu_level_2").val() == ''){
				alert("메뉴를 선택해주세요.");
				return;
			}
			if ($("#menu_level_2").find(":selected").val() == $("#up_menu_id").val()) {
				alert("현재 메뉴 위치와 동일합니다.");
				return;
			}
			$("#menu_move_parent").val($("#menu_level_2").val());

		} else if ($("#menu_lv").val() == 4) {
			if($("#menu_level_2").val() == '' || $("#menu_level_3").val() == ''){
				alert("메뉴를 선택해주세요.");
				return;
			}
			if ($("#menu_level_3").find(":selected").val() == $("#up_menu_id").val()) {
				alert("현재 메뉴 위치와 동일합니다.");
				return;
			}
			$("#menu_move_parent").val($("#menu_level_3").val());
		}

		if(confirm("이동하시겠습니까?")){
				$("#aform").attr({action:"/admin/menu/updateMenuMove.do", method:'post'}).submit();
		}
	}

	// 이동 버튼 (모달 OPEN)
	function fnMoveoModal(){
		if($("#menu_pk").val() == ''){
			alert("이동할 메뉴를 선택해주세요.");
			return;
		}

		if($("#menu_lv").val() > 4 || $("#menu_lv").val() < 3){
			alert("이동 할 수 없는 메뉴입니다. (메뉴 레벨 3, 4 만 이동 가능)");
			return;
		}
		fnRemoveModal();
		$("#menuMoveoModal").modal();
	}

	// 메뉴이동 모달 메뉴 콤보 박스 생성
	function fnSetMenuCombo(level){
		if ($("#menu_lv").val() == 3 ) {
			return;
		}

		var req ={
			"level" : level,
			"menu_level_2" : $("#menu_level_2").val()
		};

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/admin/menu/selectListMenuComboAjax.do',
			data : req,
			success : function(param) {
				if(param.resultStats.resultCode == "error"){
					alert(param.resultStats.resultMsg);
					return;
				}

				var html = "";
				html += '<option value="">선택</option>';

				if(param.resultStats.resultList.length > 0){
					for(i = 0; i < param.resultStats.resultList.length; i++){
						html += '<option value="'+param.resultStats.resultList[i].MENU_ID+'">'+param.resultStats.resultList[i].MENU_NM+'</option>';
					}
				}
				$("#menu_level_3").html(html);
				$("#menu_level_3").attr("disabled", false);
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	}

	function checkNum(evt){
     var charCode = (evt.which) ? evt.which : event.keyCode
     if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

     return true;
	 }

	//메뉴정보 조회
	function fnSelectMenuInfo(menu_id, menu_nm, menu_lv){
		var req ={
			"menu_id" : menu_id
			,"menu_nm" : menu_nm
			,"menu_lv" : menu_lv
		};

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/admin/menu/selectMenuMgtAjax.do',
			data : req,
			success : function(param) {

				if(param.resultStats.resultCode == "error"){
					alert(param.resultStats.resultMsg);
					return;
				}
				$("#menu_pk").val(param.resultMap.MENU_PK);
				$("#menu_id").val(param.resultMap.MENU_ID);
				$("#up_menu_id").val(param.resultMap.UP_MENU_ID);
				$("#up_menu_nm").val(param.resultMap.UP_MENU_NM);
				$("#menu_type_code").val(param.resultMap.MENU_TY_CODE);
				$("#menu_lv").val(param.resultMap.MENU_LEVEL);
				$("#menu_nm").val(param.resultMap.MENU_NM);
				$("#fnct_nm").val(param.resultMap.FNCT_NM);
				$("#menu_nm_cn").val(param.resultMap.MENU_NM_CN);
				$("#url").val(param.resultMap.URL);
				$("#rm").val(param.resultMap.RM);
				$("#srt_ord").val(param.resultMap.SORT_ORDR);
				$("#use_yn").val(param.resultMap.USE_YN);
				$("#disp_yn").val(param.resultMap.DISP_YN);

				var menuTyCode = param.resultMap.MENU_TY_CODE;
				var menuLv = param.resultMap.MENU_LEVEL;
				$("#icon_nm").val(param.resultMap.ICON_NM);

				if ((menuTyCode=="10" || menuTyCode=="20") && menuLv=="4") {
					$('[name=icon_nm]').attr("disabled", false);
				} else {
					$('[name=icon_nm]').attr("disabled", true);
				}

				var urlTyCode = param.resultMap.MENU_SE_CODE;
				$("#url_ty_code").val(urlTyCode);

				$('#defaultTb tr.ty_info').html("");
				$('#defaultTb th.name').text("");
				$('#defaultTb td.code').text("");
				if(urlTyCode=="10"){
					var str = '<th>시스템</th>';
							str += '<td>';
							str += '  <select name="bbs_sys_code" id="bbs_sys_code" class="form-control">';
							str += param.bbsMap.bbsSysComboStrText;
							str += '	</select>';
							str += '	<input type="hidden" name="subpath" value="'+param.bbsMap.subpath+'"/>';
							str += '</td>';
							str += '<th>게시판 명</th>';
							str += '<td>';
							str += '  <select name="bbs_code" id="bbs_code" class="form-control">';
							str += param.bbsMap.bbsCodeComboStrText;
							str += '	</select>';
							str += '</td>';
					$('#defaultTb tr.ty_info').html(str);
					$('#defaultTb th.name').text('게시판 코드');
					$('#defaultTb td.code').text($('#bbs_code').val());
					fnSetBbsCode("");
				}else if(urlTyCode=="20"){
					var str = '<th>[시스템]콘텐츠 제목</th>';
							str += '<td colspan="2" class="cntnts_nm">['+param.cntntsMap.SYS_NM+'] '+param.cntntsMap.SJ+'</td>';
							str += '<td>';
							str += '  <span class="btnR"><button type="button" id="btnCntnts" onclick="fnSearchCntnts("", this);" class="btn btn-primary btn-sm">콘텐츠 검색</button></span>';
							str += '  <input type="hidden" name="cntnts_id" value="'+param.cntntsMap.CNTNTS_ID+'">';
							str += '</td>';
					$('#defaultTb tr.ty_info').html(str);
					$('#defaultTb th.name').text('컨텐츠ID');
					$('#defaultTb td.code').text(param.cntntsMap.CNTNTS_ID);
				}

				$("#new_menu_id_val").val(param.resultMap.LOW_MENU_ID);
				$("#new_srt_ord_val").val(param.resultMap.MAX_SORT_ORDR);

				if(param.resultMap.LOW_MENU_ID==null){
					$("#new_menu_id_val").val('900');
				}

				$("#sort_ordr_1").val(param.resultMap.SORT_ORDR_1);
				$("#sort_ordr_2").val(param.resultMap.SORT_ORDR_2);
				$("#sort_ordr_3").val(param.resultMap.SORT_ORDR_3);
				$("#sort_ordr_4").val(param.resultMap.SORT_ORDR_4);
				$("#sort_ordr_5").val(param.resultMap.SORT_ORDR_5);
				$("#sort_ordr_6").val(param.resultMap.SORT_ORDR_6);

				$("#insertArea").html("");
				$("#insertBtn").hide();
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	} //메뉴정보 조회 끝

	var oldValue="";
	function fnSetOldValue(obj, idx){
		oldValue = obj.value;
	}

	// 메뉴 중복 확인 버튼
	function fnSelectMenuExistYnAjax(){
		var menu_id = $("#menu_id").val();
		var newMenuIdList = f_getList("new_menu_id");
		var newUrlList = f_getList("new_url");

		var newMenuIdListValue = menu_id + newMenuIdList[0].value;
		var newUrlListValue = newUrlList[0].value;

		for (var i = 1; i < newMenuIdList.length; i++) {
			newMenuIdListValue += ("," + menu_id + newMenuIdList[i].value);
			newUrlListValue += ("," + newUrlList[i].value);
		}

		var req = {'new_menu_id' : newMenuIdListValue
							,	'new_url' : newUrlListValue};

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/admin/menu/selectExistYnMenuMgtAjax.do',
			data : req,
			success : function(param) {
				if(param.resultStats.resultCode == "error"){
					alert(param.resultStats.resultMsg);
					return;
				}

				if(param.resultStats.resultCode == "ok"){
					if(confirm("등록하시겠습니까?")){
						$("#aform").attr({action:"/admin/menu/insertMenuMgt.do", method:'post'}).submit();
					}
				}
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	}

	//입력폼 보기
	var removeNo = 0;

	function fnInsertForm(){
		if($("#menu_id").val() == ""){
			alert("메뉴를 선택해 주세요.");
			return;
		}

		var sortDepth = $("#menu_id").val().length / 4 + 1;

		if(sortDepth>=6){
			alert(" 6 Depth 이상으로는 메뉴를 생성할 수 없습니다.");
			return;
		}

		var nextSrtOrd = 0;	//next 정렬순서
		var newSrtOrdList = f_getList("new_srt_ord");
		for(i = 0; i < newSrtOrdList.length; i++){
			if(Number(nextSrtOrd) < Number(newSrtOrdList[i].value)){
				nextSrtOrd = Number(newSrtOrdList[i].value);
			}
		}

		if(newSrtOrdList.length == 0 ) {
			nextSrtOrd = Number($("#new_srt_ord_val").val()) + 10;
		}else {
			nextSrtOrd = Number(nextSrtOrd) + 10;
		}

		var next_new_menu_id = 0;	//next 하위 메뉴 아이디
		var newMenuIdList = f_getList("new_menu_id");

		for(i = 0; i < newMenuIdList.length; i++){
			if(Number(next_new_menu_id) < Number(newMenuIdList[i].value)){
				next_new_menu_id = Number(newMenuIdList[i].value);
			}
		}
		if(newMenuIdList.length == 0 ){
			next_new_menu_id = Number($("#new_menu_id_val").val()) + 100;
		}else{
			next_new_menu_id = Number(next_new_menu_id) + 100;
		}

		var menuTypeComboStr = '<select name="new_menu_type_code" id="new_menu_type_code" class="form-control">';
			menuTypeComboStr += "${CacheCommboUtil:getComboStr(UPCODE_MENU_TYPE_CODE, 'CODE', 'CODE_NM', '', 'C')}";
			menuTypeComboStr += '</select>';

		var useYnComboStr = '<select name="new_use_yn" id="new_use_yn" class="form-control" >';
			useYnComboStr += "${CacheCommboUtil:getComboStr(UPCODE_USE_YN, 'CODE', 'CODE_NM', '', '')}";
			useYnComboStr += '</select>';

		var dispYnComboStr = '<select name="new_disp_yn" id="new_disp_yn" class="form-control" >';
			dispYnComboStr += "${CacheCommboUtil:getComboStr(UPCODE_USE_YN, 'CODE', 'CODE_NM', '', '')}";
			dispYnComboStr += '</select>';

		var urlTyComboStr = '<select name="new_url_ty_code" id="new_url_ty_code" class="form-control" >';
			urlTyComboStr += "${CacheCommboUtil:getComboStr(UPCODE_MENU_SE_CODE, 'CODE', 'CODE_NM', '30', '')}";
			urlTyComboStr += '</select>';

		removeNo++;

		var layer = '';
		layer += '<p id="removeNo_'+removeNo+'_p" class="text-center" style="margin-bottom: 4px;margin-top: 16px;">';
		layer += '<strong>[ 신규등록 메뉴 ]</strong>';
		layer += '</p>';
		layer += '<table id="removeNo_'+removeNo+'" class="table">';
		layer += '	<caption>';
		layer += '		<div class="col-xs-6" style="text-align: right;">';
		layer += '			<button type="button" class="btn bg-gradient-danger float-right mr-2" onclick="fnRemove(\'removeNo_' + removeNo + '\');">삭제</button></td>';
		layer += '			<button type="button" class="btn bg-gradient-success float-right mr-2" onclick="fnCopyForm('+removeNo+');">복제</button>';
		layer += '		</div>';
		layer += '	</caption>';
		layer += '	<colgroup>';
		layer += '		<col style="width:20%;" />';
		layer += '		<col style="width:25%;" />';
		layer += '		<col style="width:20%;" />';
		layer += '		<col style="width:25%;" />';
		layer += '	</colgroup>	';

		layer += '	<tr>';
		layer += '		<th class="required_field">상위메뉴 ID</th>';
		layer += '		<td>';
		layer += '			<input type="text" class="form-control" name="" value="'+$("#menu_id").val()+'" readonly />';
		layer += '		</td>';
		layer += '		<th class="required_field">메뉴 ID</th>';
		layer += '		<td>';
		layer += '			<input type="text" class="form-control" name="new_menu_id" id="new_menu_id" onkeypress="return checkNum(event);" value="'+next_new_menu_id+'"/>';
		layer += '		</td>';
		layer += '	</tr>';
		layer += '	<tr>';
		layer += '		<th class="required_field">메뉴명</th>';
		layer += '		<td><div class="outBox1"><input type="text" class="form-control" name="new_menu_nm" id="new_menu_nm"  maxlength="100" onkeyup="cfLengthCheck(\'메뉴명은\', this, 100);"></div></td>';
		layer += '		<th>상세기능명</th>';
		layer += '		<td><div class="outBox1"><input type="text" class="form-control" name="new_fnct_nm" id="new_fnct_nm"  maxlength="100" onkeyup="cfLengthCheck(\'상세기능명은\', this, 100);"></div></td>';
		layer += '	</tr>';
		layer += '	<tr>';
		layer += '		<th class="required_field">메뉴유형</th>';
		layer += '		<td><div class="outBox1">'+menuTypeComboStr+'</div></td>';
		layer += '		<th>정렬순서</th>';
		layer += '		<td><div class="outBox1"><input type="text" class="form-control" name="new_srt_ord" id="new_srt_ord" maxlength="4" onkeypress="return checkNum(event);" value="'+nextSrtOrd+'"></div></td>';
		layer += '	</tr>';
		layer += '	<tr>';
		layer += '		<th>메뉴아이콘';
		layer += '			<br><small>(<a href="https://ionicons.com/" target="_blank">Ionicons.com</a>)</small>';
		layer += '		</th>';
		layer += '			<td colspan="3">';
		layer += '				<input type="text" class="form-control" name="new_icon_nm" disabled="disabled" id="new_icon_nm" placeholder="아이콘의 이름을 입력해주세요." onkeyup="cfLengthCheck(\'아이콘은\', this, 100);">';
		layer += '			</td>';
		layer += '	</tr>';
		layer += '	<tr>';
		layer += '		<th>사용여부</th>';
		layer += '		<td><div class="outBox1">'+useYnComboStr+'</div></td>';
		layer += '		<th>전시여부</th>';
		layer += '		<td><div class="outBox1">'+dispYnComboStr+'</div></td>';
		layer += '	</tr>';
		layer += '	<tr>';
		layer += '		<th>url 유형</th>';
		layer += '		<td><div class="outBox1">'+urlTyComboStr+'</div></td>';
		layer += '		<th class="new_name"></th>';
		layer += '		<td class="new_code"></td>';
		layer += '	</tr>';
		layer += '	<tr class="new_ty_info"></tr>';
		layer += '	<tr>';
		layer += '		<th class="bor_last">URL</th>';
		layer += '		<td colspan="3"><div class="outBox1"><input type="text" class="form-control" name="new_url" id="new_url" maxlength="100" onkeyup="cfLengthCheck(\'URL은\', this, 100);"></div></td>';
		layer += '	</tr>';
		layer += '	<tr>';
		layer += '		<th class="bor_last">비고</th>';
		layer += '		<td colspan="4" class="bor_last"><div class="outBox1"><input type="text" class="form-control" name="new_rm" id="new_rm" maxlength="500" onkeyup="cfLengthCheck(\'비고는\', this, 500);"></div></td>';
		layer += '	</tr>';
		layer += '</table>';

		$("#insertArea").append(layer);
		$("#insertBtn").show();

		fnSetNumeric();
		fnSetUrlTyCode();
		fnSetMenuTyCode();
	} //입력폼 보기 끝

	// 입력폼 복제
	function fnCopyForm(copyId){
		var newMenuIdList = f_getList("new_menu_id");
		var newMenuNmList = f_getList("new_menu_nm");
		var newMenuTypeCodeList = f_getList("new_menu_type_code");

		var i = copyId-1;

		if(newMenuIdList[i].value == ""){
			alert("신규 메뉴ID를 입력해 주세요.");
			newMenuIdList[i].focus();
			return;
		}

		if(/^[0-9]{4}$/.test(newMenuIdList[i].value)==false){
			alert("신규 메뉴ID를 4자리수를 입력해 주세요.");
			newMenuIdList[i].focus();
			return;
		}

		if(newMenuNmList[i].value == ""){
			alert("신규 메뉴명을 입력해 주세요.");
			newMenuNmList[i].focus();
			return;
		}

		if(newMenuTypeCodeList[i].value == ""){
			alert("신규 메뉴유형을 입력해 주세요.");
			newMenuTypeCodeList[i].focus();
			return;
		}

		fnInsertForm();
		$('#removeNo_'+removeNo+' #new_menu_nm').val($('#removeNo_'+copyId+' #new_menu_nm').val());
		$('#removeNo_'+removeNo+' #new_menu_type_code').val($('#removeNo_'+copyId+' #new_menu_type_code').val());
		$('#removeNo_'+removeNo+' #new_fnct_nm').val($('#removeNo_'+copyId+' #new_fnct_nm').val());
		$('#removeNo_'+removeNo+' #new_icon_nm').val($('#removeNo_'+copyId+' #new_icon_nm').val());
		$('#removeNo_'+removeNo+' #new_use_yn').val($('#removeNo_'+copyId+' #new_use_yn').val());
		$('#removeNo_'+removeNo+' #new_disp_yn').val($('#removeNo_'+copyId+' #new_disp_yn').val());

		if ($('#removeNo_'+copyId+' #new_url_ty_code').find(":selected").val() == "30") { 		//  url 유형이 직접입력일때만 url 복제
			$('#removeNo_'+removeNo+' #new_url').val($('#removeNo_'+copyId+' #new_url').val());
		}

		var newMenuTypeCode = $('#removeNo_'+removeNo+' select[name=new_menu_type_code]').val();
		if ((newMenuTypeCode=="10" || newMenuTypeCode=="20") && $('#menu_lv').val()=="3") {
			$('#removeNo_'+removeNo+' #new_icon_nm').attr("disabled", false);
		} else {
			$('#removeNo_'+removeNo+' #new_icon_nm').val("");
			$('#removeNo_'+removeNo+' #new_icon_nm').attr("disabled", true);
		}
	}

	//신규등록 url 유형 코드 이벤트
	function fnSetUrlTyCode(){
		$('[name=new_url_ty_code]').on('change', function(){
			$(this).closest('table').find('th.new_name').text('');
			$(this).closest('table').find('td.new_code').text('');
			$(this).closest('table').find('[name=new_url]').val('');

			if($(this).val()=="10"){
				fnSelectBbsCode("", $(this), "new_");
				return false;
			}
			if($(this).val()=="20"){
				fnSelectCntntsId($(this), "new_");
				return false;
			}
			if($(this).val()=="30"){
				$(this).closest('tr').next('tr').html("");
				return false;
			}
		});
	}

	//신규등록 메뉴유형코드 이벤트
	function fnSetMenuTyCode(){
		$('#removeNo_'+removeNo+' #new_menu_type_code').on('change', function(){
			var newMenuTypeCode = $('#removeNo_'+removeNo+' select[name=new_menu_type_code]').val();
			if ((newMenuTypeCode=="10" || newMenuTypeCode=="20") && $('#menu_lv').val()=="3") {
				$('#removeNo_'+removeNo+' #new_icon_nm').attr("disabled", false);
			} else {
				$('#removeNo_'+removeNo+' #new_icon_nm').attr("disabled", true);
			}
		});
	}

	//입력폼 삭제
	function fnRemove(removeId){
		$("#"+removeId).remove();
		$("#"+removeId+"_p").remove();

		if($("#insertArea").html().length == 0){
			$("#insertBtn").hide();
		}
	}

	//메뉴정보 수정
	function fnUpdate(){
		if($("#menu_id").val() == ""){
			alert("메뉴를 선택해 주세요.");
			return;
		}else{
			if($("#menu_nm").val() == ""){
				alert("메뉴명을 입력해 주세요.");
				$("#menu_nm").focus();
				return;
			}

			if($("#menu_type_code").val() == ""){
				alert("메뉴유형을 입력해 주세요.");
				$("#menu_nm").focus();
				return;
			}

			if($('#url_ty_code').val()=="10" && ($('[name=bbs_sys_code]').val()=="" || $('[name=bbs_code]').val()=="") ){
				alert("게시판을 선택해 주세요.");
				$("#bbs_sys_code").focus();
				return;
			}else if($('#url_ty_code').val()=="20" && $('[name=cntnts_id]').val()==""){
				alert("컨텐츠를 선택해 주세요.");
				$("#btnCntnts").focus();
				return;
			}
			fnUpdateCheck();
		}
	} //메뉴정보 수정 끝

	//수정한 정보 체크
	function fnUpdateCheck(){
		var req ={
				"up_menu_id" : $("#up_menu_id").val()
				,"menu_id" : $("#menu_id").val()
				,"srt_ord" : $("#srt_ord").val()
		};

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/admin/menu/selectExistSortMenuMgtAjax.do',
			data : req,
			success : function(param) {
				if(param.resultStats.resultCode == "error"){
					alert(param.resultStats.resultMsg);
					return;
				}
				if(param.resultMap.EXIST_SORT_YN == "Y"){
					alert("중복된 정렬 순서가 존재합니다.\n다른 정렬 순서번호를 이용하시기바랍니다.");
				}
				else{
					if(confirm("수정하시겠습니까?")){
						$("#aform").attr({action:"/admin/menu/updateMenuMgt.do", method:'post'}).submit();
					}
				}
			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	} //수정한 정보 체크

	//메뉴 신규 등록
	function fnInsert(){
		var newMenuIdList = f_getList("new_menu_id");
		var newMenuNmList = f_getList("new_menu_nm");
		var newMenuTypeCodeList = f_getList("new_menu_type_code");
		var newUseYnList = f_getList("new_use_yn");
		var newDispYnList = f_getList("new_disp_yn");
		var newStrOrdList = f_getList("new_srt_ord");
		var newUrlList = f_getList("new_url");

		var menuIdDupYn = false;
		var	urlDupYn = false;

		if(newMenuIdList.length == 0){
			alert("신규메뉴정보를 입력해 주세요.");
			return;
		}	else{
			for(i = 0 ; i < newMenuIdList.length; i++){
				if(newMenuIdList[i].value == ""){
					alert("신규 메뉴ID를 입력해 주세요.");
					newMenuIdList[i].focus();
					return;
				}

				if(/^[0-9]{4}$/.test(newMenuIdList[i].value)==false){
					alert("신규 메뉴ID를 4자리수를 입력해 주세요.");
					newMenuIdList[i].focus();
					return;
				}

				if(newMenuNmList[i].value == ""){
					alert("신규 메뉴명을 입력해 주세요.");
					newMenuNmList[i].focus();
					return;
				}

				if(newMenuTypeCodeList[i].value == ""){
					alert("신규 메뉴유형을 입력해 주세요.");
					newMenuTypeCodeList[i].focus();
					return;
				}
			}

			var dupCnt = 0;
			// 메뉴 아이디 중복 검사
			for(i = 0; i < newMenuIdList.length; i++){
				dupCnt = 0;
				for(k = 0; k < newMenuIdList.length; k++){
					if(newMenuIdList[i].value == newMenuIdList[k].value){
						if(dupCnt > 1){
							menuIdDupYn = true;
							break;
						}
						dupCnt++;
					}
				}
			}

			if(menuIdDupYn){
				alert("메뉴ID가 중복입력 되었습니다.");
				return;
			}

			dupCnt = 0;
			// 메뉴 URL 중복 검사
			for(i = 0; i < newUrlList.length; i++){
				dupCnt = 0;
				for(k = 0; k < newUrlList.length; k++){
					if(newUrlList[i].value == newUrlList[k].value){
						if(newMenuIdList[i].value != null && newMenuIdList[k].value != null){
							if(dupCnt > 1){
								urlDupYn = true;
								break;
							}
						}
					dupCnt++;
					}
				}
			}

			if(urlDupYn){
				alert("메뉴url이 중복입력 되었습니다.");
				return;
			}

			if($('#new_url_ty_code').val()=="10" && ($('[name=new_bbs_sys_code]').val()=="" || $('[name=new_bbs_code]').val()=="") ){
				alert("게시판을 선택해 주세요.");
				$("#new_bbs_code").focus();
				return;
			}else if($('#new_url_ty_code').val()=="20" && $('[name=new_cntnts_id]').val()==""){
				alert("컨텐츠를 선택해 주세요.");
				$("#new_btnCntnts").focus();
				return;
			}

			// 증복 확인하는 ajax
			fnSelectMenuExistYnAjax();
		}
	} //매뉴 신규 등록 끝

	//메뉴 삭제
	function fnDelete(){
		if($("#menu_id").val() == ""){
			alert("메뉴를 선택해 주세요.");
			return;
		}

		if($("#up_menu_id").val() == ""){
			alert("Root는 삭제할 수 없습니다.");
			return;
		}

		if(confirm("\nWARNNING!!!!\n\n하위 메뉴 포함하여 삭제됩니다. \n\n삭제하시겠습니까?\n")){
			$("#aform").attr({action:"/admin/menu/deleteMenuMgt.do", method:'post'}).submit();
		}
	}

	//게시판 코드 리스트 조회
	function fnSelectBbsCode(sysCode, urlTyCodeObj, prefix){
			var req = {
				sys_code : sysCode
			};

			jQuery.ajax( {
				type : 'POST',
				dataType : 'json',
				url : '/admin/menu/selectListBbsCodeAjax.do',
				data : req,
				success : function(param) {
					if(param.resultStats.resultCode == "error"){
						alert(param.resultStats.resultMsg);
						return;
					}
					var str = '<th>시스템</th>';
							str += '<td>';
							str += '  <select name="'+prefix+'bbs_sys_code" class="form-control">';
							str += param.sysComboStrText;
							str += '	</select>';
							str += '	<input type="hidden" name="subpath" />';
							str += '</td>';
							str += '<th>게시판 명</th>';
							str += '<td>';
							str += '  <select name="'+prefix+'bbs_code" disabled="disabled" class="form-control">';
							str += '		<option>게시판</option>';
							str += '	</select>';
							str += '</td>';

					if (urlTyCodeObj.attr("id") == prefix+"url_ty_code") {
						$(urlTyCodeObj).closest('tr').next().html(str);
						fnSetBbsCode(prefix);
					}

					if(param.bbsListLength == 0){
						$(urlTyCodeObj).closest('table').find("[name="+prefix+"bbs_code]").html('<option value="">게시판</option>') ;
						$(urlTyCodeObj).closest('table').find("[name="+prefix+"bbs_code]").prop('disabled', true);
						// 게시판 코드(th, td), URL 초기화
						$(urlTyCodeObj).closest('table').find('th.'+prefix+'name').text('게시판 코드');
						$(urlTyCodeObj).closest('table').find('td.'+prefix+'code').text("");
						$(urlTyCodeObj).closest('table').find('[name='+prefix+'url]').val("");

					}else{
						$(urlTyCodeObj).closest('table').find("[name="+prefix+"bbs_code]").html(param.bbsCodeComboStrText);
						$(urlTyCodeObj).closest('table').find("[name="+prefix+"bbs_code]").prop('disabled', false);
						// 게시판 코드
						$(urlTyCodeObj).closest('table').find('th.'+prefix+'name').text('게시판 코드');
						var bbsCode = $("[name="+prefix+"bbs_code]").val();
						$(urlTyCodeObj).closest('table').find('td.'+prefix+'code').text(bbsCode);

						var subpath="";
						// subpath가 존재하는경우
						if(param.subpath != ''){
							$(urlTyCodeObj).closest('table').find("[name=subpath]").val("/"+param.subpath);
							subpath = $(urlTyCodeObj).closest('table').find('[name=subpath]').val();
						}
						// selectPageListBbsUrl은 globals에 정의되어 있음 (url.pagaListBbs = /common/bbs/selectPageListBbs.do?bbs_code=)
						$(urlTyCodeObj).closest('table').find('[name='+prefix+'url]').val(subpath + '<%=param.getString("selectPageListBbsUrl")%>' + bbsCode);
						return;
					}
				},
				error : function(jqXHR, textStatus, thrownError){
					ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
				}
			});
		}

	//게시판 코드 select 이벤트
	function fnSetBbsCode(prefix){
		$('[name='+prefix+'bbs_sys_code]').on('change', function(){
			fnSelectBbsCode($(this).val(), $(this), prefix);
		});

		$('[name='+prefix+'bbs_code]').on('change', function(){
			$(this).closest('table').find('th.'+prefix+'name').text('게시판 코드');
			$(this).closest('table').find('td.'+prefix+'code').text($(this).val());

			if($(this).closest('table').find('[name='+prefix+'menu_nm]').val().indexOf("상세")==-1){
				var subpath = $(this).closest('table').find('[name=subpath]').val();
				$(this).closest('table').find('[name='+prefix+'url]').val(subpath + '<%=param.getString("selectPageListBbsUrl")%>' + $(this).val());
			}
		});
	}

	//콘텐츠 조회
	function fnSelectCntntsId(urlTyCodeObj, prefix){
		var str = '<th>[시스템]콘텐츠 제목</th>';
				str += '<td colspan="2" class="cntnts_nm"></td>';
				str += '<td>';
				str += "  <span class='btnR'><button type=\"button\" id='"+prefix+"btnCntnts' onclick=\"fnSearchCntnts('"+prefix+"', this)\" class=\"btn btn-primary btn-sm\">콘텐츠 검색</button></span>";
				str += '  <input type="hidden" name="'+prefix+'cntnts_id">';
				str += '</td>';
		$(urlTyCodeObj).closest('tr').next('tr').html(str);
	}

	//콘텐츠 조회 window popup
	function fnSearchCntnts(prefix, obj){
		var popupX = (window.screen.width / 2) - (800 / 2); 	// 만들 팝업창 좌우 크기의 1/2 만큼 보정값으로 빼주었음
		var popupY= (window.screen.height / 2) - (1000 / 2); 	// 만들 팝업창 상하 크기의 1/2 만큼 보정값으로 빼주었음

		var tbId = $(obj).closest('table').attr('id');
		window.open('/admin/menu/selectPageListCntnts.do?prefix='+prefix+'&tbId='+tbId, 'popup', 'width=1000, height=800, left='+ popupX + ', top='+ popupY + ', screenX='+ popupX + ', screenY= '+ popupY);
		document.aform.target='popup';
	}

	function fnChageParentMenuId(obj){
		document.location.href="/admin/menu/selectListMenuMgt.do?sch_parent_menu_id=" + obj.value;
	}
</script>

<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form id="aform" method="post" action="/admin/menu/selectListMenuMgt.do">
					<input type="hidden" id="menu_pk" name="menu_pk"/>
					<input type="hidden" id="menu_move_parent" name="menu_move_parent"/>
					<div class="row">
						<div class="col-lg-5">
							<div class="card card-info card-outline">
								<div class="card-header">
									<div class="form-row">
										<div class="justify-content-start">
											<div class="form-inline">
												<select name="sch_parent_menu_id" id="sch_parent_menu_id" class="form-control ml-2" onchange="fnChageParentMenuId(this);">
													${CommboUtil:getComboStr(parentMenuIdComboStr, 'MENU_ID', 'MENU_NM',  requestScope.param.sch_parent_menu_id,'')}
												</select>
												<button type="button" class="btn bg-gradient-success ml-2" onclick="$('#jstree').jstree('close_all');"><i class="fas fa-minus"></i>&nbsp;&nbsp;접기</button>
												<button type="button" class="btn bg-gradient-secondary ml-2" style="margin-right: 5px;" onclick="$('#jstree').jstree('open_all');"><i class="fas fa-plus"></i> 펼치기</button>
											</div>
										</div>
									</div>
								</div>
								<div class="card-body">
									<!-- 좌측 메뉴 트리 -->
									<div class="left_list" id="jstree">
										<ul>
											<li>
												<c:set var="tagSumCnt" value="0"/>
												<c:forEach var="item" items="${resultList}" varStatus="status">
													<c:if test="${status.count-1 == 0}">
														<a class="pointer" onclick="fnSelectMenuInfo('${item.MENU_ID}','${item.MENU_NM}','${item.MENU_LEVEL}'); return false;">${item.MENU_NM}</a>
													</c:if>
													<c:if test="${status.count-1 != 0}">
														<%-- 하위레벨일경우 --%>
														<c:if test="${minus < item.MENU_LEVEL}">
															<ul><li>
															<c:set var="tagSumCnt" value="${tagSumCnt+1}"/>
														</c:if>
														<%-- 동레벨일경우 --%>
														<c:if test="${minus == item.MENU_LEVEL}">
															</li><li>
														</c:if>
														<%-- 상위레벨일경우 --%>
														<c:if test="${minus > item.MENU_LEVEL}">
															<c:set var="lvDept" value="${minus - item.MENU_LEVEL}"/>
															<c:forEach var="k" begin="1" end="${lvDept}">
																</li></ul>
															 	<c:set var="tagSumCnt" value="${tagSumCnt-1}"/>
															</c:forEach>
															<li>
														</c:if>
														<a class="left_manu" href="#" onclick="fnSelectMenuInfo('${item.MENU_ID}','${item.MENU_NM}','${item.MENU_LEVEL}'); return false;">${item.MENU_NM}</a>
													</c:if>
													<c:set var="minus" value="${item.MENU_LEVEL}"/>
												</c:forEach>
												<c:forEach var="i" begin="1" end="${tagSumCnt}">
													</li></ul>
												</c:forEach>
											</li>
										</ul>
									</div>
									<!-- 좌측 메뉴 트리 끝 -->
								</div>
							</div>
						</div>
						<div class="col-lg-7">
							<%-- 우즉 메뉴 테이블 --%>
							<div class="card card-info card-outline">
								<div class="card-body">
									<div class="table-responsive">
										<input type="hidden" class="btn btn-info" name="sort_ordr_1" id="sort_ordr_1" />
										<input type="hidden" class="btn btn-info"	name="sort_ordr_2" id="sort_ordr_2" />
										<input type="hidden" class="btn btn-info" name="sort_ordr_3" id="sort_ordr_3" />
										<input type="hidden" class="btn btn-info" name="sort_ordr_4" id="sort_ordr_4" />
										<input type="hidden" class="btn btn-info" name="sort_ordr_5" id="sort_ordr_5" />
										<input type="hidden" class="btn btn-info" name="sort_ordr_6" id="sort_ordr_6" />
										<input type="hidden" class="form-control" name="new_menu_id_val" id="new_menu_id_val" />
										<input type="hidden" class="form-control" name="new_srt_ord_val" id="new_srt_ord_val" />
										<table id="defaultTb" class="table text-nowrap">
											<colgroup>
												<col style="width: 20%;" />
												<col style="width: 30%;" />
												<col style="width: 20%;" />
												<col style="width: 30%;" />
											</colgroup>
											<tr>
												<th>상위메뉴 ID</th>
												<td>
													<div class="outBox1">
														<input type="text" class="form-control" name="up_menu_id" id="up_menu_id" readonly>
													</div>
												</td>
												<th>상위메뉴명</th>
												<td>
													<div class="outBox1">
														<input type="text" class="form-control" name="up_menu_nm" id="up_menu_nm" readonly>
													</div>
												</td>
											</tr>
											<tr>
												<th>메뉴 ID</th>
												<td>
													<div class="outBox1">
														<input type="text" class="form-control" name="menu_id" id="menu_id" readonly>
													</div>
												</td>
												<th>메뉴레벨</th>
												<td>
													<div class="outBox1">
														<input type="text" class="form-control" name="menu_lv" id="menu_lv" readonly>
													</div>
												</td>
											</tr>
											<tr>
												<th class="required_field">메뉴명</th>
												<td>
													<div class="outBox1">
														<input type="text" class="form-control" name="menu_nm" id="menu_nm" onkeyup="cfLengthCheck('메뉴명은', this, 100);">
													</div>
												</td>
												<th>상세기능명</th>
												<td>
													<div class="outBox1">
														<input type="text" class="form-control" name="fnct_nm" id="fnct_nm" onkeyup="cfLengthCheck('상세기능명은', this, 100);">
													</div>
												</td>
											</tr>
											<tr>
												<th class="required_field">메뉴유형 </th>
												<td>
													<div class="outBox1">
														<select name="menu_type_code" id="menu_type_code" class="form-control">
															${CacheCommboUtil:getComboStr(UPCODE_MENU_TYPE_CODE, 'CODE', 'CODE_NM', '', 'C')}
														</select>
													</div>
												</td>
												<th>정렬순서</th>
												<td>
													<div class="outBox1">
														<input type="text" class="form-control" name="srt_ord" id="srt_ord" onkeypress="return checkNum(event);" maxlength="4">
													</div></td>
											</tr>
											<tr>
												<th>메뉴아이콘
													<br><small>(<a href="https://ionicons.com/" target="_blank">Ionicons.com</a>)</small>
												</th>
												<td colspan="3">
													<input type="text" class="form-control" name="icon_nm" id="icon_nm" disabled="disabled" placeholder="아이콘의 이름을 입력해주세요." onkeyup="cfLengthCheck('아이콘은', this, 100);">
												</td>
											</tr>
											<tr>
												<th>사용여부</th>
												<td>
													<select name="use_yn" id="use_yn" class="form-control">
														${CacheCommboUtil:getComboStr(UPCODE_USE_YN, 'CODE', 'CODE_NM', '', '')}
													</select>
												</td>
												<th>전시여부</th>
												<td>
													<select name="disp_yn" id="disp_yn" class="form-control">
														${CacheCommboUtil:getComboStr(UPCODE_USE_YN, 'CODE', 'CODE_NM', '', '')}
													</select>
												</td>
											</tr>
											<tr>
												<th>url 유형</th>
												<td>
													<select name="url_ty_code" id="url_ty_code" class="form-control">
														${CacheCommboUtil:getComboStr(UPCODE_MENU_SE_CODE, 'CODE', 'CODE_NM', '30', '')}
													</select>
												</td>
												<th class="name"></th>
												<td class="code"></td>
											</tr>
											<tr class="ty_info"></tr>
											<tr>
												<th>URL</th>
												<td colspan="3"><input type="text" class="form-control" name="url" id="url" onkeyup="cfLengthCheck('URL은', this, 100);"></td>
											</tr>
											<tr>
												<th class="bor_last">비고</th>
												<td class="bor_last" colspan="3"><input type="text" class="form-control" name="rm" id="rm" onkeyup="cfLengthCheck('비고는', this, 500);"></td>
											</tr>
										</table>
									</div>
									<!-- 하위 메뉴 생성 -->
									<span id="insertArea"></span>
									<div class="show_btn mT10" id="insertBtn" style="display: none;">
										<button type="button" class="btn bg-gradient-success" onclick="fnInsert(); return false;">등록</button>
									</div>
								</div>
								<div class="card-footer form-row justify-content-end">
									<button type="button" class="btn bg-gradient-success ml-2" onclick="fnInsertForm(); return false;">신규등록</button>
									<button type="button" class="btn bg-gradient-success ml-2" onclick="fnMoveoModal(); return false;">메뉴이동</button>
									<button type="button" class="btn bg-gradient-success ml-2" onclick="fnUpdate(); return false;">수정</button>
									<button type="button" class="btn bg-gradient-danger ml-2" onclick="fnDelete(); return false;">삭제</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!-- 메뉴 이동 창 -->
	<div class="modal fade" id="menuMoveoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document" style="width:30%">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">메뉴 이동</h4>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				</div>
				<div class="modal-body">
					<form id="modalInfo" action="" method="post">
						<table id="modalTable" class="table table-bordered">
							<tbody>
								<tr>
									<th>메뉴</th>
								</tr>
								<tr>
									<td>
										<div class="outBox1">
											<select name="menu_level_2" id="menu_level_2" class='form-control' onchange=fnSetMenuCombo(3)>
												${CommboUtil:getComboStr(menuComboStr, 'MENU_ID', 'MENU_NM', '', 'C')}
											</select>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="outBox1">
											<select name="menu_level_3" id="menu_level_3" class='form-control' disabled="disabled">
												<option value="">선택</option>
											</select>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
				<div class="modal-footer">
					<%-- 이동 메뉴는 선택한 메뉴의 위로 이동됩니다. --%>
					<button type="button" class="btn bg-gradient-secondary" data-dismiss="modal"><i class="fa fa-reply"></i> 취소</button>
					<button type="button" class="btn bg-gradient-success" onclick="fnMoveMenu(); return false;"><i class="fa fa-pencil"></i> 확인</button>
				</div>
			</div>
		</div>
	</div>
</div>
