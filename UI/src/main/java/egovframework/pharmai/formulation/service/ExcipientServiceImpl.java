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

@Service("excipientService")
public class ExcipientServiceImpl extends EgovAbstractServiceImpl implements ExcipientService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	public List selectStp03(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectStp03", param);
	}

	public void insertStep3(DataMap param) throws Exception {

		List excipients = param.getList("excipients");
		List kind = param.getList("kind");
		List use_range_s = param.getList("use_range_s");
		List use_range_e = param.getList("use_range_e");
		List maximum = param.getList("maximum");
		List unit = param.getList("unit");
		List checkYn  = param.getList("checkYn");
		List startRange = param.getList("startRange");
		List endRange = param.getList("endRange");

		DataMap data = new DataMap();

		for(int i = 0; i < excipients.size(); i++) {
    		data = new DataMap();
    		data.put("prjct_id", param.getString("prjct_id"));
    		data.put("ss_user_no", param.getString("ss_user_no"));
    		data.put("excipient", excipients.get(i));
    		data.put("kind", kind.get(i));
    		data.put("maximum", maximum.get(i));
    		data.put("unit", unit.get(i));
    		data.put("use_range_s", use_range_s.get(i));
    		data.put("use_range_e", use_range_e.get(i));
    		data.put("checkYn", checkYn.get(i));
    		data.put("ipt_use_range_s", startRange.get(i));
    		data.put("ipt_use_range_e", endRange.get(i));


    		commonMybatisDao.insert("pharmai.formulation.insertFormulaStp03", data);
		}

	   	//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharmai.formulation.updateMstStep", param);

	}

}
