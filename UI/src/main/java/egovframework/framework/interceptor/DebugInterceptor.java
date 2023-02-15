/**
 *
 * 1. FileName : DebugInterceptor.java
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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : DebugInterceptor.java
 * 3. Package  : egovframework.framework.interceptor
 * 4. Comment  :
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 27. 오후 6:40:06
 * </PRE>
 */
public class DebugInterceptor extends HandlerInterceptorAdapter {

	private static Log log = LogFactory.getLog(DebugInterceptor.class);


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String userAgent = request.getHeader("User-Agent");
		log.debug("@@@@@@@@@@@@:" + userAgent);
		if (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1) {
			log.debug("Adding response header for IE");
			response.addHeader("X-UA-Compatible", "IE=edge,chrome=1");
		}

		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // Proxies.

		UrlPathHelper urlPathHelper = new UrlPathHelper();

		String printYn = "N";
		if (EgovPropertiesUtil.getProperty("interceptor.log.print.yn") != null) {
			printYn = EgovPropertiesUtil.getProperty("interceptor.log.print.yn");
		}

		if (printYn.equals("Y")) {

			log.debug("################################################################################");
			log.debug("##" + (String) (request.getAttribute("javax.servlet.include.request_uri") == null ? urlPathHelper.getOriginatingRequestUri(request) : request.getAttribute("javax.servlet.include.request_uri")));
			log.debug("################################################################################");

			int initBlankCnt = 15;
			String space = "";
			int keyNameLenghtCnt = 0;
			String valueStr = "";

			Enumeration<?> e = request.getParameterNames();

			log.debug("--------------------------------------------------------------------------------");
			log.debug("|Name           |Value");
			log.debug("--------------------------------------------------------------------------------");

			while(e.hasMoreElements()){
				space = "";
				valueStr = "";

				String key = (String) e.nextElement();
				String[] values = request.getParameterValues(key);

				keyNameLenghtCnt = (key).length();

				for (int i = 0; i < initBlankCnt - keyNameLenghtCnt; i++) {
					space += " ";
				}

				if (values.length > 1) {
					for (int i = 0; i < values.length; i++) {
						valueStr+="{"+RequestUtil.xssServletFilter(request, key, values[i])+"}";
					}
					log.debug("|"+key+space+"|"+valueStr);
				} else {
					if (log.isDebugEnabled()) {
						log.debug("|"+key+space+"|"+RequestUtil.xssServletFilter(request, key, request.getParameter(key)));
					}
				}
				log.debug("--------------------------------------------------------------------------------");
			}
		}
		return true;
	}
}
