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
import egovframework.bio.manufacturing.service.BioManuCQAsService;
import egovframework.bio.manufacturing.service.BioManufacturingMgtService;
import egovframework.bio.manufacturing.service.BioProcess_MapService;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.pharmai.manufacturing.web.ManuCQAsController;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BioManuCQAsController.java
 * 3. Package  : egovframework.bio.manufacturing.web
 * 4. Comment  : step7
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 4. 29
 * </PRE>
 */
@Controller
public class BioManuCQAsController {
	
	private static Log log = LogFactory.getLog(ManuCQAsController.class);
	
	@Resource(name = "bioManufacturingMgtService")
	private BioManufacturingMgtService bioManufacturingMgtService;
	
	@Resource(name = "bioManuCQAsService")
	private BioManuCQAsService bioManuCQAsService;
	
	@Resource(name = "bioProcess_MapService")
	private BioProcess_MapService bioProcess_MapService;
	
	// step7 첫 화면 호출
	@RequestMapping(value = "/pharmai/bio/manufacturing/selectManu_CQAs.do")
	public String selectManu_CQAs(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
		param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
		param.put("status", param.getString("status", "07"));
		param.put("userNo", userInfoVo.getUserNo());

		//project master 조회
		DataMap pjtData = bioManufacturingMgtService.selectPjtMst(param);
		String next_data = bioManufacturingMgtService.selectNextDataExt(param);
		param.put("next_data", next_data);

		List cqaList = bioManuCQAsService.selectMenufacStep07(param);
		if(cqaList.size() > 0) {
			param.put("step_new", "N");
			model.addAttribute("cqaList", cqaList);
		}else {
			param.put("step_new", "Y");
		}

		model.addAttribute("pjtData", pjtData);
		model.addAttribute("param", param);

		return "bio/manufacturing/selectManu_CQAs";
	}
	
	// step7 save 기능
	@RequestMapping(value = "/pharmai/bio/manufacturing/saveStep07.do")
	public String insertMenufacStep07(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

		param.put("ss_user_no", userInfoVo.getUserNo());

		// insert 및 insert 하기전 update(USE_YN = 'N')
		bioManufacturingMgtService.stepChangeFuncManu(param);

		bioManuCQAsService.insertMenufacStep07(param);

		//마지막 수정일
		bioManufacturingMgtService.updatePjt_mst(param);

		return "redirect: /pharmai/bio/manufacturing/selectManu_Excipent.do";

	}

	// api5번 호출
	@RequestMapping(value = "/pharmai/bio/manufacturing/getApi5Ajax.do")
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
		JSONArray jsonArrPrimary = new JSONArray();

		DataMap selectStp_01_data = bioProcess_MapService.selectStp_01_data(param);
		jdata.put("route", selectStp_01_data.getString("ROUTES_TYPE"));
		jdata.put("formulation", selectStp_01_data.getString("DOSAGE_FORM_TYPE"));
		jdata.put("method", selectStp_01_data.getString("MANUFACTURING_METHOD"));
		jsonarr.add(jdata);

		jsonArrPrimary.add(jsonarr);
		jsonob.put("data", jsonArrPrimary);

		System.out.println("jsonob : " + jsonob.toString());

		try {

			url = new URL(EgovPropertiesUtil.getProperty("Globals.api.manufacturing.api5"));
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
		resultStats.put("resultData", data);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

}
