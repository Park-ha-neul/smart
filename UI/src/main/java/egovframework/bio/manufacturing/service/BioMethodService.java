package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioMethodService {

	// 바이오의약품 제조 시작하기에서 Formulation 선택하기 시
	DataMap projectCreate(DataMap param) throws Exception;
	
	List selectFormulationProp(DataMap param) throws Exception;
	
	DataMap selectFormulationProp_dtl(DataMap param) throws Exception;
	
	DataMap selectFormulationStp2_dtl(DataMap param) throws Exception;
	
	List selectFormulationStp4_dtl(DataMap param) throws Exception;
	
	void insertCopyFormulationProp(DataMap param) throws Exception;
	
	void insertCopyFormulationProp_dtl(DataMap prop_dtl) throws Exception;

	void insertCopyFormulationStp2_dtl(DataMap stp2_dtl) throws Exception;

	void insertCopyFormulationStp4_dtl(DataMap param) throws Exception;
	
	void projectNmUpdate(DataMap param) throws Exception;
	
	// 바이오의약품 제조 step1 첫 화면
	List selectCopy_Prop(DataMap param) throws Exception;
	
	DataMap selectCopy_Prop_dtl(DataMap param) throws Exception;

	DataMap selectCopy_Stp2_dtl(DataMap param) throws Exception;
	
	List selectManufacturingStep1(DataMap param) throws Exception;
	
	// 바이오의약품 step1 저장
	void insertPrjMst(DataMap param) throws Exception;
	
	void insertManufacturStp_01(DataMap param) throws Exception;
	
	// 바이오의약품 api3 호출
	List selectCopy_Stp4_cqa(DataMap param) throws Exception;
}
