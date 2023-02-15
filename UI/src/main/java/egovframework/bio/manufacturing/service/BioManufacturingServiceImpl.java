package egovframework.bio.manufacturing.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. MethodName : selectManu_Method
 * 2. ClassName  : ManufacturingController
 * 3. Comment   : 제조 - 제보 방법 선택
 * 4. 작성자	: KSM
 * 5. 작성일	: 2021. 8. 09
 * </PRE>
 *
 * @return String
 * @param request
 * @param response
 * @param model
 * @return
 * @throws Exception
 */
@Service("bioManufacturingService")
public class BioManufacturingServiceImpl extends EgovAbstractServiceImpl implements BioManufacturingService {
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;
	
	// api1 input 데이터 호출
	public List selectFormulProp(String route, String prjct_id) throws Exception {
		// TODO Auto-generated method stub

		DataMap param = new DataMap();
		param.put("route", route);
		param.put("prjct_id", prjct_id);

		return commonMybatisDao.selectList("pharm.manufacturing.selectFormulProp", param);
	}
	
	public DataMap selectFormulPropDtl(String route, String prjct_id) throws Exception {
		// TODO Auto-generated method stub
		DataMap param = new DataMap();
		param.put("route", route);
		param.put("prjct_id", prjct_id);
		return commonMybatisDao.selectOne("pharm.manufacturing.selectFormulPropDtl", param);
	}


	public DataMap selectFormulStp02(String route, String prjct_id) throws Exception {
		// TODO Auto-generated method stub

		DataMap param = new DataMap();
		param.put("route", route);
		param.put("prjct_id", prjct_id);

		return commonMybatisDao.selectOne("pharm.manufacturing.selectFormulStp02",  param);
	}


	public List selectFormulStp04(String route, String prjct_id) throws Exception {
		// TODO Auto-generated method stub
		DataMap param = new DataMap();
		param.put("route", route);
		param.put("prjct_id", prjct_id);

		return commonMybatisDao.selectList("pharm.manufacturing.selectFormulStp04", param);
	}

}
