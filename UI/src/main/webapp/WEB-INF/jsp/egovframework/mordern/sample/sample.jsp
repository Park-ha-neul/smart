<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<body>
<div class="app-content">
	<div class="app-title">
		<div>
			<h1><i class="fas fa-angle-double-right"></i> 게시물 관리</h1>
		</div>
	</div>
	<div class="tile">
		<form role="form" id="aform" method="post" action="/admin/bbs/selectPageListBbsMgt.do">
			<input type="hidden" name="bbs_seq" />
			<div class="tile-title">
			</div>
			<div class="tile-body">
				<div class="table-responsive">
				</div>
			</div>
			<div class="tile-footer">
				<div class="form-row justify-content-end">
					<div class="form-inline">
						<button type="button" class="btn btn-outline-secondary mr-2" onclick="fnList(); return false;"><i class="fa fa-reply"></i> 목록</button>
						<button type="button" class="btn btn-outline-info" onclick="fnInsert(); return false;"><i class="fa fa-pencil"></i> 확인</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<%@ include file="/WEB-INF/jsp/egovframework/mordern/config/msg.jsp" %>
</body>