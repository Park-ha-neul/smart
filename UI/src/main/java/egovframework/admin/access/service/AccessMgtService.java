package egovframework.admin.access.service;

import java.util.List;

import org.springframework.ui.ModelMap;

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
public interface AccessMgtService {

	List selectPageListAccess(ModelMap model, DataMap param) throws Exception;
	
	String selectAccessExistYn(DataMap param) throws Exception;

	void insertAccess(DataMap param) throws Exception;
	
	DataMap selectAccess(DataMap param) throws Exception;
	
	void updateAccess(DataMap param) throws Exception;
	
	void deleteAccess(DataMap param) throws Exception;
}
