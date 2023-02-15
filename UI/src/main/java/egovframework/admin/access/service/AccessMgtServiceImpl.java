package egovframework.admin.access.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : AccessMgtServiceImpl.java
 * 3. Package  : egovframework.admin.access.service
 * 4. Comment  :
 * 5. 작성자   : JJH
 * 6. 작성일   : 2019. 1. 16. 오전 9:55:06
 * </PRE>
 */
@Service("accessMgtService")
public class AccessMgtServiceImpl extends EgovAbstractServiceImpl implements AccessMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListAccess
	 * 2. ClassName  : AccessMgtServiceImpl
	 * 3. Comment   : 사용자 리스트 조회
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 9:03:43
	 * </PRE>
	 *   @param model
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListAccess(ModelMap model, DataMap param) throws Exception {
		List resultList = new ArrayList();
		// 총개수 조회
		int totCnt = (Integer) commonMybatisDao.selectOne("admin.access.selectTotCntAccess", param);

		// 개수가 없는경우는 리스트 조회 하지 않는다.
		if(totCnt > 0){
			param.put("totalCount", totCnt);
			// param 객체에 페이지 관련 정보를 담는다.
			param = pageNavigationUtil.createNavigationInfo(model, param);
			resultList = (List) commonMybatisDao.selectList("admin.access.selectPageListAccess", param);
		}
		return resultList;
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectAccessExistYn
	 * 2. ClassName  : AccessMgtServiceImpl
	 * 3. Comment   : 사용자 중복 확인
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 9:31:22
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public String selectAccessExistYn(DataMap param) throws Exception {
		return (String) commonMybatisDao.selectOne("admin.access.selectAccessExistYn", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertAccess
	 * 2. ClassName  : AccessMgtServiceImpl
	 * 3. Comment   : 사용자 등록
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 9:03:51
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void insertAccess(DataMap param) throws Exception {
		commonMybatisDao.insert("admin.access.insertAccess", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectAccess
	 * 2. ClassName  : AccessMgtServiceImpl
	 * 3. Comment   : 사용자 조회
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 9:55:10
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectAccess(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.access.selectAccess", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateAccess
	 * 2. ClassName  : AccessMgtServiceImpl
	 * 3. Comment   : 사용자 수정
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 10:54:27
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void updateAccess(DataMap param) throws Exception {
		commonMybatisDao.update("admin.access.updateAccess", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteAccess
	 * 2. ClassName  : AccessMgtServiceImpl
	 * 3. Comment   : 사용자 삭제
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 16. 오전 10:57:07
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteAccess(DataMap param) throws Exception {
		commonMybatisDao.delete("admin.access.deleteAccess", param);
	}
}
