package egovframework.admin.popup.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : PopupMgtService.java
 * 3. Package  : egovframework.admin.popupMgt.service
 * 4. Comment  : 팝업 관리
 * 5. 작성자   : 박재현
 * 6. 작성일   : 2015. 9. 7. 오전 9:51:50
 * </PRE>
 */
public interface PopupMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntPopupMgt
	 * 2. ClassName  : PopupMgtService
	 * 3. Comment   : 팝업 총개수 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 9:53:11
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntPopupMgt(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : selectPageListPopupMgt
	 * 2. ClassName  : PopupMgtService
	 * 3. Comment   : 팝업 페이지 리스트 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 9:53:13
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListPopupMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPopupMgt
	 * 2. ClassName  : PopupMgtService
	 * 3. Comment   : 팝업 상세조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 10:14:36
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 */
	DataMap selectPopupMgt(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : deletePopupMgt
	 * 2. ClassName  : PopupMgtService
	 * 3. Comment   : 팝업 삭제
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 11:33:07
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deletePopupMgt(DataMap param)throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : insertPopupMgt
	 * 2. ClassName  : PopupMgtService
	 * 3. Comment   : 팝업 등록
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 11. 오후 4:59:05
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void insertPopupMgt(DataMap param, List fileList)throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : updatePopupMgt
	 * 2. ClassName  : PopupMgtService
	 * 3. Comment   : 팝업 수정
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 11. 오후 4:59:25
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updatePopupMgt(DataMap param, List fileList)throws Exception;
}
