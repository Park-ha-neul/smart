package egovframework.framework.common.util.file.dao;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * @Class Name : NtsysFileManageDAO.java
 * @Description : 파일정보 관리를 위한 데이터 처리 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 25.     sshinb    최초생성
 *
 * @author
 * @since 2009. 3. 25.
 * @version
 * @see
 *
 */
/**
 *
 * <PRE>
 * 1. ClassName : NtsysFileManageDAO.java
 * 2. FileName  : NtsysFileManageDAO.java
 * 3. Package  : egovframework.com.cmm.service.impl
 * 4. Comment  : 파일정보 관리를 위한 데이터 처리 클래스
 * 5. 작성자   : sshinb
 * 6. 작성일   : 2013. 9. 30. 오후 7:42:03
 * @Modification Information
 *
 *    수정일       	 수정자   	   	수정내용
 *    -------        -------		-------------------
 *    2013. 9. 30.   sshinb	    	최초생성
 *
 * </PRE>
 */
@Repository("NtsysFileManageDao")
public class NtsysFileManageDao {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	/**
     * 여러 개의 파일에 대한 정보(속성 및 상세)를 등록한다.
     *
     * @param fileList
     * @return
     * @throws Exception
     */
    public String insertFileInfs(List fileList) throws Exception {
    	NtsysFileVO vo = (NtsysFileVO)fileList.get(0);
		String atchDocId = vo.getDocId();

		Iterator iter = fileList.iterator();
		while (iter.hasNext()) {
		    vo = (NtsysFileVO)iter.next();
		    commonMybatisDao.insert("common.file.insertAttchFile", vo);
		}

		return atchDocId;
    }

    /**
     * 하나의 파일에 대한 정보(속성 및 상세)를 등록한다.
     *
     * @param vo
     * @throws Exception
     */
    public void insertFileInf(NtsysFileVO vo) throws Exception {
    	commonMybatisDao.insert("common.file.insertAttchFile", vo);
    }

    /**
     * 하나의 파일을 삭제한다.
     *
     * @param fvo
     * @throws Exception
     */
    public void deleteFileInf(DataMap param) throws Exception {
    	commonMybatisDao.delete("common.file.deleteAttchFile", param);
    }

    /**
     * 여러 개의 파일을 삭제한다.
     *
     * @param fileList
     * @throws Exception
     */
    public void deleteFileInfs(DataMap param) throws Exception {
    	commonMybatisDao.delete("common.file.deleteAttchFiles", param);
    }

    /**
     * 파일에 대한 목록을 조회한다.
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public List<NtsysFileVO> selectFileInfs(NtsysFileVO vo) throws Exception {
	return commonMybatisDao.selectList("common.file.selectAttchFiles", vo);
    }

    /**
     * 파일에 대한 상세정보를 조회한다.
     *
     * @param fvo
     * @return
     * @throws Exception
     */
    public NtsysFileVO selectFileInf(NtsysFileVO fvo) throws Exception {
	return commonMybatisDao.selectOne("common.file.selectAttchFile", fvo);
    }
}
