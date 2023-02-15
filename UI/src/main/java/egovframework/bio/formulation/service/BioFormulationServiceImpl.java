package egovframework.bio.formulation.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("bioFormulationService")
public class BioFormulationServiceImpl extends EgovAbstractServiceImpl implements BioFormulationService{
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;
	
	/**
	 * <PRE>
	 * 1. MethodName : selectPageListProject
	 * 2. ClassName  : BioFormulationServiceImpl
	 * 3. Comment   : 프로젝트 리스트
	 * 4. 작성자    : hnpark
	 * 5. 작성일    : 2022. 4. 21
	 * </PRE>
	 *   @return String
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListProject(ModelMap model,DataMap param) throws Exception {
		return(List)commonMybatisDao.selectList("pharmai.formulation.bioSelectPageListProject", param);
	}
	
	public int selectTotCntProject(DataMap param) throws Exception {
		return (Integer)commonMybatisDao.selectOne("pharmai.formulation.bioSelectTotCntProject", param);
	}
	
	// 바이오의약품 제형 시작하기
	public DataMap selectPjtMst(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharmai.formulation.selectPjtMst", param);
	}
	
	public void updateChoicePrjct(DataMap param) throws Exception {
		commonMybatisDao.update("pharmai.formulation.updateChoicePrjctSeq", param);
	}
	
	public String selectNextDataExt(DataMap param) throws Exception {
		return (String)commonMybatisDao.selectOne("pharmai.formulation.selectNextDataExt", param);
	}
	
	// 바이오의약품 제형 > 시작하기 > 프로젝트 명 입력 시 등록
	public void projectNmUpdate(DataMap param) throws Exception {
		commonMybatisDao.update("pharmai.formulation.projectNmUpdate", param);
	}
	
	public DataMap stepChangeFunc(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectOne("pharmai.formulation.stepChangeFunc", param);
	}
	
	public void updatePjt_mst(DataMap param) throws Exception {
		commonMybatisDao.update("pharmai.formulation.updatePjt_mst", param);
	}
	
	// 프로젝트 복사
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
	
	// 프로젝트 삭제
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
}
