package egovframework.admin.bbsmng.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : BbsMngServiceImpl.java
 * 3. Package  : egovframework.admin.bbsmng.service
 * 4. Comment  :
 * 5. 작성자   : mk
 * 6. 작성일   : 2018. 7. 16. 오후 2:33:09
 * </PRE>
 */
@Service("bbsMngService")
public class BbsMngServiceImpl extends EgovAbstractServiceImpl implements BbsMngService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntBbsMng
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판 관리 총 개수
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 16. 오후 2:32:32
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntBbsMng(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.bbsMng.selectTotCntBbsMng", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListBbsMng
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판 관리 목록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 16. 오후 2:33:26
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListBbsMng(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.bbsMng.selectPageListBbsMng", param);
	}


	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMng
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판 관리 상세
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 16. 오후 2:33:35
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectBbsMng(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.bbsMng.selectBbsMng", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertBbsMng
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판 관리 등록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 16. 오후 2:33:40
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void insertBbsMng(DataMap param) throws Exception {
		// 게시판 기본정보 등록
		commonMybatisDao.insert("admin.bbsMng.insertBbsMng", param);

		// 게시판 권한 정보 등록
		List authList = param.getList("author_id");

		for(int i = 0; i < authList.size(); i++){
			DataMap item = new DataMap();
			// 게시판코드 셋팅
			item.put("bbs_code", param.getString("bbs_code"));
			// 권한 아이디 셋팅
			item.put("author_id", (String)authList.get(i));

			// 게시판별 권한 등록
			commonMybatisDao.insert("admin.bbsMng.insertBbsMngAuthor", item);
		}
	}


	/**
	 * <PRE>
	 * 1. MethodName : updateBbsMng
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판 관리 수정
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 16. 오후 2:33:46
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void updateBbsMng(DataMap param) throws Exception {
		commonMybatisDao.update("admin.bbsMng.updateBbsMng", param);

		// 기존 게시판 권한 삭제
		commonMybatisDao.delete("admin.bbsMng.deleteBbsMngAuthor", param);

		// 게시판 권한 정보 등록
		List authList = param.getList("author_id");

		for(int i = 0; i < authList.size(); i++){
			DataMap item = new DataMap();
			// 게시판코드 셋팅
			item.put("bbs_code", param.getString("bbs_code"));
			// 권한 아이디 셋팅
			item.put("author_id", (String)authList.get(i));

			// 게시판별 권한 등록
			commonMybatisDao.insert("admin.bbsMng.insertBbsMngAuthor", item);
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteBbsMng
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판 관리 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 16. 오후 2:33:51
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteBbsMng(DataMap param) throws Exception {
		commonMybatisDao.delete("admin.bbsMng.deleteBbsMng", param);
		commonMybatisDao.delete("admin.bbsMng.deleteBbsMngAuthor", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectExistYnBbsCode
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판 관리 bbs_code 중복체크
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 16. 오후 2:33:57
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectExistYnBbsCode(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.bbsMng.selectExistYnBbsCode", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntBbsCode
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판 코드에 맞는 게시물 리스트 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 23. 오후 5:25:51
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntBbsCode(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.bbsMng.selectTotCntBbsCode", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsCodeSeq
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판코드 seq조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 26. 오전 10:04:08
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectBbsCodeSeq(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.bbsMng.selectBbsCodeSeq", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListMenuPop
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 메뉴 리스트 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 24. 오후 3:53:13
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListMenuPop(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.bbsMng.selectPageListMenuPop", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateMenuBbsCode
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 메뉴의 bbs_code, cntnts_id, url_ty_code 수정
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 24. 오후 3:53:20
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void updateMenuBbsCode(DataMap param) throws Exception {
		commonMybatisDao.update("admin.bbsMng.updateMenuBbsCode", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectBbsMngInfo
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판 상세 정보 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 24. 오후 3:53:33
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectBbsMngInfo(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.bbsMng.selectBbsMngInfo", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectCntntsInfo
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 컨텐츠 상세 정보 조회
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 24. 오후 3:53:47
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectCntntsInfo(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.bbsMng.selectCntntsInfo", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteMenuBbsCode
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 메뉴의 bbs_code, cntnts_id, url_ty_code 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 24. 오후 3:53:55
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteMenuBbsCode(DataMap param) throws Exception {
		commonMybatisDao.update("admin.bbsMng.deleteMenuBbsCode", param);
	}


	/**
	 * <PRE>
	 * 1. MethodName : selectListAuthor
	 * 2. ClassName  : BbsMngServiceImpl
	 * 3. Comment   : 게시판 권한 리스트 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 15. 오후 7:28:01
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public List selectListAuthor(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectList("admin.bbsMng.selectListAuthor", param);
	}

}
