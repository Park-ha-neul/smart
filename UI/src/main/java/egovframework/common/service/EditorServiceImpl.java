package egovframework.common.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("editorService")
public class EditorServiceImpl extends EgovAbstractServiceImpl implements EditorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EditorServiceImpl.class);

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	/**
	 * <PRE>
	 * 1. MethodName : insertEditorFile
	 * 2. ClassName  : EditorServiceImpl
	 * 3. Comment   : 에디터내 이미지 추가
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2016. 6. 9. 오후 2:31:11
	 * </PRE>
	 *   @param param
	 *   @param fileList
	 *   @throws Exception
	 */
	public void insertEditorFile(DataMap param, List fileList) throws Exception {
		// ########### Upload File 처리 시작 #############
		NtsysFileVO reNtsysFile = null;
		if(!fileList.isEmpty()){

			for(int i=0; i < fileList.size(); i++){
				MultipartFile mfile = (MultipartFile)fileList.get(i);
				if(!mfile.isEmpty()){
					// 파일을 서버에 물리적으로 저장하고
					// 내용 doc_id를 doc_id로 저장을 한다.
					reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, param.getString("cn_doc_id"), "editor/data/", param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
				}
			}
		}
		// ########### Upload File 처리 종료 ############
		// 파일 정보 담기
		param.put("fileInfo", reNtsysFile);
	}
}
