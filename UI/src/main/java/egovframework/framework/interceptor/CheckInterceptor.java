package egovframework.framework.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : CheckInterceptor.java
 * 3. Package  : egovframework.framework.interceptor
 * 4. Comment  : 기본적인 체크 리스트
 * 5. 작성자   : JJH
 * 6. 작성일   : 2019. 3. 5. 오후 2:29:39
 * </PRE>
 */
public class CheckInterceptor extends HandlerInterceptorAdapter {

	private static Log log = LogFactory.getLog(CheckInterceptor.class);

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 접근 아이피 조회
		String ip = RequestUtil.getClientIp(request);

		DataMap param = new DataMap();
		param.put("ip", ip);

		// 관리자 사이트는 등록된 아이피만 접근 하도록 한다.
		String flagYn = commonMybatisDao.selectOne("common.selectAccessYn", param);

		// 접근 가능한 아이피인경우
		if(flagYn.equals("Y")){
			return true;
		} else {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("msg.do.login"));
			response.sendRedirect("/common/noAccess.do");
			return false;
		}
	}
}
