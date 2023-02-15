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

		$("#style2 a").on('click', function(e) {
			e.preventDefault();
		});

		$("#style3 a").on('click', function(e) {
			e.preventDefault();
		});

		//파일이름
		var fileNames = "${preViewData.fileName}";

		if (fileNames != '') {
			var fileNameVal = fileNames.split(',');
			var text = "";

			for (var i = 0; i < fileNameVal.length; i++) {
				text += i + 1 + "." + fileNameVal[i] + "\n";
			}
			text = text.replace(/(?:\r\n|\r|\n)/g, '<br />');
			$('.fileNames').html(text);

		}

	});
</script>

<div class="container pt-3">
	<!-- 타입 분기  -->
	<div class="typeDistribution">
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
							<div class="col-lg-6">
								<p>
									<strong>게시판 명: </strong>${preViewData.BBS_NM }</p>
							</div>

							<c:if
								test="${preViewData.USER_NM != null and preViewData.USER_NM != '' }">
								<div class="col-lg-3">
									<p>
										<strong>작성자: </strong>${preViewData.USER_NM }</p>
								</div>
							</c:if>

							<c:if
								test="${preViewData.HIT_CNT != null and preViewData.HIT_CNT != '' }">
								<div class="col-lg-3">
									<p>
										<strong>조회수: </strong>${preViewData.HIT_CNT }</p>
								</div>
							</c:if>

						</div>
						<hr>

						<div class="row">
							<div class="col-lg-6">
								<p class="text-left">
									<strong>제목: </strong>${preViewData.SJ }</p>
							</div>


							<c:if
								test="${preViewData.REGIST_DT != null and preViewData.REGIST_DT != '' }">
								<div class="col-lg-4 ">
									<p>
										<strong>등록일자: </strong>${preViewData.REGIST_DT }</p>
								</div>
							</c:if>

							<c:if
								test="${preViewData.REGIST_DT == '' }">
								<div class="col-lg-4 ">
									<p>
										<strong>현재날짜: </strong>${preViewData.SYSDATE }</p>
								</div>
							</c:if>
						</div>


						<c:if test="${preViewData.CN != null and preViewData.CN != '' }">
							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>내용: </strong>${preViewData.CN }</p>
								</div>
							</div>
						</c:if>
						<hr>

						<c:if
							test="${preViewData.TEXT_CN != null and preViewData.TEXT_CN != '' }">
							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>텍스트 내용: </strong>${preViewData.TEXT_CN }</p>
								</div>
							</div>
						</c:if>

						<c:if test="${preViewData.RM != null and preViewData.RM != '' }">
							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>비고: </strong>${preViewData.RM }</p>
								</div>
							</div>
						</c:if>

						<c:if
							test="${preViewData.LINK_URL != null and preViewData.LINK_URL != '' }">
							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>링크: </strong>${preViewData.LINK_URL }</p>
								</div>
							</div>
						</c:if>

						<div class="row">
							<div class="col-lg-12">
								<p>
									<strong>첨부파일: </strong><br> <span class="fileNames"></span>
								</p>

							</div>
						</div>

						<c:if
							test="${preViewData.MVP_URL != null and preViewData.MVP_URL != '' }">
							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>동영상: </strong>
									</p>
								</div>
							</div>

							<div class="justify-content-center">
								<div class="col-lg-8">
									<div class="embed-responsive embed-responsive-16by9">
										<iframe src="${preViewData.MVP_URL }"></iframe>
									</div>
								</div>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>
		<!-- A타입 끝 -->

		<!-- B타입(전체 페이지) 시작 -->
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
								<div class="col-lg-6">
									<p>
										<strong>게시판 명: </strong>${preViewData.BBS_NM }</p>
								</div>


								<c:if
									test="${preViewData.USER_NM != null and preViewData.USER_NM != '' }">
									<div class="col-lg-3">
										<p>
											<strong>작성자: </strong>${preViewData.USER_NM }</p>
									</div>
								</c:if>

								<c:if
									test="${preViewData.HIT_CNT != null and preViewData.HIT_CNT != '' }">
									<div class="col-lg-3">
										<p>
											<strong>조회수: </strong>${preViewData.HIT_CNT }</p>
									</div>
								</c:if>

							</div>
							<hr>

							<div class="row">
								<div class="col-lg-6">
									<p class="text-left">
										<strong>제목: </strong>${preViewData.SJ }</p>
								</div>


								<c:if
									test="${preViewData.REGIST_DT != null and preViewData.REGIST_DT != '' }">
									<div class="col-lg-4 ">
										<p>
											<strong>등록일자: </strong>${preViewData.REGIST_DT }</p>
									</div>
								</c:if>

								<c:if
									test="${preViewData.REGIST_DT == null and preViewData.REGIST_DT == '' }">
									<div class="col-lg-4 ">
										<p>
											<strong>현재날짜: </strong>${preViewData.SYSDATE }</p>
									</div>
								</c:if>
							</div>


							<c:if test="${preViewData.CN != null and preViewData.CN != '' }">
								<div class="row">
									<div class="col-lg-12">
										<p>
											<strong>내용: </strong>${preViewData.CN }</p>
									</div>
								</div>
							</c:if>
							<hr>

							<c:if
								test="${preViewData.TEXT_CN != null and preViewData.TEXT_CN != '' }">
								<div class="row">
									<div class="col-lg-12">
										<p>
											<strong>텍스트 내용: </strong>${preViewData.TEXT_CN }</p>
									</div>
								</div>
							</c:if>

							<c:if test="${preViewData.RM != null and preViewData.RM != '' }">
								<div class="row">
									<div class="col-lg-12">
										<p>
											<strong>비고: </strong>${preViewData.RM }</p>
									</div>
								</div>
							</c:if>

							<c:if
								test="${preViewData.LINK_URL != null and preViewData.LINK_URL != '' }">
								<div class="row">
									<div class="col-lg-12">
										<p>
											<strong>링크: </strong>${preViewData.LINK_URL }</p>
									</div>
								</div>
							</c:if>

							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>첨부파일: </strong><br> <span class="fileNames"></span>
									</p>

								</div>
							</div>

							<c:if
								test="${preViewData.MVP_URL != null and preViewData.MVP_URL != '' }">
								<div class="row">
									<div class="col-lg-12">
										<p>
											<strong>동영상: </strong>
										</p>
									</div>
								</div>

								<div class="justify-content-center">
									<div class="col-lg-8">
										<div class="embed-responsive embed-responsive-16by9">
											<iframe src="${preViewData.MVP_URL }"></iframe>
										</div>
									</div>
								</div>
							</c:if>
						</div>
					</div>
				</div>
				<footer class="main-footer type1 bg-dark" style="text-align: center">
					<strong>FOOTER</strong>
					<div class="float-right d-none d-sm-inline-block"></div>
				</footer>

			</div>
		</div>
		<!-- b타입끝 -->
		<!--  c타입 시작 -->
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
								<div class="col-lg-6">
									<p>
										<strong>게시판 명: </strong>${preViewData.BBS_NM }</p>
								</div>


								<c:if
									test="${preViewData.USER_NM != null and preViewData.USER_NM != '' }">
									<div class="col-lg-3">
										<p>
											<strong>작성자: </strong>${preViewData.USER_NM }</p>
									</div>
								</c:if>

								<c:if
									test="${preViewData.HIT_CNT != null and preViewData.HIT_CNT != '' }">
									<div class="col-lg-3">
										<p>
											<strong>조회수: </strong>${preViewData.HIT_CNT }</p>
									</div>
								</c:if>

							</div>
							<hr>

							<div class="row">
								<div class="col-lg-6">
									<p class="text-left">
										<strong>제목: </strong>${preViewData.SJ }</p>
								</div>


								<c:if
									test="${preViewData.REGIST_DT != null and preViewData.REGIST_DT != '' }">
									<div class="col-lg-4 ">
										<p>
											<strong>등록일자: </strong>${preViewData.REGIST_DT }</p>
									</div>
								</c:if>

								<c:if
									test="${preViewData.REGIST_DT == null and preViewData.REGIST_DT == '' }">
									<div class="col-lg-4 ">
										<p>
											<strong>현재날짜: </strong>${preViewData.SYSDATE }</p>
									</div>
								</c:if>
							</div>


							<c:if test="${preViewData.CN != null and preViewData.CN != '' }">
								<div class="row">
									<div class="col-lg-12">
										<p>
											<strong>내용: </strong>${preViewData.CN }</p>
									</div>
								</div>
							</c:if>
							<hr>

							<c:if
								test="${preViewData.TEXT_CN != null and preViewData.TEXT_CN != '' }">
								<div class="row">
									<div class="col-lg-12">
										<p>
											<strong>텍스트 내용: </strong>${preViewData.TEXT_CN }</p>
									</div>
								</div>
							</c:if>

							<c:if test="${preViewData.RM != null and preViewData.RM != '' }">
								<div class="row">
									<div class="col-lg-12">
										<p>
											<strong>비고: </strong>${preViewData.RM }</p>
									</div>
								</div>
							</c:if>

							<c:if
								test="${preViewData.LINK_URL != null and preViewData.LINK_URL != '' }">
								<div class="row">
									<div class="col-lg-12">
										<p>
											<strong>링크: </strong>${preViewData.LINK_URL }</p>
									</div>
								</div>
							</c:if>

							<div class="row">
								<div class="col-lg-12">
									<p>
										<strong>첨부파일: </strong><br> <span class="fileNames"></span>
									</p>

								</div>
							</div>

							<c:if
								test="${preViewData.MVP_URL != null and preViewData.MVP_URL != '' }">
								<div class="row">
									<div class="col-lg-12">
										<p>
											<strong>동영상: </strong>
										</p>
									</div>
								</div>

								<div class="justify-content-center">
									<div class="col-lg-8">
										<div class="embed-responsive embed-responsive-16by9">
											<iframe src="${preViewData.MVP_URL }"></iframe>
										</div>
									</div>
								</div>
							</c:if>
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
	<!-- ntware 기반 ui -->
	<%-- <div class="card">
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
													<th>시스템</th>
													<td>${preViewData.SYS_CODE_NM }</td>
													<th>게시판명</th>
													<td>${preViewData.BBS_NM }</td>
												</tr>

												<tr>
													<th>작성자</th>
													<td>
														<c:if test="${preViewData.USER_NM != null and preViewData.USER_NM != '' }">
															${preViewData.USER_NM }
														</c:if>

														<c:if test="${preViewData.USER_NM == '' }">
															작성자
														</c:if>

													</td>
													<th>연락처</th>
													<td>	[${ preViewData.CTTPC_SE_NM }]
														${preViewData.REGISTER_CTTPC }
													</td>
												</tr>

												<tr>
													<th>조회수</th>
													<td>
														<c:if test="${preViewData.HIT_CNT != null and preViewData.HIT_CNT != '' }">
															${preViewData.HIT_CNT }
														</c:if>

														<c:if test="${preViewData.HIT_CNT == '' }">
															0
														</c:if>
													</td>
													<th>등록일</th>
													<td>
														<c:if test="${preViewData.REGIST_DT != null and preViewData.REGIST_DT != '' }">
															${preViewData.REGIST_DT }
														</c:if>

														<c:if test="${preViewData.REGIST_DT == '' }">
															${preViewData.SYSDATE }
														</c:if>
													</td>
												</tr>

												<tr>
													<th>제목</th>
													<td colspan="3">${preViewData.SJ }</td>
												</tr>

												<tr>
													<th>내용</th>
													<td colspan="3">${preViewData.CN }</td>
												</tr>

												<tr>
													<th>텍스트내용</th>
													<td colspan="3">${preViewData.TEXT_CN }</td>
												</tr>

												<tr>
													<th>비고</th>
													<td colspan="3">${preViewData.RM }</td>
												</tr>

												<tr>
													<th>링크</th>
													<td colspan="3">${preViewData.LINK_URL }</td>
												</tr>

												<tr>
													<th>동영상</th>
													<td colspan="3">
														<div class="embed-responsive embed-responsive-16by9">
															<iframe src="${preViewData.MVP_URL }"></iframe>
														</div>
													</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</section>
				</div>

			</div> --%>

	<!-- B타입 끝 -->

</div>














