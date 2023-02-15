package egovframework.bio.manufacturing.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("bioPHAService")
public class BioPHAServiceImpl extends EgovAbstractServiceImpl implements BioPHAService{
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;
	
	// step3 첫 화면
	public List selectMenufacStep03(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectMenufacStep03", param);
	}
	
	// step3 save
	public void insertMenufacStep03(DataMap param) throws Exception {

		String cqas = param.getString("cqas");

		param.put("fp_cqas_status", cqas);
		param.put("type", 'H');

		commonMybatisDao.insert("pharm.manufacturing.insertMenufacStep03", param);

		List cqaStatus = param.getList("cqaStatus");

		DataMap data = null;
		for(int i = 0; i < cqaStatus.size(); i++) {
			data = new DataMap();
			data.put("prjct_id", param.getString("prjct_id")); // 받아온 param 으로 차후 수정
			data.put("type", "V");
			data.put("fp_cqas_status", cqaStatus.get(i));

			//td 개수
			for(int j = 0; j < param.getInt("th_length"); j ++ ) {
				data.put("pha_col_" + (j + 1), param.getList("PHA_ICON_" + (j + 1)).get(i));
			}
			data.put("ss_user_no", param.getString("ss_user_no"));

			commonMybatisDao.insert("pharm.manufacturing.insertMenufacStep03", data);

		}

		//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharm.manufacturing.updateMstStep", param);
	}

}
