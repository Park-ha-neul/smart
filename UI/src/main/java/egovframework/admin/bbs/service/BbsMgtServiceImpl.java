package egovframework.admin.bbs.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.dao.NtsysFileManageDao;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.cryptography.EgovEnvCryptoService;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BbsMgtServiceImpl.java
 * 3. Package  : egovframework.admin.bbs.service
 * 4. Comment  :
 * 5. 작성자   : mk
 * 6. 작성일   : 2018. 7. 23. 오전 11:56:44
 * </PRE>
 */
@Service("bbsMgtService")
public class BbsMgtServiceImpl extends EgovAbstractServiceImpl implements BbsMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	/** 파일관련 */
	@Resource(name = "NtsysFileManageDao")
	private NtsysFileManageDao ntsysFileManageDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;


	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntBbsMgt
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 총 개수
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:56:47
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntBbsMgt(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.bbs.selectTotCntBbsMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListBbsMgt
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 목록
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:56:54
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListBbsMgt(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.bbs.selectPageListBbsMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMgt
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 상세
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:57:05
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectBbsMgt(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.bbs.selectBbsMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertBbsMgt
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 등록
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:57:10
	 * </PRE>
	 *   @param param
	 *   @param imgFileList
	 *   @param thumbFileList
	 *   @param fileList
	 *   @throws Exception
	 */
	public void insertBbsMgt(DataMap param, List imgFileList, List thumbFileList, List fileList) throws Exception {

		// IMAGE_DOC_ID 셋팅
		String imageDocId = StringUtil.nvl(param.getString("image_doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", imageDocId );

		// FILE_DOC_ID 셋팅
		String atchDocId = StringUtil.nvl(param.getString("atch_doc_id"), SysUtil.getDocId());
		param.put("doc_id", atchDocId );

		// THUMB_DOC_ID 셋팅
		String thumbDocId = StringUtil.nvl(param.getString("thumb_doc_id"), SysUtil.getDocId());
		param.put("thumb_doc_id", thumbDocId );

		// ########### Image File 처리 시작 #############
		String[] imgPathList = param.getArray("imgPathList");

		String cn = param.getString("cn");
		NtsysFileVO reNtsysFile = null;
		if(!imgFileList.isEmpty()){
			for(int i=0; i < imgFileList.size(); i++){
				MultipartFile mfile = (MultipartFile)imgFileList.get(i);
				//CN에 mfile name 정보를 포함하고 있지 않은 경우 재업로드를 진행하지 않는다.
				if(!cn.contains(imgPathList[i]))
					continue;

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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, imageDocId, "bbs/" + param.getString("bbs_code") + "/" , param.getString("ss_user_no"), "Y");

					cn = cn.replaceAll(imgPathList[i], reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm());

					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		param.put("cn", cn);
		// ########### Image File 처리 종료 ############


		//썸네일 파일 업로드
		fileUpload(thumbFileList, thumbDocId, "bbs/" + param.getString("bbs_code") + "/", param.getString("ss_user_no"), "Y");


		//첨부파일 업로드
		fileUpload(fileList, atchDocId, "bbs/" + param.getString("bbs_code") + "/", param.getString("ss_user_no"), "Y");


		//컨텐츠 내용 html태그 제거 후 검색용 텍스트로 변환
		/* 오류 발생으로 수정 by pjh
		String target = param.getStringOrgn("cn");
		String regEx = "<[/]?[a-zA-Z]+(((?<=[a-zA-Z]=\").(.)*?)\"|[a-zA-Z0-9 =\"'&#;:\\-\\_.%\\/!@$^*()])*>|&([a-z0-9#;])*;|>|<";
		Pattern pat = Pattern.compile(regEx);
		Matcher match = pat.matcher(target);
		param.put("search_cn", match.replaceAll(""));
		*/
		String target = param.getString("cn");
		String regEx = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";
		param.put("search_cn", target.replaceAll(regEx, ""));

		commonMybatisDao.insert("admin.bbs.insertBbsMgt",param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateBbsMgt
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 수정
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:57:18
	 * </PRE>
	 *   @param param
	 *   @param imgFileList
	 *   @param thumbFileList
	 *   @param fileList
	 *   @throws Exception
	 */
	public void updateBbsMgt(DataMap param, List imgFileList, List thumbFileList, List fileList) throws Exception {

		// IMAGE_DOC_ID 셋팅
		String imageDocId = StringUtil.nvl(param.getString("image_doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", imageDocId );

		// FILE_DOC_ID 셋팅
		String atchDocId = StringUtil.nvl(param.getString("atch_doc_id"), SysUtil.getDocId());


		// THUMB_DOC_ID 셋팅
		String thumbDocId = StringUtil.nvl(param.getString("thumb_doc_id"), SysUtil.getDocId());
		param.put("thumb_doc_id", thumbDocId );


		//#### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(imageDocId);
		List<NtsysFileVO> deleteFileList = commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
		//#### FILE LIST 검색 End ####

		String cn = param.getString("cn");
		if(deleteFileList != null && deleteFileList.size() > 0){
			//### 제거된 FILE LIST 처리 Start ###
			for(int i = deleteFileList.size()-1; i > -1 ; i--){
				if(cn.contains(deleteFileList.get(i).getFileId())){
					deleteFileList.remove(i);
				}
			}

			ntsysFileMngUtil.deleteFile(deleteFileList);	// 물리파일 삭제
			param.put("doc_id", imageDocId);

			for(int i = 0; i < deleteFileList.size(); i++){
				param.put("file_id", deleteFileList.get(i).getFileId());
				commonMybatisDao.delete("common.file.deleteAttchFile", param);// 첨부파일 DB 삭제
			}
			//### 제거된 FILE LIST 처리 End ###
		}

		// ########### Image File 처리 시작 #############
		String[] imgPathList = param.getArray("imgPathList");

		NtsysFileVO reNtsysFile = null;
		if(!imgFileList.isEmpty()){
			for(int i=0; i < imgFileList.size(); i++){
				MultipartFile mfile = (MultipartFile)imgFileList.get(i);
				//CN에 mfile name 정보를 포함하고 있지 않은 경우 재업로드를 진행하지 않는다.
				if(!cn.contains(imgPathList[i]))
					continue;

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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, imageDocId, "bbs/" + param.getString("bbs_code") + "/" , param.getString("ss_user_no"), "Y");

					cn = cn.replaceAll(imgPathList[i], reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm());

					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		param.put("cn", cn);
		// ########### Image File 처리 종료 ############

		//썸네일 파일 업로드
		fileUpload(thumbFileList, thumbDocId, "bbs/" + param.getString("bbs_code") + "/", param.getString("ss_user_no"), "Y");

		//첨부파일 업로드
		fileUpload(fileList, atchDocId, "bbs/" + param.getString("bbs_code") + "/", param.getString("ss_user_no"), "Y");

		//컨텐츠 내용 html태그 제거 후 검색용 텍스트로 변환
		/* 오류 발생으로 수정 by pjh
		String target = param.getStringOrgn("cn");
		String regEx = "<[/]?[a-zA-Z]+(((?<=[a-zA-Z]=\").(.)*?)\"|[a-zA-Z0-9 =\"'&#;:\\-\\_.%\\/!@$^*()])*>|&([a-z0-9#;])*;|>|<";
		Pattern pat = Pattern.compile(regEx);
		Matcher match = pat.matcher(target);
		param.put("search_cn", match.replaceAll(""));
		*/
//		String target = param.getStringOrgn("cn");
		String target = param.getString("cn");
		String regEx = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";
		param.put("search_cn", target.replaceAll(regEx, ""));


		//첨부파일 doc_id셋팅
		param.put("doc_id", atchDocId);
		commonMybatisDao.update("admin.bbs.updateBbsMgt",param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteBbsMgt
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 삭제
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:57:25
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteBbsMgt(DataMap param) throws Exception {

		deleteAllBbsAnswer(param);

		// 첨부 파일 삭제
		String atchDocId = param.getString("atch_doc_id");
		fileDelete(param, atchDocId);

		//썸네일 이미지 삭제
		String thumbDocId = param.getString("thumb_doc_id");
		fileDelete(param, thumbDocId);

		// 이미지 삭제
		String imageDocId = param.getString("image_doc_id");
		fileDelete(param, imageDocId);

		commonMybatisDao.delete("admin.bbs.deleteBbsMgt", param);

	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListBbsCode
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 게시판 코드 목록 조회
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:57:31
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectListBbsCode(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.bbs.selectListBbsCode", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMngInfo
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 게시판 코드 정보 조회
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:57:41
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectBbsMngInfo(DataMap param) throws Exception{
		return (DataMap) commonMybatisDao.selectOne("admin.bbs.selectBbsMngInfo", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntBbsAns
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 답변 총 개수
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 11. 7. 오후 7:11:56
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntBbsAns(DataMap param) throws Exception{
		return (Integer) commonMybatisDao.selectOne("admin.bbs.selectTotCntBbsAns", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListBbsAns
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 답변 리스트
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:57:50
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListBbsAns(DataMap param) throws Exception{
		return (List) commonMybatisDao.selectList("admin.bbs.selectPageListBbsAns", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsAns
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 답변 상세
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 11. 7. 오후 7:15:28
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectBbsAns(DataMap param) throws Exception{
		return (DataMap) commonMybatisDao.selectOne("admin.bbs.selectBbsAns", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertBbsAnswer
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 답변 등록
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:57:56
	 * </PRE>
	 *   @param param
	 *   @param imgFileList
	 *   @param fileList
	 *   @throws Exception
	 */
	public void insertBbsAnswer(DataMap param, List imgFileList, List fileList) throws Exception {

		// IMAGE_DOC_ID 셋팅
		String imageDocId = StringUtil.nvl(param.getString("ans_image_doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", imageDocId);

		// FILE_DOC_ID 셋팅
		String atchDocId = StringUtil.nvl(param.getString("ans_atch_doc_id"), SysUtil.getDocId());
		param.put("doc_id", atchDocId);

		// ########### Image File 처리 시작 #############
		String[] imgPathList = param.getArray("imgPathList");

		String cn = param.getString("cn");
		NtsysFileVO reNtsysFile = null;
		if(!imgFileList.isEmpty()){
			for(int i=0; i < imgFileList.size(); i++){
				MultipartFile mfile = (MultipartFile)imgFileList.get(i);
				//CN에 mfile name 정보를 포함하고 있지 않은 경우 재업로드를 진행하지 않는다.
				if(!cn.contains(imgPathList[i]))
					continue;

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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, imageDocId, "bbs/" + param.getString("bbs_code") + "/", param.getString("ss_user_no"), "Y");

					cn = cn.replaceAll(imgPathList[i], reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm());

					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		param.put("cn", cn);
		// ########### Image File 처리 종료 ############

		//첨부파일 업로드
		fileUpload(fileList, atchDocId, "bbs/" + param.getString("bbs_code") + "/", param.getString("ss_user_no"), "Y");

		commonMybatisDao.insert("admin.bbs.insertBbsAnswer",param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateBbsAnswer
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 답변 수정
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:58:05
	 * </PRE>
	 *   @param param
	 *   @param imgFileList
	 *   @param fileList
	 *   @throws Exception
	 */
	public void updateBbsAnswer(DataMap param, List imgFileList, List fileList) throws Exception {

		// IMAGE_DOC_ID 셋팅
		String imageDocId = StringUtil.nvl(param.getString("ans_image_doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", imageDocId);

		// FILE_DOC_ID 셋팅
		String atchDocId = StringUtil.nvl(param.getString("ans_atch_doc_id"), SysUtil.getDocId());


		//#### FILE LIST 검색 Start ####
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(imageDocId);
		List<NtsysFileVO> deleteFileList = commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
		//#### FILE LIST 검색 End ####

		//### 제거된 FILE LIST 처리 Start ###
		String cn = param.getString("cn");
		for(int i = deleteFileList.size()-1; i > -1 ; i--){
			if(cn.contains(deleteFileList.get(i).getFileId())){
				deleteFileList.remove(i);
			}
		}

		ntsysFileMngUtil.deleteFile(deleteFileList);	// 물리파일 삭제
		param.put("doc_id", imageDocId);

		for(int i = 0; i < deleteFileList.size(); i++){
			param.put("file_id", deleteFileList.get(i).getFileId());
			commonMybatisDao.delete("common.file.deleteAttchFile", param);// 첨부파일 DB 삭제
		}
		//### 제거된 FILE LIST 처리 End ###

		// ########### Image File 처리 시작 #############
		String[] imgPathList = param.getArray("imgPathList");

		NtsysFileVO reNtsysFile = null;
		if(!imgFileList.isEmpty()){
			for(int i=0; i < imgFileList.size(); i++){
				MultipartFile mfile = (MultipartFile)imgFileList.get(i);
				//CN에 mfile name 정보를 포함하고 있지 않은 경우 재업로드를 진행하지 않는다.
				if(!cn.contains(imgPathList[i]))
					continue;

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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, imageDocId, "bbs/" + param.getString("bbs_code") + "/", param.getString("ss_user_no"), "Y");

					cn = cn.replaceAll(imgPathList[i], reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm());

					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		param.put("cn", cn);
		// ########### Image File 처리 종료 ############

		//첨부파일 업로드
		fileUpload(fileList, atchDocId, "bbs/" + param.getString("bbs_code") + "/", param.getString("ss_user_no"), "Y");

		//첨부파일 doc_id셋팅
		param.put("doc_id", atchDocId);
		commonMybatisDao.update("admin.bbs.updateBbsAnswer",param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteBbsAnswer
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 답변 삭제
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 7. 23. 오전 11:58:18
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteBbsAnswer(DataMap param) throws Exception {
		// 첨부 파일 삭제
		String atchDocId = param.getString("ans_atch_doc_id");
		fileDelete(param, atchDocId);

		// 이미지 삭제
		String imageDocId = param.getString("ans_image_doc_id");
		fileDelete(param, imageDocId);

		commonMybatisDao.delete("admin.bbs.deleteBbsAnswer", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteAllBbsAnswer
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 답변 전체 삭제
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 11. 7. 오후 8:54:06
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteAllBbsAnswer(DataMap param) throws Exception {

		List deleteBbsList = (List)commonMybatisDao.selectList("admin.bbs.selectListBbsAns", param);
		DataMap deleteMap = new DataMap();
		for(int i = 0; i < deleteBbsList.size(); i++){
			deleteMap = (DataMap)deleteBbsList.get(i);

			// 첨부 파일 삭제
			String atchDocId = deleteMap.getString("DOC_ID");
			fileDelete(deleteMap, atchDocId);

			// 이미지 삭제
			String imageDocId = deleteMap.getString("IMAGE_DOC_ID");
			fileDelete(deleteMap, imageDocId);
		}

		commonMybatisDao.delete("admin.bbs.deleteAllBbsAnswer", deleteMap);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateChnageOrganBbs
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시물 관리 게시물 이동
	 * 4. 작성자	: mk
	 * 5. 작성일	: 2018. 9. 3. 오후 8:31:18
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void updateChnageOrganBbs(DataMap param) throws Exception {
		commonMybatisDao.update("admin.bbs.updateChnageOrganBbs", param);
	}

	protected void fileUpload(List fileList, String docId, String saveDir, String ssUserId, String webrootYn ) throws Exception{
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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, docId, saveDir, ssUserId, webrootYn);
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
	}

	protected void fileDelete(DataMap param, String docId) throws Exception{
		// 파일 삭제
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(docId);
		List<NtsysFileVO> dFile = (List)commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
		ntsysFileMngUtil.deleteFile(dFile, "I");
		param.put("doc_id", docId);
		commonMybatisDao.delete("common.file.deleteAttchFiles", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsAuthorYn
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 게시판에 대한 권한 확인
	 * 4. 작성자	: JJH
	 * 5. 작성일	: 2019. 1. 17. 오후 2:14:11
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public String selectBbsAuthorYn(DataMap param) throws Exception {
		return (String)commonMybatisDao.selectOne("admin.bbs.selectBbsAuthorYn", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectCheckTabSeCodeYn
	 * 2. ClassName  : BbsMgtServiceImpl
	 * 3. Comment   : 탭구분 값 권한별 확인
	 * 4. 작성자	: JJH
	 * 5. 작성일	: 2019. 1. 17. 오후 5:28:52
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public String selectCheckTabSeCodeYn(DataMap param) throws Exception {
		return (String)commonMybatisDao.selectOne("admin.bbs.selectCheckTabSeCodeYn", param);
	}
}
