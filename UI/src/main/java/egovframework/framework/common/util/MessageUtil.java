/**
 *
 * 1. FileName : MessageUtil.java
 * 2. Package : egovframework.framework.common.util
 * 3. Comment : 
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 28. 오후 4:15:23
 * 6. 변경이력 : 
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 28. :            : 신규 개발.
 */

package egovframework.framework.common.util;

import egovframework.framework.common.util.EgovMessageSource;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : MessageUtil.java
 * 3. Package  : egovframework.framework.common.util
 * 4. Comment  : 
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 28. 오후 4:15:23
 * </PRE>
 */

public class MessageUtil {
	
	@Resource(name="egovMessageSource")
    private static EgovMessageSource egovMessageSource;

	public static void setMessage(HttpServletRequest request, String message) {
		request.getSession().setAttribute("message", message);
	}
}
