<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ page import="egovframework.framework.common.object.DataMap" %>
<%@ page import="egovframework.framework.common.util.CommboUtil"%>
<%@ page import="egovframework.framework.common.util.file.vo.NtsysFileVO"%>
<jsp:useBean id="resultMap" class="egovframework.framework.common.object.DataMap" scope="request"/>
<jsp:useBean id="param" class="egovframework.framework.common.object.DataMap" scope="request"/>
<jsp:useBean id="popupImgFileList"  type="java.util.List" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="siteSeComboStr" type="java.util.List" class="java.util.ArrayList" scope="request"/>
<%-- <%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %> --%>
<%-- 해당 페이지는 사용하지 않아 기존소스에서 변경하지 않음 추후 사용할시에는 맞게끔 변경필요 --%>
<script type="text/javascript" src="/common/smarteditor/js/HuskyEZCreator.js" charset="UTF-8" ></script>
<script type="text/javascript">
//<![CDATA[

	$(function(){

		// datepick 한글 셋팅 common.js
		settingdatepickerko();

		$('[name=dspy_begin_de]').datepicker({
			 dateFormat: 'yy.mm.dd',
			 changeYear: true,
			 changeMonth : true
		});

		$('[name=dspy_end_de]').datepicker({
			dateFormat: 'yy.mm.dd',
			changeYear: true,
			changeMonth : true
		});

		$(".ui-datepicker-month").css({'color':'#222 !important'});
	});


	function fnDetail(){
		$("#aform").attr({action:"/admin/board/selectBoardMgt.do", method:'post'}).submit();
	}

	function fnUpdate(){

	  obj.getById["cn"].exec("UPDATE_CONTENTS_FIELD", []);

		if($("[name=sj]").val() == ""){
			alert("제목을 입력해 주세요.");
			$("[name=sj]").focus();
			return;
		}

		if(confirm("수정하시겠습니까?")){
			$("#aform").attr({action:"/admin/board/updateBoardMgt.do", method:'post'}).submit();
		}
	}

//]]>
</script>
<div class="wrapper">
	<!-- content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>팝업관리</h1>
		</section>

		<!-- Main content -->
		<section class="content">
			<div class="row">
				<div class="col-xs-12">

					<form role="form" id="aform" method="post" action="/admin/board/updateBoardMgt.do" enctype="multipart/form-data">
						<input type="hidden" name="cntnts_seq"		value="<%=param.getString("cntnts_seq")%>"/>
						<input type="hidden" name="image_doc_id"		value="<%=resultMap.getString("IMAGE_DOC_ID")%>"/>
						<input type="hidden" name="currentPage"		value="<%=param.getString("currentPage")%>"/>

						<div class="box">
							<div class="box-body">
								<div class="form-group">
									<label for="sj">제목</label>
									<input type="text" class="form-control" id="sj" name="sj" value="<%=resultMap.getString("SJ") %>" placeholder="제목" onkeyup="cfLengthCheck('제목은', this, 200);">
								</div>
								<div class="form-group">
									<label for="cn">내용</label>
									<textarea class="form-control" rows="3" id="cn" name="cn" placeholder="내용..." onkeyup="cfLengthCheck('내용은', this, 100);"><%=resultMap.getString("CN") %></textarea>
								</div>
								<div class="form-group">
									<label for="text_cn">텍스트 내용</label>
									<textarea class="form-control" rows="3" name="text_cn" placeholder="텍스트 내용..." onkeyup="cfLengthCheck('내용은', this, 100);"><%=resultMap.getString("TEXT_CN") %></textarea>
								</div>
								<div class="form-group">
									<label for="kwrd">키워드</label>
									<textarea class="form-control" rows="3" name="kwrd" placeholder="키워드" onkeyup="cfLengthCheck('내용은', this, 100);"><%=resultMap.getString("KWRD") %></textarea>
								</div>
							</div><!-- /.box-body -->
							<div id="imageBox"></div>
							<div class="box-footer">
								<div class="pull-right">
									<button type="button" class="btn bg-gradient-secondary" onclick="fnDetail(); return false;"><i class="fa fa-reply"></i> 취소</button>
									<button type="button" class="btn bg-gradient-success" onclick="fnUpdate(); return false;"><i class="fa fa-pencil"></i> 확인</button>
								</div>
							</div>
						</div>
					</form>
				</div><!--  /.col-xs-12 -->
			</div><!-- ./row -->
		</section><!-- /.content -->
	</div>
		<script>
    var obj = [];
    //스마트에디터 프레임생성
    nhn.husky.EZCreator.createInIFrame({
        oAppRef: obj,
        elPlaceHolder: "cn",
        sSkinURI: "/smarteditor/SmartEditor2Skin.html",
        htParams : {
            // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
            bUseToolbar : true,
            // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
            bUseVerticalResizer : true,
            // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
            bUseModeChanger : true,
        }
    });
    </script>
</div>
