

package egovframework.admin.qr.web;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.qr.service.QrCodeMgtService;
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
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : QrCodeMgtController.java
 * 3. Package  : egovframework.admin.qr.web
 * 4. Comment  : qr 코드 관리
 * 5. 작성자   : mk
 * 6. 작성일   : 2018. 7. 13. 오전 11:49:13
 * </PRE>
 */
@Controller
public class QrCodeMgtController {

	private static Log log = LogFactory.getLog(QrCodeMgtController.class);

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

    /** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	/** qrcodeService */
	@Resource(name = "qrCodeMgtService")
	private QrCodeMgtService qrCodeMgtService;

	@Resource(name="NtsysFileMngUtil")
    private NtsysFileMngUtil ntsysFileMngUtil;

	@Resource(name="NtsysFileMngService")
    private NtsysFileMngService ntsysFileMngService;


	/**
	 * <PRE>
	 * 1. MethodName : selectPageListQrCodeMgt
	 * 2. ClassName  : QrCodeMgtController
	 * 3. Comment   : qr 코드 리스트
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 1:09:43
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/qr/selectPageListQrCodeMgt.do", "/admin/qr/{sys}/selectPageListQrCodeMgt.do" })
    public String selectPageListQrCodeMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		param.put("group_id", Const.UPCODE_SYS_CODE); // 시스템 코드
    	/* ### Pasing 시작 ### */
    	int totCnt = qrCodeMgtService.selectTotCntQrCodeMgt(param);
    	param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
        List resultList = qrCodeMgtService.selectPageListQrCodeMgt(param);
        /* ### Pasing 끝 ### */

		model.addAttribute("resultList", resultList);
        model.addAttribute("param", param);

		return "admin/qr/selectPageListQrCodeMgt";
    }

	/**
	 * <PRE>
	 * 1. MethodName : selectQrCodeMgt
	 * 2. ClassName  : QrCodeMgtController
	 * 3. Comment   : qr 코드 상세
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 1:09:56
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/qr/selectQrCodeMgt.do", "/admin/qr/{sys}/selectQrCodeMgt.do" })
    public String selectQrCodeMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

    	if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

    	param.put("group_id", Const.UPCODE_SYS_CODE); // 시스템 코드
    	DataMap resultMap = qrCodeMgtService.selectQrCodeMgt(param);
    	resultMap.put("QR_CODE_URL", EgovPropertiesUtil.getProperty("site.domain.cms") + resultMap.getString("QR_PATH_URL"));

    	// #### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(resultMap.getString("IMAGE_DOC_ID"));
		List<NtsysFileVO> imageFileList = ntsysFileMngService.selectFileInfs(fvo);
		// #### FILE LIST 검색 End ####
		model.addAttribute("imageFileList", imageFileList);

        model.addAttribute("param", param);
        model.addAttribute("resultMap", resultMap);

        return "admin/qr/selectQrCodeMgt";
    }

	/**
	 * <PRE>
	 * 1. MethodName : inserFormQrCodeMgt
	 * 2. ClassName  : QrCodeMgtController
	 * 3. Comment   : qr 코드 등록폼
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 1:10:12
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/qr/insertFormQrCodeMgt.do", "/admin/qr/{sys}/insertFormQrCodeMgt.do" })
    public String insertFormQrCodeMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sys_code", sys);
		}

        model.addAttribute("param", param);

        return "admin/qr/insertFormQrCodeMgt";
    }

	/**
	 * <PRE>
	 * 1. MethodName : insertQrCodeMgt
	 * 2. ClassName  : QrCodeMgtController
	 * 3. Comment   : qr 코드 등록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 1:10:20
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/qr/insertQrCodeMgt.do", "/admin/qr/{sys}/insertQrCodeMgt.do" })
    public String insertQrCodeMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/qr/" + sys;
		} else {
			returnUrl = "/admin/qr";
		}

    	UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

    	// 파일 객체 가져옴
		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		//배경 이미지 파일 확장자 체크
		String msg = ntsysFileMngUtil.checkFileExt(fileList, "JPG,JPEG,GIF,PNG", "Y");
		if (msg != "") {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.img.not.access", new String[] { msg }));
			return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/insertFormQrCodeMgt.do", param, model);
		}

		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] {ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize")) }));
			return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/insertFormQrCodeMgt.do", param, model);
		}

		qrCodeMgtService.insertQrCodeMgt(param, fileList);

        MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

        return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListQrCodeMgt.do", null, model);
    }

	/**
	 * <PRE>
	 * 1. MethodName : updateFormQrCodeMgt
	 * 2. ClassName  : QrCodeMgtController
	 * 3. Comment   : qr 코드 수정폼
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 1:10:30
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/qr/updateFormQrCodeMgt.do", "/admin/qr/{sys}/updateFormQrCodeMgt.do" })
    public String updateFormQrCodeMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sys_code", sys);
		}

    	param.put("group_id", Const.UPCODE_SYS_CODE); // 시스템 코드
    	DataMap resultMap = qrCodeMgtService.selectQrCodeMgt(param);
    	resultMap.put("QR_CODE_URL", EgovPropertiesUtil.getProperty("site.domain.cms") + resultMap.getString("QR_PATH_URL"));

    	// #### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(resultMap.getString("IMAGE_DOC_ID"));
		List<NtsysFileVO> imageFileList = ntsysFileMngService.selectFileInfs(fvo);
		// #### FILE LIST 검색 End ####
		model.addAttribute("imageFileList", imageFileList);

        model.addAttribute("param", param);
        model.addAttribute("resultMap", resultMap);

        return "admin/qr/updateFormQrCodeMgt";
    }

	/**
	 * <PRE>
	 * 1. MethodName : updateQrCodeMgt
	 * 2. ClassName  : QrCodeMgtController
	 * 3. Comment   : qr 코드 수정
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 1:10:38
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/qr/updateQrCodeMgt.do", "/admin/qr/{sys}/updateQrCodeMgt.do" })
    public String updateQrCodeMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/qr/" + sys;
		} else {
			returnUrl = "/admin/qr";
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		String imgExistYn = "N";

		// 파일 객체 가져옴
		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		//넘어온 배경 이미지가 없으면
		if (fileList.isEmpty()) {
			// 등록 된 배경이미지가 있는지 확인한다.
			DataMap imgInfo = qrCodeMgtService.selectImgExistYn(param);
			imgExistYn = imgInfo.getString("EXIST_YN");
			if (imgExistYn.equals("Y")) {
				// 서버에 저장된 배경 이미지 파일
				List imgFileList = new ArrayList();
				String imgPath = imgInfo.getString("IMG_PATH_URL");
				param.put("imgPath", imgPath);
				String dftFilePath = EgovPropertiesUtil.getProperty("Globals.fileImgTempPath");
				fileList = fileListSet(imgPath, dftFilePath);
			}
		// 넘어온 배경 이미지가 있으면
		}else{
			//배경 이미지 파일 확장자 체크
			String msg = ntsysFileMngUtil.checkFileExt(fileList, "JPG,JPEG,GIF,PNG", "Y");
			if (msg != "") {
				MessageUtil.setMessage(request, egovMessageSource.getMessage("error.img.not.access", new String[] { msg }));
				return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/updateQrCodeMgt.do", param, model);
			}

			// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
			if (!ntsysFileMngUtil.checkFileSize(fileList)) {
				MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize")) }));
				return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/updateQrCodeMgt.do", param, model);
			}
		}

		qrCodeMgtService.updateQrCodeMgt(param, fileList, imgExistYn);

		model.addAttribute("param", param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

		// redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("qr_seq", param.getString("qr_seq"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectQrCodeMgt.do", resultParam, model);

	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteQrCodeMgt
	 * 2. ClassName  : QrCodeMgtController
	 * 3. Comment   : qr 코드 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오후 1:10:48
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/qr/deleteQrCodeMgt.do", "/admin/qr/{sys}/deleteQrCodeMgt.do" })
    public String deleteQrCodeMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템 별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/qr/" + sys;
		} else {
			returnUrl = "/admin/qr";
		}

    	DataMap param = RequestUtil.getDataMap(request);

		qrCodeMgtService.deleteQrCodeMgt(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		// redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("sch_sys_code", param.getString("sch_sys_code"));
		resultParam.put("sch_text", param.getString("sch_text"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListQrCodeMgt.do", null, model);

    }

	public List fileListSet(String imgPathList, String dftFilePath) {
		// TODO Auto-generated method stub
    	List fileList = new ArrayList();
		File file = new File(dftFilePath + imgPathList);
	    MultipartFile multipartFile = fileToMultipartFile(file);
	    fileList.add(multipartFile);

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
	    OutputStream outputStream = null;
	    try {
	        inputStream = new FileInputStream(file);
	        outputStream = fileItem.getOutputStream();
	        IOUtils.copy(inputStream, outputStream);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }finally{
	    	try{
	    		inputStream.close();
	    		outputStream.close();
			}catch (Exception e){
				e.printStackTrace();
			}
	    }

	    MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

	    return multipartFile;
	}
}
