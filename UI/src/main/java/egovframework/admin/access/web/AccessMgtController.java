package egovframework.admin.access.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.admin.access.service.AccessMgtService;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.TransReturnUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;

@Controller
public class AccessMgtController {

private static Log log = LogFactory.getLog(AccessMgtController.class);

	@Resource(name="egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "accessMgtService")
	private AccessMgtService accessMgtService;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListAccessMgt
	 * 2. ClassName  : AccessMgtController
	 * 3. Comment   : 접근 아이피 조회
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 15. 오후 4:58:58
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value={ "/admin/access/selectPageListAccessMgt.do", "/admin/access/{sys}/selectPageListAccessMgt.do" })
	public String selectPageListAccessMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		List resultList = accessMgtService.selectPageListAccess(model, param);

		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "admin/access/selectPageListAccessMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertFormAccessMgt
	 * 2. ClassName  : AccessMgtController
	 * 3. Comment   : 접근 아이피 등록폼
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 15. 오후 5:15:32
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= {"/admin/access/insertFormAccessMgt.do", "/admin/access/{sys}/insertFormAccessMgt.do"})
	public String insertFormAccessMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		model.addAttribute("param", param);
		return "admin/access/insertFormAccessMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertAccessMgt
	 * 2. ClassName  : AccessMgtController
	 * 3. Comment   : 접근 아이피 등록
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 9:03:17
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/access/insertAccessMgt.do", "/admin/access/{sys}/insertAccessMgt.do" })
	public String insertAccessMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/access/" + sys;
		} else {
			returnUrl = "/admin/access";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());
		model.addAttribute("param", param);

		// 아이피 조회
		DataMap resultMap = accessMgtService.selectAccess(param);

		// 아이피가 존재하는경우
		if(resultMap != null){
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.duple.ip"));
			return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/insertFormAccessMgt.do", param, model);
		}

		accessMgtService.insertAccess(param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListAccessMgt.do", null, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectAccessMgt
	 * 2. ClassName  : AccessMgtController
	 * 3. Comment   : 접근 아이피 조회
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 9:54:17
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/access/selectAccessMgt.do", "/admin/access/{sys}/selectAccessMgt.do" })
	public String selectAccessMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		DataMap resultMap = accessMgtService.selectAccess(param);

		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/access/selectAccessMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateFormAccess
	 * 2. ClassName  : AccessMgtController
	 * 3. Comment   : 접근 아이피 수정폼
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 10:04:01
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= {"/admin/access/updateFormAccessMgt.do", "/admin/access/{sys}/updateFormAccessMgt.do"})
	public String updateFormAccess(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultMap = accessMgtService.selectAccess(param);

		model.addAttribute("param", param);
		model.addAttribute("resultMap", resultMap);

		return "admin/access/updateFormAccessMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateAccessMgt
	 * 2. ClassName  : AccessMgtController
	 * 3. Comment   : 접근 아이피 수정
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 10:37:57
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/access/updateAccessMgt.do", "/admin/access/{sys}/updateAccessMgt.do" })
	public String updateAccessMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/access/" + sys;
		} else {
			returnUrl = "/admin/access";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		// 아이피 조회
		DataMap p = new DataMap();
		p.put("ip", param.getString("up_ip"));
		DataMap resultMap = accessMgtService.selectAccess(p);

		// 아이피가 존재하는경우
		if(resultMap != null){
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.duple.ip"));
		} else {
			accessMgtService.updateAccess(param);
			MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));
		}

		// redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("ip", param.getString("up_ip"));
		resultParam.put("currentPage", param.getString("currentPage"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectAccessMgt.do", resultParam, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteAccess
	 * 2. ClassName  : AccessMgtController
	 * 3. Comment   : 접근 아이피 삭제
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 10:56:08
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= {"/admin/access/deleteAccessMgt.do", "/admin/access/{sys}/deleteAccessMgt.do"})
	public String deleteAccessMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/access/" + sys;
		} else {
			returnUrl = "/admin/access";
		}

		param.put("user_sttus_code_stop",Const.USER_STTUS_CODE_OUT);
		accessMgtService.deleteAccess(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListAccessMgt.do", null, model);
	}
}
