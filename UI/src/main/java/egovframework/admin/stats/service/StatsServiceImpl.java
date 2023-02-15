package egovframework.admin.stats.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : StatsServiceImpl.java
 * 3. Package  : egovframework.admin.stats.service
 * 4. Comment  : 통계
 * 5. 작성자   : 박재현
 * 6. 작성일   : 2018. 7. 18. 오후 6:56:31
 * </PRE>
 */
@Service("statsService")
public class StatsServiceImpl extends EgovAbstractServiceImpl implements StatsService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntAdminEventLog
	 * 2. ClassName  : StatsServiceImpl
	 * 3. Comment   : 어드민 이벤트 로그 총 카운트 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오후 6:56:48
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntAdminEventLog(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.stats.selectTotCntAdminEventLog", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListAdminEventLog
	 * 2. ClassName  : StatsServiceImpl
	 * 3. Comment   : 어드민 이벤트 로그 목록 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오후 6:57:16
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListAdminEventLog(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.stats.selectPageListAdminEventLog", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntErrorEventLog
	 * 2. ClassName  : StatsService
	 * 3. Comment   : 에러 이벤트 로그 총 카운트 조회
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 4. 오후 5:10:19
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntErrorEventLog(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.stats.selectTotCntErrorEventLog", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListErrorEventLog
	 * 2. ClassName  : StatsService
	 * 3. Comment   : 에러 이벤트 로그 목록 조회
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 4. 오후 5:10:29
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListErrorEventLog(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.stats.selectPageListErrorEventLog", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListUV
	 * 2. ClassName  : StatsServiceImpl
	 * 3. Comment   : UV 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:03:57
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectListUV(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.stats.selectListUV", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListPV
	 * 2. ClassName  : StatsServiceImpl
	 * 3. Comment   : PV 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:04:04
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectListPV(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.stats.selectListPV", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntSatisfactionStatus
	 * 2. ClassName  : StatsServiceImpl
	 * 3. Comment   : 만족도 현황 갯수
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:04:09
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntSatisfactionStatus(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.stats.selectTotCntSatisfactionStatus", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListSatisfactionStatus
	 * 2. ClassName  : StatsServiceImpl
	 * 3. Comment   : 만족도 현황 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:04:22
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectListSatisfactionStatus(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.stats.selectListSatisfactionStatus", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntSatisfactionAvg
	 * 2. ClassName  : StatsServiceImpl
	 * 3. Comment   : 만족도 평균 갯수
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:04:26
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public int selectTotCntSatisfactionAvg(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.stats.selectTotCntSatisfactionAvg", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListSatisfactionAvg
	 * 2. ClassName  : StatsServiceImpl
	 * 3. Comment   : 만족도 평균 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:04:36
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public List selectListSatisfactionAvg(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.stats.selectListSatisfactionAvg", param);
	}
}
