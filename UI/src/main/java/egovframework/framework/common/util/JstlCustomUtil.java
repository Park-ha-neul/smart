package egovframework.framework.common.util;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : JstlCustomUtil.java
 * 3. Package  : egovframework.framework.common.util
 * 4. Comment  : JSTL 사용자 추가 함수
 * 5. 작성자   : JJH
 * 6. 작성일   : 2016. 9. 21. 오전 10:56:34
 * </PRE>
 */
public class JstlCustomUtil extends EgovAbstractDAO {

	/**
	 * <PRE>
	 * 1. MethodName : getHtml
	 * 2. ClassName  : JstlCustomUtil
	 * 3. Comment   : html 표현식으로 변경
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2016. 9. 21. 오전 10:58:28
	 * </PRE>
	 *   @return String
	 *   @param str
	 *   @return
	 */
	public static String getHtml(String str){
		// 에디터 사용시 <br />\n이 들어감; \n은 빼줌
		str = str.replace("<br />\r\n", "<br />");

		str = str.replace("\r\n", "<br />");
		str = str.replace(" ", "&nbsp;");

		// 에디터 사용시 <br />가 생성이 되기 때문에 <br&nbsp;/>로 변환이 됨; 그래서 원복해줌
		str = str.replace("&nbsp;/>", " />");

		return str;
	}
}
