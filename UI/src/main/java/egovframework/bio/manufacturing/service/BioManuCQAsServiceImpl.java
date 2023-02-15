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
 * 2. FileName  : BioManuCQAsServiceImpl.java
 * 3. Package  : egovframework.bio.manufacturing.service
 * 4. Comment  : step7
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 4. 29
 * </PRE>
 */
@Service("bioManuCQAsService")
public class BioManuCQAsServiceImpl extends EgovAbstractServiceImpl implements BioManuCQAsService{
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;
	
	// step7 첫 화면 호출
	public List selectMenufacStep07(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectMenufacStep07", param);
	}
	
	// step7 save 기능 
	public void insertMenufacStep07(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		List checkYnList = param.getList("checkYn");
		List cqas_name_List = param.getList("cqas_name");

		DataMap data = null;
		for(int i = 0; i<cqas_name_List.size(); i++) {
			data = new DataMap();

			data.put("prjct_id", param.getString("prjct_id"));
			data.put("check_yn", checkYnList.get(i));
			data.put("cqa_nm", cqas_name_List.get(i));
			data.put("ss_user_no", param.getString("ss_user_no"));

			commonMybatisDao.insert("pharm.manufacturing.insertMenufacStep07", data);

		}

		//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharm.manufacturing.updateMstStep", param);


	}

}
