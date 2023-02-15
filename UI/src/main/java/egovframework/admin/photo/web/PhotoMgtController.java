package egovframework.admin.photo.web;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.admin.author.web.AuthorMgtController;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.photo.service.PhotoMgtService;
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
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.TransReturnUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : PhotoMgtController.java
 * 3. Package  : egovframework.smp.photo.web
 * 4. Comment  : 사진
 * 5. 작성자   : 박재현
 * 6. 작성일   : 2015. 9. 7. 오전 9:51:57
 * </PRE>
 */
@Controller
public class PhotoMgtController {

	private static Log log = LogFactory.getLog(AuthorMgtController.class);

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

    @Resource(name = "photoMgtService")
    private PhotoMgtService photoMgtService;

    @Resource(name="NtsysFileMngUtil")
    private NtsysFileMngUtil ntsysFileMngUtil;

    /** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	@Resource(name="NtsysFileMngService")
    private NtsysFileMngService ntsysFileMngService;

	/**
	 * <PRE>
	 * 1. MethodName : selectPhotoMgtPageList
	 * 2. ClassName  : PhotoMgtMgtController
	 * 3. Comment   : 사진 목록
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
	@RequestMapping(value = { "/admin/photo/selectPageListPhotoMgt.do", "/admin/photo/{sys}/selectPageListPhotoMgt.do" })
	public String selectPageListPhotoMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		param.put("sch_start_photo_potogrf_de_tmp", param.getString("sch_start_photo_potogrf_de").replaceAll("\\.", ""));
		param.put("sch_end_photo_potogrf_de_tmp", param.getString("sch_end_photo_potogrf_de").replaceAll("\\.", ""));

		/* ### Pasing 시작 ### */
		param.put("sys_group_id", Const.UPCODE_SYS_CODE); //시스템 구분 코드
		int totCnt = photoMgtService.selectTotCntPhotoMgt(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = photoMgtService.selectPageListPhotoMgt(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "admin/photo/selectPageListPhotoMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPhotoMgt
	 * 2. ClassName  : PhotoMgtMgtController
	 * 3. Comment   : 사진 상세 조회
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
	@RequestMapping(value = { "/admin/photo/selectPhotoMgt.do", "/admin/photo/{sys}/selectPhotoMgt.do" })
	public String selectPhotoMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		param.put("sys_group_id", Const.UPCODE_SYS_CODE); //시스템 구분 코드
		DataMap resultMap = photoMgtService.selectPhotoGroup(param);

		param.put("doc_id", resultMap.getString("IMAGE_DOC_ID"));
		// #### FILE LIST 검색 Start ####
		List photoImgFileList = photoMgtService.selectListPhotoFiles(param);
		// #### FILE LIST 검색 End ####
		model.addAttribute("photoImgFileList", photoImgFileList);

		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/photo/selectPhotoMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertFormPhotoMgt
	 * 2. ClassName  : PhotoMgtController
	 * 3. Comment   : 사진등록 폼 이동
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
	@RequestMapping(value = { "/admin/photo/insertFormPhotoMgt.do", "/admin/photo/{sys}/insertFormPhotoMgt.do" })
	public String insertFormPhotoMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sys_code", sys);
		}

		model.addAttribute("param", param);

		return "admin/photo/insertFormPhotoMgt";
	}



	/**
	 * <PRE>
	 * 1. MethodName : insertPhotoImageAjax
	 * 2. ClassName  : PhotoMgtController
	 * 3. Comment   : 파일 업로드 Ajax
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 17. 오전 8:50:18
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/photo/insertPhotoImageAjax.do", "/admin/photo/{sys}/insertPhotoImageAjax.do" })
	public @ResponseBody DataMap insertPhotoImageAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		param.put("photo_potogrf_de", param.getString("photo_potogrf_de").replaceAll("\\.", "").replaceAll(" ", ""));

		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		photoMgtService.insertPhotoImage(param, fileList);

		DataMap resultJSON = new DataMap();
		resultJSON.put("image_doc_id", param.getString("image_doc_id"));


		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);//리턴값 : 선택된 메뉴 정보

		return resultJSON;
	}


	@RequestMapping(value = { "/admin/photo/updatePhotoImageAjax.do", "/admin/photo/{sys}/updatePhotoImageAjax.do" })
	public @ResponseBody DataMap updatePhotoImageAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		//시스템별 returnUrl 설정
		String returnUrl = "";
		if(sys != null) {
		    returnUrl = "/admin/photo/"+ sys;
		}else {
		    returnUrl = "/admin/photo";
		}

		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultMap = photoMgtService.selectPhotoGroup(param);
		param.put("doc_id", resultMap.getString("IMAGE_DOC_ID"));

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		param.put("photo_potogrf_de", param.getString("photo_potogrf_de").replaceAll("\\.", "").replaceAll(" ", ""));

		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		photoMgtService.updatePhotoImage(param, fileList);

		DataMap resultJSON = new DataMap();

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);//리턴값 : 선택된 메뉴 정보

		DataMap resultParam = new DataMap();

		resultParam.put("photo_seq", param.getString("photo_seq"));

		String returnParam = SysUtil.createUrlParam(resultParam);

		resultParam.put("returnUrl", returnUrl + "/selectPhotoMgt.do");

		resultParam.put("returnParam", returnParam);

		resultJSON.put("resultParam", resultParam);

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertPhotoMgt
	 * 2. ClassName  : PhotoMgtController
	 * 3. Comment   : 사진 입력
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
	@RequestMapping(value = { "/admin/photo/insertPhotoMgt.do", "/admin/photo/{sys}/insertPhotoMgt.do" })
	public String insertPhotoMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	//시스템별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/photo/" + sys;
		} else {
			returnUrl = "/admin/photo/";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		param.put("dspy_begin_de", param.getString("dspy_begin_de").replaceAll("\\.", "").replaceAll(" ", "").replaceAll("\\:", ""));
		param.put("dspy_end_de", param.getString("dspy_end_de").replaceAll("\\.", "").replaceAll(" ", "").replaceAll("\\:", ""));


		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
		log.debug("################:"+fileList.size());

		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize")) }));

			return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/insertFormPhotoMgt.do", param, model);
		}


		model.addAttribute("param", param);

		photoMgtService.insertPhotoMgt(param, fileList);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListPhotoMgt.do", null, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateFormPhotoMgt
	 * 2. ClassName  : PhotoMgtController
	 * 3. Comment   : 사진 수정폼
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
	@RequestMapping(value = { "/admin/photo/updateFormPhotoMgt.do", "/admin/photo/{sys}/updateFormPhotoMgt.do" })
	public String updateFormPhotoMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		param.put("sys_group_id", Const.UPCODE_SYS_CODE); //시스템 구분 코드
		DataMap resultMap = photoMgtService.selectPhotoGroup(param);

		// #### FILE LIST 검색 Start ####
		param.put("doc_id", resultMap.getString("IMAGE_DOC_ID"));
		// #### FILE LIST 검색 Start ####
		List photoImgFileList = photoMgtService.selectListPhotoFiles(param);
		// #### FILE LIST 검색 End ####
		model.addAttribute("photoImgFileList", photoImgFileList);

		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/photo/updateFormPhotoMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : updatePhotoMgt
	 * 2. ClassName  : PhotoMgtController
	 * 3. Comment   : 사진 수정
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
	@RequestMapping(value = { "/admin/photo/updatePhotoMgt.do", "/admin/photo/{sys}/updatePhotoMgt.do" })
	public String updatePhotoMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	//시스템별 returnUrl 설정
		String returnUrl = "";
		if(sys != null) {
			returnUrl = "/admin/photo/" + sys;
		}else {
			returnUrl = "/admin/photo";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		param.put("dspy_begin_de", param.getString("dspy_begin_de").replaceAll("\\.", "").replaceAll(" ", "").replaceAll("\\:", ""));
		param.put("dspy_end_de", param.getString("dspy_end_de").replaceAll("\\.", "").replaceAll(" ", "").replaceAll("\\:", ""));

		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize")) }));
			return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/updateFormPhotoMgt.do", param, model);
		}

		model.addAttribute("param", param);

		photoMgtService.updatePhotoMgt(param, fileList);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPhotoMgt.do", param, model);

	}

	/**
	 * <PRE>
	 * 1. MethodName : deletePhotoMgt
	 * 2. ClassName  : PhotoMgtMgtController
	 * 3. Comment   : 사진 삭제
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
	@RequestMapping(value = { "/admin/photo/deletePhotoMgt.do", "/admin/photo/{sys}/deletePhotoMgt.do" })
	public String deletePhotoMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

    	//시스템별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/photo/"+ sys;
        } else {
        	returnUrl = "/admin/photo";
        }

		DataMap param = RequestUtil.getDataMap(request);

		param.put("ss_user_no", userInfoVo.getUserNo());
		model.addAttribute("param", param);

		photoMgtService.deletePhotoMgt(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListPhotoMgt.do", null, model);

	}

}
