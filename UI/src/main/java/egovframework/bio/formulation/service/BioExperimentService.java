package egovframework.bio.formulation.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioExperimentService {
	
	// STEP5 첫 화면 생성
	List selectListFormulaStp_05(DataMap param) throws Exception;

	int selectStp3TotYn(DataMap param) throws Exception;
	
	int selectStp5ColumnCount() throws Exception;
	
	// API5번 호출
	List selectListFormulaStp_03(DataMap param) throws Exception;
	
	List selectListFormulaStp_04(DataMap param) throws Exception;
	
	// STEP5 save 기능
	void insertFormulaStp_05(DataMap param) throws Exception;
	
	void formulTemporarySave(DataMap param) throws Exception;
}
