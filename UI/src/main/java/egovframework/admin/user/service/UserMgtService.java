package egovframework.admin.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : UserService.java
 * 3. Package  : egovframework.admin.user.web.service
 * 4. Comment  : 사용자 관리
 * 5. 작성자   : 함경호
 * 6. 작성일   : 2015. 9. 1. 오후 3:34:36
 * </PRE>
 */
public interface UserMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntUser
	 * 2. ClassName  : UserService
	 * 3. Comment   : 사용자 총 개수 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:36
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntUser(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListUser
	 * 2. ClassName  : UserService
	 * 3. Comment   : 사용자 페이지 리스트 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:35
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListUser(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertUser
	 * 2. ClassName  : UserService
	 * 3. Comment   : 사용자 등록
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:34
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void insertUser(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectUser
	 * 2. ClassName  : UserService
	 * 3. Comment   : 사용자 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:33
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectUser(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateUser
	 * 2. ClassName  : UserService
	 * 3. Comment   : 사용자 수정
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:33
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateUser(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateUserInfo
	 * 2. ClassName  : UserMgtService
	 * 3. Comment   : 사용자 정보만 변경
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2016. 6. 3. 오후 3:05:47
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateUserInfo(HttpServletRequest request, HttpServletResponse response, DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteUser
	 * 2. ClassName  : UserService
	 * 3. Comment   : 사용자 삭제
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:32
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteUser(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectIdExistYn
	 * 2. ClassName  : UserService
	 * 3. Comment   : 사용자 아이디 중복 검사
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:32
	 * </PRE>
	 *   @return String
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	String selectIdExistYn(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectAuthorList
	 * 2. ClassName  : UserService
	 * 3. Comment   : 권한 리스트 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 5:54:26
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListAuthor(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListAuthorUser
	 * 2. ClassName  : UserService
	 * 3. Comment   : 사용자 권한리스트 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 2. 오전 9:21:26
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListAuthorUser(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListAllUser
	 * 2. ClassName  : UserService
	 * 3. Comment   : 사용자 리스트 전체 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 11. 3. 오전 9:33:47
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListAllUser(DataMap param) throws Exception;
	/**
	 * <PRE>
	 * 1. MethodName : initlUserPassword
	 * 2. ClassName  : UserMgtService
	 * 3. Comment   : 비밀번호 초기화
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 7. 오후 3:09:24
	 * </PRE>
	 *   @return void
	 *   @param param
	 */
	void initlUserPassword(DataMap param) throws Exception;


}
