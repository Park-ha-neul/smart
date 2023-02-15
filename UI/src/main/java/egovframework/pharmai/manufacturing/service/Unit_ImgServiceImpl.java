package egovframework.pharmai.manufacturing.service;

import java.util.List;

import javax.annotation.Resource;
import javax.mail.search.IntegerComparisonTerm;
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
 * 2. FileName  : Unit_ImgServiceImpl.java
 * 3. Package  : egovframework.pharmai.manufacturing.service
 * 4. Comment  : 제형 선택
 * 5. 작성자   : 김성민
 * 6. 작성일   : 2021. 8. 10. 오전 10:56:36
 * </PRE>
 */
@Service("unit_ImgService")
public class Unit_ImgServiceImpl extends EgovAbstractServiceImpl implements Unit_ImgService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	public void insertUnit_Img(DataMap param) throws Exception{

		List check_yn = param.getList("check_yn");
		List unit_nm = param.getList("unit");
		List img_src = param.getList("img_src");
		List cpp_type = param.getList("cpp");
		List cpp_length = param.getList("cpp_length");
		List risk_length = param.getList("risk_length");
		List risk_dtl = param.getList("risk_dtl");

		DataMap data = null;
		int temp = 0;
		int temp2 = 0;
		for(int i = 0; i< unit_nm.size(); i++) {
			data = new DataMap();
			data.put("checkYn", check_yn.get(i));
			data.put("unit", unit_nm.get(i));
			data.put("prjct_id", param.getString("prjct_id"));
			data.put("ss_user_no", param.getString("ss_user_no"));
			data.put("img_src", img_src.get(i));
			for(int j = 0 + temp2; j < cpp_type.size(); j++) {
				if(Integer.parseInt((String) cpp_length.get(i)) + temp2  == j) {
					temp2 += Integer.parseInt((String) cpp_length.get(i));
					break;
				}
				data.put("cpp_type", cpp_type.get(j));
				for(int z = 0 + temp; z < risk_dtl.size(); z++ ) {
					if(Integer.parseInt((String) risk_length.get(j)) + temp == z) {
						temp += Integer.parseInt((String) risk_length.get(j));
						break;
					}else {
						data.put("risk_dtl", risk_dtl.get(z));
						commonMybatisDao.insert("pharm.manufacturing.insertUnit_Img", data);
					}

				}
			}
		}




		//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharm.manufacturing.updateMstStep", param);

	}


	public List selectStp05List(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List) commonMybatisDao.selectList("pharm.manufacturing.selectStp05List", param);
	}


	public List selectDistinctUnitList(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List) commonMybatisDao.selectList("pharm.manufacturing.selectDistinctUnitList", param);
	}


	public List selectDistinctCppList(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List) commonMybatisDao.selectList("pharm.manufacturing.selectDistinctCppList", param);
	}


}
