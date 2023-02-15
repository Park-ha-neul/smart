<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/common.jsp" %>
<html>
<head>
	<title><%=headTitle%></title>

	<script type="text/javascript">
	//<![CDATA[
	$(function(){
		$('#sub').click(function(e){
			e.preventDefault();

			var pro = $('.progress');
			var probar = $('.progress-bar');
			$('#aform').ajaxSubmit({
				url : '/sample/testUploadAjax.do',
				beforeSend: function() {
					var percentVal = '0%';
					probar.css('width', '0%');
					probar.attr('aria-valuenow', '0');
					pro.addClass('active');
					probar.text('0%');
					probar.removeClass('progress-bar-success');
				},
				uploadProgress: function(event, position, total, percentComplete) {
					console.log(event.loaded);
					console.log('position : ' + position);
					console.log('total : ' + total);
					console.log('percentComplete : ' + percentComplete);

					var percentVal = percentComplete + '%';
					probar.css('width', percentVal);
					probar.attr('aria-valuenow', percentComplete);
					probar.text(percentVal);

					if(percentComplete == 100){
						pro.removeClass('active');
						probar.text('complete');
						probar.addClass('progress-bar-success');
					}
				},
			});
		});
	});
	// ]]>
	</script>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
	<br/>
	<form id="aform" method="post" enctype="multipart/form-data">
		<input type="file" name="upload" />
		<input type="file" name="upload2" />
	</form>
	<input type="button" id="sub" value="전송" />
	<br/>
	<div class="progress">
		<div class="progress-bar progress-bar-striped" role="progressbar" style="width: 0%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
	</div>
</div><!-- ./wrapper -->
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/msg.jsp" %>
</body>
</html>