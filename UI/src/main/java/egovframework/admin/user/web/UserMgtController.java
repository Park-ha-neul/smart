package egovframework.admin.user.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.user.service.UserMgtService;
import egovframework.common.service.CodeCacheService;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.TransReturnUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : UserMgtController.java
 * 3. Package  : egovframework.admin.user.web
 * 4. Comment  :
 * 5. 작성자   : JJH
 * 6. 작성일   : 2015. 12. 7. 오후 4:57:45
 * 7. 변경이력 :
 *	이름	 : 일자		  : 근거자료   : 변경내용
 *	------------------------------------------------------
 *	JJH : 2015. 12. 7. :			: 신규 개발.
 * </PRE>
 */
@Controller
public class UserMgtController {

	private static Log log = LogFactory.getLog(UserMgtController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "userMgtService")
	private UserMgtService userMgtService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListUserMgt
	 * 2. ClassName  : UserMgtController
	 * 3. Comment   : 사용자 관리 페이지 리스트 조회
	 * 4. 작성자	: 함경호
	 * 5. 작성일	: 2015. 9. 1. 오후 3:39:26
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/user/selectPageListUserMgt.do")
	public String selectPageListUserMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		// 사용자 구분 코드
		param.put("group_id", Const.UPCODE_USER_STTUS);
		param.put("user_se_code", 10);

		// 2018.02.23 Juseong 처음 페이지 로딩 시 상태를 활동으로 변경 후 처리
		if (request.getParameter("sch_user_sttus_code") == null) {
			param.put("sch_user_sttus_code", Const.USER_STTUS_CODE_ACTIVE);
		}

		/* ### Pasing 시작 ### */
		int totCnt = userMgtService.selectTotCntUser(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = userMgtService.selectPageListUser(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("totalCount", totCnt);
		model.addAttribute("resultList", resultList);
		model.addAttribute("resultJson", convertListToJson(resultList));
		model.addAttribute("param", param);

		return "admin/user/selectPageListUserMgt";
	}

	public static JSONArray convertListToJson(List<HashMap<String, Object>> bankCdList) {
		JSONArray jsonArray = new JSONArray();
		for (Map<String, Object> map : bankCdList) {
			jsonArray.add(convertMapToJson(map));
		}
		return jsonArray;
	}

	public static JSONObject convertMapToJson(Map<String, Object> map) {
		JSONObject json = new JSONObject();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			json.put(key, value);
		}
		return json;

	}

	/**
	 * <PRE>
	 * 1. MethodName : insertFormUserMgt
	 * 2. ClassName  : UserMgtController
	 * 3. Comment   : 사용자 등록 페이지
	 * 4. 작성자	: 함경호
	 * 5. 작성일	: 2015. 9. 1. 오후 3:39:25
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/user/insertFormUserMgt.do")
	public String insertFormUserMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		// 권한 리스트 조회
		List authList = userMgtService.selectListAuthor(param);

		model.addAttribute("param", param);
		model.addAttribute("authList", authList);

		return "admin/user/insertFormUserMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertUserMgt
	 * 2. ClassName  : UserMgtController
	 * 3. Comment   : 사용자 등록
	 * 4. 작성자	: 함경호
	 * 5. 작성일	: 2015. 9. 1. 오후 3:39:24
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/user/insertUserMgt.do")
	public String insertUserMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		userMgtService.insertUser(param);
		model.addAttribute("param", param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/user/selectPageListUserMgt.do", null, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectUserMgt
	 * 2. ClassName  : UserMgtController
	 * 3. Comment   : 사용자 조회
	 * 4. 작성자	: 함경호
	 * 5. 작성일	: 2015. 9. 1. 오후 3:39:22
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/user/selectUserMgt.do")
	public String selectUserMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		param.put("user_sttus_group_id", Const.UPCODE_USER_STTUS);
		param.put("user_se_group_id", Const.UPCODE_USER_SE);
		param.put("cttpc_se_group_id", Const.UPCODE_CTTPC_SE);
		param.put("yn_group_id", Const.UPCODE_USE_YN);
		DataMap resultMap = userMgtService.selectUser(param);

		// 사용자 권한 조회
		List authList = userMgtService.selectListAuthorUser(param);

		model.addAttribute("authList", authList);
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/user/selectUserMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectUserMgtAjax
	 * 2. ClassName  : UserMgtController
	 * 3. Comment   : 사용자 조회
	 * 4. 작성자	: 박재현
	 * 5. 작성일	: 2018. 7. 17. 오후 3:53:56
	 * </PRE>
	 *
	 * @return void
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/user/selectUserMgtAjax.do")
	public @ResponseBody DataMap selectUserMgtAjax(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (!param.containsKey("user_no")) {
			UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
			param.put("user_no", userInfoVo.getUserNo());
		}

		param.put("user_sttus_group_id", Const.UPCODE_USER_STTUS);
		param.put("user_se_group_id", Const.UPCODE_USER_SE);
		param.put("cttpc_se_group_id", Const.UPCODE_CTTPC_SE);
		DataMap resultMap = userMgtService.selectUser(param);

		// 연락처구분코드 조회
		List cttpcSeComboStr = CodeCacheService.getCode(Const.UPCODE_CTTPC_SE);
		// 사용자상태코드 조회
		List userSttusComboStr = CodeCacheService.getCode(Const.UPCODE_USER_STTUS);

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultMap", resultMap);
		resultJSON.put("cttpcSeComboStr", cttpcSeComboStr);
		resultJSON.put("userSttusComboStr", userSttusComboStr);

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateFormUserMgt
	 * 2. ClassName  : UserMgtController
	 * 3. Comment   : 사용자 수정 페이지
	 * 4. 작성자	: 함경호
	 * 5. 작성일	: 2015. 9. 1. 오후 3:39:21
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/user/updateFormUserMgt.do")
	public String updateFormUserMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		param.put("user_sttus_group_id", Const.UPCODE_USER_STTUS);
		param.put("user_se_group_id", Const.UPCODE_USER_SE);
		DataMap resultMap = userMgtService.selectUser(param);

		// 권한 리스트 조회
		List authList = userMgtService.selectListAuthor(param);

		// 사용자 권한 조회
		List userAuthList = userMgtService.selectListAuthorUser(param);

		model.addAttribute("param", param);
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("authList", authList);
		model.addAttribute("userAuthList", userAuthList);

		return "admin/user/updateFormUserMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateUserMgt
	 * 2. ClassName  : UserMgtController
	 * 3. Comment   : 사용자 수정
	 * 4. 작성자	: 함경호
	 * 5. 작성일	: 2015. 9. 1. 오후 3:39:20
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/user/updateUserMgt.do")
	public String updateUserMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		userMgtService.updateUser(param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		model.addAttribute("param", param);

		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/user/selectUserMgt.do", param, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateUserMgtAjax
	 * 2. ClassName  : UserMgtController
	 * 3. Comment   : 사용자 정보 수정
	 * 4. 작성자	: JJH
	 * 5. 작성일	: 2016. 6. 3. 오후 3:13:22
	 * </PRE>
	 *
	 * @return void
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/user/updateUserMgtAjax.do")
	public @ResponseBody DataMap updateUserMgtAjax(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (!param.containsKey("user_no")) {
			UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
			param.put("user_no", userInfoVo.getUserNo());
			param.put("modal_user_id", userInfoVo.getId());
		}
		userMgtService.updateUserInfo(request, response, param);

		DataMap resultJSON = new DataMap();

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", egovMessageSource.getMessage("succ.data.update"));
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteUserMgt
	 * 2. ClassName  : UserMgtController
	 * 3. Comment   : 사용자 삭제
	 * 4. 작성자	: 함경호
	 * 5. 작성일	: 2015. 9. 1. 오후 3:39:19
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/user/deleteUserMgt.do")
	public String deleteUser(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		param.put("user_sttus_code_stop", Const.USER_STTUS_CODE_OUT);
		userMgtService.deleteUser(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/user/selectPageListUserMgt.do", null, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectIdExistYnAjax
	 * 2. ClassName  : UserMgtController
	 * 3. Comment   : 사용자 아이디 중복 검사
	 * 4. 작성자	: 함경호
	 * 5. 작성일	: 2015. 9. 1. 오후 3:39:17
	 * </PRE>
	 *
	 * @return void
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/user/selectIdExistYnAjax.do")
	public @ResponseBody DataMap selectIdExistYnAjax(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		String resultMap = userMgtService.selectIdExistYn(param);

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultMap", resultMap);

		String returnMsg = "";
		if (resultMap.equals("Y")) {
			returnMsg = egovMessageSource.getMessage("error.duple.id");
		} else {
			returnMsg = egovMessageSource.getMessage("succ.duple.id");
		}

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", returnMsg);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	@RequestMapping(value = "/admin/user/initlUserPasswordAjax.do")
	public @ResponseBody DataMap initlUserPasswordAjax(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		userMgtService.initlUserPassword(param);

		DataMap resultJSON = new DataMap();

		String returnMsg = egovMessageSource.getMessage("succ.initl.pwd");

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", returnMsg);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;

	}
}
