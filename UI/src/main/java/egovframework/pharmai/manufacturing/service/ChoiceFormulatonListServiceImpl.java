package egovframework.pharmai.manufacturing.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : ChoiceFormulationServiceImpl.java
 * 3. Package  : egovframework.pharmai.manufacturing.service
 * 4. Comment  : 제형 선택
 * 5. 작성자   : 김성민
 * 6. 작성일   : 2021. 8. 10. 오전 10:56:36
 * </PRE>
 */
@Service("choiceFormulationListService")
public class ChoiceFormulatonListServiceImpl extends EgovAbstractServiceImpl implements ChoiceFormulationListService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	public int selectTotCntStep_06(DataMap param) throws Exception {
		return (Integer)commonMybatisDao.selectOne("pharm.manufacturing.selectTotCntStep_06", param);
	}

	public List selectPageListChoiceFormulation(DataMap param) throws Exception {
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectPageListChoiceFormulation", param);
	}

	public int selectCountChoiceFormulation(DataMap param) throws Exception {
		return (Integer)commonMybatisDao.selectOne("pharm.manufacturing.selectCountChoiceFormulation", param);
	}
}
