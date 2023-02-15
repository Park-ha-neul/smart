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
public interface CPP_FactorService {


	void insertMenufactStep06(DataMap param) throws Exception;

	List selectMenufactStep06(DataMap param) throws Exception;

	DataMap selectDistinctUnitStp05(DataMap param) throws Exception;



}
