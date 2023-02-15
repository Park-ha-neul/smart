package egovframework.common.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : IncludeService.java
 * 3. Package  : egovframework.common.service
 * 4. Comment  : 
 * 5. 작성자   : ntsys
 * 6. 작성일   : 2018. 2. 26. 오전 11:14:26
 * 7. 변경이력 : 
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    ntsys : 2018. 2. 26. :            : 신규 개발.
 * </PRE>
 */ 
/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : IncludeService.java
 * 3. Package  : egovframework.common.service
 * 4. Comment  : 
 * 5. 작성자   : ntsys
 * 6. 작성일   : 2018. 2. 26. 오전 11:14:32
 * 7. 변경이력 : 
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    ntsys : 2018. 2. 26. :            : 신규 개발.
 * </PRE>
 */ 
public interface IncludeService {
	
	/**
	 * <PRE>
	 * 1. MethodName : selectTopMenuList
	 * 2. ClassName  : IncludeService
	 * 3. Comment   : 헤더의 상단 메뉴 리스트 조회 
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 7. 오전 11:07:42
	 * </PRE>
	 *   @return List
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	List selectTopMenuList(DataMap dataMap) throws Exception;
	
	/**
	 * <PRE>
	 * 1. MethodName : selectLeftMenuList
	 * 2. ClassName  : IncludeService
	 * 3. Comment   : 좌측 메뉴 리스트 조회 
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 7. 오전 11:08:05
	 * </PRE>
	 *   @return List
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	List selectLeftMenuList(DataMap dataMap) throws Exception;
	
	/**
	 * <PRE>
	 * 1. MethodName : selectMenuByUrl
	 * 2. ClassName  : IncludeService
	 * 3. Comment   : URL정보로 메뉴정보 조회
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 7. 오후 5:21:01
	 * </PRE>
	 *   @return DataMap
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectMenuByUrl(DataMap dataMap) throws Exception;
}