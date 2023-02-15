package egovframework.pharmai.manufacturing.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import egovframework.admin.common.vo.UserInfoVo;
import egovframework.framework.common.constant.Const;
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
 * 2. FileName  : Manufacturing_Step1ServiceImpl.java
 * 3. Package  : egovframework.pharmai.manufacturing.service
 * 4. Comment  : 제형 선택
 * 5. 작성자   : 김성민
 * 6. 작성일   : 2021. 8. 10. 오전 10:56:36
 * </PRE>
 */
@Service("methodService")
public class MethodServiceImpl extends EgovAbstractServiceImpl implements MethodService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	public DataMap projectCreate(DataMap param) throws Exception {
		String prjct_id = commonMybatisDao.selectOne("pharm.manufacturing.projectCreate");
		param.put("prjct_id", prjct_id);

		return param;
	}

	public void insertPrjMst(DataMap param) throws Exception {
		commonMybatisDao.insert("pharm.manufacturing.insertPrjMst",param);
	}

	public void insertManufacturStp_01(DataMap param) throws Exception {

		List st = param.getList("nameList");

		DataMap data = null;
		for(int i = 0; i < st.size(); i++) {
			data = new DataMap();
			data.put("prjct_id", param.getString("prjct_id"));
			data.put("routes_type", param.getString("routes_type"));
			data.put("dosage_form_type", param.getString("dosage_form_type"));

			//체크 박스 선택된 값과 api method 항목과 비교 후 use_yn -> Y
			if(param.getString("manufacturing_method").equals(st.get(i))) {
				data.put("manufacturing_method", st.get(i));
				data.put("check_yn", "Y");
			}else {
				data.put("manufacturing_method", st.get(i));
				data.put("check_yn", "N");
			}
			data.put("ss_user_no", param.getString("ss_user_no"));
			commonMybatisDao.insert("pharm.manufacturing.insertManufacturStp_01",data);
		}

		//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharm.manufacturing.updateMstStep", param);
	}

	public void projectNmUpdate(DataMap param) throws Exception {
		commonMybatisDao.update("pharm.manufacturing.projectNmUpdate", param);
	}

	public List selectManufacturingStep1(DataMap param) throws Exception {
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectManufacturingStep1", param);
	}

	public DataMap selectFormulationProp_dtl(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharm.manufacturing.selectFormulationProp_dtl", param);
	}

	public List selectFormulationProp(DataMap param) throws Exception {
		return commonMybatisDao.selectList("pharm.manufacturing.selectFormulationProp", param);
	}

	public DataMap selectFormulationStp2_dtl(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharm.manufacturing.selectFormulationStp2_dtl", param);
	}

	public List selectFormulationStp4_dtl(DataMap param) throws Exception {
		return commonMybatisDao.selectList("pharm.manufacturing.selectFormulationStp4_dtl", param);
	}

	public void insertCopyFormulationProp(DataMap param) throws Exception {
		List prop = param.getList("prop");

		DataMap temp = null;
		for (int i = 0; i < prop.size(); i++) {
			temp = new DataMap();
			DataMap data= new DataMap();
			data = (DataMap) prop.get(i);
			temp.put("formulation_pjt_id", param.getString("formulation_pjt_id"));
			temp.put("prjct_id", param.getString("prjct_id"));
			temp.put("prop_type", data.getString("PROP_TYPE"));
			temp.put("ph1", data.getString("PH1"));
			temp.put("ph2", data.getString("PH2"));
			temp.put("ph3", data.getString("PH3"));
			temp.put("ph4", data.getString("PH4"));
			temp.put("ph5", data.getString("PH5"));
			temp.put("ph6", data.getString("PH6"));
			temp.put("ph7", data.getString("PH7"));
			temp.put("ph8", data.getString("PH8"));
			temp.put("ph9", data.getString("PH9"));
			temp.put("ph10", data.getString("PH10"));
			temp.put("ss_user_no", param.getString("ss_user_no"));

			commonMybatisDao.insert("pharm.manufacturing.insertCopyFormulationProp", temp);
		}

	}

	public void insertCopyFormulationProp_dtl(DataMap prop_dtl) throws Exception {
		DataMap temp = new DataMap();

		temp.put("prjct_id", prop_dtl.getString("prjct_id"));
		temp.put("formulation_pjt_id", prop_dtl.getString("formulation_pjt_id"));
		temp.put("pka", prop_dtl.getString("PKA"));
		temp.put("pkb", prop_dtl.getString("PKB"));
		temp.put("ca_perm", prop_dtl.getString("CA_PERM"));
		temp.put("bol_point", prop_dtl.getString("BOL_POINT"));
		temp.put("logp", prop_dtl.getString("LOGP"));
		temp.put("bio", prop_dtl.getString("BIO"));
		temp.put("dosage_form", prop_dtl.getString("DOSAGE_FORM"));
		temp.put("mol_wei", prop_dtl.getString("MOL_WEI"));
		temp.put("lip_rule", prop_dtl.getString("LIP_RULE"));
		temp.put("ss_user_no", prop_dtl.getString("ss_user_no"));
		commonMybatisDao.insert("pharm.manufacturing.insertCopyFormulationProp_dtl", temp);
	}

	public void insertCopyFormulationStp2_dtl(DataMap stp2_dtl) throws Exception {

		DataMap temp = new DataMap();

		temp.put("prjct_id", stp2_dtl.getString("prjct_id"));
		temp.put("formulation_pjt_id", stp2_dtl.getString("formulation_pjt_id"));
		temp.put("routes_type", stp2_dtl.getString("ROUTES_TYPE"));
		temp.put("dosage_form_type", stp2_dtl.getString("DOSAGE_FORM_TYPE"));
		temp.put("volume", stp2_dtl.getString("VOLUME"));
		temp.put("ss_user_no", stp2_dtl.getString("ss_user_no"));

		commonMybatisDao.insert("pharm.manufacturing.insertCopyFormulationStp2_dtl", temp);
	}

	public void insertCopyFormulationStp4_dtl(DataMap param) throws Exception {
		List stp4_dtl = param.getList("stp4_dtl");

		DataMap temp = null;
		for (int i = 0; i < stp4_dtl.size(); i++) {
			temp = new DataMap();
			DataMap data= new DataMap();
			data = (DataMap) stp4_dtl.get(i);
			temp.put("formulation_pjt_id", param.getString("formulation_pjt_id"));
			temp.put("prjct_id", param.getString("prjct_id"));
			temp.put("check_yn", data.getString("CHECK_YN"));
			temp.put("cqa_nm", data.getString("CQA_NM"));
			temp.put("ss_user_no", param.getString("ss_user_no"));

			commonMybatisDao.insert("pharm.manufacturing.insertCopyFormulationStp4_dtl", temp);
		}

	}

	public List selectCopy_Prop(DataMap param) throws Exception {
		return commonMybatisDao.selectList("pharm.manufacturing.selectCopy_Prop", param);
	}

	public DataMap selectCopy_Prop_dtl(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharm.manufacturing.selectCopy_Prop_dtl", param);
	}

	public DataMap selectCopy_Stp2_dtl(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("pharm.manufacturing.selectCopy_Stp2_dtl", param);
	}

	public List selectCopy_Stp4_cqa(DataMap param) throws Exception {
		return commonMybatisDao.selectList("pharm.manufacturing.selectCopy_Stp4_cqa", param);
	}

}
