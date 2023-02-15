package egovframework.bio.formulation.web;

import java.io.IOException;
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

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.bio.formulation.service.BioFormulationService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.DateUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import net.sf.json.JSONObject;

/**
 *
 * <PRE>
 * 1. ClassName : BioFormulationController
 * 2. FileName  : formulationController.java
 * 3. Package  : egovframework.pharmAi.formulation.web
 * 4. Comment  : 바이오의약품 제형 > Formulation 불러오기 Controller
 * 5. 작성자   : : hnpark
 * 6. 작성일   : 2022. 4. 21. 오후 4:21
 * </PRE>
 */
@Controller
public class BioFormulationController {

	private static Log log = LogFactory.getLog(BioFormulationController.class);
//
//	@Resource(name = "egovMessageSource")
//	private EgovMessageSource egovMessageSource;
//
	@Resource(name = "bioFormulationService")
	private BioFormulationService bioFormulationService;
//
//	@Resource(name = "NtsysFileMngUtil")
//	private NtsysFileMngUtil ntsysFileMngUtil;
//
//	@Resource(name = "NtsysFileMngService")
//	private NtsysFileMngService ntsysFileMngService;
//
//	/*공통영역  시작*/
//	/**
//	 * <PRE>
//	 * 1. MethodName : selectProject
//	 * 2. ClassName  : ProjectController
//	 * 3. Comment   : 프로젝트 리스트
//	 * 4. 작성자    : KSM
//	 * 5. 작성일    : 2021. 6. 23. 오후 3:30:40
//	 * </PRE>
//	 *
//	 * @return String
//	 * @param request
//	 * @param response
//	 * @param model
//	 * @return
//	 * @throws Exception
//	 */
	@RequestMapping(value = "/pharmai/bio/formulation/selectPageListProject.do")
	public String selectPageListProject(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		System.out.println("selectPageListProject start !!!!!!!!");
		DataMap param = RequestUtil.getDataMap(request);

		// 최초 리스트 페이지 진입시 초기 셋팅
		param.put("sch_disp_begin_de", param.getString("sch_disp_begin_de", DateUtil.addDay(DateUtil.getToday(), -21)));
		param.put("sch_disp_end_de", param.getString("sch_disp_end_de", DateUtil.getToday()));

		param.put("sch_disp_begin_de_tmp", param.getString("sch_disp_begin_de").replaceAll("\\.", ""));
		param.put("sch_disp_end_de_tmp", param.getString("sch_disp_end_de").replaceAll("\\.", ""));

		/* ### Pasing 시작 ### */
		int totCnt = bioFormulationService.selectTotCntProject(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = bioFormulationService.selectPageListProject(model, param);
		/* ### Pasing 끝 ### */

		model.addAttribute("totalCount", totCnt);
		model.addAttribute("param", param);
		model.addAttribute("resultList", resultList);

		return "bio/formulation/selectPageListProject";
	}
	
	@RequestMapping(value = "/pharmai/bio/formulation/copyPrjct.do")
	public String copyPrjct(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		bioFormulationService.copyPrjct(param);

		return "redirect: selectPageListProject.do";
	}

	@RequestMapping(value = { "/pharmai/bio/formulation/updateChoicePrjctAjax.do" })
	public @ResponseBody void updateChoicePrjctAjax(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", userInfoVo.getCur_step_cd()));
		param.put("userNo", userInfoVo.getUserNo());

		bioFormulationService.updateChoicePrjct(param);

		userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
		userInfoVo.setCur_prjct_type(param.getString("prjct_type"));
		userInfoVo.setCur_step_cd(param.getString("status"));

		request.getSession().setAttribute("userInfoVo", userInfoVo);

		JSONObject resultJSON = new JSONObject();

		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "ok");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);

		response.setContentType("text/plain; charset=utf-8");
		try {
			response.getWriter().write(resultJSON.toString());
		} catch (IOException e) {
			log.error(e);
		}
	}

	// 바이오의약품 제형설계 첫 화면 넘기기
	@RequestMapping(value = "/pharmai/bio/formulation/selectFormulation_START.do")
	public String formulationStart(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		System.out.println("start!!!!");

		return "bio/formulation/selectFormulation_START";
	}

	@RequestMapping(value = "/pharmai/bio/formulation/deleteFormulation.do")
	public String deleteFormulation(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		bioFormulationService.updatePrjMst(param);

		if(param.getString("menuName").equals("formulation")) {
			return "redirect:selectPageListProject.do";
		}else {
			return "redirect:/pharmai/bio/manufacturing/selectPageListManufacturing.do";
		}


	}

	/* 공통영역 종료 */

}
