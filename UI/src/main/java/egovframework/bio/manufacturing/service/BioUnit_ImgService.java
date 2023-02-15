package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioUnit_ImgService {

	// step5 첫 화면 호출
	List selectStp05List(DataMap param) throws Exception;

	List selectDistinctUnitList(DataMap param) throws Exception;

	List selectDistinctCppList(DataMap param) throws Exception;
	
	// step5 save 기능
	void insertUnit_Img(DataMap param) throws Exception;
}
