package egovframework.admin.logstrepd.service;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : LogStreMgtService.java
 * 3. Package  : egovframework.admin.logstrepd.service
 * 4. Comment  : 로그 보관 기간 설정
 * 5. 작성자   : 윤영수
 * 6. 작성일   : 2020. 05. 07. 오전 10:00
 * </PRE>
 */
public interface LogStreMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : selectLogStrePd
	 * 2. ClassName  : LogStreMgtService
	 * 3. Comment   : 로그 보관 기간 조회
	 * 4. 작성자    : 윤영수
	 * 5. 작성일    : 2020. 05. 07. 오전 10:00
	 * </PRE>
	 *   @return DataMap
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectLogStrePd() throws Exception;

	
	/**
	 * <PRE>
	 * 1. MethodName : updateLogStreMgt
	 * 2. ClassName  : LogStreMgtService
	 * 3. Comment   : 로그 보관 기간 갱신
	 * 4. 작성자    : 윤영수
	 * 5. 작성일    : 2020. 05. 07. 오전 10:00
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateLogStreMgt(DataMap param) throws Exception;
	
}
