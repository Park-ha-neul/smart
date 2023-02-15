package egovframework.admin.recsroom.service;

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
 * 2. FileName  : recsroomMgtService.java
 * 3. Package  : egovframework.admin.recsroom.service
 * 4. Comment  :  자료실
 * 5. 작성자   : Kim sung min
 * 6. 작성일   : 2021.5.03
 * </PRE>
 */
@Service("recsroomMgtService")
public class RecsroomMgtServiceImpl extends EgovAbstractServiceImpl implements RecsroomMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;


	public void insertRecsroomMgt(DataMap param, List fileList,List attachFileList) throws Exception {

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
				reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, imageDocId, "recsroom/", param.getString("ss_user_no"), "Y");

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
				reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, docId, "recsroom/", param.getString("ss_user_no"), "Y");
				// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
				commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
					}
				}
			}
		// ########### Upload File 처리 종료 ############
		String recsroomSeq = commonMybatisDao.selectOne("admin.recsroom.selectRecsroomSeq");
		param.put("recsroom_seq", recsroomSeq);
		commonMybatisDao.insert("admin.recsroom.insertRecsroomMgt", param);

	}

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
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListRecsroomMgt(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.recsroom.selectPageListRecsroomMgt", param);
	}

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
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotRecsroomMgt (DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.recsroom.selectTotRecsroomMgt", param);
	}

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
	public DataMap selectRecsroomMgt (HttpServletRequest request, HttpServletResponse response, DataMap param) throws Exception {

		/*// 쿠키 체크로 히트 카운트 중복 방지 처리
				if (CookieUtil.getCookieValue(request, "selectCnts" + param.getString("cntnts_seq")).equals("")) {
					//commonMybatisDao.update("admin.cnts.updateHitCnt", param);
					CookieUtil.addCookie(request, response, 60 * 60, "selectCnts" + param.getString("recsroom_seq"),
							param.getString("recsroom_seq"));
				}*/
		return (DataMap) commonMybatisDao.selectOne("admin.recsroom.selectRecsroomMgt", param);
	}

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
	public void deleteRecsroomMgt(DataMap param) throws Exception {

		//#### FILE LIST 검색 Start #### - 첨부 파일
    	NtsysFileVO fvo = new NtsysFileVO();
    	fvo.setDocId(param.getString("doc_id"));
    	List<NtsysFileVO> fileList = commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
    	//#### FILE LIST 검색 End ####

    	ntsysFileMngUtil.deleteFile(fileList); //물리파일 삭제
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

    	commonMybatisDao.delete("admin.recsroom.deleteRecsroomMgt", param); //recsroom DB 삭제
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateRecsroomMgt
	 * 2. ClassName  : RecsroomMgtService
	 * 3. Comment   : 자료실 수정
	 * 4. 작성자    : 김성민
	 * 5. 작성일    : 2021.5.10
	 * </PRE>
	 *   @return void
	 *   @param param , fileList , attachfileList
	 *   @throws Exception
	 */

	public void updateRecsroomMgt(DataMap param,List fileList , List attachFileList) throws Exception {

		String cn = param.getString("cn");

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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, imageDocId, "recsroom/", param.getString("ss_user_no"), "Y");

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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, docId, "recsroom/", param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		// ########### Upload File 처리 종료 ############

		commonMybatisDao.update("admin.recsroom.updateRecsroomMgt", param);

	}

}
