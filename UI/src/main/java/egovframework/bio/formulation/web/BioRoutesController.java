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
import egovframework.bio.formulation.service.BioFormulationService;
import egovframework.bio.formulation.service.BioRoutesService;
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
* 2. FileName  : BioRoutesController.java
* 3. Package  : egovframework.bio.formulation.web
* 4. Comment  :
* 5. 작성자   : :hnpark
* 6. 작성일   : 2022. 4. 22. 
* </PRE>
*/
@Controller
public class BioRoutesController {

	private static Log log = LogFactory.getLog(BioRoutesController.class);
	
	@Resource(name = "bioFormulationService")
	private BioFormulationService bioFormulationService;
	
	@Resource(name = "bioRoutesService")
	private BioRoutesService bioRoutesService;
	
	/*STEP2 시작*/
	@RequestMapping(value = "/pharmai/bio/formulation/selectRoutes.do")
	public String selectRoutes(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "02"));
		param.put("userNo", userInfoVo.getUserNo());

		String next_data = bioFormulationService.selectNextDataExt(param);
		param.put("next_data", next_data);

		DataMap dosageFormMap = bioRoutesService.selectDosageFormCnt(param);
		if (dosageFormMap != null) {
			param.put("oral_cnt", dosageFormMap.getString("ORAL_TOT"));
			param.put("parenteral_cnt", dosageFormMap.getString("PARENTERAL_TOT"));
			param.put("local_cnt", dosageFormMap.getString("LOCAL_TOT"));
		} else {
			param.put("oral_cnt", "0");
			param.put("parenteral_cnt", "0");
			param.put("local_cnt", "0");
		}

		List excipientList = bioRoutesService.selectListExcipient(param);
		if (excipientList != null && excipientList.size() > 0) {
			model.addAttribute("excipientList", excipientList);
		}

		DataMap resultMap = bioRoutesService.selectDataStep2(param);
		if (resultMap != null) { //db통신
			param.put("select_excipient", resultMap.getString("DOSAGE_FORM_TYPE"));
			param.put("routes", resultMap.getString("ROUTES_TYPE"));
			param.put("volume", resultMap.getString("VOLUME"));
			param.put("unit", resultMap.getString("UNIT"));
			param.put("step_new", "N");
		} else {
			param.put("step_new", "Y");
		}
		DataMap pjtData = bioFormulationService.selectPjtMst(param);//project master


		List list = bioRoutesService.selectListExcipient(param);


		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "bio/formulation/selectRoutes";
	}
	
	@RequestMapping(value = "/pharmai/bio/formulation/saveStep2.do")
	public String saveStep2(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		// insert 및 insert 하기전 update(USE_YN = 'N')
		bioFormulationService.stepChangeFunc(param);

		bioRoutesService.insertFormulaStp_02(param);

		//마지막 수정일
		bioFormulationService.updatePjt_mst(param);

		return "redirect: selectExcipient.do";

	}
	
	@RequestMapping(value = "/pharmai/bio/formulation/getApi2Ajax.do")
	public @ResponseBody DataMap getApi2Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		log.debug("####" + this.getClass().getName() + " START ####");
		DataMap param = RequestUtil.getDataMap(request);
		DataMap rsJSON = new DataMap();
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

		jdata.put("routes", param.getString("route"));
		jsonarr.add(jdata);

		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);

		try {

			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.formulation.api2"));
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
			returnStr = sb.toString();
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
		resultStats.put("list", data);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}
	/*STEP2 종료*/
	
}
