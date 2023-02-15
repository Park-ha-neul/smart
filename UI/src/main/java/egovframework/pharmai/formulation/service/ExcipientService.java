package egovframework.pharmai.formulation.service;

import java.util.List;

import org.springframework.ui.ModelMap;

import egovframework.framework.common.object.DataMap;

public interface ExcipientService {

	List selectStp03(DataMap param) throws Exception;

	void insertStep3(DataMap param) throws Exception;
}
