
package egovframework.admin.menu.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : MenuMgtService.java
 * 3. Package  : egovframework.admin.menu.service
 * 4. Comment  :
 * 5. 작성자   : JJH
 * 6. 작성일   : 2015. 12. 7. 오전 10:54:52
 * 7. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    JJH : 2015. 12. 7. :            : 신규 개발.
 * </PRE>
 */
public interface MenuMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntMenu
	 * 2. ClassName  : MenuService
	 * 3. Comment   : 메뉴 총 갯수
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntMenu(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListMenu
	 * 2. ClassName  : MenuService
	 * 3. Comment   : 메뉴 리스트 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListMenu(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertMenu
	 * 2. ClassName  : MenuService
	 * 3. Comment   : 메뉴 등록
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void insertMenu(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectMenu
	 * 2. ClassName  : MenuService
	 * 3. Comment   : 메뉴 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectMenu(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectMenuExistYn
	 * 2. ClassName  : MenuService
	 * 3. Comment   : 동일 메뉴 ID 존재 여부 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectExistYnMenu(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateMenu
	 * 2. ClassName  : MenuService
	 * 3. Comment   : 메뉴 수정
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateMenu(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateMenuSub
	 * 2. ClassName  : MenuService
	 * 3. Comment   : 메뉴 하위 메뉴 sort 수정
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateMenuSub(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteMenu
	 * 2. ClassName  : MenuService
	 * 3. Comment   : 메뉴 삭제
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteMenu(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectSubMenuExistYn
	 * 2. ClassName  : MenuService
	 * 3. Comment   : 하위메뉴 존재 여부 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectExistYnSubMenu(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectSubMenuExistSort
	 * 2. ClassName  : MenuService
	 * 3. Comment   : 선택 메뉴 정렬 순위 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectExistSortSubMenu(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsInfo
	 * 2. ClassName  : MenuMgtService
	 * 3. Comment   : 게시판 코드정보 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return DataMap
	 *   @param tmpParam
	 *   @return
	 */
	DataMap selectBbsInfo(DataMap tmpParam) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectCntntsInfo
	 * 2. ClassName  : MenuMgtService
	 * 3. Comment   : 컨텐츠 정보 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
	 * </PRE>
	 *   @return DataMap
	 *   @param tmpParam
	 *   @return
	 */
	DataMap selectCntntsInfo(DataMap tmpParam) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectCodeAttrbs
	 * 2. ClassName  : MenuMgtService
	 * 3. Comment   : attr 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 17. 오후 4:58:38
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 */
	DataMap selectCodeAttrbs(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListParentMenuId
	 * 2. ClassName  : MenuMgtService
	 * 3. Comment   : 부모 메뉴 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 17. 오후 4:59:23
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListParentMenuId(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListMenuCombo
	 * 2. ClassName  : MenuMgtService
	 * 3. Comment   : 메뉴 이동 모달 메뉴 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 17. 오전 11:25:50
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 */
	List selectListMenuCombo(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateMenuMove
	 * 2. ClassName  : MenuMgtService
	 * 3. Comment   : 메뉴 이동
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 17. 오후 6:18:06
	 * </PRE>
	 *   @return void
	 *   @param param
	 */
	void updateMenuMove(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListBbsCode
	 * 2. ClassName  : MenuMgtService
	 * 3. Comment   : 게시판 코드 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 18. 오후 2:35:19
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 */
	List selectListBbsCode(DataMap param) throws Exception;

}