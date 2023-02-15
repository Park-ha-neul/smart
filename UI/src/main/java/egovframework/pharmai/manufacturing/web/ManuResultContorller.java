package egovframework.pharmai.manufacturing.web;

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
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.pharmai.manufacturing.service.CPP_FactorService;
import egovframework.pharmai.manufacturing.service.ManuCQAsService;
import egovframework.pharmai.manufacturing.service.ManuExperimentService;
import egovframework.pharmai.manufacturing.service.ManuResultService;
import egovframework.pharmai.manufacturing.service.ManufacturingMgtService;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;



/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : ManuResultContorller.java
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
public class ManuResultContorller {

	
	private static Log log = LogFactory.getLog(ManuResultContorller.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "manuCQAsService")
	private ManuCQAsService manuCQAsService;

	@Resource(name = "manuExperimentService")
	private ManuExperimentService manuExperimentService;

	@Resource(name = "manuResultService")
	private ManuResultService manuResultService;

	@Resource(name = "manufacturingMgtService")
	private ManufacturingMgtService manufacturingMgtService;

	@Resource(name = "cpp_FactorService")
	private CPP_FactorService cppFactorService;


	@RequestMapping(value = "/pharmai/chemical/manufacturing/selectManu_Result.do")
	public String selectManu_Result(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		String common_path = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation");
		param.put("common_path", common_path);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "09"));
		param.put("userNo", userInfoVo.getUserNo());

		DataMap selectStp09_pareto_graph = manuResultService.selectMenufacStep09_pareto(param);
		DataMap selectStp09_response_graph = manuResultService.selectMenufacStep09_response(param);
		DataMap selectStp09_contour_graph = manuResultService.selectMenufacStep09_contour(param);

		if(selectStp09_pareto_graph == null) {
			param.put("step_new", "Y");
		}else {
			param.put("step_new", "N");
			DataMap pareto_file_nm = new DataMap();
			DataMap contour_file_nm = new DataMap();
			DataMap response_file_nm = new DataMap();

			for (int i = 0; i < 10; i++) {
				String tempData = selectStp09_pareto_graph.getString("PARETO_PATH_"+(i + 1)).replace("/home/data/t3q/uploads/", "/upload/");
				selectStp09_pareto_graph.put("PARETO_PATH_"+(i + 1), tempData);
				String[] str = tempData.split("/");
				String fileName = str[str.length-1];
				if(tempData != "") {
					int dot = fileName.lastIndexOf('.');
					String _fileName = (dot>-1)?fileName.substring(0,dot):fileName;
					pareto_file_nm.put("PRETO_PATH_"+(i + 1)+"_NM", _fileName);
				}else {
					pareto_file_nm.put("PRETO_PATH_"+(i + 1)+"_NM", fileName);
				}

			}

			model.addAttribute("selectStp09_pareto_graph", selectStp09_pareto_graph);
			model.addAttribute("pareto_file_nm", pareto_file_nm);

			for (int i = 0; i < 15; i++) {
				String tempData = selectStp09_contour_graph.getString("CONTOUR_PATH_"+(i + 1)).replace("/home/data/t3q/uploads/", "/upload/");
				selectStp09_contour_graph.put("CONTOUR_PATH_"+(i +1), tempData);
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

			model.addAttribute("selectStp09_contour_graph", selectStp09_contour_graph);
			model.addAttribute("contour_file_nm", contour_file_nm);

			for (int i = 0; i < 15; i++) {
				String tempData = selectStp09_response_graph.getString("RESPONSE_PATH_"+(i + 1)).replace("/home/data/t3q/uploads/", "/upload/");;
				selectStp09_response_graph.put("RESPONSE_PATH_"+(i+1), tempData);
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

			model.addAttribute("selectStp09_response_graph", selectStp09_response_graph);
			model.addAttribute("response_file_nm", response_file_nm);

			List selectStp10DesignList = manuResultService.selectStp10DesignList(param);
			DataMap selectStp10DesignImg = manuResultService.selectStp10DesignImgData(param);
			DataMap design_file_nm = new DataMap();

			if(selectStp10DesignList.isEmpty() && selectStp10DesignImg == null ) {
				param.put("step_new", "Y");
			}else {
				int design_length = selectStp10DesignList.size();

				for(int i = 0; i <3; i++) {
					String tempData = selectStp10DesignImg.getString("DESIGN_IMG_PATH_"+(i + 1)).replace("/home/data/t3q/uploads/", "/upload/");
					selectStp10DesignImg.put("DESIGN_IMG_PATH_"+ (i + 1), tempData);
					String[] str = tempData.split("/");
					String fileName = str[str.length -1];
					if(tempData != "") {
						int dot = fileName.lastIndexOf('.');
						String _fileName = (dot>-1)?fileName.substring(0,dot):fileName;
						design_file_nm.put("DESIGN_IMG_PATH_"+(i + 1)+"_NM", _fileName);

					}else {
						design_file_nm.put("DESIGN_IMG_PATH_"+(i + 1)+"_NM", fileName);
					}
				}

				model.addAttribute("design_length", design_length);
				model.addAttribute("selectStp10DesignList", selectStp10DesignList);
				model.addAttribute("selectStp10DesignImg", selectStp10DesignImg);
				model.addAttribute("design_file_nm", design_file_nm);
			}

			List selectStp10ReusltList = manuResultService.selectStp10result(param);
			DataMap selectStp10ResultDataMap = manuResultService.selectStp10ResultImg(param);
			DataMap result_file_nm = new DataMap();

			if(selectStp10ReusltList.size() == 0 || selectStp10ResultDataMap== null ) {
				param.put("result_step_status", "Y");
			}else {
				param.put("result_step_status", "N");
				for(int i = 0; i < 18; i++) {
					String tempData = selectStp10ResultDataMap.getString("CONTOUR_IMG_PATH_" + (i + 1)).replace("/home/data/t3q/uploads/", "/upload/");
					selectStp10ResultDataMap.put("CONTOUR_IMG_PATH_" + (i + 1), tempData);
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
					String tempData = selectStp10ResultDataMap.getString("DESIGN_IMG_PATH_" + (i + 1)).replace("/home/data/t3q/uploads/", "/upload/");;
					selectStp10ResultDataMap.put("DESIGN_IMG_PATH_" + (i+1), tempData);
					String[] str = tempData.split("/");
					String fileName = str[str.length-1];
					if(tempData != "") {
						int dot = fileName.lastIndexOf('.');
						String _fileName =(dot>-1)?fileName.substring(0,dot):fileName;
						result_file_nm.put("DESIGN_IMG_PATH_" + (i + 1) + "_NM", _fileName);
					}else {
						result_file_nm.put("DESIGN_IMG_PATH_" + (i + 1) + "_NM", fileName);
					}
				}

				List selectStp06 = manuResultService.selectMenufactStep06List(param);
				List selectStp10Design = manuResultService.selectStp10DesignList(param);

				model.addAttribute("result_file_nm", result_file_nm);
				model.addAttribute("selectStp10ReusltList", selectStp10ReusltList);
				model.addAttribute("selectStp10ResultDataMap", selectStp10ResultDataMap);
				model.addAttribute("selectStp06", selectStp06);
				model.addAttribute("selectStp10Design", selectStp10Design);


			}


		}

		//project master 조회
		DataMap pjtData = manufacturingMgtService.selectPjtMst(param);

		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "pharmai/manufacturing/selectManu_Result";
	}
	
	/**
	 * 
	 * @param path
	 * @param arrayNode
	 * @return arrayList
	 * @throws IOException 
	 */
//	arryNode에 있는 이미지 개수에 따라서 이미지 생성 및 해당 이미지 경로와 이미지 명 arrayList로 return
	private ArrayList<String> getGraphList(String path, ArrayNode arrayNode) throws IOException{
		ArrayList returnList = new ArrayList<>();
				
		for(int i=0; i< arrayNode.size(); i++){
			JsonNode jsonObject = arrayNode.get(i);
			// string을 byte 형식으로 변환 후 이미지 저장 (string -> javax.xml.bind.DatatypeConverter.parseBase64Binary)
            byte [] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(jsonObject.get("image_to_base64").asText());
            // 특정 경로에 이미지 저장 (param > path)
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
            String fileName = path + jsonObject.get("image_name").asText() + ".png";
            System.out.println("fileName check : " + fileName);
			ImageIO.write(img , "png", new File(fileName));
			// 해당 이미지를 list에 담기
			returnList.add(fileName);
		}
		
		return returnList;
	}

	@RequestMapping(value = "/pharmai/manufacturing/getApi7Ajax.do")
	public @ResponseBody DataMap getManuApi7Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		// return 상태
		DataMap resultStats = new DataMap();
				
		log.debug("####" + this.getClass().getName() + " START ####");
		DataMap resultJSON = new DataMap();
		DataMap param = RequestUtil.getDataMap(request);

		URL url;
		HttpURLConnection conn;

		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;

		JSONObject jsonob = new JSONObject();
		JSONObject jdata = new JSONObject();

		JSONArray jsonarr = new JSONArray();
		JSONArray jsonarrManufact = new JSONArray();
		JSONArray jsonArrPrimary = new JSONArray();
		JSONArray stp07dtlList = new JSONArray();

		List stp07dtl = manuCQAsService.selectMenufacStep07(param);
		DataMap stp07_Map = null;
		
		// API 호출 후 결과를 json 으로 파싱 후 매핑
		JsonNode data = null;

		for (int i = 0; i < stp07dtl.size(); i++) {
			stp07_Map = (DataMap)stp07dtl.get(i);
			if(stp07_Map.getString("CHECK_YN").equals("Y")) {
				stp07dtlList.add(stp07_Map.getString("CQA_NM"));
				jdata.put("header",stp07dtlList);
			}
		}

		jdata.put("experiment data", param.getString("experiment data"));

		List stp06dtl = manuExperimentService.selectStp_06_data(param);
		DataMap stp06_Map = null;
		DataMap temp1 = new DataMap();
		DataMap temp2 = new DataMap();
		for (int i = 0; i < stp06dtl.size(); i++) {
			stp06_Map = (DataMap)stp06dtl.get(i);

			if (stp06_Map.getString("CHECK_YN").equals("Y")) {
				temp1.put("factor", stp06_Map.getString("CPP_FACTOR"));
				temp2.put("min", stp06_Map.getInt("IPT_USE_RANGE_S"));
				temp2.put("max", stp06_Map.getInt("IPT_USE_RANGE_E"));
				temp1.put("input range", temp2);
				jsonarrManufact.add(temp1);
			}

		}

		jdata.put("cpp", jsonarrManufact);
		jdata.put("pareto-path", param.getString("pareto-path"));
		jdata.put("contour-path", param.getString("contour-path"));
		jdata.put("response-path", param.getString("response-path"));
		
		// add 22.05.31 (UI에서 이미지 저장 시 경로 지정 및 생성 )
		String prjct_id = param.getString("prjct_id");
		String path1 = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.manufacturing") + prjct_id + "/api7/tab-1/pareto/";
		String path2 = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.manufacturing") + prjct_id + "/api7/tab-2/contour/";
		String path3 = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.manufacturing") + prjct_id + "/api7/tab-3/response/";
		
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

			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.manufacturing.api7"));
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

//			log.debug(sb.toString());
//			JSONParser parser = new JSONParser();
//			data = parser.parse(sb.toString());

			// add 22.05.31 (objectMapper로 데이터 형태 변환)
			ObjectMapper om = new ObjectMapper();
			data = om.readTree(sb.toString());
			
			// output data에 globals.properties env 값 전달
			((ObjectNode)data).put("system_env", Globals.SYSTEM_ENV);
			
			
			// data에서 pareto base64 값만 가져오기
			JsonNode parse_result = data.get("result");
			ArrayNode parse_pareto = (ArrayNode) parse_result.get("pareto");
			ArrayNode parse_contour = (ArrayNode) parse_result.get("contour");
			ArrayNode parse_response = (ArrayNode) parse_result.get("response");
			
			
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
			// end binary 데이터 이미지 저장
			
			// step9 api 호출시 이미지 경로 DB 저장
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonObj = (org.json.simple.JSONObject) parser.parse(data.toString());

			org.json.simple.JSONObject jsonData = (org.json.simple.JSONObject) jsonObj.get("result");
			org.json.simple.JSONArray pareto_Png = (org.json.simple.JSONArray) jsonData.get("pareto");
			org.json.simple.JSONArray contour_Png = (org.json.simple.JSONArray) jsonData.get("contour");
			org.json.simple.JSONArray response_Png = (org.json.simple.JSONArray) jsonData.get("response");
			
			UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
			param.put("ss_user_no", userInfoVo.getUserNo());

			for (int i = 0; i < pareto_Png.size(); i++) {
				param.put("pareto_"+(i+1), pareto_Png.get(i));
			}

			for (int i = 0; i < contour_Png.size(); i++) {
				param.put("contour_"+(i+1), contour_Png.get(i));
			}

			for (int i = 0; i < response_Png.size(); i++) {
				param.put("response_"+(i+1), response_Png.get(i));
			}

			// insert 및 insert 하기전 update(USE_YN = 'N')
			manufacturingMgtService.stepChangeFuncManu(param);
			manuResultService.insertMenufacStep09(param);

			//마지막 수정일
			manufacturingMgtService.updatePjt_mst(param);

		} catch (MalformedURLException e) {
			log.error("######### 예외 발생65 ##########");
		} catch (IOException e) {
			log.error("######### 예외 발생66 ##########");
		} catch (JSONException e) {
			log.error("######### 예외 발생67 ##########");
		} finally {
			in.close();
		}

		
		resultStats.put("resultData", data);
		resultJSON.put("resultStats", resultStats);
		
		return resultJSON;
	}

	@RequestMapping(value = "/pharmai/manufacturing/getApi8Ajax.do")
	public @ResponseBody DataMap getApi8_resultAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		// return 상태
		DataMap resultStats = new DataMap();
		
		log.debug("####" + this.getClass().getName() + " START ####");
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
		JSONArray jsonarrManufacturing = new JSONArray();
		JSONArray jsonArrPrimary = new JSONArray();

		// API 호출 후 결과를 json 으로 파싱 후 매핑
		JsonNode data = null;
		
		JSONArray selectStp_04List = new JSONArray();

		List selectMenufacStep07 = manuCQAsService.selectMenufacStep07(param);
		List selectExperientList = manuExperimentService.selectStp_06_data(param);

		DataMap step07Data = null;
		DataMap step06Data =null;
		JSONArray selectStp_07List = new JSONArray();


		for(Object list : selectMenufacStep07) {
			step07Data = (DataMap) list;
			if(step07Data.getString("CHECK_YN").equals("Y")) {
				selectStp_07List.add(step07Data.getString("CQA_NM"));
			}
		}

		jdata.put("header", selectStp_07List);
		jdata.put("experiment data", param.getString("experiment_data"));


		for(Object list : selectExperientList) {
			step06Data = (DataMap) list;
			if(step06Data.getString("CHECK_YN").equals("Y")) {
				JSONObject temp = new JSONObject();
				temp.put("factor", step06Data.getString("CPP_FACTOR"));

				JSONObject temp2 = new JSONObject();
				temp2.put("min", step06Data.getString("IPT_USE_RANGE_S"));
				temp2.put("max", step06Data.getString("IPT_USE_RANGE_E"));
				temp.put("input range", temp2);

				jsonarrManufacturing.add(temp);
			}
		}

		jdata.put("cpp", jsonarrManufacturing);

		jdata.put("design-path", param.getString("design-path"));
		jdata.put("result-path", param.getString("result-path"));

//		 add 22.06.02 (UI에서 이미지 저장 시 경로 지정 및 생성)
		String prjct_id = param.getString("prjct_id");
		String path1 = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.manufacturing") + prjct_id + "/api8/tab-1/design/";
		String path2 = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.manufacturing") + prjct_id + "/api8/tab-2/result/";
		
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

			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.manufacturing.api8"));//주소 추가 필여
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

//			log.debug(sb.toString());
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
			log.error("######### 예외 발생65 ##########");
		} catch (IOException e) {
			log.error("######### 예외 발생66 ##########");
		} catch (JSONException e) {
			log.error("######### 예외 발생67 ##########");
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

		org.json.simple.JSONArray excipientArr = (org.json.simple.JSONArray) jsonData.get("cpp");

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

		manuResultService.insertManufactStep10(param);

		resultStats.put("resultData", data);

		List selectStp06 = manuResultService.selectMenufactStep06List(param);
		List selectStp10Design = manuResultService.selectStp10DesignList(param);

		resultStats.put("selectStp06", selectStp06);
		resultStats.put("selectStp10Design", selectStp10Design);
		resultJSON.put("resultStats", resultStats);
		
		return resultJSON;
	}

	@RequestMapping(value = "/pharmai/manufacturing/excelDown.do")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		OutputStream os = response.getOutputStream();

		String pathTemplateName = "template/manu_template.xlsx";

		DataMap params = RequestUtil.getDataMap(request);

		List selectStp10ResultList = manuResultService.selectStp10result(params);
		List selectStp06ResultList= manuResultService.selectMenufactStep06List(params);
		List selectStp10Design = manuResultService.selectStp10DesignList(params);

		//System.out.println("excel Down common controller selectStep6ResultList : " + selectStep3ResultList);

		ClassPathResource cpr = new ClassPathResource(pathTemplateName);
		try (InputStream input = new BufferedInputStream(cpr.getInputStream())) {// 1

			response.setHeader("Content-Disposition", "atachment; filename=\"pharmaiManufacturing_"+System.currentTimeMillis()+".xlsx\"");

			Context context = new Context(); // context 속성에 컨텍스트명과 엑셀에 쓰일 데이터를 Key & Value로 세팅
			context.putVar("step10ResultList", selectStp10ResultList); //  contextName("step10ResultList")은 엑셀 템플릿파일에서 items="컨텍스트명"과 반드시 일치
			context.putVar("step6ResultList", selectStp06ResultList); //  contextName("step6ResultList")은 엑셀 템플릿파일에서 items="컨텍스트명"과 반드시 일치
			context.putVar("step10Design", selectStp10Design); //  contextName("step10Design")은 엑셀 템플릿파일에서 items="컨텍스트명"과 반드시 일치
			context.putVar("title", "PharmAI 합성의약품 제조 결과 Report");

			JxlsHelper.getInstance().processTemplate(input, os, context); // 3

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}


}
