package egovframework.bio.formulation.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioMolPredictionService {
	
	DataMap projectCreate(DataMap param) throws Exception;
	
	DataMap selectformulaStep1(DataMap param) throws Exception;
	
	List selectFormulaProp(DataMap param) throws Exception;

	DataMap selectFormulaPropDtl(DataMap param) throws Exception;
	
	List selectFormulaDosage(DataMap param) throws Exception;
	
	DataMap selectFileData(DataMap param) throws Exception;
	
	void insertFormulaStp_01(DataMap param) throws Exception;
	
	String getBase64String(String sdfPath);
}
