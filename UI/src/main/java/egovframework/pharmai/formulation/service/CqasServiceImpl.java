package egovframework.pharmai.formulation.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import egovframework.framework.common.constant.Const;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("cqasService")
public class CqasServiceImpl extends EgovAbstractServiceImpl implements CqasService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	public List selectListFormulaStp_04(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectListFormulaStp_04", param);
	}

	public void insertFormulaStp_04(DataMap param) throws Exception{

		List cqasName = param.getList("cqas_name");
		List cqasList = param.getList("cqas_list");

		for(int i = 0; i< cqasName.size(); i++) {
			param.put("cqa_nm", cqasName.get(i));

			for(int j = 0; j< cqasList.size(); j++) {
				if(cqasName.get(i).equals(cqasList.get(j))) {
					param.put("check_yn", "Y");
					break;
				}else {
					param.put("check_yn", "N");
				}
			}

			commonMybatisDao.insert("pharmai.formulation.insertFormulaStp_04", param);
		}

	   	//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharmai.formulation.updateMstStep", param);

	}

	public DataMap selectDosage_Form_type(DataMap param) throws Exception{
		return commonMybatisDao.selectOne("pharmai.formulation.selectDosage_Form_type", param);
	}

}
