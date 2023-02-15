<%@ page contentType="text/html;charset=euc-kr" %>
<%@ page language="java" import="Kisinfo.Check.IPIN2Client" %>

<%
	/********************************************************************************************************************************************
		NICE평가정보 Copyright(c) KOREA INFOMATION SERVICE INC. ALL RIGHTS RESERVED

		서비스명 : IPIN 가상주민번호 서비스
		페이지명 : IPIN 가상주민번호 서비스 호출 페이지
	*********************************************************************************************************************************************/

	String sSiteCode	= "FQ00";		//  NICE평가정보에서 발급한 IPIN 서비스 사이트코드
	String sSitePw		= "seoullabor0308!";		//  NICE평가정보에서 발급한 IPIN 서비스 사이트패스워드


	/*
	┌ sReturnURL 변수에 대한 설명  ─────────────────────────────────────────────────────
		암호화된 인증결과 데이터를 리턴받을 URL을 프로토콜부터 풀주소로 정의해주세요.

		* URL은 반드시 프로토콜부터 입력해야 하며 외부에서 접속이 가능한 주소여야 합니다.
		* 당사 샘플페이지 중 ipin_process 페이지가 인증결과 데이터를 리턴받는 페이지입니다.

		예 - http://www.test.co.kr/ipin_process.jsp
			 https://www.test.kr:4343/ipin_process.jsp
	└────────────────────────────────────────────────────────────────────
	*/
	String sReturnURL   = "http://localhost:8070/admin/userCrtfc/ipin_process.jsp";


	/*
	┌ sCPRequest 변수에 대한 설명  ─────────────────────────────────────────────────────
		CP요청번호는 추가적인 보안처리를 위한 변수입니다. 인증 후 인증결과 데이터와 함께 전달됩니다.
		세션에 저장된 값과 비교해 데이터 위변조를 검사하거나, 사용자 특정용으로 이용할 수 있습니다.
		위변조 검사는 인증에 필수적인 처리는 아니며 보안을 위한 권고 사항입니다.

		+ CP요청번호 설정 방법
			1. 당사에서 배포된 모듈로 생성
			2. 귀사에서 임의로 정의(최대 30byte)
	└────────────────────────────────────────────────────────────────────
	*/
	String sCPRequest = "";


	// 모듈객체 생성
	IPIN2Client pClient = new IPIN2Client();

	// CP요청번호 생성
	sCPRequest = pClient.getRequestNO(sSiteCode);

	// CP요청번호 세션에 저장
	// : 저장된 값은 ipin_result 페이지에서 데이터 위변조 검사에 이용됩니다.
	session.setAttribute("CPREQUEST" , sCPRequest);


	// 인증요청 암호화 데이터 생성
	int iRtn = pClient.fnRequest(sSiteCode, sSitePw, sCPRequest, sReturnURL);

	String sEncData		= "";	// 인증요청 암호화 데이터
	String sRtnMsg		= "";	// 처리결과 메세지

	// 암호화 처리결과코드에 따른 처리
	if (iRtn == 0)
	{
        // 인증요청 암호화 데이터 추출
		sEncData = pClient.getCipherData();
		sRtnMsg = "정상 처리되었습니다.";
	}
	else if (iRtn == -1)
	{
		sRtnMsg = "암호화 시스템 오류 : 귀사 서버 환경에 맞는 모듈을 이용해주십시오.<br>오류가 지속되는 경우 iRtn 값, 서버 환경정보, 사이트코드를 기재해 문의주시기 바랍니다.";
	}
	else if (iRtn == -2)
	{
		sRtnMsg = "암호화 처리 오류 : 최신 모듈을 이용해주십시오. 오류가 지속되는 경우 iRtn 값, 서버 환경정보, 사이트코드를 기재해 문의주시기 바랍니다.";
	}
	else if (iRtn == -9)
	{
		sRtnMsg = "입력 정보 오류 : 암호화 함수에 입력된 파라미터 값을 확인해주십시오.<br>오류가 지속되는 경우, 함수 실행 직전 각 파라미터 값을 로그로 출력해 발송해주시기 바랍니다.";
	}
	else
	{
		sRtnMsg = "기타 오류: iRtn 값과 적용한 샘플소스를 발송해주시기 바랍니다.";
	}

%>

<html>
<head>
	<meta charset="EUC-KR">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>NICE평가정보 가상주민번호 서비스</title>
	<script>
		window.name ="Parent_window";

		function fnPopup(){
			window.open('', 'popupIPIN2', 'width=450, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
			document.form_ipin.target = "popupIPIN2";
			document.form_ipin.action = "https://cert.vno.co.kr/ipin.cb";
			document.form_ipin.submit();
		}
	</script>
</head>
<body>
	iRtn : <%= iRtn %> - <%= sRtnMsg %><br><br>
	인증요청 암호화 데이터 : [<%= sEncData %>]<br><br>


	<!-- 아이핀 가상주민번호 서비스 팝업 호출 form -->
	<form name="form_ipin" method="post">
		<!-- 요청모드 (필수 데이터) -->
		<input type="hidden" name="m" value="pubmain">
		<!-- 인증요청 암호화 데이터 -->
		<input type="hidden" name="enc_data" value="<%= sEncData %>">
			<!-- 팝업 호출 이미지 버튼 -->
			<a href="javascript:fnPopup();">
			<img src="http://image.creditbank.co.kr/static/img/vno/new_img/bt_17.gif" width=218 height=40 border=0>
		</a>
	</form>


	<!-- 아이핀 가상주민번호 서비스 팝업 인증결과 전달 form -->
	<form name="vnoform" method="post">
		<!-- 인증결과 암호화 데이터 -->
		<input type="hidden" name="enc_data">
	</form>
</body>
</html>