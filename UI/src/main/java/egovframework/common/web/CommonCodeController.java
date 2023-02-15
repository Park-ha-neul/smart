/**
 * 0. Project  : 노원구청 모바일 통합 맞춤형 서비스구축 프로젝트
 *
 * 1. FileName : LoginController.java
 * 2. Package : egovframework.framework.security
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 23. 오전 9:55:47
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 23. :            : 신규 개발.
 */

package egovframework.common.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.RequestUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : CommonCodeController.java
 * 3. Package  : egovframework.common.web
 * 4. Comment  : 코드 관리
 * 5. 작성자   : ntsys
 * 6. 작성일   : 2013. 12. 3. 오전 9:48:48
 * </PRE>
 */
@Controller
public class CommonCodeController {

	private static Log log = LogFactory.getLog(CommonCodeController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	/** commonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;


	@RequestMapping(value = "/common/selectCodeListAjax.do")
	public @ResponseBody DataMap selectCodeListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultJSON = new DataMap();

		List resultList = commonCodeService.selectCodeList(param);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);
		resultJSON.put("resultList", resultList);

		return resultJSON;
	}
}
