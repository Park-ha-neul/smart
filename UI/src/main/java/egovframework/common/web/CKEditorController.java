package egovframework.common.web;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.admin.user.web.UserMgtController;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

@Controller
public class CKEditorController {

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name="NtsysFileMngService")
	private NtsysFileMngService ntsysFileMngService;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	private static Log log = LogFactory.getLog(UserMgtController.class);

	/**
	 * <PRE>
	 * 1. MethodName : multipartHttpPhotoUpload
	 * 2. ClassName  : CKEditorController
	 * 3. Comment   : 에디터에서 파일 업로드시 호출
	 * 4. 작성자    : Gi.Won
	 * 5. 작성일    : 2020. 7. 22. 오후 4:16:27
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param mtfRequest
	 *   @param response
	 */
	@RequestMapping(value = "/CKEditor/multipartHttpPhotoUpload.do")
	public void multipartHttpPhotoUpload(MultipartHttpServletRequest request, MultipartHttpServletRequest mtfRequest, HttpServletResponse response){

		try {
			DataMap param = RequestUtil.getDataMap(request);

			List fileList = ntsysFileMngUtil.getFiles(request);
			NtsysFileVO reNtsysFile = null;

			String sFileInfo = ""; //리턴해 줄 파일 정보
			response.setContentType("text/html; charset=utf-8");
			PrintWriter print = response.getWriter();

			// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
			if (!ntsysFileMngUtil.checkFileSize(fileList, "Globals.ImgfileMaxSize")) {
				MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize")) }));
				print.print("ErrorSizeFile");
				print.flush();
				print.close();
			} else if (ntsysFileMngUtil.checkFileExt(fileList) != "") {
				// 파일 확장자 체크
				MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.not.access",new String[] { "" }));
				print.print("ErrorExtFile");
				print.flush();
				print.close();
			} else {
				if (!fileList.isEmpty()) {
					for (int i = 0; i < fileList.size(); i++) {
						MultipartFile mfile = (MultipartFile) fileList.get(i);
						if (!mfile.isEmpty()) {
							/**
							* parseFileInf
							* 1:파일객체
							* 2:문서아이디
							* 3:서브폴더명
							* 4:사용자번호
							* 5:Web Root Yn
							*/
							// 파일을 서버에 물리적으로 저장
							reNtsysFile = ntsysFileMngUtil.parseFileInf(mfile, "", "temp/", param.getString("ss_user_no"), "Y");

							if( param.getString("responseType").equals("json")) {
								String fileUrl = reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm();
								sFileInfo += "{\"filename\" : \"" + reNtsysFile.getFileNm() + "\", \"uploaded\" : 1, \"url\":\"" + fileUrl + "\"}";
							} else {
								sFileInfo += "&sFileName="+ reNtsysFile.getFileNm();
								sFileInfo += "&sFileURL="+ reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm();
							}
						}
					}
				}

				print.print(sFileInfo);
				print.flush();
				print.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
