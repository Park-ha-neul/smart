/**
 *
 * 1. FileName : LoginCheckInterceptor.java
 * 2. Package : egovframework.framework.interceptor
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 27. 오후 6:40:06
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 27. :            : 신규 개발.
 */

package egovframework.framework.interceptor;

import java.io.IOException;
import java.net.URLEncoder;

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
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : LoginCheckInterceptor.java
 * 3. Package  : egovframework.framework.interceptor
 * 4. Comment  :
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 27. 오후 6:40:06
 * </PRE>
 */
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

	private static Log log = LogFactory.getLog(LoginCheckInterceptor.class);

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		UrlPathHelper urlPathHelper = new UrlPathHelper();

		// session검사
		HttpSession session = request.getSession();
		UserInfoVo userInfoVo = (UserInfoVo)session.getAttribute("userInfoVo");

		String reqUrl = (String) (request.getAttribute("javax.servlet.include.request_uri") == null ? urlPathHelper.getOriginatingRequestUri(request) : request.getAttribute("javax.servlet.include.request_uri"));

		if (userInfoVo != null && userInfoVo.getId() != null && !userInfoVo.getId().equals("")) {
			if (log.isDebugEnabled()) {
				log.debug("################################################################################");
				log.debug("## User Session is Live");
				log.debug("################################################################################");
			}
			return true;
		}

		else {
			if (reqUrl.indexOf("Ajax") > 0) {
				DataMap resultStats = new DataMap();
				resultStats.put("resultCode", "error");
				resultStats.put("resultMsg", egovMessageSource.getMessage("msg.do.login"));
				JSONObject resultJSON = new JSONObject();
				resultJSON.put("resultStats", resultStats);
				response.setContentType("text/html; charset=utf-8");
				try {
					response.getWriter().write(resultJSON.toString());
				} catch (IOException e) {
					log.error(e);
				}
			} else {
				MessageUtil.setMessage(request, egovMessageSource.getMessage("msg.do.login"));
				response.sendRedirect(Globals.DO_LOGIN + "?ru=" + URLEncoder.encode(reqUrl, "UTF-8"));
			}

			if (log.isDebugEnabled()) {
				log.debug("################################################################################");
				log.debug("## User Session is Dead");
				log.debug("################################################################################");
			}
			return false;
		}
	}
}
