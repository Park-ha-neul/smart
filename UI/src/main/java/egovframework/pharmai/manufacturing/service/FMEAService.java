package egovframework.pharmai.manufacturing.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : FMEAService.java
 * 3. Package  : egovframework.pharmai.manufacturing.service
 * 4. Comment  : FMEA테이블
 * 5. 작성자   : 김성민
 * 6. 작성일   : 2021. . 12. 오후 6:20:36
 * </PRE>
 */
public interface FMEAService {

	void insertMenufacStep04(DataMap param) throws Exception;

	List selectformulaStep4(DataMap param) throws Exception;

	DataMap selectCqaValList(DataMap param) throws Exception;

}
