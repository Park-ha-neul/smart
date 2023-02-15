package egovframework.bio.manufacturing.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BioCPP_FactorServiceImpl.java
 * 3. Package  : egovframework.bio.manufacturing.service
 * 4. Comment  : step6
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 4. 29
 * </PRE>
 */
@Service("bioCpp_FactorService")
public class BioCPP_FactorServiceImpl extends EgovAbstractServiceImpl implements BioCPP_FactorService{
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;
	
	// step6 첫 화면
	public List selectMenufactStep06(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectMenufactStep06", param);
	}
	
	// step6 save 기능
	public void insertMenufactStep06(DataMap param) throws Exception {

		List cpp_factor = param.getList("cpp_factor");
		List process = param.getList("process");
		List startRange = param.getList("startRange");
		List endRange = param.getList("endRange");
		List checkYn = param.getList("checkYn");
		List use_range_s = param.getList("use_range_s");
		List use_range_e = param.getList("use_range_e");

		DataMap data =new DataMap();

		for(int i = 0; i< cpp_factor.size(); i++) {
			data = new DataMap();

			data.put("prjct_id", param.getString("prjct_id"));
			data.put("check_yn", checkYn.get(i));
			data.put("process", process.get(i));
			data.put("cpp_factor", cpp_factor.get(i));
			data.put("use_range_s", use_range_s.get(i));
			data.put("use_range_e", use_range_e.get(i));
			data.put("startRange", startRange.get(i));
			data.put("endRange", endRange.get(i));
			data.put("ss_user_no", param.getString("ss_user_no"));

			commonMybatisDao.insert("pharm.manufacturing.insertMenufactStep06", data);
		}

		//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharm.manufacturing.updateMstStep", param);

	}
	
	// api4-1 호출
	public DataMap selectDistinctUnitStp05(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (DataMap) commonMybatisDao.selectOne("pharm.manufacturing.selectDistinctUnitStp05", param);
	}

}
