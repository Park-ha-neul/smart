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
import egovframework.pharmai.manufacturing.service.ManuCQAsService;
import egovframework.pharmai.manufacturing.service.ManuExperimentService;
import egovframework.pharmai.manufacturing.service.ManufacturingMgtService;
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
public class ManuExperimentController {

	private static Log log = LogFactory.getLog(ManuExperimentController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "manuExperimentService")
	private ManuExperimentService manuExperimentService;

	/** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;

	@Resource(name = "manufacturingMgtService")
	private ManufacturingMgtService manufacturingMgtService;

	@Resource(name = "manuCQAsService")
	private ManuCQAsService manuCQAsService;

	@RequestMapping(value = "/pharmai/chemical/manufacturing/selectManu_Excipent.do")
	public String selectManu_Excipent(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "08"));
		param.put("userNo", userInfoVo.getUserNo());

		//project master 조회
		DataMap pjtData = manufacturingMgtService.selectPjtMst(param);
		String next_data = manufacturingMgtService.selectNextDataExt(param);
		param.put("next_data", next_data);

		List experimentList = manuExperimentService.selectMenufacStep08(param);


		if(experimentList.size() > 0) {
			param.put("step_new", "N");
			model.addAttribute("experimentList", experimentList);
			int stp06count = manuExperimentService.selectManufactStp06List(param);
			param.put("stp06count", stp06count);
		}else {
			param.put("step_new", "Y");
		}

		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "pharmai/manufacturing/selectManu_Excipent";
	}


	@RequestMapping(value = "/pharmai/manufacturing/saveStep08.do")
	public String insertStep03(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		param.put("ss_user_no", userInfoVo.getUserNo());

		// insert 및 insert 하기전 update(USE_YN = 'N')
		manufacturingMgtService.stepChangeFuncManu(param);

		manuExperimentService.insertMenufacStep08(param);

		//마지막 수정일
		manufacturingMgtService.updatePjt_mst(param);

		return "redirect: /pharmai/chemical/manufacturing/selectManu_Result.do";

	}

	@RequestMapping(value = "/pharmai/manufacturing/getApi6Ajax.do")
	public @ResponseBody DataMap getApi1Ajax(HttpServletRequest request, HttpServletResponse response,ModelMap model)
	 throws Exception{

		log.debug("####" + this.getClass().getName() + " START ####");
		DataMap param = RequestUtil.getDataMap(request);
		URL url;
		HttpURLConnection conn;
		String returnStr = "";

		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;

		JSONObject jsonob = new JSONObject();
		JSONObject jdata = new JSONObject();
		Object data = new Object();

		JSONArray jsonarr = new JSONArray();
		JSONArray jsonarrFormulation = new JSONArray();
		JSONArray jsonArrPrimary = new JSONArray();

		List selectStp_06_data = manuExperimentService.selectStp_06_data(param);
		DataMap selectStp_06_dataMap = null;

		for(Object list : selectStp_06_data) {
			selectStp_06_dataMap = (DataMap)list;
			if(selectStp_06_dataMap.getString("CHECK_YN").equals("Y")) {
				JSONObject temp = new JSONObject();
				JSONObject temp1 = new JSONObject();
				temp.put("factor", selectStp_06_dataMap.getString("CPP_FACTOR"));

				temp1.put("min", selectStp_06_dataMap.getInt("IPT_USE_RANGE_S"));
				temp1.put("max", selectStp_06_dataMap.getInt("IPT_USE_RANGE_E"));
				temp.put("input range", temp1);

				jsonarrFormulation.add(temp);
			}
		}

		jdata.put("cpp", jsonarrFormulation);

		DataMap cqasMap = null;
		List cqasList = manuCQAsService.selectMenufacStep07(param);

		JSONArray cqadata = new JSONArray();
		for (Object list : cqasList) {
			cqasMap = (DataMap) list;
			if (cqasMap.getString("CHECK_YN").equals("Y")) {
				cqadata.add(cqasMap.getString("CQA_NM"));
			}

		}
		jdata.put("CQAs", cqadata);

		jsonarr.add(jdata);
		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);

		System.out.println("jsonob : " + jsonob.toString());

		try {
			//api미정
			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.manufacturing.api6"));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(20000);
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

	@RequestMapping(value="/pharmai/manufacturing/saveTemporary.do")
	public String saveTemporary(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		param.put("ss_user_no", userInfoVo.getUserNo());

		// insert 및 insert 하기전 update(USE_YN = 'N')
		manufacturingMgtService.stepChangeFuncManu(param);

		manuExperimentService.temporarySave(param);

		//마지막 수정일
		manufacturingMgtService.updatePjt_mst(param);


		return "redirect: /pharmai/chemical/manufacturing/selectManu_Excipent.do?prjct_id =" + param.getString("prjct_id");
	}

}
