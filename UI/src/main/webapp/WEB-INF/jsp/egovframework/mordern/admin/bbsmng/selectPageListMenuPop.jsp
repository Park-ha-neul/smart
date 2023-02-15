<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="egovframework.framework.common.object.DataMap" %>
<%@ page import="egovframework.framework.common.util.CommboUtil"%>
<jsp:useBean id="param" class="egovframework.framework.common.object.DataMap" scope="request"/>
<jsp:useBean id="pageNavigationVo" class="egovframework.framework.common.page.vo.pageNavigationVo" scope="request"/>
<jsp:useBean id="resultList"  type="java.util.List" class="java.util.ArrayList" scope="request"/>
<%-- 해당 페이지는 사용하지 않아 기존소스에서 변경하지 않음 추후 사용할시에는 맞게끔 변경필요 --%>
<%-- <%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %> --%>
<link rel="stylesheet" href="/webjars/jstree/3.3.8/themes/default/style.min.css" />
<script type="text/javascript" src="/webjars/jstree/3.3.8/jstree.min.js"></script>

<script type="text/javascript">
	$(function(){
		// 지점검색 이벤트
		$('[name=sch_text]').on({
			'keyup' : function(e){
				if(e.which == 13){
					fnSearch();
				}
			},
			'keydown' : function(e){
				if(e.which == 13){
					e.preventDefault();
				}
			},
		});

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

	});

	//메뉴 선택시 부모창에 메뉴 id셋팅
	function fnSelectMenu(menu_id, menu_nm, url_ty_code, code){

		var req = {
				url_ty_code : url_ty_code ,
				code : code
		};

		jQuery.ajax( {
			type : 'POST',
			dataType : 'json',
			url : '/admin/bbsmng/selectMenuCodeInfo.do',
			data : req,
			success : function(param) {

				if(param.resultStats.resultCode == "error"){
					alert(param.resultStats.resultMsg);
					return;
				}

				var msg = "";

				if(param.resultMap == "" || param.resultMap == undefined){
					msg = "등록하시겠습니까?";
				}else{
					if(url_ty_code == 10){
						msg = "해당메뉴를 사용중인 게시판이 있습니다.\n"
						msg += "기관 : " + param.resultMap.CODE_NM + "\n";
						msg += "게시판 코드 : " + param.resultMap.BBS_CODE + "\n";
						msg += "게시판 명 : " + param.resultMap.BBS_NM + "\n";
					}else if(url_ty_code == 20){
						msg = "해당메뉴를 사용중인 컨텐츠가 있습니다. \n"
						msg += "기관 : " + param.resultMap.CODE_NM + "\n";
						msg += "컨텐츠 ID : " + param.resultMap.CNTNTS_ID + "\n";
						msg += "컨텐츠 제목 : " + param.resultMap.SJ + "\n";
					}
					msg += "수정하시겠습니까?";
				}

				if(confirm(msg)){
					$(opener.document).find('[name=menu_id]').val(menu_id);
					$(opener.document).find('[name=menu_nm]').val(menu_nm);
					window.close();
				}


			},
			error : function(jqXHR, textStatus, thrownError){
				ajaxJsonErrorAlert(jqXHR, textStatus, thrownError)
			}
		});
	}
</script>
<div class="wrapper">
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>메뉴 선택</h1>
		</section>

		<!-- Main content -->
		<section class="content">
			<div class="row">
				<div class="col-lg-5">
					<div class="box box-primary">
						<br/>
						<!-- 접기, 펼치기 -->
						<div>
							<div class="form-inline"
								style="text-align: right; padding-right: 15px;">
								<button class="btn btn-default"
									onclick="$('#jstree').jstree('close_all');">접기</button>
								<button class="btn btn-default"
									onclick="$('#jstree').jstree('open_all');">펼치기</button>
							</div>
						</div>
						<!-- 좌측 메뉴 트리 -->
						<div class="left_list" id="jstree">
							<ul>
								<li>
									<%
										int dataCnt = resultList.size();
										int tagSumCnt = 0;
										int lvDept = 0;

										DataMap resutMap = new DataMap();
										DataMap oldResutMap = new DataMap();

										for (int i = 0; i < dataCnt; i++) {

											resutMap = (DataMap) resultList.get(i);

											if (i == 0) {
												out.println(
														"<a class=\"pointer\" onclick=\"fnSelectMenu('"
														+ resutMap.getString("MENU_ID")
														+ "', '"
														+ resutMap.getString("MENU_NM")
														+ "', '"
														+ resutMap.getString("URL_TY_CODE")
														+ "', '"
														+ resutMap.getString("CODE")
														+ "');return false;\">"
														+ resutMap.getString("MENU_NM") + "</a>");
											} else {

												oldResutMap = (DataMap) resultList.get(i - 1);

												if (oldResutMap.getInt("MENU_LEVEL") < resutMap.getInt("MENU_LEVEL")) {
													//하위레벨일경우
													out.print("<ul><li>");
													tagSumCnt++;
												}

												if (oldResutMap.getInt("MENU_LEVEL") == resutMap.getInt("MENU_LEVEL")) {
													//동레벨일경우
													out.print("</li><li>");
												}

												if (oldResutMap.getInt("MENU_LEVEL") > resutMap.getInt("MENU_LEVEL")) {
													//상위레벨일경우
													lvDept = oldResutMap.getInt("MENU_LEVEL") - resutMap.getInt("MENU_LEVEL");

													for (int k = 0; k < lvDept; k++) {

														out.println("</li></ul>");
														tagSumCnt--;
													}

													out.println("<li>");
												}

												out.println(
														"<a href=\"#\" onclick=\"fnSelectMenu('"
														+ resutMap.getString("MENU_ID")
														+ "', '"
														+ resutMap.getString("MENU_NM")
														+ "', '"
														+ resutMap.getString("URL_TY_CODE")
														+ "', '"
														+ resutMap.getString("CODE")
														+ "');return false;\">"
														+ resutMap.getString("MENU_NM") + "</a>");
											}
										}

										for (int i = 0; i < tagSumCnt; i++) {
											out.println("</li></ul>");
											tagSumCnt--;
										}
									%>
								</li>
							</ul>
						</div>	<!-- 좌측 메뉴 트리 끝 -->
					</div>
				</div>
			</div>
		</section>
	</div>
</div><!-- ./wrapper -->

