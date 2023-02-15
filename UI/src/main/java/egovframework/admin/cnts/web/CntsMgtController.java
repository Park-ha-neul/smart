package egovframework.admin.cnts.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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

import egovframework.admin.author.web.AuthorMgtController;
import egovframework.admin.cnts.service.CntsMgtService;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.menu.service.MenuMgtService;
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
import egovframework.framework.common.util.file.vo.NtsysFileVO;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : CntsMgtController.java
 * 3. Package  : egovframework.admin.cnts.web
 * 4. Comment  : 게시판
 * 5. 작성자   : 백국현
 * 6. 작성일   : 2018. 6. 26
 * </PRE>
 */
/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : CntsMgtController.java
 * 3. Package  : egovframework.admin.cnts.web
 * 4. Comment  : 콘텐츠 컨트롤러
 * 5. 작성자   : 백국현
 * 6. 작성일   : 2018. 7. 10. 오후 4:30:12
 * </PRE>
 */
@Controller
public class CntsMgtController {

	private static Log log = LogFactory.getLog(AuthorMgtController.class);

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

	@Resource(name = "cntsMgtService")
    private CntsMgtService cntsMgtService;

	@Resource(name = "menuMgtService")
    private MenuMgtService menuMgtService;

    @Resource(name="NtsysFileMngUtil")
    private NtsysFileMngUtil ntsysFileMngUtil;

	@Resource(name="NtsysFileMngService")
    private NtsysFileMngService ntsysFileMngService;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListCntsMgt
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment  : 콘텐츠 리스트 조회
	 * 4. 작성자   : 백국현
	 * 5. 작성일   : 2018. 6. 26
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/cnts/selectPageListCntsMgt.do", "/admin/cnts/{sys}/selectPageListCntsMgt.do" })
	public String selectPageListCntsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		param.put("sys_group_id", Const.UPCODE_SYS_CODE); //시스템 구분 코드

    	/* ### Pasing 시작 ### */
    	int totCnt = cntsMgtService.selectTotCntMgt(param);

    	param.put("totalCount", totCnt);
    	param = pageNavigationUtil.createNavigationInfo(model, param);
        List resultList = cntsMgtService.selectPageListCntsMgt(param);
        /* ### Pasing 끝 ### */

        model.addAttribute("totalCount", totCnt);
        model.addAttribute("resultList", resultList);
        model.addAttribute("param", param);

		return "admin/cnts/selectPageListCntsMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectCntsMgt
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment  : 콘텐츠 상세 조회
	 * 4. 작성자   : 백국현
	 * 5. 작성일   : 2018. 6. 26
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/cnts/selectCntsMgt.do", "/admin/cnts/{sys}/selectCntsMgt.do" })
	public String selectCntsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		param.put("sys_group_id", Const.UPCODE_SYS_CODE); //시스템 구분 코드

		DataMap resultMap = cntsMgtService.selectCntsMgt(request, response, param);
		resultMap.put("CN", resultMap.htmlTagFilterRestore(resultMap.getString("CN")));

		// #### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(resultMap.getString("DOC_ID"));

		List<NtsysFileVO> fileList = ntsysFileMngService.selectFileInfs(fvo);
		// #### FILE LIST 검색 End ####

		model.addAttribute("fileList", fileList);
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/cnts/selectCntsMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : deleteCntsMgt
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment  : 콘텐츠 삭제
	 * 4. 작성자   : 백국현
	 * 5. 작성일   : 2018. 6. 26
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/cnts/deleteCntsMgt.do", "/admin/cnts/{sys}/deleteCntsMgt.do" })
	public String deleteCntsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/cnts/" + sys;
		} else {
			returnUrl = "/admin/cnts";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		DataMap param = RequestUtil.getDataMap(request);

		param.put("ss_user_no", userInfoVo.getUserNo());
		model.addAttribute("param", param);

		cntsMgtService.deleteCntsMgt(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListCntsMgt.do", param, model);
	}

	/**
	 * <PRE>
	 * 1. MethodName : inserFormCntsMgt
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment  : 콘텐츠 등록 폼 이동
	 * 4. 작성자   : 백국현
	 * 5. 작성일   : 2018. 6. 26
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/cnts/insertFormCntsMgt.do", "/admin/cnts/{sys}/insertFormCntsMgt.do" })
	public String inserFormCntsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sys_code", sys);
		}

		model.addAttribute("param", param);

		return "admin/cnts/insertFormCntsMgt";
	}



	/**
	 * <PRE>
	 * 1. MethodName : insertCntsMgt
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment  : 콘텐츠 입력
	 * 4. 작성자   : 백국현
	 * 5. 작성일   : 2018. 6. 26
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/cnts/insertCntsMgt.do", "/admin/cnts/{sys}/insertCntsMgt.do" })
	public @ResponseBody DataMap insertCntsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultStats = new DataMap();

		/* ### 이미지 정보 조회 시작 ###*/
    	List<MultipartFile> fileList = new ArrayList<MultipartFile>();
    	String[] imgPathList = request.getParameterValues("imgPath");
    	param.put("imgPathList", imgPathList);
    	param.put("last_ver", '0');
    	String dftFilePath = EgovPropertiesUtil.getProperty("Globals.fileImgTempPath");
    	if(!(imgPathList== null))
    	fileList = fileListSet(imgPathList, dftFilePath);
		 /*### 이미지 정보 조회 끝 ###*/

    	//시스템별 returnUrl 설정
    	String returnUrl = "";
    	if (sys != null) {
    		returnUrl = "/admin/cnts/"+ sys +"/selectCntsMgt.do";
        } else {
        	returnUrl = "/admin/cnts/selectCntsMgt.do";
        }

    	//업로드 파일 리스트 추가 시작//
		List attachFileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		DataMap resultJSON = new DataMap();
		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));
		resultStats.put("resultCode", "ok");

		// 파일 확장자 체크
		String msg = ntsysFileMngUtil.checkFileExt(attachFileList);
		if(msg != ""){
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.not.access", new String[] { msg }));
			resultStats.put("resultCode", "error");
			returnUrl = "/admin/cnts/insertFormCntsMgt.do?";
		}

		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if (!ntsysFileMngUtil.checkFileSize(attachFileList)) {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.fileMaxSize")) }));
			resultStats.put("resultCode", "error");
			returnUrl = "/admin/cnts/insertFormCntsMgt.do?";
			}

    	//업로드 파일  끝//

    	UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
    	param.put("ss_user_no", userInfoVo.getUserNo());

    	param.put("url_ty_code", "20");
    	cntsMgtService.insertCntsMgt(param, fileList, attachFileList);
    	model.addAttribute("param", param);

        returnUrl += "?cntnts_seq="+ param.getString("cntnts_seq");

        resultStats.put("redirectUrl",returnUrl);
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);// 리턴값 : 선택된 메뉴 정보
        response.setContentType("text/html; charset=utf-8");

        return resultJSON;

	}


	/**
	 * <PRE>
	 * 1. MethodName : updateFormCntsMgt
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment  : 콘텐츠 수정 폼
	 * 4. 작성자   : 백국현
	 * 5. 작성일   : 2018. 6. 26
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/cnts/updateFormCntsMgt.do", "/admin/cnts/{sys}/updateFormCntsMgt.do" })
	public String updateFormCntsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		param.put("sys_group_id", Const.UPCODE_SYS_CODE);

		//콘텐츠 상세 정보 조회
		DataMap resultMap = cntsMgtService.selectCntsMgt(request, response, param);

		//버전 정보를 가져온다
		List versList = cntsMgtService.selectPageListCntsVerMgt(param);

		// #### FILE LIST 검색 Start ####
 		NtsysFileVO fvo = new NtsysFileVO();
 		fvo.setDocId(resultMap.getString("DOC_ID"));

 		List<NtsysFileVO> fileList = ntsysFileMngService.selectFileInfs(fvo);
 		// #### FILE LIST 검색 End ####

		model.addAttribute("versList", versList);
		model.addAttribute("fileList", fileList);
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("param", param);

		return "admin/cnts/updateFormCntsMgt";
	}


	/**
	 * <PRE>
	 * 1. MethodName : updateCntsMgt
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment  : 콘텐츠 수정
	 * 4. 작성자   : 백국현
	 * 5. 작성일   : 2018. 6. 26
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/cnts/updateCntsMgt.do", "/admin/cnts/{sys}/updateCntsMgt.do" })
	public @ResponseBody DataMap updateCntsMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		DataMap resultJSON = new DataMap();
		DataMap resultStats = new DataMap();

		// 시스템별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/cnts/" + sys;
		} else {
			returnUrl = "/admin/cnts";
		}

		// 파일 리스트 추가 시작//
		List<MultipartFile> fileList = new ArrayList<MultipartFile>();
		String[] imgPathList = request.getParameterValues("imgPath");
		param.put("imgPathList", imgPathList);
		String dftFilePath = EgovPropertiesUtil.getProperty("Globals.fileImgTempPath");
		if (!(imgPathList == null))
			fileList = fileListSet(imgPathList, dftFilePath);
		// 파일 리스트 추가 시작//

		// 업로드 파일 리스트 추가 시작//
		List attachFileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
		resultStats.put("resultCode", "ok");

		// 파일 확장자 체크
		String msg = ntsysFileMngUtil.checkFileExt(attachFileList);
		if(msg != ""){
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.not.access", new String[] { msg }));
			resultStats.put("resultCode", "error");
		}

		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if (!ntsysFileMngUtil.checkFileSize(attachFileList)) {
			MessageUtil.setMessage(request, egovMessageSource.getMessage( "error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.fileMaxSize")) }));
			resultStats.put("resultCode", "error");
		}
    	//업로드 파일 리스트 추가 끝//

		//OldVer 체크 상태를 확인한다//
    	String lastVer = param.getString("last_ver");
    	//ver 정보를 0.1 올린다
    	String ver = (new BigDecimal(lastVer).add(new BigDecimal("0.1"))).toString();
    	param.put("last_ver", ver);
    	param.put("select_ver", "");

		model.addAttribute("param", param);

    	param.put("url_ty_code", "20");
		cntsMgtService.updateCntsMgt(param, fileList, attachFileList);

		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);// 리턴값 : 선택된 메뉴 정보

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		DataMap resultParam = new DataMap();

		DataMap redirectParam = new DataMap();
		redirectParam.put("sys_group_id", Const.UPCODE_SYS_CODE); //시스템 구분 코드
		redirectParam.put("cntnts_seq", param.getString("cntnts_seq"));
		resultParam.put("returnParam", SysUtil.createUrlParam(redirectParam));
		resultParam.put("returnUrl", returnUrl + "/selectCntsMgt.do");
		resultJSON.put("resultParam", resultParam);
		response.setContentType("text/html; charset=utf-8");

		return resultJSON;
	}


	/**
	 * <PRE>
	 * 1. MethodName : selectPageListImg
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment   : 포토 매니저 이미지 리스트
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 7. 17. 오후 2:31:45
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/cnts/selectPageListPhotoMng.do", "/admin/cnts/{sys}/selectPageListPhotoMng.do" })
	public String selectPageListImg(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

    	/* ### Pasing 시작 ### */
    	int totCnt = cntsMgtService.selectTotCntPhotoMng(param);

    	param.put("totalCount", totCnt);
    	param = pageNavigationUtil.createNavigationInfo(model, param);
        List resultList = cntsMgtService.selectPageListPhotoMng(param);
        /* ### Pasing 끝 ### */

        model.addAttribute("totalCount", totCnt);
        model.addAttribute("resultList", resultList);
        model.addAttribute("param", param);

		return "admin/cnts/selectPageListPhotoMng.noMenu";
	}


	/**
	 * <PRE>
	 * 1. MethodName : cntntIdAjax
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment   : 콘텐츠 아이디 생성
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 8. 24. 오전 11:17:12
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/cnts/selectCntntIdSeqAjax.do", "/admin/cnts/{sys}/selectCntntIdSeqAjax.do" })
	public @ResponseBody DataMap selectCntntIdSeqAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		DataMap result = cntsMgtService.cntntIdAjax(param);//메뉴 존재 여부 확인

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultId", result);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats); //리턴값 Y / N

		response.setContentType("text/html; charset=utf-8");

		return resultJSON;

	}

	/**
	 * <PRE>
	 * 1. MethodName : cntntIdChkAjax
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment   : 콘텐츠 아이디 중복 확인
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 8. 24. 오전 11:17:17
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/cnts/cntntIdChkAjax.do", "/admin/cnts/{sys}/cntntIdChkAjax.do" })
	public @ResponseBody DataMap cntntIdChkAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		int result = cntsMgtService.cntntIdChkAjax(param);//메뉴 존재 여부 확인

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultNum", result);

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : fileListSet
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment   : 업로드된 이미지 정보를 List로 저장
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 7. 12. 오후 4:35:10
	 * </PRE>
	 *   @return List
	 *   @param imgPathList
	 *   @param dftFilePath
	 *   @return
	 */
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


	/**
	 * <PRE>
	 * 1. MethodName : fileToMultipartFile
	 * 2. ClassName  : CntsMgtController
	 * 3. Comment   : file 객체를 MultipartFile 객체로 변환
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 7. 12. 오후 4:35:21
	 * </PRE>
	 *   @return MultipartFile
	 *   @param file
	 *   @return
	 */
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
	@RequestMapping(value = "/admin/cnts/selectCNTSPreview.do")
	public String selectBBSPreview(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap cntsData = new DataMap();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String sysdate = format.format(date);

		cntsData.put("CNTNTS_ID", param.getString("cntnts_id"));
		cntsData.put("SJ", param.getString("sj"));
		cntsData.put("CN", param.getStringOrgn("cn"));
		cntsData.put("TEXT_CN", param.getString("text_cn"));
		cntsData.put("SCRIPT", param.getString("script"));
		cntsData.put("KWRD", param.getString("kwrd"));
		cntsData.put("SYSDATE", sysdate);
		cntsData.put("LAST_VER", param.getString("select_ver"));
		cntsData.put("CNTNTS_ID", param.getString("cntnts_id"));
		cntsData.put("REGIST_DT", param.getString("regist_dt"));
		cntsData.put("UPDT_DT", param.getString("updt_dt"));
		cntsData.put("HIT_CNT", param.getString("hit_cnt"));
		cntsData.put("fileName", param.getString("fileName")); // 파일이름 및 확장자

		//insert page, update page 파라미터값에 따른 분기
		if(!"".equals(param.getString("SYS_NM"))){
			cntsData.put("SYS_NM", param.getString("SYS_NM")); //시스템
		}else {
			cntsData.put("SYS_NM", param.getString("sys_code")); //시스템
		}



		model.addAttribute("cntsData", cntsData);

		return "admin/cnts/selectCNTSPreview.preview";

	}

}
