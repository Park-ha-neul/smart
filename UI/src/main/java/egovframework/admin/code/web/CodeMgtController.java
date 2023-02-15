package egovframework.admin.code.web;

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

import egovframework.admin.code.service.CodeMgtService;
import egovframework.admin.common.vo.UserInfoVo;
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

@Controller
public class CodeMgtController{

	private static Log log = LogFactory.getLog(CodeMgtController.class);

	@Resource(name="egovMessageSource")
	private EgovMessageSource egovMessageSource;

	/** codeMgtService */
	@Resource(name = "codeMgtService")
	private CodeMgtService codeMgtService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	/** codeCacheService */
	@Resource(name = "codeCacheService")
	private CodeCacheService codeCacheService;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListGroupCodeMgt
	 * 2. ClassName  : CodeController
	 * 3. Comment   : 그룹코드 리스트
	 * 4. 작성자	: 김영배
	 * 5. 작성일	: 2015. 8. 31. 오후 9:59:57
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/code/selectPageListGroupCodeMgt.do")
	public String selectPageListGroupCodeMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");

		DataMap param = RequestUtil.getDataMap(request);
		/* ### Pasing 시작 ### */
		int totCnt = codeMgtService.selectTotCntGroupCode(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = codeMgtService.selectPageListGroupCode(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "admin/code/selectPageListGroupCodeMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : selectListCodeMgt
	 * 2. ClassName  : CodeController
	 * 3. Comment   : 코드 리스트 조회
	 * 4. 작성자	: venus
	 * 5. 작성일	: 2013. 9. 3. 오후 11:43:25
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/code/selectListCodeMgt.do")
	public String selectListCodeMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");
		DataMap param = RequestUtil.getDataMap(request);

		List resultList = codeMgtService.selectListCode(param);
		model.addAttribute("resultList", resultList);
		DataMap resultMap = codeMgtService.selectGroupCode(param);
		model.addAttribute("resultMap", resultMap);

		model.addAttribute("param", param);

		DataMap codeParam = new DataMap();
		codeParam.put("group_id", Const.UPCODE_USE_YN);
		model.addAttribute("ynCodeList", commonCodeService.selectCodeList(codeParam));

		return "admin/code/selectListCodeMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : deleteGroupCodeMgt
	 * 2. ClassName  : CodeMgtController
	 * 3. Comment   : 그룹코드 및 상세코드 삭제
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:50:22
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @param status
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/code/deleteGroupCodeMgt.do")
	public String deleteGroupCodeMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");


		DataMap param = RequestUtil.getDataMap(request);
		codeMgtService.deleteGroupCode(param);
		model.addAttribute("param", param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));
		status.setComplete();

		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/code/selectPageListGroupCodeMgt.do", null, model);
	}


	/**
	 * <PRE>
	 * 1. MethodName : insertFormGroupCodeMgt
	 * 2. ClassName  : CodeMgtController
	 * 3. Comment   : 그룹코드 입력폼
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:50:33
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @param status
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/code/insertFormGroupCodeMgt.do")
	public String insertFormGroupCodeMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");

		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultMap = codeMgtService.selectMaxGroupId(param);
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/code/insertFormGroupCodeMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : insertGroupCodeMgt
	 * 2. ClassName  : CodeMgtController
	 * 3. Comment   : 그룹코드 추가
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:50:49
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @param status
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/code/insertGroupCodeMgt.do")
	public String insertGroupCodeMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		DataMap groupCodeExistYn = codeMgtService.selectExistYnGroupCode(param);
		if(groupCodeExistYn.getString("EXIST_YN").equals("Y")){
			String[] grpCode = new String[1];
			grpCode[0] = param.getString("groupId");
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.grp.code.dup", grpCode));
			return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/code/insertFormGroupCodeMgt.do", param, model);
		}
		codeMgtService.insertGroupCode(param);
		model.addAttribute("param", param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));
		status.setComplete();

		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/code/selectPageListGroupCodeMgt.do", null, model);
	}


	/**
	 * <PRE>
	 * 1. MethodName : updateCodeMgt
	 * 2. ClassName  : CodeMgtController
	 * 3. Comment   : 그룹코드 및 상세코드 수정
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:51:04
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @param status
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/code/updateCodeMgt.do")
	public String updateCodeMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		codeMgtService.updateCode(param);
		model.addAttribute("param", param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));
		status.setComplete();

		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/code/selectListCodeMgt.do", param, model);
	}


	/**
	 * <PRE>
	 * 1. MethodName : selectExistYnGroupIdAjax
	 * 2. ClassName  : CodeMgtController
	 * 3. Comment   : 그룹ID 중복체크
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 27. 오전 10:05:35
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/code/selectExistYnGroupIdAjax.do")
	public @ResponseBody DataMap selectExistYnGroupIdAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultMap = codeMgtService.selectExistYnGroupCode(param);//group id 존재 여부 확인

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultMap", resultMap);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats); //리턴값 Y / N

		return resultJSON;
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : refreshCacheCode
	 * 2. ClassName  : CodeMgtController
	 * 3. Comment   : 코드 캐쉬 새로 고침
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 12. 오후 4:26:49
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @param status
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/code/refreshCacheCode.do")
	public String refreshCacheCode(HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");

		CodeCacheService.clear();
		codeCacheService.resetCodeList();

		DataMap param = RequestUtil.getDataMap(request);
		param.put("redirectUrl", "/admin/code/selectPageListGroupCodeMgt.do");
		model.addAttribute("param", param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.refresh"));
		status.setComplete();

		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/code/selectPageListGroupCodeMgt.do", null, model);
	}


}
