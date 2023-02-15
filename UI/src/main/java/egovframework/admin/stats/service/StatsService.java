package egovframework.admin.stats.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;


/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : StatsService.java
 * 3. Package  : egovframework.admin.stats.service
 * 4. Comment  : 통계
 * 5. 작성자   : 박재현
 * 6. 작성일   : 2018. 7. 18. 오후 6:56:42
 * </PRE>
 */
public interface StatsService {


	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntAdminEventLog
	 * 2. ClassName  : StatsService
	 * 3. Comment   : 어드민 이벤트 로그 총 카운트 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오후 6:57:13
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntAdminEventLog(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : selectPageListAdminEventLog
	 * 2. ClassName  : StatsService
	 * 3. Comment   : 어드민 이벤트 로그 목록 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오후 6:57:29
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListAdminEventLog(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntErrorEventLog
	 * 2. ClassName  : StatsService
	 * 3. Comment   : 에러 이벤트 로그 총 카운트 조회
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 4. 오후 5:10:19
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntErrorEventLog(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : selectPageListErrorEventLog
	 * 2. ClassName  : StatsService
	 * 3. Comment   : 에러 이벤트 로그 목록 조회
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 4. 오후 5:10:29
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListErrorEventLog(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListUV
	 * 2. ClassName  : StatsService
	 * 3. Comment   : UV 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:02:19
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListUV(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListPV
	 * 2. ClassName  : StatsService
	 * 3. Comment   : PV 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:02:30
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListPV(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntSatisfactionStatus
	 * 2. ClassName  : StatsService
	 * 3. Comment   : 만족도 현황 갯수
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:02:35
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntSatisfactionStatus(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListSatisfactionStatus
	 * 2. ClassName  : StatsService
	 * 3. Comment   : 만족도 현황 목록
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:02:45
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListSatisfactionStatus(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntSatisfactionAvg
	 * 2. ClassName  : StatsService
	 * 3. Comment   : 만족도 평균 갯수
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:03:08
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntSatisfactionAvg(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectListSatisfactionAvg
	 * 2. ClassName  : StatsService
	 * 3. Comment   : 만족도 평균 리스트
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 3. 오전 10:03:13
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListSatisfactionAvg(DataMap param) throws Exception;

}
