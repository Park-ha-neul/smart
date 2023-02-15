package egovframework.bio.formulation.web;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.JSONParser;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.bio.formulation.service.BioDoeService;
import egovframework.bio.formulation.service.BioExperimentService;
import egovframework.bio.formulation.service.BioFormulationService;
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
* 2. FileName  : BioDoeController.java
* 3. Package  : egovframework.bio.formulation.web
* 4. Comment  :
* 5. 작성자   : :hnpark
* 6. 작성일   : 2022. 4. 26. 오전 11:17
* </PRE>
*/
@Controller
public class BioDoeController {

	private static Log Log = LogFactory.getLog(BioDoeController.class);

	@Resource(name = "bioDoeService")
	private BioDoeService bioDoeService;

	@Resource(name = "bioFormulationService")
	private BioFormulationService bioFormulationService;

	@Resource(name = "bioExperimentService")
	private BioExperimentService bioExperimentService;

	/*STEP6 시작*/
	@RequestMapping(value = "/pharmai/bio/formulation/selectDoE.do")
	public String formulationResult(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		DataMap param = RequestUtil.getDataMap(request);
		String common_path = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation");

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "06"));
		param.put("userNo", userInfoVo.getUserNo());

		DataMap selectStep6Graph_preto = bioDoeService.selectStep6Graph_preto(param);
		DataMap selectStep6Graph_contour = bioDoeService.selectStep6Graph_contour(param);
		DataMap selectStep6Graph_response = bioDoeService.selectStep6Graph_response(param);

		if(selectStep6Graph_preto ==  null) {
			param.put("step_new", "Y");
		}else { //db통신
			param.put("step_new", "N");
			DataMap preto_file_nm = new DataMap();
			DataMap contour_file_nm = new DataMap();
			DataMap response_file_nm = new DataMap();

			for (int i = 0; i < 5; i++) {
				String tempData = selectStep6Graph_preto.getString("PRETO_PATH_"+(i + 1)).replace("/home/data/t3q/uploads/", "/upload/");
				selectStep6Graph_preto.put("PRETO_PATH_"+(i + 1), tempData);
				String[] str = tempData.split("/");
				String fileName = str[str.length-1];
				if(tempData != "") {
					int dot = fileName.lastIndexOf('.');
					String _fileName = (dot>-1)?fileName.substring(0,dot):fileName;
					preto_file_nm.put("PRETO_PATH_"+(i + 1)+"_NM", _fileName);
				}else {
					preto_file_nm.put("PRETO_PATH_"+(i + 1)+"_NM", fileName);
				}
			}

			model.addAttribute("selectStep6Graph_preto", selectStep6Graph_preto);
			model.addAttribute("preto_file_nm", preto_file_nm);

			for (int i = 0; i < 15; i++) {
				String tempData = selectStep6Graph_contour.getString("CONTOUR_PATH_"+(i + 1)).replace("/home/data/t3q/uploads/", "/upload/");
//				selectStep6Graph_contour.put("CONTOUR_PATH_"+i, tempData);
				selectStep6Graph_contour.put("CONTOUR_PATH_"+(i + 1), tempData);
				String[] str = tempData.split("/");
				String fileName = str[str.length-1];
				if(tempData != "") {
					int dot = fileName.lastIndexOf('.');
					String _fileName = (dot>-1)?fileName.substring(0,dot):fileName;
					contour_file_nm.put("CONTOUR_PATH_"+(i + 1)+"_NM", _fileName);
				}else {
					contour_file_nm.put("CONTOUR_PATH_"+(i + 1)+"_NM", fileName);
				}
			}

			model.addAttribute("selectStep6Graph_contour", selectStep6Graph_contour);
			model.addAttribute("contour_file_nm", contour_file_nm);

			for (int i = 0; i < 15; i++) {
				String tempData = selectStep6Graph_response.getString("RESPONSE_PATH_"+(i + 1)).replace("/home/data/t3q/uploads/", "/upload/");;
//				selectStep6Graph_response.put("RESPONSE_PATH_"+i, tempData);
				selectStep6Graph_response.put("RESPONSE_PATH_"+(i + 1), tempData);
				String[] str = tempData.split("/");
				String fileName = str[str.length-1];
				if(tempData != "") {
					int dot = fileName.lastIndexOf('.');
					String _fileName = (dot>-1)?fileName.substring(0,dot):fileName;
					response_file_nm.put("RESPONSE_PATH_"+(i + 1)+"_NM", _fileName);
				}else {
					response_file_nm.put("RESPONSE_PATH_"+(i + 1)+"_NM", fileName);
				}
			}

			model.addAttribute("selectStep6Graph_response", selectStep6Graph_response);
			model.addAttribute("response_file_nm", response_file_nm);

		}

		List selectStep6Design = bioDoeService.selectStep6Design(param);
		DataMap selectStep6Design_img = bioDoeService.selectStep6Design_img(param);
		DataMap design_file_nm = new DataMap();

		//api7_design 값 체크
		if(selectStep6Design.isEmpty() && selectStep6Design_img == null) {
			param.put("step_new", "Y");
		}else{ //db통신
			int Design_length = selectStep6Design.size();

			for (int i = 0; i < 3; i++) {
				String tempData = selectStep6Design_img.getString("DESIGN_IMG_PATH_"+(i + 1)).replace("/home/data/t3q/uploads/", "/upload/");
				selectStep6Design_img.put("DESIGN_IMG_PATH_"+ (i + 1), tempData);
				String[] str = tempData.split("/");
				String fileName = str[str.length-1];
				if(tempData != "") {
					int dot = fileName.lastIndexOf('.');
					String _fileName = (dot>-1)?fileName.substring(0,dot):fileName;
					design_file_nm.put("DESIGN_IMG_PATH_"+(i + 1)+"_NM", _fileName);
				}else {
					design_file_nm.put("DESIGN_IMG_PATH_"+(i + 1)+"_NM", fileName);
				}
			}

			model.addAttribute("Design_length", Design_length);
			model.addAttribute("selectStep6Design", selectStep6Design);
			model.addAttribute("selectStep6Design_img", selectStep6Design_img);
			model.addAttribute("design_file_nm", design_file_nm);
		}

		List selectStep6ResultList = bioDoeService.selectStp07result(param);
		DataMap selectStepResultImgData = bioDoeService.selectStp07resultImg(param);
		DataMap result_file_nm = new DataMap();
		//api호출
		if(selectStep6ResultList.size() == 0 || selectStepResultImgData == null) {
			param.put("result_step_status", "Y");
		}else { //db호출
			param.put("result_step_status", "N");
			for (int i = 0; i < 18; i++) {
				String tempData = selectStepResultImgData.getString("CONTOUR_IMG_PATH_" + (i + 1)).replace("/home/data/t3q/uploads/", "/upload/");;
				selectStepResultImgData.put("CONTOUR_IMG_PATH_" + (i+1), tempData);
				String[] str = tempData.split("/");
				String fileName = str[str.length-1];
				if(tempData != "") {
					int dot = fileName.lastIndexOf('.');
					String _fileName = (dot>-1)?fileName.substring(0,dot):fileName;
					result_file_nm.put("CONTOUR_IMG_PATH_" + (i + 1) + "_NM", _fileName);
				}else {
					result_file_nm.put("CONTOUR_IMG_PATH_" + (i + 1) + "_NM", fileName);
				}
			}

			for (int i = 0; i < 3; i++) {
				String tempData = selectStepResultImgData.getString("DESIGN_IMG_PATH_" + (i + 1)).replace("/home/data/t3q/uploads/", "/upload/");;
				selectStepResultImgData.put("DESIGN_IMG_PATH_" + (i+1), tempData);
				String[] str = tempData.split("/");
				String fileName = str[str.length-1];
				if(tempData != "") {
					int dot = fileName.lastIndexOf('.');
					String _fileName = (dot>-1)?fileName.substring(0,dot):fileName;
					result_file_nm.put("DESIGN_IMG_PATH_" + (i + 1) + "_NM", _fileName);
				}else {
					result_file_nm.put("DESIGN_IMG_PATH_" + (i + 1) + "_NM", fileName);
				}
			}

			List selectStp_06 = bioDoeService.selectListFormulaStp_06(param);
			List selectStp_03 = bioExperimentService.selectListFormulaStp_03(param);

			model.addAttribute("result_file_nm", result_file_nm);
			model.addAttribute("selectStep6ResultList", selectStep6ResultList);
			model.addAttribute("selectStepResultImgData", selectStepResultImgData);
			model.addAttribute("selectStp_06", selectStp_06);
			model.addAttribute("selectStp_03", selectStp_03);

		}



		DataMap pjtData = bioFormulationService.selectPjtMst(param);//project master
		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);
		model.addAttribute("common_path", common_path);



		return "bio/formulation/selectDoE";
	}
	
	/**
	 * 
	 * @param path
	 * @param arrayNode
	 * @return arrayList
	 * @throws IOException
	 */
//	arryNode에 있는 이미지 개수에 따라서 이미지 생성 및 해당 이미지 경로와 이미지 명 arrayList로 return
	private ArrayList<String> getGraphList(String path, ArrayNode arrayNode) throws IOException {
		ArrayList returnList = new ArrayList<>();

		for (int i = 0; i < arrayNode.size(); i++) {
			JsonNode jsonObject = arrayNode.get(i);
			// string을 byte 형식으로 변환 후 이미지 저장 (string ->
			// javax.xml.bind.DatatypeConverter.parseBase64Binary)
			byte[] imageBytes = javax.xml.bind.DatatypeConverter
					.parseBase64Binary(jsonObject.get("image_to_base64").asText());
			// 특정 경로에 이미지 저장 (param > path)
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			String fileName = path + jsonObject.get("image_name").asText() + ".png";
			System.out.println("fileName check : " + fileName);
			ImageIO.write(img, "png", new File(fileName));
			// 해당 이미지를 list에 담기
			returnList.add(fileName);
		}

		return returnList;
	}

	@RequestMapping(value = "/pharmai/bio/formulation/getApi6Ajax.do")
	public @ResponseBody DataMap getApi6Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		// return 상태
		DataMap resultStats = new DataMap();
		
		Log.debug("####" + this.getClass().getName() + " START ####");
		DataMap resultJSON = new DataMap();
		DataMap param = RequestUtil.getDataMap(request);

		URL url;
		HttpURLConnection conn;

		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;

		JSONObject jsonob = new JSONObject();
		JSONObject jdata = new JSONObject();
		JSONArray jsonarr = new JSONArray();
		JSONArray jsonArrPrimary = new JSONArray();

		jdata.put("experiment data", param.getString("experiment data"));

		List selectStp_03 = bioExperimentService.selectListFormulaStp_03(param);
		List selectStp_04 = bioExperimentService.selectListFormulaStp_04(param);

		DataMap selectStp_03Map = null;
		DataMap selectStp_04Map = null;
		JSONArray selectStp_04List = new JSONArray();
		JSONArray jsonarrFormulation = new JSONArray();

		// API 호출 후 결과를 json 으로 파싱 후 매핑
		JsonNode data = null;
		
		for(Object list : selectStp_04) {
			selectStp_04Map = (DataMap) list;
			if (selectStp_04Map.getString("CHECK_YN").equals("Y")) {
				selectStp_04List.add(selectStp_04Map.getString("CQA_NM"));
				jdata.put("header",selectStp_04List);
			}
		}

		for (Object list : selectStp_03) {
			selectStp_03Map = (DataMap) list;
			if (selectStp_03Map.getString("CHECK_YN").equals("Y")) {
				JSONObject temp = new JSONObject();
				temp.put("excipients", selectStp_03Map.getString("EXCIPIENT"));
				temp.put("kind", selectStp_03Map.getString("KIND"));

				JSONObject temp1 = new JSONObject();
				temp1.put("value", selectStp_03Map.getString("MAXIMUM"));
				temp1.put("unit", selectStp_03Map.getString("UNIT"));
				temp.put("max", temp1);

				JSONObject temp2 = new JSONObject();
				temp2.put("min", selectStp_03Map.getString("USE_RANGE_S"));
				temp2.put("max", selectStp_03Map.getString("USE_RANGE_E"));
				temp.put("use range", temp2);

				JSONObject temp3 = new JSONObject();
				temp3.put("min", selectStp_03Map.getString("IPT_USE_RANGE_S"));
				temp3.put("max", selectStp_03Map.getString("IPT_USE_RANGE_E"));
				temp.put("input range", temp3);

				jsonarrFormulation.add(temp);

			}
		}
		jdata.put("formulation", jsonarrFormulation);

		jdata.put("pareto-path", param.getString("pareto-path"));
		jdata.put("contour-path", param.getString("contour-path"));
		jdata.put("response-path", param.getString("response-path"));
		
		// add 22.05.30 (UI에서 이미지 저장 시 경로 지정 및 생성)
		String prjct_id = param.getString("prjct_id");
		String path1 = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + prjct_id + "/api6/tab-1/pareto/";
		String path2 = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + prjct_id + "/api6/tab-2/contour/";
		String path3 = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + prjct_id + "/api6/tab-3/response/";
		
		// path에 해당하는 디렉토리가 존재하지 않는 경우 해당 디렉토리 생성 
		File directory1 = new File(path1);
		File directory2 = new File(path2);
		File directory3 = new File(path3);
		
		if(!directory1.exists() || !directory2.exists() || !directory3.exists()) {
			directory1.mkdirs();
			directory2.mkdirs();
			directory3.mkdirs();
		}
		
		jsonarr.add(jdata);
		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);

		try {

			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.formulation.api6"));
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

//			Log.debug(sb.toString());
//			JSONParser parser = new JSONParser();
//			data = parser.parse(sb.toString());
			
			// add 22.06.02 (objectMapper로 데이터 형태 변환)
			ObjectMapper om = new ObjectMapper();
			data = om.readTree(sb.toString());
			
			// output data에 globals.properties env 값 전달
			((ObjectNode)data).put("system_env", Globals.SYSTEM_ENV);
			
			// data에서 pareto base64 값만 가져오기
			JsonNode parse_result = data.get("result");
			ArrayNode parse_pareto = (ArrayNode) parse_result.get("pareto");
			ArrayNode parse_contour = (ArrayNode) parse_result.get("contour");
			ArrayNode parse_response = (ArrayNode) parse_result.get("response");

			//** start binary 데이터가  빈값으로 들어오는 경우 예외처리*/
			if (StringUtils.isEmpty(String.valueOf(parse_pareto)) || StringUtils.isEmpty(String.valueOf(parse_contour)) || StringUtils.isEmpty(String.valueOf(parse_response))) {
				// 추가 (code, msg) jsp에서 code가 000이 아닌 경우 해당 msg를 alert창으로 보여주게끔 되어있음
				((ObjectNode)data).put("code", "111");
				((ObjectNode)data).put("msg", "이미지 binary 데이터 값이 비어있습니다. 다시 확인해주세요.");
				resultStats.put("resultData", data);
				return resultJSON; 
			}
			
			/** start binary 데이터 이미지 저장*/
			// base64 -> 이미지 저장 후 경로를 담을 list 변수 선언
			ArrayList<String> pareto_list = getGraphList(path1, parse_pareto);
			ArrayList<String> contour_list = getGraphList(path2, parse_contour);
			ArrayList<String> response_list = getGraphList(path3, parse_response);
			
			// 기존 path로 주어지던 key값 활용
			((ObjectNode)parse_result).put("pareto", om.valueToTree(pareto_list));
			((ObjectNode)parse_result).put("contour", om.valueToTree(contour_list));
			((ObjectNode)parse_result).put("response", om.valueToTree(response_list));
			/** end binary 데이터 이미지 저장 */

			System.out.println("최종 data check : " + data);
			
		} catch (MalformedURLException e) {
			Log.error("######### 예외 발생65 ##########");
		} catch (IOException e) {
			Log.error("######### 예외 발생66 ##########");
		} catch (JSONException e) {
			Log.error("######### 예외 발생67 ##########");
		} finally {
			in.close();
		}

		//step6 api 호출시 이미지 경로 db 저장
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonObj = (org.json.simple.JSONObject) parser.parse(data.toString());

		org.json.simple.JSONObject jsons = (org.json.simple.JSONObject) jsonObj.get("result");
		org.json.simple.JSONArray respPlotList = (org.json.simple.JSONArray) jsons.get("response");
		org.json.simple.JSONArray contourList = (org.json.simple.JSONArray) jsons.get("contour");
		org.json.simple.JSONArray paretoList = (org.json.simple.JSONArray) jsons.get("pareto");


		DataMap outputData = new DataMap();
		for(int i = 0; i < respPlotList.size(); i++) {
			outputData.put("response_path_" + (i + 1), respPlotList.get(i));
			outputData.put("contour_path_" + (i + 1), contourList.get(i));

		}

		for(int i = 0; i < paretoList.size(); i++) {
			outputData.put("preto_path_" + (i + 1), paretoList.get(i));
		}

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		outputData.put("prjct_id", param.getString("prjct_id"));
		outputData.put("status", param.getString("status"));
		outputData.put("ss_user_no", userInfoVo.getUserNo());
		//insert 및 insert 하기전 update(USE_YN = 'N')
		bioFormulationService.stepChangeFunc(outputData);

		bioDoeService.insertStep6Graph(outputData);

		resultStats.put("resultData", data);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	@RequestMapping(value = "/pharmai/bio/formulation/getApi7Ajax.do")
	public @ResponseBody DataMap getApi6_resultAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		// return 상태
		DataMap resultStats = new DataMap();
		
		Log.debug("####" + this.getClass().getName() + " START ####");
		DataMap resultJSON = new DataMap();
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
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

		// API 호출 후 결과를 json 으로 파싱 후 매핑
		JsonNode data = null;
		
		DataMap selectStp_03Map = null;
		DataMap selectStp_04Map = null;
		JSONArray selectStp_04List = new JSONArray();

		List selectStp_03 = bioExperimentService.selectListFormulaStp_03(param);
		List selectStp_04 = bioExperimentService.selectListFormulaStp_04(param);

		for(Object list : selectStp_04) {
			selectStp_04Map = (DataMap) list;
			if (selectStp_04Map.getString("CHECK_YN").equals("Y")) {
				selectStp_04List.add(selectStp_04Map.getString("CQA_NM"));

			}
		}
		jdata.put("header",selectStp_04List);
		jdata.put("experiment data", param.getString("experiment_data"));

		for (Object list : selectStp_03) {
			selectStp_03Map = (DataMap) list;
			if (selectStp_03Map.getString("CHECK_YN").equals("Y")) {
				JSONObject temp = new JSONObject();
				temp.put("excipients", selectStp_03Map.getString("EXCIPIENT"));
				temp.put("kind", selectStp_03Map.getString("KIND"));

				JSONObject temp1 = new JSONObject();
				temp1.put("value", selectStp_03Map.getString("MAXIMUM"));
				temp1.put("unit", selectStp_03Map.getString("UNIT"));
				temp.put("max", temp1);

				JSONObject temp2 = new JSONObject();
				temp2.put("min", selectStp_03Map.getString("USE_RANGE_S"));
				temp2.put("max", selectStp_03Map.getString("USE_RANGE_E"));
				temp.put("use range", temp2);

				JSONObject temp3 = new JSONObject();
				temp3.put("min", selectStp_03Map.getString("IPT_USE_RANGE_S"));
				temp3.put("max", selectStp_03Map.getString("IPT_USE_RANGE_E"));
				temp.put("input range", temp3);

				jsonarrFormulation.add(temp);

			}
		}
		jdata.put("formulation", jsonarrFormulation);


		jdata.put("design-path", param.getString("design-path"));
		jdata.put("result-path", param.getString("result-path"));
		
//		 add 22.05.31 (UI에서 이미지 저장 시 경로 지정 및 생성)
		String prjct_id = param.getString("prjct_id");
		String path1 = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + prjct_id + "/api7/tab-1/design/";
		String path2 = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + prjct_id + "/api7/tab-2/result/";
		
		// path에 해당하는 디렉토리가 존재하지 않는 경우 해당 디렉토리 생성 
		File directory1 = new File(path1);
		File directory2 = new File(path2);
		
		if(!directory1.exists() || !directory2.exists()) {
			directory1.mkdirs();
			directory2.mkdirs();
		}

		String[] y_variable_list = request.getParameterValues("y_variables[]");
		String[] effects_list =  request.getParameterValues("effects[]");
		String[] ipt_start_ys_list =  request.getParameterValues("ipt_start_ys[]");
		String[] ipt_end_ys_list =  request.getParameterValues("ipt_end_ys[]");

		JSONArray jsonarrFormulation2 = new JSONArray();
		JSONObject jsonob2 = new JSONObject();

		for(int i = 0; i< y_variable_list.length; i++) {
			JSONObject temp = new JSONObject();
			JSONObject temp2 = new JSONObject();
			JSONArray jsonarrFormulation3 = new JSONArray();

			temp2.put("min", ipt_start_ys_list[i]);
			temp2.put("max", ipt_end_ys_list[i]);
			temp.put(effects_list[i], temp2);

			jsonarrFormulation3.add(temp);
			jsonob2.put(y_variable_list[i], jsonarrFormulation3);

		}
		jsonarrFormulation2.add(jsonob2);
		jdata.put("response", jsonarrFormulation2);

		jsonarr.add(jdata);
		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);

		try {

			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.formulation.api7"));//주소 추가 필여
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

//			Log.debug(sb.toString());
//			JSONParser parser = new JSONParser();
//			data = parser.parse(sb.toString());
			
			// add 22.06.02 (objectMapper로 데이터 형태 변환)
			ObjectMapper om = new ObjectMapper();
			data = om.readTree(sb.toString());
			
			// output data에 globals.properties env 값 전달
			((ObjectNode)data).put("system_env", Globals.SYSTEM_ENV);

			JsonNode parse_result = data.get("result");
			ArrayNode parse_design = (ArrayNode) parse_result.get("design");
			JsonNode parse_final = (JsonNode) parse_result.get("final");
			ArrayNode parse_contour = (ArrayNode) parse_final.get("contour");

			if (StringUtils.isEmpty(String.valueOf(parse_design)) || StringUtils.isEmpty(String.valueOf(parse_contour))) {
				// 추가 (code, msg) jsp에서 code가 000이 아닌 경우 해당 msg를 alert창으로 보여주게끔 되어있음
				((ObjectNode)data).put("code", "111");
				((ObjectNode)data).put("msg", "이미지 binary 데이터 값이 비어있습니다. 다시 확인해주세요.");
				resultStats.put("resultData", data);
				return resultJSON; 
			}
			
			/** start binary 데이터 이미지 저장*/
			// base64 -> 이미지 저장 후 경로를 담을 list 변수 선언
			ArrayList<String> design_list = getGraphList(path1, parse_design);
			ArrayList<String> contour_list = getGraphList(path2, parse_contour);
			
			// 기존 path로 주어지던 key값 활용
			((ObjectNode)parse_result).put("design", om.valueToTree(design_list));
			((ObjectNode)parse_final).put("contour", om.valueToTree(contour_list));
			((ObjectNode)parse_final).put("design", om.valueToTree(design_list));
			
			/** end binary 데이터 이미지 저장 */

			System.out.println("최종 data check : " + data);

		} catch (MalformedURLException e) {
			Log.error("######### 예외 발생65 ##########");
		} catch (IOException e) {
			Log.error("######### 예외 발생66 ##########");
		} catch (JSONException e) {
			Log.error("######### 예외 발생67 ##########");
		} finally {
			in.close();
		}

		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonObj = (org.json.simple.JSONObject) parser.parse(data.toString());
		
		org.json.simple.JSONObject jsonData = (org.json.simple.JSONObject) jsonObj.get("result");
		org.json.simple.JSONArray design = (org.json.simple.JSONArray) jsonData.get("design");

		for(int i = 0; i < design.size(); i++ ) {
			param.put("design_img_path_" + (i  + 1 ), design.get(i));
		}

		org.json.simple.JSONArray excipientArr = (org.json.simple.JSONArray) jsonData.get("excipient");

		List<String> excipenetArrName = new ArrayList<>();
		List<String> excipenetArrMin= new ArrayList<>();
		List<String> excipenetArrMax= new ArrayList<>();
		List<String> controlMinArr=  new ArrayList<>();
		List<String> controlMaxArr=  new ArrayList<>();
		List<String> designMinArr=  new ArrayList<>();
		List<String> designMaxArr=  new ArrayList<>();
		List<String> knowledgeMinArr=  new ArrayList<>();
		List<String> knowledgeMaxArr=  new ArrayList<>();

		for(int i = 0; i < excipientArr.size(); i++ ) {
			org.json.simple.JSONObject obj = (org.json.simple.JSONObject) excipientArr.get(i);
			List list = new ArrayList<>(obj.keySet());
			excipenetArrName.add(list.get(0).toString());
			org.json.simple.JSONObject rangedata = (org.json.simple.JSONObject) obj.get(list.get(0).toString());
			String min = rangedata.get("min").toString();
			String max = rangedata.get("max").toString();
			excipenetArrMin.add(min);
			excipenetArrMax.add(max);

		}

		param.put("excipientNameArr", excipenetArrName);
		param.put("minArr", excipenetArrMin);
		param.put("maxArr", excipenetArrMax);

		org.json.simple.JSONArray factorArr = (org.json.simple.JSONArray) jsonData.get("factor");

		for(int i = 0; i< factorArr.size(); i++ ) {
			org.json.simple.JSONObject obj = (org.json.simple.JSONObject) factorArr.get(i);
			List list = new ArrayList<>(obj.keySet());
			org.json.simple.JSONObject factorObj = (org.json.simple.JSONObject) obj.get(list.get(0).toString());
			List factorNmList = new ArrayList<>(factorObj.keySet());
			org.json.simple.JSONObject SpaceObj = (org.json.simple.JSONObject) factorObj.get(factorNmList.get(0).toString());

			org.json.simple.JSONObject controlSpaceObj = (org.json.simple.JSONObject) SpaceObj.get("control space");
			String controlMin = controlSpaceObj.get("min").toString();
			String controlMax = controlSpaceObj.get("max").toString();

			controlMinArr.add(controlMin);
			controlMaxArr.add(controlMax);

			org.json.simple.JSONObject designSpaceObj = (org.json.simple.JSONObject) SpaceObj.get("design space");
			String designMin =   designSpaceObj.get("min").toString();
			String designMax =  designSpaceObj.get("max").toString();

			designMinArr.add(designMin);
			designMaxArr.add(designMax);

			org.json.simple.JSONObject knowledgeSpaceObj = (org.json.simple.JSONObject) SpaceObj.get("knowledge space");
			String knowledgeMin =  knowledgeSpaceObj.get("min").toString();
			String knowledgeMax =   knowledgeSpaceObj.get("max").toString();

			knowledgeMinArr.add(knowledgeMin);
			knowledgeMaxArr.add(knowledgeMax);
		}

		param.put("controlMinArr", controlMinArr);
		param.put("controlMaxArr", controlMaxArr);
		param.put("designMinArr", designMinArr);
		param.put("designMaxArr", designMaxArr);
		param.put("knowledgeMinArr", knowledgeMinArr);
		param.put("knowledgeMaxArr", knowledgeMaxArr);


		org.json.simple.JSONObject finalObj = (org.json.simple.JSONObject) jsonData.get("final");
		org.json.simple.JSONArray finalContourPng = (org.json.simple.JSONArray) finalObj.get("contour");

		for(int i = 0; i < finalContourPng.size(); i++) {
			param.put("contour_img_path_" + (i + 1), finalContourPng.get(i));
		}

		org.json.simple.JSONArray finaldesignPng = (org.json.simple.JSONArray) finalObj.get("design");

		for(int i = 0; i < finaldesignPng.size(); i++ ) {
			param.put("design_img_path_" + (i + 1), finaldesignPng.get(i));
		}

		param.put("ss_user_no", userInfoVo.getUserNo());

		bioDoeService.insertStp07Design(param);

		resultStats.put("resultData", data);

		List selectStp_06 = bioDoeService.selectListFormulaStp_06(param);
		//Step3(CQAs), Step6(개별 응답값에 대한 목표치)
		resultStats.put("selectStp_03", selectStp_03);
		resultStats.put("selectStp_06", selectStp_06);
		resultJSON.put("resultStats", resultStats);
		
		System.out.println("api7 최종 resultJSON : " + resultJSON);

		return resultJSON;
	}

	/*STEP6 종료*/

	@RequestMapping(value = "/pharmai/bio/formulation/excelDown.do")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		OutputStream os = response.getOutputStream();

		String pathTemplateName = "template/formu_template.xlsx";

		DataMap params = RequestUtil.getDataMap(request);

		List selectStep6ResultList = bioDoeService.selectStp07result(params);
		List selectStep3ResultList = bioExperimentService.selectListFormulaStp_03(params);
		List selectFormulaStp_06 = bioDoeService.selectListFormulaStp_06(params);

		//System.out.println("excel Down common controller selectStep6ResultList : " + selectStep3ResultList);

		ClassPathResource cpr = new ClassPathResource(pathTemplateName);
		try (InputStream input = new BufferedInputStream(cpr.getInputStream())) {// 1

			response.setHeader("Content-Disposition", "atachment; filename=\"bioFormulation_"+System.currentTimeMillis()+".xlsx\"");

			Context context = new Context(); // context 속성에 컨텍스트명과 엑셀에 쓰일 데이터를 Key & Value로 세팅
			context.putVar("step6ResultList", selectStep6ResultList); //  contextName("step6ResultList")은 엑셀 템플릿파일에서 items="컨텍스트명"과 반드시 일치
			context.putVar("step3ResultList", selectStep3ResultList); //  contextName("step3ResultList")은 엑셀 템플릿파일에서 items="컨텍스트명"과 반드시 일치
			context.putVar("step3FormulaList", selectFormulaStp_06); //  contextName("step3ResultList")은 엑셀 템플릿파일에서 items="컨텍스트명"과 반드시 일치
			context.putVar("title", "PharmAI 바이오의약품 제형 결과 Report");

			JxlsHelper.getInstance().processTemplate(input, os, context); // 3

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}
}
