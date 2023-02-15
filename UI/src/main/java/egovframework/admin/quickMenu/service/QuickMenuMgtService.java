package egovframework.admin.quickMenu.service;

import java.util.List;

import egovframework.admin.quickMenu.vo.QuickMenuImgFileVo;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

public interface QuickMenuMgtService {

	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectTotCntqQuickMenu
	 * 2. ClassName  : QuickMenuMgtService
	 * 3. Comment   : 퀵메뉴 총 갯수
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 4:52:02
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 */
	int selectTotCntQuickMenu(DataMap param);

	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectPageListQuickMenu
	 * 2. ClassName  : QuickMenuMgtService
	 * 3. Comment   : 퀵 메뉴 목록 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 4:52:33
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListQuickMenu(DataMap param) throws Exception;


	/**
	 *
	 * <PRE>
	 * 1. MethodName : insertQuickMenu
	 * 2. ClassName  : QuickMenuMgtService
	 * 3. Comment   : 퀵메뉴 입력
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 4:56:03
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param filelist_w
	 *   @param filelist_m
	 *   @throws Exception
	 */
	void insertQuickMenu(DataMap param, List filelist_w, List filelist_m) throws Exception;


	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectQuickMenu
	 * 2. ClassName  : QuickMenuMgtService
	 * 3. Comment   : 퀵메뉴 상세 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 4:55:54
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectQuickMenu(DataMap param) throws Exception;


	/**
	 *
	 * <PRE>
	 * 1. MethodName : updateQuickMenu
	 * 2. ClassName  : QuickMenuMgtService
	 * 3. Comment   : 퀵메뉴 수정
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 4:55:36
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param filelist_w
	 *   @param filelist_m
	 *   @throws Exception
	 */
	void updateQuickMenu(DataMap param, List filelist_w, List filelist_m) throws Exception;


	/**
	 *
	 * <PRE>
	 * 1. MethodName : deleteQuickMenu
	 * 2. ClassName  : QuickMenuMgtService
	 * 3. Comment   : 퀵 메뉴 삭제
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 4:55:07
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteQuickMenu(DataMap param) throws Exception;


	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectQuickMenuImgs
	 * 2. ClassName  : QuickMenuMgtService
	 * 3. Comment   : 퀵메뉴 이미지 리스트 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 4:54:47
	 * </PRE>
	 *   @return List
	 *   @param ifvo
	 *   @return
	 *   @throws Exception
	 */
	List selectQuickMenuImgs(QuickMenuImgFileVo ifvo) throws Exception;

	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectQuickMenuImg
	 * 2. ClassName  : QuickMenuMgtService
	 * 3. Comment   : 퀵 메뉴 이미지 정보 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 4:53:55
	 * </PRE>
	 *   @return NtsysFileVO
	 *   @param fvo
	 *   @return
	 *   @throws Exception
	 */

	public NtsysFileVO selectQuickMenuImg(NtsysFileVO fvo) throws Exception;

	/**
	 *
	 * <PRE>
	 * 1. MethodName : deleteQuickMenuImg
	 * 2. ClassName  : QuickMenuMgtService
	 * 3. Comment   : 퀵메뉴 이미지 정보 삭제
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 4:53:34
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteQuickMenuImg(DataMap param) throws Exception;
}
