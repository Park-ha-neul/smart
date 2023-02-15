/**
 *
 * 1. FileName : LoginCheckInterceptor.java
 * 2. Package : egovframework.framework.interceptor
 * 3. Comment :
 * 4. 작성자  : venus
 * 5. 작성일  : 2013. 8. 27. 오후 6:40:06
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    venus : 2013. 8. 27. :            : 신규 개발.
 */

package egovframework.framework.interceptor;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.CookieUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : LoginCheckInterceptor.java
 * 3. Package  : egovframework.framework.interceptor
 * 4. Comment  :
 * 5. 작성자   : venus
 * 6. 작성일   : 2013. 8. 27. 오후 6:40:06
 * </PRE>
 */
public class AuthCheckInterceptor extends HandlerInterceptorAdapter {

	private static Log log = LogFactory.getLog(AuthCheckInterceptor.class);

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="egovMessageSource")
	private EgovMessageSource egovMessageSource;

	/**
	 * <PRE>
	 * 1. MethodName : preHandle
	 * 2. ClassName  : AuthCheckInterceptor
	 * 3. Comment   : 권환관리 인터셉터
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 6. 14. 오후 3:54:54
	 * </PRE>
	 *   @param request
	 *   @param response
	 *   @param handler
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub

		UrlPathHelper urlPathHelper = new UrlPathHelper();

		// session검사
		HttpSession session = request.getSession();

		UserInfoVo userInfoVo = (UserInfoVo) session.getAttribute("userInfoVo");

		String requestUrl = (String) (request.getAttribute("javax.servlet.include.request_uri") == null ? urlPathHelper.getOriginatingRequestUri(request) : request.getAttribute("javax.servlet.include.request_uri"));

		if (userInfoVo != null && userInfoVo.getId() != null && !userInfoVo.getId().equals("")) {
			DataMap dataMap = new DataMap();
			dataMap.put("ss_user_no", userInfoVo.getUserNo());
			dataMap.put("request_url", requestUrl);
			String accYn = commonMybatisDao.selectOne("common.selectUserAcessMenu", dataMap);

			//게시판 접근 시 권한 확인
			String url[] = requestUrl.split("/");
			String bbsAccYn = null;
			if("bbs".equals(url[2])) {
				// 게시판 코드와 사용자 번호로 게시판 접근 권한 체크
				DataMap param = RequestUtil.getDataMap(request);
				param.put("ss_user_no", userInfoVo.getUserNo());
				bbsAccYn = commonMybatisDao.selectOne("common.selectUserBbsAuth", param);

				if(!bbsAccYn.equals("Y")) {
					log.debug("##Deny Access Bbs##");
					MessageUtil.setMessage(request, egovMessageSource.getMessage("error.access.menu"));
					response.sendRedirect(requestUrl);
					return false;
				}
			}

			if(log.isDebugEnabled()){
				log.debug("###request.getRequestURI():"+request.getRequestURI());
			}

			if (accYn.equals("Y")) {
				log.debug("##Allow Auth Menu##");
				return true;
			} else {

				boolean ajaxFlag = false;
				if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
					ajaxFlag = true;
				}

				if (ajaxFlag) { // Ajax Call 일경우
					DataMap resultStats = new DataMap();
					resultStats.put("resultCode", "error");
					resultStats.put("resultMsg", egovMessageSource.getMessage("error.access.menu"));
					JSONObject resultJSON = new JSONObject();
					resultJSON.put("resultStats", resultStats);
					response.setContentType("application/json; charset=utf-8");
					try {
						response.getWriter().write(resultJSON.toString());
					} catch (IOException e) {
						log.error(e);
					}
				} else {
					MessageUtil.setMessage(request, egovMessageSource.getMessage("error.access.menu"));

					request.getSession().removeAttribute("userInfoVo");
					request.getSession().invalidate();

					CookieUtil.removeCookie(request, response, "autoLoginId");
					CookieUtil.removeCookie(request, response, "autoLoginPw");

					response.sendRedirect(Globals.DO_LOGIN);
				}

				if (log.isDebugEnabled()) {
					log.debug("##Not Auth Menu##");
				}
				return false;
			}
		} else {

			MessageUtil.setMessage(request, egovMessageSource.getMessage("msg.do.login"));
			response.sendRedirect(Globals.DO_LOGIN);
			if (log.isDebugEnabled()) {
				log.debug("##Session is Dead##");
			}
			return false;
		}
	}
}
