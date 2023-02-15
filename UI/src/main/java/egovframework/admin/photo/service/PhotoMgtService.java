package egovframework.admin.photo.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : PhotoMgtService.java
 * 3. Package  : egovframework.admin.photoMgt.service
 * 4. Comment  : 사진 관리
 * 5. 작성자   : 박재현
 * 6. 작성일   : 2015. 9. 7. 오전 9:51:50
 * </PRE>
 */ 
public interface PhotoMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntPhotoMgt
	 * 2. ClassName  : PhotoMgtService
	 * 3. Comment   : 사진 총개수 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 9:53:11
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntPhotoMgt(DataMap param) throws Exception;
	
	

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListPhotoMgt
	 * 2. ClassName  : PhotoMgtService
	 * 3. Comment   : 사진 페이지 리스트 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 9:53:13
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListPhotoMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPhotoMgt
	 * 2. ClassName  : PhotoMgtService
	 * 3. Comment   : 사진 상세조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 10:14:36
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 */
	DataMap selectPhotoGroup(DataMap param) throws Exception;
	
	
	
	/**
	 * <PRE>
	 * 1. MethodName : selectListPhotoFiles
	 * 2. ClassName  : PhotoMgtService
	 * 3. Comment   : 사진목록 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오전 8:49:58
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectListPhotoFiles(DataMap param) throws Exception;

	
	/**
	 * <PRE>
	 * 1. MethodName : deletePhotoMgt
	 * 2. ClassName  : PhotoMgtService
	 * 3. Comment   : 사진 삭제
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 11:33:07
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deletePhotoMgt(DataMap param)throws Exception;
	
	
	
	/**
	 * <PRE>
	 * 1. MethodName : insertPhotoMgt
	 * 2. ClassName  : PhotoMgtService
	 * 3. Comment   : 사진 등록
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 11. 오후 4:59:05
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void insertPhotoMgt(DataMap param, List fileList)throws Exception;
	
	
	
	/**
	 * <PRE>
	 * 1. MethodName : updatePhotoMgt
	 * 2. ClassName  : PhotoMgtService
	 * 3. Comment   : 사진 수정
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 11. 오후 4:59:25
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updatePhotoMgt(DataMap param, List fileList)throws Exception;
	
	
	/**
	 * <PRE>
	 * 1. MethodName : insertPhotoImage
	 * 2. ClassName  : PhotoMgtService
	 * 3. Comment   : 사진 업로드 등록용
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오전 8:51:33
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param fileList
	 *   @throws Exception
	 */
	void insertPhotoImage(DataMap param, List fileList)throws Exception;
	
	
	/**
	 * <PRE>
	 * 1. MethodName : updatePhotoImage
	 * 2. ClassName  : PhotoMgtService
	 * 3. Comment   : 사진 업로드 수정용
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오전 8:51:59
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param fileList
	 *   @throws Exception
	 */
	void updatePhotoImage(DataMap param, List fileList)throws Exception;
	

}
