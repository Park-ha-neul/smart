/**
 *
 * 1. FileName : SubMenuService.java
 * 2. Package : egovframework.common.service
 * 3. Comment : 
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 23. 오전 10:05:03
 * 6. 변경이력 : 
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 23. :            : 신규 개발.
 */

package egovframework.common.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface SubMenuService {

	/**
	 * <PRE>
	 * 1. MethodName : menuSelectList
	 * 2. ClassName  : SubMenuService
	 * 3. Comment   : 메뉴 리스트 조회
	 * 4. 작성자    : jjh
	 * 5. 작성일    : 2014. 1. 6. 오전 9:22:48
	 * </PRE>
	 *   @return List
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	List menuSelectList(DataMap dataMap) throws Exception;
	
	/**
	 * <PRE>
	 * 1. MethodName : menuTreeSelectList
	 * 2. ClassName  : SubMenuService
	 * 3. Comment   : 하위메뉴 리스트 조회
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 10. 1. 오후 6:04:50
	 * </PRE>
	 *   @return List
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	List menuTreeSelectList(DataMap dataMap) throws Exception;
	
	/**
	 * <PRE>
	 * 1. MethodName : menuSelect
	 * 2. ClassName  : SubMenuService
	 * 3. Comment   : 메뉴 조회
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 9. 4. 오후 1:58:56
	 * </PRE>
	 *   @return DataMap
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	DataMap menuSelect(DataMap dataMap) throws Exception;
	
	/**
	 * <PRE>
	 * 1. MethodName : menuUrlSelect
	 * 2. ClassName  : SubMenuService
	 * 3. Comment   : 메뉴조회(URL)
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 10. 2. 오후 4:20:53
	 * </PRE>
	 *   @return DataMap
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	DataMap menuUrlSelect(DataMap dataMap)  throws Exception;
}
