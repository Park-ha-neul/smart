package egovframework.bio.manufacturing.web;

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
import egovframework.bio.manufacturing.service.BioManufacturingMgtService;
import egovframework.bio.manufacturing.service.BioMethodService;
import egovframework.bio.manufacturing.service.BioPHAService;
import egovframework.bio.manufacturing.service.BioProcess_MapService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.pharmai.manufacturing.web.PHAController;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BioPHAController.java
 * 3. Package  : egovframework.bio.web.service
 * 4. Comment  : step3
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 4. 29
 * </PRE>
 */
@Controller
public class BioPHAController {
	
	private static Log log = LogFactory.getLog(PHAController.class);
	
	@Resource(name = "bioManufacturingMgtService")
	private BioManufacturingMgtService bioManufacturingMgtService;
	
	@Resource(name = "bioPHAService")
	private BioPHAService bioPHAService;
	
	@Resource(name = "bioProcess_MapService")
	private BioProcess_MapService bioProcess_MapService;
	
	@Resource(name = "bioMethodService")
	private BioMethodService bioMethodService;
	
	@RequestMapping(value = "/pharmai/bio/manufacturing/selectManu_PHA.do")
	public String selectManu_PHA(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "03"));
		param.put("userNo", userInfoVo.getUserNo());

		//project master 조회
		DataMap pjtData = bioManufacturingMgtService.selectPjtMst(param);
		String next_data = bioManufacturingMgtService.selectNextDataExt(param);
		param.put("next_data", next_data);

		List pHaList = bioPHAService.selectMenufacStep03(param);
		if(pHaList.size() > 0) {
			param.put("step_new", "N");
			model.addAttribute("pHaList", pHaList);
		}else {
			param.put("step_new", "Y");
		}

		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "bio/manufacturing/selectManu_PHA";
	}


	@RequestMapping(value = "/pharmai/bio/manufacturing/saveStep03.do")
	public String insertStep03(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		param.put("ss_user_no", userInfoVo.getUserNo());

		// insert 및 insert 하기전 update(USE_YN = 'N')
		bioManufacturingMgtService.stepChangeFuncManu(param);

		bioPHAService.insertMenufacStep03(param);

		//마지막 수정일
		bioManufacturingMgtService.updatePjt_mst(param);

		return "redirect: selectManu_FMEA.do";

	}

	@RequestMapping(value = "/pharmai/bio/manufacturing/getApi3Ajax.do")
	public @ResponseBody DataMap getManuApi3Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");
		DataMap param = RequestUtil.getDataMap(request);

		URL url;
		HttpURLConnection conn;

		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;

		JSONObject jsonob = new JSONObject();
		JSONObject jdata = new JSONObject();

		JSONArray jsonarr = new JSONArray();
		JSONArray jsonarrManufact = new JSONArray();
		JSONArray jsonarrFormulation = new JSONArray();
		JSONArray jsonArrPrimary = new JSONArray();
		Object data = new Object();

		DataMap stp1_dtl = bioProcess_MapService.selectStp_01_data(param);

		jdata.put("routes", stp1_dtl.getString("ROUTES_TYPE"));
		jdata.put("formulation", stp1_dtl.getString("DOSAGE_FORM_TYPE"));
		jdata.put("method", stp1_dtl.getString("MANUFACTURING_METHOD"));

		List stp2_dtl = bioProcess_MapService.selectMenufacStep02(param);
		DataMap processMap = null;

		for (int i = 0; i < stp2_dtl.size(); i++) {
		processMap = (DataMap)stp2_dtl.get(i);
			if (processMap.getString("CHECK_YN").equals("Y")) {
				jsonarrManufact.add(processMap.getString("PROCESS_NAME"));
			}
		}

		jdata.put("level", jsonarrManufact);

		List stp4_dtl = bioMethodService.selectCopy_Stp4_cqa(param);

		DataMap cqlData = null;
		for(int i = 0; i < stp4_dtl.size(); i++) {
			cqlData = (DataMap) stp4_dtl.get(i);
			jsonarrFormulation.add(cqlData.getString("CQA_NM"));
		}

		jdata.put("cqas", jsonarrFormulation);

		jsonarr.add(jdata);
		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);

		try {

			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.manufacturing.api3"));
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
