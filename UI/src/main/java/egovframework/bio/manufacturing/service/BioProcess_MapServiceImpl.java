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
 * 2. FileName  : BioProcess_MapServiceImpl.java
 * 3. Package  : egovframework.bio.web.service
 * 4. Comment  :  Step2 페이지
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 4. 29
 * </PRE>
 */
@Service("bioProcess_MapService")
public class BioProcess_MapServiceImpl extends EgovAbstractServiceImpl implements BioProcess_MapService {
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;
	
	// step2 initPage
	public List selectMenufacStep02(DataMap param) throws Exception {
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectMenufacStep02", param);
	}
	
	// step2 save
	public void insertMenufacStep02(DataMap param) throws Exception {
		// TODO Auto-generated method stub

		List process_name_list = param.getList("process_name");
		List input_material_list = param.getList("input_material");
		List process_param_list = param.getList("process_param");
		List output_material_list = param.getList("output_material");
		List checkYn_list = param.getList("checkYn");

		DataMap data = null;
		for(int i = 0; i < process_name_list.size(); i++) {
			data = new DataMap();
			data.put("check_Yn", checkYn_list.get(i));
			data.put("prjct_id", param.getString("prjct_id"));
			data.put("process_name", process_name_list.get(i));
			data.put("input_material", input_material_list.get(i));
			data.put("process_param", process_param_list.get(i));
			data.put("output_material", output_material_list.get(i));
			data.put("ss_user_no", param.getString("ss_user_no"));

			commonMybatisDao.insert("pharm.manufacturing.insertMenufacStep02", data);
		}

		//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharm.manufacturing.updateMstStep", param);


	}
	
	// api2 호출
	public DataMap selectStp_01_data(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharm.manufacturing.selectStp_01_data",param);
	}

}
