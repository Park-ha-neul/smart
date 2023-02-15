package egovframework.bio.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioManufacturingService {

	// api1 input 데이터 호출
	List selectFormulProp(String route, String prjct_id ) throws Exception;
	
	DataMap selectFormulPropDtl(String route, String prjct_id) throws Exception;

	DataMap selectFormulStp02(String route, String prjct_id) throws Exception;

	List selectFormulStp04(String route, String prjct_id) throws Exception;
}
