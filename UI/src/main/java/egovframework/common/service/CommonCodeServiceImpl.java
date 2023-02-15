/**
 *
 *
 * 1. FileName : LoginServiceImpl.java
 * 2. Package : egovframework.framework.security.service
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 23. 오전 10:05:19
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 23. :            : 신규 개발.
 */

package egovframework.common.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : CommonCodeServiceImpl.java
 * 3. Package  : egovframework.common.service
 * 4. Comment  : 코드관리
 * 5. 작성자   : ntsys
 * 6. 작성일   : 2013. 12. 3. 오전 9:49:02
 * </PRE>
 */
@Service("commonCodeService")
public class CommonCodeServiceImpl extends EgovAbstractServiceImpl implements CommonCodeService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	@Resource(name="egovMessageSource")
	private EgovMessageSource egovMessageSource;

	/**
	 * <PRE>
	 * 1. MethodName : codeSelectList
	 * 2. ClassName  : CommonCodeServiceImpl
	 * 3. Comment   : 코드 리스트 조회
	 * 4. 작성자    : ntsys
	 * 5. 작성일    : 2013. 12. 3. 오전 9:55:40
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectCodeList(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("common.selectCodeList", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectGroupCodeList
	 * 2. ClassName  : CommonCodeServiceImpl
	 * 3. Comment   : 그룹 코드 리스트 조회
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 17. 오전 9:57:58
	 * </PRE>
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	public List selectGroupCodeList(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("common.selectGroupCodeList", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectCode
	 * 2. ClassName  : CommonCodeServiceImpl
	 * 3. Comment   : 코드 조회
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2019. 1. 21. 오후 4:03:05
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectCode(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("common.selectCode", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertErrorLog
	 * 2. ClassName  : CommonCodeServiceImpl
	 * 3. Comment   : 에러 로그 삽입
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 5. 오전 11:03:05
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public void insertErrorLog(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.insert("common.insertErrorLog", param);
	}
}
