package egovframework.framework.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 시스템 공통적으로 사용되는 메소드들을 갖고 있다.
 * </PRE>
 *
 * @author  박재현
 * @version $Revision: 1.2 $
 */
public class SysUtil {

	private static Log log = LogFactory.getLog(SysUtil.class);

	public static Random generator = new Random();
	private static final String DOC_ID_SYMBOLS = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM";

	public static String getDocId() {

		String randomId = "";

		for (int i = 0; i < 20; i++) {
			randomId = randomId + DOC_ID_SYMBOLS.charAt(generator.nextInt(DOC_ID_SYMBOLS.length()));
		}

		return System.currentTimeMillis() + randomId;

	}

	public static String getFileId() {

		String randomId = "";

		for (int i = 0; i < 25; i++) {
			randomId = randomId + DOC_ID_SYMBOLS.charAt(generator.nextInt(DOC_ID_SYMBOLS.length()));
		}

		return System.currentTimeMillis() + randomId;

	}



	public static String newLineForEapprovForHwp(String strOriData) {
		StringBuffer buffer = new StringBuffer();
		strOriData = strOriData.replaceAll("\\", "￦");
		for (int i = 0; i < strOriData.length(); i++) {

			int intCode = String.valueOf(strOriData.charAt(i)).hashCode();
			if (intCode == 10 || intCode == 13) {
				buffer.append("\\r\\n");
				i++;
			} else if (intCode == 34) // ["]
			{
				buffer.append("\\\"");
			} else if (intCode == 39) // [']
			{
				buffer.append("\\\'");
			} else {
				buffer.append(String.valueOf(strOriData.charAt(i)));
			}
		}
		String strReturn = buffer.toString();

		return strReturn;
	}



	public static String fileImg(String fileName) {
		String[] fileGif = { "bmp", "doc", "etc", "exe", "gif", "gul", "htm", "html", "hwp", "ini", "jpg", "mgr", "mpg", "pdf", "ppt", "print", "tif", "txt", "wav", "xls", "xml",
				"zip" };
		if (fileName == null || "".equals(fileName)) {
			return "";
		}

		int start = fileName.lastIndexOf(".");
		String name = "";
		if (start > -1) {
			name = fileName.substring(start + 1).toLowerCase();
		}

		boolean retFlag = false;
		for (int fileInx = 0; fileInx < fileGif.length; fileInx++) {
			if (name.equals(fileGif[fileInx])) {
				retFlag = true;
				break;
			}
		}
		String retStr = "";
		if (retFlag) {
			retStr = "<img src='/zz/img/attach_" + name + ".gif'>";
		} else {
			retStr = "<img src='/zz/img/icon_doc.gif'>";
		}
		return retStr;

	}

	/*
	 * 파일 명을 구해온다
	 */
	public static String getFileName(String fileName) {

		String stringStr = "";

		if (fileName.lastIndexOf(".") != -1) {
			stringStr = fileName.substring(0, fileName.lastIndexOf("."));
		}

		return stringStr;
	}

	/*
	 * 파일 확장자를 구해온다
	 */
	public static String getFileExtName(String fileName) {

		String stringStr = "";

		if (fileName.lastIndexOf(".") != -1) {
			stringStr = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		}

		return stringStr;
	}

	/**
	 * <PRE>
	 * 페이지 리다이렉트시 필요한 파라메터를 생성한다.
	 * </PRE>
	 *
	 * @author  박재현
	 * @version $Revision: 1.2 $
	 */
	public static String createInputObj(DataMap dataMap) {

		int max = dataMap.size() - 1;
		Iterator it = dataMap.entrySet().iterator();
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i <= max; i++) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			String value = null;
			Object o = entry.getValue();

			if (o == null) {
				value = "";
			} else {
				Class c = o.getClass();
				if (c.getName().equalsIgnoreCase("java.util.ArrayList")) {
					ArrayList<String> tmpList = (ArrayList) o;
					for (int k = 0; k < tmpList.size(); k++) {
						buf.append("<input type = \"hidden\" name = \"" + key + "\" value = \"" + tmpList.get(k) + "\"/>\n");
					}
				} else {
					value = o.toString();
					buf.append("<input type = \"hidden\" name = \"" + key + "\" value = \"" + value + "\"/>\n");
				}
			}
		}
		return buf.toString();
	}

	/**
	 * <PRE>
	 * 1. MethodName : createUrlParam
	 * 2. ClassName  : SysUtil
	 * 3. Comment   : 리턴 방식이 GET 일때 return URL의 Param 생성
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 12. 오전 9:36:10
	 * </PRE>
	 *   @return String
	 *   @param dataMap
	 *   @return
	 */
	public static String createUrlParam(DataMap dataMap) {
		String addUrlStr = "";

		if(dataMap == null) {
			return addUrlStr;
		}else {
			addUrlStr = "?";
		}

		List<String> keyList = new ArrayList<String>(dataMap.keySet());
		for (int i = 0; i < keyList.size(); i++) {
			if(i == 0) {
				addUrlStr += keyList.get(i) + "=" + dataMap.getString(keyList.get(i)).replaceAll("\\[", "").replaceAll("\\]", "");
			}else {
				addUrlStr += "&" + keyList.get(i) + "=" + dataMap.getString(keyList.get(i)).replaceAll("\\[", "").replaceAll("\\]", "");
			}
		}

		return addUrlStr;
	}

	/**
	 * <PRE>
	 * 1. MethodName : byteCalculation
	 * 2. ClassName  : SysUtil
	 * 3. Comment   :
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 12. 오전 10:03:43
	 * </PRE>
	 *   @return String
	 *   @param bytes
	 *   @return
	 */
	public static String byteCalculation(String bytes) {
		String retFormat = "0";
		Double size = Double.parseDouble(bytes);
		String[] s = { "bytes", "KB", "MB", "GB", "TB", "PB" };

		if (!bytes.equals("0")) {
			int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
			DecimalFormat df = new DecimalFormat("#,###.##");
			double ret = ((size / Math.pow(1024, Math.floor(idx))));
			retFormat = df.format(ret) + " " + s[idx];
		} else {
			retFormat += " " + s[0];
		}
		 return retFormat;
	}



	/**
	 * <PRE>
	 * 1. MethodName : getBrowser
	 * 2. ClassName  : SysUtil
	 * 3. Comment   : 브라우저 종류
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 12. 29. 오전 10:00:28
	 * </PRE>
	 *   @return String
	 *   @param header
	 *   @return
	 */
	public static String getBrowser(HttpServletRequest request){
		String header = request.getHeader("User-Agent");
		String browser = "";
		if (header.indexOf("MSIE") > -1) {
			browser = "MSIE";
		} else if (header.indexOf("rv:11.0") > -1) {
			browser = "MSIE";
		} else if (header.indexOf("Chrome") > -1) {
			browser = "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			browser = "Opera";
		} else {
			browser = "Firefox";
		}

		return browser;
	}

	public static String getBrowserFileName(HttpServletRequest request, String filename){
		String browser = getBrowser(request);
		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE")) {
			try {
				encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
//				System.out.println("######### 예외 발생41 ##########");
				log.error("######### 예외 발생41 ##########");
			}
		} else if (browser.equals("Firefox")) {
			try {
				encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
//				System.out.println("######### 예외 발생42 ##########");
				log.error("######### 예외 발생42 ##########");
			}
		} else if (browser.equals("Opera")) {
			try {
				encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
//				System.out.println("######### 예외 발생43 ##########");
				log.error("######### 예외 발생43 ##########");
			}
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					try {
						sb.append(URLEncoder.encode("" + c, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
//						System.out.println("######### 예외 발생44 ##########");
						log.error("######### 예외 발생44 ##########");
					}
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		}

		return dispositionPrefix + encodedFilename;
	}


	/**
	 * <PRE>
	 * 1. MethodName : dataMapToJsonString
	 * 2. ClassName  : SysUtil
	 * 3. Comment   : datamap TO json string
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2020. 5. 20. 오후 6:14:50
	 * </PRE>
	 *   @return String
	 *   @param data
	 *   @return
	 *   @throws Exception
	 */
	public static String dataMapToJsonString(DataMap data) throws Exception {
		String returnStr = "";

		if(data != null) {
			ObjectMapper mapper = new ObjectMapper();
			returnStr = mapper.writeValueAsString(data);
		}

		return returnStr;
	}

	/**
	 * <PRE>
	 * 1. MethodName : dataJsonStringToMap
	 * 2. ClassName  : SysUtil
	 * 3. Comment   : json string TO datamap
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2020. 5. 20. 오후 6:16:02
	 * </PRE>
	 *   @return DataMap
	 *   @param jsonString
	 *   @return
	 *   @throws Exception
	 */
	public static DataMap jsonStringToDataMap(String jsonString) throws Exception {
		DataMap returnMap = new DataMap();

		if(!StringUtil.nvl(jsonString).equals("")) {
			ObjectMapper mapper = new ObjectMapper();
			returnMap = mapper.readValue(jsonString, DataMap.class);
		}

		return returnMap;
	}
}
