package egovframework.admin.bbs.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BbsMgtService.java
 * 3. Package  : egovframework.admin.bbs.service
 * 4. Comment  :
 * 5. 작성자   : mk
 * 6. 작성일   : 2018. 7. 23. 오전 11:41:55
 * </PRE>
 */
public interface BbsMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntBbsMgt
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 총 개수
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:41:58
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntBbsMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListBbsMgt
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 목록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:42:11
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListBbsMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMgt
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 상세
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:42:17
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectBbsMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertBbsMgt
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 등록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:42:23
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param imgFileList
	 *   @param thumbFileList
	 *   @param fileList
	 *   @throws Exception
	 */
	void insertBbsMgt(DataMap param, List imgFileList, List thumbFileList, List fileList) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateBbsMgt
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 수정
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:42:28
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param imgFileList
	 *   @param thumbFileList
	 *   @param fileList
	 *   @throws Exception
	 */
	void updateBbsMgt(DataMap param, List imgFileList, List thumbFileList, List fileList) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteBbsMgt
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:42:34
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteBbsMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListBbsCode
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 게시판 코드 목록 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:55:28
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListBbsCode(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMngInfo
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 게시판 코드 정보 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:55:55
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectBbsMngInfo(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntBbsAns
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 답변 총 개수
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 11. 7. 오후 7:12:28
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntBbsAns(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListBbsAns
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 답변 목록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:56:05
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListBbsAns(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsAns
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시판 관리 답변 상세
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 11. 7. 오후 7:15:11
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectBbsAns(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertBbsAnswer
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 답변 등록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:56:18
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param imgFileList
	 *   @param fileList
	 *   @throws Exception
	 */
	void insertBbsAnswer(DataMap param, List imgFileList, List fileList) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateBbsAnswer
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 답변 수정
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:56:24
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param imgFileList
	 *   @param fileList
	 *   @throws Exception
	 */
	void updateBbsAnswer(DataMap param, List imgFileList, List fileList) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteBbsAnswer
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 답변 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:56:31
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteBbsAnswer(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteAllBbsAnswer
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 답변 전체 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 11. 7. 오후 8:54:29
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exceptio
	 */
	void deleteAllBbsAnswer(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateChnageOrganBbs
	 * 2. ClassName  : BbsMgtService
	 * 3. Comment   : 게시물 관리 기관 및 게시판 변경
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 9. 3. 오후 7:25:55
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateChnageOrganBbs(DataMap param) throws Exception;

	String selectBbsAuthorYn(DataMap param) throws Exception;

	String selectCheckTabSeCodeYn(DataMap param) throws Exception;
}
