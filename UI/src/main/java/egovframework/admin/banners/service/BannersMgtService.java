
package egovframework.admin.banners.service;

import java.util.List;

import egovframework.admin.banners.vo.BannersImgFileVo;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.file.vo.NtsysFileVO;


public interface BannersMgtService {

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntBanners
	 * 2. ClassName  : BannersMgtService
	 * 3. Comment   : 배너 총 개수 조회
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:05:16
	 * </PRE>
	 *   @return int
	 *   @param param
	 *   @return
	 */
	int selectTotCntBanners(DataMap param);

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListBanners
	 * 2. ClassName  : BannersMgtService
	 * 3. Comment   : 배너 목록 조회
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:05:45
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	List selectPageListBanners(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : insertBanners
	 * 2. ClassName  : BannersMgtService
	 * 3. Comment   : 배너 입력
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:06:10
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param filelist_w
	 *   @param filelist_m
	 *   @throws Exception
	 */
	void insertBanners(DataMap param, List filelistW, List filelistM) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : selectBanners
	 * 2. ClassName  : BannersMgtService
	 * 3. Comment   : 배너 상세 조회
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:06:31
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	DataMap selectBanners(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : updateBanners
	 * 2. ClassName  : BannersMgtService
	 * 3. Comment   : 배너 수정
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:06:54
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param filelist_w
	 *   @param filelist_m
	 *   @throws Exception
	 */
	void updateBanners(DataMap param, List filelistW, List filelistM) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : deleteBanners
	 * 2. ClassName  : BannersMgtService
	 * 3. Comment   : 배너 삭제
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:07:06
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	void deleteBanners(DataMap param) throws Exception;


	/**
	 * <PRE>
	 * 1. MethodName : selectBannersImgs
	 * 2. ClassName  : BannersMgtService
	 * 3. Comment   : 배너 이미지 리스트 조회
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 8. 9. 오후 8:26:56
	 * </PRE>
	 *   @return List
	 *   @param ifvo
	 *   @return
	 *   @throws Exception
	 */
	List selectBannersImgs(BannersImgFileVo ifvo) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : selectBannersImg
	 * 2. ClassName  : BannersMgtService
	 * 3. Comment   : 배너 이미지정보 조회
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 8. 9. 오후 8:26:59
	 * </PRE>
	 *   @return NtsysFileVO
	 *   @param fvo
	 *   @return
	 *   @throws Exception
	 */
	public NtsysFileVO selectBannersImg(NtsysFileVO fvo) throws Exception;

	/**
	 * <PRE>
	 * 1. MethodName : deleteBannersImg
	 * 2. ClassName  : BannersMgtService
	 * 3. Comment   : 배너 이미지정보 삭제
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 8. 9. 오후 8:27:02
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteBannersImg(DataMap param) throws Exception;


}
