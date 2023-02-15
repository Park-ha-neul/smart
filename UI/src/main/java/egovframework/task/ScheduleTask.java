package egovframework.task;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.util.DateUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

public class ScheduleTask extends EgovAbstractServiceImpl{

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	private static Log log = LogFactory.getLog(ScheduleTask.class);

	@Scheduled(fixedDelay=1000 * 10) // 10초
	public void scheduleTest(){

		log.debug(DateUtil.getToday("yyyy.MM.dd hh:mm:ss") + "########## [scheduleTest] START SPRING TASK!!!!!!!!!!!! ##################");
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("scheduleTest!!!");
		log.debug(DateUtil.getToday("yyyy.MM.dd hh:mm:ss") + "########## [scheduleTest] END SPRING TASK!!!!!!!!!!!! ##################");
	}

	@Scheduled(fixedDelay=1000 * 10) // 10초
	public void scheduleTest2(){

		log.debug(DateUtil.getToday("yyyy.MM.dd hh:mm:ss") + "########## [scheduleTest2] START SPRING TASK!!!!!!!!!!!! ##################");
		log.debug("scheduleTest2!!!");
		log.debug(DateUtil.getToday("yyyy.MM.dd hh:mm:ss") + "########## [scheduleTest2] END SPRING TASK!!!!!!!!!!!! ##################");
	}
}
