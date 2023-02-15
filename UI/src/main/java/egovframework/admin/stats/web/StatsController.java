package egovframework.admin.stats.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.admin.stats.service.StatsService;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.DateUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.RequestUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : StatsController.java
 * 3. Package  : egovframework.admin.user.web
 * 4. Comment  :
 * 5. 작성자   : JJH
 * 6. 작성일   : 2015. 12. 7. 오후 4:57:45
 * 7. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    JJH : 2015. 12. 7. :            : 신규 개발.
 * </PRE>
 */
@Controller
public class StatsController {

	private static Log log = LogFactory.getLog(StatsController.class);

	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

    @Resource(name = "statsService")
    private StatsService statsService;

    /** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListAdminEventLog
	 * 2. ClassName  : StatsController
	 * 3. Comment   : 관리자 이벤트 로그 목록조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오후 8:26:36
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/stats/selectPageListAdminEventLog.do")
	public String selectPageListAdminEventLog(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		param.put("sch_start_event_de", param.getString("sch_start_event_de", DateUtil.getToday()));
		param.put("sch_end_event_de", param.getString("sch_end_event_de", DateUtil.getToday()));

		param.put("sch_start_event_de_tmp", param.getString("sch_start_event_de").replaceAll("\\.", ""));
		param.put("sch_end_event_de_tmp", param.getString("sch_end_event_de").replaceAll("\\.", ""));

		/* ### Pasing 시작 ### */
		int totCnt = statsService.selectTotCntAdminEventLog(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = statsService.selectPageListAdminEventLog(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("totalCount", totCnt);
		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "admin/stats/selectPageListAdminEventLog";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListErrorEventLog
	 * 2. ClassName  : StatsController
	 * 3. Comment   : 에러 이벤트 로그 목록조회
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 4. 오후 5:8:11
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/admin/stats/selectPageListErrorEventLog.do")
	public String selectPageListErrorEventLog(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		param.put("sch_start_event_de", param.getString("sch_start_event_de", DateUtil.getToday()));
		param.put("sch_end_event_de", param.getString("sch_end_event_de", DateUtil.getToday()));

		param.put("sch_start_event_de_tmp", param.getString("sch_start_event_de").replaceAll("\\.", ""));
		param.put("sch_end_event_de_tmp", param.getString("sch_end_event_de").replaceAll("\\.", ""));

		/* ### Pasing 시작 ### */
		int totCnt = statsService.selectTotCntErrorEventLog(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = statsService.selectPageListErrorEventLog(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("totalCount", totCnt);
		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "admin/stats/selectPageListErrorEventLog";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListUV
	 * 2. ClassName  : StatsController
	 * 3. Comment   : UV 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 9:59:58
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = {"/admin/stats/selectListUV.do", "/admin/stats/{sys}/selectListUV.do"})
    public String selectListUV(@PathVariable(name = "sys", required = false) String sys,  HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		param.put("sch_start_de", param.getString("sch_start_de", DateUtil.getToday()));
		param.put("sch_end_de", param.getString("sch_end_de", DateUtil.getToday()));

		param.put("sch_start_de_tmp", param.getString("sch_start_de").replaceAll("\\.", ""));
		param.put("sch_end_de_tmp", param.getString("sch_end_de").replaceAll("\\.", ""));

		List resultList = statsService.selectListUV(param);

		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "admin/stats/selectListUV";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListPV
	 * 2. ClassName  : StatsController
	 * 3. Comment   : PV 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:00:20
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/admin/stats/selectListPV.do", "/admin/stats/{sys}/selectListPV.do" })
    public String selectListPV(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		param.put("sch_start_de", param.getString("sch_start_de", DateUtil.getToday()));
		param.put("sch_end_de", param.getString("sch_end_de", DateUtil.getToday()));

		param.put("sch_start_de_tmp", param.getString("sch_start_de").replaceAll("\\.", ""));
		param.put("sch_end_de_tmp", param.getString("sch_end_de").replaceAll("\\.", ""));

		List resultList = statsService.selectListPV(param);

		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "admin/stats/selectListPV";
	}


	/**
	 * <PRE>
	 * 1. MethodName : selectListSatisfactionStatus
	 * 2. ClassName  : StatsController
	 * 3. Comment   : 만족도 현황 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:00:44
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = {"/admin/stats/selectListSatisfactionStatus.do", "/admin/stats/{sys}/selectListSatisfactionStatus.do"})
	public String selectListSatisfactionStatus(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		/* ### Pasing 시작 ### */
		param.put("sch_start_de", param.getString("sch_start_de", DateUtil.getToday()));
		param.put("sch_end_de", param.getString("sch_end_de", DateUtil.getToday()));

		param.put("sch_start_de_tmp", param.getString("sch_start_de").replaceAll("\\.", ""));
		param.put("sch_end_de_tmp", param.getString("sch_end_de").replaceAll("\\.", ""));

		param.put("up_code_sys", Const.UPCODE_SYS_CODE);

		int totCnt = statsService.selectTotCntSatisfactionStatus(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		/* ### Pasing 끝 ### */

		List resultList = statsService.selectListSatisfactionStatus(param);

		model.addAttribute("param", param);
		model.addAttribute("resultList", resultList);

		return "admin/stats/selectListSatisfactionStatus";
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListSatisfactionAvg
	 * 2. ClassName  : StatsController
	 * 3. Comment   : 만족도 평균 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:00:59
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value = { "/admin/stats/selectListSatisfactionAvg.do", "/admin/stats/{sys}/selectListSatisfactionAvg.do" })
	public String selectListSatisfactionAvg(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		/* ### Pasing 시작 ### */
		param.put("sch_start_de", param.getString("sch_start_de", DateUtil.getToday()));
		param.put("sch_end_de", param.getString("sch_end_de", DateUtil.getToday()));

		param.put("sch_start_de_tmp", param.getString("sch_start_de").replaceAll("\\.", ""));
		param.put("sch_end_de_tmp", param.getString("sch_end_de").replaceAll("\\.", ""));

		param.put("up_code_sys", Const.UPCODE_SYS_CODE);

		int totCnt = statsService.selectTotCntSatisfactionAvg(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		/* ### Pasing 끝 ### */

		List resultList = statsService.selectListSatisfactionAvg(param);

		model.addAttribute("param", param);
		model.addAttribute("resultList", resultList);

		return "admin/stats/selectListSatisfactionAvg";
	}
}
