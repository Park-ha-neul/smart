package egovframework.pharmai.manufacturing.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import egovframework.admin.common.vo.UserInfoVo;
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
 * 2. FileName  : UserServiceImpl.java
 * 3. Package  : egovframework.admin.user.web.service
 * 4. Comment  : 사용자 관리
 * 5. 작성자   : 함경호
 * 6. 작성일   : 2015. 9. 1. 오후 3:34:30
 * </PRE>
 */
@Service("manuCQAsService")
public class ManuCQAsServiceImpl extends EgovAbstractServiceImpl implements ManuCQAsService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	/** 암호화서비스 */
	@Resource(name = "egovEnvCryptoService")
	EgovEnvCryptoService cryptoService;

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


	public List selectMenufacStep07(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List)commonMybatisDao.selectList("pharm.manufacturing.selectMenufacStep07", param);
	}



}
