/**
 *
 * 1. FileName : WebCommonServiceImpl.java
 * 2. Package : egovframework.common.service
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
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : WebCommonServiceImpl.java
 * 3. Package  : egovframework.common.service
 * 4. Comment  :
 * 5. 작성자   : jjh
 * 6. 작성일   : 2014. 1. 6. 오전 9:14:20
 * </PRE>
 */
@Service("webCommonService")
public class WebCommonServiceImpl extends EgovAbstractServiceImpl implements WebCommonService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;


	/**
	 * <PRE>
	 * 1. MethodName : selectBanner
	 * 2. ClassName  : WebCommonServiceImpl
	 * 3. Comment   : 배너 데이터 가져오기
	 * 4. 작성자    : jjh
	 * 5. 작성일    : 2014. 1. 22. 오후 8:09:47
	 * </PRE>
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectBanner(DataMap dataMap) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("common.selectBanner", dataMap);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectBannerList
	 * 2. ClassName  : WebCommonServiceImpl
	 * 3. Comment   : 배너 리스트
	 * 4. 작성자    : jjh
	 * 5. 작성일    : 2014. 1. 23. 오후 8:54:38
	 * </PRE>
	 *   @param dataMap
	 *   @return
	 *   @throws Exception
	 */
	public List selectBannerList(DataMap dataMap) throws Exception {
		return (List) commonMybatisDao.selectList("common.selectBannerList", dataMap);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateThemaOption
	 * 2. ClassName  : WebCommonServiceImpl
	 * 3. Comment   : 사용자 테마 설정 수정
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2020. 5. 20. 오후 5:53:58
	 * </PRE>
	 *   @param dataMap
	 *   @throws Exception
	 */
	public void updateThemaOption(DataMap dataMap) throws Exception {
		commonMybatisDao.update("common.updateThemaOption", dataMap);
	}
}
