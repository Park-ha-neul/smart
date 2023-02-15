package egovframework.bio.manufacturing.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.bio.manufacturing.service.BioChoiceFormulationListService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.RequestUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BioChoiceFormulationListController.java
 * 3. Package  : egovframework.bio.manufacturing.web
 * 4. Comment  :
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 04. 29
 * </PRE>
 */
@Controller
public class BioChoiceFormulationController {

	@Resource(name = "bioChoiceFormulationListService")
	private BioChoiceFormulationListService bioChoiceFormulationListService;
	
	// 바이오의약품 제조 시작하기
	@RequestMapping(value = "/pharmai/bio/manufacturing/selectPageListChoiceFormulation.do")
	public String selectPageListChoiceFormulation(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		/* ### Pasing 시작 ### */
//		int totCnt = bioChoiceFormulationListService.selectTotCntStep_06(param);
		int totCnt = bioChoiceFormulationListService.selectPageCountChoiceFormulation(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = bioChoiceFormulationListService.selectPageListChoiceFormulation(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("totalCount", totCnt);
		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "bio/manufacturing/selectPageListChoiceFormulation";
	}
}
