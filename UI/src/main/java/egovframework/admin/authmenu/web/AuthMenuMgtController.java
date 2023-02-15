/**
 *
 *
 * 1. FileName : LoginController.java
 * 2. Package : egovframework.framework.security
 * 3. Comment :
 * 4. 작성자  : venus
 * 5. 작성일  : 2013. 8. 23. 오전 9:55:47
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    venus : 2013. 8. 23. :            : 신규 개발.
 */

package egovframework.admin.authmenu.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import egovframework.admin.authmenu.service.AuthMenuMgtService;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.menu.service.MenuMgtService;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.TransReturnUtil;


/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : AuthMenuMenuController.java
 * 3. Package  : egovframework.admin.authmenu.web
 * 4. Comment  : 메뉴 관리
 * 5. 작성자   : 김영배
 * 6. 작성일   : 2015. 9. 1. 오후 4:13:40
 * </PRE>
 */
@Controller
public class AuthMenuMgtController{

	private static Log log = LogFactory.getLog(AuthMenuMgtController.class);

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

	/** authMenuMgtService */
    @Resource(name = "authMenuMgtService")
    private AuthMenuMgtService authMenuMgtService;

    /** menuMgtService */
	@Resource(name = "menuMgtService")
	private MenuMgtService menuMgtService;

    /**
     * <PRE>
     * 1. MethodName : selectPageListAuthMenuMgt
     * 2. ClassName  : AuthMenuController
     * 3. Comment   : 권한별메뉴 페이지 리스트 조회
     * 4. 작성자    : venus
     * 5. 작성일    : 2013. 8. 29. 오후 2:34:05
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value="/admin/authmenu/selectPageListAuthMenuMgt.do")
    public String selectPageListAuthMenuMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);
    	/* ### Pasing 시작 ### */
    	int totCnt = authMenuMgtService.selectTotCntAuthMenu(param);
    	param.put("totalCount", totCnt);
    	param = pageNavigationUtil.createNavigationInfo(model, param);
        List resultList = authMenuMgtService.selectPageListAuthMenu(param);
        /* ### Pasing 끝 ### */

        model.addAttribute("resultList", resultList);
        model.addAttribute("param", param);
        return "admin/authmenu/selectPageListAuthMenuMgt";
    }

    /**
     * <PRE>
     * 1. MethodName : selectListAuthMenuMgt
     * 2. ClassName  : AuthMenuController
     * 3. Comment   : 권한별메뉴 리스트 조회
     * 4. 작성자    : venus
     * 5. 작성일    : 2013. 9. 3. 오후 3:22:10
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value="/admin/authmenu/selectListAuthMenuMgt.do")
    public String selectListAuthMenuMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

    	List parentMenuIdComboStr = menuMgtService.selectListParentMenuId(param);
		if (parentMenuIdComboStr.size() > 1) {
    		param.put("sch_parent_menu_id", param.getString("sch_parent_menu_id", ((DataMap)parentMenuIdComboStr.get(1)).getString("MENU_ID")));
		} else if (parentMenuIdComboStr.size() > 0) {
    		param.put("sch_parent_menu_id", param.getString("sch_parent_menu_id", ((DataMap)parentMenuIdComboStr.get(0)).getString("MENU_ID")));
		}

    	List resultList = authMenuMgtService.selectListAuthMenu(param);
        DataMap resultMap = authMenuMgtService.selectInfoAuthMenu(param);
        param.put("AUTHOR_NM", resultMap.getString("AUTHOR_NM"));

        model.addAttribute("parentMenuIdComboStr", parentMenuIdComboStr);

        model.addAttribute("resultList", resultList);
        model.addAttribute("param", param);

        return "admin/authmenu/selectListAuthMenuMgt";
    }

    /**
     * <PRE>
     * 1. MethodName : insertAuthMenuMgt
     * 2. ClassName  : AuthMenuController
     * 3. Comment   : 권한별메뉴 등록
     * 4. 작성자    : venus
     * 5. 작성일    : 2013. 8. 29. 오후 2:35:33
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value="/admin/authmenu/insertAuthMenuMgt.do")
    public String insertAuthMenuMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model, SessionStatus status) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

    	UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_id", userInfoVo.getUserNo());
    	authMenuMgtService.insertAuthMenu(param);
        MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));
		status.setComplete();

		//redirect param 설정
		DataMap resultParam = new DataMap();
		resultParam.put("author_id", param.getString("author_id"));
        return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/authmenu/selectListAuthMenuMgt.do", resultParam, model);
    }

}
