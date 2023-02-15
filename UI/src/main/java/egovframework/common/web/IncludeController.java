package egovframework.common.web;

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
import org.springframework.web.util.UrlPathHelper;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.common.service.CommonCodeService;
import egovframework.common.service.IncludeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;

@Controller
public class IncludeController {

	private static Log log = LogFactory.getLog(IncludeController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	/** CommonCodeService */
	@Resource(name = "includeService")
	private IncludeService includeService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;


//	/**
//	 * <PRE>
//	 * 1. MethodName : header
//	 * 2. ClassName  : IncludeController
//	 * 3. Comment   : 헤더 영역 생성
//	 * 4. 작성자	: 박재현
//	 * 5. 작성일	: 2018. 7. 10. 오후 2:05:19
//	 * </PRE>
//	 *   @return String
//	 *   @param request
//	 *   @param response
//	 *   @param model
//	 *   @return
//	 *   @throws Exception
//	 */
//	@RequestMapping(value = "/common/inc/header.do")
//	public String header(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
//
//
//		DataMap param = RequestUtil.getDataMap(request);
//		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
//		param.put("ss_user_no", userInfoVo.getUserNo());
//
//		//CMS메뉴코드
//		param.put("up_menu_id", Const.MENU_ID_CMS);
//		List topMenuList = includeService.selectTopMenuList(param);
//
//		UrlPathHelper urlPathHelper = new UrlPathHelper();
//		String url = urlPathHelper.getOriginatingRequestUri(request);
//		param.put("url", url);
//
//		DataMap resultMap = includeService.selectMenuByUrl(param);
//		String top_menu_id = Const.MENU_ID_TOP;
//		if ( resultMap != null ) {
//			top_menu_id = resultMap.getString("MENU_ID").substring(0, 12);
//		}
//
//		model.addAttribute("top_menu_id", top_menu_id);
//		model.addAttribute("topMenuList", topMenuList);
//
//		return "common/inc/header";
//	}
//
//	/**
//	 * <PRE>
//	 * 1. MethodName : menu
//	 * 2. ClassName  : IncludeController
//	 * 3. Comment   : 사이드 메뉴 영역 생성
//	 * 4. 작성자	: 박재현
//	 * 5. 작성일	: 2018. 7. 10. 오후 2:05:31
//	 * </PRE>
//	 *   @return String
//	 *   @param request
//	 *   @param response
//	 *   @param model
//	 *   @return
//	 *   @throws Exception
//	 */
//	@RequestMapping(value = "/common/inc/menu.do")
//	public String menu(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
//
//
//		DataMap param = RequestUtil.getDataMap(request);
//		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
//		param.put("ss_user_no", userInfoVo.getUserNo());
//
//		//CMS메뉴코드
//		UrlPathHelper urlPathHelper = new UrlPathHelper();
//		String url = urlPathHelper.getOriginatingRequestUri(request);
//		param.put("url", url);
//		DataMap resultMap = includeService.selectMenuByUrl(param);
//
//		String top_menu_id = Const.MENU_ID_TOP;
//		if ( resultMap != null ) {
//			model.addAttribute("menu_id", resultMap.getString("MENU_ID"));
//			top_menu_id = resultMap.getString("MENU_ID").substring(0, 12);
//			model.addAttribute("cur_menu_nm", resultMap.getString("MENU_NM"));
//		}
//		model.addAttribute("top_menu_id", top_menu_id);
//
//
//		param.put("authorId", userInfoVo.getAuthorId());
//
//
//		log.debug("####:"+userInfoVo.getAuthorId());
//
//		// 180226 수정 - 연도계산 후 프로젝트 목록 나오도록 수정
//		List projectYearComboStr = null;
//		model.addAttribute("projectYearComboStr", projectYearComboStr);
//
//		return "common/inc/menu";
//	}

	@RequestMapping(value = "/common/inc/selectLeftMenuListAjax.do")
	public @ResponseBody DataMap selectLeftMenuListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		DataMap resultJSON = new DataMap();

		// 세션에 담겨져 있는 메뉴 리스트로 조회해서 리턴한다.
		// 1depth 메뉴가 키값으로 데이터가 들어있다.
		DataMap leftMenuInfo = (DataMap)request.getSession().getAttribute("leftMenuMap");

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultStats.put("leftMenuList", leftMenuInfo.get(param.getString("up_menu_id")));

		// CMS메뉴코드
		param.put("up_menu_id", Const.MENU_ID_CMS);
		List topMenuList = includeService.selectTopMenuList(param);
		model.addAttribute("topMenuList", topMenuList);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}
}
