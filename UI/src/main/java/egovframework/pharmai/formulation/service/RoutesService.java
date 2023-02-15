package egovframework.pharmai.formulation.service;

import java.util.List;

import org.springframework.ui.ModelMap;

import egovframework.framework.common.object.DataMap;

public interface RoutesService {

	DataMap selectStp02(DataMap param) throws Exception;

	void insertFormulaStp_02(DataMap param ) throws Exception;

	DataMap selectDataStep2(DataMap param) throws Exception;

	DataMap selectDosageFormCnt(DataMap param) throws Exception;

	List selectListExcipient(DataMap param) throws Exception;

	void insertStep2(DataMap param) throws Exception;
}
