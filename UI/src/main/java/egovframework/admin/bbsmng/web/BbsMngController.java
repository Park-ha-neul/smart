package egovframework.admin.bbsmng.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import egovframework.admin.bbsmng.service.BbsMngService;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.user.service.UserMgtService;
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
import egovframework.framework.common.util.TransReturnUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BbsMngController.java
 * 3. Package  : egovframework.admin.bbsmng.web
 * 4. Comment  :
 * 5. 작성자   : mk
 * 6. 작성일   : 2018. 7. 16. 오후 2:34:17
 * </PRE>
 */
@Controller
public class BbsMngController {

	private static Log log = LogFactory.getLog(BbsMngController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "bbsMngService")
	private BbsMngService bbsMngService;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListBbsMgt
	 * 2. ClassName  : BbsMngController
	 * 3. Comment   : 게시판 관리 목록
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 16. 오후 2:34:20
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/bbsmng/selectPageListBbsMng.do", "/admin/bbsmng/{sys}/selectPageListBbsMng.do" })
	public String selectPageListBbsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		param.put("group_id", Const.UPCODE_SYS_CODE); //시스템 구분 코드
		param.put("bbs_ty_group_id", Const.UPCODE_BBS_TY); //게시판 유형 코드

		/* ### Pasing 시작 ### */
		int totCnt = bbsMngService.selectTotCntBbsMng(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = bbsMngService.selectPageListBbsMng(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "admin/bbsmng/selectPageListBbsMng";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMng
	 * 2. ClassName  : BbsMngController
	 * 3. Comment   : 게시판 관리 상세
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 16. 오후 2:34:27
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/bbsmng/selectBbsMng.do", "/admin/bbsmng/{sys}/selectBbsMng.do" })
	public String selectBbsMng(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		param.put("group_id", Const.UPCODE_SYS_CODE);//시스템 구분 코드
		param.put("bbs_ty_group_id", Const.UPCODE_BBS_TY); //게시판 유형 코드
		param.put("yn_group_id", Const.UPCODE_USE_YN); //여부 코드
		param.put("bbs_cntnts_position_group_id", Const.UPCODE_BBS_CNTNTS_POSITION); // 일반게시판 컨텐츠 위치 코드

		DataMap resultMap = bbsMngService.selectBbsMng(param);
		int bbsCodeCnt = bbsMngService.selectTotCntBbsCode(param);

		if(bbsCodeCnt > 0){
			param.put("showDelBtn", "N");
		}else{
			param.put("showDelBtn", "Y");
		}

		model.addAttribute("param", param);
		model.addAttribute("resultMap", resultMap);

		return "admin/bbsmng/selectBbsMng";
	}


	/**
	 * <PRE>
	 * 1. MethodName : inserFormBbsMng
	 * 2. ClassName  : BbsMngController
	 * 3. Comment   : 게시판 관리 등록폼
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 16. 오후 2:34:34
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/bbsmng/insertFormBbsMng.do", "/admin/bbsmng/{sys}/insertFormBbsMng.do" })
	public String insertFormBbsMng(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sys_code", sys);
		}

		// 권한 리스트 조회
		List authList = bbsMngService.selectListAuthor(param);
		model.addAttribute("authList", authList);
		model.addAttribute("param", param);

		return "admin/bbsmng/insertFormBbsMng";
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertBbsMng
	 * 2. ClassName  : BbsMngController
	 * 3. Comment   : 게시판 관리 등록
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 16. 오후 2:34:41
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/bbsmng/insertBbsMng.do", "/admin/bbsmng/{sys}/insertBbsMng.do" })
	public String insertBbsMng(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if(sys != null) {
			returnUrl = "/admin/bbsmng/" + sys;
		}else {
			returnUrl = "/admin/bbsmng";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		/* ### 이미지 정보 조회 시작 ### */
    	List fileList = new ArrayList();
    	String[] imgPathList = request.getParameterValues("imgPath");
    	param.put("imgPathList", imgPathList);
    	param.put("last_ver", '0');
    	String dftFilePath = EgovPropertiesUtil.getProperty("Globals.fileImgTempPath");
    	if(!(imgPathList== null))
    	fileList = fileListSet(imgPathList, dftFilePath);
		/* ### 이미지 정보 조회 끝 ### */

		bbsMngService.insertBbsMng(param);

		//메뉴테이블에 bbs_code update
		param.put("url_ty_code", "10");
		bbsMngService.updateMenuBbsCode(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListBbsMng.do", null, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateFormBbsMng
	 * 2. ClassName  : BbsMngController
	 * 3. Comment   : 게시판 관리 수정폼
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 16. 오후 2:34:48
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbsmng/updateFormBbsMng.do", "/admin/bbsmng/{sys}/updateFormBbsMng.do" })
	public String updateFormBbsMng(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		// 권한 리스트 조회
		List authList = bbsMngService.selectListAuthor(param);
		model.addAttribute("authList", authList);

		param.put("group_id", Const.UPCODE_SYS_CODE);//시스템 구분 코드
		param.put("bbs_ty_group_id", Const.UPCODE_BBS_TY); //게시판 유형 코드
		param.put("yn_group_id", Const.UPCODE_USE_YN); //여부 코드
		param.put("bbs_cntnts_position_group_id", Const.UPCODE_BBS_CNTNTS_POSITION); // 일반게시판 컨텐츠 위치 코드
		DataMap resultMap = bbsMngService.selectBbsMng(param);

		int bbsCodeCnt = bbsMngService.selectTotCntBbsCode(param);

		if(bbsCodeCnt > 0){
			param.put("existBbs", "Y");
		}else{
			param.put("existBbs", "N");
		}

		model.addAttribute("param", param);
		model.addAttribute("resultMap", resultMap);

		return "admin/bbsmng/updateFormBbsMng";
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateBbsMng
	 * 2. ClassName  : BbsMngController
	 * 3. Comment   : 게시판 관리 수정
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 16. 오후 2:34:55
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= {"/admin/bbsmng/updateBbsMng.do", "/admin/bbsmng/{sys}/updateBbsMng.do" })
	public String updateBbsMng(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if(sys != null) {
			returnUrl = "/admin/bbsmng/" + sys;
		}else {
			returnUrl = "/admin/bbsmng";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		/* ### 이미지 정보 조회 시작 ### */
    	List fileList = new ArrayList();
    	String[] imgPathList = request.getParameterValues("imgPath");
    	param.put("imgPathList", imgPathList);
    	param.put("last_ver", '0');
    	String dftFilePath = EgovPropertiesUtil.getProperty("Globals.fileImgTempPath");
    	if(!(imgPathList== null))
    	fileList = fileListSet(imgPathList, dftFilePath);
		/* ### 이미지 정보 조회 끝 ### */

		bbsMngService.updateBbsMng(param);

		//메뉴테이블에 bbs_code 삭제
		bbsMngService.deleteMenuBbsCode(param);

		//메뉴테이블에 bbs_code update
		param.put("url_ty_code", "10");
		bbsMngService.updateMenuBbsCode(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		// redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("group_id", Const.UPCODE_SYS_CODE);//시스템 구분 코드
		resultParam.put("bbs_ty_group_id", Const.UPCODE_BBS_TY); //게시판 유형 코드
		resultParam.put("yn_group_id", Const.UPCODE_USE_YN); //여부 코드
		resultParam.put("bbs_cntnts_position_group_id", Const.UPCODE_BBS_CNTNTS_POSITION); // 일반게시판 컨텐츠 위치 코드
		resultParam.put("bbs_code", param.get("bbs_code"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectBbsMng.do", resultParam, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteBbsMng
	 * 2. ClassName  : BbsMngController
	 * 3. Comment   : 게시판 관리 삭제
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 16. 오후 2:35:10
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbsmng/deleteBbsMng.do", "/admin/bbsmng/{sys}/deleteBbsMng.do" })
	public String deleteBbsMng(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if(sys != null) {
			returnUrl = "/admin/bbsmng/" + sys;
		}else {
			returnUrl = "/admin/bbsmng";
		}

		DataMap param = RequestUtil.getDataMap(request);

		//메뉴테이블에 bbs_code 삭제
		param.put("before_menu_id",param.getString("menu_id"));
		bbsMngService.deleteMenuBbsCode(param);

		bbsMngService.deleteBbsMng(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		// redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("sch_row_per_page", param.getString("sch_row_per_page"));
		resultParam.put("sch_sys_code", param.getString("sch_sys_code"));
		resultParam.put("sch_bbs_ty_code", param.getString("sch_bbs_ty_code"));
		resultParam.put("sch_use_yn", param.getString("sch_use_yn"));
		resultParam.put("sch_text", param.getString("sch_text"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListBbsMng.do", resultParam, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectExistYnBbsCodeAjax
	 * 2. ClassName  : BbsMngController
	 * 3. Comment   : 게시판 관리 bbs_code 중복체크
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 16. 오후 2:35:00
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/bbsmng/selectExistYnBbsCodeAjax.do", "/admin/bbsmng/{sys}/selectExistYnBbsCodeAjax.do" })
	public @ResponseBody DataMap selectExistYnBbsCodeAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultMap = bbsMngService.selectExistYnBbsCode(param);// bbs_code 존재 여부 확인

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
	 * <PRE>
	 * 1. MethodName : selectBbsCodeSeqAjax
	 * 2. ClassName  : BbsMngController
	 * 3. Comment   : 게시판코드 seq조회
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 26. 오전 10:03:46
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/bbsmng/selectBbsCodeSeqAjax.do", "/admin/bbsmng/{sys}/selectBbsCodeSeqAjax.do" })
	public @ResponseBody DataMap selectBbsCodeSeqAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultMap = bbsMngService.selectBbsCodeSeq(param);

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultMap", resultMap);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	public List fileListSet(String[] imgPathList, String dftFilePath) {

		List fileList = new ArrayList();

		if(imgPathList.length > 0){

	    	for(String str : imgPathList){
	    		File file = new File(dftFilePath + str);
			    MultipartFile multipartFile = fileToMultipartFile(file);
			    fileList.add(multipartFile);
	    	}
		}
		return fileList;
	}

	public MultipartFile fileToMultipartFile(File file) {
	    FileItem fileItem = null;
	    try {
	        fileItem = new DiskFileItem(null, Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			OutputStream outputStream = fileItem.getOutputStream();
			IOUtils.copy(inputStream, outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

	    return multipartFile;
	}
}
