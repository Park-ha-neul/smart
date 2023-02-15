package egovframework.pharmai.manufacturing.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.pharmai.manufacturing.service.ManufacturingMgtService;
import egovframework.pharmai.manufacturing.service.Unit_ImgService;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : Manufacturing_ImgController.java
 * 3. Package  : egovframework.pharmai.manufacturing.web
 * 4. Comment  :
 * 5. 작성자   : KSM
 * 6. 작성일   : 2021. 08. 09. 오후 3:43:45
 * 7. 변경이력 :
 *	이름	 : 일자		  : 근거자료   : 변경내용
 *	------------------------------------------------------
 *	KSM : 2021. 08. 9. :			: 신규 개발.
 * </PRE>
 */
@Controller
public class Unit_ImgController {

	private static Log log = LogFactory.getLog(Unit_ImgController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "unit_ImgService")
	private Unit_ImgService unit_ImgService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	@Resource(name = "manufacturingMgtService")
	private ManufacturingMgtService manufacturingMgtService;

	/**
	 * <PRE>
	 * 1. MethodName : selectManufacturing_IMG
	 * 2. ClassName  : ManufacturingController
	 * 3. Comment   : 제조 - 유닛 공정 이미지
	 * 4. 작성자	: KIM SM
	 * 5. 작성일	: 2021. 8. 17
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pharmai/chemical/manufacturing/selectManu_UNIT_IMG.do")
	public String selectManu_UNIT_IMG(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "05"));
		param.put("userNo", userInfoVo.getUserNo());

		DataMap pjtData = manufacturingMgtService.selectPjtMst(param);

		String next_data = manufacturingMgtService.selectNextDataExt(param);
		param.put("next_data", next_data);

		param.put("step_new", "Y"); // select 후 분기 처리 필요

		List stp05List = unit_ImgService.selectStp05List(param);

		if(stp05List.size() > 0) {
			param.put("step_new", "N");
			List distinctUnitList = unit_ImgService.selectDistinctUnitList(param);
			List distinctCppList = unit_ImgService.selectDistinctCppList(param);
			model.addAttribute("stp05List", stp05List);
			model.addAttribute("distinctUnitList", distinctUnitList);
			model.addAttribute("distinctCppList", distinctCppList);
		}else {
			param.put("step_new", "Y");
		}

		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "pharmai/manufacturing/selectManu_UNIT_IMG";
	}

	@RequestMapping(value = "/pharmai/manufacturing/saveStep05.do")
	public String saveStep05(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		param.put("ss_user_no", userInfoVo.getUserNo());
		userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
		userInfoVo.setCur_prjct_type(param.getString("prjct_type"));
		userInfoVo.setCur_step_cd(param.getString("status"));

		request.getSession().setAttribute("userInfoVo", userInfoVo);

		// insert 및 insert 하기전 update(USE_YN = 'N')
		manufacturingMgtService.stepChangeFuncManu(param);

		unit_ImgService.insertUnit_Img(param);

		//마지막 수정일
		manufacturingMgtService.updatePjt_mst(param);

		return "redirect: /pharmai/chemical/manufacturing/selectManu_CPP_Factor.do";
	}



}
