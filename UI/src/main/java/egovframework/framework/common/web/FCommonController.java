/**
 *
 * 1. FileName : FCommonController.java
 * 2. Package : egovframework.framework.common.web
 * 3. Comment :
 * 4. 작성자  : 조정현
 * 5. 작성일  : 2014. 8. 20. 오전 9:55:47
 * 6. 변경이력 :
 *	이름	 : 일자		  : 근거자료   : 변경내용
 *	------------------------------------------------------
 *	조정현 : 2014. 8. 20. :			: 신규 개발.
 */

package egovframework.framework.common.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.HttpUtil;
import egovframework.framework.common.util.RequestUtil;
import net.sf.json.JSON;
import net.sf.json.JSONException;
import net.sf.json.xml.XMLSerializer;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : FCommonController.java
 * 3. Package  : egovframework.framework.common.web
 * 4. Comment  :
 * 5. 작성자   : 조정현
 * 6. 작성일   : 2013. 10. 10. 오전 9:55:47
 * </PRE>
 */
@Controller
public class FCommonController {

	private static Log log = LogFactory.getLog(FCommonController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	/**
	 * <PRE>
	 * 1. MethodName : getDataCrossDomainXml
	 * 2. ClassName  :  CommonController
	 * 3. Comment   : 다른 도메인의 url을 호출하여 정보를 얻어올때(XML)
	 * 4. 작성자	: 조정현
	 * 5. 작성일	: 2013. 10. 10. 오후 5:21:08
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = "/fcommon/getDataCrossDomainXmlAjax.do")
	public @ResponseBody DataMap getDataCrossDomainXmlAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");

		DataMap param = RequestUtil.getDataMap(request);

//		String returnStr = HttpUtil.getHttpRtnData(param.getStringOrgn("url"), "UTF-8");
		String returnStr = HttpUtil.getHttpRtnData(param.getString("url"), "UTF-8");

		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON resultList = xmlSerializer.read(returnStr);

		DataMap resultJSON = new DataMap();

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", "");
		resultStats.put("resultList", resultList);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getDataCrossDomainJson
	 * 2. ClassName  : FCommonController
	 * 3. Comment   : 다른 도메인의 url을 호출하여 정보를 얻어올때(JSON)
	 * 4. 작성자	: 조정현
	 * 5. 작성일	: 2014. 8. 29. 오후 2:29:05
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping(value = "/fcommon/getDataCrossDomainJsonAjax.do")
	public @ResponseBody DataMap getDataCrossDomainJsonAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");

		DataMap param = RequestUtil.getDataMap(request);

		URL url;
		String returnStr = "";

		StringBuffer sb = new StringBuffer();
		BufferedReader in  = null;

		try {
			url = new URL(param.getStringOrgn("url"));
			in = new BufferedReader(new InputStreamReader( url.openStream(), "UTF-8"));
			String inputLine;

			while ((inputLine = in.readLine()) != null){
				sb.append( inputLine.trim());
			}

			log.debug(sb.toString());
//			System.out.println( sb.toString());
			returnStr = sb.toString();

		} catch (MalformedURLException e) {
//			System.out.println("######### 예외 발생65 ##########");
			log.error("######### 예외 발생65 ##########");
		} catch (IOException e) {
//			System.out.println("######### 예외 발생66 ##########");
			log.error("######### 예외 발생66 ##########");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			System.out.println("######### 예외 발생67 ##########");
			log.error("######### 예외 발생67 ##########");
		}
		finally{
			in.close();
		}

		DataMap resultJSON = new DataMap();

		DataMap returnList = new DataMap();
		returnList.put("returnList", returnStr);

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", "");
		resultStats.put("resultList", returnList);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}
}
