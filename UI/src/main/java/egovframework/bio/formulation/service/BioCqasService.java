package egovframework.bio.formulation.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioCqasService {

	List selectListFormulaStp_04(DataMap param) throws Exception;
	
	DataMap selectDosage_Form_type(DataMap param) throws Exception;
	
	// step4 save 기능
	void insertFormulaStp_04(DataMap param) throws Exception;
}
