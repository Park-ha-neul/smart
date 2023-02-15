<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<html>
<head>
<%-- 	<%@ include file="/common/inc/meta.jspf" %> --%>
	<title><%=headTitle%></title>
<%-- 	<%@ include file="/common/inc/cssScript.jspf" %> --%>

	<script type="text/javascript">
	//<![CDATA[

	// ]]>
	</script>
</head>

<!--
	Body 클래스 정의
	=================
	|-------------------------------------------------------|
	| SKINS			| skin-blue								|
	|				| skin-black							|
	|				| skin-purple							|
	|				| skin-yellow							|
	|				| skin-red								|
	|				| skin-green							|
	|LAYOUT OPTIONS | fixed									|
	|				| layout-boxed							|
	|				| layout-top-nav						|
	|				| sidebar-collapse						|
	|				| sidebar-mini							|
	|-------------------------------------------------------|
	-->
<body class="hold-transition sidebar-mini">
<div class="wrapper">

	<!-- 헤더  -->
<%-- 	<c:import url="/common/inc/header.do" charEncoding="utf-8" /> --%>

	<!-- 좌측 메뉴 -->
<%-- 	<c:import url="/common/inc/menu.do" charEncoding="utf-8" /> --%>

	<!-- Content Wrapper. Contains page content -->
<!-- 	<div class="content-wrapper"> -->
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>
			탬플릿
			<small>템플릿 페이지</small>
			</h1>
			<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
			<li class="active">Here</li>
			</ol>
		</section>

		<!-- Main content -->
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box box-primary">
						<div class="box-header">
							<h3 class="box-title">테이블형 리스트</h3>
							<div class="box-tools form-inline">
								<div class="form-group">
									<select class="form-control input-sm">
										<option>option 1</option>
										<option>option 2</option>
										<option>option 3</option>
										<option>option 4</option>
										<option>option 5</option>
									</select>
									<div class="input-group" style="width: 150px;">
										<input type="text" name="table_search" class="form-control input-sm pull-right" placeholder="Search">
										<div class="input-group-btn">
											<button class="btn btn-sm btn-default"><i class="fa fa-search"></i></button>
										</div>
									</div>
								</div>
							</div>
						</div><!-- /.box-header -->
						<div class="box-body table-responsive no-padding">
							<table class="table table-hover">
								<tbody>
									<tr>
										<th>ID</th>
										<th>User</th>
										<th>Date</th>
										<th>Status</th>
										<th>Reason</th>
									</tr>
									<tr>
										<td>183</td>
										<td>John Doe</td>
										<td>11-7-2014</td>
										<td><span class="label label-success">Approved</span></td>
										<td>Bacon ipsum dolor sit amet salami venison chicken flank fatback doner.</td>
									</tr>
									<tr>
										<td>219</td>
										<td>Alexander Pierce</td>
										<td>11-7-2014</td>
										<td><span class="label label-warning">Pending</span></td>
										<td>Bacon ipsum dolor sit amet salami venison chicken flank fatback doner.</td>
									</tr>
									<tr>
										<td>657</td>
										<td>Bob Doe</td>
										<td>11-7-2014</td>
										<td><span class="label label-primary">Approved</span></td>
										<td>Bacon ipsum dolor sit amet salami venison chicken flank fatback doner.</td>
									</tr>
									<tr>
										<td>175</td>
										<td>Mike Doe</td>
										<td>11-7-2014</td>
										<td><span class="label label-danger">Denied</span></td>
										<td>Bacon ipsum dolor sit amet salami venison chicken flank fatback doner.</td>
									</tr>
								</tbody>
							</table>
						</div><!-- /.box-body -->
						<div class="box-footer clearfix">
							<ul class="pagination pagination-sm no-margin pull-right">
								<li><a href="#">&laquo;</a></li>
								<li><a href="#">&lt;</a></li>
								<li><a href="#">1</a></li>
								<li><a href="#">2</a></li>
								<li><a href="#">3</a></li>
								<li><a href="#">&gt;</a></li>
								<li><a href="#">&raquo;</a></li>
							</ul>
						</div>
					</div><!-- /.box -->

					<div class="box box-primary">
						<div class="box-header">
							<h3 class="box-title">테이블 폼</h3>
						</div><!-- /.box-header -->
						<div class="box-body table-responsive no-padding">
							<table class="table table-bordered">
								<colgroup>
									<col style="width:15%;">
									<col style="width:35%;">
									<col style="width:15%;">
									<col style="width:35%;">
								</colgroup>
								<tbody>
									<tr>
										<th>ID</th>
										<th><input type="text" class="form-control" placeholder="input"></th>
										<th>Date</th>
										<th><input type="text" class="form-control" placeholder="input" disabled></th>
									</tr>
									<tr>
										<th>ID</th>
										<th>
											<input type="text" class="form-control" placeholder="input">
										</th>
										<th>Date</th>
										<th>
											<select class="form-control">
												<option>1</option>
												<option>1</option>
												<option>1</option>
												<option>1</option>
												<option>1</option>
											</select>
										</th>
									</tr>
									<tr>
										<th>ID</th>
										<th><input type="file"></th>
										<th>Date</th>
										<th><input type="text" class="form-control" placeholder="input"></th>
									</tr>
									<tr>
										<th>ID</th>
										<th>
											<div class="form-inline">
												<div class="form-group">
													<div class="checkbox">
													<label>
														<input type="checkbox">
														Checkbox 1
													</label>
													</div>

													<div class="checkbox">
													<label>
														<input type="checkbox">
														Checkbox 2
													</label>
													</div>

													<div class="checkbox">
													<label>
														<input type="checkbox" disabled="">
														Checkbox disabled
													</label>
													</div>
												</div>
											</div>
										</th>
										<th>Date</th>
										<th>
											<div class="form-inline">
											<div class="form-group">
												<div class="radio">
												<label>
													<input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked="">
													Option
												</label>
												</div>
												<div class="radio">
												<label>
													<input type="radio" name="optionsRadios" id="optionsRadios2" value="option2">
													Option
												</label>
												</div>
												<div class="radio">
												<label>
													<input type="radio" name="optionsRadios" id="optionsRadios3" value="option3" disabled="">
													Option
												</label>
												</div>
											</div>
											</div>
										</th>
									</tr>
									<tr>
										<th>ID</th>
										<th colspan="3"><textarea class="form-control" rows="3" placeholder="Enter ..."></textarea></th>
									</tr>
								</tbody>
							</table>
						</div><!-- /.box-body -->
						<div class="box-footer">
							<button type="submit" class="btn btn-default">Cancel</button>
							<div class="pull-right">
							<button class="btn btn-default"><i class="fa fa-reply"></i> 목록</button>
							<button type="submit" class="btn btn-info"><i class="fa fa-pencil"></i> 등록</button>
							</div>
						</div>
					</div><!-- /.box -->

					<div class="box box-primary">
						<div class="box-header with-border">
								<h3 class="box-title">1단구성 폼</h3>
						</div><!-- /.box-header -->
						<!-- form start -->
						<form role="form">
							<div class="box-body">
								<div class="form-group">
									<label for="exampleInputEmail1">Email address</label>
									<input type="email" class="form-control" id="exampleInputEmail1" placeholder="Enter email">
								</div>
								<div class="form-group">
									<label for="exampleInputPassword1">Password</label>
									<input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
								</div>
								<div class="form-group">
									<label for="exampleInputFile">File input</label>
									<input type="file" id="exampleInputFile">
									<p class="help-block">Example block-level help text here.</p>
								</div>
								<div class="checkbox">
									<label>
									<input type="checkbox"> Check me out
									</label>
								</div>
							</div><!-- /.box-body -->
							<div class="box-footer">
								<button type="submit" class="btn btn-default">Cancel</button>
								<div class="pull-right">
									<button class="btn btn-default"><i class="fa fa-reply"></i> 목록</button>
									<button type="submit" class="btn btn-info"><i class="fa fa-pencil"></i> 등록</button>
								</div>
							</div>
						</form>
					</div>

					<div class="box box-default">
						<div class="box-header with-border">
							<h3 class="box-title">2단구성 폼</h3>
							<div class="box-tools pull-right">
							<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
							<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>
							</div>
						</div><!-- /.box-header -->
						<div class="box-body">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
									<label>Minimal</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
									<div class="form-group">
									<label>Minimal</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
									<div class="form-group">
									<label>Minimal</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
								</div><!-- /.col -->
								<div class="col-md-6">
									<div class="form-group">
									<label>Multiple</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
									<div class="form-group">
									<label>Multiple</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
									<div class="form-group">
									<label>Multiple</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
								</div><!-- /.col -->
							</div><!-- /.row -->
						</div><!-- /.box-body -->
						<div class="box-footer">
							<button type="submit" class="btn btn-default">Cancel</button>
							<div class="pull-right">
							<button class="btn btn-default"><i class="fa fa-reply"></i> 목록</button>
							<button type="submit" class="btn btn-info"><i class="fa fa-pencil"></i> 등록</button>
							</div>
						</div>
					</div>

					<div class="box box-default">
						<div class="box-header with-border">
							<h3 class="box-title">3단구성 폼</h3>
							<div class="box-tools pull-right">
							<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
							<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>
							</div>
						</div><!-- /.box-header -->
						<div class="box-body">
							<div class="row">
								<div class="col-md-4">
									<div class="form-group">
									<label>Minimal</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
									<div class="form-group">
									<label>Minimal</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
									<div class="form-group">
									<label>Minimal</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
								</div><!-- /.col -->
								<div class="col-md-4">
									<div class="form-group">
									<label>Multiple</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
									<div class="form-group">
									<label>Multiple</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
									<div class="form-group">
									<label>Multiple</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
								</div><!-- /.col -->
								<div class="col-md-4">
									<div class="form-group">
									<label>Multiple</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
									<div class="form-group">
									<label>Multiple</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
									<div class="form-group">
									<label>Multiple</label>
									<input type="text" class="form-control" placeholder="input">
									</div><!-- /.form-group -->
								</div><!-- /.col -->
							</div><!-- /.row -->
						</div><!-- /.box-body -->
						<div class="box-footer">
							<button type="submit" class="btn btn-default">Cancel</button>
							<div class="pull-right">
							<button class="btn btn-default"><i class="fa fa-reply"></i> 목록</button>
							<button type="submit" class="btn btn-info"><i class="fa fa-pencil"></i> 등록</button>
							</div>
						</div>
					</div>
				</div><!--  /.col-xs-12 -->
			</div><!-- ./row -->
		</section><!-- /.content -->
<!-- 	</div>/.content-wrapper -->
</div><!-- ./wrapper -->
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/msg.jsp" %>
</body>

</html>