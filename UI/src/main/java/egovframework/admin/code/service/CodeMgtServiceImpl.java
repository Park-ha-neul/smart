package egovframework.admin.code.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : CodeServiceImpl.java
 * 3. Package  : egovframework.admin.code.service
 * 4. Comment  : 코드관리
 * 5. 작성자   : 김영배
 * 6. 작성일   : 2015. 9. 1. 오후 3:48:22
 * </PRE>
 */
@Service("codeMgtService")
public class CodeMgtServiceImpl extends EgovAbstractServiceImpl implements CodeMgtService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;


    /**
     * <PRE>
     * 1. MethodName : selectTotCntGroupCode
     * 2. ClassName  : CodeServiceImpl
     * 3. Comment   : 그룹코드 카운트
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:48:31
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public int selectTotCntGroupCode(DataMap param) throws Exception {
    	return (Integer)commonMybatisDao.selectOne("admin.code.selectTotCntGroupCode", param);
    }


    /**
     * <PRE>
     * 1. MethodName : selectPageListGroupCode
     * 2. ClassName  : CodeServiceImpl
     * 3. Comment   : 그룹코드 조회
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:48:38
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public List selectPageListGroupCode(DataMap param) throws Exception {
    	return (List) commonMybatisDao.selectList("admin.code.selectPageListGroupCode", param);
    }


    /**
     * <PRE>
     * 1. MethodName : selectListCode
     * 2. ClassName  : CodeServiceImpl
     * 3. Comment   : 상세코드 리스트
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:49:03
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public List selectListCode(DataMap param) throws Exception {
    	return (List) commonMybatisDao.selectList("admin.code.selectListCode", param);
    }


    /**
     * <PRE>
     * 1. MethodName : selectGroupCode
     * 2. ClassName  : CodeServiceImpl
     * 3. Comment   : 그룹코드 상세
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:49:14
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public DataMap selectGroupCode(DataMap param) throws Exception {
    	return (DataMap) commonMybatisDao.selectOne("admin.code.selectGroupCode", param);
    }


    /**
     * <PRE>
     * 1. MethodName : deleteGroupCode
     * 2. ClassName  : CodeServiceImpl
     * 3. Comment   : 그룹코드 및 상세코드 삭제
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:49:30
     * </PRE>
     *   @param param
     *   @throws Exception
     */
    public void deleteGroupCode(DataMap param) throws Exception {
    	List groupCodeList = param.getList("del_grp_code");

    	DataMap tmpMap = new DataMap();
    	for(int i = 0; i < groupCodeList.size(); i++){
    		tmpMap.put("group_id", groupCodeList.get(i));

    		commonMybatisDao.delete("admin.code.deleteGroupCode", tmpMap);
    		commonMybatisDao.delete("admin.code.deleteCode", tmpMap);
    	}
    }


    /**
     * <PRE>
     * 1. MethodName : selectExistYnGroupCode
     * 2. ClassName  : CodeServiceImpl
     * 3. Comment   : 그룹코드 중복 체크
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:49:40
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public DataMap selectExistYnGroupCode(DataMap param) throws Exception {
    	return (DataMap) commonMybatisDao.selectOne("admin.code.selectExistYnGroupCode", param);
    }


    /**
     * <PRE>
     * 1. MethodName : insertGroupCode
     * 2. ClassName  : CodeServiceImpl
     * 3. Comment   : 그룹코드 추가
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:49:49
     * </PRE>
     *   @param param
     *   @throws Exception
     */
    public void insertGroupCode(DataMap param) throws Exception {
    	commonMybatisDao.insert("admin.code.insertGroupCode", param);
    }


    /**
     * <PRE>
     * 1. MethodName : updateCode
     * 2. ClassName  : CodeServiceImpl
     * 3. Comment   : 그룹코드 및 상세코드 수정
     * 4. 작성자    : 김영배
     * 5. 작성일    : 2015. 9. 1. 오후 3:49:56
     * </PRE>
     *   @param param
     *   @throws Exception
     */
    public void updateCode(DataMap param) throws Exception {

    	commonMybatisDao.update("admin.code.updateGroupCode", param);
    	commonMybatisDao.delete("admin.code.deleteCode", param);

    	List codeList = param.getList("code");
    	List codeNmList = param.getList("code_nm");
    	List codeNmEnList = param.getList("code_nm_eng");
    	List attr1List = param.getList("attrb_1");
    	List attr2List = param.getList("attrb_2");
    	List attr3List = param.getList("attrb_3");
    	List srtOrdList = param.getList("sort_ordr");
    	List useYnList = param.getList("use_yn");

    	DataMap tmpMap = new DataMap();
    	tmpMap.put("group_id", param.getString("group_id"));
    	tmpMap.put("group_nm", param.getString("group_nm"));
    	tmpMap.put("ss_user_no", param.getString("ss_user_no"));


    	if(codeList != null){
	    	for(int i = 0; i < codeList.size(); i++){
	    		tmpMap.put("code", codeList.get(i));
	    		tmpMap.put("code_nm", codeNmList.get(i));
	    		tmpMap.put("code_nm_eng", codeNmEnList.get(i));
	    		tmpMap.put("attrb_1", attr1List.get(i));
	    		tmpMap.put("attrb_2", attr2List.get(i));
	    		tmpMap.put("attrb_3", attr3List.get(i));
	    		tmpMap.put("sort_ordr", srtOrdList.get(i));
	    		tmpMap.put("use_yn", useYnList.get(i));

	    		commonMybatisDao.update("admin.code.insertCode", tmpMap);
	    	}
    	}
    }

    /**
     * <PRE>
     * 1. MethodName : selectMaxGroupId
     * 2. ClassName  : CodeServiceImpl
     * 3. Comment   : 그룹아이디 중 최대값 -> 코드 등록할 때 자동 채번
     * 4. 작성자    : 김유리
     * 5. 작성일    : 2015. 9. 1. 오후 3:49:14
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public DataMap selectMaxGroupId(DataMap param) throws Exception {
    	return (DataMap) commonMybatisDao.selectOne("admin.code.selectMaxGroupId", param);
    }


    /**
     * <PRE>
     * 1. MethodName : selectExistYnGroupId
     * 2. ClassName  : CodeMgtServiceImpl
     * 3. Comment   : 그룹ID 중복체크
     * 4. 작성자    : mk
     * 5. 작성일    : 2018. 7. 27. 오전 10:05:23
     * </PRE>
     *   @param param
     *   @return
     *   @throws Exception
     */
    public DataMap selectExistYnGroupId(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.code.selectExistYnGroupId", param);
	}
}
