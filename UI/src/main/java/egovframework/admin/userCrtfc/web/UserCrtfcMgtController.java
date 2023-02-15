package egovframework.admin.userCrtfc.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.user.service.UserMgtService;
import egovframework.admin.userCrtfc.service.UserCrtfcMgtServiceImpl;
import egovframework.common.service.CodeCacheService;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.Base64Utils;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.TransReturnUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : UserCrtfcMgtController.java
 * 3. Package  : egovframework.admin.userCrtfc.web
 * 4. Comment  :
 * 5. 작성자   : 김성민
 * 6. 작성일   : 2021. 5. 14. 오후 2:10:45
 * 7. 변경이력 :
 *	이름	 : 일자		  : 근거자료   : 변경내용
 *	------------------------------------------------------
 *	김성민 : 2020. 5. 14. :			: 신규 개발.
 * </PRE>
 */
@Controller
public class UserCrtfcMgtController {

	private static Log log = LogFactory.getLog(UserCrtfcMgtController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	/**
	 * <PRE>
	 * 1. MethodName : ipin_main
	 * 2. ClassName  : UserCrtfcMgtController
	 * 3. Comment   : 아이핀 메인
	 * 4. 작성자	: 김성민
	 * 5. 작성일	: 2021. 5.14. 오후 14:10:26
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/userCrtfc/ipin_main.do")
	public String ipin_main(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		DataMap iPinCertify = new DataMap();

		String returnUrl = "/admin/userCrtfc/ipin_result.do";
		iPinCertify.put("returnURL", returnUrl);
	    //iPinCertify = UserCrtfcMgtServiceImpl.iPinMainMod(request, iPinCertify);

	    model.addAttribute("param", param);
	    model.addAttribute("iPinCertify", iPinCertify);

		return "admin/userCrtfc/ipin_main";
	}

	/**
	 * <PRE>
	 * 1. MethodName : ipin_process
	 * 2. ClassName  : UserCrtfcMgtController
	 * 3. Comment   : 아이핀 과정
	 * 4. 작성자	: 김성민
	 * 5. 작성일	: 2021. 5.14. 오후 14:10:26
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/userCrtfc/ipin_process.do")
	public String ipin_process(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		System.out.println("------- 과정 입장");

		response.setContentType("text/html; charset=euc-kr");
		return "admin/userCrtfc/ipin_process";
	}

	/**
	 * <PRE>
	 * 1. MethodName : ipin_result
	 * 2. ClassName  : UserCrtfcMgtController
	 * 3. Comment   : 아이핀 결과
	 * 4. 작성자	: 김성민
	 * 5. 작성일	: 2021. 5.14. 오후 14:10:26
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/userCrtfc/ipin_result.do")
	public String ipin_result(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		System.out.println("------- 결과 입장");

		response.setContentType("text/html; charset=euc-kr");
		return "admin/userCrtfc/ipin_result";
	}

}
