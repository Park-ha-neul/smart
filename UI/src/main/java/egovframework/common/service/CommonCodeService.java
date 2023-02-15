package egovframework.common.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : CommonCodeService.java
 * 3. Package  : egovframework.common.service
 * 4. Comment  : 코드관리
 * 5. 작성자   : ntsys
 * 6. 작성일   : 2013. 12. 3. 오전 9:49:09
 * </PRE>
 */
public interface CommonCodeService {

	/**
	 * <PRE>
	 * 1. MethodName : codeSelectList
	 * 2. ClassName  : CommonCodeService
	 * 3. Comment   : 코드리스트 조회
	 * 4. 작성자    : ntsys
	 * 5. 작성일    : 2013. 12. 3. 오전 9:55:07
	 * </PRE>
	 *   @return List
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	List selectCodeList(DataMap dataMap) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectGroupCodeList
	 * 2. ClassName  : CommonCodeService
	 * 3. Comment   : 그룹코드 리스트 조회
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 17. 오전 9:57:31
	 * </PRE>
	 *   @return List
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	List selectGroupCodeList(DataMap param) throws Exception;

	DataMap selectCode(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertErrorLog
	 * 2. ClassName  : CommonCodeServiceImpl
	 * 3. Comment   : 에러 로그 삽입
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 5. 오전 11:02:22
	 * </PRE>
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	void insertErrorLog (DataMap dataMap) throws Exception;

}
