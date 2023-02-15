package egovframework.admin.cnts.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : CntsMgtService.java
 * 3. Package  : egovframework.admin.cnts.service
 * 4. Comment  : 콘텐츠 관리
 * 5. 작성자   : 백국현
 * 6. 작성일   : 2018.6.26
 * </PRE>
 */
public interface CntsMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntMgt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 총개수 조회
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018.6.26
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListCntsMgt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 페이지 리스트 조회
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018.6.26
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListCntsMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectCntsMgt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 상세조회
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018.6.26
	 * </PRE>
	 * @param request
	 * @param response
	 *   @return DataMap
	 *   @param param
	 *   @return
	 */
	DataMap selectCntsMgt(HttpServletRequest request, HttpServletResponse response, DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : selectCntsMenuMgt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 메뉴 리스트 조회
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 8. 27. 오후 7:04:09
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectCntsMenuMgt(DataMap param) throws Exception;




	/**
	 * <PRE>
	 * 1. MethodName : selectPageListCntsVerMgt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 버전 정보
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 7. 11. 오후 3:57:39
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListCntsVerMgt(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : deleteCntsMgt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 삭제
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018.6.26
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteCntsMgt(DataMap param)throws Exception;



	/**
	 * <PRE>
	 * 1. MethodName : selectOrgan
	 * 2. ClassName  : CntsMgtServiceImpl
	 * 3. Comment   : 조직코드 조회
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 7. 9. 오후 1:54:01
	 * </PRE>
	 *   @throws Exception
	 */
	List selectOrgan()throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertCntsMgt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 등록
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018.6.26
	 * </PRE>
	 *   @return void
	 *   @param param
	 * @param fileList
	 *   @throws Exception
	 */
	void insertCntsMgt(DataMap param, List fileList, List attachFileList)throws Exception;



	/**
	 * <PRE>
	 * 1. MethodName : updateCntsMgt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 수정
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018.6.26
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateCntsMgt(DataMap param, List fileList, List attachList)throws Exception;



	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntPhotoMng
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 포토매니저 리스트 카운트
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 7. 13. 오후 2:19:11
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntPhotoMng(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListPhotoMng
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 포토매니저 리스트 출력
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 7. 13. 오후 1:36:13
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListPhotoMng(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateHitCnt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 히트 카운트 증가
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018.6.26
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void updateHitCnt(DataMap param) throws Exception;


	/**
	 *
	 * <PRE>
	 * 1. MethodName : cntntIdAjax
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 아이디 생성
	 * 4. 작성자    : :LWJ
	 * 5. 작성일    : 2020. 6. 9. 오후 4:49:52
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap cntntIdAjax(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : cntntIdChkAjax
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 아이디 중복 확인
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 8. 24. 오전 11:20:54
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int cntntIdChkAjax(DataMap param) throws Exception;

}
