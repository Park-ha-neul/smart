/**
 *
 *
 * 1. FileName : TemplateController.java
 * 2. Package : egovframework.sample.web
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 23. 오전 9:55:47
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 23. :            : 신규 개발.
 */

package egovframework.sample.web;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.file.NtsysFileMngUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : TemplateController.java
 * 3. Package  : egovframework.sample.web
 * 4. Comment  :
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 23. 오전 9:55:47
 * </PRE>
 */
@Controller
public class SampleController {

	private static Log log = LogFactory.getLog(SampleController.class);

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	/**
	 * <PRE>
	 * 1. MethodName : sample
	 * 2. ClassName  : SampleController
	 * 3. Comment   : UI 샘플용 페이지
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 11. 오후 4:00:16
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	@RequestMapping(value="/sample/sample.do")
	public String sample(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		return "sample/template";
	}

	@RequestMapping(value="/sample/fileUpload.do")
	public String testUpload(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		return "sample/fileUpload";
	}

	@RequestMapping(value="/sample/testUploadAjax.do")
	public @ResponseBody DataMap testUploadAjax(MultipartHttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);

		//return 상태
		DataMap resultJSON = new DataMap();
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", "");
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	@RequestMapping(value="/sample/chart.do")
	public String chart(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		return "sample/chart";
	}

}
