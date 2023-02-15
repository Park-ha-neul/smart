package egovframework.admin.stdTerm.web;

import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.stdTerm.service.StdTermMgtService;
import egovframework.admin.user.service.UserMgtService;
import egovframework.admin.user.web.UserMgtController;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : UserMgtController.java
 * 3. Package  : egovframework.admin.user.web
 * 4. Comment  :
 * 5. 작성자   : JJH
 * 6. 작성일   : 2015. 12. 7. 오후 4:57:45
 * 7. 변경이력 :
 *	이름	 : 일자		  : 근거자료   : 변경내용
 *	------------------------------------------------------
 *	JJH : 2015. 12. 7. :			: 신규 개발.
 * </PRE>
 */

@Controller
public class StdTermMgtController {

		private static Log log = LogFactory.getLog(StdTermMgtController.class);

		@Resource(name = "egovMessageSource")
		private EgovMessageSource egovMessageSource;

		@Resource(name = "stdTermMgtService")
		private StdTermMgtService stdTermMgtService;

		/** CommonCodeService */
		@Resource(name = "commonCodeService")
		private CommonCodeService commonCodeService;

		@Resource(name="NtsysFileMngUtil")
	    private NtsysFileMngUtil ntsysFileMngUtil;

		@Resource(name="NtsysFileMngService")
	    private NtsysFileMngService ntsysFileMngService;

		/**
		 * <PRE>
		 * 1. MethodName : selectPageListStdTermMgt
		 * 2. ClassName  : StdTermMgtController
		 * 3. Comment   : 행정표준용어 리스트 페이지
		 * 4. 작성자	: 김성민
		 * 5. 작성일	: 2021. 4. 22. 오후 3:12:26
		 * </PRE>
		 *
		 * @return String
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/admin/stdTerm/selectPageListStdTermMgt.do")
		public String selectPageListStdTermMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

			DataMap param = RequestUtil.getDataMap(request);

			List fileList = stdTermMgtService.selectDistinctFileName();
			model.addAttribute("fileList", fileList);

			if(request.getParameter("fileCode") == null){
				param.put("fileCode", stdTermMgtService.selectTopFile());
			}

			/* ### Pasing 시작 ### */
			int totCnt = stdTermMgtService.selectTotCntStdTermMgt(param);
			param.put("totalCount", totCnt);
			param = pageNavigationUtil.createNavigationInfo(model, param);
			List resultList = stdTermMgtService.selectPageListStdTermMgt(param);
			/* ### Pasing 끝 ### */

			model.addAttribute("resultList", resultList);
			model.addAttribute("param", param);
			return "admin/stdTerm/selectPageListStdTermMgt";
		}

		/**
		 * <PRE>
		 * 1. MethodName : insertFormStdTermMgt
		 * 2. ClassName  : StdTermMgtController
		 * 3. Comment   : 행정표준용어 인서트 폼
		 * 4. 작성자	: 김성민
		 * 5. 작성일	: 2021. 4. 22. 오후 3:12:26
		 * </PRE>
		 *
		 * @return String
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/admin/stdTerm/insertFormStdTermMgt.do")
		public String insertFormStdTermMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

			return "admin/stdTerm/insertFormStdTermMgt";
		}

		/**
		 * <PRE>
		 * 1. MethodName : insertStdTermMgt
		 * 2. ClassName  : StdTermMgtController
		 * 3. Comment   : 행정표준용어 등록
		 * 4. 작성자	: 김성민
		 * 5. 작성일	: 2021. 4. 22. 오후 4:27:26
		 * </PRE>
		 *
		 * @return String
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/admin/stdTerm/insertStdTermMgt.do")
		public @ResponseBody DataMap insertStdTermMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

			DataMap param = RequestUtil.getDataMap(request);
			DataMap resultStats = new DataMap();

	    	//업로드 파일 리스트 추가 시작//
			List attachFileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

			DataMap resultJSON = new DataMap();
			MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));
			resultStats.put("resultCode", "ok");

			UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
			param.put("ss_user_no", userInfoVo.getUserNo());

			stdTermMgtService.insertstdTermMgt(param, attachFileList);

			resultStats.put("resultMsg", "");
			resultJSON.put("resultStats", resultStats);// 리턴값 : 선택된 메뉴 정보
	        response.setContentType("text/html; charset=utf-8");

	        return resultJSON;
		}

}
