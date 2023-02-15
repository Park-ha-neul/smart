package egovframework.bio.manufacturing.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("bioChoiceFormulationListService")
public class BioChoiceFormulationListServiceImpl extends EgovAbstractServiceImpl implements BioChoiceFormulationListService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;
	
	// 바이오의약품 제조 시작하기
	public int selectTotCntStep_06(DataMap param) throws Exception {
		return (Integer)commonMybatisDao.selectOne("pharm.manufacturing.bioSelectTotCntStep_06", param);
	}

	public List selectPageListChoiceFormulation(DataMap param) throws Exception {
		return (List)commonMybatisDao.selectList("pharm.manufacturing.bioSelectPageListChoiceFormulation", param);
	}
	
	public int selectPageCountChoiceFormulation(DataMap param) throws Exception {
		return (Integer)commonMybatisDao.selectOne("pharm.manufacturing.bioSelectCountListChoiceFormulation", param);
	}
}
