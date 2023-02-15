package egovframework.framework.common.util;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.UrlPathHelper;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.object.DataMap;

public class ExceptionResolver extends AbstractHandlerExceptionResolver{

    /** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	private static Log log = LogFactory.getLog(ExceptionResolver.class);

	private String defaultErrorView;

	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	// controller 에서 나는 에러를 여기서 log를 남기기 위해 사용
	@Override
	public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exception) {

		exception.printStackTrace();

		ModelAndView model = new ModelAndView();
		model.setViewName(defaultErrorView);

		DataMap param = new DataMap();
		UserInfoVo ssUserInfoVo = SessionUtil.getSessionUserInfoVo(request);

		param.put("error", getStackTrace(exception));
		param.put("request_url", request.getRequestURI());
		param.put("ss_user_no", ssUserInfoVo.getUserNo());
		param.put("param", RequestUtil.getDataMap(request).toString());

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

		param.put("ip", ip);

		model.addObject("param", param);

		// 에러 이벤트 로그 남기기
		try {
			commonCodeService.insertErrorLog(param);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	public String getStackTrace(Exception e) {
		StringBuffer sBuf = new StringBuffer();
		sBuf.append(e);
		StackTraceElement[] trace = e.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			sBuf.append("\n " + trace[i]);
		}
		return sBuf.toString();
	}
}