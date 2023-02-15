<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="CacheCommboUtil"
	uri="/WEB-INF/tlds/CacheCommboUtil.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp"%>

<script>
	$(document).ready(function() {
		//toggle 및 클릭 버튼 기능 remove();

		$('#style1 a').on('click', function(e) {
			e.preventDefault();
		});

		$("#style2").on('click', function(e) {
			e.preventDefault();
		});

		$("#style3 a").on('click', function(e) {
			e.preventDefault();
		});

		//파일이름
		var fileNames = "${cntsData.fileName}";

		if(fileNames != ''){
			var fileNameVal = fileNames.split(',');
			var text = "";

			for(var i = 0; i < fileNameVal.length; i++){
				text += i+1 + "." + fileNameVal[i] + "\n ";
			}
			text = text.replace(/(?:\r\n|\r|\n)/g, '<br />');
			$('.fileNames').html(text);

		}
	});
</script>



<div class="container pt-3">
	<div class="typeDistribution">
		<!-- 타입 분기  -->
		<ul class="nav  nav-justified pb-3">
			<li class="nav-item"><a class="nav-link active"
				data-toggle="tab" href="#style1">A타입(컨텐츠영역)</a></li>

			<li class="nav-item"><a class="nav-link" data-toggle="tab"
				href="#style2">B타입(전체)</a></li>

			<li class="nav-item"><a class="nav-link" data-toggle="tab"
				href="#style3">C타입(헤더, 푸터)</a></li>
		</ul>
	</div>

	<div class="tab-content">
		<!-- style1 A타입(컨텐츠영역) 시작 -->
		<div id="style1" class="tab-pane active">
			<div class="container-fluid">
				<div class="card">
					<div class="card-header bg-secondary text-white">
						<span class="font-weight-bold"><i>미리보기(컨텐츠영역)- 미리보기
								영역으로 페이지 전환은 불가합니다.</i></span>
					</div>
					<div class="card-body">
						<div class="row">

							<div class="col-lg-4">
								<p>
									<strong>콘텐츠아이디: </strong>${cntsData.CNTNTS_ID }</p>
							</div>

							<div class="col-lg-4">
								<c:if
									test="${cntsData.HIT_CNT != null and cntsData.HIT_CNT != '' }">
									<p>
										<strong>조회수: </strong>${cntsData.HIT_CNT }</p>
								</c:if>

								<c:if test="${cntsData.HIT_CNT == '' }">
									<p>
										<strong>조회수: </strong>0
									</p>
								</c:if>

							</div>

						</div>

						<div class="row">
							<c:if
								test="${cntsData.REGIST_DT != NULL and cntsData.REGIST_DT != '' }">
								<div class="col-lg-4">
									<p>
										<strong>등록일시: </strong>${cntsData.REGIST_DT }</p>
								</div>
							</c:if>

							<c:if test="${cntsData.REGIST_DT == '' }">
								<div class="col-lg-4">
									<p>
										<strong>등록일시: </strong>${cntsData.SYSDATE }</p>
								</div>
							</c:if>

							<c:if
								test="${cntsData.UPDT_DT != NULL and cntsData.UPDT_DT != '' }">
								<div class="col-lg-4">
									<p>
										<strong>수정일시: </strong>${cntsData.UPDT_DT }</p>
								</div>
							</c:if>

							<c:if test="${cntsData.UPDT_DT == '' }">
								<div class="col-lg-4">
									<p>
										<strong>수정일시: </strong>${cntsData.SYSDATE }</p>
								</div>
							</c:if>

						</div>
						<hr>
						<div class="row">
							<div class="col-lg-12">
								<p>
									<strong>제목: </strong>${cntsData.SJ }</p>
							</div>
						</div>


						<div class="row">
							<div class="col-lg-12">
								<p>
									<strong>내용: </strong>${cntsData.CN }</p>
							</div>
						</div>
						<hr>


						<div class="row">
							<div class="col-lg-12">
								<p>
									<strong>텍스트 내용: </strong>${cntsData.TEXT_CN }</p>
							</div>
						</div>



						<div class="row">
							<div class="col-lg-12">
								<p>
									<strong>스크립트 </strong>${cntsData.SCRIPT }</p>
							</div>
						</div>


						<div class="row">
							<div class="col-lg-12">
								<p>
									<strong>첨부파일: </strong><br> <span class="fileNames"></span>
								</p>

							</div>
						</div>



						<div class="row">
							<div class="col-lg-4">
								<p>
									<strong>키워드: </strong>${cntsData.KWRD }</p>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
		<!-- A타입 끝 -->



		<!-- B타입 시작 -->
		<div id="style2" class="tab-pane bd-navbar">
			<div class="container-fluid">
				<header>
					<nav
						class="main-header sidebar-dark-primary dark ${ssThemaOption.t4}"
						style="padding-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px;">
						<h1 style="color: white; text-align: center;">HEADER GNB_BAR</h1>
					</nav>
				</header>
				<section class="bg-sidebar">
					<aside class="main-sidebar elevation-4 sidebar-dark-primary dark">
						<!-- Brand Logo -->
						<a href="#" class="brand-link ${ssThemaOption.t4} text-center">
							<span>로고</span>
						</a>
						<!-- Sidebar -->

						<div class="sidebar">
							<!-- Sidebar user panel (optional) -->
							<div class="user-panel mt-3 pb-3 mb-3 d-flex">
								<div class="info top_menu"></div>
							</div>

							<!-- Sidebar Menu -->
							<nav class="mt-3">
								<ul id="sidebar"
									class="nav nav-pills nav-sidebar flex-column nav-flat nav-child-indent ${ssThemaOption.c3} ${ssThemaOption.c4} "
									data-widget="treeview" role="menu" data-accordion="false">
									<li class="text-center"><span
										style="font-size: 30px; color: white;">sidebar</span></li>
								</ul>
							</nav>
							<!-- /.sidebar-menu -->
						</div>
						<!-- /.sidebar -->
					</aside>
				</section>

				<div class="content-wrapper pt-3 pb-1">
					<div class="card">
						<div class="card-header bg-secondary text-white">
							<span class="font-weight-bold"><i>미리보기(컨텐츠영역)- 미리보기
									영역으로 페이지 전환은 불가합니다.</i></span>
						</div>
						<div class="card-body">
							<div class="row">

								<div class="col-lg-4">
									<p>
										<strong>콘텐츠아이디: </strong>${cntsData.CNTNTS_ID }</p>
								</div>

								<div class="col-lg-4">
									<c:if
										test="${cntsData.HIT_CNT != null and cntsData.HIT_CNT != '' }">
										<p>
											<strong>조회수: </strong>${cntsData.HIT_CNT }</p>
									</c:if>

									<c:if test="${cntsData.HIT_CNT == '' }">
										<p>
											<strong>조회수: </strong>0
										</p>
									</c:if>

								</div>

							</div>

							<div class="row">
								<c:if
									test="${cntsData.REGIST_DT != NULL and cntsData.REGIST_DT != '' }">
									<div class="col-lg-4">
										<p>
											<strong>등록일시: </strong>${cntsData.REGIST_DT }</p>
									</div>
								</c:if>

								<c:if test="${cntsData.REGIST_DT == '' }">
									<div class="col-lg-4">
										<p>
											<strong>등록일시: </strong>${cntsData.SYSDATE }</p>
									</div>
								</c:if>

								<c:if
									test="${cntsData.UPDT_DT != NULL and cntsData.UPDT_DT != '' }">
									<div class="col-lg-4">
										<p>
											<strong>수정일시: </strong>${cntsData.UPDT_DT }</p>
									</div>
								</c:if>

								<c:if test="${cntsData.UPDT_DT == '' }">
									<div class="col-lg-4">
										<p>
											<strong>수정일시: </strong>${cntsData.SYSDATE }</p>
									</div>
								</c:if>

							</div>
							<hr>
							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>제목: </strong>${cntsData.SJ }</p>
								</div>
							</div>

							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>내용: </strong>${cntsData.CN }</p>
								</div>
							</div>

							<hr>

							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>텍스트 내용: </strong>${cntsData.TEXT_CN }</p>
								</div>
							</div>

							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>스크립트 </strong>${cntsData.SCRIPT }</p>
								</div>
							</div>

							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>첨부파일: </strong><br> <span class="fileNames"></span>
									</p>

								</div>
							</div>

							<div class="row">
								<div class="col-lg-4">
									<p>
										<strong>키워드: </strong>${cntsData.KWRD }</p>
								</div>
							</div>

						</div>
					</div>
				</div>
				<footer class="main-footer type1 bg-dark" style="text-align: center">
					<strong>FOOTER</strong>
					<div class="float-right d-none d-sm-inline-block"></div>
				</footer>

			</div>
		</div>


		<!-- B타입 끝 -->
		<!-- C타입 시작 -->
		<div id="style3" class="tab-pane bd-navbar">
			<div class="container-fluid">
				<header>
					<nav
						class="main-header sidebar-dark-primary dark ${ssThemaOption.t4}"
						style="padding-top: 0px; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; margin-left: 0;">
						<h1 style="color: white; text-align: center;">HEADER GNB_BAR</h1>
					</nav>
				</header>

				<div class="content pt-3 pb-1">
					<div class="card">
						<div class="card-header bg-secondary text-white">
							<span class="font-weight-bold"><i>미리보기(컨텐츠영역)- 미리보기
									영역으로 페이지 전환은 불가합니다.</i></span>
						</div>
						<div class="card-body">
							<div class="row">

								<div class="col-lg-4">
									<p>
										<strong>콘텐츠아이디: </strong>${cntsData.CNTNTS_ID }</p>
								</div>

								<div class="col-lg-4">
									<c:if
										test="${cntsData.HIT_CNT != null and cntsData.HIT_CNT != '' }">
										<p>
											<strong>조회수: </strong>${cntsData.HIT_CNT }</p>
									</c:if>

									<c:if test="${cntsData.HIT_CNT == '' }">
										<p>
											<strong>조회수: </strong>0
										</p>
									</c:if>

								</div>

							</div>

							<div class="row">
								<c:if
									test="${cntsData.REGIST_DT != NULL and cntsData.REGIST_DT != '' }">
									<div class="col-lg-4">
										<p>
											<strong>등록일시: </strong>${cntsData.REGIST_DT }</p>
									</div>
								</c:if>

								<c:if test="${cntsData.REGIST_DT == '' }">
									<div class="col-lg-4">
										<p>
											<strong>등록일시: </strong>${cntsData.SYSDATE }</p>
									</div>
								</c:if>

								<c:if
									test="${cntsData.UPDT_DT != NULL and cntsData.UPDT_DT != '' }">
									<div class="col-lg-4">
										<p>
											<strong>수정일시: </strong>${cntsData.UPDT_DT }</p>
									</div>
								</c:if>

								<c:if test="${cntsData.UPDT_DT == '' }">
									<div class="col-lg-4">
										<p>
											<strong>수정일시: </strong>${cntsData.SYSDATE }</p>
									</div>
								</c:if>

							</div>
							<hr>
							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>제목: </strong>${cntsData.SJ }</p>
								</div>
							</div>

							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>내용: </strong>${cntsData.CN }</p>
								</div>
							</div>

							<hr>

							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>텍스트 내용: </strong>${cntsData.TEXT_CN }</p>
								</div>
							</div>

							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>스크립트 </strong>${cntsData.SCRIPT }</p>
								</div>
							</div>

							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>첨부파일: </strong><br> <span class="fileNames"></span>
									</p>
								</div>
							</div>

							<div class="row">
								<div class="col-lg-4">
									<p>
										<strong>키워드: </strong>${cntsData.KWRD }</p>
								</div>
							</div>

						</div>
					</div>
				</div>
				<footer class="main-footer type1 bg-dark"
					style="text-align: center; margin-left: 0;">
					<strong>FOOTER</strong>
					<div class="float-right d-none d-sm-inline-block"></div>
				</footer>

			</div>
		</div>
	</div>
</div>

<!-- nt_ware ui form -->
<%-- <div id="style2" class="tab-pane">
			<div id="style2" class="tab-pane">
				<div class="card">
					<jsp:include page="/WEB-INF/tiles/mordern/components/navi.jsp" />
					<jsp:include page="/WEB-INF/tiles/mordern/components/left.jsp" />
					<div class="content-wrapper">
						<section class="content-header">
							<h1></h1>
						</section>
						<section class="content">
							<div class="row">
								<div class="col-lg-12">
									<div class="box box-primary">
										<div class="box-body no-padding">
											<table class="table table-bordered" style="width: 100%">
												<colgroup>
													<col style="width: 15%;">
													<col style="width: 35%;">
													<col style="width: 15%;">
													<col style="width: 35%;">
												</colgroup>
												<tbody>

													<tr>
														<th>버전정보</th>
														<td><c:if
																test="${cntsData.LAST_VER != null and cntsData.LAST_VER != '' }">
																	${cntsData.LAST_VER }
																</c:if> <c:if test="${cntsData.LAST_VER == '' }">
																	버전정보란
																</c:if></td>
														<th>컨텐츠아이디</th>
														<td>${cntsData.CNTNTS_ID }</td>
													</tr>

													<tr>
														<th>제목</th>
														<td>${cntsData.SJ }</td>
														<th>시스템</th>
														<td>${cntsData.SYS_NM }</td>
													</tr>

													<tr>
														<th>등록일시</th>
														<td><c:if
																test="${cntsData.REGIST_DT != NULL and cntsData.REGIST_DT != '' }">
																	${cntsData.REGIST_DT }
																</c:if> <c:if test="${cntsData.REGIST_DT == '' }">
																	${cntsData.SYSDATE }
																</c:if></td>
														<th>수정일시</th>
														<td><c:if
																test="${cntsData.UPDT_DT != NULL and cntsData.UPDT_DT != '' }">
																	${cntsData.UPDT_DT }
																</c:if> <c:if test="${cntsData.UPDT_DT == '' }">
																	${cntsData.SYSDATE }
																</c:if></td>
													</tr>

													<tr>
														<th>조회수</th>
														<td><c:if
																test="${cntsData.HIT_CNT != NULL and cntsData.HIT_CNT != '' }">
																	${cntsData.HIT_CNT}
																</c:if> <c:if test="${cntsData.HIT_CNT == '' }">
																	0
																</c:if></td>
														<th>키워드</th>
														<td>${cntsData.KWRD }</td>
													</tr>

													<tr>
														<th>내용</th>
														<td colspan="3">${cntsData.CN }</td>
													</tr>

													<tr>
														<th>텍스트내용</th>
														<td colspan="3">${cntsData.TEXT_CN }</td>
													</tr>

													<tr>
														<th>스크립트</th>
														<td colspan="3">${cntsData.SCRIPT }</td>
													</tr>


												</tbody>
											</table>
										</div>
									</div>
								</div>
							</div>
						</section>
					</div>

				</div>


			</div>

		</div> --%>






