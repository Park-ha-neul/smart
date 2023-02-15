package egovframework.admin.common.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : LoginService.java
 * 3. Package  : egovframework.admin.common.service
 * 4. Comment  : 관리자 로그인
 * 5. 작성자   : kimkm
 * 6. 작성일   : 2020. 4. 1. 오후 5:15:52
 * </PRE>
 */
public interface LoginService {
	
	/**
	 * <PRE>
	 * 1. MethodName : registerCheckUserId
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 사용자 아이디 중복 체크
	 * 4. 작성자    : 허웅
	 * 5. 작성일    : 2022. 4. 21
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	
	int registerCheckUserId(DataMap param) throws Exception;
	
	/**
	 * <PRE>
	 * 1. MethodName : registerUser
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 회원가입 권한 생성
	 * 4. 작성자    : 허웅
	 * 5. 작성일    : 2022. 4. 15
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	void registerUserAuthor(DataMap param) throws Exception;
	
	/**
	 * <PRE>
	 * 1. MethodName : checkIfUserExists
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 회원가입
	 * 4. 작성자    : 허웅
	 * 5. 작성일    : 2022. 4. 15
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	void registerUser(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : checkIfUserExists
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 사용자 존재 여부 체크
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 1. 오후 5:16:54
	 * </PRE>
	 *   @return String
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	String checkIfUserExists(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectUserInfo
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 사용자 정보 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 1. 오후 5:17:45
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectUserInfo(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateLoginFailCount
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 로그인실패 카운트 갱신
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 1. 오후 5:16:40
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int updateLoginFailCount(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertAdminEventLog
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 관리자 로그 삽입
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 1. 오후 5:19:01
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void insertAdminEventLog(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertAdminEventLog
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 에러 로그 삽입
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 5. 오전 11:23:02
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void insertErrorEventLog(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListUserAuthId
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 권한 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 7. 오후 5:44:20
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListUserAuthId(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : initlUserPasswordCheck
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 비밀번호 초기화 여부 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 7. 오후 5:01:10
	 * </PRE>
	 *   @return String
	 *   @param param
	 *   @return
	 */
	DataMap initlUserPasswordCheck(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectAccessYn
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 접근 ip 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 12. 오후 4:22:46
	 * </PRE>
	 *   @return boolean
	 *   @param dataParam
	 *   @return
	 */
	boolean selectAccessYn(DataMap dataParam) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectAuthorMenuByUrl
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 권한에 따른 초기 url
	 * 4. 작성자    : KALEL
	 * 5. 작성일    : 2021. 3. 24. 오전 11:06:19
	 * </PRE>
	 *   @return String
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	String selectAuthorMenuByUrl(DataMap param) throws Exception;
}
