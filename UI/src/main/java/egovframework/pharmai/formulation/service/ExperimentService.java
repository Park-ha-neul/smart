package egovframework.pharmai.formulation.service;

import java.util.List;

import org.springframework.ui.ModelMap;

import egovframework.framework.common.object.DataMap;

public interface ExperimentService {

	void insertFormulaStp_05(DataMap param) throws Exception;

	List selectListFormulaStp_03(DataMap param) throws Exception;

	List selectListFormulaStp_04(DataMap param) throws Exception;

	List selectListFormulaStp_05(DataMap param) throws Exception;

	int selectStp5ColumnCount() throws Exception;

	int selectStp3TotYn(DataMap param) throws Exception;

	void formulTemporarySave(DataMap param) throws Exception;
}
