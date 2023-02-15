/**
 * 
 *
 * 1. FileName : SessionUtil.java
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import egovframework.admin.common.vo.UserInfoVo;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : SessionUtil.java
 * 3. Package  : egovframework.framework.common.util
 * 4. Comment  : 
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 28. 오후 7:19:28
 * </PRE>
 */
public class SessionUtil {
	
	public static UserInfoVo getSessionUserInfoVo(HttpServletRequest request){
		
		UserInfoVo userInfoVo = null;
		HttpSession session = request.getSession(false);
		
		if(session != null && session.getAttribute("userInfoVo") != null){
			userInfoVo = (UserInfoVo)session.getAttribute("userInfoVo");
		}		
		return userInfoVo;		
	}
}
