package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioPHAService {

	// step3 첫 화면
	List selectMenufacStep03(DataMap param) throws Exception;
	
	// step3 save
	void insertMenufacStep03(DataMap param) throws Exception;
}
