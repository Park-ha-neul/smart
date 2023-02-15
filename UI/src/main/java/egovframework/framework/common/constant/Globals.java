package egovframework.framework.common.constant;

import egovframework.framework.common.util.EgovPropertiesUtil;

/**
 *  Class Name : Globals.java
 *  Description : 시스템 구동 시 프로퍼티를 통해 사용될 전역변수를 정의한다.
 *  Modification Information
 *
 *     수정일         수정자                   수정내용
 *   -------    --------    ---------------------------
 *   2009.01.19    박지욱          최초 생성
 *
 *  @author 공통 서비스 개발팀 박지욱
 *  @since 2009. 01. 19
 *  @version 1.0
 *  @see
 *
 */

public class Globals {
	//OS 유형
    public static final String OS_TYPE = EgovPropertiesUtil.getProperty("Globals.OsType");
    //ShellFile 경로
    public static final String SHELL_FILE_PATH = EgovPropertiesUtil.getPathProperty("Globals.ShellFilePath");
    //퍼로퍼티 파일 위치
    public static final String CONF_PATH = EgovPropertiesUtil.getPathProperty("Globals.ConfPath");
    //파일포맷 정보 프로퍼티 위치
    public static final String FILE_FORMAT_PATH = EgovPropertiesUtil.getPathProperty("Globals.FileFormatPath");
    //Client정보 프로퍼티 위치
    public static final String CLIENT_CONF_PATH = EgovPropertiesUtil.getPathProperty("Globals.ClientConfPath");
    //Server정보 프로퍼티 위치
    public static final String SERVER_CONF_PATH = EgovPropertiesUtil.getPathProperty("Globals.ServerConfPath");

    //파일 업로드 원 파일명
	public static final String ORIGIN_FILE_NM = "originalFileName";
	//파일 확장자
	public static final String FILE_EXT = "fileExtension";
	//파일크기
	public static final String FILE_SIZE = "fileSize";
	//업로드된 파일명
	public static final String UPLOAD_FILE_NM = "uploadFileName";
	//파일경로
	public static final String FILE_PATH = "filePath";
	//파일저장시 확장자
	public static final String FILE_EXT_C = ".file";

	//메일발송요청 XML파일경로
	public static final String MAIL_REQUEST_PATH = EgovPropertiesUtil.getPathProperty("Globals.MailRequestPath");
	//메일발송응답 XML파일경로
	public static final String MAIL_RESPONSE_PATH = EgovPropertiesUtil.getPathProperty("Globals.MailRResponsePath");

	// G4C 연결용 IP (localhost)
	public static final String LOCAL_IP = EgovPropertiesUtil.getProperty("Globals.LocalIp");

	/*
	 * 추가 정보
	 */
	public static final String JS_ROOT = EgovPropertiesUtil.getProperty("path.js");
	public static final String IMG_ROOT = EgovPropertiesUtil.getProperty("path.img");
	public static final String CSS_ROOT = EgovPropertiesUtil.getProperty("path.css");

	//URL 정보
	public static final String DO_HOME = EgovPropertiesUtil.getProperty("do.home");
	public static final String DO_LOGIN = EgovPropertiesUtil.getProperty("do.login");

	//JSP 정보
	public static final String JSP_HOME = EgovPropertiesUtil.getProperty("jsp.home");
	public static final String JSP_LOGIN = EgovPropertiesUtil.getProperty("jsp.login");
	public static final String JSP_CLOSE = EgovPropertiesUtil.getProperty("jsp.close");

	// 파일 용량 정보
	public static final String FILE_MAX_SIZE = EgovPropertiesUtil.getProperty("Globals.fileMaxSize");
	public static final String IMG_FILE_MAX_SIZE = EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize");

	// 적정 이미지 해상도
	public static final String IMG_MAX_SIZE = EgovPropertiesUtil.getProperty("Globals.imgMaxSize");
	// Server URL
	public static final String SITE_NAME = EgovPropertiesUtil.getProperty("site.name");
	public static final String SITE_DOMAIN_CMS = EgovPropertiesUtil.getProperty("site.domain.cms");		//CMS
	// Naver Map v3.0 API 를 사용하기 위한 clientID값
	public static final String NAVERMAP_CLIENT_ID = EgovPropertiesUtil.getProperty("Globals.NaverMapClientId");
	// env
	public static final String SYSTEM_ENV = EgovPropertiesUtil.getProperty("Globals.env");

	//게시판, 컨텐츠 default url
	public static final String SELECT_PAGE_LIST_BBS_URL = EgovPropertiesUtil.getProperty("url.pagaListBbs");
	public static final String SELECT_CNTNTS_URL = EgovPropertiesUtil.getProperty("url.selectCntnts");

	// 접근 IP 제한 여부
	public static final String ACCESS_IP_YN = EgovPropertiesUtil.getProperty("access.ip.yn");
	// interceptor log 출력 여부
	public static final String INTERCEPTOR_LOG_PRINT_YN = EgovPropertiesUtil.getProperty("interceptor.log.print.yn");
	// task 동작 여부
	public static final String TASK_OPERATION_YN = EgovPropertiesUtil.getProperty("task.operation.yn");
	// 중복로그인 여부
	public static final String DUPLLOGIN = EgovPropertiesUtil.getProperty("Globals.duplLogin");
	// 기본 테마 설정; json string 형식
	public static final String DEFAULT_THEME = EgovPropertiesUtil.getProperty("Globals.default.theme");

	// 기본 리턴 method
	public static final String METHOD_GET = "get";
	public static final String METHOD_POST = "post";

	public static final String PROTOCOL = EgovPropertiesUtil.getProperty("Globals.protocol");
	public static final String DOMAIN_CENTER = EgovPropertiesUtil.getProperty("Globals.domain.center");

	//개발 사이트
	public static final String DOMAIN_ADMIN = EgovPropertiesUtil.getProperty("domain.dev.admin");
}
