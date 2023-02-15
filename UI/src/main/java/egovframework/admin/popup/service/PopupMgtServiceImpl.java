package egovframework.admin.popup.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : PopupMgtServiceImpl.java
 * 3. Package  : egovframework.admin.popup.service
 * 4. Comment  : 팝업관리 관리
 * 5. 작성자   : 박재현
 * 6. 작성일   : 2015. 9. 7. 오전 9:51:39
 * </PRE>
 */
@Service("popupMgtService")
public class PopupMgtServiceImpl extends EgovAbstractServiceImpl implements PopupMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;


	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntPopupMgt
	 * 2. ClassName  : PopupMgtServiceImpl
	 * 3. Comment   : 팝업관리 총개수 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 9:52:14
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntPopupMgt(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.popup.selectTotCntPopupMgt", param);
	}



	/**
	 * <PRE>
	 * 1. MethodName : selectPageListPopupMgt
	 * 2. ClassName  : PopupMgtServiceImpl
	 * 3. Comment   : 팝업관리 페이지 리스트 조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 9:52:16
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListPopupMgt(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.popup.selectPageListPopupMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPopupMgt
	 * 2. ClassName  : PopupMgtServiceImpl
	 * 3. Comment   : 팝업 상세조회
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 10:14:57
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectPopupMgt(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.popup.selectPopupMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deletePopupMgt
	 * 2. ClassName  : PopupMgtServiceImpl
	 * 3. Comment   : 팝업 삭제
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 7. 오전 11:33:21
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deletePopupMgt(DataMap param) throws Exception {


		//#### FILE LIST 검색 Start ####
    	NtsysFileVO fvo = new NtsysFileVO();
    	fvo.setDocId(param.getString("image_doc_id"));
    	List<NtsysFileVO> fileList = commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
    	//#### FILE LIST 검색 End ####

    	ntsysFileMngUtil.deleteFile(fileList);	// 물리파일 삭제
    	param.put("doc_id", param.getString("image_doc_id"));
    	commonMybatisDao.delete("common.file.deleteAttchFiles", param);// 첨부파일 DB 삭제

    	commonMybatisDao.delete("admin.popup.deletePopupMgt", param);
	}


	/**
	 * <PRE>
	 * 1. MethodName : insertPopupMgt
	 * 2. ClassName  : PopupMgtServiceImpl
	 * 3. Comment   : 팝업 등록
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 11. 오후 4:52:33
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	public void insertPopupMgt(DataMap param, List fileList) throws Exception {

		// 글 ID 셋팅
		String docId = StringUtil.nvl(param.getString("image_doc_id"), SysUtil.getDocId());
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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, docId, "popup/", param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		// ########### Upload File 처리 종료 ############
//		param.put("cn", param.getStringOrgn("cn"));
		commonMybatisDao.insert("admin.popup.insertPopupMgt", param);
	}


	/**
	 * <PRE>
	 * 1. MethodName : updatePopupMgt
	 * 2. ClassName  : PopupMgtServiceImpl
	 * 3. Comment   : 팝업 수정
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2015. 9. 11. 오후 4:54:54
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @throws Exception
	 */
	public void updatePopupMgt(DataMap param, List fileList) throws Exception {

		// 글 ID 셋팅
		String docId = StringUtil.nvl(param.getString("image_doc_id"), SysUtil.getDocId());
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
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, param.getString("image_doc_id"), "popup/", param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
//		param.put("cn", param.getStringOrgn("cn"));
		// ########### Upload File 처리 종료 ############
		commonMybatisDao.update("admin.popup.updatePopupMgt", param);
	}
}
