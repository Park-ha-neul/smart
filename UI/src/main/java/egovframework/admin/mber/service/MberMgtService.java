package egovframework.admin.mber.service;

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
public interface MberMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListMber
	 * 2. ClassName  : MberMgtService
	 * 3. Comment   : 사용자 리스트 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 19. 오후 6:10:59
	 * </PRE>
	 *   @return List
	 *   @param model
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListMber(ModelMap model, DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectMberExistYn
	 * 2. ClassName  : MberMgtService
	 * 3. Comment   : 사용자 중복 확인
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 19. 오후 6:11:30
	 * </PRE>
	 *   @return String
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	String selectMberExistYn(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertMber
	 * 2. ClassName  : MberMgtService
	 * 3. Comment   : 사용자 등록
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 19. 오후 6:12:05
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void insertMber(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectMber
	 * 2. ClassName  : MberMgtService
	 * 3. Comment   : 사용자 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 19. 오후 6:12:21
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectMber(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateMber
	 * 2. ClassName  : MberMgtService
	 * 3. Comment   : 사용자 수정
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 19. 오후 6:12:31
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateMber(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteMber
	 * 2. ClassName  : MberMgtService
	 * 3. Comment   : 사용자 삭제
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 19. 오후 6:12:42
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteMber(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertExcelMber
	 * 2. ClassName  : MberMgtService
	 * 3. Comment   : 엑셀 사용자 등록
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 19. 오후 6:12:51
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param dataList
	 *   @throws Exception
	 */
	void insertExcelMber(DataMap param, List dataList) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteListMber
	 * 2. ClassName  : MberMgtService
	 * 3. Comment   : 사용자 리스트 삭제
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 19. 오후 6:13:07
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteListMber(DataMap param) throws Exception;
}
