package egovframework.framework.common.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jxls.common.Context;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.jxls.util.JxlsHelper;
import org.springframework.web.multipart.MultipartFile;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : ExcelTemplateUtil.java
 * 3. Package  : egovframework.framework.common.util
 * 4. Comment  : 엑셀파일 template로 다운로드
 * 5. 작성자   : JJH
 * 6. 작성일   : 2018. 6. 8. 오전 11:29:34
 * </PRE>
 */ 
public class ExcelTemplateUtil {
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 브라우저 구분 얻기.
	 * 
	 * @param request
	 * @return
	 */
	private static String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (header.indexOf("rv:11.0") > -1) {
			return "MSIE";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}

	/**
	 * Disposition 지정하기.
	 * 
	 * @param filename
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private static void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String browser = getBrowser(request);

		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else {
			//throw new RuntimeException("Not supported browser");
			throw new IOException("Not supported browser");
		}

		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

		if ("Opera".equals(browser)) {
			response.setContentType("application/octet-stream;charset=UTF-8");
		}
	}
	
	/**
	 * <PRE>
	 * 1. MethodName : downloadExcel
	 * 2. ClassName  : ExcelTemplateUtil
	 * 3. Comment   : 엑셀 다운로드
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2018. 6. 8. 오전 11:30:31
	 * </PRE>
	 *   @return void
	 *   @throws Exception
	 */
	public static void downloadExcel(HttpServletRequest request, HttpServletResponse response, DataMap data, String templateFile, String fileNm) throws Exception {
		String mimetype = "application/vnd.ms-excel";
		response.setContentType(mimetype);
		setDisposition(fileNm, request, response);
		
		InputStream is = null;
		OutputStream os = null;
		
		is = new BufferedInputStream(new FileInputStream(EgovPropertiesUtil.getProperty("excel.tempalte.path") + "/" + templateFile));
		os = response.getOutputStream();
		
		Context context = new Context();
		context.putVar("data", data);
		JxlsHelper.getInstance().processTemplate(is, os, context);
	}
	
	/**
	 * <PRE>
	 * 1. MethodName : readExcel
	 * 2. ClassName  : ExcelTemplateUtil
	 * 3. Comment   : 엑셀 파일 읽기
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2018. 9. 28. 오전 11:25:09
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param configFile
	 *   @param file
	 *   @param mappingMap
	 *   @throws Exception
	 */
	public static void readExcel(HttpServletRequest request, HttpServletResponse response, String configFile, MultipartFile file, DataMap mappingMap) throws Exception {
		
		InputStream inputXML = new BufferedInputStream(new FileInputStream(EgovPropertiesUtil.getProperty("excel.config.path") + "/" + configFile));
		XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
		InputStream inputXLS = new BufferedInputStream(file.getInputStream());
		
		List<DataMap> result = new ArrayList<DataMap>();
		
		// mappingMap에 데이터 입력이됨
		XLSReadStatus readStatus = mainReader.read(inputXLS, mappingMap);
	}
}

