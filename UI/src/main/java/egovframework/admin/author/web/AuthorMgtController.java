package egovframework.admin.author.web;

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

import egovframework.admin.author.service.AuthorMgtService;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.common.service.CodeCacheService;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.TransReturnUtil;


/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : AuthorMgtController.java
 * 3. Package  : egovframework.admin.author.web
 * 4. Comment  : 권한 관리
 * 5. 작성자   : 김영배
 * 6. 작성일   : 2015. 9. 1. 오후 3:44:37
 * </PRE>
 */
@Controller
public class AuthorMgtController {

	private static Log log = LogFactory.getLog(AuthorMgtController.class);

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

	/** authorMgtService */
    @Resource(name = "authorMgtService")
    private AuthorMgtService authorMgtService;

    /** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;


	/**
	 * <PRE>
	 * 1. MethodName : selectPageListAuthorMgt
	 * 2. ClassName  : AuthorMgtController
	 * 3. Comment   : 권한 목록
	 * 4. 작성자    : 김영배
	 * 5. 작성일    : 2015. 9. 1. 오후 3:44:49
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/author/selectPageListAuthorMgt.do")
    public String selectPageListAuthorMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
    	/* ### Pasing 시작 ### */
    	int totCnt = authorMgtService.selectTotCntAuthor(param);
    	param.put("totalCount", totCnt);
    	param = pageNavigationUtil.createNavigationInfo(model, param);
        List resultList = authorMgtService.selectPageListAuthor(param);
        /* ### Pasing 끝 ### */

        model.addAttribute("totalCount", totCnt);
        model.addAttribute("resultList", resultList);
        model.addAttribute("param", param);

        return "admin/author/selectPageListAuthorMgt";
    }


    /**
     * <PRE>
     * 1. MethodName : insertFormAuthorMgt
     * 2. ClassName  : AuthorMgtController
     * 3. Comment   : 권한 추가폼
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:44:58
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value="/admin/author/insertFormAuthorMgt.do")
    public String insertFormAuthorMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        return "admin/author/insertFormAuthorMgt";
    }


    /**
     * <PRE>
     * 1. MethodName : insertAuthorMgt
     * 2. ClassName  : AuthorMgtController
     * 3. Comment   : 권한 추가
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:45:14
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value="/admin/author/insertAuthorMgt.do")
    public String insertAuthorMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);
    	UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
    	param.put("ss_user_no", userInfoVo.getUserNo());
    	authorMgtService.insertAuthor(param);
        model.addAttribute("param", param);
        MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

        return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/author/selectPageListAuthorMgt.do", null, model);
    }

    /**
     *
     * <PRE>
     * 1. MethodName : updateFormAuthorMgt
     * 2. ClassName  : AuthorMgtController
     * 3. Comment   : 권한 수정 폼
     * 4. 작성자    : Ahn So Young
     * 5. 작성일    : 2020. 6. 10. 오전 11:00:18
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value="/admin/author/updateFormAuthorMgt.do")
    public String updateFormAuthorMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

    	// 권한 조회
    	DataMap resultMap = authorMgtService.selectAuthor(param);

		// 권한 사용자 조회 - 사용자 상태 코드
		param.put("group_id", Const.UPCODE_USER_STTUS);
		param.put("user_se_code", 10);

		//2018.02.23 Juseong 처음 페이지 로딩 시 상태를 활동으로 변경 후 처리
		if(request.getParameter("sch_user_sttus_code") == null){
			param.put("sch_user_sttus_code", Const.USER_STTUS_CODE_ACTIVE);
		}

    	model.addAttribute("resultMap", resultMap);
        model.addAttribute("param", param);

        return "admin/author/updateFormAuthorMgt";
    }

    /**
    *
    * <PRE>
    * 1. MethodName : updateFormAuthorMgtAjax
    * 2. ClassName  : AuthorMgtController
    * 3. Comment   : 권한 수정 폼 ajax
    * 4. 작성자    : Ahn So Young
    * 5. 작성일    : 2020. 6. 11. 오전 10:43:23
    * </PRE>
    *   @return String
    *   @param request
    *   @param response
    *   @param model
    *   @return
    *   @throws Exception
    */
    @RequestMapping(value="/admin/author/updateFormAuthorMgtAjax.do")
    public @ResponseBody DataMap updateFormAuthorMgtAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

		//2018.02.23 Juseong 처음 페이지 로딩 시 상태를 활동으로 변경 후 처리
		if(request.getParameter("sch_user_sttus_code") == null){
			param.put("sch_user_sttus_code", Const.USER_STTUS_CODE_ACTIVE);
		}

		param.put("group_id", Const.UPCODE_USER_STTUS);

		// ### Pasing 시작 ### */
		int totCnt = authorMgtService.selectTotCntUser(param);
    	param.put("totalCount", totCnt);
    	param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = authorMgtService.selectPageListUser(param);
		//### Pasing 종료 ### */

		DataMap resultJSON = new DataMap();

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultJSON.put("resultStats", resultStats);
		resultJSON.put("resultList", resultList);

		return resultJSON;
    }

    /**
     * <PRE>
     * 1. MethodName : updateAuthorMgt
     * 2. ClassName  : AuthorMgtController
     * 3. Comment   : 권한 수정
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:45:38
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value="/admin/author/updateAuthorMgt.do")
    public String updateAuthorMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);
    	UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
    	param.put("ss_user_no", userInfoVo.getUserNo());

    	authorMgtService.updateAuthor(param);
        model.addAttribute("param", param);
        MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

        //redirect param 설정
        DataMap resultParam = new DataMap();
        resultParam.put("author_id", param.getString("author_id"));
        return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/author/updateFormAuthorMgt.do", resultParam, model);
    }


    /**
     * <PRE>
     * 1. MethodName : deleteAuthorMgt
     * 2. ClassName  : AuthorMgtController
     * 3. Comment   : 권한 삭제
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:45:47
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value="/admin/author/deleteAuthorMgt.do")
    public String deleteAuthorMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);
    	authorMgtService.deleteAuthor(param);
        model.addAttribute("param", param);
        MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

        return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/author/selectPageListAuthorMgt.do", null, model);
    }


    /**
     * <PRE>
     * 1. MethodName : selectExistYnAuthorMgtAjax
     * 2. ClassName  : AuthorMgtController
     * 3. Comment   : 권한 중복 체크
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:45:54
     * </PRE>
     *   @return void
     *   @param request
     *   @param response
     *   @param model
     *   @throws Exception
     */
    @RequestMapping(value="/admin/author/selectExistYnAuthorMgtAjax.do")
	public @ResponseBody DataMap selectExistYnAuthorMgtAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_id", userInfoVo.getId());

		String existYn = authorMgtService.selectExistYnAuthor(param);
		model.addAttribute("param", param);

		DataMap resultJSON = new DataMap();
		DataMap resultMsg = new DataMap();
		resultMsg.put("existYn", existYn);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", resultMsg);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

}