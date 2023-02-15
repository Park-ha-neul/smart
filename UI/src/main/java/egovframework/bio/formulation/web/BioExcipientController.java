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
import egovframework.bio.formulation.service.BioExcipientService;
import egovframework.bio.formulation.service.BioFormulationService;
import egovframework.bio.formulation.service.BioRoutesService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.pharmai.formulation.web.ExcipientController;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Controller
public class BioExcipientController {
	
	private static Log log = LogFactory.getLog(ExcipientController.class);
	
	@Resource(name = "bioFormulationService")
	private BioFormulationService bioFormulationService;
	
	@Resource(name = "bioExcipientService")
	private BioExcipientService bioExcipientService;
	
	@Resource(name = "bioRoutesService")
	private BioRoutesService bioRoutesService;
	
	/*STEP3 시작*/
	@RequestMapping(value = "/pharmai/bio/formulation/selectExcipient.do")
	public String selectExcipient(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "03"));
		param.put("userNo", userInfoVo.getUserNo());

		String next_data = bioFormulationService.selectNextDataExt(param);
		param.put("next_data", next_data);


		List resultList = bioExcipientService.selectStp03(param);

		if(resultList.size() == 0) { //신규 api
			DataMap resultMap = bioRoutesService.selectStp02(param);
			param.put("formulation", resultMap.getString("DOSAGE_FORM_TYPE"));
			param.put("unit", resultMap.getString("UNIT"));
			param.put("value", resultMap.getString("VOLUME"));
			param.put("smiles", resultMap.getString("RETURN_SMILE_STRING"));
			param.put("stp_02_seq", resultMap.getString("STP_02_SEQ"));
			param.put("step_new", "Y");

		}else { //db통신
			param.put("step_new", "N");
			model.addAttribute("resultList", resultList);
		}

		DataMap pjtData = bioFormulationService.selectPjtMst(param);//project master

		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);
		return "bio/formulation/selectExcipient";
	}
	
	// step3에서 사용범위 입력 후 해당 입력값 저장
	@RequestMapping(value = "/pharmai/bio/formulation/saveStep3.do")
	public String saveStep3(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		param.put("ss_user_no", userInfoVo.getUserNo());
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));

		bioFormulationService.stepChangeFunc(param);

		bioExcipientService.insertStep3(param);

		//마지막 수정일
		bioFormulationService.updatePjt_mst(param);

		return "redirect: selectCQAsList.do";
	}
	
	@RequestMapping(value = "/pharmai/bio/formulation/getApi3Ajax.do")
	public @ResponseBody DataMap getApi3Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		URL url;
		HttpURLConnection conn;
		String returnStr = "";

		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;

		JSONObject jsonob = new JSONObject();
		JSONObject pdata = new JSONObject();
		JSONObject jdata = new JSONObject();
		Object data = new Object();

		JSONArray jsonarr = new JSONArray();
		JSONArray jsonArrPrimary = new JSONArray();


		jdata.put("smiles", param.getString("smiles"));
		jdata.put("formulation", param.getString("formulation"));
		pdata.put("value", param.getString("primary[value]"));
		pdata.put("unit", param.getString("primary[unit]"));
		jdata.put("primary", pdata);
		jsonarr.add(jdata);

		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);

		try {

			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.formulation.api3"));
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
	/*STEP3 종료*/

}
