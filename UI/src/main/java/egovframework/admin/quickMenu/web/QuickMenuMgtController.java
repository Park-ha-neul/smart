package egovframework.admin.quickMenu.web;

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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.quickMenu.service.QuickMenuMgtService;
import egovframework.admin.quickMenu.vo.QuickMenuImgFileVo;
import egovframework.common.service.CodeCacheService;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.TransReturnUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

@Controller
public class QuickMenuMgtController {
	private static Log log = LogFactory.getLog(QuickMenuMgtController.class);

	/** egovMessageSource */
	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

	/** quickMenuMgtService */
    @Resource(name = "quickMenuMgtService")
    private QuickMenuMgtService quickMenuMgtService;

    /** NtsysFileMngService */
    @Resource(name="NtsysFileMngService")
    private NtsysFileMngService ntsysFileMngService;

    /** NtsysFileMngUtil */
    @Resource(name="NtsysFileMngUtil")
    private NtsysFileMngUtil ntsysFileMngUtil;

    /** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectPageListQuickMenuMgt
	 * 2. ClassName  : QuickMenuMgtController
	 * 3. Comment   : 퀵메뉴 목록 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:25:09
	 * </PRE>
	 *   @return String
	 *   @param sys
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/quickMenu/selectPageListQuickMenuMgt.do", "/admin/quickMenu/{sys}/selectPageListQuickMenuMgt.do" })
    public String selectPageListQuickMenuMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		log.debug("####" + this.getClass().getName() + " START ####");

    	DataMap param = RequestUtil.getDataMap(request);

    	if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

    	param.put("sys_group_id", Const.UPCODE_SYS_CODE);
		param.put("zone_group_id", Const.UPCODE_QUICK_ZONE);

		//퀵메뉴 구역 코드 조회
		List zoneComboStr = CodeCacheService.getCode(Const.UPCODE_QUICK_ZONE);
		model.addAttribute("zoneComboStr", zoneComboStr);

    	/* ### Pasing 시작 ### */
    	int totCnt = quickMenuMgtService.selectTotCntQuickMenu(param);
    	param.put("totalCount", totCnt);
    	param = pageNavigationUtil.createNavigationInfo(model, param);
        List resultList = quickMenuMgtService.selectPageListQuickMenu(param);
        /* ### Pasing 끝 ### */

        model.addAttribute("totalCount", totCnt);
        model.addAttribute("resultList", resultList);
        model.addAttribute("param", param);

        return "admin/quickMenu/selectPageListQuickMenuMgt";
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectQuickMenuMgt
	 * 2. ClassName  : QuickMenuMgtController
	 * 3. Comment   : 퀵메뉴 상세 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:30:20
	 * </PRE>
	 *   @return String
	 *   @param sys
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/quickMenu/selectQuickMenuMgt.do", "/admin/quickMenu/{sys}/selectQuickMenuMgt.do" })
    public String selectQuickMenuMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	log.debug("####" + this.getClass().getName() + " START ####");

    	DataMap param = RequestUtil.getDataMap(request);

    	if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		param.put("sys_group_id", Const.UPCODE_SYS_CODE);
		param.put("zone_group_id", Const.UPCODE_QUICK_ZONE);

    	DataMap resultMap = quickMenuMgtService.selectQuickMenu(param);

    	//이미지 파일 정보 조회
    	QuickMenuImgFileVo ifvo = new QuickMenuImgFileVo();
    	List<QuickMenuImgFileVo> fileList = null;

		ifvo.setDocId(resultMap.getString("WEB_IMAGE_DOC_ID"));
		fileList = quickMenuMgtService.selectQuickMenuImgs(ifvo);
		model.addAttribute("webImageList", fileList);

		ifvo.setDocId(resultMap.getString("MOBILE_IMAGE_DOC_ID"));
		fileList = quickMenuMgtService.selectQuickMenuImgs(ifvo);
		model.addAttribute("mobileImageList", fileList);

		System.out.println(ifvo.getFileRltvPath()+ifvo.getFileId()+"."+ifvo.getFileExtNm());

    	model.addAttribute("resultMap", resultMap);
        model.addAttribute("param", param);

        return "admin/quickMenu/selectQuickMenuMgt";
    }

	/**
	 *
	 * <PRE>
	 * 1. MethodName : insertFormQuickMenuMgt
	 * 2. ClassName  : QuickMenuMgtController
	 * 3. Comment   : 퀵메뉴 등록 폼
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 11. 오전 9:15:20
	 * </PRE>
	 *   @return String
	 *   @param sys
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/quickMenu/insertFormQuickMenuMgt.do", "/admin/quickMenu/{sys}/insertFormQuickMenuMgt.do" })
    public String insertFormQuickMenuMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	log.debug("####" + this.getClass().getName() + " START ####");

    	DataMap param = RequestUtil.getDataMap(request);

    	if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sys_code", sys);
		}

		// 퀵 메뉴 구역 코드 조회
		List zoneComboStr = CodeCacheService.getCode(Const.UPCODE_QUICK_ZONE);
		System.out.println("zoneComboStr : "+ zoneComboStr);
		model.addAttribute("zoneComboStr", zoneComboStr);

        model.addAttribute("param", param);

        return "admin/quickMenu/insertFormQuickMenuMgt";
    }

	/**
	 *
	 * <PRE>
	 * 1. MethodName : insertQuickMenuImageAjax
	 * 2. ClassName  : QuickMenuMgtController
	 * 3. Comment   : 퀵메뉴 등록
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 11. 오전 9:19:14
	 * </PRE>
	 *   @return void
	 *   @param sys
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/quickMenu/insertQuickMenuImageAjax.do", "/admin/quickMenu/{sys}/insertQuickMenuImageAjax.do" })
	public @ResponseBody DataMap insertQuickMenuImageAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		List fileList_w = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request, "upload_0");
		List fileList_m = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request, "upload_1");

		// return 상태
		DataMap resultJSON = new DataMap();
		DataMap resultStats = new DataMap();

		// 파일 확장자 체크
		String msg1 = ntsysFileMngUtil.checkFileExt(fileList_w);
		String msg2 = ntsysFileMngUtil.checkFileExt(fileList_m);

		if (msg1 != "") {
			resultStats.put("resultCode", "error");
			resultStats.put("resultMsg", egovMessageSource.getMessage("error.file.not.access", new String[] { msg1 }));
			resultJSON.put("resultStats", resultStats);
		} else if (msg2 != "") {
			resultStats.put("resultCode", "error");
			resultStats.put("resultMsg", egovMessageSource.getMessage("error.file.not.access", new String[] { msg2 }));
			resultJSON.put("resultStats", resultStats);
		} else {
			quickMenuMgtService.insertQuickMenu(param, fileList_w, fileList_m);

			MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));
			resultStats.put("resultCode", "ok");
			resultStats.put("resultMsg", "");
			resultJSON.put("resultStats", resultStats);// 리턴값 : 선택된 메뉴 정보
		}

		response.setContentType("text/html; charset=utf-8");


		return resultJSON;

	}

	 /**
	  *
	  * <PRE>
	  * 1. MethodName : updateFormQuickMenuMgt
	  * 2. ClassName  : QuickMenuMgtController
	  * 3. Comment   : 퀵메뉴 수정폼
	  * 4. 작성자    : Lee WooJin
	  * 5. 작성일    : 2020. 5. 11. 오전 9:26:58
	  * </PRE>
	  *   @return String
	  *   @param sys
	  *   @param request
	  *   @param response
	  *   @param model
	  *   @return
	  *   @throws Exception
	  */
	@RequestMapping(value = { "/admin/quickMenu/updateFormQuickMenuMgt.do", "/admin/quickMenu/{sys}/updateFormQuickMenuMgt.do" })
    public String updateFormQuickMenuMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	log.debug("####" + this.getClass().getName() + " START ####");

    	DataMap param = RequestUtil.getDataMap(request);

    	if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		// 퀵 메뉴 구역 코드 조회
		List zoneComboStr = CodeCacheService.getCode(Const.UPCODE_QUICK_ZONE);
		System.out.println("zoneComboStr : "+ zoneComboStr);
		model.addAttribute("zoneComboStr", zoneComboStr);

		param.put("sys_group_id", Const.UPCODE_SYS_CODE);
		param.put("zone_group_id", Const.UPCODE_QUICK_ZONE);
		DataMap resultMap = quickMenuMgtService.selectQuickMenu(param);

		//이미지 파일 정보 조회
    	QuickMenuImgFileVo ifvo = new QuickMenuImgFileVo();
    	List<QuickMenuImgFileVo> fileList = null;

		ifvo.setDocId(resultMap.getString("WEB_IMAGE_DOC_ID"));
		fileList = quickMenuMgtService.selectQuickMenuImgs(ifvo);
		model.addAttribute("webImageList", fileList);

		ifvo.setDocId(resultMap.getString("MOBILE_IMAGE_DOC_ID"));
		fileList = quickMenuMgtService.selectQuickMenuImgs(ifvo);
		model.addAttribute("mobileImageList", fileList);

    	model.addAttribute("resultMap", resultMap);
        model.addAttribute("param", param);

        return "admin/quickMenu/updateFormQuickMenuMgt";
    }

	/**
	 *
	 * <PRE>
	 * 1. MethodName : updateQuickMenuImageAjax
	 * 2. ClassName  : QuickMenuMgtController
	 * 3. Comment   : 퀵메뉴 수정
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 11. 오전 9:28:25
	 * </PRE>
	 *   @return void
	 *   @param sys
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
    @RequestMapping(value = { "/admin/quickMenu/updateQuickMenuImageAjax.do", "/admin/quickMenu/{sys}/updateQuickMenuImageAjax.do" })
	public @ResponseBody DataMap updateQuickMenuImageAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	//시스템별 returnUrl 설정
    	String returnUrl = "";
    	if(sys != null) {
    	    returnUrl = "/admin/quickMenu/"+ sys;
    	}else {
    	    returnUrl = "/admin/quickMenu";
    	}

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		List fileList_w = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request, "upload_0");
		List fileList_m = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request, "upload_1");

		// return 상태
    	DataMap resultJSON = new DataMap();
		DataMap resultStats = new DataMap();

		// 파일 확장자 체크
		String msg1 = ntsysFileMngUtil.checkFileExt(fileList_w);
		String msg2 = ntsysFileMngUtil.checkFileExt(fileList_m);

		if (msg1 != "") {
			resultStats.put("resultCode", "error");
			resultStats.put("resultMsg", egovMessageSource.getMessage("error.file.not.access", new String[] { msg1 }));
			resultJSON.put("resultStats", resultStats);
		} else if (msg2 != "") {
			resultStats.put("resultCode", "error");
			resultStats.put("resultMsg", egovMessageSource.getMessage("error.file.not.access", new String[] { msg2 }));
			resultJSON.put("resultStats", resultStats);
		} else {
			quickMenuMgtService.updateQuickMenu(param, fileList_w, fileList_m);

			MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));
			resultStats.put("resultCode", "ok");
			resultStats.put("resultMsg", "");
			resultJSON.put("resultStats", resultStats);// 리턴값 : 선택된 메뉴 정보
		}

		DataMap resultParam = new DataMap();

		DataMap redirectParam = new DataMap();
		redirectParam.put("quickMenu_seq", param.getString("quickMenu_seq"));

		resultParam.put("returnParam", SysUtil.createUrlParam(redirectParam));
		resultParam.put("returnUrl", returnUrl + "/selectQuickMenuMgt.do");
		resultJSON.put("resultParam", resultParam);
		response.setContentType("text/html; charset=utf-8");

		return resultJSON;

	}

    /**
     *
     * <PRE>
     * 1. MethodName : deleteQuickMenuMgt
     * 2. ClassName  : QuickMenuMgtController
     * 3. Comment   : 퀵메뉴 삭제
     * 4. 작성자    : Lee WooJin
     * 5. 작성일    : 2020. 5. 11. 오전 9:29:47
     * </PRE>
     *   @return String
     *   @param sys
     *   @param request
     *   @param response
     *   @param model
     *   @param status
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value = { "/admin/quickMenu/deleteQuickMenuMgt.do", "/admin/quickMenu/{sys}/deleteQuickMenuMgt.do" })
    public String deleteQuickMenuMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

    	log.debug("####" + this.getClass().getName() + " START ####");

    	DataMap param = RequestUtil.getDataMap(request);

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/quickMenu/" + sys;
		} else {
			returnUrl = "/admin/quickMenu";
		}

    	quickMenuMgtService.deleteQuickMenu(param);

        MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));
        status.setComplete();

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListQuickMenuMgt.do", null, model);

    }

    /**
     *
     * <PRE>
     * 1. MethodName : deleteQuickMenuFileAjax
     * 2. ClassName  : QuickMenuMgtController
     * 3. Comment   : 퀵메뉴 이미지 삭제
     * 4. 작성자    : Lee WooJin
     * 5. 작성일    : 2020. 5. 11. 오전 9:31:26
     * </PRE>
     *   @return void
     *   @param sys
     *   @param request
     *   @param response
     *   @param model
     *   @throws Exception
     */
    @RequestMapping(value = { "/admin/quickMenu/deleteQuickMenuFileAjax.do", "/admin/quickMenu/{sys}/deleteQuickMenuFileAjax.do" })
    public @ResponseBody DataMap deleteQuickMenuFileAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

    	NtsysFileVO fvo = new NtsysFileVO();
    	fvo.setFileId(param.getString("file_id"));

    	fvo = quickMenuMgtService.selectQuickMenuImg(fvo);

    	//return 상태
    	DataMap resultStats = new DataMap();
    	DataMap resultJSON = new DataMap();

    	boolean del_yn = false;

    	// d_type 설정이 안되어있으면 기본값
    	if(StringUtil.nvl(param.getString("d_type")).equals("")){
    		// 무조건 설정된 값으로
			param.put("d_type", EgovPropertiesUtil.getProperty("Globals.fileSaveType"));
		}

		// 원래 이미지, 확장자
		if (param.getString("d_type").equals("O")) {
			del_yn = ntsysFileMngUtil.deleteFile(fvo, "O");
		}
		// file_id, 원래 확장자
		else if (param.getString("d_type").equals("I")) {
			del_yn = ntsysFileMngUtil.deleteFile(fvo, "I");
		}
		// file_id, 변환 확장자
		else {
			del_yn = ntsysFileMngUtil.deleteFile(fvo, "C");
		}

		quickMenuMgtService.deleteQuickMenuImg(param);

		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", "삭제에 성공하였습니다.");
		resultJSON.put("resultStats", resultStats);

		response.setContentType("text/html; charset=utf-8");
		return resultJSON;

	}

}
