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
 * 2. FileName  : BioUnit_ImgServiceImpl.java
 * 3. Package  : egovframework.bio.manufacturing.service
 * 4. Comment  : 유닛공정이미지
 * 5. 작성자   : hnpark
 * 6. 작성일   : 2022. 4. 29
 * </PRE>
 */
@Service("bioUnit_ImgService")
public class BioUnit_ImgServiceImpl extends EgovAbstractServiceImpl implements BioUnit_ImgService{
	
	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;
	
	// step5 첫 화면 호출
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
	
	// step5 save 기능
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

	}
}
