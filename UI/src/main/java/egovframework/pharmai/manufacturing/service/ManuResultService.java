package egovframework.pharmai.manufacturing.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : UserService.java
 * 3. Package  : egovframework.admin.user.web.service
 * 4. Comment  : 사용자 관리
 * 5. 작성자   : 함경호
 * 6. 작성일   : 2015. 9. 1. 오후 3:34:36
 * </PRE>
 */
public interface ManuResultService {

	DataMap selectMenufacStep09_pareto(DataMap param) throws Exception;

	DataMap selectMenufacStep09_contour(DataMap param) throws Exception;

	DataMap selectMenufacStep09_response(DataMap param) throws Exception;

	void insertMenufacStep09(DataMap param) throws Exception;

	List selectStp10DesignList(DataMap param) throws Exception;

	DataMap selectStp10DesignImgData(DataMap param) throws Exception;

	void insertManufactStep10(DataMap param) throws Exception;

	List selectStp10result(DataMap param) throws Exception;

	DataMap selectStp10ResultImg(DataMap param) throws Exception;

	List selectMenufactStep06List(DataMap param) throws Exception;

}
