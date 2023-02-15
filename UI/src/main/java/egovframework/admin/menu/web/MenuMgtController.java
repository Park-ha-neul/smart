
package egovframework.admin.menu.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import egovframework.admin.bbs.service.BbsMgtService;
import egovframework.admin.cnts.service.CntsMgtService;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.menu.service.MenuMgtService;
import egovframework.common.service.CodeCacheService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.CacheCommboUtil;
import egovframework.framework.common.util.CommboUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.TransReturnUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

@Controller
public class MenuMgtController {

	private static Log log = LogFactory.getLog(MenuMgtController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	/** menuMgtService */
	@Resource(name = "menuMgtService")
	private MenuMgtService menuMgtService;

	/** bbsMgtService */
	@Resource(name = "bbsMgtService")
	private BbsMgtService bbsMgtService;

	/** cntsMgtService */
	@Resource(name = "cntsMgtService")
    private CntsMgtService cntsMgtService;

	@Resource(name="NtsysFileMngService")
    private NtsysFileMngService ntsysFileMngService;

	/**
	 * <PRE>
	 * 1. MethodName : selectListMenuMgt
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 메뉴 리스트 조회
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 4:04:02
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/selectListMenuMgt.do")
	public String selectListMenuMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		int totCnt = menuMgtService.selectTotCntMenu(param); //메뉴 전체갯수
		param.put("totalCount", totCnt);
		List resultList = menuMgtService.selectListMenu(param); //메뉴 리스트정보 가져오기

		model.addAttribute("resultList", resultList);

		DataMap codeParam = new DataMap();

		codeParam.put("group_id", Const.UPCODE_USE_YN);
		codeParam.put("level", "2");
		List menuComboStr = menuMgtService.selectListMenuCombo(codeParam); // 메뉴 이동 모달 메뉴 콤보

		param.put("selectPageListBbsUrl", Globals.SELECT_PAGE_LIST_BBS_URL);

		List parentMenuIdComboStr = menuMgtService.selectListParentMenuId(param);

		model.addAttribute("menuComboStr", menuComboStr);
		model.addAttribute("parentMenuIdComboStr", parentMenuIdComboStr);
		model.addAttribute("param", param);

		return "admin/menu/selectListMenuMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : selectMenuMgtAjax
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 메뉴 상세 조회
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 4:04:36
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/selectMenuMgtAjax.do")
	public @ResponseBody DataMap selectMenuMgtAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultMap = menuMgtService.selectMenu(param);//메뉴 정보 가져오기

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultMap", resultMap);

		DataMap tmpParam = new DataMap();
		DataMap bbsMap = new DataMap();
		DataMap cntntsMap = new DataMap();
		DataMap tmpMap = null;

		String sysCode = "";
		String bbsCode = "";

		tmpParam.put("group_id", Const.UPCODE_SYS_CODE);

		if(resultMap.getString("MENU_SE_CODE").equals("10") && !resultMap.getString("BBS_CODE").equals("")){
			tmpParam.put("bbs_code", resultMap.getString("BBS_CODE"));
			tmpMap = menuMgtService.selectBbsInfo(tmpParam);
			if (tmpMap != null) {
				sysCode = tmpMap.getString("SYS_CODE");
				bbsCode = tmpMap.getString("BBS_CODE");
			}

			String bbsSysComboStrText = CacheCommboUtil.getComboStr(Const.UPCODE_SYS_CODE, "CODE", "CODE_NM", sysCode, "");
			bbsMap.put("bbsSysComboStrText", bbsSysComboStrText);

			tmpParam.put("sys_code", sysCode);
			tmpParam.put("group_id", Const.UPCODE_BBS_TY);
			List bbsCodeComboStr = menuMgtService.selectListBbsCode(tmpParam);
			String bbsCodeComboStrText = CommboUtil.getComboStr(bbsCodeComboStr, "BBS_CODE", "BBS_NM", bbsCode, "");
			bbsMap.put("bbsCodeComboStrText", bbsCodeComboStrText);

			tmpParam.put("group_id", Const.UPCODE_SYS_CODE);
			DataMap codeAttrbs = menuMgtService.selectCodeAttrbs(tmpParam);
			if (codeAttrbs != null) {
				bbsMap.put("subpath", codeAttrbs.getString("ATTRB_1"));
			} else {
				bbsMap.put("subpath", "");
			}
		} else if (resultMap.getString("MENU_SE_CODE").equals("20") && !resultMap.getString("CNTNTS_ID").equals("")) {
			tmpParam.put("cntnts_id", resultMap.getString("CNTNTS_ID"));
			cntntsMap = menuMgtService.selectCntntsInfo(tmpParam);
		}
		resultJSON.put("bbsMap", bbsMap);
		resultJSON.put("cntntsMap", cntntsMap);
		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats); //리턴값 : 선택된 메뉴 정보

		return resultJSON;
	}


	/**
	 * <PRE>
	 * 1. MethodName : selectExistYnMenuMgtAjax
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 메뉴 존재 여부 체크
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 4:15:12
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/selectExistYnMenuMgtAjax.do")
	public @ResponseBody DataMap selectExistYnMenuMgtAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultMap = new DataMap();

		String resultCode = "ok";
		String resultMsg = "";

		String newMenuId = param.getString("new_menu_id");
		String[] newMenuIdList = newMenuId.split(",");
		String newMenuUrl = param.getString("new_url");
		String[] newMenuUrlList = newMenuUrl.split(",", newMenuIdList.length);

		for (int i = 0; i < newMenuIdList.length; i++) {
			DataMap dataMap = new DataMap();
			dataMap.put("new_menu_id", newMenuIdList[i]);
			if (newMenuUrlList.length > 0) {
				if (!newMenuUrlList[i].isEmpty()) {
					dataMap.put("new_url", newMenuUrlList[i]);
				}
			}
			resultMap = menuMgtService.selectExistYnMenu(dataMap);

			if (resultMap.getString("EXIST_YN").equals("Y")) {
				resultCode = "error";
				if (resultMap.getString("DUP_TYPE").equals("ID")) {
					resultMsg = egovMessageSource.getMessage("error.menu.id.dup", new String[] { newMenuIdList[i] });
				}
				if (resultMap.getString("DUP_TYPE").equals("URL")) {
					resultMsg = egovMessageSource.getMessage("error.menu.url.dup", new String[] { newMenuUrlList[i] });
				}
				break;
			}
		}

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultMap", resultMap);

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", resultCode);
		resultStats.put("resultMsg", resultMsg);
		resultJSON.put("resultStats", resultStats); // 리턴값 Y / N

		return resultJSON;
	}


	/**
	 * <PRE>
	 * 1. MethodName : insertMenuMgt
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 메뉴 등록
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 4:15:29
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @param status
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/insertMenuMgt.do")
	public String insertMenuMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_id", userInfoVo.getUserNo());

		int upMenuLv = param.getInt("menu_lv");// 메뉴 레벨(뎁스)
		param.put("new_menu_lv", upMenuLv + 1);

		menuMgtService.insertMenu(param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));
		status.setComplete();

		// redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("sch_parent_menu_id", param.getString("sch_parent_menu_id"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/menu/selectListMenuMgt.do", resultParam, model);	}


	/**
	 * <PRE>
	 * 1. MethodName : updateMenuMgt
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 메뉴 수정
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 4:16:28
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @param status
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/updateMenuMgt.do")
	public String updateMenuMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		param.put("menu_nm", param.getStringOrgn("menu_nm"));
		param.put("url", param.getStringOrgn("url"));

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		menuMgtService.updateMenu(param); // 해당 메뉴 업데이트
		menuMgtService.updateMenuSub(param); // 하위 메뉴 업데이트

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));
		status.setComplete();

		// redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("sch_parent_menu_id", param.getString("sch_parent_menu_id"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/menu/selectListMenuMgt.do", resultParam, model);
	}


	/**
	 * <PRE>
	 * 1. MethodName : deleteMenuMgt
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 메뉴 삭제
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 4:17:21
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @param status
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/deleteMenuMgt.do")
	public String deleteMenuMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		menuMgtService.deleteMenu(param);		//선택된 메뉴 및 하위 메뉴 삭제

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));
		status.setComplete();

		// redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("sch_parent_menu_id", param.getString("sch_parent_menu_id"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/menu/selectListMenuMgt.do", resultParam, model);
	}


	/**
	 * <PRE>
	 * 1. MethodName : selectExistSortMenuMgtAjax
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 메뉴 정렬 정보 존재 여부 체크
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 4:15:12
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/selectExistSortMenuMgtAjax.do")
	public @ResponseBody DataMap selectExistSortMenuMgtAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultMap = menuMgtService.selectExistSortSubMenu(param);// 메뉴의 동일 뎁스의 정렬 순서가 존재하는지 확인

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultMap", resultMap);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);//return 값 Y:N

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListBbsCodeAjax
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 게시판 코드 조회
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 8. 27. 오후 5:29:02
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/selectListBbsCodeAjax.do")
	public @ResponseBody DataMap selectListBbsCodeAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultJSON = new DataMap();
		if (param.getString("sys_code").equals("")) {
			String sysComboStrText = CacheCommboUtil.getComboStr(Const.UPCODE_SYS_CODE, "CODE", "CODE_NM", "", "");
			resultJSON.put("sysComboStrText", sysComboStrText);
			// CodeCache의 syscode 중 첫번째 syscode 로 조회
			List sysCodeList = CodeCacheService.getCode(Const.UPCODE_SYS_CODE);
			DataMap firstData = (DataMap) sysCodeList.get(0);
			param.put("sys_code", firstData.getString("CODE"));
		}
		param.put("group_id", Const.UPCODE_BBS_TY);
		List bbsCodeComboStr = menuMgtService.selectListBbsCode(param);
		String bbsCodeComboStrText = CommboUtil.getComboStr(bbsCodeComboStr, "BBS_CODE", "BBS_NM", "", "");
		resultJSON.put("bbsCodeComboStrText", bbsCodeComboStrText);
		resultJSON.put("bbsListLength", bbsCodeComboStr.size());

		param.put("group_id", Const.UPCODE_SYS_CODE);
		DataMap codeAttrbs = menuMgtService.selectCodeAttrbs(param);
		resultJSON.put("subpath", codeAttrbs.getString("ATTRB_1"));

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);// 리턴값 : 선택된 메뉴 정보

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListCntnts
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 컨텐츠 게시판 조회
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 8. 27. 오후 5:29:52
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/selectPageListCntnts.do")
	public String selectPageListCntnts(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		param.put("sys_group_id", Const.UPCODE_SYS_CODE); //시스템 코드

    	/* ### Pasing 시작 ### */
    	int totCnt = cntsMgtService.selectTotCntMgt(param);
    	param.put("totalCount", totCnt);
    	param = pageNavigationUtil.createNavigationInfo(model, param);
        List resultList = cntsMgtService.selectPageListCntsMgt(param);
        /* ### Pasing 끝 ### */

        param.put("selectCntntsUrl", Globals.SELECT_CNTNTS_URL);

        model.addAttribute("totalCount", totCnt);
        model.addAttribute("resultList", resultList);
        model.addAttribute("param", param);

		return "admin/menu/selectPageListCntnts.popUp";

	}

	@RequestMapping(value = "/admin/menu/selectCntnts.do")
	public String selectCntsMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		param.put("sys_group_id", Const.UPCODE_SYS_CODE); //시스템 구분 코드

		DataMap resultMap = cntsMgtService.selectCntsMgt(request, response, param);

		resultMap.put("CN", resultMap.htmlTagFilterRestore(resultMap.getString("CN")));

		// #### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(resultMap.getString("DOC_ID"));
		List<NtsysFileVO> fileList = ntsysFileMngService.selectFileInfs(fvo);
		// #### FILE LIST 검색 End ####

		param.put("selectCntntsUrl", Globals.SELECT_CNTNTS_URL);

		model.addAttribute("fileList", fileList);
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/menu/selectCntnts.noMenu";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListMenuComboAjax
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 메뉴 이동 모달 메뉴 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 17. 오후 4:56:09
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 * @return
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/selectListMenuComboAjax.do")
	public @ResponseBody DataMap selectListMenuComboAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultJSON = new DataMap();
		List resultList = menuMgtService.selectListMenuCombo(param);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultList", resultList);
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);// 리턴값 : 선택된 메뉴 정보

		return resultJSON;
	}


	/**
	 * <PRE>
	 * 1. MethodName : updateMenuMove
	 * 2. ClassName  : MenuMgtController
	 * 3. Comment   : 메뉴 이동
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 6. 오후 3:36:17
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @param status
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/menu/updateMenuMove.do")
	public String updateMenuMove(HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		menuMgtService.updateMenuMove(param); // 메뉴 이동

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));
		status.setComplete();

		// redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("sch_parent_menu_id", param.getString("sch_parent_menu_id"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/menu/selectListMenuMgt.do", resultParam, model);
	}
}