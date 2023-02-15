package egovframework.admin.bbs.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ibm.icu.text.SimpleDateFormat;

import egovframework.admin.bbs.service.BbsMgtService;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.DateUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.TransReturnUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cryptography.EgovEnvCryptoService;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BbsMgtController.java
 * 3. Package  : egovframework.admin.bbs.web
 * 4. Comment  :
 * 5. 작성자   : mk
 * 6. 작성일   : 2018. 7. 23. 오전 11:58:37
 * </PRE>
 */
@Controller
public class BbsMgtController {

	private static Log log = LogFactory.getLog(BbsMgtController.class);

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

	/** bbsMgtService */
	@Resource(name = "bbsMgtService")
	private BbsMgtService bbsMgtService;

	@Resource(name="NtsysFileMngUtil")
    private NtsysFileMngUtil ntsysFileMngUtil;

	@Resource(name="NtsysFileMngService")
    private NtsysFileMngService ntsysFileMngService;

	/** 암호화서비스 */
	@Resource(name = "egovEnvCryptoService")
	EgovEnvCryptoService cryptoService;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListBbsMgt
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 목록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:58:26
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/selectPageListBbsMgt.do", "/admin/bbs/{sys}/selectPageListBbsMgt.do" })
	public String selectPageListBbsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_author_id", userInfoVo.getAuthorId());
		List authList = userInfoVo.getAuthorIdList();
		param.put("authList", authList);

		param.put("group_id", Const.UPCODE_SYS_CODE); //시스템 구분 코드
		param.put("bbs_ty_group_id", Const.UPCODE_BBS_TY); //게시판 유형 코드

		// 상세에서 목록으로 돌아왔을경우 검색 페이징번호가 있을때 currentPage에 셋팅함
		if(!param.getString("sch_currentPage").equals("")){
			param.put("currentPage", param.getString("sch_currentPage"));
		}

		/* ### Pasing 시작 ### */
		int totCnt = bbsMgtService.selectTotCntBbsMgt(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = bbsMgtService.selectPageListBbsMgt(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "admin/bbs/selectPageListBbsMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMgt
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 상세
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:58:41
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/selectBbsMgt.do", "/admin/bbs/{sys}/selectBbsMgt.do" })
    public String selectBbsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		String ss_author_id = "";

		for (int i = 0; i < userInfoVo.getAuthorIdList().size(); i++) {
			if (userInfoVo.getAuthorIdList().get(i).equals(Const.CODE_AUTHOR_ADMIN)) {
				ss_author_id = userInfoVo.getAuthorIdList().get(i).toString();
				break;
			}
			ss_author_id = userInfoVo.getAuthorIdList().get(i).toString();
		}

		param.put("ss_author_id", ss_author_id);

		param.put("group_id", Const.UPCODE_SYS_CODE);//시스템 구분 코드
		param.put("bbs_ty_group_id", Const.UPCODE_BBS_TY); //게시판 유형 코드
		param.put("cttpc_se_group_id",Const.UPCODE_CTTPC_SE); 	//연락처 구분 코드

		DataMap resultMap = bbsMgtService.selectBbsMgt(param);

		if(!resultMap.getString("REGISTER_CTTPC").equals("")) {
			resultMap.put("REGISTER_CTTPC", cryptoService.decrypt(resultMap.getString("REGISTER_CTTPC")));
		}

		// #### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(resultMap.getString("DOC_ID"));
		List<NtsysFileVO> atchFileList = ntsysFileMngService.selectFileInfs(fvo);
		// #### FILE LIST 검색 End ####
		model.addAttribute("atchFileList", atchFileList);

		// #### THUMB IMAGE FILE LIST 검색 Start ####
		NtsysFileVO thumbFvo = new NtsysFileVO();
		thumbFvo.setDocId(resultMap.getString("THUMB_DOC_ID"));
		List<NtsysFileVO> thumbImgFileList = ntsysFileMngService.selectFileInfs(thumbFvo);
		// #### THUMB IMAGE FILE LIST 검색 End ####
		model.addAttribute("thumbImgFileList", thumbImgFileList);

		resultMap.put("CN", resultMap.htmlTagFilterRestore(resultMap.getString("CN")));

		// 댓글 수정 삭제 등록의 동작이 아닌 리스트에서 상세 페이지에 들어왔을때 리스트의 페이징 번호를 sch_currentPage에 담아둠 댓글의
		// 페이징과 겹치지 않게 하기 위해 수정
		if(!param.getString("currentPage").equals("") && param.getString("sch_currentPage").equals("")){
			param.put("sch_currentPage", param.getString("currentPage"));
			param.put("currentPage","");
		}

		// 댓글 페이지번호가 있을시 currentPage에 담아서 댓글 페이지번호 유지
		if (!param.getString("ans_currentPage").equals("")) {
			param.put("currentPage", param.getString("ans_currentPage"));
		}

		//답변 조회
    	int ansTotCnt = bbsMgtService.selectTotCntBbsAns(param);
		param.put("totalCount", ansTotCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultAnsList = bbsMgtService.selectPageListBbsAns(param);

		DataMap ansMap = new DataMap();
		for (int k = 0; k < resultAnsList.size(); k++) {
			ansMap = (DataMap) resultAnsList.get(k);
			if (!(ansMap == null)) {
				// #### 답변 FILE LIST 검색 Start ####
				NtsysFileVO ansAtchFvo = new NtsysFileVO();
				ansAtchFvo.setDocId(ansMap.getString("DOC_ID"));
				List<NtsysFileVO> ansFileList = ntsysFileMngService.selectFileInfs(ansAtchFvo);
				// #### 답변 FILE LIST 검색 End ####
				ansMap.put("ansFileList", ansFileList);
			}
		}

        model.addAttribute("param", param);
        model.addAttribute("resultMap", resultMap);
        model.addAttribute("resultAnsList", resultAnsList);

        return "admin/bbs/selectBbsMgt";
    }

	/**
	 * <PRE>
	 * 1. MethodName : inserFormBbsMgt
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 입력폼
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:58:47
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/insertFormBbsMgt.do", "/admin/bbs/{sys}/insertFormBbsMgt.do" })
	public String inserFormBbsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		String ss_author_id = "";

		for (int i = 0; i < userInfoVo.getAuthorIdList().size(); i++) {
			if (userInfoVo.getAuthorIdList().get(i).equals(Const.CODE_AUTHOR_ADMIN)) {
				ss_author_id = userInfoVo.getAuthorIdList().get(i).toString();
				break;
			}
			ss_author_id = userInfoVo.getAuthorIdList().get(i).toString();
		}

		param.put("ss_author_id", ss_author_id);

		model.addAttribute("param", param);

		return "admin/bbs/insertFormBbsMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : insertBbsMgt
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 등록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:58:54
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/insertBbsMgt.do", "/admin/bbs/{sys}/insertBbsMgt.do" })
	public @ResponseBody DataMap insertBbsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		// 시스템별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/bbs/" + sys;
		} else {
			returnUrl = "/admin/bbs";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());
		param.put("ss_author_id", userInfoVo.getAuthorId());

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		DataMap resultJSON = new DataMap();
		DataMap resultStats = new DataMap();

		//이미지 파일
		List<MultipartFile> imgFileList = new ArrayList<MultipartFile>();
		String[] imgPathList = request.getParameterValues("imgPath");
		param.put("imgPathList", imgPathList);
		param.put("last_ver", '0');
		String dftFilePath = EgovPropertiesUtil.getProperty("Globals.fileImgTempPath");
		if(!(imgPathList== null)) {
			imgFileList = fileListSet(imgPathList, dftFilePath);
		}

		// 파일 객체 가져옴
		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
		// 파일 확장자 체크
		String msg = ntsysFileMngUtil.checkFileExt(fileList);

		if (msg != "") {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.not.access", new String[] { msg }));
			model.addAttribute("param", param);
			resultStats.put("resultCode", "error");
		} else if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.fileMaxSize")) }));
			model.addAttribute("param", param);
			resultStats.put("resultCode", "error");
		} else {
			resultStats.put("resultCode", "ok");
		}

		// 썸네일 파일 객체 가져옴
		List thumbFileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest) request, "thumb_file");
		// 썸네일 이미지 파일 확장자 체크
		String thumb_msg = ntsysFileMngUtil.checkFileExt(thumbFileList, "JPG,JPEG,GIF,PNG", "Y");
		if (thumb_msg != "") {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.img.not.access", new String[] { thumb_msg }));

			resultStats.put("resultCode", "error");
		}

		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.fileMaxSize")) }));
			resultStats.put("resultCode", "error");
		}

		bbsMgtService.insertBbsMgt(param, imgFileList, thumbFileList, fileList);

		model.addAttribute("param", param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);//리턴값 : 선택된 메뉴 정보

		response.setContentType("text/html; charset=utf-8");

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateFormBbsMgt
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 수정폼
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:58:59
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/updateFormBbsMgt.do", "/admin/bbs/{sys}/updateFormBbsMgt.do" })
    public String updateFormBbsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());
		param.put("ss_author_id", userInfoVo.getAuthorId());

		param.put("group_id", Const.UPCODE_SYS_CODE);//시스템 구분 코드
		param.put("bbs_ty_group_id", Const.UPCODE_BBS_TY); //게시판 유형 코드
		param.put("cttpc_se_group_id",Const.UPCODE_CTTPC_SE); 	//연락처 구분 코드

		DataMap resultMap = bbsMgtService.selectBbsMgt(param);

/*		//2021.03.10 추가
		if(!resultMap.getString("REGISTER_CTTPC").equals("")) {
			resultMap.put("REGISTER_CTTPC", cryptoService.decrypt(resultMap.getString("REGISTER_CTTPC")));
		}*/

		// #### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(resultMap.getString("DOC_ID"));
		List<NtsysFileVO> atchFileList = ntsysFileMngService.selectFileInfs(fvo);
		// #### FILE LIST 검색 End ####
		model.addAttribute("atchFileList", atchFileList);

		// #### THUMB IMAGE FILE LIST 검색 Start ####
		NtsysFileVO thumbFvo = new NtsysFileVO();
		thumbFvo.setDocId(resultMap.getString("THUMB_DOC_ID"));
		List<NtsysFileVO> thumbImgFileList = ntsysFileMngService.selectFileInfs(thumbFvo);
		// #### THUMB IMAGE FILE LIST 검색 End ####
		model.addAttribute("thumbImgFileList", thumbImgFileList);

		//sch_currentPage리스트 페이지의 페이징번호를 다시 currentPage에 넣어줘야 취소 로 상세 페이지로 돌아갈때 목록 검색조건의  페이징 번호가 유지됨
		param.put("currentPage", param.getString("sch_currentPage"));

		model.addAttribute("param", param);
		model.addAttribute("resultMap", resultMap);

		return "admin/bbs/updateFormBbsMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : updateBbsMgt
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 수정
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:59:06
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/updateBbsMgt.do", "/admin/bbs/{sys}/updateBbsMgt.do" })
	public @ResponseBody DataMap updateBbsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/bbs/" + sys;
		} else {
			returnUrl = "/admin/bbs";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());
		param.put("ss_author_id", userInfoVo.getAuthorId());

		DataMap resultStats = new DataMap();
		DataMap resultJSON = new DataMap();
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

		//이미지 파일
		List<MultipartFile> imgFileList = new ArrayList<MultipartFile>();
		String[] imgPathList = request.getParameterValues("imgPath");
		param.put("imgPathList", imgPathList);
		param.put("last_ver", '0');
		String dftFilePath = EgovPropertiesUtil.getProperty("Globals.fileImgTempPath");
		if(!(imgPathList== null))
		imgFileList = fileListSet(imgPathList, dftFilePath);

		//파일 객체 가져옴
		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
		// 파일 확장자 체크
		String msg = ntsysFileMngUtil.checkFileExt(fileList);
		if (msg != "") {
			MessageUtil.setMessage(request,egovMessageSource.getMessage("error.file.not.access", new String[] { msg }));
			model.addAttribute("param", param);
			resultStats.put("resultCode", "error");
		} else if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] {ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.fileMaxSize")) }));
			model.addAttribute("param", param);
			resultStats.put("resultCode", "error");
		} else {
			resultStats.put("resultCode", "ok");
		}

		// 썸네일 파일 객체 가져옴
		List thumbFileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest) request, "thumb_file");
		// 썸네일 이미지 파일 확장자 체크
		String thumb_msg = ntsysFileMngUtil.checkFileExt(thumbFileList, "JPG,JPEG,GIF,PNG", "Y");
		if (thumb_msg != "") {
			MessageUtil.setMessage(request,egovMessageSource.getMessage("error.img.not.access", new String[] { thumb_msg }));
			resultStats.put("resultCode", "error");
		}

		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if (!ntsysFileMngUtil.checkFileSize(thumbFileList)) {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize")) }));
			resultStats.put("resultCode", "error");
		}

		bbsMgtService.updateBbsMgt(param, imgFileList, thumbFileList, fileList);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		model.addAttribute("param", param);

		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);// 리턴값 : 선택된 메뉴 정보

		DataMap resultParam = new DataMap();

		resultParam.put("returnUrl", returnUrl + "/selectBbsMgt.do");
		String returnParam = SysUtil.createUrlParam(param);
		resultParam.put("returnParam", returnParam);

		resultJSON.put("resultParam", resultParam);

		response.setContentType("text/html; charset=utf-8");

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteBbsMgt
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:59:45
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/deleteBbsMgt.do", "/admin/bbs/{sys}/deleteBbsMgt.do" })
	public String deleteBbsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if(sys != null) {
			returnUrl = "/admin/bbs/" + sys;
		}else {
			returnUrl = "/admin/bbs";
		}

		DataMap param = RequestUtil.getDataMap(request);

		bbsMgtService.deleteBbsMgt(param);

		model.addAttribute("param", param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListBbsMgt.do", param, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListBbsCodeAjax
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 게시판 코드 목록 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오전 11:59:49
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/bbs/selectListBbsCodeAjax.do",  "/admin/bbs/{sys}/selectListBbsCodeAjax.do" })
	public @ResponseBody DataMap selectListBbsCodeAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		// 자신의 권한 아이디 셋
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());
		List authList = userInfoVo.getAuthorIdList();
		param.put("authList", authList);

		param.put("group_id", Const.UPCODE_BBS_TY); //게시판 유형 구분 코드
		List bbsCodeList = bbsMgtService.selectListBbsCode(param);

		DataMap resultJSON = new DataMap();
		resultJSON.put("bbsCodeList", bbsCodeList);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMngInfoAjax
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 게시판 코드 정보 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오후 12:00:02
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/bbs/selectBbsMngInfoAjax.do", "/admin/bbs/{sys}/selectBbsMngInfoAjax.do" })
	public @ResponseBody DataMap selectBbsMngInfoAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		DataMap bbsMngInfo = bbsMgtService.selectBbsMngInfo(param);

		DataMap resultJSON = new DataMap();
		resultJSON.put("bbsMngInfo", bbsMngInfo);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertBbsAnswer
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 답변 등록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오후 12:00:13
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/insertBbsAnswer.do", "/admin/bbs/{sys}/insertBbsAnswer.do" })
	public @ResponseBody DataMap insertBbsAnswer(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		//시스템별 returnUrl 설정
		String returnUrl = "";
		if(sys != null) {
			returnUrl = "/admin/bbs/" + sys;
		}else {
			returnUrl = "/admin/bbs";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		DataMap resultStats = new DataMap();

		//이미지 파일
		List<MultipartFile> imgFileList = new ArrayList<MultipartFile>();
		String[] imgPathList = request.getParameterValues("imgPath");
		param.put("imgPathList", imgPathList);
		param.put("last_ver", '0');
		String dftFilePath = EgovPropertiesUtil.getProperty("Globals.fileImgTempPath");
		if(!(imgPathList== null)) {
			imgFileList = fileListSet(imgPathList, dftFilePath);
		}

		//파일 객체 가져옴
		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
		// 파일 확장자 체크
		String msg = ntsysFileMngUtil.checkFileExt(fileList);
		if(msg != ""){
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.not.access",
					new String[] { msg }));
			model.addAttribute("param", param);
			resultStats.put("resultCode", "error");
		}else if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
			MessageUtil.setMessage(request, egovMessageSource.getMessage(
					"error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.fileMaxSize")) }));
			model.addAttribute("param", param);
			resultStats.put("resultCode", "error");
		}else {
			resultStats.put("resultCode", "ok");
		}

		bbsMgtService.insertBbsAnswer(param, imgFileList, fileList);

		DataMap resultJSON = new DataMap();
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.answer.insert"));

		model.addAttribute("param", param);

		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);//리턴값 : 선택된 메뉴 정보

		response.setContentType("text/html; charset=utf-8");
		return resultJSON;

    }

	/**
	 * <PRE>
	 * 1. MethodName : updateBbsAnswerForm
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 답변 수정폼
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 11. 7. 오후 7:18:57
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/updateBbsAnswerForm.do", "/admin/bbs/{sys}/updateBbsAnswerForm.do" })
    public String updateBbsAnswerForm(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		param.put("group_id", Const.UPCODE_SYS_CODE);			//시스템 코드
		DataMap resultMap = bbsMgtService.selectBbsAns(param);
		resultMap.put("CN", resultMap.htmlTagFilterRestore(resultMap.getString("CN")));

		// #### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(resultMap.getString("DOC_ID"));
		List<NtsysFileVO> ansFileList = ntsysFileMngService.selectFileInfs(fvo);
		// #### FILE LIST 검색 End ####
		model.addAttribute("ansFileList", ansFileList);

		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/bbs/updateBbsAnswerForm";
    }

	/**
	 * <PRE>
	 * 1. MethodName : updateBbsAnswer
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 답변 수정
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 18. 오후 7:16:23
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/updateBbsAnswer.do", "/admin/bbs/{sys}/updateBbsAnswer.do" })
	public @ResponseBody DataMap updateBbsAnswer(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		//시스템별 returnUrl 설정
		String returnUrl = "";
		if(sys != null) {
			returnUrl = "/admin/bbs/" + sys;
		}else {
			returnUrl = "/admin/bbs";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		DataMap resultStats = new DataMap();
		DataMap resultJSON = new DataMap();
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.answer.update"));

		//이미지 파일
		List<MultipartFile> imgFileList = new ArrayList<MultipartFile>();
		String[] imgPathList = request.getParameterValues("imgPath");
		param.put("imgPathList", imgPathList);
		param.put("last_ver", '0');
		String dftFilePath = EgovPropertiesUtil.getProperty("Globals.fileImgTempPath");
		if(!(imgPathList== null))
			imgFileList = fileListSet(imgPathList, dftFilePath);

		//파일 객체 가져옴
		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
		// 파일 확장자 체크
		String msg = ntsysFileMngUtil.checkFileExt(fileList);
		if(msg != ""){
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.not.access",
					new String[] { msg }));
			model.addAttribute("param", param);
			resultStats.put("resultCode", "error");
		}else if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
			MessageUtil.setMessage(request, egovMessageSource.getMessage(
					"error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.fileMaxSize")) }));
			model.addAttribute("param", param);
			resultStats.put("resultCode", "error");
		}else {
			resultStats.put("resultCode", "ok");
		}

		bbsMgtService.updateBbsAnswer(param, imgFileList, fileList);

		model.addAttribute("param", param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.answer.update"));

		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats); //리턴값 : 선택된 메뉴 정보

		DataMap resultParam = new DataMap();

		resultParam.put("returnUrl", returnUrl + "/selectBbsMgt.do");
		String returnParam = SysUtil.createUrlParam(param);
		resultParam.put("returnParam", returnParam);

		resultJSON.put("resultParam", resultParam);

		response.setContentType("text/html; charset=utf-8");

		return resultJSON;

	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteBbsAnswer
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 답변 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오후 12:00:30
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/deleteBbsAnswer.do", "/admin/bbs/{sys}/deleteBbsAnswer.do" })
    public String deleteBbsAnswer(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		//시스템별 returnUrl 설정
		String returnUrl = "";
		if(sys != null) {
			returnUrl = "/admin/bbs/" + sys;
		}else {
			returnUrl = "/admin/bbs";
		}

    	DataMap param = RequestUtil.getDataMap(request);

		bbsMgtService.deleteBbsAnswer(param);

		model.addAttribute("param", param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectBbsMgt.do", param, model);
    }

	/**
	 * <PRE>
	 * 1. MethodName : selectListSysBbs
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 게시물 이동 팝업
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 9. 3. 오후 8:31:35
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/selectListSysBbs.do", "/admin/bbs/{sys}/selectListSysBbs.do" })
	public String selectListSysBbs(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		model.addAttribute("param", param);

		return "admin/bbs/selectListSysBbs.popUp";
	}

	/**
	 * <PRE>
	 * 1. MethodName : changeSysBbs
	 * 2. ClassName  : BbsMgtController
	 * 3. Comment   : 게시물 관리 게시물 이동
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 9. 3. 오후 8:31:28
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value= { "/admin/bbs/changeSysBbs.do", "/admin/bbs/{sys}/changeSysBbs.do" })
	public String changeSysBbs(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		//시스템별 returnUrl 설정
		String returnUrl = "";
		if(sys != null) {
			returnUrl = "/admin/bbs/" + sys;
		}else {
			returnUrl = "/admin/bbs";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		bbsMgtService.updateChnageOrganBbs(param);

		model.addAttribute("param", param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectBbsMgt.do", param, model);

	}

	public List<MultipartFile> fileListSet(String[] imgPathList, String dftFilePath) {
		// TODO Auto-generated method stub
    	List<MultipartFile> fileList = new ArrayList<MultipartFile>();

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

	//미리보기
	@RequestMapping(value = "/admin/bbs/selectBBSPreview.do")
	public String selectBBSPreview(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap preViewData = new DataMap();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date date = new Date();

		String sysdate = format.format(date);

		preViewData.put("SJ", param.getString("sj"));// 제목
		preViewData.put("CN", param.getStringOrgn("cn")); // 내용
		preViewData.put("TEXT_CN", param.getString("text_cn")); //텍스트 내용
		preViewData.put("RM", param.getString("rm")); //비고
		preViewData.put("REGIST_DT", param.getString("regist_dt")); //날짜
		preViewData.put("USER_NM", param.getString("USER_NM")); // 작성자
		preViewData.put("LINK_URL", param.getString("link_url")); //링크 url
		preViewData.put("MVP_URL", param.getString("mvp_url")); //동영상 링크
		preViewData.put("HIT_CNT", param.getString("HIT_CNT")); //조회수
		preViewData.put("SYSDATE", sysdate ); // 현재시간
		preViewData.put("CTTPC_SE_NM", param.getString("CTTPC_SE_NM")); //
		preViewData.put("REGISTER_CTTPC", param.getString("REGISTER_CTTPC")); //전화번호
		preViewData.put("fileName", param.getString("fileName")); // 파일이름 및 확장자

		//insert page, update page 파라미터값에 따른 분기
		if(!"".equals(param.getString("SYS_CODE_NM"))){
			preViewData.put("SYS_CODE_NM", param.getString("SYS_CODE_NM")); //시스템
		}else {
			preViewData.put("SYS_CODE_NM", param.getString("sys_code")); //시스템
		}

		if(!"".equals(param.getString("BBS_NM"))){
			preViewData.put("BBS_NM", param.getString("BBS_NM")); //게시판명
		}else {
			preViewData.put("BBS_NM", param.getString("bbs_code")); //게시판명
		}

		model.addAttribute("preViewData", preViewData);

		return "admin/bbs/selectBBSPreview.preview";

	}
}
