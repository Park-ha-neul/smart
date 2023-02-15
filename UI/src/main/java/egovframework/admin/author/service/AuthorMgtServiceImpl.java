package egovframework.admin.author.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : AuthorServiceImpl.java
 * 3. Package  : egovframework.admin.author.web.service
 * 4. Comment  : 권한 관리
 * 5. 작성자   : 김영배
 * 6. 작성일   : 2015. 8. 31. 오전 11:23:59
 * </PRE>
 */
@Service("authorMgtService")
public class AuthorMgtServiceImpl extends EgovAbstractServiceImpl implements AuthorMgtService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;


    /**
     * <PRE>
     * 1. MethodName : selectTotCntAuthor
     * 2. ClassName  : AuthorServiceImpl
     * 3. Comment   : 권한 카운트
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:43:36
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public int selectTotCntAuthor(DataMap param) throws Exception {
    	return (Integer) commonMybatisDao.selectOne("admin.author.selectTotCntAuthor", param);
    }


    /**
     * <PRE>
     * 1. MethodName : selectPageListAuthor
     * 2. ClassName  : AuthorServiceImpl
     * 3. Comment   : 권한 목록
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:43:43
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public List selectPageListAuthor(DataMap param) throws Exception {
    	return (List) commonMybatisDao.selectList("admin.author.selectPageListAuthor", param);
    }


    /**
     * <PRE>
     * 1. MethodName : insertAuthor
     * 2. ClassName  : AuthorServiceImpl
     * 3. Comment   : 권한 추가
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:43:52
     * </PRE>
     *   @param param
     *   @throws Exception
     */
    public void insertAuthor(DataMap param) throws Exception {
    	commonMybatisDao.insert("admin.author.insertAuthor", param);
    }


    /**
     * <PRE>
     * 1. MethodName : selectAuthor
     * 2. ClassName  : AuthorServiceImpl
     * 3. Comment   : 권한 상세
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:44:04
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public DataMap selectAuthor(DataMap param) throws Exception {
    	return (DataMap) commonMybatisDao.selectOne("admin.author.selectAuthor", param);
    }

    /**
     * <PRE>
     * 1. MethodName : updateAuthor
     * 2. ClassName  : AuthorServiceImpl
     * 3. Comment   : 권한 수정
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:44:10
     * </PRE>
     *   @param param
     *   @throws Exception
     */
    public void updateAuthor(DataMap param) throws Exception {
    	commonMybatisDao.update("admin.author.updateAuthor", param);
    }


    /**
     * <PRE>
     * 1. MethodName : deleteAuthor
     * 2. ClassName  : AuthorServiceImpl
     * 3. Comment   : 권한 삭제
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:44:15
     * </PRE>
     *   @param param
     *   @throws Exception
     */
    public void deleteAuthor(DataMap param) throws Exception {
    	commonMybatisDao.delete("admin.author.deleteUserAuthor", param);
    	commonMybatisDao.delete("admin.author.deleteAuthorMenu", param);
    	commonMybatisDao.delete("admin.author.deleteAuthor", param);
    }


    /**
     * <PRE>
     * 1. MethodName : selectExistYnAuthor
     * 2. ClassName  : AuthorServiceImpl
     * 3. Comment   : 권한 중복 체크
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:44:21
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public String selectExistYnAuthor(DataMap param) throws Exception {
    	return (String)commonMybatisDao.selectOne("admin.author.selectExistYnAuthor", param);
    }

    /**
     * <PRE>
     * 1. MethodName : selectTotCntUser
     * 2. ClassName  : AuthorMgtServiceImpl
     * 3. Comment   : 권한 사용자 총 카운트
     * 4. 작성자    : Ahn So Young
     * 5. 작성일    : 2020. 6. 10. 오전 11:2:20
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    @Override
    public int selectTotCntUser(DataMap param) throws Exception {
    	return (Integer) commonMybatisDao.selectOne("admin.author.selectTotCntUser", param);
    }

    /**
     * <PRE>
     * 1. MethodName : selectPageListUser
     * 2. ClassName  : AuthorMgtServiceImpl
     * 3. Comment   : 권한 사용자 조회
     * 4. 작성자    : Ahn So Young
     * 5. 작성일    : 2020. 6. 10. 오전 11:2:27
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
	@Override
	public List selectPageListUser(DataMap param) throws Exception {
    	return (List) commonMybatisDao.selectList("admin.author.selectPageListUser", param);
	}

}