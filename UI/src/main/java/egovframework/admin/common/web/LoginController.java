package egovframework.admin.common.web;

import java.util.Iterator;
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
import org.springframework.web.bind.annotation.RequestMethod;

import egovframework.admin.common.service.LoginService;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.common.service.CommonCodeService;
import egovframework.common.service.IncludeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.CookieUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SeedScrtyUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.TransReturnUtil;
import egovframework.framework.common.util.session.EgovHttpSessionBindingListener;

@Controller
public class LoginController {

	private static Log log = LogFactory.getLog(LoginController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	/** adminLoginService */
	@Resource(name = "loginService")
	private LoginService loginService;

	@Resource(name = "includeService")
	private IncludeService includeService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;
	
	/**
	 * <PRE>
	 * 1. MethodName : registerCheckUseridAjax
	 * 2. Classname : UserMgtController
	 * 3. Comment : 사용자 아이디 중복 체크
	 * 4. 작성자 : 허웅
	 * 5. 작성일 : 2022.04.21
	 * </PRE>
	 */
	
	@RequestMapping(value = "/admin/registerCheckUserIdAjax.do", method = { RequestMethod.POST })
	public @ResponseBody DataMap registerCheckUserId(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		DataMap param = RequestUtil.getDataMap(request);
		
        DataMap resultJSON = new DataMap();
        
        int result = loginService.registerCheckUserId(param);
        
        if (result == 0) {
        	
        	resultJSON.put("resultMsg", "success");
        	
        } else {
        	
        	resultJSON.put("resultMsg", "exist");
        	
        }
        
		
		return resultJSON;
	}
	
	
	/**
	 * <PRE>
	 * 1. MethodName : registerUser
	 * 2. Classname : UserMgtController
	 * 3. Comment : 사용자 회원가입
	 * 4. 작성자 : 허웅
	 * 5. 작성일 : 2022.04.14
	 * </PRE>
	 */
	
	@RequestMapping(value = "/admin/registerUserAjax.do", method = { RequestMethod.POST })
	public @ResponseBody DataMap registerUser(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{
		
		DataMap param = RequestUtil.getDataMap(request);
		
        Iterator<String> keys = param.keySet().iterator();

        while( keys.hasNext() ){

            String key = keys.next();

            System.out.println( String.format("키 : %s, 값 : %s", key, param.get(key)) );

        }
        
        param.put("id", param.get("r_user_id"));
        param.put("pwd", param.get("r_password"));
        param.put("name", param.get("r_user_name"));
        
        DataMap resultJSON = new DataMap();
        
        
        loginService.registerUser(param);
        loginService.registerUserAuthor(param);
        
        resultJSON.put("resultMsg", "success");
        
		// 사용자 존재 여부 확인
//		String result = loginService.checkIfUserExists(param);
		
		// 사용자 존재 여부 = N
//		if (null == result) {
//			
//	        loginService.registerUser(param);
//	        loginService.registerUserAuthor(param);
//	        
//			resultJSON.put("resultMsg", "success");
//			
//			return resultJSON;
//
//		} else {
//			
//			resultJSON.put("resultMsg", "exist");
//			
//			return resultJSON;
//		}
        
		return resultJSON;
	}
	

	/**
	 * <PRE>
	 * 1. MethodName : login
	 * 2. ClassName  : LoginController
	 * 3. Comment   : 로그인
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 1. 오후 4:33:18
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/login.do")
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		UserInfoVo ssUserInfoVo = SessionUtil.getSessionUserInfoVo(request);

		DataMap param = RequestUtil.getDataMap(request);
		model.addAttribute("param", param);

		// 세선
		if (ssUserInfoVo != null && ssUserInfoVo.getId() != null && !ssUserInfoVo.getId().equals("")) {
			// 어드민 로그 이벤트 삽입
			insertAdminEventLog(request, ssUserInfoVo.getUserNo());
			return TransReturnUtil.returnPage(Globals.METHOD_GET, Globals.DO_HOME, null, model);
		}

		// 쿠키
		String autoLoginId = CookieUtil.getCookieValue(request, "autoLoginId");
		String autoLoginPw = CookieUtil.getCookieValue(request, "autoLoginPw");

		if (!autoLoginId.equals("") && !autoLoginPw.equals("")) {
			param.put("id", autoLoginId);
			param.put("pwd", SeedScrtyUtil.decryptText(autoLoginPw));
		}

		if (!param.getString("id").equals("") && !param.getString("pwd").equals("")) {
			// 사용자 존재 여부 확인
			String result = loginService.checkIfUserExists(param);
			// 사용자 존재 여부 = N
			if (null == result) {
				/*MessageUtil.setMessage(request, egovMessageSource.getMessage("error.common.login"));*/
				model.addAttribute("msg", egovMessageSource.getMessage("error.common.login"));
				return Globals.JSP_LOGIN;
			}

			int failCount = Integer.parseInt(result);
			int limitFailCnt = 5; // 최대 실패 제한 횟수

			// 사용자 정보 조회
			DataMap userInfoMap = loginService.selectUserInfo(param);

			// 로그인 아이디/비번이 일치하지는 경우
			if (userInfoMap != null) {

				// 접근 아이피 조회
				if (Globals.ACCESS_IP_YN.equals("Y")) {
					String ip = RequestUtil.getClientIp(request);

					DataMap dataParam = new DataMap();
					dataParam.put("ip", ip);
					boolean access = loginService.selectAccessYn(dataParam);
					if (!access) {
						DataMap returnParam = RequestUtil.getDataMap(request);
						returnParam.put("ip", RequestUtil.getClientIp(request));
						model.addAttribute("param", returnParam);

						return "common/noAccess.login";
					}
				}

				// 로그인 실패 제한 횟수초과
				if (failCount >= limitFailCnt) {
					String[] failCntArray = new String[1];
					failCntArray[0] = Integer.toString(limitFailCnt);
					/*MessageUtil.setMessage(request, egovMessageSource.getMessage("error.common.login.cnt.over", failCntArray));*/
					model.addAttribute("msg", egovMessageSource.getMessage("error.common.login.cnt.over", failCntArray));
					return Globals.JSP_LOGIN;
				}

				UserInfoVo userInfoVo = new UserInfoVo();

				userInfoVo.setUserNo(userInfoMap.getString("USER_NO"));
				userInfoVo.setId(userInfoMap.getString("USER_ID"));
				userInfoVo.setUserNm(userInfoMap.getString("USER_NM"));
				userInfoVo.setThemaOption(userInfoMap.getString("THEMA_OPTION"));
				userInfoVo.setCur_prjct_id(userInfoMap.getString("CUR_PRJCT_ID"));
				userInfoVo.setCur_step_cd(userInfoMap.getString("CUR_STEP_CD"));
				userInfoVo.setCur_prjct_type(userInfoMap.getString("CUR_PRJCT_TYPE"));

				param.put("ss_user_no", userInfoVo.getUserNo());
				List userAuthIdList = loginService.selectListUserAuthId(param);

				userInfoVo.setAuthorIdList(userAuthIdList);

				request.getSession().setAttribute("userInfoVo", userInfoVo);
				request.getSession().setMaxInactiveInterval(60 * 60);

				// 자동로그인 일경우 쿠키 굽기
				if (param.getString("checkAutoLogin").equals("on")) {
					CookieUtil.addCookie(request, response, 60 * 60 * 24 * 365, "autoLoginId", param.getString("id"));
					CookieUtil.addCookie(request, response, 60 * 60 * 24 * 365, "autoLoginPw", SeedScrtyUtil.encryptText(param.getString("pwd")));
				}

				// 로그인 실패 횟수 0으로 초기화
				param.put("fail_cnt", "0");
				loginService.updateLoginFailCount(param);

				// 권한별 시작 URL 정보
				String returnPage = "";
				String auth = String.valueOf(userAuthIdList.get(0));
				if (auth.equals(Const.CODE_AUTHOR_ADMIN)) {
					returnPage = Globals.DO_HOME;
				} else {
					returnPage = getDoHome(auth);
				}

				if (!param.getString("ru").equals("")) {
					returnPage = param.getString("ru");
				}

				if(Globals.DUPLLOGIN.equals("Y")) {
					// 중복로그인 방지를 위한 소스(아이디 기반으로 처리함)
					EgovHttpSessionBindingListener listener = new EgovHttpSessionBindingListener();
					request.getSession().setAttribute(userInfoVo.getId(), listener);
				}

				// 로그인시에 권한별 메뉴 정보를 모두 세션에 저장한다; 그래서 권한및 메뉴 추가시에는 로그아웃 하였다가 다시 로그인을 해야함
				// 기존에는 페이지 로드때마다 신규로 쿼리 조회를 하였지만 성능향상을 위해 변경
				DataMap p2 = new DataMap();
				p2.put("ss_user_no", userInfoVo.getUserNo());

				// CMS메뉴코드
				p2.put("up_menu_id", Const.MENU_ID_CMS);
				List<DataMap> topMenuList = includeService.selectTopMenuList(p2);
				request.getSession().setAttribute("topMenuList", topMenuList);

				// left 전체 메뉴 셋팅
				// 1depth 메뉴별 서브 메뉴 리스트 셋팅
				DataMap p3 = new DataMap();
				p3.put("ss_user_no", userInfoVo.getUserNo());

				DataMap leftMenuMap = new DataMap();

				for (DataMap topMenuInfo : topMenuList) {
					p3.put("up_menu_id", topMenuInfo.getString("MENU_ID"));
					// 해당 1depth 메뉴 하위메뉴 조회
					leftMenuMap.put(topMenuInfo.getString("MENU_ID"), includeService.selectLeftMenuList(p3));
				}

				request.getSession().setAttribute("leftMenuMap", leftMenuMap);

				// 어드민 로그 이벤트 삽입
				insertAdminEventLog(request, userInfoMap.getString("USER_NO"));
				return TransReturnUtil.returnPage(Globals.METHOD_GET, returnPage, null, model);
			} else {
				// 로그인 실패 횟수 증가(기존 실패횟수 + 1)
				param.put("fail_cnt", Integer.toString(failCount + 1));
				loginService.updateLoginFailCount(param);

				// 로그인 실패 제한 횟수초과
				if (failCount >= limitFailCnt) {
					String[] failCntArray = new String[1];
					failCntArray[0] = Integer.toString(limitFailCnt);
					/*MessageUtil.setMessage(request, egovMessageSource.getMessage("error.common.login.cnt.over", failCntArray));*/
					model.addAttribute("msg", egovMessageSource.getMessage("error.common.login.cnt.over", failCntArray));
				} else {
					// 로그인 화면으로 이동
					String[] failCntArray = new String[2];
					failCntArray[0] = Integer.toString(limitFailCnt);
					failCntArray[1] = Integer.toString(failCount + 1);
					/*MessageUtil.setMessage(request, egovMessageSource.getMessage("error.common.login.cnt", failCntArray));*/
					model.addAttribute("msg", egovMessageSource.getMessage("error.common.login.cnt", failCntArray));
				}
				return Globals.JSP_LOGIN;
			}
		} else {
			return Globals.JSP_LOGIN;
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : logout
	 * 2. ClassName  : LoginController
	 * 3. Comment   : 로그아웃
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 5. 16. 오후 5:20:41
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		request.getSession().invalidate();
		CookieUtil.removeCookie(request, response, "autoLoginId");
		CookieUtil.removeCookie(request, response, "autoLoginPw");
		return TransReturnUtil.returnPage(Globals.METHOD_GET, Globals.DO_LOGIN, null, model);

	}

	/**
	 * <PRE>
	 * 1. MethodName : initlUserPasswordCheckAjax
	 * 2. ClassName  : LoginController
	 * 3. Comment   : 비밀번호 초기화 여부 확인 Ajax
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 7. 오후 5:46:26
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/initlUserPasswordCheckAjax.do")
	public @ResponseBody DataMap initlUserPasswordCheckAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		String resultCode = "";
		String returnMsg = "";

		DataMap userInfoMap = loginService.initlUserPasswordCheck(param);

		DataMap resultJSON = new DataMap();

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", resultCode);
		resultStats.put("resultMsg", returnMsg);
		resultStats.put("userInfoMap", userInfoMap);
		resultJSON.put("resultStats", resultStats);

		response.setContentType("text/json; charset=utf-8");

		return resultJSON;

	}

	/**
	 * <PRE>
	 * 1. MethodName : insertAdminEventLog
	 * 2. ClassName  : LoginController
	 * 3. Comment   : PageViewInterceptor 제외된 로그인 로직 어드민 이벤트 로그 삽입
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 11. 오후 5:10:47
	 * </PRE>
	 *   @return void
	 */
	private void insertAdminEventLog(HttpServletRequest request, String userNo) {
		DataMap param = new DataMap();

		String reqUrl = request.getRequestURI();
		param.put("pageUrl", reqUrl);
		param.put("userIp", request.getRemoteAddr());
		param.put("ssUserNo", userNo);
		param.put("param", RequestUtil.getDataMap(request).toString());

		try {
			loginService.insertAdminEventLog(param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : getDoHome
	 * 2. ClassName  : LoginController
	 * 3. Comment   : 권한별 시작 URL 정보
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 4. 오후 5:31:59
	 * </PRE>
	 *
	 * @return String
	 * @param auth
	 * @return
	 * @throws Exception
	 */
	private String getDoHome(String auth) throws Exception {
		String[] authStr = auth.split("_"); // authStr[0] : ADMIN
											// authStr[1] : 시스템 코드 (ex. 10)
		String url = "";
		if(authStr.length > 1) {
			url = EgovPropertiesUtil.getProperty("do.auth." + authStr[1] + ".home");
		} else {

			DataMap param = new DataMap();
			param.put("author_id", auth);
			param.put("menu_level4", "4");
			param.put("menu_level5", "5");
			param.put("search_url", "selectPageList");

			url = loginService.selectAuthorMenuByUrl(param);
			if (url.equals("") || url.equals(null)) {
				url = Globals.DO_HOME;
			}

		}

		return url;

	}
}
