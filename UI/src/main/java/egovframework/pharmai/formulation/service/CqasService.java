package egovframework.pharmai.formulation.service;

import java.util.List;

import org.springframework.ui.ModelMap;

import egovframework.framework.common.object.DataMap;

public interface CqasService {

	List selectListFormulaStp_04(DataMap param) throws Exception;

	void insertFormulaStp_04(DataMap param) throws Exception;

	DataMap selectDosage_Form_type(DataMap param) throws Exception;
}
