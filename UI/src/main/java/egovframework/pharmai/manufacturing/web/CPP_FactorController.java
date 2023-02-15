package egovframework.pharmai.manufacturing.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.pharmai.manufacturing.service.CPP_FactorService;
import egovframework.pharmai.manufacturing.service.ManufacturingMgtService;
import egovframework.pharmai.manufacturing.service.MethodService;
import egovframework.pharmai.manufacturing.service.Process_MapService;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

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
public class CPP_FactorController {

	private static Log log = LogFactory.getLog(CPP_FactorController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "cpp_FactorService")
	private CPP_FactorService cpp_FactorService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	@Resource(name = "manufacturingMgtService")
	private ManufacturingMgtService manufacturingMgtService;

	@Resource(name = "methodService")
	private MethodService methodService;

	@Resource(name = "process_MapService")
	private Process_MapService process_MapService;

	/**
	 * <PRE>
	 * 1. MethodName : selectManufacturing_CPP_Factor
	 * 2. ClassName  : ManufacturingController
	 * 3. Comment   : 제조 - CPP Factor 테이블
	 * 4. 작성자	: SUHAN
	 * 5. 작성일	: 2021. 8. 27
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pharmai/chemical/manufacturing/selectManu_CPP_Factor.do")
	public String selectManu_CPP_Factor(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "06"));
		param.put("userNo", userInfoVo.getUserNo());

		String next_data = manufacturingMgtService.selectNextDataExt(param);
		param.put("next_data", next_data);

		List cqaList = cpp_FactorService.selectMenufactStep06(param);
		if(cqaList.size() > 0) {
			param.put("step_new", "N");
			model.addAttribute("cqaList", cqaList);
		}else {
			param.put("step_new", "Y");
		}

		//project master 조회
		DataMap pjtData = manufacturingMgtService.selectPjtMst(param);
		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "pharmai/manufacturing/selectManu_CPP_Factor";
	}


	@RequestMapping(value = "/pharmai/manufacturing/saveStep06.do")
	public String insertStep06(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		param.put("ss_user_no", userInfoVo.getUserNo());

		// insert 및 insert 하기전 update(USE_YN = 'N')
		manufacturingMgtService.stepChangeFuncManu(param);

		cpp_FactorService.insertMenufactStep06(param);

		//마지막 수정일
		manufacturingMgtService.updatePjt_mst(param);

		return "redirect: /pharmai/chemical/manufacturing/selectManu_CQAs.do";
	}

	@RequestMapping(value = "/pharmai/manufacturing/getApi4_1Ajax.do")
	public @ResponseBody DataMap getApi4_1Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");
		DataMap param = RequestUtil.getDataMap(request);

		URL url;
		HttpURLConnection conn;

		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;

		JSONObject jsonob = new JSONObject();
		JSONObject jdata = new JSONObject();

		JSONArray jsonarr = new JSONArray();
		JSONArray jsonarrFormulation = new JSONArray();
		JSONArray jsonArrPrimary = new JSONArray();
		Object data = new Object();

		DataMap stp1_dtlMap = new DataMap();
		JSONArray temnparr = new JSONArray();

		List stp1_dtl = methodService.selectManufacturingStep1(param);
		DataMap stp5_dtl = cpp_FactorService.selectDistinctUnitStp05(param);

		for (int i = 0; i < stp1_dtl.size(); i++) {
			stp1_dtlMap = (DataMap)stp1_dtl.get(i);

			if(stp1_dtlMap.getString("CHECK_YN").equals("Y")) {
				jdata.put("routes", stp1_dtlMap.getString("ROUTES_TYPE"));
				jdata.put("formulation", stp1_dtlMap.getString("DOSAGE_FORM_TYPE"));
				jdata.put("method", stp1_dtlMap.getString("MANUFACTURING_METHOD"));

			}

		}

		temnparr.add(stp5_dtl.getString("PROCESS_NAME"));

		jdata.put("level", temnparr);

		jsonarr.add(jdata);
		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);


		try {
			//api미정
			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.manufacturing.api4_1"));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			bw.write(jsonob.toString());
			bw.flush();
			bw.close();

			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine.trim());
			}
			conn.disconnect();

			log.debug(sb.toString());
			JSONParser parser = new JSONParser();
			data = parser.parse(sb.toString());

		} catch (MalformedURLException e) {
			log.error("######### 예외 발생65 ##########");
		} catch (IOException e) {
			log.error("######### 예외 발생66 ##########");
		} catch (JSONException e) {
			log.error("######### 예외 발생67 ##########");
		} finally {
			in.close();
		}
		DataMap resultJSON = new DataMap();

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultData", data);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}





}
