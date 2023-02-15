package egovframework.admin.userCrtfc.service;

import javax.servlet.http.HttpServletRequest;

import egovframework.framework.common.object.DataMap;

public interface UserCrtfcMgtService {

	/**
	   * <PRE>
	   * 1. MethodName : iPinMainMod
	   * 2. ClassName  : UserCrtfcMgtService
	   * 3. Comment   : IPin 인증창
	   * 4. 작성자    : 김성민
	   * 5. 작성일    : 2021. 5. 17. 오전 10:35:07
	   * </PRE>
	   *
	   * @return DataMap
	   * @param request
	   * @needed_parameter request.returnURL(리턴받을주소값) - String
	   *
	   * @return param
	   * @return param.getString("sReturnURL", 리턴받을 주소값);
	   * @return param.getString("sEncData", 암호화된 데이터);
	   * @return param.getString("sRtnMsg", 처리결과 메세지);
	   * @throws Exception
	   */
	  public DataMap iPinMainMod(HttpServletRequest request, DataMap param) throws Exception;

}
