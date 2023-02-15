package egovframework.framework.common.util.file.service;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 *
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : NtsysFileMngServiceImpl.java
 * 3. Package  : egovframework.com.cmm.service.impl
 * 4. Comment  : 파일정보의 관리를 위한 구현 클래스
 * 5. 작성자   : sshinb
 * 6. 작성일   : 2013. 10. 1. 오전 10:08:43
 * @Modification Information
 *
 *    수정일       		수정자         수정내용
 *    -------        	-------     -------------------
 *    2013. 10. 1.     	sshinb    	최초생성
 *    2015. 12. 08		jjh			mybatis로 변경(공통 mybatis dao사용) > 기존 dao 로직으로 impl단으로 올림
 *
 * </PRE>
 */
@Service("NtsysFileMngService")
public class NtsysFileMngServiceImpl extends EgovAbstractServiceImpl implements NtsysFileMngService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	public static final Logger LOGGER = Logger.getLogger(NtsysFileMngServiceImpl.class.getName());

    /**
     * 하나의 파일에 대한 정보(속성 및 상세)를 등록한다.
     *
     */
    public String insertFileInf(NtsysFileVO fvo) throws Exception
    {
		String docId = fvo.getDocId();
		commonMybatisDao.insert("common.file.insertAttchFile", fvo);

		return docId;
    }

    /**
     * 여러 개의 파일에 대한 정보(속성 및 상세)를 등록한다.
     *
     */
    public String insertFileInfs(List fvoList) throws Exception
    {
		String atchFileId = "";

		if (fvoList.size() != 0) {

			NtsysFileVO vo = (NtsysFileVO)fvoList.get(0);
			atchFileId = vo.getDocId();

			Iterator iter = fvoList.iterator();
			while (iter.hasNext()) {
				vo = (NtsysFileVO)iter.next();
				commonMybatisDao.insert("common.file.insertAttchFile", vo);
			}
		}

		if(atchFileId == ""){
			atchFileId = null;
		}

		return atchFileId;
    }

    /**
     * 파일에 대한 목록을 조회한다.
     *
     * @param fvo : NtsysFileVO
     * @return
     * @throws Exception
     */
    public List<NtsysFileVO> selectFileInfs(NtsysFileVO fvo) throws Exception
    {
    	List<NtsysFileVO> fileInfs= null;
    	if(fvo.getDocId() != null && !fvo.getDocId().equals("")){
    		return commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
    	}

    	return fileInfs;
    }

    /**
     * 하나의 파일을 삭제한다.
     *
     */
    public void deleteFileInf(DataMap param) throws Exception
    {
    	commonMybatisDao.delete("common.file.deleteAttchFile", param);
    }

    /**
     * 여러 개의 파일을 삭제한다.
     *
     */
    public void deleteFileInfs(DataMap param) throws Exception
    {
    	commonMybatisDao.delete("common.file.deleteAttchFiles", param);
    }

    /**
     * 파일에 대한 상세정보를 조회한다.
     *
     */
    public NtsysFileVO selectFileInf(NtsysFileVO fvo) throws Exception
    {
    	return commonMybatisDao.selectOne("common.file.selectAttchFile", fvo);
    }
}
