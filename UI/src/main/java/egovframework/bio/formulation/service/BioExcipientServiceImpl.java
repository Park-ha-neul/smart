package egovframework.bio.formulation.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("bioExcipientService")
public class BioExcipientServiceImpl extends EgovAbstractServiceImpl implements BioExcipientService{
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;
	
	// select step3 (첫 화면)
	public List selectStp03(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectStp03", param);
	}
	
	// step3에서 입력한 사용범위 저장
	public void insertStep3(DataMap param) throws Exception {

		List excipients = param.getList("excipients");
		List kind = param.getList("kind");
		List use_range_s = param.getList("use_range_s");
		List use_range_e = param.getList("use_range_e");
		List maximum = param.getList("maximum");
		List unit = param.getList("unit");
		List checkYn  = param.getList("checkYn");
		List startRange = param.getList("startRange");
		List endRange = param.getList("endRange");

		DataMap data = new DataMap();

		for(int i = 0; i < excipients.size(); i++) {
    		data = new DataMap();
    		data.put("prjct_id", param.getString("prjct_id"));
    		data.put("ss_user_no", param.getString("ss_user_no"));
    		data.put("excipient", excipients.get(i));
    		data.put("kind", kind.get(i));
    		data.put("maximum", maximum.get(i));
    		data.put("unit", unit.get(i));
    		data.put("use_range_s", use_range_s.get(i));
    		data.put("use_range_e", use_range_e.get(i));
    		data.put("checkYn", checkYn.get(i));
    		data.put("ipt_use_range_s", startRange.get(i));
    		data.put("ipt_use_range_e", endRange.get(i));


    		commonMybatisDao.insert("pharmai.formulation.insertFormulaStp03", data);
		}

	   	//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharmai.formulation.updateMstStep", param);

	}
	
}
