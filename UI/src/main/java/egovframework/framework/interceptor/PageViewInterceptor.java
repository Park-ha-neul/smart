/**
 *
 * 1. FileName : PageViewInterceptor.java
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : PageViewInterceptor.java
 * 3. Package  : egovframework.framework.interceptor
 * 4. Comment  :
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 27. 오후 6:40:06
 * </PRE>
 */
public class PageViewInterceptor extends HandlerInterceptorAdapter {

	private static Log log = LogFactory.getLog(PageViewInterceptor.class);

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		UrlPathHelper urlPathHelper = new UrlPathHelper();

		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
		}

		String reqUrl = (String) (request.getAttribute("javax.servlet.include.request_uri") == null ? urlPathHelper.getOriginatingRequestUri(request) : request.getAttribute("javax.servlet.include.request_uri"));
		DataMap param = RequestUtil.getDataMap(request);
		param.put("pageUrl", reqUrl);
		param.put("userIp", ip);
		param.put("param", RequestUtil.getDataMap(request).toString());

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		if (userInfoVo != null && reqUrl.indexOf("login.do") == -1) {
			param.put("ssUserNo", userInfoVo.getUserNo());

			log.debug("####param:" + param.toString());
			log.debug("####reqUrl:" + reqUrl);

			commonMybatisDao.insert("common.insertAdminEventLog", param);
		}

		return true;
	}
}
