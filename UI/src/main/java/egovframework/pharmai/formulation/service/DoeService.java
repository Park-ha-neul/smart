package egovframework.pharmai.formulation.service;

import java.util.List;

import org.springframework.ui.ModelMap;

import egovframework.framework.common.object.DataMap;

public interface DoeService {

	List selectListFormulaStp_05(DataMap param) throws Exception;

	void insertStep6Graph(DataMap param) throws Exception;

	DataMap selectStep6Graph_preto(DataMap param) throws Exception;

	DataMap selectStep6Graph_contour(DataMap param) throws Exception;

	DataMap selectStep6Graph_response(DataMap param) throws Exception;

	void insertStp07Design(DataMap param) throws Exception;

	List selectStep6Design(DataMap param) throws Exception;

	DataMap selectStep6Design_img(DataMap param) throws Exception;

	List selectStp07result(DataMap param) throws Exception;

	DataMap selectStp07resultImg(DataMap param) throws Exception;

	List selectListFormulaStp_06(DataMap param) throws Exception;

}
