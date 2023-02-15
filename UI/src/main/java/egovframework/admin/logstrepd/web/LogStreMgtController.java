package egovframework.admin.logstrepd.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.admin.logstrepd.service.LogStreMgtService;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.TransReturnUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : LogStreMgtController.java
 * 3. Package  : egovframework.admin.logstrepd.web
 * 4. Comment  : 로그 보관 기간 설정
 * 5. 작성자   : 윤영수
 * 6. 작성일   : 2020. 05. 07. 오전 10:00
 * </PRE>
 */
@Controller
public class LogStreMgtController {

	private static Log log = LogFactory.getLog(LogStreMgtController.class);

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

    @Resource(name = "logStreMgtService")
    private LogStreMgtService logStreMgtService;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListLogStreMgt
	 * 2. ClassName  : LogStreMgtController
	 * 3. Comment   : 로그 보관 기간 설정 페이지
	 * 4. 작성자    : 윤영수
	 * 5. 작성일    : 2020. 05. 07. 오전 10:00
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/logstrepd/selectPageLogStreMgt.do")
	public String selectPageLogStreMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		// 로그 보관 기간 조회
		DataMap resultMap = logStreMgtService.selectLogStrePd();
		model.addAttribute("resultMap", resultMap);

		return "admin/logstrepd/selectPageLogStreMgt";
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateLogStreMgt
	 * 2. ClassName  : LogStreMgtController
	 * 3. Comment   : 로그 보관 기간 갱신
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 12. 오후 4:43:15
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = "/admin/logstrepd/updateLogStreMgt.do")
	public String updateLogStreMgt(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
    	param.put("ss_user_no", userInfoVo.getUserNo());

    	logStreMgtService.updateLogStreMgt(param);
    	MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.update"));

    	model.addAttribute("param", param);

//    	param.put("redirectUrl", "/admin/logstrepd/selectPageLogStreMgt.do");
//		return "common/redirect";

    	//redirect param 설정
    	DataMap resultParam = new DataMap();
    	resultParam.put("storage_pd", param.getString("storage_pd"));
		return TransReturnUtil.returnPage(Globals.METHOD_GET, "/admin/logstrepd/selectPageLogStreMgt.do", resultParam, model);
	}
}
