package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioManuCQAsService {

	// step7 첫 화면 호출
	List selectMenufacStep07(DataMap param) throws Exception;
	
	// step7 save 기능
	void insertMenufacStep07(DataMap param) throws Exception;
}
