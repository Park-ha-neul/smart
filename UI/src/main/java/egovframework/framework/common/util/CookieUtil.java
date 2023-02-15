/**
 * 
 *
 * 1. FileName : CookieUtil.java
 * 2. Package : egovframework.framework.common.util
 * 3. Comment : 
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 28. 오후 7:19:28
 * 6. 변경이력 : 
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 28. :            : 신규 개발.
 */

package egovframework.framework.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : CookieUtil.java
 * 3. Package  : egovframework.framework.common.util
 * 4. Comment  : 
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 28. 오후 7:19:28
 * </PRE>
 */
public class CookieUtil {
	
	private static Log log = LogFactory.getLog(CookieUtil.class);
	
	
	public static void addCookie(HttpServletRequest request, HttpServletResponse response, int maxAge, String cookieName, String cookieValue) {

		log.debug("####addCookie START ####::cookieName:"+cookieName+",cookieValue:"+cookieValue);
		
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(maxAge); //초단위
		cookie.setPath("/");

		response.addCookie(cookie);
	}
	
	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String domain, int maxAge, String path, String cookieName, String cookieValue) {

		log.debug("####addCookie START ####:domain:"+domain+",cookieName:"+cookieName+",cookieValue:"+cookieValue);
		
		Cookie cookie = new Cookie(cookieName, cookieValue);
		//cookie.setDomain(domain);
		cookie.setMaxAge(maxAge); //초단위
		cookie.setPath(path);

		response.addCookie(cookie);
	}

	public static String getCookieValue(HttpServletRequest request, String cookieName) {

		Cookie[] cookies = request.getCookies();
		String cookieValue = "";

		if(cookies != null){
			for (int i = 0; i < cookies.length; i++) {
				if (cookieName.equals(cookies[i].getName())) {
					cookieValue = cookies[i].getValue();
				}
			}
		}
		return cookieValue;
	}
	
	
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String killCookieName) {
		
		log.debug("####removeCookie START ####:killCookieName:"+killCookieName);

		Cookie killCookie = new Cookie(killCookieName, "");
		killCookie.setMaxAge(0); //초단위
		killCookie.setPath("/");
		response.addCookie(killCookie);
	}

	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String domain, String path, String killCookieName) {
		
		log.debug("####removeCookie START ####:domain:"+domain+",killCookieName:"+killCookieName);

		Cookie killCookie = new Cookie(killCookieName, "");
		killCookie.setDomain(domain);
		killCookie.setMaxAge(0); //초단위
		killCookie.setPath(path);
		response.addCookie(killCookie);
	}
}
