/**
 *
 *
 * 1. FileName : MenuMgtServiceImpl.java
 * 2. Package : egovframework.admin.menu.service
 * 3. Comment :
 * 4. 작성자  : venus
 * 5. 작성일  : 2013. 8. 23. 오전 10:05:19
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    venus : 2013. 8. 23. :            : 신규 개발.
 */

package egovframework.admin.menu.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("menuMgtService")
public class MenuMgtServiceImpl extends EgovAbstractServiceImpl implements MenuMgtService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;


    /**
     * <PRE>
     * 1. MethodName : selectTotCntMenu
     * 2. ClassName  : MenuServiceImpl
     * 3. Comment   : 메뉴 총 개수
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
     * </PRE>
     *   @return int
     *   @param param
     *   @return
     *   @throws Exception
     */
    public int selectTotCntMenu(DataMap param) throws Exception {
    	return (Integer) commonMybatisDao.selectOne("admin.menu.selectTotCntMenu", param);
    }

    /**
     * <PRE>
     * 1. MethodName : selectListMenu
     * 2. ClassName  : MenuServiceImpl
     * 3. Comment   : 메뉴 리스트
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
     * </PRE>
     *   @return List
     *   @param param
     *   @return
     *   @throws Exception
     */
    public List selectListMenu(DataMap param) throws Exception {
    	return (List) commonMybatisDao.selectList("admin.menu.selectListMenu", param);
    }

    /**
     * <PRE>
     * 1. MethodName : insertMenu
     * 2. ClassName  : MenuServiceImpl
     * 3. Comment   : 메뉴 등록
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
     * </PRE>
     *   @return void
     *   @param param
     *   @throws Exception
     */
    public void insertMenu(DataMap param) throws Exception {
    	String menuId = param.getString("menu_id");	//상위 메뉴 아이디
    	List newMenuId = param.getList("new_menu_id");	//신규 메뉴 아이디(하위 메뉴 아이디)
		List newMenuNm = param.getList("new_menu_nm");
		List newFnctNm = param.getList("new_fnct_nm");
		List newIconNm = param.getList("new_icon_nm");
		List newMenuTypeCode = param.getList("new_menu_type_code");
		List newUrl = param.getList("new_url");
		List newSrtOrd = param.getList("new_srt_ord");
		List newUseYn = param.getList("new_use_yn");
		List newDispYn = param.getList("new_disp_yn");
		List newUrlTyCode = param.getList("new_url_ty_code");
		List newBbsCode = param.getList("new_bbs_code");
		List newCntntsId = param.getList("new_cntnts_id");
		List newRm = param.getList("new_rm");
		int newBbsCodeIndex = 0;
		int newCntntsIdIndex = 0;

		DataMap tmp = new DataMap();
		tmp.put("ss_user_id", param.getString("ss_user_id"));
		tmp.put("new_menu_lv", param.getString("new_menu_lv"));
		tmp.put("menu_id", param.getString("menu_id"));

		tmp.put("sort_ordr_1", param.getString("sort_ordr_1"));
		tmp.put("sort_ordr_2", param.getString("sort_ordr_2"));
		tmp.put("sort_ordr_3", param.getString("sort_ordr_3"));
		tmp.put("sort_ordr_4", param.getString("sort_ordr_4"));
		tmp.put("sort_ordr_5", param.getString("sort_ordr_5"));
		tmp.put("sort_ordr_6", param.getString("sort_ordr_6"));

		int sortDepth = param.getString("menu_id").length() / 4 + 1;

		for (int i = 0; i < newMenuId.size(); i++) {
			switch (sortDepth) {
			case 1: tmp.put("sort_ordr_1", newSrtOrd.get(i));break;
			case 2: tmp.put("sort_ordr_2", newSrtOrd.get(i));break;
			case 3: tmp.put("sort_ordr_3", newSrtOrd.get(i));break;
			case 4: tmp.put("sort_ordr_4", newSrtOrd.get(i));break;
			case 5: tmp.put("sort_ordr_5", newSrtOrd.get(i));break;
			case 6: tmp.put("sort_ordr_6", newSrtOrd.get(i));break;
			default:break;
			}

			// menu_pk 자동 채번
			int maxMenuPk = (Integer) commonMybatisDao.selectOne("admin.menu.selectNumberMenuPk", "");
			String menuPk = "M".concat(String.format("%07d", maxMenuPk));

	    	tmp.put("new_menu_pk", menuPk);
			tmp.put("new_menu_id", menuId + newMenuId.get(i));
			tmp.put("new_menu_nm", newMenuNm.get(i));
			tmp.put("new_fnct_nm", newFnctNm.get(i));
			if (param.containsKey("new_icon_nm")) {
				tmp.put("new_icon_nm", newIconNm.get(i));
			}
			tmp.put("new_menu_type_code", newMenuTypeCode.get(i));
			tmp.put("new_url", newUrl.get(i));
			tmp.put("new_srt_ord", newSrtOrd.get(i));
			tmp.put("new_use_yn", newUseYn.get(i));
			tmp.put("new_disp_yn", newDispYn.get(i));
			tmp.put("new_rm", newRm.get(i));

			tmp.put("new_url_ty_code", newUrlTyCode.get(i));
			if (newUrlTyCode.get(i).equals("10")) {
				tmp.put("new_bbs_code", newBbsCode.get(newBbsCodeIndex++));
			} else if (newUrlTyCode.get(i).equals("20")) {
				tmp.put("new_cntnts_id", newCntntsId.get(newCntntsIdIndex++));
			}

			commonMybatisDao.insert("admin.menu.insertMenu", tmp);
		}
    }

    /**
     * <PRE>
     * 1. MethodName : selectMenu
     * 2. ClassName  : MenuServiceImpl
     * 3. Comment   : 메뉴 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
     * </PRE>
     *   @return DataMap
     *   @param param
     *   @return
     *   @throws Exception
     */
    public DataMap selectMenu(DataMap param) throws Exception {
    	return (DataMap) commonMybatisDao.selectOne("admin.menu.selectMenu", param);
    }

    /**
     * <PRE>
     * 1. MethodName : selectExistYnMenu
     * 2. ClassName  : MenuServiceImpl
     * 3. Comment   : 동일 메뉴 ID 존재 여부 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
     * </PRE>
     *   @return DataMap
     *   @param param
     *   @return
     *   @throws Exception
     */
	public DataMap selectExistYnMenu(DataMap param) throws Exception {
		DataMap dataMap = new DataMap();

		// 메뉴 ID 중복 확인
		DataMap dataMenuIdMap = commonMybatisDao.selectOne("admin.menu.selectExistYnMenuId", param);
		if (dataMenuIdMap.getString("EXIST_YN").equals("Y")) {
			dataMap.put("EXIST_YN", "Y");
			dataMap.put("DUP_TYPE", "ID");
		}

		if (param.containsKey("new_url")) {
			// 메뉴 URL 중복 확인
			DataMap dataMenuUrlMap = commonMybatisDao.selectOne("admin.menu.selectExistYnMenuUrl", param);
			if (dataMenuUrlMap.getString("EXIST_YN").equals("Y")) {
				dataMap.put("EXIST_YN", "Y");
				dataMap.put("DUP_TYPE", "URL");
			}
		}
		return dataMap;
	}

    /**
     * <PRE>
     * 1. MethodName : updateMenu
     * 2. ClassName  : MenuServiceImpl
     * 3. Comment   : 메뉴 수정
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
     * </PRE>
     *   @return void
     *   @param param
     *   @throws Exception
     */
    public void updateMenu(DataMap param) throws Exception {
    	int sortDepth = param.getString("menu_id").length() / 4; // 현재 메뉴의 뎁스 정보 가져오기
    	//현재 뎁스로 업데이트 필드 정하기
		switch (sortDepth) {
			case 1: param.put("sort_ordr_1", param.getString("srt_ord"));break;
			case 2: param.put("sort_ordr_2", param.getString("srt_ord"));break;
			case 3: param.put("sort_ordr_3", param.getString("srt_ord"));break;
			case 4: param.put("sort_ordr_4", param.getString("srt_ord"));break;
			case 5: param.put("sort_ordr_5", param.getString("srt_ord"));break;
			case 6: param.put("sort_ordr_6", param.getString("srt_ord"));break;
		}
    	commonMybatisDao.update("admin.menu.updateMenu", param);
    	commonMybatisDao.update("admin.menu.deleteUrlTyValue", param);
    }

    /**
     * <PRE>
     * 1. MethodName : updateMenuSub
     * 2. ClassName  : MenuServiceImpl
     * 3. Comment   : 하위 메뉴 소트 수정
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
     * </PRE>
     *   @return void
     *   @param param
     *   @throws Exception
     */
    public void updateMenuSub(DataMap param) throws Exception {
    	int sortDepth = param.getString("menu_id").length() / 4;
    	//현재 뎁스로 업데이트 필드 정하기
    	param.put("sortDepth", sortDepth);
    	commonMybatisDao.update("admin.menu.updateMenuSub", param);
    }

    /**
     * <PRE>
     * 1. MethodName : deleteMenu
     * 2. ClassName  : MenuServiceImpl
     * 3. Comment   : 메뉴 삭제
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
     * </PRE>
     *   @return void
     *   @param param
     *   @throws Exception
     */
    public void deleteMenu(DataMap param) throws Exception {
    	commonMybatisDao.delete("admin.menu.deleteAuthMenu", param);
    	commonMybatisDao.delete("admin.menu.deleteMenu", param);
    }

    /**
     * <PRE>
     * 1. MethodName : selectExistYnSubMenu
     * 2. ClassName  : MenuServiceImpl
     * 3. Comment   : 하위메뉴 존재 여부 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public DataMap selectExistYnSubMenu(DataMap param) throws Exception {
    	return (DataMap) commonMybatisDao.selectOne("admin.menu.selectExistYnSubMenu", param);
    }

    /**
     * <PRE>
     * 1. MethodName : selectExistSortSubMenu
     * 2. ClassName  : MenuServiceImpl
     * 3. Comment   : 동일 레벨 메뉴에 동일 정렬순서가 있는지 조회
	 * 4. 작성자    : YYS
	 * 5. 작성일    : 2020. 4. 2. 오후 3:57
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public DataMap selectExistSortSubMenu(DataMap param) throws Exception {
    	int menuLevel = param.getString("menu_id").length() / 4;//현재 선택 메뉴의 뎁스정보 알기
    	param.put("menuLevel", menuLevel);
    	return (DataMap) commonMybatisDao.selectOne("admin.menu.selectExistSortSubMenu", param);
    }

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
	@Override
	public DataMap selectBbsInfo(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.menu.selectBbsInfo", param);
	}

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
	@Override
	public DataMap selectCntntsInfo(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.menu.selectCntntsInfo", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectCodeAttrbs
	 * 2. ClassName  : MenuMgtServiceImpl
	 * 3. Comment   : attr 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 17. 오후 4:57:56
	 * </PRE>
	 *   @param param
	 *   @return
	 */
	@Override
	public DataMap selectCodeAttrbs(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.menu.selectCodeAttrbs", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListParentMenuId
	 * 2. ClassName  : MenuMgtServiceImpl
	 * 3. Comment   : 부모 메뉴 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 17. 오후 4:59:12
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public List selectListParentMenuId(DataMap param) throws Exception {
    	return (List) commonMybatisDao.selectList("admin.menu.selectListParentMenuId", param);
    }

	/**
	 * <PRE>
	 * 1. MethodName : selectListMenuLevel
	 * 2. ClassName  : MenuMgtServiceImpl
	 * 3. Comment   : 메뉴 이동 모달 메뉴 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 17. 오전 11:26:13
	 * </PRE>
	 *   @param param
	 *   @return
	 */
	@Override
	public List selectListMenuCombo(DataMap param) throws Exception {
		// TODO Auto-generated method stub
    	return (List) commonMybatisDao.selectList("admin.menu.selectListMenuCombo", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateMenuMove
	 * 2. ClassName  : MenuMgtServiceImpl
	 * 3. Comment   : 메뉴 이동
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 17. 오후 6:20:49
	 * </PRE>
	 *   @param param
	 */
	@Override
	public void updateMenuMove(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		int menuMoveParentLevel = param.getString("menu_move_parent").length();

		if (menuMoveParentLevel == 8) {
			// level = 2
			commonMybatisDao.update("admin.menu.updateMenuMoveLevel2", param);
		} else if (menuMoveParentLevel == 12) {
			// level = 3
			commonMybatisDao.update("admin.menu.updateMenuMoveLevel3", param);
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListBbsCode
	 * 2. ClassName  : MenuMgtServiceImpl
	 * 3. Comment   : 게시판 코드 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 18. 오후 2:35:42
	 * </PRE>
	 *   @param param
	 *   @return
	 */
	@Override
	public List selectListBbsCode(DataMap param) throws Exception {
		// TODO Auto-generated method stub
    	return (List) commonMybatisDao.selectList("admin.menu.selectListBbsCode", param);
	}

}
