package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioManufacturingMgtService {
	
	// 바이오의약품 step1 화면 출력
	DataMap selectPjtMst(DataMap param) throws Exception;
	
	void updateChoicePrjct(DataMap param) throws Exception;
	
	String selectNextDataExt(DataMap param) throws Exception;
	
	// 바이오의약품 step1 save
	DataMap stepChangeFuncManu(DataMap param) throws Exception;
	
	void updatePjt_mst(DataMap param) throws Exception;

	// 바이오의약품 제조 불러오기 첫 화면
	int selectTotCntManufacturing(DataMap param) throws Exception;
	
	List selectPageListManufacturing(DataMap param) throws Exception;
	
	// 프로젝트 복사
	void manufacturingCopyPrj(DataMap param) throws Exception;
}
