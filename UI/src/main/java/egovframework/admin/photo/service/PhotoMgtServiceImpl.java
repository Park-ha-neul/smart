package egovframework.admin.photo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.admin.photo.vo.PhotoMetaVo;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovWebUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : PhotoMgtServiceImpl.java
 * 3. Package  : egovframework.admin.photo.service
 * 4. Comment  : 사진관리 관리
 * 5. 작성자   : 박재현
 * 6. 작성일   : 2015. 9. 7. 오전 9:51:39
 * </PRE>
 */
@Service("photoMgtService")
public class PhotoMgtServiceImpl extends EgovAbstractServiceImpl implements PhotoMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;


	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntPhotoMgt
	 * 2. ClassName  : PhotoMgtServiceImpl
	 * 3. Comment   : 사진관리 총개수 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 9:52:14
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntPhotoMgt(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.photo.selectTotCntPhotoMgt", param);
	}



	/**
	 * <PRE>
	 * 1. MethodName : selectPageListPhotoMgt
	 * 2. ClassName  : PhotoMgtServiceImpl
	 * 3. Comment   : 사진관리 페이지 리스트 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 9:52:16
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListPhotoMgt(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.photo.selectPageListPhotoMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPhotoGroup
	 * 2. ClassName  : PhotoMgtServiceImpl
	 * 3. Comment   : 사진 상세조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 10:14:57
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectPhotoGroup(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.photo.selectPhotoGroup", param);
	}



	/**
	 * <PRE>
	 * 1. MethodName : selectListPhotoFiles
	 * 2. ClassName  : PhotoMgtServiceImpl
	 * 3. Comment   : 사진 목록 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오전 8:49:39
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectListPhotoFiles(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.photo.selectListPhotoFiles", param);
	}


	/**
	 * <PRE>
	 * 1. MethodName : deletePhotoMgt
	 * 2. ClassName  : PhotoMgtServiceImpl
	 * 3. Comment   : 사진 삭제
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 11:33:21
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deletePhotoMgt(DataMap param) throws Exception {


		//#### FILE LIST 검색 Start ####
    	NtsysFileVO fvo = new NtsysFileVO();
    	fvo.setDocId(param.getString("image_doc_id"));
    	List<NtsysFileVO> fileList = commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
    	//#### FILE LIST 검색 End ####

    	ntsysFileMngUtil.deleteFile(fileList);	// 물리파일 삭제
    	param.put("doc_id", param.getString("image_doc_id"));
    	commonMybatisDao.delete("common.file.deleteAttchFiles", param);// 첨부파일 DB 삭제

    	commonMybatisDao.delete("admin.photo.deletePhotoGroup", param);
    	commonMybatisDao.delete("admin.photo.deletePhotoMeta", param);
	}


	/**
	 * <PRE>
	 * 1. MethodName : insertPhotoMgt
	 * 2. ClassName  : PhotoMgtServiceImpl
	 * 3. Comment   : 사진 등록
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 11. 오후 4:52:33
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	public void insertPhotoMgt(DataMap param, List fileList) throws Exception {

		// 글 ID 셋팅
		String docId = StringUtil.nvl(param.getString("doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", docId );

		// ########### Upload File 처리 시작 #############
		NtsysFileVO reNtsysFile = null;
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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, docId, "photo/", param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		// ########### Upload File 처리 종료 ############

		//commonMybatisDao.insert("admin.photo.insertPhotoMgt", param);
	}


	/**
	 * <PRE>
	 * 1. MethodName : updatePhotoMgt
	 * 2. ClassName  : PhotoMgtServiceImpl
	 * 3. Comment   : 사진 수정
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 11. 오후 4:54:54
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	public void updatePhotoMgt(DataMap param, List fileList) throws Exception {

		// ########### Upload File 처리 시작 #############
		NtsysFileVO reNtsysFile = null;
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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, param.getString("image_doc_id"), "photo/", param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		// ########### Upload File 처리 종료 ############
		commonMybatisDao.update("admin.photo.updatePhotoMgt", param);
	}



	/**
	 * <PRE>
	 * 1. MethodName : insertPhotoImage
	 * 2. ClassName  : PhotoMgtServiceImpl
	 * 3. Comment   : 포토 임시 업로드 등록용
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오전 8:46:50
	 * </PRE>
	 *   @param param
	 *   @param fileList
	 *   @throws Exception
	 */
	public void insertPhotoImage(DataMap param, List fileList) throws Exception {

		// 글 ID 셋팅
		String docId = StringUtil.nvl(param.getString("doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", docId );

		List cationList = param.getList("caption");
		List keywordList = param.getList("kwrd");
		PhotoMetaVo vo = null;

		commonMybatisDao.insert("admin.photo.insertPhotoGroup", param);

		// ########### Upload File 처리 시작 #############
		NtsysFileVO reNtsysFile = null;
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
					reNtsysFile	= ntsysFileMngUtil.chgSavePhotoFileVO(mfile, docId, "photo/", param.getString("ss_user_no"));

					vo = ntsysFileMngUtil.writePhotoFile(reNtsysFile, mfile, reNtsysFile.getFileId(), EgovWebUtil.filePathBlackList(reNtsysFile.getFileAsltPath()));

					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);

					param.put("file_id", reNtsysFile.getFileId());
					param.put("caption", cationList.get(i));
					param.put("kwrd", keywordList.get(i));
					param.put("meta_info", vo.getMetaInfo());
					param.put("width", vo.getWidth());
					param.put("height", vo.getHeight());
					commonMybatisDao.insert("admin.photo.insertPhotoMeta", param);
				}
			}
		}
		// ########### Upload File 처리 종료 ############

		//commonMybatisDao.insert("admin.photo.insertPhotoMgt", param);
	}



	/**
	 * <PRE>
	 * 1. MethodName : updatePhotoImage
	 * 2. ClassName  : PhotoMgtServiceImpl
	 * 3. Comment   : 포토 임시 업로드 수정용
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2018. 7. 18. 오전 8:47:03
	 * </PRE>
	 *   @param param
	 *   @param fileList
	 *   @throws Exception
	 */
	public void updatePhotoImage(DataMap param, List fileList) throws Exception {

		// 글 ID 셋팅
		String docId = StringUtil.nvl(param.getString("doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", docId );

		List cationList = param.getList("caption");
		List keywordList = param.getList("kwrd");
		PhotoMetaVo vo = null;

		List fileIdUpdateList = param.getList("file_id_update");
		List cationUpdateList = param.getList("caption_update");
		List keywordUpdateList = param.getList("kwrd_update");

		if(fileIdUpdateList != null && fileIdUpdateList.size() > 0){
			DataMap tmp = new DataMap();
			for(int i = 0; i < fileIdUpdateList.size(); i++){

				tmp.put("file_id_update", fileIdUpdateList.get(i));
				tmp.put("caption_update", cationUpdateList.get(i));
				tmp.put("kwrd_update", keywordUpdateList.get(i));
				tmp.put("ss_user_no", param.getString("ss_user_no"));

				commonMybatisDao.update("admin.photo.updatePhotoMeta", tmp);
			}
		}


		commonMybatisDao.update("admin.photo.updatePhotoGroup", param);

		// ########### Upload File 처리 시작 #############
		NtsysFileVO reNtsysFile = null;
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
					reNtsysFile	= ntsysFileMngUtil.chgSavePhotoFileVO(mfile, docId, "photo/", param.getString("ss_user_no"));

					vo = ntsysFileMngUtil.writePhotoFile(reNtsysFile, mfile, reNtsysFile.getFileId(), EgovWebUtil.filePathBlackList(reNtsysFile.getFileAsltPath()));

					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);

					param.put("file_id", reNtsysFile.getFileId());
					param.put("caption", cationList.get(i));
					param.put("kwrd", keywordList.get(i));
					param.put("meta_info", vo.getMetaInfo());
					param.put("width", vo.getWidth());
					param.put("height", vo.getHeight());
					commonMybatisDao.insert("admin.photo.insertPhotoMeta", param);
				}
			}
		}
		// ########### Upload File 처리 종료 ############

	}

}
