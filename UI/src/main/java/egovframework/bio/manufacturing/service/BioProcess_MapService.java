package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioProcess_MapService {
	
	// step2 첫 화면 호출
	List selectMenufacStep02(DataMap param) throws Exception;
	
	// step2 save
	void insertMenufacStep02(DataMap param) throws Exception;
	
	// api2 호출
	DataMap selectStp_01_data(DataMap param) throws Exception;

}
