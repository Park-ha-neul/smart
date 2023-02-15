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
 * 2. FileName  : FMEAServiceImpl.java
 * 3. Package  : egovframework.pharmai.manufacturing.service
 * 4. Comment  : FMEA테이블
 * 5. 작성자   : 김성민
 * 6. 작성일   : 2021. 8. 12. 오후 6:20:30
 * </PRE>
 */
@Service("fmeaService")
public class FMEAServiceImpl extends EgovAbstractServiceImpl implements FMEAService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	/** 암호화서비스 */
	@Resource(name = "egovEnvCryptoService")
	EgovEnvCryptoService cryptoService;

	public List selectformulaStep4(DataMap param) throws Exception {
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectformulaStep4", param);
	}

	public void insertMenufacStep04(DataMap param) throws Exception {

		String prjct_id = param.getString("prjct_id");
		List Arr_Unit = param.getList("Unit");
		List Arr_CPPs = param.getList("CPPs");
		List Arr_Failure_Mode = param.getList("Failure_Mode");
		List Arr_IP_FP_CQAs = param.getList("IP_FP_CQAs");
		List rist_status = param.getList("riskStatus");

		DataMap data = new DataMap();
		for(int i = 0; i < Arr_Unit.size(); i++) {

			data.put("prjct_id", prjct_id);
			data.put("FMEA_UNIT", Arr_Unit.get(i));
			data.put("FMEA_CPPS", Arr_CPPs.get(i));
			data.put("FMEA_FAILURE_MODE", Arr_Failure_Mode.get(i));
			data.put("FMEA_CQAS_QTTP", Arr_IP_FP_CQAs.get(i));
			data.put("risk_status", rist_status.get(i));
			data.put("ss_user_no", param.getString("ss_user_no"));

			commonMybatisDao.insert("pharm.manufacturing.insertMenufacStep04", data);
		}

		//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharm.manufacturing.updateMstStep", param);

	}

	public DataMap selectCqaValList(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (DataMap) commonMybatisDao.selectOne("pharm.manufacturing.selectCqaValList", param);
	}

}


