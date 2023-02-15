package egovframework.pharmai.formulation.service;

import java.util.List;

import org.springframework.ui.ModelMap;

import egovframework.framework.common.object.DataMap;

public interface FormulationService {

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListProject
	 * 2. ClassName  : FormulationServiceImpl
	 * 3. Comment   : 프로젝트 리스트
	 * 4. 작성자    : KSM
	 * 5. 작성일    : 2021. 6. 23. 오후 3:30:40
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListProject (ModelMap model,DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntProject
	 * 2. ClassName  : FormulationServiceImpl
	 * 3. Comment   : 프로젝트 총 개수 조회
	 * 4. 작성자    : KSM
	 * 5. 작성일    : 2021. 6. 23. 오후 3:30:40
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntProject(DataMap param) throws Exception;

	String selectNextDataExt(DataMap param) throws Exception;

	void updateChoicePrjct(DataMap param) throws Exception;

	DataMap selectPjtMst(DataMap param) throws Exception;

	void prjStepChange(DataMap param) throws Exception;

	void copyPrjct(DataMap param) throws Exception;

	void projectNmUpdate(DataMap param) throws Exception;

	public void updateUseYnStep01(DataMap param) throws Exception;

	public void updateUseYnStep01Prop(DataMap param) throws Exception;

	public void updateUseYnStep01PropDtl(DataMap param) throws Exception;

	public void updateUseYnStep01Dosage(DataMap param) throws Exception;

	public void updateUseYnFormulaStp02(DataMap param) throws Exception;

	public void updateUseYnExcipient(DataMap param) throws Exception;

	public void updateUseYnStep3(DataMap param) throws Exception;

	public void updateUseYnFormulaStp_04(DataMap param) throws Exception;

	public void updateUseYnFormulaStp_05(DataMap param) throws Exception;

	DataMap stepChangeFunc(DataMap param) throws Exception;

	public void updatePrjMst(DataMap param) throws Exception;

	public void updatePjt_mst(DataMap param) throws Exception;

}
