package egovframework.bio.formulation.web;

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
import egovframework.bio.formulation.service.BioCqasService;
import egovframework.bio.formulation.service.BioFormulationService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
*
* <PRE>
* 1. ClassName :
* 2. FileName  : CqasController.java
* 3. Package  : egovframework.bio.formulation.web
* 4. Comment  :
* 5. 작성자   : :hnpark
* 6. 작성일   : 2022. 4. 25. 
* </PRE>
*/
@Controller
public class BioCqasController {

	private static Log log = LogFactory.getLog(BioCqasController.class);

	@Resource(name = "bioFormulationService")
	private BioFormulationService bioFormulationService;
	
	@Resource(name = "bioCqasService")
	private BioCqasService bioCqasService;
	
	/*STEP4 시작*/
	//step4 메인 화면 호출
	@RequestMapping(value = "/pharmai/bio/formulation/selectCQAsList.do")
	public String selectCQAsList(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "04"));
		param.put("userNo", userInfoVo.getUserNo());

		String next_data = bioFormulationService.selectNextDataExt(param);
		param.put("next_data", next_data);

		List cqasList = bioCqasService.selectListFormulaStp_04(param);
		if (cqasList != null && cqasList.size() > 0) { //db통신
			model.addAttribute("cqasList", cqasList);
			param.put("step_new", "N");
		} else { //새페이지
			param.put("step_new", "Y");
			DataMap dosage_form = bioCqasService.selectDosage_Form_type(param);
			param.put("cqa_nm", dosage_form.getString("DOSAGE_FORM_TYPE"));
		}
		DataMap pjtData = bioFormulationService.selectPjtMst(param);//project master

		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "bio/formulation/selectCQAsList";
	}
	
	// API4번 호출
	@RequestMapping(value = "/pharmai/bio/formulation/getApi4Ajax.do")
	public @ResponseBody DataMap getApi4Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");
		DataMap param = RequestUtil.getDataMap(request);

		URL url;
		HttpURLConnection conn;

		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;

		JSONObject jsonob = new JSONObject();
		JSONObject jdata = new JSONObject();

		JSONArray jsonarr = new JSONArray();
		JSONArray jsonArrPrimary = new JSONArray();
		Object data = new Object();

		jdata.put("formulation", param.getString("formulation"));
		jsonarr.add(jdata);

		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);

		try {

			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.formulation.api4"));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
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
			// TODO Auto-generated catch block
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
	
	@RequestMapping(value = "/pharmai/bio/formulation/saveStep4.do")
	public String saveStep4(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		bioFormulationService.stepChangeFunc(param);

		// insert
		bioCqasService.insertFormulaStp_04(param);

		//마지막 수정일
		bioFormulationService.updatePjt_mst(param);

		return "redirect: selectExperiment.do";

	}
	/*STEP4 종료*/
}
