/**
 *
 * 1. FileName : CommboUtil.java
 * 2. Package : egovframework.framework.common.util
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 9. 1. 오후 12:32:01
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 9. 1. :            : 신규 개발.
 */

package egovframework.framework.common.util;

import java.util.List;

import egovframework.framework.common.constant.Const;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : CommboUtil.java
 * 3. Package  : egovframework.framework.common.util
 * 4. Comment  :
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 9. 1. 오후 12:32:01
 * </PRE>
 */

public class CommboUtil extends EgovAbstractDAO {


	public static String getOrganComboStr(List resourceCodeList, String valueColName, String nameColName, String selectedDtlCd, String addOptionType, String authorId, String organCode) {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		if (addOptionType.equals("C")) {	//Choice
			rstlStr = "<option value=''>선택</option>";
		}
		else if (addOptionType.equals("A")) {	//All
			rstlStr = "<option value=''>전체</option>";
		}
		else if(!addOptionType.equals("")){
			rstlStr = "<option value=''>" + addOptionType + "</option>";
		}

		if(authorId.equals(Const.CODE_AUTHOR_ADMIN)){

			for(int i = 0; i < resourceCodeList.size(); i++){
				selStr = "";
				tmpMap = (DataMap)resourceCodeList.get(i);
				if(tmpMap.getString(valueColName).equals(selectedDtlCd)){
					selStr = "selected='selected'";
				}

				rstlStr += "<option value='"+tmpMap.getString(valueColName)+"' "+selStr+">"+tmpMap.getString(nameColName)+"</option>";
			}
		}
		else{

			for(int i = 0; i < resourceCodeList.size(); i++){
				selStr = "";
				tmpMap = (DataMap)resourceCodeList.get(i);
				if(tmpMap.getString(valueColName).equals(organCode)){
					selStr = "selected='selected'";
					rstlStr += "<option value='"+tmpMap.getString(valueColName)+"' "+selStr+">"+tmpMap.getString(nameColName)+"</option>";
				}
			}

		}





		return rstlStr;
	}


	public static String getComboStr(List resourceCodeList, String valueColName, String nameColName, String selectedDtlCd, String addOptionType) {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		if (addOptionType.equals("C")) {	//Choice
			rstlStr = "<option value=''>선택</option>";
		}
		else if (addOptionType.equals("A")) {	//All
			rstlStr = "<option value=''>전체</option>";
		}
		else if(!addOptionType.equals("")){
			rstlStr = "<option value=''>" + addOptionType + "</option>";
		}

		for(int i = 0; i < resourceCodeList.size(); i++){
			selStr = "";
			tmpMap = (DataMap)resourceCodeList.get(i);
			if(tmpMap.getString(valueColName).equals(selectedDtlCd)){
				selStr = "selected='selected'";
			}

			rstlStr += "<option value='"+tmpMap.getString(valueColName)+"' "+selStr+">"+tmpMap.getString(nameColName)+"</option>";
		}

		return rstlStr;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getComboStr2
	 * 2. ClassName  : CommboUtil
	 * 3. Comment   : select box 그리기
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2020. 5. 25. 오후 2:29:03
	 * </PRE>
	 *   @return String
	 *   @param resourceCodeList
	 *   @param compareColName
	 *   @param nameColName
	 *   @param selectedDtlCd
	 *   @param valueColName
	 *   @param addOptionType
	 *   @return
	 */
	public static String getComboStr2(List resourceCodeList, String compareColName, String nameColName, String selectedDtlCd, String valueColName, String addOptionType) {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		if (addOptionType.equals("C")) {	//Choice
			rstlStr = "<option value=''>선택</option>";
		}
		else if (addOptionType.equals("A")) {	//All
			rstlStr = "<option value=''>전체</option>";
		}
		else if(!addOptionType.equals("")){
			rstlStr = "<option value=''>" + addOptionType + "</option>";
		}

		for(int i = 0; i < resourceCodeList.size(); i++){
			selStr = "";
			tmpMap = (DataMap)resourceCodeList.get(i);
			if(tmpMap.getString(compareColName).equals(selectedDtlCd))
				selStr = "selected='selected'";

			rstlStr += "<option value='"+tmpMap.getString(valueColName)+"' "+selStr+">"+tmpMap.getString(nameColName)+"</option>";
		}

		return rstlStr;
	}


	// 코드 리스트에서 해당 코드 이름 찾을때 사용
	public static String getComboName(List resourceCodeList, String valueColName, String nameColName, String selectedDtlCd) {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		for(int i = 0; i < resourceCodeList.size(); i++){
			selStr = "";
			tmpMap = (DataMap)resourceCodeList.get(i);
			if(tmpMap.getString(valueColName).equals(selectedDtlCd)){
				rstlStr = tmpMap.getString(nameColName);
				break;
			}
		}

		return rstlStr;
	}

	public static String getComboRadioStr(List resourceCodeList, String valueColName, String nameColName, String selectedDtlCd, String name) {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		for(int i = 0; i < resourceCodeList.size(); i++){
			selStr = "";
			tmpMap = (DataMap)resourceCodeList.get(i);
			if(tmpMap.getString(valueColName).equals(selectedDtlCd))
				selStr = "checked=\"checked\"";

			rstlStr += "<input type='radio' name='" + name + "' id='" + (name + i) + "' value='"+tmpMap.getString(valueColName)+"' "+selStr+" / ><label for='" + (name + i) + "'>"+tmpMap.getString(nameColName)+"</label>  ";
		}

		return rstlStr;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getComboStrExcept
	 * 2. ClassName  : CommboUtil
	 * 3. Comment   : select list 중 특정값 제외
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 11. 10. 오전 8:37:53
	 * </PRE>
	 *   @return String
	 *   @param resourceCodeList
	 *   @param valueColName
	 *   @param nameColName
	 *   @param selectedDtlCd
	 *   @param addOptionType
	 *   @param exceptVal
	 *   @return
	 */
	public static String getComboStrExcept(List resourceCodeList, String valueColName, String nameColName, String selectedDtlCd, String addOptionType, String[] exceptVal) {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		if (addOptionType.equals("C")) {	//Choice
			rstlStr = "<option value=''>선택하세요</option>";
		}
		else if (addOptionType.equals("A")) {	//All
			rstlStr = "<option value=''>전체</option>";
		}

		for(int i = 0; i < resourceCodeList.size(); i++){
			selStr = "";
			tmpMap = (DataMap)resourceCodeList.get(i);
			boolean view = true;

			for(int j = 0; j < exceptVal.length; j++){
				String v = tmpMap.getString(valueColName);
				if(exceptVal[j].equals(v)){
					view = false;
					break;
				}
			}

			if(view){
				if(tmpMap.getString(valueColName).equals(selectedDtlCd))
					selStr = "selected='selected'";

				rstlStr += "<option value='"+tmpMap.getString(valueColName)+"' "+selStr+">"+tmpMap.getString(nameColName)+"</option>";
			}
		}

		return rstlStr;
	}



	/**
	 * <PRE>
	 * 1. MethodName : getCommboHourStr
	 * 2. ClassName  : CommboUtil
	 * 3. Comment   : 시간 select list 폼
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 11. 10. 오전 8:37:42
	 * </PRE>
	 *   @return String
	 *   @param selectedNum
	 *   @return
	 */
	public static String getCommboHourStr(String selectedNum){
		String rstlStr = "";

		if(selectedNum.equals("") || selectedNum == null){
			selectedNum = "00";
		}

		selectedNum = String.format("%02d", Integer.parseInt(selectedNum));

		for(int i = 0; i < 24; i++){
			String h = String.format("%02d", i);
			String select = "";

			if(selectedNum.equals(h)){
				select = "selected=\"selected\"";
			}

			rstlStr += "<option value=\"" + h + "\" " + select + ">" + h + "</option>";
		}

		return rstlStr;
	}

	public static String getCommboMinStr(String selectedNum){
		return getCommboMinStr(selectedNum, "1");
	}

	/**
	 * <PRE>
	 * 1. MethodName : getCommboMinStr
	 * 2. ClassName  : CommboUtil
	 * 3. Comment   : 분 select 폼 리스트
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 11. 10. 오전 8:37:25
	 * </PRE>
	 *   @return String
	 *   @param selectedNum
	 *   @param passTime
	 *   @return
	 */
	public static String getCommboMinStr(String selectedNum, String passTime){
		String rstlStr = "";
		int pass = Integer.parseInt(passTime) > 0 ? Integer.parseInt(passTime) : 1;

		if(selectedNum.equals("") || selectedNum == null){
			selectedNum = "00";
		}

		selectedNum = String.format("%02d", Integer.parseInt(selectedNum));

		for(int i = 0; i < 60; i += pass){
			String h = String.format("%02d", i);
			String select = "";

			if(selectedNum.equals(h)){
				select = "selected=\"selected\"";
			}

			rstlStr += "<option value=\"" + h + "\" " + select + ">" + h + "</option>";
		}

		return rstlStr;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getOrganComboStrAttr
	 * 2. ClassName  : CommboUtil
	 * 3. Comment   : 속성값 사용 select list
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 26. 오후 2:17:55
	 * </PRE>
	 *   @return String
	 *   @param resourceCodeList
	 *   @param valueColName
	 *   @param nameColName
	 *   @param selectedDtlCd
	 *   @param addOptionType
	 *   @param attrColName
	 *   @param authorId
	 *   @param organCode
	 *   @return
	 */
	public static String getOrganComboStrAttr(List resourceCodeList, String valueColName, String nameColName, String selectedDtlCd, String addOptionType, String attrColName, String authorId, String organCode) {

		String rstlStr = "";
		String selStr = "";
		DataMap tmpMap = new DataMap();

		if(authorId.equals(Const.CODE_AUTHOR_ADMIN)){

			if (addOptionType.equals("C")) {	//Choice
				rstlStr = "<option value=''>선택</option>";
			}
			else if (addOptionType.equals("A")) {	//All
				rstlStr = "<option value=''>전체</option>";
			}
			else if(!addOptionType.equals("")){
				rstlStr = "<option value=''>" + addOptionType + "</option>";
			}

			for(int i = 0; i < resourceCodeList.size(); i++){
				selStr = "";
				tmpMap = (DataMap)resourceCodeList.get(i);
				if(tmpMap.getString(valueColName).equals(selectedDtlCd)){
					selStr = "selected='selected'";
				}

				rstlStr += "<option value='"+tmpMap.getString(valueColName)+"' "+selStr+
						" attr_1='"+tmpMap.getString(attrColName)+"'>"
						+tmpMap.getString(nameColName)+"</option>";
			}
		}
		else{

			for(int i = 0; i < resourceCodeList.size(); i++){
				selStr = "";
				tmpMap = (DataMap)resourceCodeList.get(i);
				if(tmpMap.getString(valueColName).equals(organCode)){
					selStr = "selected='selected'";
					rstlStr += "<option value='"+tmpMap.getString(valueColName)+"' "+selStr+
							" attr_1='"+tmpMap.getString(attrColName)+"'>"
							+tmpMap.getString(nameColName)+"</option>";
				}
			}

		}





		return rstlStr;
	}
}
