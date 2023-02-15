<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"	  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"	 uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<html>
<head>
	<title><%=headTitle%></title>

	<script type="text/javascript" src="/webjars/chart.js/2.9.3/dist/Chart.min.js"></script>

	<script type="text/javascript">
	//<![CDATA[
	$(function(){
		var myChart = new Chart($('#myChart'), {
			type: 'bar',
			data: {
				labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
				datasets: [{
					label: '투표수',
					data: [12, 19, 3, 5, 2, -5],
// 					data: [{x:'Red', y:15}],
					backgroundColor: [
						'rgba(255, 99, 132, 0.2)',
						'rgba(54, 162, 235, 0.2)',
						'rgba(255, 206, 86, 0.2)',
						'rgba(75, 192, 192, 0.2)',
						'rgba(153, 102, 255, 0.2)',
						'rgba(255, 159, 64, 0.2)'
					],
					borderColor: [
						'rgba(255, 99, 132, 1)',
						'rgba(54, 162, 235, 1)',
						'rgba(255, 206, 86, 1)',
						'rgba(75, 192, 192, 1)',
						'rgba(153, 102, 255, 1)',
						'rgba(255, 159, 64, 1)'
					],
					borderWidth: 1
				}]
			},
			options: {
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero: true
						},
// 						stacked : true
					}]
				},
				legend : {
					display : true,
					labels : {
						fontColor : 'red'
					}
				},
			}
		});
	});
	// ]]>
	</script>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
	<div style="width:400px; height:400px">
		<canvas id="myChart" width="400" height="400"></canvas>
	</div>
</div><!-- ./wrapper -->
</body>
</html>