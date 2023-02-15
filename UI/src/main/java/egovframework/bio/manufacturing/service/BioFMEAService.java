package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioFMEAService {

	// step4 첫 화면
	List selectformulaStep4(DataMap param) throws Exception;
	
	// step4 save 기능
	void insertMenufacStep04(DataMap param) throws Exception;
	
	// api4 호출
	DataMap selectCqaValList(DataMap param) throws Exception;
}
