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

import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.RequestUtil;
import egovframework.pharmai.manufacturing.service.ChoiceFormulationListService;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : ChoiceFormulationListController.java
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
public class ChoiceFormulationListController {

	private static Log log = LogFactory.getLog(ChoiceFormulationListController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "choiceFormulationListService")
	private ChoiceFormulationListService choiceFormulationListService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListChoiceFormulation
	 * 2. ClassName  : ManufacturingMgtController
	 * 3. Comment   : 제조 리스트 조회
	 * 4. 작성자	: kimsm
	 * 5. 작성일	: 2021. 8. 09
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pharmai/chemical/manufacturing/selectPageListChoiceFormulation.do")
	public String selectPageListChoiceFormulation(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		/* ### Pasing 시작 ### */
		int totCnt = choiceFormulationListService.selectCountChoiceFormulation(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = choiceFormulationListService.selectPageListChoiceFormulation(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("totalCount", totCnt);
		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "pharmai/manufacturing/selectPageListChoiceFormulation";
	}


}
