package egovframework.bio.manufacturing.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
import egovframework.bio.manufacturing.service.BioManufacturingService;
import egovframework.bio.manufacturing.service.BioMethodService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.pharmai.manufacturing.web.MethodController;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BioMethodController.java
 * 3. Package  : egovframework.bio.manufacturing.service
 * 4. Comment  : 제조방법 선택
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 4. 29
 * </PRE>
 */

@Controller
public class BioMethodController {
	
	private static Log log = LogFactory.getLog(MethodController.class);
	
	@Resource(name = "bioManufacturingMgtService")
	private BioManufacturingMgtService bioManufacturingMgtService;
	
	@Resource(name = "bioMethodService")
	private BioMethodService bioMethodService;
	
	@Resource(name = "bioManufacturingService")
	private BioManufacturingService bioManufacturingService;
	
	// step1 페이지 이동
	@RequestMapping(value = "/pharmai/bio/manufacturing/selectManu_Method.do")
	public String selectManu_Method(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String step_new = "Y";
		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		//project master 조회
		DataMap pjtData = bioManufacturingMgtService.selectPjtMst(param);
		if (pjtData == null) { //api

			//Input(step1)에 처음으로 들어온 경우 세션과 user가 선택한 프로젝트 정보는 초기화 한다.
			userInfoVo.setCur_prjct_id("");
			bioManufacturingMgtService.updateChoicePrjct(param);
			userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
			request.getSession().setAttribute("userInfoVo", userInfoVo);
			param.put("step_new", "Y");

			//Copy 데이더 조회
			List prop = bioMethodService.selectCopy_Prop(param);
			DataMap prop_dtl = bioMethodService.selectCopy_Prop_dtl(param);
			DataMap stp2_dtl = bioMethodService.selectCopy_Stp2_dtl(param);
			//api1 method 항목
			String str = param.getString("method");
			String[] methodArray = str.split(",");

			ArrayList<String> methodList = new ArrayList<String>();
			for (int i = 0; i < methodArray.length; i++) {
				methodList.add(methodArray[i]);
			}

			model.addAttribute("methodList", methodList);
			model.addAttribute("prop", prop);
			model.addAttribute("prop_dtl", prop_dtl);
			model.addAttribute("stp2_dtl", stp2_dtl);

		} else { //db

			param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
			param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
			param.put("status", param.getString("status", "01"));
			param.put("userNo", userInfoVo.getUserNo());

			String next_data = bioManufacturingMgtService.selectNextDataExt(param);
			param.put("next_data", next_data);

			List stepDataList =  bioMethodService.selectManufacturingStep1(param);// stp01
			model.addAttribute("stepDataList", stepDataList);
		}

		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "bio/manufacturing/selectManu_Method";
	}
	
	// 바이오의약품 제조 시작하기에서 Formulation 선택 시
	@RequestMapping(value = { "/pharmai/bio/manufacturing/projectCreateAjax.do"})
	public @ResponseBody DataMap projectCreateAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultStats = new DataMap();
		DataMap resultJSON = new DataMap();

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		if ("".equals(param.getString("prjct_id")) || param.getString("prjct_id") == null) {
			// prjct_id 빈값 또는 null 일때는 프로젝트 ID 생성
			bioMethodService.projectCreate(param);
			resultStats.put("prjStatus", "N");

			List prop = bioMethodService.selectFormulationProp(param); //2줄
			param.put("prop", prop);

			DataMap prop_dtl = bioMethodService.selectFormulationProp_dtl(param);
			prop_dtl.put("formulation_pjt_id", param.getString("formulation_pjt_id"));
			prop_dtl.put("prjct_id", param.getString("prjct_id"));
			prop_dtl.put("ss_user_no", userInfoVo.getUserNo());

			DataMap stp2_dtl = bioMethodService.selectFormulationStp2_dtl(param);

			stp2_dtl.put("formulation_pjt_id", param.getString("formulation_pjt_id"));
			stp2_dtl.put("prjct_id", param.getString("prjct_id"));
			stp2_dtl.put("ss_user_no", userInfoVo.getUserNo());

			List stp4_dtl = bioMethodService.selectFormulationStp4_dtl(param); // 6줄
			param.put("stp4_dtl", stp4_dtl);

			//copy 제형
			bioMethodService.insertCopyFormulationProp(param);
			bioMethodService.insertCopyFormulationProp_dtl(prop_dtl);
			bioMethodService.insertCopyFormulationStp2_dtl(stp2_dtl);
			bioMethodService.insertCopyFormulationStp4_dtl(param);


			resultJSON.put("resultStats", resultStats);
			resultJSON.put("prjct_id", param.getString("prjct_id"));
			resultJSON.put("projectName", param.getString("projectName"));


		}else {
			// prjct_id 존재하는 경우 일때는 프로젝트 명 업데이트
			bioMethodService.projectNmUpdate(param);
			resultStats.put("prjStatus", "Y");
			resultJSON.put("resultStats", resultStats);
		}

		request.getSession().setAttribute("userInfoVo", userInfoVo);
		response.setContentType("text/plain; charset=utf-8");

		return resultJSON;

	}
	
	// step1 save
	@RequestMapping(value = "/pharmai/bio/manufacturing/saveStep01.do")
	public String saveStep01(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		if(param.getString("step_new").equals("Y")) {
			bioMethodService.insertPrjMst(param);
		}

		// insert 및 insert 하기전 update(USE_YN = 'N')
		bioManufacturingMgtService.stepChangeFuncManu(param);

		bioMethodService.insertManufacturStp_01(param);
		//마지막 수정일
		bioManufacturingMgtService.updatePjt_mst(param);

		param.put("prjct_id", param.getString("prjct_id"));
		param.put("prjct_type", "M");
		param.put("status", "01");
		param.put("userNo", userInfoVo.getUserNo());

		bioManufacturingMgtService.updateChoicePrjct(param);

		userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
		userInfoVo.setCur_prjct_type(param.getString("prjct_type"));
		userInfoVo.setCur_step_cd(param.getString("status"));

		request.getSession().setAttribute("userInfoVo", userInfoVo);

		return "redirect: selectManu_Process_Map.do";
	}
	
	// api1번 호출
	@RequestMapping(value = "/pharmai/bio/manufacturing/getApi1Ajax.do")
	public @ResponseBody DataMap getApi1Ajax(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultStats = new DataMap();
		DataMap resultJSON = new DataMap();


		DataMap stp2_dtl = bioMethodService.selectFormulationStp2_dtl(param);

		JSONObject ob = (JSONObject) manuApi1InputData(stp2_dtl.getString("ROUTES_TYPE"), param.getString("prjct_id"));

		//api1 호출

		URL url;
		HttpURLConnection conn;
		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;
		String returnStr = "";
		Object data = new Object();

		try {

			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.manufacturing.api1"));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(20000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			bw.write(ob.toString());
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

		// return 상태
		resultStats.put("resultData", data);
		resultJSON.put("resultStats", resultStats);
		resultJSON.put("prjct_id", param.getString("prjct_id"));
		resultJSON.put("projectName", param.getString("projectName"));

		return resultJSON;
	}
	
	//api1 inputData 호출(route(oral, parenteral, local))
		public JSON manuApi1InputData(String route, String prjct_id) throws Exception {

			JSONObject jsonob = new JSONObject();
			JSONObject jdata = new JSONObject();
			JSONArray jsonarr = new JSONArray();
			JSONArray jsonArrPrimary = new JSONArray();

			List selectFormulPropList = bioManufacturingService.selectFormulProp(route, prjct_id);
			DataMap selectFormulPropDtl = bioManufacturingService.selectFormulPropDtl(route, prjct_id);
			DataMap selectFormulStp02 = bioManufacturingService.selectFormulStp02(route, prjct_id);
			List selectFormulStp04 = bioManufacturingService.selectFormulStp04(route, prjct_id);

			JSONArray dtlArr = new JSONArray();

			for(int i = 0; i < selectFormulPropList.size(); i++) {
				DataMap data = (DataMap) selectFormulPropList.get(i);
				if(data.getString("PROP_TYPE").equals("MS")) {
					for(int j = 1; j <= 10; j++) {
						dtlArr.add(data.getDouble("PH" + j));
					}
				}
			}

			jdata.put("pH Mass Solubility", dtlArr);

			jdata.put("logP", selectFormulPropDtl.getString("LOGP"));

			JSONArray tempArr = new JSONArray();
			JSONObject json2 = new JSONObject();

			String bio = selectFormulPropDtl.getString("BIO");
			String[] bios = bio.split(",");

			for(int i = 0; i < bios.length; i++) {
				tempArr.add(bios[i]);
			}

			jdata.put("Bioavailability", tempArr);
			jdata.put("Molecular weight(g/mol)", selectFormulPropDtl.getDouble("MOL_WEI"));

			json2.put("properties", jdata);
			json2.put("routes", route);
			json2.put("formulation", selectFormulStp02.getString("DOSAGE_FORM_TYPE"));
			json2.put("primary", selectFormulStp02.getInt("VOLUME"));
			jsonarr.add(json2);
			jsonArrPrimary.add(jsonarr);
			jsonob.put("data", jsonArrPrimary);

			return jsonob;
		}

}
