package egovframework.common.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.common.service.CommonCodeService;
import egovframework.common.service.EditorService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

@Controller
public class EditorController {

	private static Log log = LogFactory.getLog(EditorController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "editorService")
	private EditorService editorService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	@RequestMapping(value = "/editor/editorFileUpload.do")
	public @ResponseBody DataMap ckeditorFileUpload(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultJSON = new DataMap();

		// 파일 객체 가져옴
		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		String returnStr = "";
		String msg = "";

		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if(!ntsysFileMngUtil.checkEachFileSize(fileList, "Globals.ImgfileMaxSize")){
			msg = egovMessageSource.getMessage("error.img.size.over", new String[]{ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize"))});
			// 메세지 셋팅하여 호출
			returnStr += "<script type=\"text/javascript\">window.parent.fnEditorFileUploadCallback.apply(window.parent, ['" + param.getString("CKEditorFuncNum") + "', '', false, '" + msg + "']);</script>";
		} else {
			try {

				UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
				param.put("ss_user_id", userInfoVo.getId());

				// 파일 저장
				editorService.insertEditorFile(param, fileList);
				// 파일 정보
				NtsysFileVO fileInfo = (NtsysFileVO)param.get("fileInfo");

				String imgPath = fileInfo.getFileRltvPath() + fileInfo.getFileId() + "." + fileInfo.getFileExtNm();
				// 해당 창이 iframe으로 되어있기때문에 상위 윈도우창을 타겟으로 스크립트문을 작성하여 준다.
				returnStr += "<script type=\"text/javascript\">window.parent.fnEditorFileUploadCallback.apply(window.parent, ['" + param.getString("CKEditorFuncNum") + "', '" + imgPath + "', true, '']);</script>";
			} catch (Exception e1) {
				// 에러시
				returnStr += "<script type=\"text/javascript\">window.parent.fnEditorFileUploadCallback.apply(window.parent, ['" + param.getString("CKEditorFuncNum") + "', '', false, '']);</script>";
			}
		}

		response.setContentType("text/html; charset=utf-8");
		return resultJSON;

	}

}
