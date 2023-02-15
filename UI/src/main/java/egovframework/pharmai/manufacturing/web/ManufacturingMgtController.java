package egovframework.pharmai.manufacturing.web;

import java.io.IOException;
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

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.common.service.CodeCacheService;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.DateUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.TransReturnUtil;
import egovframework.pharmai.manufacturing.service.ManufacturingMgtService;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : ManufacturingMgtController.java
 * 3. Package  : egovframework.pharmai.manufacturing.web
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
public class ManufacturingMgtController {

	private static Log log = LogFactory.getLog(ManufacturingMgtController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "manufacturingMgtService")
	private ManufacturingMgtService manufacturingService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	/**
	 * <PRE>
	 * 1. MethodName : insertFormManufacturing
	 * 2. ClassName  : ManufacturingMgtController
	 * 3. Comment   : 제조 등록 페이지
	 * 4. 작성자	: yidy
	 * 5. 작성일	: 2021. 5. 25
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pharmai/manufacturing/insertFormManufacturing.do")
	public String insertFormManufacturing(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		// 권한 리스트 조회
		List authList = manufacturingService.selectListAuthor(param);

		model.addAttribute("param", param);
		model.addAttribute("authList", authList);

		return "pharmai/manufacturing/insertFormManufacturing";
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertManufacturing
	 * 2. ClassName  : ManufacturingMgtController
	 * 3. Comment   : 사용자 등록
	 * 4. 작성자	: yidy
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
	@RequestMapping(value = "/pharmai/manufacturing/insertManufacturing.do")
	public String insertManufacturing(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		manufacturingService.insertUser(param);
		model.addAttribute("param", param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/pharmai/chemical/manufacturing/selectPageListManufacturing.do", null, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectManufacturingAjax
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
	@RequestMapping(value = "/pharmai/manufacturing/selectManufacturingAjax.do")
	public @ResponseBody DataMap selectManufacturingAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (!param.containsKey("user_no")) {
			UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
			param.put("user_no", userInfoVo.getUserNo());
		}

		param.put("user_sttus_group_id", Const.UPCODE_USER_STTUS);
		param.put("user_se_group_id", Const.UPCODE_USER_SE);
		param.put("cttpc_se_group_id", Const.UPCODE_CTTPC_SE);
		DataMap resultMap = manufacturingService.selectUser(param);

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
	 * 1. MethodName : updateFormManufacturing
	 * 2. ClassName  : ManufacturingMgtController
	 * 3. Comment   : 사용자 수정 페이지
	 * 4. 작성자	: yidy
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
	@RequestMapping(value = "/pharmai/manufacturing/updateFormManufacturing.do")
	public String updateFormManufacturing(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		param.put("user_sttus_group_id", Const.UPCODE_USER_STTUS);
		param.put("user_se_group_id", Const.UPCODE_USER_SE);
		DataMap resultMap = manufacturingService.selectUser(param);

		// 권한 리스트 조회
		List authList = manufacturingService.selectListAuthor(param);

		// 사용자 권한 조회
		List userAuthList = manufacturingService.selectListAuthorUser(param);

		model.addAttribute("param", param);
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("authList", authList);
		model.addAttribute("userAuthList", userAuthList);

		return "pharmai/manufacturing/updateFormManufacturing";
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateManufacturing
	 * 2. ClassName  : ManufacturingMgtController
	 * 3. Comment   : 사용자 수정
	 * 4. 작성자	: yidy
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
	@RequestMapping(value = "/pharmai/manufacturing/updateManufacturing.do")
	public String updateManufacturing(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		manufacturingService.updateUser(param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		model.addAttribute("param", param);

		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/pharmai/manufacturing/selectManufacturing.do", param, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateManufacturingAjax
	 * 2. ClassName  : ManufacturingMgtController
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
	@RequestMapping(value = "/pharmai/manufacturing/updateManufacturingAjax.do")
	public @ResponseBody DataMap updateManufacturingAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (!param.containsKey("user_no")) {
			UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
			param.put("user_no", userInfoVo.getUserNo());
			param.put("modal_user_id", userInfoVo.getId());
		}
		manufacturingService.updateUserInfo(request, response, param);

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
	 * 1. MethodName : deleteManufacturing
	 * 2. ClassName  : ManufacturingMgtController
	 * 3. Comment   : 사용자 삭제
	 * 4. 작성자	: yidy
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
	@RequestMapping(value = "/pharmai/manufacturing/deleteManufacturing.do")
	public String deleteManufacturing(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		param.put("user_sttus_code_stop", Const.USER_STTUS_CODE_OUT);
		manufacturingService.deleteUser(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/pharmai/chemical/manufacturing/selectPageListManufacturing.do", null, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectIdExistYnAjax
	 * 2. ClassName  : ManufacturingMgtController
	 * 3. Comment   : 사용자 아이디 중복 검사
	 * 4. 작성자	: yidy
	 * 5. 작성일	: 2015. 9. 1. 오후 3:39:17
	 * </PRE>
	 *
	 * @return void
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/pharmai/manufacturing/selectIdExistYnAjax.do")
	public @ResponseBody DataMap selectIdExistYnAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		String resultMap = manufacturingService.selectIdExistYn(param);

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

	@RequestMapping(value = "/pharmai/manufacturing/initlUserPasswordAjax.do")
	public @ResponseBody DataMap initlUserPasswordAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		manufacturingService.initlUserPassword(param);

		DataMap resultJSON = new DataMap();

		String returnMsg = egovMessageSource.getMessage("succ.initl.pwd");

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", returnMsg);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;

	}


	/**
	 * <PRE>
	 * 1. MethodName : selectManufacturing_START
	 * 2. ClassName  : ManufacturingMgtController
	 * 3. Comment   : ManufacturingStart
	 * 4. 작성자	: Kim sm
	 * 5. 작성일	: 2021. 8. 09
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pharmai/chemical/manufacturing/selectManu_START.do")
	public String selectManufacturing_START(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		return "pharmai/manufacturing/selectManu_START";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListManufacturing
	 * 2. ClassName  : ManufacturingMgtController
	 * 3. Comment   : 제조 리스트 조회
	 * 4. 작성자	: kimsm
	 * 5. 작성일	: 2021. 8. 09
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pharmai/chemical/manufacturing/selectPageListManufacturing.do")
	public String selectPageListManufacturing(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		// 최초 리스트 페이지 진입시 초기 셋팅
		param.put("sch_disp_begin_de", param.getString("sch_disp_begin_de", DateUtil.addDay(DateUtil.getToday(), -21)));
		param.put("sch_disp_end_de", param.getString("sch_disp_end_de", DateUtil.getToday()));

		param.put("sch_disp_begin_de_tmp", param.getString("sch_disp_begin_de").replaceAll("\\.", ""));
		param.put("sch_disp_end_de_tmp", param.getString("sch_disp_end_de").replaceAll("\\.", ""));

		/* ### Pasing 시작 ### */
		int totCnt = manufacturingService.selectTotCntManufacturing(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = manufacturingService.selectPageListManufacturing(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("totalCount", totCnt);
		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "pharmai/manufacturing/selectPageListManufacturing";
	}

	@RequestMapping(value = "/pharmai/manufacturing/headerStep.do")
	public String headerStep(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);
		model.addAttribute("param", param);

		return "pharmai/manufacturing/headerStep";
	}

	@RequestMapping(value = { "/pharmai/manufacturing/updateChoicePrjctAjax.do" })
	public @ResponseBody void updateChoicePrjctAjax(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", userInfoVo.getCur_step_cd()));
		param.put("userNo", userInfoVo.getUserNo());

		manufacturingService.updateChoicePrjct(param);

		userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
		userInfoVo.setCur_prjct_type(param.getString("prjct_type"));
		userInfoVo.setCur_step_cd(param.getString("status"));

		request.getSession().setAttribute("userInfoVo", userInfoVo);

		JSONObject resultJSON = new JSONObject();

		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);

		response.setContentType("text/plain; charset=utf-8");
		try {
			response.getWriter().write(resultJSON.toString());
		} catch (IOException e) {
			log.error(e);
		}
	}


	@RequestMapping(value = "/pharmai/manufacturing/copyPrjct.do")
	public String ManufacturingCopy(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		manufacturingService.manufacturingCopyPrj(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.copy"));

		return "redirect: /pharmai/chemical/manufacturing/selectPageListManufacturing.do";
	}




}
