package egovframework.admin.recsroom.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import egovframework.framework.common.object.DataMap;

	/**
 	* <PRE>
 	* 1. ClassName :
 	* 2. FileName  : RecsroomMgtService.java
 	* 3. Package  : egovframework.admin.recsroom.service
 	* 4. Comment  : 자료실
 	* 5. 작성자   : 김성민
 	* 6. 작성일   : 2021.5.03
 	* </PRE>
 	*/
	public interface RecsroomMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : insertRecsroomMgt
	 * 2. ClassName  : RecsroomMgtService
	 * 3. Comment   : 자료실
	 * 4. 작성자    : 김성민
	 * 5. 작성일    : 2021.5.03
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param fileList
	 *   @param attachFileList
	 *   @throws Exception
	 */
	void insertRecsroomMgt(DataMap param, List fileList, List attachFileList)throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListRecsroomMgt
	 * 2. ClassName  : RecsroomMgtService
	 * 3. Comment   : 자료실 리스트 조회
	 * 4. 작성자    : 김성민
	 * 5. 작성일    : 2021.5.04
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @throws Exception
	 */
	public List selectPageListRecsroomMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotRecsroomMgt
	 * 2. ClassName  : RecsroomMgtService
	 * 3. Comment   : 자료실 총개수 조회
	 * 4. 작성자    : 김성민
	 * 5. 작성일    : 2021.5.04
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @throws Exception
	 */
	int selectTotRecsroomMgt (DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectRecsroomMgt
	 * 2. ClassName  : RecsroomMgtService
	 * 3. Comment   : 자료실 상세조회
	 * 4. 작성자    : 김성민
	 * 5. 작성일    : 2021.5.04
	 * </PRE>
	 *  @return DataMap
	 * 	@param request
	 * 	@param response
	 *  @param param
	 *  @return
	 */
	DataMap selectRecsroomMgt (HttpServletRequest request, HttpServletResponse response, DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteRecsroomMgt
	 * 2. ClassName  : RecsroomMgtService
	 * 3. Comment   : 자료실 삭제
	 * 4. 작성자    : 김성민
	 * 5. 작성일    : 2021.5.06
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteRecsroomMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateRecsroomMgt
	 * 2. ClassName  : RecsroomMgtService
	 * 3. Comment   : 자료실 수정
	 * 4. 작성자    : 김성민
	 * 5. 작성일    : 2021.5.10
	 * </PRE>
	 *   @return void
	 *   @param param, fileList, attachFileList
	 *   @throws Exception
	 */
	public void updateRecsroomMgt(DataMap param, List fileList , List attachFileList) throws Exception;

}
