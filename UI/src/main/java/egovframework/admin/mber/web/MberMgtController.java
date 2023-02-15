package egovframework.admin.mber.web;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.mber.service.MberMgtService;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.ExcelTemplateUtil;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;

@Controller
public class MberMgtController {

	private static Log log = LogFactory.getLog(MberMgtController.class);

	@Resource(name="egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "mberMgtService")
	private MberMgtService mberMgtService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListMberMgt
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 사용자 리스트 조회
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
	@RequestMapping(value= {"/admin/mber/selectPageListMberMgt.do", "/admin/mber/{sys}/selectPageListMberMgt.do"})
	public String selectPageListMberMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		List resultList = mberMgtService.selectPageListMber(model, param);

		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "admin/mber/selectPageListMberMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertFormMberMgt
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 사용자 등록폼
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
	@RequestMapping(value= {"/admin/mber/insertFormMberMgt.do", "/admin/mber/{sys}/insertFormMberMgt.do"})
	public String insertFormMberMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap codeParam = new DataMap();

		// 사용자상태코드 조회
		codeParam.put("group_id", Const.UPCODE_USER_STTUS);
		List userSttusComboStr = commonCodeService.selectCodeList(codeParam);

		model.addAttribute("userSttusComboStr", userSttusComboStr);

		return "admin/mber/insertFormMberMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertMberMgt
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 사용자 등록
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
	@RequestMapping(value= {"/admin/mber/insertMberMgt.do", "/admin/mber/{sys}/insertMberMgt.do"})
	public String insertMberMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		mberMgtService.insertMber(param);
		model.addAttribute("param", param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		return "redirect:/admin/mber/selectPageListMberMgt.do";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectMberMgt
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 사용자 조회
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
	@RequestMapping(value= {"/admin/mber/selectMberMgt.do", "/admin/mber/{sys}/selectMberMgt.do"})
	public String selectMberMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultMap = mberMgtService.selectMber(param);

		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/mber/selectMberMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateFormMber
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 사용자 수정폼 조회
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
	@RequestMapping(value= {"/admin/mber/updateFormMberMgt.do", "/admin/mber/{sys}/updateFormMberMgt.do"})
	public String updateFormMber(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultMap = mberMgtService.selectMber(param);
		DataMap codeParam = new DataMap();

		// 사용자상태코드 조회
		codeParam.put("group_id", Const.UPCODE_USER_STTUS);
		List userSttusComboStr = commonCodeService.selectCodeList(codeParam);

		model.addAttribute("userSttusComboStr", userSttusComboStr);
		model.addAttribute("param", param);
		model.addAttribute("resultMap", resultMap);

		return "admin/mber/updateFormMberMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateMberMgt
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 사용자 수정
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
	@RequestMapping(value= {"/admin/mber/updateMberMgt.do", "/admin/mber/{sys}/updateMberMgt.do"})
	public String updateMberMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		mberMgtService.updateMber(param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		model.addAttribute("param", param);

		return "redirect:/admin/mber/selectMberMgt.do?user_no=" + param.getString("user_no") + "&sch_type=" + param.getString("sch_type") + "&sch_text=" + param.getString("sch_text") + "&currentPage=" + param.getString("currentPage");
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteMber
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 사용자 삭제
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
	@RequestMapping(value= {"/admin/mber/deleteMberMgt.do", "/admin/mber/{sys}/deleteMberMgt.do"})
	public String deleteMber(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		param.put("user_sttus_code_stop",Const.USER_STTUS_CODE_OUT);
		mberMgtService.deleteMber(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		return "redirect:/admin/mber/selectPageListMberMgt.do";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectIdExistYnAjax
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 사용자 존재 여부 확인
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 10:56:40
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/mber/selectIdExistYnAjax.do")
	public @ResponseBody DataMap selectIdExistYnAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		String resultMap = mberMgtService.selectMberExistYn(param);

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultMap", resultMap);

		String returnMsg = "";
		if(resultMap.equals("Y")){
			returnMsg = egovMessageSource.getMessage("error.duple.user");
		}
		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", returnMsg);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertExcelFormMber
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 엑셀 업로드 폼
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 11:16:21
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */

	@RequestMapping(value= {"/admin/mber/insertExcelFormMberMgt.do", "/admin/mber/{sys}/insertExcelFormMberMgt.do"})
	public String insertExcelFormMber(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		model.addAttribute("param", param);

		return "admin/mber/insertExcelFormMberMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectExcelMberAjax
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 엑셀 데이터 조회
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오후 1:47:16
	 * </PRE>
	 *   @return DataMap
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/mber/selectExcelMberMgtAjax.do")
	public @ResponseBody DataMap selectExcelMberAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);
		DataMap returnMap = new DataMap();
		String resultMsg = "";

		// 파일 객체 가져옴
		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		// 파일이 없는경우
		if(fileList.size() == 0){
			return RequestUtil.errorJsonMsg(response, egovMessageSource.getMessage("error.no.file"));
		}

		// 맵핑 맵 설정
		List<DataMap> result = new ArrayList<DataMap>();
		DataMap beans = new DataMap();
		beans.put("userList", result);

		ExcelTemplateUtil.readExcel(request, response, "user_config.xml", (MultipartFile)fileList.get(0), beans);

		List userList = new ArrayList();

		// 사용자가 존재하는지 확인
		for(int i = 0; i < beans.getList("userList").size(); i++){
			DataMap item = (DataMap)beans.getList("userList").get(i);

			// 핸드폰 번호 띄어쓰기, - 변경
			item.put("moblphon_no", StringUtil.nvl((String)item.get("moblphon_no")).replace("-", "").replace(" ", ""));
			// 중복 확인
			item.put("dupl_yn", mberMgtService.selectMberExistYn(item));

			// 리스트에 추가
			userList.add(item);
		}

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultList", userList);
		System.out.println("userList : " + userList);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", resultMsg);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertExcelMber
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 엑셀 사용자 등록
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오후 2:34:45
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= {"/admin/mber/insertExcelMberMgt.do", "/admin/mber/{sys}/insertExcelMberMgt.do"})
	public String insertExcelMber(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		// 파일 객체 가져옴
		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		// 파일이 없는경우
		if(fileList.size() == 0){
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.no.file"));
			return "redirect:/admin/mber/insertExcelFormMberMgt.do";
		}

		// 맵핑 맵 설정
		List<DataMap> result = new ArrayList<DataMap>();
		DataMap beans = new DataMap();
		beans.put("userList", result);

		ExcelTemplateUtil.readExcel(request, response, "user_config.xml", (MultipartFile)fileList.get(0), beans);

		// 사용자 등록
		mberMgtService.insertExcelMber(param, beans.getList("userList"));

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		model.addAttribute("param", param);

		return "redirect:/admin/mber/selectPageListMberMgt.do";
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteListMberMgt
	 * 2. ClassName  : MberMgtController
	 * 3. Comment   : 사용자 리스트 삭제
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 3. 18. 오후 2:40:17
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= {"/admin/mber/deleteListMberMgt.do", "/admin/mber/{sys}/deleteListMberMgt.do"})
	public String deleteListMberMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		mberMgtService.deleteListMber(param);
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		model.addAttribute("param", param);

		return "redirect:/admin/mber/selectPageListMberMgt.do";
	}
}
