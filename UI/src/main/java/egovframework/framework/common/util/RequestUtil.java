package egovframework.framework.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeFilter;
import com.nhncorp.lucy.security.xss.XssSaxFilter;

import egovframework.framework.common.object.DataMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

/**
 * <PRE>
 * 1. ClassName : RequestUtil
 * 2. FileName  : RequestUtil.java
 * 3. Package  : framework.common.util
 * 4. Comment  : request 객체 관련 클래스
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 14. 오후 1:58:47
 * </PRE>
 */
public class RequestUtil {

	private static Log log = LogFactory.getLog(RequestUtil.class);

	/**
	 * <PRE>
	 * 1. MethodName : getDataMap
	 * 2. ClassName  : RequestUtil
	 * 3. Comment   : request 객체의 데이터를 dataMap 에 담기
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오후 1:59:28
	 * </PRE>
	 *   @return DataMap
	 *   @param request
	 *   @return
	 */
	public static DataMap getDataMap(HttpServletRequest request){

		DataMap dMap = new DataMap();
		Enumeration<?> e = request.getParameterNames();

		while(e.hasMoreElements()){
			String key = (String)e.nextElement();
			String[] values = request.getParameterValues(key);
			if (values.length > 1) {
				List<Object> list = new ArrayList<Object>(values.length);
				for (int i = 0; i < values.length; i++) {
					list.add(xssServletFilter(request, key, values[i]));
				}
				dMap.put(key, list);

			} else {
				dMap.put(key, xssServletFilter(request, key, request.getParameter(key)));
			}
		}

		return dMap;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getJsonDataMap
	 * 2. ClassName  : RequestUtil
	 * 3. Comment   : jsonString 으로 오는 파라미터를 DataMap으로 변환
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2018. 10. 19. 오후 1:19:17
	 * </PRE>
	 *   @return DataMap
	 *   @param request
	 *   @return
	 */
	public static DataMap getJsonDataMap(HttpServletRequest request){
		DataMap dMap = new DataMap();
		StringBuffer jb = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			return dMap;
		}

		log.debug("json String : " + jb.toString());

		Map states = JSONObject.fromObject(jb.toString());
		DataMap item = new DataMap();

		Iterator<String> keys = states.keySet().iterator();
		while( keys.hasNext() ){
			String key = keys.next();

			// 리스트 인경우
			if(states.get(key).getClass() == JSONArray.class){
				item.put(key, convertJsonListToDataMapList((JSONArray)states.get(key)));
			}
			// 단순 오브젝트인경우
			else {
				// string 인경우
				if(states.get(key).getClass() == String.class){
					item.put(key, xssServletFilter(request, key, (String)states.get(key)));
				}
				// boolean 형태인경우
				else if(states.get(key).getClass() == Boolean.class){
					// string 으로 변경하여 넣는다.
					item.put(key, String.valueOf(states.get(key)));
				}
				// 널 값인경우 빈값으로 처리
				else if(states.get(key).getClass() == JSONNull.class){
					item.put(key, "");
				}
				// 그외
				else {
					item.put(key, states.get(key));
				}
			}
		}

		return item;
	}

	/**
	 * <PRE>
	 * 1. MethodName : convertJsonListToDataMapList
	 * 2. ClassName  : RequestUtil
	 * 3. Comment   : json array 를 datamap 리스트로 변경
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2018. 10. 22. 오전 10:17:41
	 * </PRE>
	 *   @return List<DataMap>
	 *   @param list
	 *   @return
	 */
	private static List<DataMap> convertJsonListToDataMapList(JSONArray list){
		List<DataMap> ld = new ArrayList();
		DataMap item = new DataMap();

		for(int i = 0; i < list.size(); i++){
			item = new DataMap();
			Map obj = list.getJSONObject(i);

			Iterator<String> keys = obj.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();

				if(obj.get(key).getClass() == JSONArray.class){
					item.put(key, convertJsonListToDataMapList((JSONArray)obj.get(key)));
				}
				else {
					// string 인경우
					if(obj.get(key).getClass() == String.class){
						item.put(key, xssFilter((String)obj.get(key)));
					}
					// boolean 형태인경우
					else if(obj.get(key).getClass() == Boolean.class){
						// string 으로 변경하여 넣는다.
						item.put(key, String.valueOf(obj.get(key)));
					}
					// 널 값인경우 빈값으로 처리
					else if(obj.get(key).getClass() == JSONNull.class){
						item.put(key, "");
					}
					// 그외
					else {
						item.put(key, obj.get(key));
					}
				}
			}

			ld.add(item);
		}

		return ld;
	}

//	public static String htmlTagFilter(String src) {

//		if (src != null) {
//			src = src.replaceAll("&", "&amp;");
//			src = src.replaceAll("<", "&lt;");
//			src = src.replaceAll(">", "&gt;");
//			src = src.replaceAll("\"", "&quot;");	//쌍따옴표
//			src = src.replaceAll("\'", "&#039;");	//작은따옴표
//		}
//		return src;
//	}

	/**
	 * <PRE>
	 * 1. MethodName : xssServletFilter
	 * 2. ClassName  : RequestUtil
	 * 3. Comment   : 네이버 lucy xss 사용; 원래는 servlet filter 등록으로 사용하려 했으나 multipart 문제가 있어서 소스에서 직접 필터링 처리로 변경
	 * 				  lucy-xss-servlet-filter-rule.xml 등록에 따라 필터링 처리한다.
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2020. 7. 23. 오후 1:58:06
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param key
	 *   @param value
	 *   @return
	 */
	public static String xssServletFilter(HttpServletRequest request, String key, String value) {
		String contextPath = ((HttpServletRequest) request).getContextPath();
		String path = ((HttpServletRequest) request).getRequestURI().substring(contextPath.length());
		XssEscapeFilter xssEscapeFilter = XssEscapeFilter.getInstance();

		return xssEscapeFilter.doFilter(path, key, value);
	}

	/**
	 * <PRE>
	 * 1. MethodName : xssFilter
	 * 2. ClassName  : RequestUtil
	 * 3. Comment   : 네이버 xss 방지 적용
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2020. 7. 23. 오후 1:58:02
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param key
	 *   @param value
	 *   @return
	 */
	public static String xssFilter(String value) {
		XssSaxFilter filter = XssSaxFilter.getInstance(true);

		return filter.doFilter(value);
	}

	/**
	 * <PRE>
	 * 1. MethodName : getRemoteAddr
	 * 2. ClassName  : RequestUtil
	 * 3. Comment   : 프록시 서버를 걸쳐온 clientIP도 가져온다.
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 9. 11. 오후 8:04:46
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @return
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");

		if(null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")){
			clientIp = request.getHeader("REMOTE_ADDR");
		}

		if(null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")){
			clientIp = request.getRemoteAddr();
		}

		return clientIp;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getClientIp
	 * 2. ClassName  : RequestUtil
	 * 3. Comment   : 클라이언트 IP
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2018. 9. 18. 오후 5:35:12
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @return
	 */
	public static String getClientIp(HttpServletRequest request){
		String ip = request.getHeader("X-FORWARDED-FOR");

		if(ip == null || ip.length() == 0){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0){
			ip = request.getHeader("WL-Proxy-Client-IP");  // 웹로직
		}
		if(ip == null || ip.length() == 0){
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * <PRE>
	 * 1. MethodName : errorJsonMsg
	 * 2. ClassName  : RequestUtil
	 * 3. Comment   : json 메세지 공통
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2018. 8. 28. 오후 2:55:33
	 * </PRE>
	 *   @return void
	 *   @param response
	 *   @param code
	 *   @param msg
	 */
	public static DataMap errorJsonMsg(HttpServletResponse response, String msg){
		DataMap returnMap = new DataMap();
		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", "error");
		resultStats.put("resultMsg", msg);
		returnMap.put("resultStats", resultStats);//리턴값 : 선택된 메뉴 정보

		return returnMap;
	}
}
