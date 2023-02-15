package egovframework.bio.formulation.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface BioDoeService {
	
	// step6 첫 화면 생성 (api6번을 통해 나온 그래프를 보여주는 화면)
	// pareto chart
	DataMap selectStep6Graph_preto(DataMap param) throws Exception;
	
	// contour plot
	DataMap selectStep6Graph_contour(DataMap param) throws Exception;
	
	// response
	DataMap selectStep6Graph_response(DataMap param) throws Exception;
	
	// design area
	List selectStep6Design(DataMap param) throws Exception;
	
	// design space
	DataMap selectStep6Design_img(DataMap param) throws Exception;
	
	List selectStp07result(DataMap param) throws Exception;

	DataMap selectStp07resultImg(DataMap param) throws Exception;
	
	List selectListFormulaStp_06(DataMap param) throws Exception;
	
	// api6번 호출
	void insertStep6Graph(DataMap param) throws Exception;
	
	// api7번 호출
	void insertStp07Design(DataMap param) throws Exception;

}
