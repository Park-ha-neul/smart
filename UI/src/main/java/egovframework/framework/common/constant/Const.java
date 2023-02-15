/**
 *
 *
 * 1. FileName : Const.java
 * 2. Package : egovframework.framework.common.constant
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 26. 오전 11:18:30
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 26. :            : 신규 개발.
 */

package egovframework.framework.common.constant;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : Const.java
 * 3. Package  : egovframework.framework.common.constant
 * 4. Comment  : constant
 * 5. 작성자   : kimkm
 * 6. 작성일   : 2020. 4. 21. 오후 5:19:53
 * </PRE>
 */
public class Const {

	//페이징처리시 기본설정
	public static final String DEF_FIRST_PAGE 				= "1"; 			// 디폴트 첫번째 페이지
	public static final String DEF_ROW_PER_PAGE 			= "10"; 		// 디폴트 한화면에 보여줘야할 컨텐츠 수
	public static final String DEF_ROW_PER_PAGE_05 			= "5"; 			// 디폴트 한화면에 보여줘야할 컨텐츠 수
	public static final String DEF_ROW_PER_PAGE_20 			= "20"; 		// 디폴트 한화면에 보여줘야할 컨텐츠 수
	public static final String DEF_CURRENT_PAGE 			= "1"; 			// 디폴트 현재페이지
	public static final String DEF_NAVI_COUNT 				= "10"; 		// 디폴트 페이지 갯수
	public static final String DEF_NAVI_COUNT_05 			= "5"; 			// 디폴트 페이지 갯수

	//TB_MENU
	public static final String MENU_ID_CMS 					= "10001";		// CMS MENU ID
	public static final String MENU_ID_TOP 					= "100010001100";	// 홈페이지 관리

	//TB_CODE 그룹코드 리스트
	public static final String UPCODE_USE_YN 				= "R000010";	// 사용 여부 코드
	public static final String UPCODE_DEL_YN 				= "R000020";	// 삭제 여부 코드
	public static final String UPCODE_SYS_CODE 				= "R000030";	// 시스템 코드
	public static final String UPCODE_ROW_PER_PAGE			= "R000040";	// 페이지 당 목록 수
	public static final String UPCODE_FACIL_CODE			= "R000050";	// 시설 코드
	public static final String UPCODE_COORD_CODE			= "R000060";	// 좌표 코드
	public static final String UPCODE_MENU_TYPE_CODE 		= "R000070";	// 메뉴유형코드
	public static final String UPCODE_MENU_SE_CODE			= "R000080";	// 메뉴구분코드
	public static final String UPCODE_CTTPC_SE 				= "R000090";	// 연락처구분코드
	public static final String UPCODE_USER_STTUS 			= "R000100";	// 사용자상태코드
	public static final String UPCODE_USER_SE 				= "R000110";	// 사용자구분코드
	public static final String UPCODE_BBS_TY 				= "R000120";	// 게시판구분코드
	public static final String UPCODE_BBS_CNTNTS_POSITION 	= "R000130"; 	// 일반게시판 컨텐츠 위치 코드
	public static final String UPCODE_POPUP_SE 				= "R000140";	// 팝업구분코드
	public static final String UPCODE_BANNER_ZONE 			= "R000150";	// 배너 구역 코드
	public static final String UPCODE_QUICK_ZONE 			= "R000160";	// 퀵메뉴 구역 코드

	//메뉴 구분 코드
	public static final String MENU_SE_CODE_CNTS_ID 		= "10";		// 콘텐츠 ID
	public static final String MENU_SE_CODE_BBS				= "20";		// 게시판 코드
	public static final String MENU_SE_CODE_USER_INSERT 	= "30";		// 직접입력

	//어드민 상태코드
	public static final String USER_STTUS_CODE_TEMP_JOIN 	= "0";		// 임시가입
	public static final String USER_STTUS_CODE_ACTIVE 		= "10";		// 활동
	public static final String USER_STTUS_CODE_OUT 			= "20";		// 탈퇴
	public static final String USER_STTUS_CODE_STOP 		= "99";		// 정지

	public static final String CODE_AUTHOR_ADMIN 			= "ADMIN";	// 권한코드 - 관리자

	public static final String PROP_TYPE_LO 				= "LO"; 	//ph logD
	public static final String PROP_TYPE_MS 				= "MS"; 	// ph Mass_Solubility

}
