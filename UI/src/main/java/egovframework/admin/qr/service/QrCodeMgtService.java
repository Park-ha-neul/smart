

package egovframework.admin.qr.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;


/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : QrCodeMgtService.java
 * 3. Package  : egovframework.admin.qr.service
 * 4. Comment  :
 * 5. 작성자   : mk
 * 6. 작성일   : 2018. 7. 11. 오전 11:31:47
 * </PRE>
 */
public interface QrCodeMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntQrCodeMgt
	 * 2. ClassName  : QrCodeMgtService
	 * 3. Comment   : qr 코드 총개수
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 11. 오전 11:36:47
	 * </PRE>
	 *   @return String
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	int selectTotCntQrCodeMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListQrCodeMgt
	 * 2. ClassName  : QrCodeMgtService
	 * 3. Comment   : qr 코드 리스트
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 11. 오전 11:36:50
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListQrCodeMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectQrCodeMgt
	 * 2. ClassName  : QrCodeMgtService
	 * 3. Comment   : qr 코드 상세
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 11. 오전 11:37:03
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectQrCodeMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : insertQrCodeMgt
	 * 2. ClassName  : QrCodeMgtService
	 * 3. Comment   : qr code 등록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 11. 오전 11:37:20
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param fileList
	 *   @throws Exception
	 */
	void insertQrCodeMgt(DataMap param, List fileList) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : updateQrCodeMgt
	 * 2. ClassName  : QrCodeMgtService
	 * 3. Comment   : qr code 수정
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 11. 오전 11:37:28
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param fileList
	 *   @throws Exception
	 */
	void updateQrCodeMgt(DataMap param, List fileList, String imgExistYn) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteQrCodeMgt
	 * 2. ClassName  : QrCodeMgtService
	 * 3. Comment   : qr code 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 11. 오전 11:37:40
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteQrCodeMgt(DataMap param) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectImgExistYn
	 * 2. ClassName  : QrCodeMgtService
	 * 3. Comment   : 상세정보에 배경이미지 존재 유무 확인
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 11. 오후 4:40:35
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectImgExistYn(DataMap param) throws Exception;
}
