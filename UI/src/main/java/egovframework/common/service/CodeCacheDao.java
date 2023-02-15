package egovframework.common.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import egovframework.framework.common.dao.CommonMybatisDao;

@Repository("codeCacheDao ")
public class CodeCacheDao {
	/** commonDao */
	@Resource(name = "commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	/**
	 * <PRE>
	 * 1. MethodName : selectListCode
	 * 2. ClassName  : CodeCacheDao
	 * 3. Comment   : 공통코드상세 List 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 21. 오후 2:42:43
	 * </PRE>
	 *
	 * @return ArrayList
	 * @return
	 */
	public List<?> selectListCode() {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectList("common.selectListCode");
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListCodeGroupId
	 * 2. ClassName  : CodeCacheDao
	 * 3. Comment   : 공통코드 List 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 22. 오후 12:50:50
	 * </PRE>
	 *   @return ArrayList<DataMap>
	 *   @return
	 */
	public List<?> selectListCodeGroupId() {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectList("common.selectListCodeGroupId");
	}

}
