package egovframework.task;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

public class DeleteAdminEventLogTask extends EgovAbstractServiceImpl{

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	private static Log log = LogFactory.getLog(DeleteAdminEventLogTask.class);

	/**
	 *
	 * <PRE>
	 * 1. MethodName : schedule
	 * 2. ClassName  : DeleteAdminEventLogServiceImpl
	 * 3. Comment   : 로그 삭제 task
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 8. 오후 2:54:25
	 * </PRE>
	 *   @return void
	 *   @throws IOException
	 */
	@Scheduled(cron = "0 15 0 * * *") // 매일 0시 15분 실행
//	@Scheduled(fixedDelay = 1000 * 10) // 10초 간격 테스트
	public void schedule() throws IOException {

		if (Globals.TASK_OPERATION_YN.equals("Y")) {

			SimpleDateFormat nowTime = null;

			nowTime = new SimpleDateFormat("hh:mm:ss a");
			log.debug(nowTime + "########## START SPRING TASK!!!!!!!!!!!! ##################");

			// 로그 보관 기간 조회
			DataMap resultMap = commonMybatisDao.selectOne("task.selectLogStrePd");

			DataMap param = new DataMap();
			param.put("log_stre_pd", resultMap.getString("LOG_STRE_PD"));

			// 로그 삭제
			commonMybatisDao.delete("task.deleteOldAdminEventLog", param);
			commonMybatisDao.delete("task.deleteOldErrorEventLog", param);

			nowTime = new SimpleDateFormat("hh:mm:ss a");
			log.debug(nowTime + "########## END SPRING TASK!!!!!!!!!!!! ##################");
		}
	}
}
