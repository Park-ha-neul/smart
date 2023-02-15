package egovframework.bio.manufacturing.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. MethodName : 
 * 2. ClassName  : BioManufacturingMgtServiceImpl
 * 3. Comment   : step1 화면
 * 4. 작성자    : hnpark
 * 5. 작성일    : 2022. 4. 29
 * </PRE>
 *   @param param
 *   @throws Exception
 */

@Service("bioManufacturingMgtService")
public class BioManufacturingMgtServiceImpl extends EgovAbstractServiceImpl implements BioManufacturingMgtService{
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;
	
	public DataMap selectPjtMst(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharm.manufacturing.selectPjtMst", param);
	}
	
	public void updateChoicePrjct(DataMap param) throws Exception {
		commonMybatisDao.update("pharm.manufacturing.updateChoicePrjctSeq", param);
	}
	
	public String selectNextDataExt(DataMap param) throws Exception {
		return (String)commonMybatisDao.selectOne("pharm.manufacturing.selectNextDataExt", param);
	}
	
	// 바이오의약품 step1 save
	public DataMap stepChangeFuncManu(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharm.manufacturing.stepChangeFuncManu", param);
	}

	public void updatePjt_mst(DataMap param) throws Exception {
		commonMybatisDao.update("pharm.manufacturing.updatePjt_mst", param);
	}

	// 바이오의약품 제조 불러오기 첫 화면
	public int selectTotCntManufacturing(DataMap param) throws Exception {
		return (Integer)commonMybatisDao.selectOne("pharm.manufacturing.selectTotCntManufacturing", param);
	}
	
	public List selectPageListManufacturing(DataMap param) throws Exception {
		return (List)commonMybatisDao.selectList("pharm.manufacturing.bioSelectPageListManufacturing", param);
	}
	
	// 프로젝트 복사
	public void manufacturingCopyPrj(DataMap param) {

		String new_prjct_id = commonMybatisDao.selectOne("pharm.manufacturing.projectCreate");
		param.put("new_prjct_id", new_prjct_id);



		commonMybatisDao.update("pharm.manufacturing.manuCopyPrjctMst", param);
		commonMybatisDao.update("pharm.manufacturing.manuStp01Copy", param);
		commonMybatisDao.update("pharm.manufacturing.manuStp02Copy", param);
		commonMybatisDao.update("pharm.manufacturing.manuStp03Copy", param);

	}
}
