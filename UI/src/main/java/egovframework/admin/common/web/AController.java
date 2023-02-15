/**
 *
 *
 * 1. FileName : LoginController.java
 * 2. Package : egovframework.framework.security
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 23. 오전 9:55:47
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 23. :            : 신규 개발.
 */

package egovframework.admin.common.web;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.RequestUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  :
 * 3. Package  : egovframework.front.common.web
 * 4. Comment  : REST API 개발시 참조하면됨
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 23. 오전 9:55:47
 * </PRE>
 */
@RestController
public class AController {

	private static Log log = LogFactory.getLog(AController.class);

	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@RequestMapping(value="/test.do", method={RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> mailTest(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		DataMap param = RequestUtil.getDataMap(request);

		CharsetDetector detector;
		CharsetMatch match;
		detector = new CharsetDetector();

		detector.setText(param.getString("msg").getBytes());
		match = detector.detect();

		log.info(" encoding is \"" + match.getName() + "\"");

		String msg = URLDecoder.decode(param.getString("msg"), "EUC-KR");

		log.debug("msg : " +  msg);
		log.debug("msg2 : " +  param.getString("msg"));

//		String value = request.getHeader("Authorization");
//		try {
//
//			//
//			List<Map<String, Object>> chargerList = new ArrayList();
//			Map info = new HashMap();
//			info.put("email", "candler1202@naver.com");
//			info.put("nm", "조정현");
//
//			chargerList.add(info);
//
//			// 성공셋팅
//			returnMap.put("resultCode", "success");
////			returnMap.put(HecateConstant.PARAM_RESULT_DATA, data);
//		} catch (Exception e) {
////			logger.error("selectTrnscHashInfo {}", e);
//			// 에러코드 셋팅
////			returnMap.put(HecateConstant.PARAM_RESULT_CODE, HecateConstant.PARAM_EXCEPTION_ERROR);
////			returnMap.put(HecateConstant.PARAM_RESULT_MESSAGE, util.getMessage(null, HecateConstant.DEFAULT_SERVER_ERROR, null));
////			return new ResponseEntity(returnMap, HttpStatus.INTERNAL_SERVER_ERROR);
//		}

//		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return ResponseEntity.ok(returnMap);
	}

}
