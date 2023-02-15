package egovframework.bio.manufacturing.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("bioFMEAService")
public class BioFMEAServiceImpl extends EgovAbstractServiceImpl implements BioFMEAService{
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;
	
	// step4 첫 화면 호출
	public List selectformulaStep4(DataMap param) throws Exception {
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectformulaStep4", param);
	}

	// step4 save 기능
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
	
	// api4 호출
	public DataMap selectCqaValList(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (DataMap) commonMybatisDao.selectOne("pharm.manufacturing.selectCqaValList", param);
	}


}
