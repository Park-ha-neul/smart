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
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.DateUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BioManufacturingMgtController.java
 * 3. Package  : egovframework.bio.manufacturing.web
 * 4. Comment  :
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 5. 2
 * </PRE>
 */
@Controller
public class BioManufacturingMgtController {
	
	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;
	
	@Resource(name = "bioManufacturingMgtService")
	private BioManufacturingMgtService bioManufacturingMgtService;
	
	// 바이오의약품 불러오기 첫 화면
	@RequestMapping(value = "/pharmai/bio/manufacturing/selectPageListManufacturing.do")
	public String selectPageListManufacturing(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		// 최초 리스트 페이지 진입시 초기 셋팅
		param.put("sch_disp_begin_de", param.getString("sch_disp_begin_de", DateUtil.addDay(DateUtil.getToday(), -21)));
		param.put("sch_disp_end_de", param.getString("sch_disp_end_de", DateUtil.getToday()));

		param.put("sch_disp_begin_de_tmp", param.getString("sch_disp_begin_de").replaceAll("\\.", ""));
		param.put("sch_disp_end_de_tmp", param.getString("sch_disp_end_de").replaceAll("\\.", ""));

		/* ### Pasing 시작 ### */
		int totCnt = bioManufacturingMgtService.selectTotCntManufacturing(param);
		param.put("totalCount", totCnt);
		param = pageNavigationUtil.createNavigationInfo(model, param);
		List resultList = bioManufacturingMgtService.selectPageListManufacturing(param);
		/* ### Pasing 끝 ### */

		model.addAttribute("totalCount", totCnt);
		model.addAttribute("resultList", resultList);
		model.addAttribute("param", param);

		return "bio/manufacturing/selectPageListManufacturing";
	}
	
	// 바이오의약품 제조 프로젝트 복사
	@RequestMapping(value = "/pharmai/bio/manufacturing/copyPrjct.do")
	public String ManufacturingCopy(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		bioManufacturingMgtService.manufacturingCopyPrj(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.copy"));

		return "redirect: /pharmai/bio/manufacturing/selectPageListManufacturing.do";
	}
	
}
