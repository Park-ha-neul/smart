
package egovframework.admin.authmenu.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : AuthMenuMgtServiceImpl.java
 * 3. Package  : egovframework.admin.authmenu.service
 * 4. Comment  : 권한 별 메뉴 관리
 * 5. 작성자   : JJH
 * 6. 작성일   : 2015. 12. 7. 오후 4:04:51
 * 7. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    JJH : 2015. 12. 7. :            : 신규 개발.
 * </PRE>
 */
@Service("authMenuMgtService")

public class AuthMenuMgtServiceImpl extends EgovAbstractServiceImpl implements AuthMenuMgtService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

    /**
     * <PRE>
     * 1. MethodName : selectTotCntAuthMenu
     * 2. ClassName  : AuthMenuServiceImpl
     * 3. Comment   : 권한 별 메뉴 총 개수
     * 4. 작성자    : venus
     * 5. 작성일    : 2013. 8. 29. 오후 2:29:36
     * </PRE>
     *   @return int
     *   @param param
     *   @return
     *   @throws Exception
     */
    public int selectTotCntAuthMenu(DataMap param) throws Exception {
    	return (Integer) commonMybatisDao.selectOne("admin.authmenu.selectTotCntAuthMenu", param);
    }

    /**
     * <PRE>
     * 1. MethodName : selectPageListAuthMenu
     * 2. ClassName  : AuthMenuServiceImpl
     * 3. Comment   : 권한 별 메뉴 페이지 리스트 조회
     * 4. 작성자    : venus
     * 5. 작성일    : 2013. 8. 29. 오후 2:15:28
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public List selectPageListAuthMenu(DataMap param) throws Exception {
    	return (List) commonMybatisDao.selectList("admin.authmenu.selectPageListAuthMenu", param);
    }

    /**
     * <PRE>
     * 1. MethodName : selectListAuthMenu
     * 2. ClassName  : AuthMenuServiceImpl
     * 3. Comment   : 권한 별 메뉴  리스트  조회
     * 4. 작성자    : venus
     * 5. 작성일    : 2013. 8. 29. 오후 2:15:28
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public List selectListAuthMenu(DataMap param) throws Exception {
    	return (List) commonMybatisDao.selectList("admin.authmenu.selectListAuthMenu", param);
    }

    /**
     * <PRE>
     * 1. MethodName : insertAuthMenu
     * 2. ClassName  : AuthMenuServiceImpl
     * 3. Comment   : 권한 별 메뉴 등록
     * 4. 작성자    : venus
     * 5. 작성일    : 2013. 8. 29. 오후 2:19:26
     * </PRE>
     *   @param param
     *   @throws Exception
     */
    public void insertAuthMenu(DataMap param) throws Exception {

    	commonMybatisDao.delete("admin.authmenu.deleteAuthMenu", param);
    	List menuIdList = param.getList("menu_pk");
    	DataMap tmpMap = new DataMap();
		if (menuIdList != null) {
			for (int i = 0; i < menuIdList.size(); i++) {
	    		tmpMap.put("menu_pk", menuIdList.get(i));
	    		tmpMap.put("author_id", param.getString("author_id"));
	    		tmpMap.put("ss_user_id", param.getString("ss_user_id"));
	    		commonMybatisDao.insert("admin.authmenu.insertAuthMenu", tmpMap);
	    	}
    	}
    }


    public DataMap selectInfoAuthMenu(DataMap param) throws Exception {
    	return (DataMap) commonMybatisDao.selectOne("admin.authmenu.selectInfoAuthMenu", param);
    }

}
