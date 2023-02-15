package egovframework.bio.manufacturing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.RequestUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : ManufacturingMgtController.java
 * 3. Package  : egovframework.pharmai.manufacturing.web
 * 4. Comment  :
 * 5. 작성자   : JJH
 * 6. 작성일   : 2015. 12. 7. 오후 4:57:45
 * 7. 변경이력 :
 *	이름	 : 일자		  : 근거자료   : 변경내용
 *	------------------------------------------------------
 *	JJH : 2015. 12. 7. :			: 신규 개발.
 * </PRE>
 */
@Controller
public class BioManufacturingController {
	@RequestMapping(value = "/pharmai/bio/manufacturing/selectManu_START.do")
	public String selectManufacturing_START(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		return "bio/manufacturing/selectManu_START";
	}

}
