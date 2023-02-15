package egovframework.pharmai.manufacturing.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.egovutil.EgovFileScrty;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.CookieUtil;
import egovframework.framework.common.util.HashUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.cryptography.EgovEnvCryptoService;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : UserServiceImpl.java
 * 3. Package  : egovframework.admin.user.web.service
 * 4. Comment  : 사용자 관리
 * 5. 작성자   : 함경호
 * 6. 작성일   : 2015. 9. 1. 오후 3:34:30
 * </PRE>
 */
@Service("cpp_FactorService")
public class CPP_FactorServiceImpl extends EgovAbstractServiceImpl implements CPP_FactorService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	/** 암호화서비스 */
	@Resource(name = "egovEnvCryptoService")
	EgovEnvCryptoService cryptoService;


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

	public List selectMenufactStep06(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectMenufactStep06", param);
	}

	public DataMap selectDistinctUnitStp05(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (DataMap) commonMybatisDao.selectOne("pharm.manufacturing.selectDistinctUnitStp05", param);
	}


}
