package egovframework.bio.formulation.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioExcipientService {

	// step3
	List selectStp03(DataMap param) throws Exception;
	
	// step3에서 입력한 사용범위를 넘겨줌
	void insertStep3(DataMap param) throws Exception;
}
