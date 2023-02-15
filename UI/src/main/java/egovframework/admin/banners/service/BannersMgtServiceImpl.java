

package egovframework.admin.banners.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.admin.banners.vo.BannersImgFileVo;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


@Service("bannersMgtService")
public class BannersMgtServiceImpl extends EgovAbstractServiceImpl implements BannersMgtService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntBanners
	 * 2. ClassName  : BannersMgtServiceImpl
	 * 3. Comment   : 배너 총 개수 조회
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:07:43
	 * </PRE>
	 *   @param param
	 *   @return
	 */
	public int selectTotCntBanners(DataMap param) {
		return (Integer) commonMybatisDao.selectOne("admin.banners.selectTotCntBanners", param);
	}



    /**
     * <PRE>
     * 1. MethodName : selectPageListBanners
     * 2. ClassName  : BannersMgtServiceImpl
     * 3. Comment   : 배너 목록
     * 4. 작성자    : 김진영
     * 5. 작성일    : 2018. 7. 23. 오전 10:07:57
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public List selectPageListBanners(DataMap param) throws Exception {
    	 return (List) commonMybatisDao.selectList("admin.banners.selectPageListBanners", param);
	}


	/**
	 * <PRE>
	 * 1. MethodName : insertBanners
	 * 2. ClassName  : BannersMgtServiceImpl
	 * 3. Comment   : 배너 입력
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:14:50
	 * </PRE>
	 *   @param param
	 *   @param fileList_w
	 *   @param fileList_m
	 *   @throws Exception
	 */
	public void insertBanners(DataMap param, List filelistW, List filelistM) throws Exception {

		// 글 ID 셋팅
		param.put("web_image_doc_id", SysUtil.getDocId());
		param.put("mobile_image_doc_id", SysUtil.getDocId());

		uploadFile(filelistW, param.getString("web_image_doc_id"), "banner_w/", param, "1");
		uploadFile(filelistM, param.getString("mobile_image_doc_id"), "banner_m/", param, "2");

		commonMybatisDao.insert("admin.banners.insertBanners", param);
	}



	/**
	 * <PRE>
	 * 1. MethodName : selectBanners
	 * 2. ClassName  : BannersMgtServiceImpl
	 * 3. Comment   : 배너 상세 조회
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:15:24
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectBanners(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("admin.banners.selectBanners", param);
	}


	/**
	 * <PRE>
	 * 1. MethodName : updateBanners
	 * 2. ClassName  : BannersMgtServiceImpl
	 * 3. Comment   : 배너 수정
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:15:49
	 * </PRE>
	 *   @param param
	 *   @param fileList_w
	 *   @param fileList_m
	 *   @throws Exception
	 */
	public void updateBanners(DataMap param, List fileListW, List fileListM) throws Exception {

		List updateFileIdList = param.getList("file_id_update");
		List updateSortOrdrList = param.getList("sort_ordr_update");
		List updateUrlList = param.getList("link_url_update");
		List updateAttrb1List = param.getList("attrb_1_update");
		List updateAttrb2List = param.getList("attrb_2_update");
		List updateAttrb3List = param.getList("attrb_3_update");

		if(updateFileIdList != null && updateFileIdList.size() > 0){
			DataMap tmpMap = new DataMap();
			for(int i = 0; i < updateFileIdList.size(); i++){
				tmpMap.put("file_id", updateFileIdList.get(i));
				tmpMap.put("sort_ordr", updateSortOrdrList.get(i));
				tmpMap.put("link_url", updateUrlList.get(i));
				tmpMap.put("attrb_1", updateAttrb1List.get(i));
				tmpMap.put("attrb_2", updateAttrb2List.get(i));
				tmpMap.put("attrb_3", updateAttrb3List.get(i));

				commonMybatisDao.update("admin.banners.updateBannersImg", tmpMap);
			}
		}

		String webImageDocId = StringUtil.nvl(param.getString("web_image_doc_id"), SysUtil.getDocId());
		param.put("web_image_doc_id", webImageDocId );

		String mobileImageDocId = StringUtil.nvl(param.getString("mobile_image_doc_id"), SysUtil.getDocId());
		param.put("mobile_image_doc_id", mobileImageDocId );

		uploadFile(fileListW, param.getString("web_image_doc_id"), "banner_w/", param, "1");
		uploadFile(fileListM, param.getString("mobile_image_doc_id"), "banner_m/", param, "2");

		commonMybatisDao.update("admin.banners.updateBanners", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteBanners
	 * 2. ClassName  : BannersMgtServiceImpl
	 * 3. Comment   : 배너 삭제
	 * 4. 작성자    : 안소영
	 * 5. 작성일    : 2020. 6. 17. 오전 10:13:01
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteBanners(DataMap param) throws Exception {

    	// 배너 이미지 테이블에서 삭제
		deleteFile(param.getString("web_image_doc_id"));
		deleteFile(param.getString("mobile_image_doc_id"));

		commonMybatisDao.delete("admin.banners.deleteBanners", param);
	}

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
	public List selectBannersImgs(BannersImgFileVo ifvo) throws Exception {
		return commonMybatisDao.selectList("admin.banners.selectBannersImgs", ifvo);
	}

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
	public NtsysFileVO selectBannersImg(NtsysFileVO fvo) throws Exception {
		return (NtsysFileVO) commonMybatisDao.selectOne("admin.banners.selectBannersImg", fvo);
	}

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
	public void deleteBannersImg(DataMap param) throws Exception {
		commonMybatisDao.delete("admin.banners.deleteBannersImg", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : uploadFile
	 * 2. ClassName  : BannersMgtServiceImpl
	 * 3. Comment   : 배너 이미지 업로드
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:16:29
	 * </PRE>
	 *   @return void
	 *   @param fileList
	 *   @param doc_id
	 *   @param dir
	 *   @param ss_user_no
	 *   @throws Exception
	 */
	protected void uploadFile(List fileList, String docId, String dir, DataMap param, String uploadZoneNo) throws Exception{
		List sortOrdrList = param.getList("sort_ordr_"+uploadZoneNo);
		List linkUrl = param.getList("link_url_"+uploadZoneNo);
		List attrb1 = param.getList("attrb_1_"+uploadZoneNo);
		List attrb2 = param.getList("attrb_2_"+uploadZoneNo);
		List attrb3 = param.getList("attrb_3_"+uploadZoneNo);

		// ########### Upload File 처리 시작 #############
		NtsysFileVO reNtsysFile = null;
		DataMap tmpMap = new DataMap();
		if(!fileList.isEmpty()){

			for(int i=0; i < fileList.size(); i++){
				MultipartFile mfile = (MultipartFile)fileList.get(i);
				if(!mfile.isEmpty()){
					/*
					 * parseFileInf
					 * 1:파일객체
					 * 2:문서아이디
					 * 3:서브폴더명
					 * 4:사용자번호
					 * 5:Web Root Yn
					 */
					// 파일을 서버에 물리적으로 저장하고
					reNtsysFile = ntsysFileMngUtil.parseFileInf(mfile, docId, dir, param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("admin.banners.insertBannersImg", reNtsysFile);

					//배너이미지 정보 입력
					tmpMap.put("sort_ordr", sortOrdrList.get(i));
					tmpMap.put("link_url", linkUrl.get(i));
					tmpMap.put("attrb_1", attrb1.get(i));
					tmpMap.put("attrb_2", attrb2.get(i));
					tmpMap.put("attrb_3", attrb3.get(i));
					tmpMap.put("file_id", reNtsysFile.getFileId());
					commonMybatisDao.update("admin.banners.updateBannersImg", tmpMap);
				}
			}
		}
		// ########### Upload File 처리 종료 ############
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteFile
	 * 2. ClassName  : BannersMgtServiceImpl
	 * 3. Comment   : 배너 이미지 삭제
	 * 4. 작성자    : 김진영
	 * 5. 작성일    : 2018. 7. 23. 오전 10:16:52
	 * </PRE>
	 *   @return void
	 *   @param doc_id
	 *   @throws Exception
	 */
	protected void deleteFile(String docId) throws Exception{

		//이미지 파일 정보 조회
    	BannersImgFileVo ifvo = new BannersImgFileVo();
		ifvo.setDocId(docId);
		List<NtsysFileVO> fileList = commonMybatisDao.selectList("admin.banners.selectBannersImgs", ifvo);

    	ntsysFileMngUtil.deleteFile(fileList);	// 물리파일 삭제

    	DataMap param = new DataMap();
    	param.put("doc_id", docId);
    	commonMybatisDao.delete("admin.banners.deleteBannersImgs", param);//파일정보 DB 삭제
	}
}
