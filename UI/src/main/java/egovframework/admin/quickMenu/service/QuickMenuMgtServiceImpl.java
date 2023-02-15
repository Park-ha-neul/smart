package egovframework.admin.quickMenu.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.admin.quickMenu.vo.QuickMenuImgFileVo;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("quickMenuMgtService")
public class QuickMenuMgtServiceImpl extends EgovAbstractServiceImpl implements QuickMenuMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectTotCntqQuickMenu
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵 메뉴 총 개수 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:01:39
	 * </PRE>
	 *   @param param
	 *   @return
	 */
	public int selectTotCntQuickMenu(DataMap param) {
		// TODO Auto-generated method stub
		return (Integer) commonMybatisDao.selectOne("admin.quickMenu.selectTotCntQuickMenu", param);
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectPageListQuickMenu
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵메뉴 목록 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:03:13
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListQuickMenu(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (List) commonMybatisDao.selectList("admin.quickMenu.selectPageListQuickMenu", param);
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : insertQuickMenu
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵메뉴 입력
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:03:29
	 * </PRE>
	 *   @param param
	 *   @param filelist_w
	 *   @param filelist_m
	 *   @throws Exception
	 */
	public void insertQuickMenu(DataMap param, List fileList_w, List fileList_m) throws Exception {
		// TODO Auto-generated method stub
		// 글 ID 셋팅
		param.put("web_image_doc_id", SysUtil.getDocId());
		param.put("mobile_image_doc_id", SysUtil.getDocId());

		uploadFile(fileList_w, param.getString("web_image_doc_id"), "quickMenu_w/", param, "1");
		uploadFile(fileList_m, param.getString("mobile_image_doc_id"), "quickMenu_m/", param, "2");

		commonMybatisDao.insert("admin.quickMenu.insertQuickMenu", param);
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectQuickMenu
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵메뉴 상세 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:05:57
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectQuickMenu(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectOne("admin.quickMenu.selectQuickMenu", param);
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : updateQuickMenu
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵메뉴 수정
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:07:51
	 * </PRE>
	 *   @param param
	 *   @param fileList_w
	 *   @param fileList_m
	 *   @throws Exception
	 */
	public void updateQuickMenu(DataMap param, List fileList_w, List fileList_m) throws Exception {
		// TODO Auto-generated method stub
		List updateFileIdList = param.getList("file_id_update");
		List updateSortOrdrList = param.getList("sort_ordr_update");
		List updateUrlList = param.getList("link_url_update");
		List updateAttrb1List = param.getList("attrb_1_update");
		List updateAttrb2List = param.getList("attrb_2_update");
		List updateAttrb3List = param.getList("attrb_3_update");

		if (updateFileIdList != null && updateFileIdList.size() > 0) {
			DataMap tmpMap = new DataMap();
			for (int i = 0; i < updateFileIdList.size(); i++) {
				tmpMap.put("file_id", updateFileIdList.get(i));
				tmpMap.put("sort_ordr", updateSortOrdrList.get(i));
				tmpMap.put("link_url", updateUrlList.get(i));
				tmpMap.put("attrb_1", updateAttrb1List.get(i));
				tmpMap.put("attrb_2", updateAttrb2List.get(i));
				tmpMap.put("attrb_3", updateAttrb3List.get(i));

				commonMybatisDao.update("admin.quickMenu.updateQuickMenuImg", tmpMap);
			}
		}

		String web_image_doc_id = StringUtil.nvl(param.getString("web_image_doc_id"), SysUtil.getDocId());
		param.put("web_image_doc_id", web_image_doc_id);

		String mobile_image_doc_id = StringUtil.nvl(param.getString("mobile_image_doc_id"), SysUtil.getDocId());
		param.put("mobile_image_doc_id", mobile_image_doc_id);

		uploadFile(fileList_w, param.getString("web_image_doc_id"), "quickMenu_w/", param, "1");
		uploadFile(fileList_m, param.getString("mobile_image_doc_id"), "quickMenu_m/", param, "2");

		commonMybatisDao.update("admin.quickMenu.updateQuickMenu", param);
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : deleteQuickMenu
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵메뉴 삭제
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:08:23
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteQuickMenu(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		deleteFile(param.getString("web_image_doc_id"));
		deleteFile(param.getString("mobile_image_doc_id"));

		commonMybatisDao.delete("admin.quickMenu.deleteQuickMenu", param);
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectQuickMenuImgs
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵메뉴 이미지 리스트 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:09:03
	 * </PRE>
	 *   @param ifvo
	 *   @return
	 *   @throws Exception
	 */
	public List selectQuickMenuImgs(QuickMenuImgFileVo ifvo) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectList("admin.quickMenu.selectQuickMenuImgs", ifvo);
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : selectQuickMenuImg
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵메뉴 이미지 정보 조회
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:10:02
	 * </PRE>
	 *   @param fvo
	 *   @return
	 *   @throws Exception
	 */
	public NtsysFileVO selectQuickMenuImg(NtsysFileVO fvo) throws Exception {
		// TODO Auto-generated method stub
		return (NtsysFileVO) commonMybatisDao.selectOne("admin.quickMenu.selectQuickMenuImg", fvo);
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : deleteQuickMenuImg
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵메뉴 이미지 정보 삭제
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:10:32
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteQuickMenuImg(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		commonMybatisDao.delete("admin.quickMenu.deleteQuickMenuImg", param);
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : uploadFile
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵메뉴 이미지 업로드
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:11:45
	 * </PRE>
	 *   @return void
	 *   @param fileList
	 *   @param doc_id
	 *   @param dir
	 *   @param param
	 *   @param uploadZoneNo
	 *   @throws Exception
	 */
	protected void uploadFile(List fileList, String doc_id, String dir, DataMap param, String uploadZoneNo) throws Exception{
		List sortOrdrList = param.getList("sort_ordr_" + uploadZoneNo);
		List linkUrl = param.getList("link_url_" + uploadZoneNo);
		List attrb1 = param.getList("attrb_1_" + uploadZoneNo);
		List attrb2 = param.getList("attrb_2_" + uploadZoneNo);
		List attrb3 = param.getList("attrb_3_" + uploadZoneNo);

		// ########### Upload File 처리 시작 #############
		NtsysFileVO _reNtsysFile = null;
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
					_reNtsysFile = ntsysFileMngUtil.parseFileInf(mfile, doc_id, dir, param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("admin.quickMenu.insertQuickMenuImg", _reNtsysFile);

					// 배너이미지 정보 입력
					tmpMap.put("sort_ordr", sortOrdrList.get(i));
					tmpMap.put("link_url", linkUrl.get(i));
					tmpMap.put("attrb_1", attrb1.get(i));
					tmpMap.put("attrb_2", attrb2.get(i));
					tmpMap.put("attrb_3", attrb3.get(i));
					tmpMap.put("file_id", _reNtsysFile.getFileId());
					commonMybatisDao.update("admin.quickMenu.updateQuickMenuImg", tmpMap);
				}
			}
		}
		// ########### Upload File 처리 종료 ############
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : deleteFile
	 * 2. ClassName  : QuickMenuMgtServiceImpl
	 * 3. Comment   : 퀵메뉴 이미지 삭제
	 * 4. 작성자    : Lee WooJin
	 * 5. 작성일    : 2020. 5. 8. 오후 5:13:12
	 * </PRE>
	 *   @return void
	 *   @param doc_id
	 *   @throws Exception
	 */
	protected void deleteFile(String doc_id) throws Exception{
		QuickMenuImgFileVo ifvo = new QuickMenuImgFileVo();
		ifvo.setDocId(doc_id);
		List<NtsysFileVO> fileList = commonMybatisDao.selectList("admin.quickMenu.selectQuickMenuImgs", ifvo);

		ntsysFileMngUtil.deleteFile(fileList); // 물리파일 삭제

		DataMap param = new DataMap();
		param.put("doc_id", doc_id);
		commonMybatisDao.delete("admin.quickMenu.deleteQuickMenuImgs", param);// 파일정보 DB 삭제
	}

}

