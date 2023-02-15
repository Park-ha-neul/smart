/**
 * 0. Project  : XXXX 프로젝트
 *
 * 1. FileName : DataMap.java
 * 2. Package : framework.common.object
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 14. 오전 10:58:55
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 14. :            : 신규 개발.
 */

package egovframework.framework.common.object;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : DataMap.java
 * 3. Package  : framework.common.object
 * 4. Comment  :
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 14. 오전 10:58:55
 * </PRE>
 */

public class DataMap extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DataMap.class);

	/**
	 * <PRE>
	 * 1. MethodName : DataMap
	 * 2. ClassName  : DataMap
	 * 3. Comment   :
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오전 10:58:55
	 * </PRE>
	 */
	public DataMap() {
		super();
	}

	public DataMap(LinkedHashMap<String, Object> data) {
		putAll(data);
	}

	/**
	 * <PRE>
	 * 1. MethodName : getBoolean
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 boolean 형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오후 1:00:16
	 * </PRE>
	 *   @return boolean
	 *   @param key
	 *   @return
	 */
	public boolean getBoolean(String key) {
		String value = getString(key);
		boolean isTrue = false;
		try {
			isTrue = (new Boolean(value)).booleanValue();
		} catch (Exception e) {
			log.error("######### 예외 발생5 ##########");
		}
		return isTrue;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getString
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 String 형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오전 11:28:19
	 * </PRE>
	 *   @return String
	 *   @param key
	 *   @return
	 */
	public String getString(String key) {

		if (key == null) {
			return "";
		}

		Object value = get(key);

		if (value == null) {
			return "";
		}

		return value.toString();
	}

	/**
	 * <PRE>
	 * 1. MethodName : getStringOrgn
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 String 원형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 12. 10. 오후 3:26:10
	 * </PRE>
	 *   @return String
	 *   @param key
	 *   @return
	 */
	public String getStringOrgn(String key) {

		if (key == null) {
			return "";
		}

		Object value = get(key);

		if (value == null) {
			return "";
		}

		return htmlTagFilterRestore(value.toString());
	}

	/**
	 * <PRE>
	 * 1. MethodName : getString
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 String 형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오전 11:28:19
	 * </PRE>
	 *   @return String
	 *   @param key
	 *   @return
	 */
	public String getString(String key, String rtnStr) {

		if (key == null) {
			return rtnStr;
		}

		Object value = get(key);

		if (value == null) {
			return rtnStr;
		} else if (value.toString().equals("")) {
			return rtnStr;
		}

		return value.toString();
	}

	public String getNullYn(String key) {

		if (key == null) {
			return "Y";
		}

		Object value = get(key);

		if (value == null) {
			return "Y";
		} else {
			return "N";
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : getStringOrgn
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 String 원형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 12. 10. 오후 3:25:50
	 * </PRE>
	 *   @return String
	 *   @param key
	 *   @param rtnStr
	 *   @return
	 */
	public String getStringOrgn(String key, String rtnStr) {

		if (key == null) {
			return rtnStr;
		}

		Object value = get(key);

		if (value == null) {
			return rtnStr;
		} else if (value.toString().equals("")) {
			return rtnStr;
		}

		return htmlTagFilterRestore(value.toString());
	}

	/**
	 * <PRE>
	 * 1. MethodName : getInt
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 int 형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오후 12:58:30
	 * </PRE>
	 *   @return int
	 *   @param key
	 *   @return
	 */
	public int getInt(String key) {
		double value = getDouble(key);
		return (int) value;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getLong
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 long 형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오후 12:58:48
	 * </PRE>
	 *   @return long
	 *   @param key
	 *   @return
	 */
	public long getLong(String key) {

		String value = getString(key);
		if (value.equals(""))
			return 0L;

		long lvalue = 0L;
		try {
			lvalue = Long.valueOf(value).longValue();
		} catch (Exception e) {
			lvalue = 0L;
		}

		return lvalue;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getFloat
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 float 형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오후 12:59:21
	 * </PRE>
	 *   @return double
	 *   @param key
	 *   @return
	 */
	public double getFloat(String key) {
		return (float) getDouble(key);
	}

	/**
	 * <PRE>
	 * 1. MethodName : getDouble
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 double 형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오후 12:53:17
	 * </PRE>
	 *   @return double
	 *   @param key
	 *   @return
	 */
	public double getDouble(String key) {

		String value = getString(key);

		if (value.equals(""))
			return 0;
		double num = 0;
		try {
			num = Double.valueOf(value).doubleValue();
		} catch (Exception e) {
			num = 0;
		}
		return num;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getArray
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 Array 형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오전 11:37:38
	 * </PRE>
	 *   @return String[]
	 *   @param key
	 *   @return
	 */
	public String[] getArray(String key) {

		if (key == null) {
			return new String[0];
		}

		Object value = get(key);

		if (value == null) {
			return new String[0];
		}

		if (value instanceof String[]) {
			return (String[]) value;
		} else {
			return new String[] { ((String) value).toString() };
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : getList
	 * 2. ClassName  : DataMap
	 * 3. Comment   : DataMap 에서 List 형태로 값추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 14. 오후 12:38:08
	 * </PRE>
	 *   @return List<Object>
	 *   @param key
	 *   @return
	 */
	public List<Object> getList(String key) {

		List<Object> list = null;

		try {
			list = new ArrayList<Object>();
			list = (List<Object>) super.get(key);
		} catch (ClassCastException e) {

			try {

				list = new ArrayList<Object>();
				list.add((String) super.get(key));
			} catch (Exception se) {
				list = null;
			}
		}

		return list;
	}

	public String getHtml(String key) {
		String value = null;
		try {
			Object o = (Object) super.get(key);
			Class c = o.getClass();
			if (o == null)
				value = "";
			else if (c.isArray()) {
				int length = Array.getLength(o);
				if (length == 0)
					value = "";
				else {
					Object item = Array.get(o, 0);
					if (item == null)
						value = "";
					else {
						value = item.toString();
						value = value.replaceAll("\n", "<br/>");
						value = value.replaceAll(" ", "&nbsp;");
					}
				}
			} else {
				value = o.toString();
				value = value.replaceAll("\n", "<br/>");
				value = value.replaceAll(" ", "&nbsp;");
			}
		} catch (Exception e) {
			value = "";
		}
		return value;
	}

	public String htmlTagFilterRestore(String src) {

		if (src != null) {
			src = src.replaceAll("&amp;", "&");
			src = src.replaceAll("&lt;", "<");
			src = src.replaceAll("&gt;", ">");
			src = src.replaceAll("&quot;", "\"");
			src = src.replaceAll("&#039;", "\'");
			src = src.replaceAll("<br/>", "\n");
			src = src.replaceAll("&nbsp;", " ");
		}
		return src;
	}

}
