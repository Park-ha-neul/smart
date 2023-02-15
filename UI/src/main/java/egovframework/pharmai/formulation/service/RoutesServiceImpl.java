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

@Service("routesService")
public class RoutesServiceImpl extends EgovAbstractServiceImpl implements RoutesService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

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

	public DataMap selectDataStep2(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("pharmai.formulation.selectDataStep2", param);
	}

	public DataMap selectDosageFormCnt(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("pharmai.formulation.selectDosageFormCnt", param);
	}

	public List selectListExcipient(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectListExcipient", param);
	}

	public void insertStep2(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		 commonMybatisDao.insert("pharmai.formulation.insertStep2", param);
	}

}
