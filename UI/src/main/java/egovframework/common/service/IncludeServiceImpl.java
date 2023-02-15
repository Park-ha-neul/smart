package egovframework.common.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("includeService")
public class IncludeServiceImpl extends EgovAbstractServiceImpl implements
		IncludeService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;
	
	public List selectTopMenuList(DataMap dataMap) throws Exception {
		return commonMybatisDao.selectList("common.selectTopMenuList", dataMap);
	}

	public List selectLeftMenuList(DataMap dataMap) throws Exception {
		return commonMybatisDao.selectList("common.selectLeftMenuList", dataMap);
	}

	public DataMap selectMenuByUrl(DataMap dataMap) throws Exception {
		return (DataMap)commonMybatisDao.selectOne("common.selectMenuByUrl", dataMap);
	}
}
