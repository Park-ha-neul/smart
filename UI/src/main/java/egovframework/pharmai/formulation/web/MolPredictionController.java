package egovframework.pharmai.formulation.web;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.EgovWebUtil;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.pharmai.formulation.service.FormulationService;
import egovframework.pharmai.formulation.service.MolPredictionService;
import net.sf.json.JSONException;

/**
 *
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : formulationController.java
 * 3. Package  : egovframework.pharmAi.formulation.web
 * 4. Comment  :
 * 5. 작성자   : :LWJ
 * 6. 작성일   : 2021. 5. 21. 오후 1:17:50
 * </PRE>
 */
@Controller
public class MolPredictionController {

	private static Log log = LogFactory.getLog(MolPredictionController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name = "formulationService")
	private FormulationService formulationService;

	@Resource(name = "molPredictionService")
	private MolPredictionService molPredictionService;

	@Resource(name = "NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	@Resource(name = "NtsysFileMngService")
	private NtsysFileMngService ntsysFileMngService;

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;


	/*STEP1 시작*/
	@RequestMapping(value = "/pharmai/chemical/formulation/selectInput.do")
	public String selectInput(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		String step_new = "Y";
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		DataMap param = RequestUtil.getDataMap(request);

		DataMap project = formulationService.selectPjtMst(param);
		if (project == null) { // api
			step_new = "Y";

			//Input(step1)에 처음으로 들어온 경우 세션과 user가 선택한 프로젝트 정보는 초기화 한다.
			userInfoVo.setCur_prjct_id("");
			formulationService.updateChoicePrjct(param);
			userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
			request.getSession().setAttribute("userInfoVo", userInfoVo);

		} else { //db
			step_new = "N";
			String repalceInputType = "";

			param.put("prjct_id", param.getString("prjct_id", userInfoVo.getCur_prjct_id()));
			param.put("prjct_type", param.getString("prjct_type", userInfoVo.getCur_prjct_type()));
			param.put("status", param.getString("status", "01"));
			param.put("userNo", userInfoVo.getUserNo());

			String next_data = formulationService.selectNextDataExt(param);
			param.put("next_data", next_data);

			DataMap pjtData = formulationService.selectPjtMst(param);// project master
			DataMap stepData = molPredictionService.selectformulaStep1(param);// stp01
			if (stepData.getString("INPUT_TYPE").equals("F")) {
				repalceInputType = stepData.getString("INPUT_TYPE").replace("F", "sdf");
				param.put("doc_id", stepData.getString("DOC_ID"));
				DataMap fileData = molPredictionService.selectFileData(param); //file select

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
			List propList = molPredictionService.selectFormulaProp(param);// property
			DataMap propDtl = molPredictionService.selectFormulaPropDtl(param); // property detail
			List dosageList = molPredictionService.selectFormulaDosage(param); // dosage

			model.addAttribute("pjtData", pjtData);
			model.addAttribute("stepData", stepData);
			model.addAttribute("propList", propList);
			model.addAttribute("propDtl", propDtl);
			model.addAttribute("dosageList", dosageList);
		}

		param.put("step_new", step_new);

		model.addAttribute("param", param);

		return "pharmai/formulation/selectInput";
	}

	@RequestMapping(value = "/pharmai/formulation/saveStep1.do")
	public String saveStep1(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultStats = new DataMap();
		String returnUrl = "";

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		formulationService.stepChangeFunc(param);

		molPredictionService.insertFormulaStp_01(param);

		param.put("prjct_id", param.getString("prjct_id"));
		param.put("prjct_type", "F");
		param.put("status", "01");
		param.put("userNo", userInfoVo.getUserNo());

		formulationService.updateChoicePrjct(param);

		userInfoVo.setCur_prjct_id(param.getString("prjct_id"));
		userInfoVo.setCur_prjct_type(param.getString("prjct_type"));
		userInfoVo.setCur_step_cd(param.getString("status"));

		//마지막 수정일
		formulationService.updatePjt_mst(param);

		request.getSession().setAttribute("userInfoVo", userInfoVo);

		return "redirect: /pharmai/chemical/formulation/selectRoutes.do";

	}

	@RequestMapping(value = { "/pharmai/formulation/projectCreateAjax.do"})
	public @ResponseBody DataMap projectCreateAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		DataMap resultStats = new DataMap();
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		if ("".equals(param.getString("prjct_id")) || param.getString("prjct_id") == null) {
			//* prjct_id 빈값 또는 null 일때는 프로젝트 ID 생성
			molPredictionService.projectCreate(param);
			resultStats.put("prjStatus", "N");

		} else {
			//*prjct_id 존재하는 경우 일때는 프로젝트 명 업데이트
			formulationService.projectNmUpdate(param);
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

	@RequestMapping(value = "/pharmai/formulation/getApi1Ajax.do")
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
			String base64String = molPredictionService.getBase64String(sdfPath);
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
			System.out.println("directory 생성");
		}else {
			System.out.println("directory 생성 error");
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
	
	// chemical name이 input 값인 경우 DB 조회
	@RequestMapping(value = "/pharmai/formulation/chemicalToSmiles.do")
	public @ResponseBody DataMap chemicalToSmiles(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		DataMap param = RequestUtil.getDataMap(request);

		// service 조회
		DataMap smiles = molPredictionService.chemicalDBcheck(param);
		System.out.println("smiles check : " + smiles);
		
		DataMap resultJSON = new DataMap();
		resultJSON.put("list", smiles);
		
		return resultJSON;
	}

	// 파일 읽어서 서버 내 저장
	@RequestMapping(value= { "/pharmai/formulation/getFileAjax.do"})
	public @ResponseBody DataMap getFileAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		DataMap param = RequestUtil.getDataMap(request);
		DataMap resultJSON = new DataMap();
		DataMap resultStats = new DataMap();

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		// 파일 객체 가져옴
		List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
		// 파일 확장자 체크
		String msg = ntsysFileMngUtil.checkFileExt(fileList);

		if (msg != "") {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.not.access", new String[] { msg }));
			model.addAttribute("param", param);
			resultStats.put("resultCode", "error");
			resultStats.put("resultMsg", "sdf 파일만 첨부 가능합니다.");
		} else if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.fileMaxSize")) }));
			model.addAttribute("param", param);
			resultStats.put("resultCode", "error");
		} else {
			resultStats.put("resultCode", "ok");
		}


		// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
		if (!ntsysFileMngUtil.checkFileSize(fileList)) {
			MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.fileMaxSize")) }));
			resultStats.put("resultCode", "error");
		}

		if(resultStats.getString("resultCode").equals("ok")) {
			//파일 업로드 처리
			NtsysFileVO reNtsysFile = null;
			if(!fileList.isEmpty()){
				String doc_id = StringUtil.nvl(param.getString("doc_id"), SysUtil.getDocId());
				for(int i=0; i < fileList.size(); i++){
					MultipartFile mfile = (MultipartFile)fileList.get(i);
					if(!mfile.isEmpty()){
						/* * parseFileInf
						 * 1:파일객체
						 * 2:문서아이디
						 * 3:서브폴더명
						 * 4:사용자번호
						 * 5:Web Root Yn*/
						// 파일을 서버에 물리적으로 저장하고
						reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, doc_id, "formulation/" + param.getString("prjct_id")+"/"+"api1/sdf/", param.getString("ss_user_no"), "Y", "O");

					}
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}

			MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));

			resultStats.put("reNtsysFile", reNtsysFile);

		}


		resultJSON.put("resultStats", resultStats);//리턴값 : 선택된 메뉴 정보
		response.setContentType("text/html; charset=utf-8");
		return resultJSON;
	}

	/*STEP1 종료*/
	@RequestMapping(value= { "/pharmai/formulation/propExcelDownLoad.do"})
	public void propExcel(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		String filePath = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + param.getString("prjct_id") + "/excel/";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar cal = Calendar.getInstance();

        String fileName = sdf.format(cal.getTime()) + "_propData.xlsx";
        String browser = request.getHeader("User-Agent");

        if (browser.indexOf("MSIE") > -1) {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.indexOf("Trident") > -1) {       // IE11
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.indexOf("Firefox") > -1) {
            fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.indexOf("Opera") > -1) {
            fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.indexOf("Chrome") > -1) {

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < fileName.length(); i++) {
               char c = fileName.charAt(i);
               if (c > '~') {
            	   sb.append(URLEncoder.encode("" + c, "UTF-8"));
               } else {
            	   sb.append(c);
           		}

            }

            fileName = sb.toString();
        } else if (browser.indexOf("Safari") > -1){
            fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1")+ "\"";
        } else {
        	fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1")+ "\"";
        }


		XSSFWorkbook xssfWb = null;
		XSSFSheet xssfSheet = null;
		XSSFRow xssfRow = null;
		XSSFCell xssfCell = null;

		int rowNo = 0; // 행의 갯수
		xssfWb = new XSSFWorkbook(); //XSSFWorkbook 객체 생성
		xssfSheet = xssfWb.createSheet("MolPrediction"); // 워크시트 이름 설정

		// 폰트 스타일
		XSSFFont font = xssfWb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL); // 폰트 스타일
		font.setFontHeightInPoints((short)20); // 폰트 크기
		font.setBold(true); // Bold 설정
		font.setColor(new XSSFColor(Color.decode("#457ba2"))); // 폰트 색 지정

		//테이블 셀 스타일
		CellStyle cellStyle = xssfWb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		xssfSheet.setColumnWidth(0, (xssfSheet.getColumnWidth(0))); // 0번째 컬럼 넓이 조절
		cellStyle.setFont(font); // cellStyle에 font를 적용

		//셀병합
		xssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9)); //첫행, 마지막행, 첫열, 마지막열 병합
		// 타이틀 생성
		xssfRow = xssfSheet.createRow(rowNo++); // 행 객체 추가
		xssfCell = xssfRow.createCell((short) 0); // 추가한 행에 셀 객체 추가
		xssfCell.setCellStyle(cellStyle); // 셀에 스타일 지정
		xssfCell.setCellValue("pH Mass_Solubility1 (g/L)"); // 데이터 입력

		List propList = molPredictionService.selectFormulaProp(param);// property --logD, logP 나누자


		//테이블 스타일 설정
		XSSFCellStyle tableCellStyle = xssfWb.createCellStyle(); //기본형
		XSSFCellStyle tableCellStyle2 = xssfWb.createCellStyle(); // backgrond 컬러 추가시 셀 병합 border style
		XSSFCellStyle tableCellStyle3 = xssfWb.createCellStyle();



		tableCellStyle2.setAlignment(HorizontalAlignment.CENTER);
		tableCellStyle2.setBorderBottom(BorderStyle.THIN);
		tableCellStyle2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		tableCellStyle2.setBorderLeft(BorderStyle.THIN);
		tableCellStyle2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		tableCellStyle2.setBorderTop(BorderStyle.THIN);
		tableCellStyle2.setTopBorderColor(IndexedColors.BLACK.getIndex());
		tableCellStyle2.setBorderRight(BorderStyle.THIN);
		tableCellStyle2.setRightBorderColor(IndexedColors.BLACK.getIndex());

		tableCellStyle2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		tableCellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		tableCellStyle3.setAlignment(HorizontalAlignment.CENTER);

		xssfRow = xssfSheet.createRow(rowNo++);
		DataMap data =null;

		//pH Mass_Solubility1 (g/L)
		for(int i = 0; i < 10; i++) {
			xssfCell = xssfRow.createCell((short) i);
			xssfCell.setCellStyle(tableCellStyle);
			xssfCell.setCellValue("PH" + (i + 1));
		}
		xssfRow = xssfSheet.createRow(rowNo++);

		for(int i = 0; i<propList.size(); i++) {
			data = (DataMap) propList.get(i);
			if(data.getString("PROP_TYPE").equals("MS")) {
				for(int j = 0; j<10; j++) {
					xssfCell = xssfRow.createCell((short) j);
					xssfCell.setCellStyle(tableCellStyle);
					xssfCell.setCellValue(data.getString("PH" + (j+1)));

				}
			}
		}

		xssfRow = xssfSheet.createRow(rowNo++);//줄바꿈
		xssfRow = xssfSheet.createRow(rowNo++);//줄바꿈

		//--pH logD 그래프 시작

		xssfSheet.setColumnWidth(rowNo, (xssfSheet.getColumnWidth(rowNo))); // 0번째 컬럼 넓이 조절
		cellStyle.setFont(font); // cellStyle에 font를 적용

		//셀병합
		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 9)); //첫행, 마지막행, 첫열, 마지막열 병합
		// 타이틀 생성
		xssfRow = xssfSheet.createRow(rowNo++); // 행 객체 추가
		xssfCell = xssfRow.createCell((short) 0); // 추가한 행에 셀 객체 추가
		xssfCell.setCellStyle(cellStyle); // 셀에 스타일 지정
		xssfCell.setCellValue("pH logD"); // 데이터 입력

		xssfRow = xssfSheet.createRow(rowNo++);


		for(int i = 0; i < 10; i++) {
			xssfCell = xssfRow.createCell((short) i);
			xssfCell.setCellStyle(tableCellStyle);
			xssfCell.setCellValue("PH" + (i + 1));
		}
		xssfRow = xssfSheet.createRow(rowNo++);

		for(int i = 0; i<propList.size(); i++) {
			data = (DataMap) propList.get(i);
			if(data.getString("PROP_TYPE").equals("LO")) {
				for(int j = 0; j<10; j++) {
					xssfCell = xssfRow.createCell((short) j);
					xssfCell.setCellStyle(tableCellStyle);
					xssfCell.setCellValue(data.getString("PH" + (j+1)));

				}
			}
		}
		//--pH logD 끝

		xssfRow = xssfSheet.createRow(rowNo++);//줄바꿈
		xssfRow = xssfSheet.createRow(rowNo++);//줄바꿈


		//--Prop Detail 시작
		DataMap propDtl = molPredictionService.selectFormulaPropDtl(param); // property detail

		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합
		xssfRow = xssfSheet.createRow(rowNo++);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(tableCellStyle2);
		xssfCell.setCellValue("pka"); // 데이터 입력

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 2);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(tableCellStyle);
		xssfCell.setCellValue(propDtl.getString("PKA")); // 데이터 입력

		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합
		xssfRow = xssfSheet.createRow(rowNo++);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(tableCellStyle2);
		xssfCell.setCellValue("pKb"); // 데이터 입력

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 2);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(tableCellStyle);
		xssfCell.setCellValue(propDtl.getString("PKB")); // 데이터 입력

		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합
		xssfRow = xssfSheet.createRow(rowNo++);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(tableCellStyle2);
		xssfCell.setCellValue("Caco2 Permeability"); // 데이터 입력

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 2);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(tableCellStyle);
		xssfCell.setCellValue(propDtl.getString("CA_PERM")); // 데이터 입력

		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합
		xssfRow = xssfSheet.createRow(rowNo++);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(tableCellStyle2);
		xssfCell.setCellValue("Dosage Form"); // 데이터 입력

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 2);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(tableCellStyle);
		xssfCell.setCellValue(propDtl.getString("DOSAGE_FORM")); // 데이터 입력

		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합
		xssfRow = xssfSheet.createRow(rowNo++);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(tableCellStyle2);
		xssfCell.setCellValue("Molecular wight(g/mol)"); // 데이터 입력

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 2);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(tableCellStyle);
		xssfCell.setCellValue(propDtl.getString("MOL_WEI")); // 데이터 입력

		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합
		xssfRow = xssfSheet.createRow(rowNo++);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(tableCellStyle2);
		xssfCell.setCellValue("Lipinski's Rule of 5"); // 데이터 입력

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 2);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(tableCellStyle);
		xssfCell.setCellValue(propDtl.getString("LIP_RULE")); // 데이터 입력

		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합
		xssfRow = xssfSheet.createRow(rowNo++);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(tableCellStyle2);
		xssfCell.setCellValue("logP"); // 데이터 입력

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 2);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(tableCellStyle);
		xssfCell.setCellValue(propDtl.getString("LOGP")); // 데이터 입력

		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합
		xssfRow = xssfSheet.createRow(rowNo++);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(tableCellStyle2);
		xssfCell.setCellValue("Bolling point(°C)"); // 데이터 입력

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 2);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(tableCellStyle);
		xssfCell.setCellValue(propDtl.getString("BOL_POINT")); // 데이터 입력

		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합
		xssfRow = xssfSheet.createRow(rowNo++);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(tableCellStyle2);
		xssfCell.setCellValue("Bioavailability"); // 데이터 입력

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 2);
		xssfCell.setCellStyle(tableCellStyle2);

		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(tableCellStyle);
		xssfCell.setCellValue(propDtl.getString("BIO")); // 데이터 입력

		//--Prop Detail 끝


		xssfRow = xssfSheet.createRow(rowNo++);//줄바꿈
		xssfRow = xssfSheet.createRow(rowNo++);//줄바꿈

		// --DOSAGE 시작
		List dosageList = molPredictionService.selectFormulaDosage(param); // dosage

		//셀병합
		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 5)); //첫행, 마지막행, 첫열, 마지막열 병합
		// 타이틀 생성
		xssfRow = xssfSheet.createRow(rowNo++); // 행 객체 추가
		xssfCell = xssfRow.createCell((short) 0); // 추가한 행에 셀 객체 추가
		xssfCell.setCellStyle(cellStyle); // 셀에 스타일 지정
		xssfCell.setCellValue("DOSAGE"); // 데이터 입력

		xssfRow = xssfSheet.createRow(rowNo++);//줄바꿈

		xssfRow = xssfSheet.createRow(rowNo++); // 줄바꿈


		xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합


		xssfCell = xssfRow.createCell((short) 3); // 추가한 행에 셀 객체 추가
		xssfCell.setCellStyle(tableCellStyle3);
		xssfCell.setCellValue("oral"); // 데이터 입력


		xssfCell = xssfRow.createCell((short) 4); // 추가한 행에 셀 객체 추가
		xssfCell.setCellStyle(tableCellStyle3);
		xssfCell.setCellValue("parenteral"); // 데이터 입력


		xssfCell = xssfRow.createCell((short) 5); // 추가한 행에 셀 객체 추가
		xssfCell.setCellStyle(tableCellStyle3);
		xssfCell.setCellValue("local"); // 데이터 입력

		xssfRow = xssfSheet.createRow(rowNo++); // 행 객체 추가

		for(int i = 0; i < dosageList.size(); i++) {
			data = (DataMap) dosageList.get(i);

			xssfSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 2)); //첫행, 마지막행, 첫열, 마지막열 병합
			xssfCell = xssfRow.createCell((short) 0); // 추가한 행에 셀 객체 추가
			xssfCell.setCellStyle(tableCellStyle3);
			xssfCell.setCellValue(data.getString("PROPERTIES_NM")); // 데이터 입력

			xssfCell = xssfRow.createCell((short) 3); // 추가한 행에 셀 객체 추가
			xssfCell.setCellStyle(tableCellStyle3);
			xssfCell.setCellValue(data.getString("ORAL_YN").equals("Y")? "O" : "X"); // 데이터 입력

			xssfCell = xssfRow.createCell((short) 4); // 추가한 행에 셀 객체 추가
			xssfCell.setCellStyle(tableCellStyle3);
			xssfCell.setCellValue(data.getString("PARENTERAL_YN").equals("Y")? "O" : "X"); // 데이터 입력

			xssfCell = xssfRow.createCell((short) 5); // 추가한 행에 셀 객체 추가
			xssfCell.setCellStyle(tableCellStyle3);
			xssfCell.setCellValue(data.getString("LOCAL_YN").equals("Y")? "O": "X"); // 데이터 입력


			xssfRow = xssfSheet.createRow(rowNo++); // 행 객체 추가
		}

        response.setContentType("application/download;charset=utf-8");
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("Content-Type",  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); //xlsx 출력
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");


        //파일 경로  파일 객체로 생성
		File saveFolder = new File(EgovWebUtil.filePathBlackList(filePath));

		//파일 경로에 폴더가 없으면 폴더생성
		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}

		//파일경로 + 파일이름
		String realFilePath = filePath + fileName;

		File file = new File(realFilePath);
		FileOutputStream fos = null;
		fos = new FileOutputStream(file);
		xssfWb.write(response.getOutputStream());

		if (fos != null) fos.close();




	}




}
