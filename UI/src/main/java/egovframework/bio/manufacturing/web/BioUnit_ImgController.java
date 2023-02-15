package egovframework.bio.manufacturing.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.bio.manufacturing.service.BioManufacturingMgtService;
import egovframework.bio.manufacturing.service.BioUnit_ImgService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BioUnit_ImgController.java
 * 3. Package  : egovframework.bio.manufacturing.service
 * 4. Comment  : 유닛 공정 이미지
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 4. 29
 * </PRE>
 */
@Controller
public class BioUnit_ImgController {
	
	@Resource(name = "bioManufacturingMgtService")
	private BioManufacturingMgtService bioManufacturingMgtService;
	
	@Resource(name = "bioUnit_ImgService")
	private BioUnit_ImgService bioUnit_ImgService;
	
	
	// step5 첫 화면 호출
	@RequestMapping(value = "/pharmai/bio/manufacturing/selectManu_UNIT_IMG.do")
	public String selectManu_UNIT_IMG(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "05"));
		param.put("userNo", userInfoVo.getUserNo());

		DataMap pjtData = bioManufacturingMgtService.selectPjtMst(param);

		String next_data = bioManufacturingMgtService.selectNextDataExt(param);
		param.put("next_data", next_data);

		param.put("step_new", "Y"); // select 후 분기 처리 필요

		List stp05List = bioUnit_ImgService.selectStp05List(param);

		if(stp05List.size() > 0) {
			param.put("step_new", "N");
			List distinctUnitList = bioUnit_ImgService.selectDistinctUnitList(param);
			List distinctCppList = bioUnit_ImgService.selectDistinctCppList(param);
			model.addAttribute("stp05List", stp05List);
			model.addAttribute("distinctUnitList", distinctUnitList);
			model.addAttribute("distinctCppList", distinctCppList);
		}else {
			param.put("step_new", "Y");
		}

		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "bio/manufacturing/selectManu_UNIT_IMG";
	}
	
	// step5 save 기능
	@RequestMapping(value = "/pharmai/bio/manufacturing/saveStep05.do")
	public String saveStep05(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		param.put("ss_user_no", userInfoVo.getUserNo());
		userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
		userInfoVo.setCur_prjct_type(param.getString("prjct_type"));
		userInfoVo.setCur_step_cd(param.getString("status"));

		request.getSession().setAttribute("userInfoVo", userInfoVo);

		// insert 및 insert 하기전 update(USE_YN = 'N')
		bioManufacturingMgtService.stepChangeFuncManu(param);

		bioUnit_ImgService.insertUnit_Img(param);

		//마지막 수정일
		bioManufacturingMgtService.updatePjt_mst(param);

		return "redirect: /pharmai/bio/manufacturing/selectManu_CPP_Factor.do";
	}

}
