package egovframework.pharmai.formulation.service;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import egovframework.framework.common.constant.Const;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("molPredictionService")
public class MolPredictionServiceImpl extends EgovAbstractServiceImpl implements MolPredictionService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	public DataMap projectCreate(DataMap param) throws Exception {
		String prjct_id = commonMybatisDao.selectOne("pharmai.formulation.projectCreate");
		param.put("prjct_id", prjct_id);

		return param;
	}

	public DataMap selectformulaStep1(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectOne("pharmai.formulation.selectformulaStep1", param);
	}
	
	public DataMap chemicalDBcheck(DataMap param) throws Exception {
		System.out.println("svcimpl param check : " + param);
		String smiles = commonMybatisDao.selectOne("pharmai.formulation.chemicalDBcheck", param);
		param.put("smiles", smiles);
		
		return param;
	}

	public void insertFormulaStp_01(DataMap param) throws Exception{

		param.put("prjct_id", param.getString("prjct_id"));
		DataMap createPrjYn = commonMybatisDao.selectOne("pharmai.formulation.selectPjtMst", param);
		if(createPrjYn == null) {
			commonMybatisDao.insert("pharmai.formulation.insertPrjMst", param);
		}

	   	List pms = param.getList("pms");
    	List logD   = param.getList("logD");
    	String repalceInputType = "";

    	if(param.getString("inputType").equals("smiles")) {
    		repalceInputType = param.getString("inputType").replace("smiles", "S");
    		param.put("inputType", repalceInputType);
    		param.put("chem_nm", null);
    		param.put("sdf_nm", null);
    		param.put("ntsysFileDocId", null);
    	}else if(param.getString("inputType").equals("chemical")) {
    		repalceInputType = param.getString("inputType").replace("chemical", "N");
    		param.put("inputType", repalceInputType);
    		param.put("smiles", null);
    		param.put("sdf_nm", null);
    		param.put("ntsysFileDocId", null);
    	}else {
    		repalceInputType = param.getString("inputType").replace("sdf", "F");
    		param.put("inputType", repalceInputType);
    		param.put("chem_nm", null);
    		param.put("smiles", null);
    		param.put("ntsysFileDocId", param.getString("ntsysFileDocId"));

    	}

    	commonMybatisDao.insert("pharmai.formulation.insertFormulaStp_01", param);

    	String stpSeq = commonMybatisDao.selectOne("pharmai.formulation.selectLastSeqStp");
    	param.put("stpSeq", stpSeq);

    	if(pms != null) {
//    		param.put("prop_type", Const.PROP_TYPE_LO);
    		param.put("prop_type", Const.PROP_TYPE_MS);
        	param.put("ph1", pms.get(0));
        	param.put("ph2", pms.get(1));
        	param.put("ph3", pms.get(2));
        	param.put("ph4", pms.get(3));
        	param.put("ph5", pms.get(4));
        	param.put("ph6", pms.get(5));
        	param.put("ph7", pms.get(6));
        	param.put("ph8", pms.get(7));
        	param.put("ph9", pms.get(8));
        	param.put("ph10", pms.get(9));
        	commonMybatisDao.insert("pharmai.formulation.insertFormulaProp", param);
    	}

    	if(logD != null) {
//    		param.put("prop_type", Const.PROP_TYPE_MS);
    		param.put("prop_type", Const.PROP_TYPE_LO);
        	param.put("ph1", logD.get(0));
        	param.put("ph2", logD.get(1));
        	param.put("ph3", logD.get(2));
        	param.put("ph4", logD.get(3));
        	param.put("ph5", logD.get(4));
        	param.put("ph6", logD.get(5));
        	param.put("ph7", logD.get(6));
        	param.put("ph8", logD.get(7));
        	param.put("ph9", logD.get(8));
        	param.put("ph10", logD.get(9));
        	commonMybatisDao.insert("pharmai.formulation.insertFormulaProp", param);
    	}
    	param.put("caPerm", param.getStringOrgn("caPerm"));
    	commonMybatisDao.insert("pharmai.formulation.insertFormulaPropDtl", param);

    	List dosage_name = param.getList("dosage_name");
    	List oral = param.getList("oral");
    	List parenteral = param.getList("parenteral");
    	List local = param.getList("local");

    	DataMap data = new DataMap();

    	if(dosage_name != null) {

    		for(int i = 0; i < dosage_name.size(); i++) {
        		data = new DataMap();
            	data.put("stpSeq", stpSeq);
            	data.put("ss_user_no", param.getString("ss_user_no"));
            	data.put("prjct_id", param.getString("prjct_id"));
        		data.put("propertiesNm", dosage_name.get(i));
        		data.put("oral", oral.get(i));
        		data.put("parenteral", parenteral.get(i));
        		data.put("local", local.get(i));

        		commonMybatisDao.insert("pharmai.formulation.insertDosage", data);

        	}
    	}

    	//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharmai.formulation.updateMstStep", param);

	}

	public List selectFormulaProp(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectList("pharmai.formulation.selectFormulaProp", param);
	}

	public DataMap selectFormulaPropDtl(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectOne("pharmai.formulation.selectFormulaPropDtl", param);
	}

	public List selectFormulaDosage(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectList("pharmai.formulation.selectFormulaDosage", param);
	}

	public DataMap selectFileData(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectOne("pharmai.formulation.selectFileData", param);
	}

	@Override
	public String getBase64String(String sdfPath) {
		String encodedString = null;
		byte[] fileContent;
		try {
			fileContent = FileUtils.readFileToByteArray(new File(sdfPath));
			encodedString = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return encodedString;
	}

}
