package egovframework.admin.bbsmng.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BbsMngService.java
 * 3. Package  : egovframework.admin.bbsmng.service
 * 4. Comment  :
 * 5. 작성자   : mk
 * 6. 작성일   : 2018. 7. 13. 오후 5:14:45
 * </PRE>
 */
public interface BbsMngService {

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntBbsMng
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 게시판 관리 총 개수
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 5:14:48
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntBbsMng(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListBbsMng
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 게시판 관리 목록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 5:15:00
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListBbsMng(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMng
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 게시판 관리 상세
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 5:15:09
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectBbsMng(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertBbsMng
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 게시판 관리 등록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 5:15:14
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void insertBbsMng(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateBbsMng
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 수정
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 5:15:19
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateBbsMng(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteBbsMng
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 게시판 관리 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 5:15:24
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteBbsMng(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : selectExistYnBbsCode
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 게시판 코드 중복 체크
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 16. 오전 10:08:46
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectExistYnBbsCode(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntBbsCode
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 게시판 코드에 맞는 게시물 리스트 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오후 5:26:11
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntBbsCode(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsCodeSeq
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 게시판코드 seq조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 26. 오전 10:04:16
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectBbsCodeSeq(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListMenuPop
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 메뉴 리스트 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 24. 오후 3:54:48
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListMenuPop(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateMenuBbsCode
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 메뉴의 bbs_code, cntnts_id, url_ty_code 수정
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 24. 오후 3:54:40
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateMenuBbsCode(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMngInfo
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 게시판 상세 정보 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 24. 오후 3:54:33
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectBbsMngInfo(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectCntntsInfo
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 컨텐츠 상세 정보 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 24. 오후 3:54:26
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectCntntsInfo(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteMenuBbsCode
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 메뉴의 bbs_code, cntnts_id, url_ty_code 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 24. 오후 3:54:22
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteMenuBbsCode(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListAuthor
	 * 2. ClassName  : BbsMngService
	 * 3. Comment   : 게시판 권한 리스트 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 15. 오후 7:27:16
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 */
	List selectListAuthor(DataMap param) throws Exception;
}
