package egovframework.pharmai.formulation.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("doeService")
public class DoeServiceImpl extends EgovAbstractServiceImpl implements DoeService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	public List selectListFormulaStp_05(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectListFormulaStp_05", param);
	}

	public void insertStep6Graph(DataMap param) throws Exception {
		commonMybatisDao.insert("pharmai.formulation.insertStep6Graph", param);

       //INSERT시 CHANGE
       String step = param.getString("status");
       int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
       param.put("step_cd", "0" + plusCnt);

       commonMybatisDao.update("pharmai.formulation.updateMstStep", param);

	}

	public DataMap selectStep6Graph_preto(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharmai.formulation.selectStep6Graph_preto", param);
	}

	public DataMap selectStep6Graph_contour(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharmai.formulation.selectStep6Graph_contour", param);
	}

	public DataMap selectStep6Graph_response(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharmai.formulation.selectStep6Graph_response", param);
	}

	public DataMap selecStep6Graph(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharmai.formulation.selecStep6Graph", param);
	}

	public void insertStp07Design(DataMap param) throws Exception {

		// 공통 함수로 update 안쓰고 스텝 6 조회 부분에 대한 update로직 따로 구현.
		commonMybatisDao.update("pharmai.formulation.updateStp07Design", param);
		commonMybatisDao.update("pharmai.formulation.updateStp07DesignImg", param);
		commonMybatisDao.update("pharmai.formulation.updateStp07result", param);
		commonMybatisDao.update("pharmai.formulation.updateStp07resultImg", param);

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

			commonMybatisDao.insert("pharmai.formulation.insertStp07Design", data);
		}

		commonMybatisDao.insert("pharmai.formulation.insertStp07DesignImg", param);

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

			commonMybatisDao.insert("pharmai.formulation.insertStp07result", data);
		}

		commonMybatisDao.insert("pharmai.formulation.insertStp07resultImg", param);

	}

	public List selectStep6Design(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectStep6Design", param);
	}

	public DataMap selectStep6Design_img(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharmai.formulation.selectStep6Design_img", param);
	}

	public List selectStp07result(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectStp07result", param);
	}

	public DataMap selectStp07resultImg(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectOne("pharmai.formulation.selectStp07resultImg", param);
	}


	public List selectListFormulaStp_06(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectListFormulaStp_06", param);
	}


}
