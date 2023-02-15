package egovframework.admin.cnts.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.CookieUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

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
@Service("cntsMgtService")
public class CntsMgtServiceImpl extends EgovAbstractServiceImpl implements CntsMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;


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
	public int selectTotCntMgt(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.cnts.selectTotCntMgt", param);
	}


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
	public List selectPageListCntsMgt(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.cnts.selectPageListCntsMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectCntsMgt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 콘텐츠 상세조회
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018.6.26
	 * </PRE>
	 *   @return DataMap
	 *   @param param
	 *   @return
	 */
	public DataMap selectCntsMgt(HttpServletRequest request, HttpServletResponse response, DataMap param) throws Exception {

		// 쿠키 체크로 히트 카운트 중복 방지 처리
		if (CookieUtil.getCookieValue(request, "selectCnts" + param.getString("cntnts_seq")).equals("")) {
			commonMybatisDao.update("admin.cnts.updateHitCnt", param);
			CookieUtil.addCookie(request, response, 60 * 60, "selectCnts" + param.getString("cntnts_seq"),
					param.getString("cntnts_seq"));
		}
		// select_ver 값이 존재하는지 확인하여 현재 oldver이 선택됐는지 판단한다
		if (param.getString("select_ver") != null && !(param.getString("select_ver").equals("")))
			return (DataMap) commonMybatisDao.selectOne("admin.cnts.selectOldVerMgt", param);
		else
			return (DataMap) commonMybatisDao.selectOne("admin.cnts.selectCntsMgt", param);
	}


	public List selectCntsMenuMgt(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.cnts.selectCntsMenuMgt", param);
	}
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
	public void deleteCntsMgt(DataMap param) throws Exception {

		//파일 DB 및 물리파일 삭제 필요 시 주석 해제


		//#### FILE LIST 검색 Start #### - 첨부 파일
    	NtsysFileVO fvo = new NtsysFileVO();
    	fvo.setDocId(param.getString("doc_id"));
    	List<NtsysFileVO> fileList = commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
    	//#### FILE LIST 검색 End ####

    	ntsysFileMngUtil.deleteFile(fileList);	// 물리파일 삭제
    	commonMybatisDao.delete("common.file.deleteAttchFiles", param);// 첨부파일 DB 삭제


		//#### FILE LIST 검색 Start #### - 이미지 파일
    	fvo = new NtsysFileVO();
    	fvo.setDocId(param.getString("image_doc_id"));
    	fileList = commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
    	//#### FILE LIST 검색 End ####

    	ntsysFileMngUtil.deleteFile(fileList);	// 물리파일 삭제
    	param.put("doc_id", param.getString("image_doc_id"));
    	commonMybatisDao.delete("common.file.deleteAttchFiles", param);// 첨부파일 DB 삭제

    	//파일 삭제 처리 완료


		commonMybatisDao.update("admin.cnts.updateMenuCntsEmpty", param); //메뉴 정보를 비운다

		//commonMybatisDao.update("admin.cnts.deleteCntsMgt", param); //삭제 체크 여부 변경

		commonMybatisDao.delete("admin.cnts.deleteCntsMgt", param); //cntnts DB 삭제
		commonMybatisDao.delete("admin.cnts.deleteCntsVerMgt", param); //cntnts_ver DB 삭제
	}


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
	public void insertCntsMgt(DataMap param, List fileList, List attachFileList) throws Exception {

		// 글 IMAGE_DOC_ID 셋팅
		String imageDocId = StringUtil.nvl(param.getString("image_doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", imageDocId );


		// 글 DOC_ID 셋팅
		String docId = StringUtil.nvl(param.getString("doc_id"), SysUtil.getDocId());
		param.put("doc_id", docId );


		// ########### Image File 처리 시작 #############
    	String[] imgPathList = param.getArray("imgPathList");

		String cn = param.getString("cn");
		NtsysFileVO reNtsysFile = null;
		if(!fileList.isEmpty()){

			for(int i=0; i < fileList.size(); i++){
				MultipartFile mfile = (MultipartFile)fileList.get(i);
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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, imageDocId, "cnts/", param.getString("ss_user_no"), "Y");

					cn = cn.replaceAll(imgPathList[i], reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm());

					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		param.put("cn", cn);
		// ########### Image File 처리 종료 ############

		// ########### Upload File 처리 시작 #############
		reNtsysFile = null;
		if(!attachFileList.isEmpty()){

			for(int i=0; i < attachFileList.size(); i++){
				MultipartFile mfile = (MultipartFile)attachFileList.get(i);
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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, docId, "cnts/", param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		// ########### Upload File 처리 종료 ############
		String cntntsSeq = commonMybatisDao.selectOne("admin.cnts.selectCntntsSeq");
		param.put("cntnts_seq", cntntsSeq);
		commonMybatisDao.insert("admin.cnts.insertCntsMgt", param);
		commonMybatisDao.insert("admin.cnts.insertCntsVerMgt", param);
		/*commonMybatisDao.update("admin.cnts.updateMenuCntsCode", param);*/
	}

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
	public List selectOrgan() throws Exception{
		return (List)commonMybatisDao.selectList("admin.cnts.selectOrgan");
	}



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
	public List selectPageListCntsVerMgt(DataMap param) throws Exception{
		return (List)commonMybatisDao.selectList("admin.cnts.selectPageListCntsVerMgt", param);
	}


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
	public void updateCntsMgt(DataMap param, List fileList, List attachFileList) throws Exception {

    	String cn = param.getString("cn");


/*		파일 DB 및 물리파일 삭제 필요 시 주석 해제
 *
		//#### FILE LIST 검색 Start ####
    	NtsysFileVO fvo = new NtsysFileVO();
    	fvo.setDoc_id(param.getString("image_doc_id"));
    	List<NtsysFileVO> deleteFileList = commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
    	//#### FILE LIST 검색 End ####

    	//### 제거된 FILE LIST 처리 Start ###
    	for(int i = deleteFileList.size()-1; i > -1 ; i--){
    		if(cn.contains(deleteFileList.get(i).getFile_id())){
    			deleteFileList.remove(i);
    		}
    	}

    	ntsysFileMngUtil.deleteFile(deleteFileList);	// 물리파일 삭제
    	param.put("doc_id", param.getString("image_doc_id"));

    	for(int i = 0; i < deleteFileList.size(); i++){
    		param.put("file_id", deleteFileList.get(i).getFile_id());


        	commonMybatisDao.delete("common.file.deleteAttchFile", param);// 첨부파일 DB 삭제
    	}
    	//### 제거된 FILE LIST 처리 End ###

    	*/

		// ########### Image File 처리 시작 #############
    	String[] imgPathList = param.getArray("imgPathList");

		// 글 IMAGE_DOC_ID 셋팅
		String imageDocId = StringUtil.nvl(param.getString("image_doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", imageDocId );

		// 글 DOC_ID 셋팅
		String docId = StringUtil.nvl(param.getString("doc_id"), SysUtil.getDocId());
		param.put("doc_id", docId );

		NtsysFileVO reNtsysFile = null;
		if(!fileList.isEmpty()){

			for(int i=0; i < fileList.size(); i++){
				MultipartFile mfile = (MultipartFile)fileList.get(i);
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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, imageDocId, "cnts/", param.getString("ss_user_no"), "Y");

					cn = cn.replaceAll(imgPathList[i], reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm());

					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		param.put("cn", cn);

		// ########### Image File 처리 종료 ############

		// ########### Upload File 처리 시작 #############
		reNtsysFile = null;
		if(!attachFileList.isEmpty()){

			for(int i=0; i < attachFileList.size(); i++){
				MultipartFile mfile = (MultipartFile)attachFileList.get(i);
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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, docId, "cnts/", param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		// ########### Upload File 처리 종료 ############



		commonMybatisDao.update("admin.cnts.updateCntsMgt", param);
		commonMybatisDao.insert("admin.cnts.insertCntsVerMgt", param);
		/*
		commonMybatisDao.update("admin.cnts.updateMenuCntsEmpty", param);
		commonMybatisDao.update("admin.cnts.updateMenuCntsCode", param);
		*/
	}


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
	public int selectTotCntPhotoMng(DataMap param) throws Exception{
		return (Integer) commonMybatisDao.selectOne("admin.cnts.selectTotCntPhotoMng", param);
	}


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

	public List selectPageListPhotoMng(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.cnts.selectPageListPhotoMng", param);
	}
	/**
	 * <PRE>
	 * 1. MethodName : updateHitCnt
	 * 2. ClassName  : CntsMgtService
	 * 3. Comment   : 컨텐츠 히트 카운트 증가
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018.6.26
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */

	public void updateHitCnt(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.update("admin.cnts.updateHitCnt", param);

	}


	/**
	 *
	 * <PRE>
	 * 1. MethodName : cntntIdAjax
	 * 2. ClassName  : CntsMgtServiceImpl
	 * 3. Comment   : contst_id 조회
	 * 4. 작성자    : :LWJ
	 * 5. 작성일    : 2020. 6. 9. 오후 4:49:00
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public DataMap cntntIdAjax(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (DataMap) commonMybatisDao.selectOne("admin.cnts.selectCntntIdSeq", param);
	}


	/**
	 * <PRE>
	 * 1. MethodName : cntntIdChkAjax
	 * 2. ClassName  : CntsMgtServiceImpl
	 * 3. Comment   : 콘텐츠 아이디 중복 확인
	 * 4. 작성자    : 백국현
	 * 5. 작성일    : 2018. 8. 11. 오후 2:06:02
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public int cntntIdChkAjax(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (Integer)commonMybatisDao.selectOne("admin.cnts.selectOrganChk", param);

	}

}
