package egovframework.bio.formulation.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("bioRoutesService")
public class BioRoutesServiceImpl extends EgovAbstractServiceImpl implements BioRoutesService{
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	public DataMap selectDataStep2(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("pharmai.formulation.selectDataStep2", param);
	}
	
	public DataMap selectDosageFormCnt(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("pharmai.formulation.selectDosageFormCnt", param);
	}

	public List selectListExcipient(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectListExcipient", param);
	}
	
	// step2에서 입력한 데이터를 불러옴
	public DataMap selectStp02(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (DataMap)commonMybatisDao.selectOne("pharmai.formulation.selectStp02", param);
	}
	
	public void insertFormulaStp_02(DataMap param) throws Exception{
		commonMybatisDao.insert("pharmai.formulation.insertStep2", param);

		String stp_02_seq = commonMybatisDao.selectOne("pharmai.formulation.selectStp02Seq");
		List excipiecntList = param.getList("shape_name");

		for(int i = 0; i< excipiecntList.size(); i++) {
			param.put("stp_02_seq", stp_02_seq);
			param.put("excipiecnt_nm", excipiecntList.get(i));

			if(excipiecntList.get(i).equals(param.getString("shape_list"))) {
				param.put("check_yn", "Y");
			}else {
				param.put("check_yn", "N");
			}

			commonMybatisDao.insert("pharmai.formulation.insertExcipient", param);
		}

	   	//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharmai.formulation.updateMstStep", param);

	}
}
