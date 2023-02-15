package egovframework.framework.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import egovframework.common.service.CommonCodeService;
import egovframework.common.service.IncludeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.RequestUtil;

public class CommonInterceptor extends HandlerInterceptorAdapter {

	private static Log log = LogFactory.getLog(CommonInterceptor.class);

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	/** CommonCodeService */
	@Resource(name = "includeService")
	private IncludeService includeService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	@Resource(name="egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 메뉴 관련
		DataMap param = RequestUtil.getDataMap(request);

		UrlPathHelper urlPathHelper = new UrlPathHelper();
		String url = urlPathHelper.getOriginatingRequestUri(request);
		param.put("url", url);

		DataMap resultMap = includeService.selectMenuByUrl(param);
		String topMenuId = Const.MENU_ID_TOP;
		if (resultMap != null) {
			topMenuId = resultMap.getString("MENU_ID").substring(0, 12);
			request.setAttribute("menu_id", resultMap.getString("MENU_ID"));
			request.setAttribute("cur_menu_nm", resultMap.getString("MENU_NM"));
		}

		request.setAttribute("top_menu_id", topMenuId);

		return true;
	}
}
