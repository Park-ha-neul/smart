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
 * 2. FileName  : UserService.java
 * 3. Package  : egovframework.bio.manufacturing.web
 * 4. Comment  : step8
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 4. 29
 * </PRE>
 */
@Service("bioManuResultService")
public class BioManuResultServiceImpl extends EgovAbstractServiceImpl implements BioManuResultService{
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	// step8 첫 화면 호출
	public DataMap selectMenufacStep09_pareto(DataMap param) {
		return commonMybatisDao.selectOne("pharm.manufacturing.selectMenufacStep09_pareto",param);
	}

	public DataMap selectMenufacStep09_response(DataMap param) {
		return commonMybatisDao.selectOne("pharm.manufacturing.selectMenufacStep09_response",param);
	}

	public DataMap selectMenufacStep09_contour(DataMap param) {
		return commonMybatisDao.selectOne("pharm.manufacturing.selectMenufacStep09_contour",param);
	}
	
	public List selectStp10DesignList(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectList("pharm.manufacturing.selectStp10DesignList", param);
	}

	public DataMap selectStp10DesignImgData(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectOne("pharm.manufacturing.selectStp10DesignImg", param);
	}
	
	public List selectStp10result(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectStp10result", param);
	}

	public DataMap selectStp10ResultImg(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectOne("pharm.manufacturing.selectStp10ResultImg", param);
	}

	public List selectMenufactStep06List(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectMenufactStep06List", param);
	}

	// api7번 호출
	public void insertMenufacStep09(DataMap param) {
		commonMybatisDao.insert("pharm.manufacturing.insertMenufacStep09",param);

		//INSERT시 CHANGE
       String step = param.getString("status");
       int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
       param.put("step_cd", "0" + plusCnt);

	   commonMybatisDao.update("pharmai.formulation.updateMstStep", param);
	}
	
	// api8번 호출
	public void insertManufactStep10(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("pharm.manufacturing.updateManuStp10Design", param);
		commonMybatisDao.update("pharm.manufacturing.updateManuStp10DesignImg", param);
		commonMybatisDao.update("pharm.manufacturing.updateManuStp10Result", param);
		commonMybatisDao.update("pharm.manufacturing.updateManuStp10ResultImg", param);

		List y_variable_list = param.getList("y_variables[]");
		List effects_list = param.getList("effects[]");
		List ipt_start_ys_list = param.getList("ipt_start_ys[]");
		List ipt_end_ys_list = param.getList("ipt_end_ys[]");

		for(int i = 0; i < y_variable_list.size(); i++ ) {
			DataMap data = new DataMap();
			data.put("prjct_id", param.getString("prjct_id"));
			data.put("y_variable", y_variable_list.get(i));
			data.put("effect", effects_list.get(i));
			data.put("ipt_start_y", ipt_start_ys_list.get(i));
			data.put("ipt_end_y", ipt_end_ys_list.get(i));
			data.put("ss_user_no", param.getString("ss_user_no"));

			commonMybatisDao.insert("pharm.manufacturing.insertStp10Design", data);
		}

		commonMybatisDao.insert("pharm.manufacturing.insertStp10DesignImg", param);

		List excipientNameArr = param.getList("excipientNameArr");
		List minArr = param.getList("minArr");
		List maxArr = param.getList("maxArr");

		List controlMinArr = param.getList("controlMinArr");
		List controlMaxArr = param.getList("controlMaxArr");
		List designMinArr = param.getList("designMinArr");
		List designMaxArr = param.getList("designMaxArr");
		List knowledgeMinArr = param.getList("knowledgeMinArr");
		List knowledgeMaxArr = param.getList("knowledgeMaxArr");

		for(int i = 0; i< excipientNameArr.size(); i++ ) {
			DataMap data = new DataMap();
			data.put("prjct_id", param.getString("prjct_id"));
			data.put("excipent_nm", excipientNameArr.get(i));
			data.put("excipent_min", minArr.get(i));
			data.put("excipent_max", maxArr.get(i));
			data.put("control_space_min", controlMinArr.get(i));
			data.put("control_space_max", controlMaxArr.get(i));
			data.put("design_space_min", designMinArr.get(i));
			data.put("design_space_max", designMaxArr.get(i));
			data.put("knowledge_space_min", knowledgeMinArr.get(i));
			data.put("knowledge_space_max", knowledgeMaxArr.get(i));
			data.put("ss_user_no", param.getString("ss_user_no"));

			commonMybatisDao.insert("pharm.manufacturing.insertStp10result", data);
		}

		commonMybatisDao.insert("pharm.manufacturing.insertStp10ResultImg", param);

	}
}
