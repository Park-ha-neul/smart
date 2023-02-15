package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioManuExperimentService {

	// step8 첫 화면 호출
	List selectMenufacStep08(DataMap param) throws Exception;
	
	int selectManufactStp06List(DataMap param) throws Exception;
	
	// step8 save 기능
	void insertMenufacStep08(DataMap param) throws Exception;
	
	// api6 호출
	List selectStp_06_data(DataMap param) throws Exception;
	
	// step8 실험치 중간저장 기능
	void temporarySave(DataMap param) throws Exception;
}
