package egovframework.framework.common.util;

import java.util.List;

import egovframework.common.service.CodeCacheService;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : CacheCommboUtil.java
 * 3. Package  : egovframework.framework.common.util
 * 4. Comment  : Cache 데이터의 Commbo 생성
 * 5. 작성자   : kimkm
 * 6. 작성일   : 2020. 4. 22. 오후 3:47:46
 * </PRE>
 */
public class CacheCommboUtil extends EgovAbstractDAO {

	/**
	 * <PRE>
	 * 1. MethodName : getComboStr
	 * 2. ClassName  : CacheCommboUtil
	 * 3. Comment   : groupId의 코드 리스트 생성
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 23. 오후 3:18:03
	 * </PRE>
	 *   @return String
	 *   @param groupId
	 *   @param valueColName
	 *   @param nameColName
	 *   @param selectedDtlCd
	 *   @param addOptionType
	 *   @return
	 *   @throws Exception
	 */
	public static String getComboStr(String groupId, String valueColName, String nameColName, String selectedDtlCd, String addOptionType) throws Exception {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		if (addOptionType.equals("C")) { 				// Choice
			rstlStr = "<option value=''>선택</option>";
		} else if (addOptionType.equals("A")) { 		// All
			rstlStr = "<option value=''>전체</option>";
		} else if (!addOptionType.equals("")) {
			rstlStr = "<option value=''>" + addOptionType + "</option>";
		}

		List resourceCodeList = CodeCacheService.getCode(groupId);

		for (int i = 0; i < resourceCodeList.size(); i++) {
			selStr = "";
			tmpMap = (DataMap) resourceCodeList.get(i);
			if (tmpMap.getString(valueColName).equals(selectedDtlCd)) {
				selStr = "selected='selected'";
			}
			rstlStr += "<option value='" + tmpMap.getString(valueColName) + "' " + selStr + ">" + tmpMap.getString(nameColName) + "</option>";
		}
		return rstlStr;
	}


	/**
	 * <PRE>
	 * 1. MethodName : getComboStr
	 * 2. ClassName  : CacheCommboUtil
	 * 3. Comment   : groupId의 특정 코드 리스트 반환
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 23. 오후 3:19:36
	 * </PRE>
	 *   @return String
	 *   @param groupId
	 *   @param codeName
	 *   @param valueColName
	 *   @param nameColName
	 *   @param selectedDtlCd
	 *   @param addOptionType
	 *   @return
	 *   @throws Exception
	 */
	public static String getEqulesComboStr(String groupId, String code, String valueColName, String nameColName, String selectedDtlCd, String addOptionType) throws Exception {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		if (addOptionType.equals("C")) { 				// Choice
			rstlStr = "<option value=''>선택</option>";
		} else if (addOptionType.equals("A")) { 		// All
			rstlStr = "<option value=''>전체</option>";
		} else if (!addOptionType.equals("")) {
			rstlStr = "<option value=''>" + addOptionType + "</option>";
		}

		List resourceCodeList = CodeCacheService.getCode(groupId, code);

		for (int i = 0; i < resourceCodeList.size(); i++) {
			selStr = "";
			tmpMap = (DataMap) resourceCodeList.get(i);
			if (tmpMap.getString(valueColName).equals(selectedDtlCd)) {
				selStr = "selected='selected'";
			}
			rstlStr += "<option value='" + tmpMap.getString(valueColName) + "' " + selStr + ">" + tmpMap.getString(nameColName) + "</option>";
		}
		return rstlStr;
	}


	/**
	 * <PRE>
	 * 1. MethodName : getComboStrAttr
	 * 2. ClassName  : CacheCommboUtil
	 * 3. Comment   : groupId의 코드 및 Attr 리스트 반환
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 23. 오후 3:13:37
	 * </PRE>
	 *   @return String
	 *   @param groupId
	 *   @param valueColName
	 *   @param nameColName
	 *   @param selectedDtlCd
	 *   @param addOptionType
	 *   @return
	 *   @throws Exception
	 */
	public static String getComboStrAttr(String groupId, String valueColName, String nameColName, String selectedDtlCd,	String addOptionType) throws Exception {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		List resourceCodeList = CodeCacheService.getCode(groupId);

		if (addOptionType.equals("C")) { // Choice
			rstlStr = "<option value=''>선택</option>";
		} else if (addOptionType.equals("A")) { // All
			rstlStr = "<option value=''>전체</option>";
		} else if (!addOptionType.equals("")) {
			rstlStr = "<option value=''>" + addOptionType + "</option>";
		}

		for (int i = 0; i < resourceCodeList.size(); i++) {
			selStr = "";
			tmpMap = (DataMap) resourceCodeList.get(i);
			if (tmpMap.getString(valueColName).equals(selectedDtlCd)) {
				selStr = "selected='selected'";
			}

			rstlStr += "<option value='" + tmpMap.getString(valueColName) + "' "
					+ " attr_1='" + tmpMap.getString("ATTRB_1") + "' "
					+ " attr_2='" + tmpMap.getString("ATTRB_2") + "' "
					+ " attr_3='" + tmpMap.getString("ATTRB_3") + "' "
					+	selStr + ">"
					+ 	tmpMap.getString(nameColName) + "</option>";
		}

		return rstlStr;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getComboStrAttr
	 * 2. ClassName  : CacheCommboUtil
	 * 3. Comment   : groupId의 특정코드 및 Attr 리스트 반환
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 23. 오후 3:13:37
	 * </PRE>
	 *   @return String
	 *   @param groupId
	 *   @param valueColName
	 *   @param nameColName
	 *   @param selectedDtlCd
	 *   @param addOptionType
	 *   @return
	 *   @throws Exception
	 */
	public static String getEqulesComboStrAttr(String groupId, String code, String valueColName, String nameColName, String selectedDtlCd, String addOptionType) throws Exception {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		List resourceCodeList = CodeCacheService.getCode(groupId, code);

		if (addOptionType.equals("C")) { // Choice
			rstlStr = "<option value=''>선택</option>";
		} else if (addOptionType.equals("A")) { // All
			rstlStr = "<option value=''>전체</option>";
		} else if (!addOptionType.equals("")) {
			rstlStr = "<option value=''>" + addOptionType + "</option>";
		}

		for (int i = 0; i < resourceCodeList.size(); i++) {
			selStr = "";
			tmpMap = (DataMap) resourceCodeList.get(i);
			if (tmpMap.getString(valueColName).equals(selectedDtlCd)) {
				selStr = "selected='selected'";
			}
			System.out.println(tmpMap.getString("ATTRB_1"));
			System.out.println(tmpMap.getString("ATTRB_2"));
			System.out.println(tmpMap.getString("ATTRB_3"));
			rstlStr += "<option value='" + tmpMap.getString(valueColName) + "' "
					+ " attr_1='" + tmpMap.getString("ATTRB_1") + "' "
					+ " attr_2='" + tmpMap.getString("ATTRB_2") + "' "
					+ " attr_3='" + tmpMap.getString("ATTRB_3") + "' "
					+	selStr + ">"
					+ 	tmpMap.getString(nameColName) + "</option>";
		}

		return rstlStr;
	}


	/**
	 *
	 * <PRE>
	 * 1. MethodName : getRadioStrAttrImg
	 * 2. ClassName  : CacheCommboUtil
	 * 3. Comment   : 속성 1에 들어있는 이미지와 groupId를 라디오 버튼의 형태로 출력
	 * 4. 작성자    : :LWJ
	 * 5. 작성일    : 2020. 6. 17. 오후 5:07:52
	 * </PRE>
	 *   @return String
	 *   @param groupId
	 *   @param valueColName
	 *   @param nameColName
	 *   @param selectedDtlCd
	 *   @param radioName
	 *   @return
	 *   @throws Exception
	 */
	public static String getRadioStrAttrImg(String groupId, String valueColName, String nameColName, String selectedDtlCd, String radioName) throws Exception {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		List resourceCodeList = CodeCacheService.getCode(groupId);

		for (int i = 0; i < resourceCodeList.size(); i++) {
			selStr = "";
			tmpMap = (DataMap) resourceCodeList.get(i);
			if (tmpMap.getString(valueColName).equals(selectedDtlCd)) {
				selStr = "checked='checked'";
			}

			rstlStr +=  "<div style='display:inline-block; margin-right:10px;'>"
					+ "		<img src='" + tmpMap.getString("ATTRB_1") + "' style='display:block; margin:10px auto; width:50px;'>"
					+  "	<div class='form-check' style='text-align:center;'>"
					+  "		<input class='form-check-input' type='radio' name='"+radioName+"' id='"+radioName+i+"' value='"+tmpMap.getString(valueColName)+"'"+selStr+" />"
					+  "		<label class='form-check-label' for='"+radioName+i+"' style='margin-right:10px;'>"
					+  				tmpMap.getString(nameColName)
					+  "		</label>"
					+ "		</div>"
					+ "</div>";
		}
		return rstlStr;
	}

}
