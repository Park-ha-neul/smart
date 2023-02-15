package egovframework.admin.author.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;


/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : AuthorService.java
 * 3. Package  : egovframework.admin.author.web.service
 * 4. Comment  : 권한관리
 * 5. 작성자   : 김영배
 * 6. 작성일   : 2015. 9. 1. 오후 3:42:10
 * </PRE>
 */
public interface AuthorMgtService {


	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntAuthor
	 * 2. ClassName  : AuthorService
	 * 3. Comment   : 권한 카운트
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:42:20
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntAuthor(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : selectPageListAuthor
	 * 2. ClassName  : AuthorService
	 * 3. Comment   : 권한 목록
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:42:27
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListAuthor(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : insertAuthor
	 * 2. ClassName  : AuthorService
	 * 3. Comment   : 권한 추가
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:42:35
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void insertAuthor(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : selectAuthor
	 * 2. ClassName  : AuthorService
	 * 3. Comment   : 권한 상세
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:42:46
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectAuthor(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : updateAuthor
	 * 2. ClassName  : AuthorService
	 * 3. Comment   : 권한 수정
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:42:52
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateAuthor(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : deleteAuthor
	 * 2. ClassName  : AuthorService
	 * 3. Comment   : 권한 삭제
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:43:01
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteAuthor(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : selectExistYnAuthor
	 * 2. ClassName  : AuthorService
	 * 3. Comment   : 권한 중복 체크
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:43:09
	 * </PRE>
	 *   @return String
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	String selectExistYnAuthor(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntUser
	 * 2. ClassName  : AuthorMgtService
	 * 3. Comment   : 권한 사용자 총 카운트
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 10. 오전 11:1:23
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 */
	int selectTotCntUser(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListUser
	 * 2. ClassName  : AuthorMgtService
	 * 3. Comment   : 권한 사용자 조회
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 10. 오전 11:1:28
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 */
	List selectPageListUser(DataMap param) throws Exception;
}
