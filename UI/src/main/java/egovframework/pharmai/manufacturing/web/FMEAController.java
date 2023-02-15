package egovframework.pharmai.manufacturing.web;

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
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.pharmai.manufacturing.service.FMEAService;
import egovframework.pharmai.manufacturing.service.ManufacturingMgtService;
import egovframework.pharmai.manufacturing.service.MethodService;
import egovframework.pharmai.manufacturing.service.PHAService;
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
public class FMEAController {

	private static Log log = LogFactory.getLog(FMEAController.class);

	@Resource(name = "phaService")
	private PHAService phaService;


	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "fmeaService")
	private FMEAService fmeaService;

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
	 * 1. MethodName : selectManufacturingFMEA
	 * 2. ClassName  : ManufacturingController
	 * 3. Comment   : 제조 - FMEA 테이블
	 * 4. 작성자	: KSM
	 * 5. 작성일	: 2021. 8. 04
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pharmai/chemical/manufacturing/selectManu_FMEA.do")
	public String selectManu_FMEA(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "04"));
		param.put("userNo", userInfoVo.getUserNo());

		String common_path = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.manufacturing");
		param.put("common_path", common_path);

		String next_data = manufacturingMgtService.selectNextDataExt(param);
		param.put("next_data", next_data);

		List resultList = fmeaService.selectformulaStep4(param);// stp04
		if(resultList.size() == 0) { // api
			param.put("step_new", "Y");

		} else { //db
			param.put("step_new", "N");
			model.addAttribute("resultList", resultList);
		}

		//project master 조회
		DataMap pjtData = manufacturingMgtService.selectPjtMst(param);

		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "pharmai/manufacturing/selectManu_FMEA";
	}

	/**
	 * <PRE>
	 * 1. MethodName : saveStep04
	 * 2. ClassName  : ManufacturingController
	 * 3. Comment   : 제조 - FMEA 테이블 INSERT
	 * 4. 작성자	: KSM
	 * 5. 작성일	: 2021. 8. 04
	 * </PRE>
	 *
	 * @return String
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pharmai/manufacturing/saveStep04.do")
	public String saveStep04(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
		userInfoVo.setCur_prjct_type(param.getString("prjct_type"));
		userInfoVo.setCur_step_cd(param.getString("status"));

		// insert 및 insert 하기전 update(USE_YN = 'N')
		manufacturingMgtService.stepChangeFuncManu(param);

		fmeaService.insertMenufacStep04(param);

		//마지막 수정일
		manufacturingMgtService.updatePjt_mst(param);

		return "redirect: /pharmai/chemical/manufacturing/selectManu_UNIT_IMG.do";
	}

	@RequestMapping(value = "/pharmai/manufacturing/getApi4Ajax.do")
	public @ResponseBody DataMap getApi4Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		//api4 - step4,5 outdata가 있어서 공통으로 사용
		log.debug("####" + this.getClass().getName() + " START ####");
		DataMap param = RequestUtil.getDataMap(request);

		URL url;
		HttpURLConnection conn;

		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;

		JSONObject jsonob = new JSONObject();
		JSONObject jdata = new JSONObject();


		JSONArray jsonarr = new JSONArray();
		JSONArray jsonarrFormulations = new JSONArray();
		JSONArray jsonArrPrimary = new JSONArray();
		Object data = new Object();

		DataMap stp1_dtlMap = new DataMap();
		DataMap stp2_dtlMap = new DataMap();


		JSONArray temnparr = new JSONArray();

		List stp1_dtl = methodService.selectManufacturingStep1(param);
		List stp2_dtl = process_MapService.selectMenufacStep02(param);

		DataMap cqaValData = fmeaService.selectCqaValList(param);
		String[] arr1 = cqaValData.getString("PHA_COL_1").split(",");
		String[] arr2 = cqaValData.getString("PHA_COL_2").split(",");
		String[] arr3 = cqaValData.getString("PHA_COL_3").split(",");
		String[] arr4 = cqaValData.getString("PHA_COL_4").split(",");
		String[] arr5 = cqaValData.getString("PHA_COL_5").split(",");
		String[] arr6 = cqaValData.getString("PHA_COL_6").split(",");
		String[] arr7 = cqaValData.getString("PHA_COL_7").split(",");
		String[] arr8 = cqaValData.getString("PHA_COL_8").split(",");

		List arrayList = new ArrayList<>();
		arrayList.add(arr1);
		arrayList.add(arr2);
		arrayList.add(arr3);
		arrayList.add(arr4);
		arrayList.add(arr5);
		arrayList.add(arr6);
		arrayList.add(arr7);
		arrayList.add(arr8);

		for (int i = 0; i < stp1_dtl.size(); i++) {
			stp1_dtlMap = (DataMap)stp1_dtl.get(i);

			if(stp1_dtlMap.getString("CHECK_YN").equals("Y")) {
				jdata.put("routes", stp1_dtlMap.getString("ROUTES_TYPE"));
				jdata.put("formulation", stp1_dtlMap.getString("DOSAGE_FORM_TYPE"));
				jdata.put("method", stp1_dtlMap.getString("MANUFACTURING_METHOD"));

			}

		}

		JSONObject temp = new JSONObject();
		JSONObject temp2 = new JSONObject();

		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < stp2_dtl.size(); i++) { //level(ex.사과,혼합...)
			stp2_dtlMap = (DataMap)stp2_dtl.get(i);

			if(stp2_dtlMap.getString("CHECK_YN").equals("Y")) { // checkYn
				temnparr.add(stp2_dtlMap.getString("PROCESS_NAME"));

/*
				{'사과' : ['높음', '낮음', '낮음', '낮음', '낮음']},
				{'혼합' : ['중간', '중간', '중간', '중간', '낮음']},
				{'과립' : ['낮음', '낮음', '낮음', '낮음', '낮음']}
*/
				for(int j = 0; j< arrayList.size(); j++) { //배열담은 arrayList
					String[] array = (String[]) arrayList.get(j);
					JSONArray jsonarrFormulation = new JSONArray();
					temp = new JSONObject();
 					for(int z = 1; z <array.length; z++) {// Cqa(ex. 낮음, 중간, 높음)
						if(stp2_dtlMap.getString("PROCESS_NAME").equals(array[0])) {
							jsonarrFormulation.add(array[z]);
							temp.put(array[0], jsonarrFormulation);

						}
					}
 					if(!temp.isEmpty()) {
 						jsonArray.add(temp);
 					}


				}

			}
		}

		jsonarrFormulations.add(temp);
		jdata.put("pha", jsonArray);
		jdata.put("level", temnparr);
		jdata.put("path", param.getString("common_path"));

		System.out.println(jdata.toString());

		jsonarr.add(jdata);
		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);


		try {
			//api미정
			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.manufacturing.api4"));
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
