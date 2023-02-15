<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.framework.common.util.CommboUtil"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CommboUtil" uri="/WEB-INF/tlds/CommboUtil.tld"%>

<%@ page import="egovframework.framework.common.object.DataMap"%>
<jsp:useBean id="resultList" type="java.util.List" 	class="java.util.ArrayList" scope="request" />
<jsp:useBean id="parentMenuIdComboStr" type="java.util.List" class="java.util.ArrayList" scope="request" />

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<link rel="stylesheet" href="/webjars/jstree/3.3.8/themes/default/style.min.css" />
<script type="text/javascript" src="/webjars/jstree/3.3.8/jstree.min.js"></script>

<style type="text/css">
	/* webjars 로 바꾸면서 이미지를 따로 지정해준다. */
	.jstree-default .jstree-node, .jstree-default .jstree-icon {background-image: url(/common/images/jsTree/32px.png);}
</style>

<script type="text/javascript">
//<![CDATA[
	//페이지 로드 후 메뉴트리 최상단으로 이동
	window.onload = function(){
	$("#id_resize").scrollTop(0);
	}

	$(function() {
		$('#jstree').jstree({
			"core" : {
				"themes" : {
					"icons" : false,
				}
			},
			"checkbox" : {
				"keep_selected_style" : false,
				"whole_node" : true,
			},
			"plugins" : [
				"wholerow", "checkbox"
			],
			'types': {
				'default' : {
					'icon' : '/common/js/jsTree_new/themes/default/40px1.png'
				}
			}
		});

		$('#jstree').jstree("open_all");

		var checkboxCnt = $( "input[name='menu_pk']" ).length;

		for(i = 0; i < checkboxCnt; i++){
			if($("input[name='menu_pk']").eq(i).prop("checked") == true){
				if($("input[name='menu_pk']").eq(i).parent().parent().find("input:checked").length == 1){	//말단 객체만 클릭 이벤트 실행
					$(".jstree-anchor").eq(i+1).trigger('click');
				}
			}
		}

		$("#id_resize").css("height", $(window).height()-200);
		$(window).resize(function(){
			$("#id_resize").css("height", $(window).height()-200);
		});

	});

	function fnSetCheckBox(){
		var checkboxCnt = $(".jstree-anchor").length;
		for(i = 0; i < checkboxCnt; i++){
			if(i > 0 && $(".jstree-anchor").eq(i).hasClass("jstree-clicked") || $(".jstree-checkbox").eq(i).hasClass("jstree-undetermined")){
				$("input[name='menu_pk']").eq(i-1).prop("checked", true);
			}
			else{
				$("input[name='menu_pk']").eq(i-1).prop("checked", false);
			}
		}
	}

	function fnInsert(){
		// 전체를 저장시에는 jstree가 접혀있을경우 하위 태그들을 삭제하기 때문에 모두 펼친후 저장하도록 한다.
		$('#jstree').jstree("open_all");

		fnSetCheckBox();

		if(confirm("등록하시겠습니까?")){
			$("#aform").attr({action:"/admin/authmenu/insertAuthMenuMgt.do", method:'post'}).submit();
		}
	}

	function fnList(){
		$("#aform").attr({action:"/admin/authmenu/selectPageListAuthMenuMgt.do", method:'post'}).submit();
	}

	function fnSelectCheckbox(menuId){

		if($("#"+menuId).prop("checked") == true){
			$("#"+menuId).prop("checked", false);
		}
		else{
			$("#"+menuId).prop("checked", true);
		}
	}

	function fnChageParentMenuId(){
		$("#aform").attr({action:"/admin/authmenu/selectListAuthMenuMgt.do", method:'post'}).submit();
	}
//]]>
</script>
<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form id="aform" method="post" action="/admin/auth/selectPageListAuthMenuMgt.do">
					<!--검색조건-->
					<input type="hidden" name="author_id" id="author_id" value="${requestScope.param.author_id}"/>
					<input type="hidden" name="sch_authmenu_nm" value = "${requestScope.param.sch_authmenu_nm}"/>
					<!--페이징조건-->
					<input type = "hidden" name = "currentPage" value = "${requestScope.param.currentPage}"/>

					<div class="row">
						<div class="col-lg-6">
							<div class="card card-info card-outline" id="id_resize" style="overflow-y: auto;height: 1px;" >
								<div class="card-header">
									<div class="form-row">
										<div class="justify-content-start">
											<div class="form-inline">
												<select name="sch_parent_menu_id" id="sch_parent_menu_id" class="form-control" onchange="fnChageParentMenuId();">
													${CommboUtil:getComboStr(parentMenuIdComboStr, 'MENU_ID', 'MENU_NM', requestScope.param.sch_parent_menu_id, '')}
												</select>
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
												<c:set var="chkStr" value=""/>
												<c:forEach var="item" items="${resultList}" varStatus="status">
													<c:set var="chkStr" value=""/>
													<c:if test="${item.AUTH_YN == 'Y'}">
														<c:set var="chkStr" value="checked"/>
													</c:if>
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
														<input type="checkbox" class="d-none" name="menu_pk" id="${item.MENU_PK}" value="${item.MENU_PK}" ${chkStr} />
														${item.MENU_NM}
													</c:if>
													<c:set var="minus" value="${item.MENU_LEVEL}"/>
												</c:forEach>
												<c:forEach var="i" begin="1" end="${tagSumCnt}">
													</li></ul>
												</c:forEach>
											</li>
										</ul>
									</div>
									<!-- /.좌측 메뉴 트리 -->
								</div>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="card card-info card-outline">
								<div class="card-header">
									<h3 class="card-title">권한</h3>
								</div>
								<div class="card-body">
									<div class="table-responsive">
										<table id="defaultTb" class="table text-nowrap">
											<colgroup>
												<col style="width: 20%;">
												<col style="width: 80%;">
											</colgroup>
											<tbody>
												<tr>
													<th>권한 ID</th>
													<td>
														<div class="outBox1">
															<%=param.getString("author_id")%>
														</div>
													</td>
												</tr>
												<tr>
													<th>권한 명</th>
													<td>
														<div class="outBox1">
															<%=param.getString("AUTHOR_NM")%>
														</div>
													</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								<div class="card-footer">
									<div class="form-row justify-content-end">
										<div class="form-inline">
											<button type="button" class="btn bg-gradient-secondary mr-2" onclick="fnList(); return false;">목록</button>
											<button type="button" class="btn bg-gradient-success" onclick="fnInsert(); return false;">확인</button>
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
