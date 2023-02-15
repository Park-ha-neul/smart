package egovframework.admin.popup.web;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.admin.author.web.AuthorMgtController;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.popup.service.PopupMgtService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.TransReturnUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : PopupMgtController.java
 * 3. Package  : egovframework.smp.popup.web
 * 4. Comment  : 팝업
 * 5. 작성자   : 박재현
 * 6. 작성일   : 2015. 9. 7. 오전 9:51:57
 * </PRE>
 */
@Controller
public class PopupMgtController {

	private static Log log = LogFactory.getLog(AuthorMgtController.class);

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

    @Resource(name = "popupMgtService")
    private PopupMgtService popupMgtService;

    @Resource(name="NtsysFileMngUtil")
    private NtsysFileMngUtil ntsysFileMngUtil;

	@Resource(name="NtsysFileMngService")
    private NtsysFileMngService ntsysFileMngService;

	/**
	 * <PRE>
	 * 1. MethodName : selectPopupMgtPageList
	 * 2. ClassName  : PopupMgtMgtController
	 * 3. Comment   : 팝업 목록
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 9:52:03
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/popup/selectPageListPopupMgt.do", "/admin/popup/{sys}/selectPageListPopupMgt.do" })
	public String selectPageListPopupMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		param.put("group_id_popup_se", Const.UPCODE_POPUP_SE);
		param.put("group_id", Const.UPCODE_SYS_CODE); // 시스템 코드
		param.put("sch_disp_begin_de_tmp", param.getString("sch_disp_begin_de").replaceAll("\\.", ""));
		param.put("sch_disp_end_de_tmp", param.getString("sch_disp_end_de").replaceAll("\\.", ""));

		/* ### Pasing 시작 ### */
		int totCnt = popupMgtService.selectTotCntPopupMgt(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = popupMgtService.selectPageListPopupMgt(param);
		/* ### Pasing 끝 ### */

        model.addAttribute("resultList", resultList);
        model.addAttribute("param", param);

		return "admin/popup/selectPageListPopupMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPopupMgt
	 * 2. ClassName  : PopupMgtMgtController
	 * 3. Comment   : 팝업 상세 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 10:12:17
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/popup/selectPopupMgt.do", "/admin/popup/{sys}/selectPopupMgt.do" })
	public String selectPopupMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		param.put("group_id", Const.UPCODE_SYS_CODE); // 시스템 코드
		param.put("group_id_popup_se", Const.UPCODE_POPUP_SE);

		DataMap resultMap = popupMgtService.selectPopupMgt(param);

		// #### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(resultMap.getString("IMAGE_DOC_ID"));
		List<NtsysFileVO> popupImgFileList = ntsysFileMngService.selectFileInfs(fvo);
		// #### FILE LIST 검색 End ####
		model.addAttribute("popupImgFileList", popupImgFileList);

		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/popup/selectPopupMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : deletePopupMgt
	 * 2. ClassName  : PopupMgtMgtController
	 * 3. Comment   : 팝업 삭제
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 11:33:51
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/popup/deletePopupMgt.do", "/admin/popup/{sys}/deletePopupMgt.do" })
	public String deletePopupMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/popup/" + sys;
		} else {
			returnUrl = "/admin/popup";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		DataMap param = RequestUtil.getDataMap(request);

		param.put("ss_user_no", userInfoVo.getUserNo());
		model.addAttribute("param", param);

		popupMgtService.deletePopupMgt(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListPopupMgt.do", null, model);
	}


	/**
	 * <PRE>
	 * 1. MethodName : insertFormPopupMgt
	 * 2. ClassName  : PopupMgtController
	 * 3. Comment   : 팝업등록 폼 이동
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 14. 오전 11:17:16
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/popup/insertFormPopupMgt.do", "/admin/popup/{sys}/insertFormPopupMgt.do" })
	public String insertFormPopupMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sys_code", sys);
		}

		model.addAttribute("param", param);

		return  "admin/popup/insertFormPopupMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : insertPopupMgt
	 * 2. ClassName  : PopupMgtController
	 * 3. Comment   : 팝업 입력
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 11. 오후 5:08:57
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/popup/insertPopupMgt.do", "/admin/popup/{sys}/insertPopupMgt.do" })
	public String insertPopupMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/popup/" + sys;
		} else {
			returnUrl = "/admin/popup";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		param.put("disp_begin_de", param.getString("disp_begin_de").replaceAll("\\.", "").replaceAll(" ", "").replaceAll("\\:", ""));
		param.put("disp_end_de", param.getString("disp_end_de").replaceAll("\\.", "").replaceAll(" ", "").replaceAll("\\:", ""));

		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		//이미지 파일 확장자 체크
		String msg = ntsysFileMngUtil.checkFileExt(fileList, "JPG,JPEG,GIF,PNG", "Y");
		if(msg != ""){
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.img.not.access", new String[] { msg }));
			return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/insertFormPopupMgt.do", param, model);
		}

		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			MessageUtil.setMessage(request, egovMessageSource.getMessage(
					"error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize")) }));
			return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/insertFormPopupMgt.do", param, model);
		}

		popupMgtService.insertPopupMgt(param, fileList);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListPopupMgt.do", null, model);
	}


	/**
	 * <PRE>
	 * 1. MethodName : updateFormPopupMgt
	 * 2. ClassName  : PopupMgtController
	 * 3. Comment   : 팝업 수정폼
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 14. 오후 1:20:03
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/popup/updateFormPopupMgt.do", "/admin/popup/{sys}/updateFormPopupMgt.do" })
	public String updateFormPopupMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		param.put("group_id_popup_se", Const.UPCODE_POPUP_SE);
		param.put("group_id", Const.UPCODE_SYS_CODE); // 시스템 코드
		DataMap resultMap = popupMgtService.selectPopupMgt(param);

		// #### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(resultMap.getString("IMAGE_DOC_ID"));
		List<NtsysFileVO> popupImgFileList = ntsysFileMngService.selectFileInfs(fvo);
		// #### FILE LIST 검색 End ####
		model.addAttribute("popupImgFileList", popupImgFileList);

		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/popup/updateFormPopupMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : updatePopupMgt
	 * 2. ClassName  : PopupMgtController
	 * 3. Comment   : 팝업 수정
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 11. 오후 5:09:05
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/popup/updatePopupMgt.do", "/admin/popup/{sys}/updatePopupMgt.do" })
	public String updatePopupMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/popup/" + sys;
		} else {
			returnUrl = "/admin/popup";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		param.put("disp_begin_de", param.getString("disp_begin_de").replaceAll("\\.", "").replaceAll(" ", "").replaceAll("\\:", ""));
		param.put("disp_end_de", param.getString("disp_end_de").replaceAll("\\.", "").replaceAll(" ", "").replaceAll("\\:", ""));

		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
		// 이미지 파일 확장자 체크
		String msg = ntsysFileMngUtil.checkFileExt(fileList, "JPG,JPEG,GIF,PNG", "Y");
		if (msg != "") {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.img.not.access", new String[] { msg }));
			return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/updateFormPopupMgt.do", param, model);
		}

		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize")) }));
			return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/updateFormPopupMgt.do", param, model);
		}

		popupMgtService.updatePopupMgt(param, fileList);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		// redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("group_id", Const.UPCODE_SYS_CODE);
		resultParam.put("group_id_popup_se", Const.UPCODE_POPUP_SE);
		resultParam.put("popup_seq", param.getString("popup_seq"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPopupMgt.do", resultParam, model);
	}
}
