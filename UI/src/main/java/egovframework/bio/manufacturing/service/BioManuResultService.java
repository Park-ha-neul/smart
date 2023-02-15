package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioManuResultService {
	
	// step8 첫 화면 호출
	DataMap selectMenufacStep09_pareto(DataMap param) throws Exception;
	
	DataMap selectMenufacStep09_contour(DataMap param) throws Exception;

	DataMap selectMenufacStep09_response(DataMap param) throws Exception;
	
	List selectStp10DesignList(DataMap param) throws Exception;

	DataMap selectStp10DesignImgData(DataMap param) throws Exception;

	List selectStp10result(DataMap param) throws Exception;

	DataMap selectStp10ResultImg(DataMap param) throws Exception;
	
	List selectMenufactStep06List(DataMap param) throws Exception;
	
	// api7번 호출
	void insertMenufacStep09(DataMap param) throws Exception;
	
	// api8번 호출
	void insertManufactStep10(DataMap param) throws Exception;

}
