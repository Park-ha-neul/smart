package egovframework.admin.code.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : CodeMgtService.java
 * 3. Package  : egovframework.admin.code.service
 * 4. Comment  : 
 * 5. 작성자   : JJH
 * 6. 작성일   : 2015. 12. 7. 오후 4:25:56
 * 7. 변경이력 : 
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    JJH : 2015. 12. 7. :            : 신규 개발.
 * </PRE>
 */ 
public interface CodeMgtService {
	

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntGroupCode
	 * 2. ClassName  : CodeService
	 * 3. Comment   : 그룹코드 카운트
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:46:42
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntGroupCode(DataMap param) throws Exception;
	

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListGroupCode
	 * 2. ClassName  : CodeService
	 * 3. Comment   : 그룹코드 조회
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:46:50
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListGroupCode(DataMap param) throws Exception;

	
	/**
	 * <PRE>
	 * 1. MethodName : selectListCode
	 * 2. ClassName  : CodeService
	 * 3. Comment   : 상세코드 리스트 
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:47:00
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListCode(DataMap param) throws Exception;
	

	/**
	 * <PRE>
	 * 1. MethodName : selectGroupCode
	 * 2. ClassName  : CodeService
	 * 3. Comment   : 그룹코드 상세 
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:47:11
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectGroupCode(DataMap param) throws Exception;
	

	/**
	 * <PRE>
	 * 1. MethodName : deleteGroupCode
	 * 2. ClassName  : CodeService
	 * 3. Comment   : 그룹코드 및 상세코드 삭제
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:47:19
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteGroupCode(DataMap param) throws Exception;

	
	/**
	 * <PRE>
	 * 1. MethodName : selectGroupCodeExistYn
	 * 2. ClassName  : CodeService
	 * 3. Comment   : 그룹코드 중복 체크
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:47:32
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectExistYnGroupCode(DataMap param) throws Exception;

	
	/**
	 * <PRE>
	 * 1. MethodName : insertGroupCode
	 * 2. ClassName  : CodeService
	 * 3. Comment   : 그룹코드 추가 
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:47:50
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void insertGroupCode(DataMap param) throws Exception;
	

	/**
	 * <PRE>
	 * 1. MethodName : updateCode
	 * 2. ClassName  : CodeService
	 * 3. Comment   : 그룹코드 및 상세코드 수정
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:47:58
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateCode(DataMap param) throws Exception;
	
	/**
	 * <PRE>
	 * 1. MethodName : selectMaxGroupId
	 * 2. ClassName  : CodeService
	 * 3. Comment   : 그룹아이디 중 최대값 -> 코드 등록할 때 자동 채번
	 * 4. 작성자    : 김유리
	 * 5. 작성일    : 2015. 9. 1. 오후 3:47:58
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	*/
	DataMap selectMaxGroupId(DataMap param) throws Exception;
	
	/**
	 * <PRE>
	 * 1. MethodName : selectExistYnGroupId
	 * 2. ClassName  : CodeMgtService
	 * 3. Comment   : 그룹ID 중복체크
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 27. 오전 10:05:07
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectExistYnGroupId(DataMap param) throws Exception;
}
