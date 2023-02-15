package egovframework.bio.formulation.web;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.bio.formulation.service.BioFormulationService;
import egovframework.bio.formulation.service.BioMolPredictionService;
import egovframework.framework.common.constant.Globals;
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
* 2. FileName  : BioMolPredictionController.java
* 3. Package  : egovframework.bio.formulation.web
* 4. Comment  :
* 5. 작성자   : :hnpark
* 6. 작성일   : 2022. 4. 22. 
* </PRE>
*/
@Controller
public class BioMolPredictionController {

	private static Log log = LogFactory.getLog(BioMolPredictionController.class);
	
	@Resource(name = "bioFormulationService")
	private BioFormulationService bioFormulationService;
	
	@Resource(name = "bioMolPredictionService")
	private BioMolPredictionService bioMolPredictionService;
	
	/*STEP1 시작*/
	@RequestMapping(value = "/pharmai/bio/formulation/selectInput.do")
	public String selectInput(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		String step_new = "Y";
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);

		DataMap project = bioFormulationService.selectPjtMst(param);
		if (project == null) { // api
			step_new = "Y";

			//Input(step1)에 처음으로 들어온 경우 세션과 user가 선택한 프로젝트 정보는 초기화 한다.
			userInfoVo.setCur_prjct_id("");
			bioFormulationService.updateChoicePrjct(param);
			userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
			request.getSession().setAttribute("userInfoVo", userInfoVo);

		} else { //db
			step_new = "N";
			String repalceInputType = "";

			param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
			param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
			param.put("status", param.getString("status", "01"));
			param.put("userNo", userInfoVo.getUserNo());

			String next_data = bioFormulationService.selectNextDataExt(param);
			param.put("next_data", next_data);

			DataMap pjtData = bioFormulationService.selectPjtMst(param);// project master
			DataMap stepData = bioMolPredictionService.selectformulaStep1(param);// stp01
			if (stepData.getString("INPUT_TYPE").equals("F")) {
				repalceInputType = stepData.getString("INPUT_TYPE").replace("F", "sdf");
				param.put("doc_id", stepData.getString("DOC_ID"));
				DataMap fileData = bioMolPredictionService.selectFileData(param); //file select

				if(request.getParameter("delFile") == null || request.getParameter("delFile").equals("") ) {
					model.addAttribute("fileData", fileData);
				}

				stepData.put("INPUT_TYPE", repalceInputType);

			} else if (stepData.getString("INPUT_TYPE").equals("S")) {
				repalceInputType = stepData.getString("INPUT_TYPE").replace("S", "smiles");
				stepData.put("INPUT_TYPE", repalceInputType);
			} else {
				repalceInputType = stepData.getString("INPUT_TYPE").replace("N", "chemical");
				stepData.put("INPUT_TYPE", repalceInputType);
			}
			List propList = bioMolPredictionService.selectFormulaProp(param);// property
			DataMap propDtl = bioMolPredictionService.selectFormulaPropDtl(param); // property detail
			List dosageList = bioMolPredictionService.selectFormulaDosage(param); // dosage

			model.addAttribute("pjtData", pjtData);
			model.addAttribute("stepData", stepData);
			model.addAttribute("propList", propList);
			model.addAttribute("propDtl", propDtl);
			model.addAttribute("dosageList", dosageList);
		}

		param.put("step_new", step_new);

		model.addAttribute("param", param);

		return "bio/formulation/selectInput";
	}
	
	// STEP1에서 NEXT 버튼 누를 시에 STEP1 내용 저장
	@RequestMapping(value = "/pharmai/bio/formulation/saveStep1.do")
	public String saveStep1(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultStats = new DataMap();
		String returnUrl = "";

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		bioFormulationService.stepChangeFunc(param);

		bioMolPredictionService.insertFormulaStp_01(param);

		param.put("prjct_id", param.getString("prjct_id"));
		param.put("prjct_type", "F");
		param.put("status", "01");
		param.put("userNo", userInfoVo.getUserNo());

		bioFormulationService.updateChoicePrjct(param);

		userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
		userInfoVo.setCur_prjct_type(param.getString("prjct_type"));
		userInfoVo.setCur_step_cd(param.getString("status"));

		//마지막 수정일
		bioFormulationService.updatePjt_mst(param);

		request.getSession().setAttribute("userInfoVo", userInfoVo);

		return "redirect: selectRoutes.do";

	}
	
	// 바이오의약품 제형 시작하기 > 프로젝트명 입력 시 등록
	@RequestMapping(value = { "/pharmai/bio/formulation/projectCreateAjax.do"})
	public @ResponseBody DataMap projectCreateAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultStats = new DataMap();
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		if ("".equals(param.getString("prjct_id")) || param.getString("prjct_id") == null) {
			//* prjct_id 빈값 또는 null 일때는 프로젝트 ID 생성
			bioMolPredictionService.projectCreate(param);
			resultStats.put("prjStatus", "N");

		} else {
			//*prjct_id 존재하는 경우 일때는 프로젝트 명 업데이트
			bioFormulationService.projectNmUpdate(param);
			resultStats.put("prjStatus", "Y");
		}

		request.getSession().setAttribute("userInfoVo", userInfoVo);

		DataMap resultJSON = new DataMap();

		resultJSON.put("projectName", param.getString("projectName"));
		resultJSON.put("prjct_id", param.getString("prjct_id"));
		resultJSON.put("resultStats", resultStats);

		response.setContentType("text/plain; charset=utf-8");

		return resultJSON;

	}
	
	@RequestMapping(value = "/pharmai/bio/formulation/getApi1Ajax.do")
	public @ResponseBody DataMap getApi1Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model)
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

		String sdfPath = param.getString("sdfPath");
		// 로컬 테스트용
//		sdfPath = "D:/sdf/Meticillin.sdf";
		// param으로 받은 sdf file 경로가 없는 경우는 type이 sdf가 아닌 경우이므로 value를 binary로 변환해서 return할 필요가 없다.
		if (StringUtils.isEmpty(sdfPath)) {
			jdata.put("type",param.getString("type"));
			jdata.put("value",param.getString("value"));
			jsonarr.add(jdata);
		}else {
			String base64String = bioMolPredictionService.getBase64String(sdfPath);
			jdata.put("type",param.getString("type"));
			jdata.put("value",base64String);
			jsonarr.add(jdata);
		}

		// 추가 (프로젝트 id 받아서 해당하는 path에 이미지 저장)
		String prjct_id = param.getString("prjct_id");
		String path = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + prjct_id +"/api1/png";
		
		// 받은 path가 존재하지 않아서 발생하는 문제
		File directory = new File(path);
		
		// path에 해당하는 디렉토리가 존재하지 않는 경우 해당 디렉토리 생성
		if(!directory.exists()) {
			directory.mkdirs();
		}

		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);

		System.out.println("jsonob : " + jsonob.toString());

		try {
			
			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.formulation.api1"));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Accept", "application/json");
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
					
			// output data에 globals.properties env 값 전달
			((HashMap<String, Object>) data).put("system_env", Globals.SYSTEM_ENV);
			
			// data에서 binary 값만 가져오기
			Object parse_result = ((org.json.simple.JSONObject) data).get("result");
			Object parse_molecule = ((org.json.simple.JSONObject) parse_result).get("molecule");
			Object parse_text = ((org.json.simple.JSONObject) parse_molecule).get("molecule image to text");
			
			/** start binary 데이터 이미지 저장*/
			// object -> string 형변환
			String str_parse_text = String.valueOf(parse_text);
			
			//** start binary 데이터가  빈값으로 들어오는 경우 예외처리*/
			if (str_parse_text == null || str_parse_text.equals("") ) {
				// 추가 (code, msg) jsp에서 code가 000이 아닌 경우 해당 msg를 alert창으로 보여주게끔 되어있음
				((HashMap<String, Object>) data).put("code", "111");
				((HashMap<String, Object>) data).put("msg", "이미지 binary 데이터 값이 비어있습니다. 다시 확인해주세요.");
				
			}
			//** end binary 데이터가  빈값으로 들어오는 경우 예외처리*/
			
			// string을 byte 형식으로 변환 후 이미지 저장 (string -> javax.xml.bind.DatatypeConverter.parseBase64Binary)
			byte [] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(str_parse_text);
			
			// 특정 경로에 이미지 저장 (param > path)
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			ImageIO.write(img , "png", new File(path + "/molecule_drawing.png"));
			
			// 기존 path로 주어지던 key값 활용
			
			((org.json.simple.JSONObject)parse_molecule).put("generated Molecule", path + "/molecule_drawing.png");
			/** end binary 데이터 이미지 저장*/
			
			System.out.println("최종 data check : " + data);

		} catch (MalformedURLException e) {
			log.error("######### 예외 발생65 ##########");
		} catch (IOException e) {
			log.error("######### 예외 발생66 ##########");
			log.error(e);
			// binary 데이터로 이미지를 그릴 수 없는 경우 (jsp에서 code가 000이 아닌 경우 해당 msg를 alert창으로 보여주게끔 되어있음)
			((HashMap<String, Object>) data).put("code", "111");
			((HashMap<String, Object>) data).put("msg", "이미지 binary 데이터 값을 다시 확인해주세요.");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error("######### 예외 발생67 ##########");
		} catch (Exception e){
			log.error(e);
		}finally {
			in.close();
		}

		DataMap resultJSON = new DataMap();

		// return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("list", data);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}
	
	
}
