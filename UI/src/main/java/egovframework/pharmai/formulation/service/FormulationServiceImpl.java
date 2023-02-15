package egovframework.pharmai.formulation.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import egovframework.framework.common.constant.Const;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("formulationService")
public class FormulationServiceImpl extends EgovAbstractServiceImpl implements FormulationService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

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
	public List selectPageListProject(ModelMap model,DataMap param) throws Exception {
		return(List)commonMybatisDao.selectList("pharmai.formulation.selectPageListProject", param);
	}

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
	public int selectTotCntProject(DataMap param) throws Exception {
		return (Integer)commonMybatisDao.selectOne("pharmai.formulation.selectTotCntProject", param);
	}

	public String selectNextDataExt(DataMap param) throws Exception {
		return (String)commonMybatisDao.selectOne("pharmai.formulation.selectNextDataExt", param);
	}

	public void updateChoicePrjct(DataMap param) throws Exception {
		commonMybatisDao.update("pharmai.formulation.updateChoicePrjctSeq", param);
	}

	public DataMap selectPjtMst(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharmai.formulation.selectPjtMst", param);
	}

	public void prjStepChange(DataMap param) throws Exception{
		commonMybatisDao.update("pharmai.formulation.prjStepChange", param);
	}

	public void copyPrjct(DataMap param) throws Exception{

		String new_prjct_id = commonMybatisDao.selectOne("pharmai.formulation.projectCreate");
		param.put("new_prjct_id", new_prjct_id);

		commonMybatisDao.update("pharmai.formulation.copyPrjctMst", param);
		commonMybatisDao.update("pharmai.formulation.copyPrjctStp01", param);
		commonMybatisDao.update("pharmai.formulation.copyPrjctProp", param);
		commonMybatisDao.update("pharmai.formulation.copyPrjctDosage", param);
		commonMybatisDao.update("pharmai.formulation.copyPrjctPropDtl", param);
		commonMybatisDao.update("pharmai.formulation.copyPrjctStp02", param);
		commonMybatisDao.update("pharmai.formulation.copyPrjctExcipient", param);
	}

	public void projectNmUpdate(DataMap param) throws Exception {
		commonMybatisDao.update("pharmai.formulation.projectNmUpdate", param);
	}


	public void updateUseYnStep01(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("pharmai.formulation.updateUseYnStep01", param);

	}


	public void updateUseYnStep01Prop(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("pharmai.formulation.updateUseYnStep01Prop", param);
	}


	public void updateUseYnStep01PropDtl(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("pharmai.formulation.updateUseYnStep01PropDtl", param);
	}


	public void updateUseYnStep01Dosage(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("pharmai.formulation.updateUseYnStep01Dosage", param);
	}


	public void updateUseYnFormulaStp02(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("pharmai.formulation.updateUseYnFormulaStp02", param);
	}


	public void updateUseYnExcipient(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("pharmai.formulation.updateUseYnExcipient", param);
	}


	public void updateUseYnStep3(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("pharmai.formulation.updateUseYnStep3", param);
	}


	public void updateUseYnFormulaStp_04(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("pharmai.formulation.updateUseYnFormulaStp_04", param);
	}


	public void updateUseYnFormulaStp_05(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("pharmai.formulation.updateUseYnFormulaStp_05", param);
	}

	public DataMap stepChangeFunc(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectOne("pharmai.formulation.stepChangeFunc", param);
	}

	public void updatePrjMst(DataMap param) throws Exception {

		List prjct_id_list = param.getList("filter");

		DataMap data = null;
		for(int i = 0; i < prjct_id_list.size(); i++) {
			data = new DataMap();
			data.put("ss_user_no", param.getString("ss_user_no"));
			data.put("prjct_id", prjct_id_list.get(i));

			commonMybatisDao.update("pharmai.formulation.updatePrjMst", data);
		}
	}

	public void updatePjt_mst(DataMap param) throws Exception {
		commonMybatisDao.update("pharmai.formulation.updatePjt_mst", param);
	}

}
