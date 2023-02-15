package egovframework.framework.common.util;

import org.springframework.ui.ModelMap;

import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;

public class TransReturnUtil {

	/**
	 * <PRE>
	 * 1. MethodName : returnPage
	 * 2. ClassName  : TransReturnUtil
	 * 3. Comment   : 입력, 수정, 삭제후 공통 리턴페이지 설정
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2020. 6. 5. 오전 9:12:25
	 * </PRE>
	 *   @return String
	 *   @param method
	 *   @param returnUrl
	 *   @param param
	 *   @param model
	 *   @return
	 * @throws Exception
	 */
	public static String returnPage(String method, String returnUrl, DataMap returnParam, ModelMap model) throws Exception {

		// return redirectStr
		String redirectStr = "";

		if (method.equals(Globals.METHOD_GET)) {
			// GET 방식
			String urlParam = SysUtil.createUrlParam(returnParam);
			redirectStr = "redirect:" + returnUrl + urlParam;
		} else if (method.equals(Globals.METHOD_POST)) {
			// POST 방식
			// 최종적으로 넘길 데이터
			DataMap formParam = new DataMap();
			formParam.put("method", method);
			formParam.put("redirectUrl", returnUrl);
			model.addAttribute("formParam", formParam);
			model.addAttribute("returnParam", returnParam);
			redirectStr = "common/redirect";
		} else {
			throw new Exception();
		}

		return redirectStr;
	}
}
