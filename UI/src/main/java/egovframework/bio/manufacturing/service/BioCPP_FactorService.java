package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioCPP_FactorService {
	
	// step6 첫 화면
	List selectMenufactStep06(DataMap param) throws Exception;

	// step6 save 기능
	void insertMenufactStep06(DataMap param) throws Exception;
	
	// api4-1 호출
	DataMap selectDistinctUnitStp05(DataMap param) throws Exception;
}
