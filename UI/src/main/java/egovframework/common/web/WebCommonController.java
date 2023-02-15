/**
 *
 *
 * 1. FileName : WebCommonController.java
 * 2. Package : egovframework.common.web
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 23. 오전 9:55:47
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 23. :            : 신규 개발.
 */

package egovframework.common.web;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.common.service.WebCommonService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EmailUtil;
import egovframework.framework.common.util.HttpUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.SysUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : WebCommonController.java
 * 3. Package  : egovframework.common.web
 * 4. Comment  :
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 23. 오전 9:55:47
 * </PRE>
 */
@Controller
public class WebCommonController {

	private static Log log = LogFactory.getLog(WebCommonController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "webCommonService")
	private WebCommonService webCommonService;

	@Resource(name = "emailUtil")
	private EmailUtil emailUtil;

	@RequestMapping(value = "/web/useInfo.do")
	public String useInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		model.addAttribute("param", param);

		return "common/useInfo";
	}

	@RequestMapping(value = "/web/viewer.do")
	public String viewer(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		model.addAttribute("param", param);

		// 메일 테스트
		param.put("title", "메일 제목 입니다.");
		param.put("msg", "메일 테스트입니다");
		emailUtil.sendEmail(param, "insertOrder.vm", "jjh@ntsys.co.kr", null, "candler1202@naver.com");

		return "common/viewer";
	}

	@RequestMapping(value = "/common/close.do")
	public String close(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		return "common/close";
	}

	@RequestMapping(value = "/common/xmlToJsonAjax.do")
	public @ResponseBody DataMap xmlToJsonAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

//		param.put("xml_url", param.getStringOrgn("xml_url"));

		Map xmlMap = null;
		if (!param.getString("xml_url").equals("")) {
			xmlMap = HttpUtil.getXmlToMap(param.getString("xml_url"), "UTF-8");
		}

		DataMap resultJSON = new DataMap();
		resultJSON.put("resultMap", xmlMap);

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	@RequestMapping(value = "/common/noAccess.do")
	public String noAccess(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		param.put("ip", RequestUtil.getClientIp(request));
		model.addAttribute("param", param);

		return "common/noAccess";
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateThemaOptionAjax
	 * 2. ClassName  : WebCommonController
	 * 3. Comment   : 테마 설정 수정
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2020. 5. 20. 오후 5:47:07
	 * </PRE>
	 *
	 * @return DataMap
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/updateThemaOptionAjax.do")
	public @ResponseBody DataMap updateThemaOptionAjax(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		// 테마 설정 셋팅
		DataMap themeSetInfo = new DataMap();
		themeSetInfo.put("c1", param.getString("c1"));
		themeSetInfo.put("c2", param.getString("c2"));
		themeSetInfo.put("c3", param.getString("c3"));
		themeSetInfo.put("c4", param.getString("c4"));
		themeSetInfo.put("c5", param.getString("c5"));

		// 테마 타입에 따른 값 분리 저장
		// 각각의 인덱스별 클래스 위치
		// main-header : 0, body : 1, main-sidebar:2, brand-link:3, icheck:4
		String[] typeList = param.getString("theme").split(":");
		themeSetInfo.put("t1", typeList[0]); // main header
		themeSetInfo.put("t2", typeList[1]); // body
		themeSetInfo.put("t3", typeList[2]); // main side bar
		themeSetInfo.put("t4", typeList[3]); // main logo area
		themeSetInfo.put("t5", typeList[4]); // check box & radio
		themeSetInfo.put("l1", param.getString("logo")); // logo image

		// 테마설정 json string값으로 변경
		String themeSet = SysUtil.dataMapToJsonString(themeSetInfo);
		param.put("theme_set", themeSet);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		// 테마 설정 업데이트
		webCommonService.updateThemaOption(param);

		// 사용자 세션 테마 설정 업데이트
		userInfoVo.setThemaOption(themeSet);
		request.getSession().setAttribute("userInfoVo", userInfoVo);

		DataMap resultJSON = new DataMap();

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", egovMessageSource.getMessage("succ.data.update"));
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/template/excelDown.do")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		OutputStream os = response.getOutputStream();

		String pathTemplateName = "template/sample_template.xlsx";

		ClassPathResource cpr = new ClassPathResource(pathTemplateName);
		try (InputStream input = new BufferedInputStream(cpr.getInputStream())) {// 1

			response.setHeader("Content-Disposition", "atachment; filename=\"test_"+System.currentTimeMillis()+".xlsx\"");

			Context context = new Context();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "a");
			map.put("birthDate", "2022-05-18");
			map.put("payment", 20);
			map.put("bonus", 1);

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.add(map);
			context.putVar("employees", list);
			context.putVar("title", "Good");

			JxlsHelper.getInstance().processTemplate(input, os, context); // 3

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}
}
