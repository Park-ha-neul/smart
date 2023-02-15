package egovframework.admin.logstrepd.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : LogStreMgtServiceImpl.java
 * 3. Package  : egovframework.admin.logstrepd.service
 * 4. Comment  : 로그 보관 기간 설정
 * 5. 작성자   : 윤영수
 * 6. 작성일   : 2020. 05. 07. 오전 10:00
 * </PRE>
 */
@Service("logStreMgtService")
public class LogStreMgtServiceImpl extends EgovAbstractServiceImpl implements LogStreMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	/**
	 * <PRE>
	 * 1. MethodName : selectLogStrePd
	 * 2. ClassName  : LogStreMgtServiceImpl
	 * 3. Comment   : 로그 보관 기간 조회
	 * 4. 작성자    : 윤영수
	 * 5. 작성일    : 2020. 05. 07. 오전 10:00
	 * </PRE>
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public DataMap selectLogStrePd() throws Exception{
		// TODO Auto-generated method stub
		DataMap data = commonMybatisDao.selectOne("admin.logstrepd.selectLogStrePd");

		if (data == null) {
			data = new DataMap();
			data.put("LOG_STRE_PD", "0");
		}

		return data;
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateLogStreMgt
	 * 2. ClassName  : LogStreMgtServiceImpl
	 * 3. Comment   : 로그 보관 기간 갱신
	 * 4. 작성자    : 윤영수
	 * 5. 작성일    : 2020. 05. 07. 오전 10:00
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	@Override
	public void updateLogStreMgt(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("admin.logstrepd.deleteLogStreMgt", param);
		commonMybatisDao.update("admin.logstrepd.insertLogStreMgt", param);
	}
}
