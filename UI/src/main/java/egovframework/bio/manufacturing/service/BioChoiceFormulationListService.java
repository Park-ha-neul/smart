package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioChoiceFormulationListService {

	// 바이오의약품 제조 시작하기
	int selectTotCntStep_06(DataMap param) throws Exception;

	List selectPageListChoiceFormulation(DataMap param) throws Exception;
	
	int selectPageCountChoiceFormulation(DataMap param) throws Exception;

}
