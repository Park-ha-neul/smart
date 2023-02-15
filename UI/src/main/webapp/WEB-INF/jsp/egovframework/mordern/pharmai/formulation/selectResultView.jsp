<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Util" uri="/WEB-INF/tlds/Util.tld"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>

<script type="text/javascript">
//<![CDATA[

	$(function () {
		$('[data-toggle="tooltip"]').tooltip()
	})

//]]>
</script>

<style type="text/css">
	.result-view-title{ text-align: center; letter-spacing: 0px; color: #555555; background: #FAFAFA 0% 0% no-repeat padding-box;}
	.result-view{width: 100%; border: 1px solid #E2E2E2;}
	.svgZoomformulation{border: 1px solid #E2E2E2;}
</style>

<div class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<form role="form" id="aform" method="post" action="" enctype="multipart/form-data">
					<div class="card card-info card-outline">
						<div class="card-body">
							<div class="row">
								<div class="col-sm-4">
									<div class="row">
										<h3 class="card-title">Input</h3>
									</div>
									<div class="svgZoomformulation mt10" style="overflow: hidden;">
									<!--?xml version='1.0' encoding='iso-8859-1'?-->
										<svg version="1.1" baseProfile="full" xmlns="http://www.w3.org/2000/svg" xmlns:rdkit="http://www.rdkit.org/xml" xmlns:xlink="http://www.w3.org/1999/xlink" xml:space="preserve" width="900px" height="300px">
										<rect style="opacity:1.0;fill:#FFFFFF;stroke:none" width="900" height="300" x="0" y="0"> </rect>
										<path d="M 244.291,57.9247 229.513,75.9866" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 229.513,75.9866 214.736,94.0484" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 198.031,102.557 172.976,98.4586" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 172.976,98.4586 147.921,94.3604" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 210.372,113.876 219.016,136.739" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 219.016,136.739 227.66,159.602" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 153.485,92.2568 144.841,69.3939" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 144.841,69.3939 136.197,46.5311" style="fill:none;fill-rule:evenodd;stroke:#FF0000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 142.357,96.464 133.713,73.6011" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 133.713,73.6011 125.069,50.7383" style="fill:none;fill-rule:evenodd;stroke:#FF0000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 147.921,94.3604 110.253,140.398" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 110.253,140.398 131.29,196.038" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 124.537,144.537 139.262,183.484" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 110.253,140.398 84.8414,141.601" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 84.8414,141.601 59.4294,142.804" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 131.29,196.038 112.378,211.194" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 112.378,211.194 93.4665,226.351" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 131.29,196.038 156.345,200.136" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 156.345,200.136 181.4,204.234" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 76.2808,227.596 55.7159,214.092" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 55.7159,214.092 35.1509,200.589" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 76.6411,213.6 62.2456,204.148" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 62.2456,204.148 47.8502,194.696" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 35.1509,200.589 41.6386,176.857" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 41.6386,176.857 48.1263,153.125" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 42.891,133.297 28.2637,115.046" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 28.2637,115.046 13.6364,96.7951" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 193.741,215.554 202.385,238.416" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 202.385,238.416 211.029,261.279" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 198.104,195.726 212.882,177.664" style="fill:none;fill-rule:evenodd;stroke:#0000FF;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 212.882,177.664 227.66,159.602" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 226.7,165.472 251.422,169.516" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 251.422,169.516 276.144,173.56" style="fill:none;fill-rule:evenodd;stroke:#FF0000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 228.62,153.732 253.342,157.775" style="fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<path d="M 253.342,157.775 278.064,161.819" style="fill:none;fill-rule:evenodd;stroke:#FF0000;stroke-width:2px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"></path>
										<text x="198.031" y="113.876" style="font-size:19px;font-style:normal;font-weight:normal;fill-opacity:1;stroke:none;font-family:sans-serif;text-anchor:start;fill:#0000FF"><tspan>N</tspan></text>
										<text x="117.625" y="48.6347" style="font-size:19px;font-style:normal;font-weight:normal;fill-opacity:1;stroke:none;font-family:sans-serif;text-anchor:start;fill:#FF0000"><tspan>O</tspan></text>
										<text x="76.2808" y="243.152" style="font-size:19px;font-style:normal;font-weight:normal;fill-opacity:1;stroke:none;font-family:sans-serif;text-anchor:start;fill:#0000FF"><tspan>N</tspan></text>
										<text x="42.2436" y="153.125" style="font-size:19px;font-style:normal;font-weight:normal;fill-opacity:1;stroke:none;font-family:sans-serif;text-anchor:start;fill:#0000FF"><tspan>N</tspan></text>
										<text x="181.4" y="215.554" style="font-size:19px;font-style:normal;font-weight:normal;fill-opacity:1;stroke:none;font-family:sans-serif;text-anchor:start;fill:#0000FF"><tspan>N</tspan></text>
										<text x="277.104" y="179.118" style="font-size:19px;font-style:normal;font-weight:normal;fill-opacity:1;stroke:none;font-family:sans-serif;text-anchor:start;fill:#FF0000"><tspan>O</tspan></text>
										</svg>
									</div>
									<span data-toggle="tooltip" data-placement="top" title="CN1C(=O)C2=C(N=CN2C)N(C)C1=O">
										<p class="result-view-left-text overflow">SMILES : CN1C(=O)C2=C(N=CN2C)N(C)C1=O</p>
									</span>
								</div>
								<div class="col-sm-4">
									<div class="row">
										<h3 class="card-title">Properties</h3>
										<button type="button" class="btn btn-default btn-xs l-Align mr-1"  onclick="return false;">
											<ion-icon name="document-text-outline"></ion-icon>
											Excel download
										</button>
									</div>
									<div class="clearfix"></div>
									<div>
										<table class="table table-bordered mt10" id="tb_property">
											<tbody>
												<tr>
													<td class="text-center result-view-title">pH Mass_Solubility (g/L)</th>
												</tr>
											</tbody>
										</table>
										<div class="center-block" style="width: 290px; height: 226px;">
											<div style="zoom: 0.5;"></div>
										</div>
									</div>
									<div>
										<table class="table table-bordered mt10" id="tb_property_1">
											<tbody>
												<tr>
													<td class="text-center result-view-title">pH logD</th>
												</tr>
											</tbody>
										</table>
										<div class="center-block" style="width: 290px; height: 226px;">
											<div style="zoom: 0.5;"></div>
										</div>
									</div>
								</div>
								<div class="col-sm-4">
									<div class="row ml-1">
										<h3 class="card-title">Dosage form selection</h3>
									</div>
									<div class="mt10">
										<div class="card-body p-0 result-view">
											<table class="table table-sm" id="physical-properties-table">
												<thead>
													<tr>
														<td class="text-center">Physical properties</td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td class="text-center">
															<input type="checkbox" value="CODE0000001"><span data-toggle="modal" data-target="#modal-sm">Molecular weight</span>
														</td>
													</tr>
													<tr>
														<td class="text-center">
															<input type="checkbox" value="CODE0000001"><span data-toggle="modal" data-target="#modal-sm">Molecular weight</span>
														</td>
													</tr>
												</tbody>
											</table>
											<div class="center mt20">
												<button type="button" class="btn btn-danger" >save</button>
											</div>
											<div class="mt20 ml-1 mr-1">
												<table class="table table-bordered" id="result-view-data-table-1">
													<thead>
														<tr>
															<td class="text-center">Physical properties</td>
															<td class="text-center">Route</td>
															<td class="text-center">Form</td>
															<td class="text-center">Inactive Ingredient</td>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td class="text-center">pH 3 solubility</td>
															<td class="text-center">N/A</td>
															<td class="text-center">N/A</td>
															<td class="text-center">N/A</td>
														</tr>
													</tbody>
												</table>
											</div>
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

<div class="modal fade" id="modal-sm" style="display: none;" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<p>Route</p>
				<p>(Molecular weight)</p>

			</div>
			<div class="modal-body">
				<table class="popup_checkbox_table table-sm ">
					<tbody>
						<tr>
							<td>
								<input type="checkbox" class="mr-3" value="CODE0000008"><span>Oral</span>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" class="mr-3" value="CODE0000009"><span>Parenteral</span>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" class="mr-3" value="CODE0000010"><span>Local</span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer justify-content-between">
				<button type="button" class="btn btn-danger">Confirm</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
</div>
</div>