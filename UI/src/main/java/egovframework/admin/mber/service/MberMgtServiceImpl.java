package egovframework.admin.mber.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import egovframework.framework.common.constant.Const;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.HashUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : UserServiceImpl.java
 * 3. Package  : egovframework.admin.mber.web.service
 * 4. Comment  : 사용자 관리
 * 5. 작성자   : 함경호
 * 6. 작성일   : 2015. 9. 1. 오후 3:34:30
 * </PRE>
 */
/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : MberMgtServiceImpl.java
 * 3. Package  : egovframework.admin.mber.service
 * 4. Comment  :
 * 5. 작성자   : JJH
 * 6. 작성일   : 2019. 1. 16. 오전 9:55:06
 * </PRE>
 */
@Service("mberMgtService")
public class MberMgtServiceImpl extends EgovAbstractServiceImpl implements MberMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListMber
	 * 2. ClassName  : MberMgtServiceImpl
	 * 3. Comment   : 사용자 리스트 조회
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 9:03:43
	 * </PRE>
	 *   @param model
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListMber(ModelMap model, DataMap param) throws Exception {
		List resultList = new ArrayList();
		// 총개수 조회
		int totCnt = (Integer) commonMybatisDao.selectOne("admin.mber.selectTotCntMber", param);

		// 개수가 없는경우는 리스트 조회 하지 않는다.
		if(totCnt > 0){
			param.put("totalCount", totCnt);
			// param 객체에 페이지 관련 정보를 담는다.
			param = pageNavigationUtil.createNavigationInfo(model, param);
			resultList = (List) commonMybatisDao.selectList("admin.mber.selectPageListMber", param);
		}
		return resultList;
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectMberExistYn
	 * 2. ClassName  : MberMgtServiceImpl
	 * 3. Comment   : 사용자 중복 확인
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 9:31:22
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public String selectMberExistYn(DataMap param) throws Exception {
		return (String) commonMybatisDao.selectOne("admin.mber.selectMberExistYn", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertMber
	 * 2. ClassName  : MberMgtServiceImpl
	 * 3. Comment   : 사용자 등록
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 9:03:51
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void insertMber(DataMap param) throws Exception {

		// 비밀번호 암호화
		param.put("password", HashUtil.getHashCode(param.getString("password")));
		commonMybatisDao.insert("admin.mber.insertMber", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectMber
	 * 2. ClassName  : MberMgtServiceImpl
	 * 3. Comment   : 사용자 조회
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 9:55:10
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectMber(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.mber.selectMber", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateMber
	 * 2. ClassName  : MberMgtServiceImpl
	 * 3. Comment   : 사용자 수정
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 10:54:27
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void updateMber(DataMap param) throws Exception {

		// 비밀번호 암호화
		param.put("password", HashUtil.getHashCode(param.getString("password")));
		commonMybatisDao.update("admin.mber.updateMber", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteMber
	 * 2. ClassName  : MberMgtServiceImpl
	 * 3. Comment   : 사용자 삭제
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 10:57:07
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteMber(DataMap param) throws Exception {
		commonMybatisDao.delete("admin.mber.deleteMber", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertExcelMber
	 * 2. ClassName  : MberMgtServiceImpl
	 * 3. Comment   : 엑셀 사용자 등록
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오후 2:42:12
	 * </PRE>
	 *   @param param
	 *   @param dataList
	 *   @throws Exception
	 */
	public void insertExcelMber(DataMap param, List dataList) throws Exception {
		// 사용자가 존재하는지 확인
		for(int i = 0; i < dataList.size(); i++){
			DataMap item = (DataMap)dataList.get(i);

			// 존재하지 않는경우만 등록
			if(commonMybatisDao.selectOne("admin.mber.selectMberExistYn", item).equals("N")){

				// 등록자와 회원상태코드를 추가한다.
				item.put("ss_user_no", param.getString("ss_user_no"));
				item.put("user_sttus_code", Const.USER_STTUS_CODE_ACTIVE);

				// 핸드폰 번호 띄어쓰기, - 변경
				item.put("moblphon_no", item.getString("moblphon_no").replace("-", "").replace(" ", ""));

				commonMybatisDao.insert("admin.mber.insertMber", item);
			}
		}

	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteListMber
	 * 2. ClassName  : MberMgtServiceImpl
	 * 3. Comment   : 사용자 리스트 삭제
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 3. 18. 오후 2:41:03
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteListMber(DataMap param) throws Exception {

		List delList = param.getList("del_no");
		for(int i = 0; i < delList.size(); i++){
			DataMap p = new DataMap();
			p.put("user_no", delList.get(i));

			commonMybatisDao.update("admin.mber.deleteMber", p);
		}
	}
}
