package egovframework.pharmai.manufacturing.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : Step2Service.java
 * 3. Package  : egovframework.admin.user.web.service
 * 4. Comment  :
 * 5. 작성자   : 김성민
 * 6. 작성일   : 2021. 8. 10. 오후 4:03:36
 * </PRE>
 */
public interface Process_MapService {

	void insertMenufacStep02(DataMap param) throws Exception;

	List selectMenufacStep02(DataMap param) throws Exception;

	DataMap selectStp_01_data(DataMap param) throws Exception;
}
