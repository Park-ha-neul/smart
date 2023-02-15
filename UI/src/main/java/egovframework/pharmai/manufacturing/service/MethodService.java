package egovframework.pharmai.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : Manufacturing_Step1Service.java
 * 3. Package  : egovframework.pharmai.manufacturing.service
 * 4. Comment  : 제형 선택
 * 5. 작성자   : 김성민
 * 6. 작성일   : 2021. 8. 10. 오전 10:56:36
 * </PRE>
 */
public interface MethodService {

	DataMap projectCreate(DataMap param) throws Exception;

	void insertPrjMst(DataMap param) throws Exception;

	void insertManufacturStp_01(DataMap param) throws Exception;

	void projectNmUpdate(DataMap param) throws Exception;

	List selectManufacturingStep1(DataMap param) throws Exception;

	DataMap selectFormulationProp_dtl(DataMap param) throws Exception;

	DataMap selectFormulationStp2_dtl(DataMap param) throws Exception;

	List selectFormulationStp4_dtl(DataMap param) throws Exception;

	List selectFormulationProp(DataMap param) throws Exception;

	void insertCopyFormulationProp(DataMap param) throws Exception;

	void insertCopyFormulationProp_dtl(DataMap prop_dtl) throws Exception;

	void insertCopyFormulationStp2_dtl(DataMap stp2_dtl) throws Exception;

	void insertCopyFormulationStp4_dtl(DataMap param) throws Exception;

	List selectCopy_Prop(DataMap param) throws Exception;

	DataMap selectCopy_Prop_dtl(DataMap param) throws Exception;

	DataMap selectCopy_Stp2_dtl(DataMap param) throws Exception;

	List selectCopy_Stp4_cqa(DataMap param) throws Exception;

}
