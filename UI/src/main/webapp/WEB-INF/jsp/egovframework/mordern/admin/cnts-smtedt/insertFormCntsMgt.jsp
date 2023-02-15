<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<%-- 해당 페이지는 사용하지 않아 기존소스에서 변경하지 않음 추후 사용할시에는 맞게끔 변경필요 --%>
<script type="text/javascript" src="/common/smarteditor/js/HuskyEZCreator.js" charset="UTF-8" ></script>
<script src="https://cdn.ckeditor.com/ckeditor5/10.1.0/classic/ckeditor.js"></script>
<script type="text/javascript">
//<![CDATA[

//smartEditor set//

    //전역변수



	function f_GoList(){
		document.location.href="/admin/author/selectPageListAuthorMgt.do";
	}

	function f_GoInsert(){

    obj.getById["cn"].exec("UPDATE_CONTENTS_FIELD", []);

		if($("#sj").val() == ""){
			alert("제목을 입력해 주세요.");
			$("#sj").focus();
			return;
		}
		if(confirm("등록하시겠습니까?")){
			$("#aform").attr({action:"/admin/board/insertBoardMgt.do", method:'post'}).submit();
		}
	}

//]]>
</script>
<div class="wrapper">
	<!-- content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>게시판관리</h1>
		</section>

		<!-- Main content -->
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box box-primary">
						<!-- form start -->
						<form role="form" id="aform" method="post" action="/admin/board/insertBoardMgt.do">
							<input type="hidden" id="menu_id" name="menu_id" value="${menu_id}">
							<input type="hidden" id="img_doc_id" name="img_doc_id" value="${menu_id}">
							<div class="box-body">
								<div class="form-group">
									<label for="menu_id">메뉴 아이디</label>
									<input type="text" class="form-control" value="${menu_id}" disabled>
								</div>
								<div class="form-group">
									<label for="sj">제목</label>
									<input type="text" class="form-control" id="sj" name="sj" placeholder="제목" onkeyup="cfLengthCheck('제목은', this, 200);">
								</div>
								<div class="form-group">
									<label for="cn">내용</label>
									<textarea class="form-control" rows="3" id="cn" name="cn" placeholder="내용..."></textarea>
								</div>
								<div class="form-group">
									<label for="text_cn">텍스트 내용</label>
									<textarea class="form-control" rows="3" id="text_cn" name="text_cn" placeholder="텍스트 내용..."></textarea>
								</div>
								<div class="form-group">
									<label for="kwrd">키워드</label>
									<textarea class="form-control" rows="3" name="kwrd" placeholder="키워드" onkeyup="cfLengthCheck('키워드는', this, 4000);"></textarea>
								</div>
							</div><!-- /.box-body -->
								<div id="imageBox"></div>
							<div class="box-footer">
								<div class="pull-right">
									<button type="button" class="btn bg-gradient-secondary" onclick="f_GoList(); return false;"><i class="fa fa-reply"></i> 목록</button>
									<button type="button" class="btn bg-gradient-success" onclick="f_GoInsert(); return false;"><i class="fa fa-pencil"></i> 등록</button>
								</div>
							</div>
						</form>
					</div><!-- /.box -->
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

